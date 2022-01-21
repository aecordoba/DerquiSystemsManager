/*
 * 		City.java
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
 * 		City.java
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
public class City {
	private int id;
	private String name;
	private State state;
	private static final Logger logger = LogManager.getLogger(City.class);

	/**
	 * 
	 */
	public City() {
	}

	/**
	 * @param name
	 * @param state
	 */
	public City(String name, State state) {
		this.name = name;
		this.state = state;
	}

	public City(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("cityId"));
		setName(resultSet.getString("cityName"));

		int stateId = resultSet.getInt("stateId");
		State state = LocationsManager.getState(stateId);
		if (state == null) {
			state = new State(resultSet);
			LocationsManager.addState(state);
		}
		setState(state);
	}

	public static List<City> getCitiesList(Connection connection) throws Exception {
		List<City> citiesList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getCitiesList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				City city = new City();
				city.setId(resultSet.getInt("id"));
				city.setName(resultSet.getString("name"));
				citiesList.add(city);
			}
		} catch (SQLException e) {
			logger.error("Cannot get cities list.", e);
			throw new Exception("Cannot get cities list.");
		} finally {
			statement.close();
		}
		return citiesList;
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
	 * @return the state
	 */
	public State getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(State state) {
		this.state = state;
	}
}
