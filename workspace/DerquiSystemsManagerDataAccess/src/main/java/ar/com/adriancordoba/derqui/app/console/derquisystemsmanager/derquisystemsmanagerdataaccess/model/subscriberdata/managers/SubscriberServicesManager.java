/*
 * 		SubscriberServicesManager.java
 *   Copyright (C) 2017  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		SubscriberServicesManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Dec 8, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.managers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.SubscriberService;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class SubscriberServicesManager {
	private static List<SubscriberService> subscriberServicesList;
	private static final Logger logger = LogManager.getLogger(SubscriberServicesManager.class);

	static {
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		try {
			databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
			connection = databaseConnectionsManager.takeConnection();
			getSubscriberServicesList(connection);
			logger.debug(subscriberServicesList.size() + " subscriber services found.");
		} catch (Exception ex) {
			logger.error("SubscriberServicesManager couldn't fill subscriber services list.");
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
	}

	private static void getSubscriberServicesList(Connection connection) throws Exception {
		subscriberServicesList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getSubscriberServicesList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				SubscriberService subscriberService = new SubscriberService(resultSet);
				subscriberServicesList.add(subscriberService);
			}
		} catch (SQLException e) {
			logger.error("Cannot get subscriber services list from database.", e);
			throw new Exception("Cannot get subscriber services list from database.");
		} finally {
			statement.close();
		}
	}

	public static SubscriberService getSubscriberService(int id) {
		SubscriberService subscriberService = null;
		for (SubscriberService temp : subscriberServicesList) {
			if (temp.getId() == id) {
				subscriberService = temp;
				break;
			}
		}
		return subscriberService;
	}

	public static SubscriberService getSubscriberService(String name) {
		SubscriberService subscriberService = null;
		for (SubscriberService temp : subscriberServicesList) {
			if (temp.getName().equals(name)) {
				subscriberService = temp;
				break;
			}
		}
		return subscriberService;
	}

	/**
	 * @return the subscriberServicesList
	 */
	public static List<SubscriberService> getSubscriberServicesList() {
		return subscriberServicesList;
	}
}
