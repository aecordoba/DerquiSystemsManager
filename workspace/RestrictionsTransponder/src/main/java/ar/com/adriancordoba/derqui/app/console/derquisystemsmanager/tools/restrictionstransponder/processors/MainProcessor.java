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
 *  Adrián E. Córdoba [software.asia@gmail.com]		Nov 6, 2019
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.restrictionstransponder.processors;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.configuration.Configuration;
import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.restrictionstransponder.processors.model.RestrictionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.restrictionstransponder.processors.model.RestrictionsProcessor;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.restrictionstransponder.processors.model.Subscriber;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class MainProcessor {
	private static CompositeConfiguration configuration;
	private DatabaseConnectionsManager databaseConnectionsManager;
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
	}

	public boolean process() {
		boolean result = true;

		try {
			RestrictionsProcessor restrictionsProcessor = new RestrictionsProcessor();
			List<Subscriber> subscribersList = restrictionsProcessor.getSubscribersList();

			// BufferedWriter writer = new BufferedWriter(new
			// FileWriter("/home/adrian/Development/Derqui Systems
			// Manager/data/result.txt"));
			// for (Subscriber subscriber : subscribersList) {
			// writer.write(subscriber.toString());
			// writer.newLine();
			// writer.flush();
			// }
			// writer.close();

			Connection connection = databaseConnectionsManager.takeConnection();

			try {
				RestrictionsManager restrictionsManager = new RestrictionsManager(connection);
				
				for(Subscriber subscriber : subscribersList) {
					subscriber.update(connection, restrictionsManager);
				}
			} catch (Exception exception) {
				logger.error("Error reading restrictions from database.", exception);
				result = false;
			} finally {
				databaseConnectionsManager.returnConnection(connection);
			}
		} catch (Exception e) {
			logger.error("Error reading Excel file", e);
			result = false;
		}

		return result;
	}
}
