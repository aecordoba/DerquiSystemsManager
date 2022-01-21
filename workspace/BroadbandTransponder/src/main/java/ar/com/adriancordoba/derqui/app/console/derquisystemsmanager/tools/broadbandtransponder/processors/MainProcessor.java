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
 *  Adrián E. Córdoba [software.asia@gmail.com]		Oct 1, 2019
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.broadbandtransponder.processors;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.configuration.Configuration;
import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.app.console.commonservices.excel97.ExcelFileScanner;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.broadbandtransponder.subscribers.Subscriber;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.broadbandtransponder.wiring.Broadband;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.broadbandtransponder.wiring.Wiring;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class MainProcessor {
	private static CompositeConfiguration configuration;
	private DatabaseConnectionsManager databaseConnectionsManager;
	private static final Logger logger = LogManager.getLogger(MainProcessor.class);

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
	}

	public boolean process() {
		boolean result = true;
		List<List<String>> backupADSL = new ArrayList<>();
		List<Wiring> wiringsList = new ArrayList<>();

		try {
			ExcelFileScanner backupADSLScanner = new ExcelFileScanner();
			backupADSL = backupADSLScanner.getExcelContent();
		} catch (Exception e) {
			result = false;
			logger.fatal("Cannot scan excel file.", e);
		}

		for (List<String> row : backupADSL) {
			if (row.size() > 11 && !row.get(2).isEmpty() && row.get(11).isEmpty()) {
				try {
					Wiring wiring = new Wiring();

					String subscriberString = row.get(2);
					Subscriber subscriber = new Subscriber();
					subscriber.setOfficeCode(Integer.parseInt(subscriberString.substring(0, 3)));
					subscriber.setMcdu(subscriberString.substring(3, 7).toCharArray());
					wiring.setSubscriber(subscriber);
					
					wiring.setCablePair(row.get(3));

					String dslamString = row.get(0);
					if (dslamString.equals("VRO1"))
						dslamString = "VRS1";
					else if (dslamString.startsWith("VR"))
						dslamString = "VRS3" + dslamString.substring(dslamString.indexOf('-'));
					else if (dslamString.equals("Spdale"))
						dslamString = "SPD";
					Broadband broadband = new Broadband();
					if (dslamString.contains("-")) {
						broadband.setDslam("D-" + dslamString.substring(0, dslamString.indexOf('-')));
						broadband.setSlot(Integer.parseInt(dslamString.substring(dslamString.indexOf('-') + 1, dslamString.length())));
					} else if (dslamString.contains("_")) {
						broadband.setDslam("D-" + dslamString.substring(0, dslamString.indexOf('_')));
						broadband.setSlot(Integer.parseInt(dslamString.substring(dslamString.indexOf('_') + 1, dslamString.length())));
					} else {
						broadband.setDslam("D-" + dslamString);
					}
					String portString = row.get(1);
					broadband.setPort(Integer.parseInt(portString.substring(0, portString.indexOf('.'))));
					broadband.setUsername(row.get(5));
					broadband.setPassword(row.get(6));
					wiring.setBroadband(broadband);

					wiringsList.add(wiring);
				} catch (Exception e) {
					StringBuilder content = new StringBuilder();
					for (String cell : row) {
						content.append(cell);
						content.append("::");
					}
					logger.warn("Problem to extract register: " + content.toString(), e);
					result = false;
				}
			}
		}

		Connection connection = databaseConnectionsManager.takeConnection();
		for (Wiring wiring : wiringsList) {

			try {
				wiring.getBroadband().setBroadbandPortId(connection);
				wiring.setWiringData(connection);
				if(wiring.getWiringId() > 0 && wiring.getBroadband().getBroadbandPortId() > 0) {
					wiring.update(connection);
				} else {
					logger.warn("Cannot find data for " + wiring.getSubscriber() + " cable/pair: " + wiring.getCablePair() + ".");
					result = false;
				}
				
			} catch (Exception e) {
				logger.warn("Cannot complete data for " + wiring.getSubscriber());
				result = false;
			}

		}
//		writer.close();
		databaseConnectionsManager.returnConnection(connection);

		return result;
	}
}
