/*
 * 		IdentificationType.java
 *   Copyright (C) 2016  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		IdentificationType.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jul 24, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person;

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
public class IdentificationType {
	private int id;
	private String name;
	private static final Logger logger = LogManager.getLogger(IdentificationType.class);

	/**
	 * 
	 */
	public IdentificationType() {
	}

	/**
	 * @param name
	 */
	public IdentificationType(String name) {
		this.name = name;
	}

	public static List<IdentificationType> getIdentificationsTypesList(Connection connection) throws Exception {
		List<IdentificationType> identificationsTypesList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getIdentificationsTypesList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				IdentificationType identificationType = new IdentificationType();
				identificationType.setId(resultSet.getInt("id"));
				identificationType.setName(resultSet.getString("name"));
				identificationsTypesList.add(identificationType);
			}
		} catch (SQLException e) {
			logger.error("Cannot execute getIdentificationsTypesList().", e);
			throw new Exception("Cannot get identifications types list from database.");
		} finally {
			statement.close();
		}
		return identificationsTypesList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
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
}
