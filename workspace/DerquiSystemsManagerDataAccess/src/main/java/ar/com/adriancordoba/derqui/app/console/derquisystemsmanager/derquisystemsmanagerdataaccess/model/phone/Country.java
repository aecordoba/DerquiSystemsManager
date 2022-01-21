/*
 * 		Country.java
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
 * 		Country.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jul 24, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Country {
	private int id;
	private String name;
	private int code;

	/**
	 * 
	 */
	public Country() {
	}

	/**
	 * @param name
	 * @param code
	 */
	public Country(String name, int code) {
		this.name = name;
		this.code = code;
	}

	public Country(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("countryId"));
		setCode(resultSet.getInt("countryCode"));
		setName(resultSet.getString("countryName"));
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
}
