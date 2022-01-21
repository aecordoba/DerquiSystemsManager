/*
 * 		Street.java
 *   Copyright (C) 2019  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		Street.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Nov 5, 2019
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.addressestransponder.processors.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Street {
	private String name;
	private static Map<String, Integer> streetsMap;
	private static final Logger logger = LogManager.getLogger(Street.class);

	public static void insertStreets(Connection connection, Set<String> streetsList) throws SQLException {
		streetsMap = new HashMap<>();
		int id = 1;
		for (String street : streetsList) {
			CallableStatement statement = null;
			try {
				statement = connection.prepareCall("{call insertStreet(?, ?)}");
				statement.setInt(1, id);
				statement.setString(2, street);
				statement.execute();
				streetsMap.put(street, id);
				id++;
			} catch (SQLException e) {
				logger.error("Cannot execute stored procedure insertStreet() for " + street + ".", e);
			} finally {
				statement.close();
			}
		}
	}

	@Override
	public String toString() {
		return "Street [name=" + name + "]";
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
	 * @return the id
	 */
	public static int getId(String name) {
		return streetsMap.get(name);
	}
}
