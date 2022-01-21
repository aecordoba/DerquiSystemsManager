/*
 * 		RepairingTypesManager.java
 *   Copyright (C) 2018  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		RepairingTypesManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jan 30, 2018
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs.managers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs.RepairingType;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class RepairingTypesManager {
	private static List<RepairingType> repairingTypesList;
	private static final Logger logger = LogManager.getLogger(RepairingTypesManager.class);

	static {
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
		connection = databaseConnectionsManager.takeConnection();
		try {
			loadRepairingTypesList(connection);
		} catch (Exception e) {
			logger.error("Cannot fill repaiirng types data from database.", e);
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
	}

	private static void loadRepairingTypesList(Connection connection) throws Exception {
		repairingTypesList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getRepairingTypesList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				RepairingType repairingType = new RepairingType(resultSet);
				repairingTypesList.add(repairingType);
			}
		} catch (SQLException e) {
			logger.error("Cannot execute getRepairingTypesList().", e);
			throw new Exception("Cannot get repairing types list from database.");
		} finally {
			statement.close();
		}
	}

	public static RepairingType getRepairingType(int id) {
		RepairingType repairingType = null;
		for (RepairingType temp : repairingTypesList) {
			if (temp.getId() == id) {
				repairingType = temp;
				break;
			}
		}
		return repairingType;
	}

	/**
	 * @return the repairingTypesList
	 */
	public static List<RepairingType> getRepairingTypesList() {
		return repairingTypesList;
	}
}
