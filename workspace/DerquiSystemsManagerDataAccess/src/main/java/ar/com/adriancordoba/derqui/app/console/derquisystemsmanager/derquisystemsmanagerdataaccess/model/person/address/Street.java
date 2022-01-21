/*
 * 		Street.java
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
 * 		Street.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jul 10, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.managers.StreetsManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Street {
	private int id;
	private String name;
	private String code;
	private String spare;

	private static final Logger logger = LogManager.getLogger(Street.class);

	/**
	 * 
	 */
	public Street() {
	}

	/**
	 * @param name
	 * @param code
	 * @param spare
	 */
	public Street(String name, String code, String spare) {
		this.name = name;
		this.code = code;
		this.spare = spare;
	}

	public static List<Street> getStreetsList(Connection connection) throws Exception {
		List<Street> streetsList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getStreetsList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Street street = new Street();
				street.setId(resultSet.getInt("id"));
				street.setName(resultSet.getString("name"));
				street.setCode(resultSet.getString("code"));
				street.setSpare(resultSet.getString("spare"));
				streetsList.add(street);
			}
		} catch (SQLException e) {
			logger.error("Cannot get streets list.", e);
			throw new Exception("Cannot get streets list.");
		} finally {
			statement.close();
		}
		return streetsList;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Street other = (Street) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param abreviation the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the spare
	 */
	public String getSpare() {
		return spare;
	}

	/**
	 * @param spare the spare to set
	 */
	public void setSpare(String spare) {
		this.spare = spare;
	}
}
