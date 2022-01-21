/*
 * 		RolesManager.java
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
 * 		RolesManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jun 28, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.managers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.IdentificationType;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.Role;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class RolesManager {
	private static List<Role> rolesList = new ArrayList<>();

	private static final Logger logger = LogManager.getLogger(RolesManager.class);

	static {
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		try {
			databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
			connection = databaseConnectionsManager.takeConnection();
			rolesList = Role.getRolesList(connection);
			logger.debug(rolesList.size() + " role(s) found.");
		} catch (Exception ex) {
			logger.error("RolesManager couldn't fill roles list.");
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
	}

	public static Role getRole(int id) {
		Role role = null;
		for (Role temp : rolesList) {
			if (temp.getId() == id) {
				role = temp;
				break;
			}
		}
		return role;
	}

	public static List<Role> getRolesList() {
		return rolesList;
	}

	public static void clearRolesList() {
		rolesList.clear();
	}
}
