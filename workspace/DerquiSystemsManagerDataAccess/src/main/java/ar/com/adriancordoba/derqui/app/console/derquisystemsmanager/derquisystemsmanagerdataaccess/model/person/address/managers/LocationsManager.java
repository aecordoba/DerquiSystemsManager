/*
 * 		LocationsManager.java
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
 * 		LocationsManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Feb 2, 2018
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.managers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.City;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.Location;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.State;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Country;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class LocationsManager {
	private static List<Location> locationsList;
	private static List<City> citiesList;
	private static List<State> statesList;
	private static List<Country> countriesList;

	private static final Logger logger = LogManager.getLogger(LocationsManager.class);

	static {
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		try {
			databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
			connection = databaseConnectionsManager.takeConnection();
			loadLocations(connection);
		} catch (Exception ex) {
			logger.error("LocationssManager couldn't fill locations lists.");
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
	}

	private static void loadLocations(Connection connection) throws Exception {
		locationsList = new ArrayList<>();
		citiesList = new ArrayList<>();
		statesList = new ArrayList<>();
		countriesList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getLocationsList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Location location = new Location(resultSet);
				locationsList.add(location);
			}
			logger.info(locationsList.size() + " locations found.");
			logger.info(citiesList.size() + " cities found.");
			logger.info(statesList.size() + " states found.");
			logger.info(countriesList.size() + " countries found.");
		} catch (SQLException e) {
			logger.error("Cannot execute getLocationsList().", e);
			throw new Exception("Cannot get locations lists from database.");
		} finally {
			statement.close();
		}
	}

	// Countries.

	public static Country getCountry(int id) {
		Country country = null;
		for (Country temp : countriesList) {
			if (temp.getId() == id) {
				country = temp;
				break;
			}
		}
		return country;
	}

	public static void addCountry(Country country) {
		countriesList.add(country);
	}

	// States.

	public static State getState(int id) {
		State state = null;
		for (State temp : statesList) {
			if (temp.getId() == id) {
				state = temp;
				break;
			}
		}
		return state;
	}

	public static void addState(State state) {
		statesList.add(state);
	}

	// Cities.

	public static City getCity(int id) {
		City city = null;
		for (City temp : citiesList) {
			if (temp.getId() == id) {
				city = temp;
				break;
			}
		}
		return city;
	}

	public static void addCity(City city) {
		citiesList.add(city);
	}

	// Locations.

	public static Location getLocation(int id) {
		Location location = null;
		for (Location temp : locationsList) {
			if (temp.getId() == id) {
				location = temp;
				break;
			}
		}
		return location;
	}

	/**
	 * @return the locationsList
	 */
	public static List<Location> getLocationsList() {
		return locationsList;
	}

	/**
	 * @return the citiesList
	 */
	public static List<City> getCitiesList() {
		return citiesList;
	}

	/**
	 * @return the statesList
	 */
	public static List<State> getStatesList() {
		return statesList;
	}

	/**
	 * @return the countriesList
	 */
	public static List<Country> getCountriesList() {
		return countriesList;
	}
}
