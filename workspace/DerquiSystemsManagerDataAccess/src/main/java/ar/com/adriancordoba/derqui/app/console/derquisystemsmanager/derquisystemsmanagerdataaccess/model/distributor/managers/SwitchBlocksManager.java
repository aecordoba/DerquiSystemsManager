/*
 * 		SwitchBlocksManager.java
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
 * 		SwitchBlocksManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jan 11, 2018
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.managers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.SwitchBlock;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class SwitchBlocksManager {
	private static List<SwitchBlock> switchBlocksList;
	private static final Logger logger = LogManager.getLogger(SwitchBlocksManager.class);

	static {
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		try {
			databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
			connection = databaseConnectionsManager.takeConnection();
			loadSwitchBlocksList(connection);
			logger.debug(switchBlocksList.size() + " switch blocks found.");
		} catch (Exception ex) {
			logger.error("SwitchBlocksManager couldn't fill switch blocks list.");
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
	}

	public static void loadSwitchBlocksList(Connection connection) throws Exception {
		switchBlocksList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getSwitchBlocksList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				SwitchBlock switchBlock = new SwitchBlock(resultSet);
				switchBlocksList.add(switchBlock);
			}
		} catch (SQLException e) {
			logger.error("Cannot execute getSwitchBlocksList().", e);
			throw new Exception("Cannot get switch blocks list from database.");
		} finally {
			statement.close();
		}
	}

	public static SwitchBlock getSwitchBlock(int id) {
		SwitchBlock switchBlock = null;
		for (SwitchBlock temp : switchBlocksList) {
			if (temp.getId() == id) {
				switchBlock = temp;
				break;
			}
		}
		return switchBlock;
	}

	public static SwitchBlock getSwitchBlock(int siteId, String name) {
		SwitchBlock switchBlock = null;
		for (SwitchBlock temp : switchBlocksList) {
			if ((temp.getName().equals(name)) && (temp.getSite().getId() == siteId)) {
				switchBlock = temp;
				break;
			}
		}
		return switchBlock;
	}

	/**
	 * @return the switchBlocksList
	 */
	public static List<SwitchBlock> getSwitchBlocksList() {
		return switchBlocksList;
	}
}
