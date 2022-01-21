/*
 * 		Role.java
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
 * 		Role.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jun 28, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Role {
	private int id;
	private String name;
	private String description;

	private static final Logger logger = LogManager.getLogger(Role.class);

	/**
	 * 
	 */
	public Role() {
	}

	/**
	 * @param name
	 * @param description
	 */
	public Role(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public static List<Role> getRolesList(Connection connection) throws Exception {
		List<Role> rolesList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getRolesList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Role role = new Role();
				role.setId(resultSet.getInt("id"));
				role.setName(resultSet.getString("name"));
				role.setDescription(resultSet.getString("description"));

				rolesList.add(role);
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getRolesList().", e);
			throw new Exception("Cannot get roles list from database.");
		} finally {
			statement.close();
		}
		return rolesList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
