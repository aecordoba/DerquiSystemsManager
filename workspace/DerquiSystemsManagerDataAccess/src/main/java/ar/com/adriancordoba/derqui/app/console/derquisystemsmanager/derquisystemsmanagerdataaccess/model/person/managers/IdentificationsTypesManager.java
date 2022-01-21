/*
 * 		IdentificationsTypesManager.java
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
 * 		IdentificationsTypesManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jan 6, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.managers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.IdentificationType;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class IdentificationsTypesManager {
	private static List<IdentificationType> identificationsTypesList = new ArrayList<>();
	private static final Logger logger = LogManager.getLogger(IdentificationsTypesManager.class);

	static {
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		try {
			databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
			connection = databaseConnectionsManager.takeConnection();
			identificationsTypesList = IdentificationType.getIdentificationsTypesList(connection);
			logger.debug(identificationsTypesList.size() + " identification(s) type(s) found.");
		} catch (Exception ex) {
			logger.error("IdentificationsTypesManager couldn't fill identifications types list.");
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
	}

	public static IdentificationType getIdentificationType(int id) {
		IdentificationType identificationType = null;
		for (IdentificationType temp : identificationsTypesList) {
			if (temp.getId() == id) {
				identificationType = temp;
				break;
			}
		}
		return identificationType;
	}

	public static void clearIdentificationsTypesList() {
		identificationsTypesList.clear();
	}

	public static void addIdentificationType(IdentificationType identificationType) {
		identificationsTypesList.add(identificationType);
	}

	/**
	 * @return the identificationsTypesList
	 */
	public static List<IdentificationType> getIdentificationsTypesList() {
		return identificationsTypesList;
	}
}
