/*
 * 		SubscriberBroadbandStatesManager.java
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
 * 		SubscriberBroadbandStatesManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Dec 9, 2017
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
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors.SubscriberBroadbandStateName;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.state.SubscriberBroadbandState;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class SubscriberBroadbandStatesManager {
	private static List<SubscriberBroadbandState> subscriberBroadbandStatesList;
	private static final Logger logger = LogManager.getLogger(SubscriberBroadbandStatesManager.class);

	static {
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		try {
			databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
			connection = databaseConnectionsManager.takeConnection();
			getSubscriberBroadbandStatesList(connection);
			logger.debug(subscriberBroadbandStatesList.size() + " subscriber broadband states found.");
		} catch (Exception ex) {
			logger.error("SubscriberStatesManager couldn't fill subscriber broadband states list.");
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
	}

	private static void getSubscriberBroadbandStatesList(Connection connection) throws Exception {
		subscriberBroadbandStatesList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getSubscriberBroadbandStatesList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				SubscriberBroadbandState subscriberBroadbandState = new SubscriberBroadbandState(resultSet);
				subscriberBroadbandStatesList.add(subscriberBroadbandState);
			}
		} catch (SQLException e) {
			logger.error("Cannot get subscriber broadband states list from database.", e);
			throw new Exception("Cannot get subscriber broadband states list from database.");
		} finally {
			statement.close();
		}
	}

	public static SubscriberBroadbandState getSubscriberBroadbandState(int id) {
		SubscriberBroadbandState subscriberBroadbandState = null;
		for (SubscriberBroadbandState temp : subscriberBroadbandStatesList) {
			if (temp.getId() == id) {
				subscriberBroadbandState = temp;
				break;
			}
		}
		return subscriberBroadbandState;
	}

	public static SubscriberBroadbandState getSubscriberBroadbandState(SubscriberBroadbandStateName name) {
		SubscriberBroadbandState subscriberBroadbandState = null;
		for (SubscriberBroadbandState temp : subscriberBroadbandStatesList) {
			if (temp.getName() == name) {
				subscriberBroadbandState = temp;
				break;
			}
		}
		return subscriberBroadbandState;
	}

	/**
	 * @return the subscriberBroadbandStatesList
	 */
	public static List<SubscriberBroadbandState> getSubscriberBroadbandStatesList() {
		return subscriberBroadbandStatesList;
	}
}
