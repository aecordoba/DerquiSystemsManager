/*
 * 		UsersManager.java
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
 * 		UsersManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Feb 28, 2018
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.managers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.Person;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.Role;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.User;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class UsersManager {
	private static List<User> usersList = new ArrayList<>();

	private static final Logger logger = LogManager.getLogger(UsersManager.class);

	static {
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		try {
			databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
			connection = databaseConnectionsManager.takeConnection();
			usersList = getUsersList(connection);
			logger.debug(usersList.size() + " user(s) found.");
		} catch (Exception ex) {
			logger.error("UsersManager couldn't fill users list.");
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
	}

	private static List<User> getUsersList(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getUsersList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int userId = resultSet.getInt("userId");
				User user = getUser(userId);
				if (user != null) {
					user = new User();
					user.setId(userId);
					user.setUsername(resultSet.getString("username"));
					user.addRole(RolesManager.getRole(resultSet.getInt("roleId")));

					Person person = new Person();
					person.setId(resultSet.getInt("personId"));
					person.setFirstName(resultSet.getString("firstName"));
					person.setMiddleName(resultSet.getString("middleName"));
					person.setLastName(resultSet.getString("lastName"));
					user.setPerson(person);

					usersList.add(user);
				}else
					user.addRole(RolesManager.getRole(resultSet.getInt("roleId")));
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getUsersList().", e);
			throw new Exception("Cannot get users list from database.");
		} finally {
			statement.close();
		}
		return usersList;
	}

	public static User getUser(int id) {
		User user = null;
		for (User temp : usersList) {
			if (temp.getId() == id) {
				user = temp;
				break;
			}
		}
		return user;
	}

	public static List<User> getUsersList() {
		return usersList;
	}

	public static void clearUsersList() {
		usersList.clear();
	}
}
