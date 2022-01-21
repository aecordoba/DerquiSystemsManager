/*
 * 		MainProcess.java
 *   Copyright (C) 2016  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		MainProcess.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jul 16, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanageruserinterface.process;

import java.sql.Connection;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.configuration.Configuration;
import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.common.CommonTasks;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.features.Features;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.User;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanageruserinterface.version.Version;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.MainFrame;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.Login;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class MainProcess {
	private static CompositeConfiguration configuration;
	
	private final ResourceBundle bundle = ResourceBundle.getBundle("i18n/MessagesBundle"); // NOI18N
	private static final Logger logger = LogManager.getLogger(MainProcess.class);;

	public MainProcess() throws Exception {
		try {
			configuration = Configuration.getInstance("settings.xml");
			configuration.addProperty("DBUser", "smapp");
			configuration.addProperty("DBPassword", "smapp123");
			configuration.addProperty("userInterfaceVersion", Version.getVersion());
			Features features = Features.getInstance();
		} catch (ConfigurationException ex) {
			logger.fatal("Cannot load configuration file.", ex);
			throw new Exception("Fatal error.");
		}
	}

	public boolean process() {
		boolean result = false;
		result = initializeDatabaseData();
		if (result) {
			User user = Login.logUser();
			if (user != null) {
				logger.info("User " + user + " is logged in.");

				java.awt.EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (Version.isDependenciesVersionSatisfied()) {
							new MainFrame(user).setVisible(true);
						} else {
							logger.fatal("Dependencies versions are not satisfied.");
							JOptionPane.showMessageDialog(null, bundle.getString("Messages.NoSatisfiedDependenciesVersions"), "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				});

			} else {
				DatabaseConnectionsManager databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
				if (!databaseConnectionsManager.closeConnections())
					logger.error("Some connections could not be closed.");

			}
		}
		return result;
	}

	private boolean initializeDatabaseData() {
		boolean result = false;
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		try {
			databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
			connection = databaseConnectionsManager.takeConnection();

			CommonTasks.fillOwnNumerationTables(connection);

			logger.info("Database data were initialized normally.");
			result = true;
		} catch (Exception ex) {
			logger.error("Cannot initialize database data.");
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
		return result;
	}

	private boolean clearDatabaseData() {
		boolean result = false;
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		try {
			databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
			connection = databaseConnectionsManager.takeConnection();

			CommonTasks.clearOwnNumerationTables(connection);

			logger.info("Database data were cleared normally.");
			result = true;
		} catch (Exception ex) {
			logger.error("Cannot clear database data.");
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
		return result;
	}
}
