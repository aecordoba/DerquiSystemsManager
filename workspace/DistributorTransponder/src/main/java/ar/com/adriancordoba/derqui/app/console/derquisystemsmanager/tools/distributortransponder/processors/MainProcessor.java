/*
 * 		MainProcessor.java
 *   Copyright (C) 2019  Adrián E. Córdoba [software.asia@gmail.com]
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * 		MainProcessor.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Aug 29, 2019
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.distributortransponder.processors;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.configuration.Configuration;
import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.app.console.switching.subscribers.Subscriber;
import ar.com.adriancordoba.app.console.switching.subscribers.discoverers.SubscribersScanner;
import ar.com.adriancordoba.app.console.switching.subscribers.equipments.ELineEquipment;
import ar.com.adriancordoba.app.console.switching.subscribers.equipments.Equipment;
import ar.com.adriancordoba.app.console.switching.subscribers.equipments.SigmaELUEquipment;
import ar.com.adriancordoba.app.console.switching.subscribers.equipments.SigmaLineEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.distributortransponder.model.OfficeCodeExtractor;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class MainProcessor {
	private static CompositeConfiguration configuration;
	private DatabaseConnectionsManager databaseConnectionsManager;
	private SubscribersScanner subscribersScanner;
	private OfficeCodeExtractor officeCodeExtractor;
	private PairsProcessor pairsProcessor;
	private static final Logger logger = LogManager.getLogger(MainProcessor.class);

	/**
	 * @throws Exception
	 * 
	 */
	public MainProcessor() throws Exception {
		// Add database user to configuration.
		try {
			configuration = Configuration.getInstance("settings.xml");
			configuration.addProperty("DBUser", "smapp");
			configuration.addProperty("DBPassword", "smapp123");

		} catch (ConfigurationException ex) {
			logger.fatal("Cannot load configuration file.", ex);
			throw new Exception("Fatal error.");
		}
		// Get the database connections manager.
		databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
		// Read subscribers data files into a list of subscribers objects.
		subscribersScanner = new SubscribersScanner();
		// Get own offices codes from database.
		Connection connection = databaseConnectionsManager.takeConnection();
		officeCodeExtractor = new OfficeCodeExtractor(connection);
		databaseConnectionsManager.returnConnection(connection);
		// Read pairs Excel file.
		pairsProcessor = new PairsProcessor();
	}

	public boolean process() {
		boolean result = true;
		Connection connection = databaseConnectionsManager.takeConnection();
		List<Subscriber> subscribersList;
		String technology = null;
		int[] identifiers = { 0, 0 }; // [equipmentId, distributorId]
		try {

			// Iteration on subscribers list (subscribers from subscribers data files).
			subscribersList = subscribersScanner.getSubscribersList();
			for (Subscriber subscriber : subscribersList) {
				// Get equipmentId and distributorId from database.
				Equipment equipment = subscriber.getEquipment();
				if (equipment instanceof ELineEquipment) {
					technology = "NEAX61E";
					identifiers = getNeax61EEquipmentIdentifiers(connection, equipment);
				} else if (equipment instanceof SigmaLineEquipment) {
					technology = "NEAX61SIGMA";
					identifiers = getNeax61SigmaEquipmentIdentifiers(connection, equipment);
				} else if (equipment instanceof SigmaELUEquipment) {
					technology = "NEAX61SIGMA_ELU";
					identifiers = getNeax61SigmaELUEquipmentIdentifiers(connection, equipment);
				}

				if (identifiers[0] != 0) {
					// Insert subscriber (assignment).
					insertSubscriber(connection, subscriber, technology, identifiers[0]);

					// Get pairId from Excel file and database.
					int pairId = pairsProcessor.getPairId(subscriber, connection);
					if (pairId > 0) {
						// Insert wiring.
						insertWiring(connection, identifiers[1], pairId);
					} else if (pairId == 0){
						logger.warn("Could not found pair ID for " + subscriber.getPhoneNumber() + " from database.");
						result = false;
					}
				} else {
					logger.warn("Could not found equipment ID for " + subscriber.getPhoneNumber() + " (" + equipment + ") from database.");
					result = false;
				}
			}

		} catch (Exception e) {
			logger.error("Cannot process subscribers.", e);
		} finally {
			databaseConnectionsManager.returnConnection(connection);
			if (!databaseConnectionsManager.closeConnections())
				logger.error("Some connections could not be closed.");
		}
		return result;
	}

	private int[] getNeax61EEquipmentIdentifiers(Connection connection, Equipment equipment) throws Exception {
		int[] identifiers = { 0, 0 };
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getNeax61EIdentifiers(?, ?, ?, ?, ?, ?)}");
			statement.setString(1, (String.valueOf(((ELineEquipment) equipment).getSpce())));
			statement.setString(2, (String.valueOf(((ELineEquipment) equipment).getHighway())));
			statement.setString(3, (String.valueOf(((ELineEquipment) equipment).getSubhighway())));
			statement.setString(4, (String.valueOf(((ELineEquipment) equipment).getGroup())));
			statement.setString(5, (String.valueOf(((ELineEquipment) equipment).getSw())));
			statement.setString(6, (String.valueOf(((ELineEquipment) equipment).getLevel())));
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				identifiers[0] = resultSet.getInt("neax61eId");
				identifiers[1] = resultSet.getInt("distributorId");
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getNeax61EIdentifiers() for " + equipment + ".", e);
			throw new Exception("Cannot get identifiers from database.");
		} finally {
			statement.close();
		}
		return identifiers;
	}

	private int[] getNeax61SigmaEquipmentIdentifiers(Connection connection, Equipment equipment) throws Exception {
		int[] identifiers = { 0, 0 };
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getNeax61SigmaIdentifiers(?, ?, ?, ?, ?)}");
			statement.setString(1, (String.valueOf(((SigmaLineEquipment) equipment).getTimeSwitch())));
			statement.setString(2, (String.valueOf(((SigmaLineEquipment) equipment).getkHighway())));
			statement.setString(3, (String.valueOf(((SigmaLineEquipment) equipment).getpHighway())));
			statement.setString(4, (String.valueOf(((SigmaLineEquipment) equipment).getRow())));
			statement.setString(5, (String.valueOf(((SigmaLineEquipment) equipment).getColumn())));
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				identifiers[0] = resultSet.getInt("neax61SigmaId");
				identifiers[1] = resultSet.getInt("distributorId");
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getNeax61SigmaIdentifiers() for " + equipment + ".", e);
			throw new Exception("Cannot get identifiers from database.");
		} finally {
			statement.close();
		}
		return identifiers;
	}

	private int[] getNeax61SigmaELUEquipmentIdentifiers(Connection connection, Equipment equipment) throws Exception {
		int[] identifiers = { 0, 0 };
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getNeax61SigmaELUIdentifiers(?, ?)}");
			statement.setString(1, ((SigmaELUEquipment) equipment).getAnw());
			statement.setInt(2, (Integer.parseInt(String.valueOf(((SigmaELUEquipment) equipment).getL3addr()))));
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				identifiers[0] = resultSet.getInt("sigmal3addrId");
				identifiers[1] = resultSet.getInt("distributorId");
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getNeax61SigmaELUIdentifiers() for " + equipment + ".", e);
			throw new Exception("Cannot get identifiers from database.");
		} finally {
			statement.close();
		}
		return identifiers;
	}

	private void insertSubscriber(Connection connection, Subscriber subscriber, String technology, int equipmentId) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call insertSubscriber(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			statement.setInt(1, officeCodeExtractor.getOfficeCodeId(subscriber.getOfficeCode().getCode()));
			statement.setString(2, String.valueOf(subscriber.getNumber()));
			statement.setString(3, technology);
			statement.setInt(4, equipmentId);
			statement.setString(5, "OWNER");
			statement.setInt(6, 0);
			statement.setString(7, null);
			statement.setString(8, null);
			statement.setString(9, null);
			statement.setInt(10, 0);
			statement.setInt(11, 0);
			statement.setInt(12, 0);
			statement.setInt(13, 0);
			statement.setString(14, null);
			statement.setString(15, null);
			statement.setString(16, null);
			statement.setInt(17, 0);
			statement.setInt(18, 0);
			statement.setString(19, null);
			statement.setInt(20, 0);
			statement.setString(21, null);
			statement.setString(22, "INDIVIDUAL");
			statement.setString(23, "1");
			statement.setString(24, "01");
			statement.setString(25, "DISABLED");
			statement.setInt(26, 1);

			statement.execute();
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure insertSubscriber() for " + subscriber.getPhoneNumber() + ".", e);
			throw new Exception("Cannot insert subscriber in database.");
		} finally {
			statement.close();
		}
	}

	private void insertWiring(Connection connection, int distributorId, int pairId) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call insertWiring(?, ?, ?, ?, ?, ?, ?, ?)}");

			statement.setInt(1, distributorId);
			statement.setInt(2, pairId);
			statement.setInt(3, 0);
			statement.setString(4, null);
			statement.setString(5, null);
			statement.setInt(6, 0);
			statement.setString(7, null);
			statement.setInt(8, 1);

			statement.execute();
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure insertWiring().", e);
			throw new Exception("Cannot insert wiring in database.");
		} finally {
			statement.close();
		}
	}
}
