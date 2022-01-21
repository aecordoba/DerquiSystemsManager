/*
 * 		Area.java
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
 * 		Area.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jul 24, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.managers.OfficeCodesDataManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Area {
	private int id;
	private String name;
	private int code;
	private Country country;

	private static final Logger logger = LogManager.getLogger(Area.class);

	/**
	 * 
	 */
	public Area() {
	}

	/**
	 * @param name
	 * @param code
	 * @param country
	 */
	public Area(String name, int code, Country country) {
		this.name = name;
		this.code = code;
		this.country = country;
	}

	public Area(ResultSet resultSet, Country country) throws SQLException {
		setId(resultSet.getInt("areaId"));
		setName(resultSet.getString("areaName"));
		setCode(resultSet.getInt("areaCode"));
		setCountry(country);
	}

	public void insert(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call insertArea(?, ?, ?, ?)}");
			statement.setInt(1, country.getCode());
			statement.setString(2, country.getName());
			statement.setInt(3, getCode());
			statement.setString(4, getName());

			statement.execute();
			logger.debug("Area " + getName() + " was inserted in database.");
			OfficeCodesDataManager.reload(connection);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure insertArea().", e);
			throw new Exception("Cannot insert area in database.");
		} finally {
			statement.close();
		}
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

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the country
	 */
	public Country getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(Country country) {
		this.country = country;
	}
}
