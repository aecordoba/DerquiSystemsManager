/*
 * 		State.java
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
 * 		State.java
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
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Country;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class State {
	private int id;
	private String name;
	private Country country;
	private static final Logger logger = LogManager.getLogger(State.class);

	/**
	 * 
	 */
	public State() {
	}

	/**
	 * @param name
	 * @param country
	 */
	public State(String name, Country country) {
		this.name = name;
		this.country = country;
	}

	public State(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("stateId"));
		setName(resultSet.getString("stateName"));

		int countryId = resultSet.getInt("countryId");
		Country country = LocationsManager.getCountry(countryId);
		if (country == null) {
			country = new Country(resultSet);
			LocationsManager.addCountry(country);
		}
		setCountry(country);
	}

	public static List<State> getStatesList(Connection connection) throws Exception {
		List<State> statesList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getStatesList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				State state = new State();
				state.setId(resultSet.getInt("id"));
				state.setName(resultSet.getString("name"));
				statesList.add(state);
			}
		} catch (SQLException e) {
			logger.error("Cannot get states list.", e);
			throw new Exception("Cannot get cities list.");
		} finally {
			statement.close();
		}
		return statesList;
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
