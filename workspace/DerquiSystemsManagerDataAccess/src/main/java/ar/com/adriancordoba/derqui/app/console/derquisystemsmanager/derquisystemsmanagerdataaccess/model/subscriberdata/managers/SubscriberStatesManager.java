/*
 * 		SubscriberStatesManager.java
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
 * 		SubscriberStatesManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Nov 28, 2017
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
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors.SubscriberStateName;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.SubscriberLineClass;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.state.SubscriberState;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class SubscriberStatesManager {
	private static List<SubscriberState> subscriberStatesList;
	private static final Logger logger = LogManager.getLogger(SubscriberStatesManager.class);

	static {
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		try {
			databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
			connection = databaseConnectionsManager.takeConnection();
			getSubscriberStatesList(connection);
			logger.debug(subscriberStatesList.size() + " subscriber states found.");
		} catch (Exception ex) {
			logger.error("SubscriberStatesManager couldn't fill subscriber states list.");
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
	}

	private static void getSubscriberStatesList(Connection connection) throws Exception {
		subscriberStatesList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getSubscriberStatesList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				SubscriberState subscriberState = new SubscriberState(resultSet);
				subscriberStatesList.add(subscriberState);
			}
		} catch (SQLException e) {
			logger.error("Cannot get subscriber states list from database.", e);
			throw new Exception("Cannot get subscriber states list from database.");
		} finally {
			statement.close();
		}
	}

	public static SubscriberState getSubscriberState(int id) {
		SubscriberState subscriberState = null;
		for (SubscriberState temp : subscriberStatesList) {
			if (temp.getId() == id) {
				subscriberState = temp;
				break;
			}
		}
		return subscriberState;
	}

	public static SubscriberState getSubscriberState(SubscriberStateName name) {
		SubscriberState subscriberState = null;
		for (SubscriberState temp : subscriberStatesList) {
			if (temp.getName() == name) {
				subscriberState = temp;
				break;
			}
		}
		return subscriberState;
	}

	/**
	 * @return the subscriberStatesList
	 */
	public static List<SubscriberState> getSubscriberStatesList() {
		return subscriberStatesList;
	}
}
