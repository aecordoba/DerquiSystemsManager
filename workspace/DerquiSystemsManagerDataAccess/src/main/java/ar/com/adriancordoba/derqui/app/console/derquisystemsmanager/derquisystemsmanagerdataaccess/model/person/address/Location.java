/*
 * 		Location.java
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
 * 		Location.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jul 24, 2016
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

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.managers.LocationsManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Location {
	private int id;
	private String name;
	private City city;
	private static final Logger logger = LogManager.getLogger(Location.class);

	/**
	 * 
	 */
	public Location() {
	}

	/**
	 * @param name
	 * @param city
	 */
	public Location(String name, City city) {
		this.name = name;
		this.city = city;
	}

	public Location(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("id"));
		setName(resultSet.getString("name"));
		
		int cityId = resultSet.getInt("cityId");
		City city = LocationsManager.getCity(cityId);
		if(city == null) {
			city = new City(resultSet);
			LocationsManager.addCity(city);
		}
		setCity(city);
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
	 * @return the city
	 */
	public City getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(City city) {
		this.city = city;
	}
}
