/*
 * 		OfficeCodesDataManager.java
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
 * 		OfficeCodesDataManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Dec 4, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.managers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Area;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Country;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.OfficeCode;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Phone;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class OfficeCodesDataManager {
	private static List<Country> countriesList;
	private static List<Country> ownCountriesList;
	private static List<Country> countriesWithAreasList;
	private static List<Area> areasList;
	private static List<Area> ownAreasList;
	private static List<OfficeCode> officeCodesList;
	private static List<OfficeCode> ownOfficeCodesList;
	private static List<Phone> ownNumerationList;

	private static final Logger logger = LogManager.getLogger(OfficeCodesDataManager.class);

	static {
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
		connection = databaseConnectionsManager.takeConnection();
		try {
			fillOfficeCodesData(connection);
		} catch (Exception e) {
			logger.error("Cannot fill office codes data from database.", e);
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
	}

	private static void fillOfficeCodesData(Connection connection) throws Exception {
		countriesList = new ArrayList<>();
		ownCountriesList = new ArrayList<>();
		countriesWithAreasList = new ArrayList<>();
		areasList = new ArrayList<>();
		ownAreasList = new ArrayList<>();
		officeCodesList = new ArrayList<>();
		ownOfficeCodesList = new ArrayList<>();
		ownNumerationList = new ArrayList<>();

		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getOfficeCodesDataList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Country country = getCountry(resultSet.getInt("countryId"));
				if (country == null) {
					country = new Country(resultSet);
					countriesList.add(country);
				}

				int areaId = resultSet.getInt("areaId");
				if (areaId > 0) {
					Area area = getArea(areaId);
					if (area == null) {
						area = new Area(resultSet, country);
						areasList.add(area);
					}

					if (!countriesWithAreasList.contains(country))
						countriesWithAreasList.add(country);

					int officeCodeId = resultSet.getInt("officeCodeId");
					if (officeCodeId > 0) {
						OfficeCode officeCode = getOfficeCode(officeCodeId);
						if (officeCode == null) {
							officeCode = new OfficeCode(resultSet, area);
							officeCodesList.add(officeCode);
						}

						String number = resultSet.getString("number");
						if (number != null) {
							Phone ownNumeration = new Phone();
							if (!number.isEmpty())
								ownNumeration.setNumber(number);
							ownNumeration.setOfficeCode(officeCode);
							ownNumerationList.add(ownNumeration);

							if (!ownCountriesList.contains(country))
								ownCountriesList.add(country);
							if (!ownAreasList.contains(area))
								ownAreasList.add(area);
							if (!ownOfficeCodesList.contains(officeCode))
								ownOfficeCodesList.add(officeCode);
						}
					}
				}
			}
			logger.info(countriesList.size() + " countries found (" + ownCountriesList.size() + " own country).");
			logger.info(areasList.size() + " areas found (" + ownAreasList.size() + " own area(s)).");
			logger.info(officeCodesList.size() + " office codes found (" + ownOfficeCodesList.size() + " own office code(s)).");
		} catch (SQLException e) {
			logger.error("Cannot execute getOfficeCodesDataList().", e);
			throw new Exception("Cannot get office codes data list from database.");
		} finally {
			statement.close();
		}
	}

	public static void reload(Connection connection) throws Exception {
		fillOfficeCodesData(connection);
	}

	// Countries.

	private static Country getCountry(int id) {
		Country country = null;
		for (Country temp : countriesList) {
			if (temp.getId() == id) {
				country = temp;
				break;
			}
		}
		return country;
	}

	public static List<Country> getCountriesListOrderedByName() {
		List<Country> orderedCountriesList = new ArrayList<>();
		for (Country country : countriesList)
			orderedCountriesList.add(country);

		Collections.sort(orderedCountriesList, new Comparator<Country>() {

			@Override
			public int compare(Country country1, Country country2) {
				return country1.getName().compareTo(country2.getName());
			}
		});
		return orderedCountriesList;
	}

	// Areas.

	private static Area getArea(int id) {
		Area area = null;
		for (Area temp : areasList) {
			if (temp.getId() == id) {
				area = temp;
				break;
			}
		}
		return area;
	}

	public static List<Area> getAreasListOrderedByName() {
		List<Area> orderedAreasList = new ArrayList<>();
		for (Area area : areasList)
			orderedAreasList.add(area);

		Collections.sort(orderedAreasList, new Comparator<Area>() {

			@Override
			public int compare(Area area1, Area area2) {
				return area1.getName().compareTo(area2.getName());
			}
		});
		return orderedAreasList;
	}

	// Office Codes.

	public static OfficeCode getOfficeCode(int id) {
		OfficeCode officeCode = null;
		for (OfficeCode temp : officeCodesList) {
			if (temp.getId() == id) {
				officeCode = temp;
				break;
			}
		}
		return officeCode;
	}

	/**
	 * @return the countriesList
	 */
	public static List<Country> getCountriesList() {
		return countriesList;
	}

	/**
	 * @return the ownCountriesList
	 */
	public static List<Country> getOwnCountriesList() {
		return ownCountriesList;
	}

	/**
	 * @return the countriesWithAreasList
	 */
	public static List<Country> getCountriesWithAreasList() {
		return countriesWithAreasList;
	}

	/**
	 * @return the areasList
	 */
	public static List<Area> getAreasList() {
		return areasList;
	}

	/**
	 * @return the ownAreasList
	 */
	public static List<Area> getOwnAreasList() {
		return ownAreasList;
	}

	/**
	 * @return the officeCodesList
	 */
	public static List<OfficeCode> getOfficeCodesList() {
		return officeCodesList;
	}

	/**
	 * @return the ownOfficeCodesList
	 */
	public static List<OfficeCode> getOwnOfficeCodesList() {
		return ownOfficeCodesList;
	}

	/**
	 * @return the ownNumerationList
	 */
	public static List<Phone> getOwnNumerationList() {
		return ownNumerationList;
	}
}
