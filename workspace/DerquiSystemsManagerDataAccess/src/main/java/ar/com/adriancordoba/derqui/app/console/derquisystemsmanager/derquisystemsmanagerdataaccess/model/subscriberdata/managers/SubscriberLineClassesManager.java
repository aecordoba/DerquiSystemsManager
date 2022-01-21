/*
 * 		SubscriberLineClassesManager.java
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
 * 		SubscriberLineClassesManager.java
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
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors.SubscriberLineClassType;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.SubscriberLineClass;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class SubscriberLineClassesManager {
	private static List<SubscriberLineClass> subscriberLineClassesList;
	private static final Logger logger = LogManager.getLogger(SubscriberLineClassesManager.class);

	static {
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		try {
			databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
			connection = databaseConnectionsManager.takeConnection();
			getSubscriberLineClassesList(connection);
			logger.debug(subscriberLineClassesList.size() + " subscriber line classes found.");
		} catch (Exception ex) {
			logger.error("SubscriberLineClassesManager couldn't fill subscriber line classes list.");
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
	}

	private static void getSubscriberLineClassesList(Connection connection) throws Exception{
		subscriberLineClassesList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getSubscriberLineClassesList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				SubscriberLineClass subscriberLineClass = new SubscriberLineClass(resultSet);
				subscriberLineClassesList.add(subscriberLineClass);
			}
		} catch (SQLException e) {
			logger.error("Cannot get subscriber line classes list from database.", e);
			throw new Exception("Cannot get subscriber line classes list from database.");
		} finally {
			statement.close();
		}
	}

	public static SubscriberLineClass getSubscriberLineClass(int id) {
		SubscriberLineClass subscriberLineClass = null;
		for(SubscriberLineClass temp : subscriberLineClassesList) {
			if(temp.getId() == id) {
				subscriberLineClass = temp;
				break;
			}
		}
		return subscriberLineClass;
	}
	
	public static SubscriberLineClass getSubscriberLineClass(SubscriberLineClassType type) {
		SubscriberLineClass subscriberLineClass = null;
		for(SubscriberLineClass temp : subscriberLineClassesList) {
			if(temp.getType() == type) {
				subscriberLineClass = temp;
				break;
			}
		}
		return subscriberLineClass;
	}

	/**
	 * @return the subscriberLineClassesList
	 */
	public static List<SubscriberLineClass> getSubscriberLineClassesList() {
		return subscriberLineClassesList;
	}
}
