/*
 * 		DSLAMManufacturer.java
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
 * 		DSLAMManufacturer.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jan 28, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class DSLAMManufacturer implements Comparable<DSLAMManufacturer> {
	private int id;
	private String name;

	private static final Logger logger = LogManager.getLogger(DSLAMManufacturer.class);

	/**
	 * 
	 */
	public DSLAMManufacturer() {
	}

	/**
	 * @param name
	 */
	public DSLAMManufacturer(String name) {
		this.name = name;
	}

	public DSLAMManufacturer(ResultSet resultSet, boolean board) throws SQLException {
		if (!board) {
			setId(resultSet.getInt("dslamManufacturerId"));
			setName(resultSet.getString("dslamManufacturerName"));
		} else {
			setId(resultSet.getInt("boardManufacturerId"));
			setName(resultSet.getString("boardManufacturerName"));
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(DSLAMManufacturer otherManufacturer) {
		return getName().compareTo(otherManufacturer.getName());
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
