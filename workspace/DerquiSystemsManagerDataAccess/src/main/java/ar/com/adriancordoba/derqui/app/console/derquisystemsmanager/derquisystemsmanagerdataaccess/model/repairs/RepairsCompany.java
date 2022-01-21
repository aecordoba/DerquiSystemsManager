/*
 * 		RepairsCompany.java
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
 * 		RepairsCompany.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jan 26, 2018
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class RepairsCompany {
	private int id;
	private String name;

	/**
	 * @param name
	 */
	public RepairsCompany(String name) {
		this.name = name;
	}

	/**
	 * 
	 */
	public RepairsCompany() {
	}

	public RepairsCompany(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("repairsCompanyId"));
		setName(resultSet.getString("repairsCompanyName"));
	}

	/* (non-Javadoc)
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
