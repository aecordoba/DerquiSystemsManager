/*
 * 		Property.java
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
 * 		Property.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Oct 16, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.features;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Features {
	private static Features instance;
	private Map<String, String> featuresMap;
	private static final Logger logger = LogManager.getLogger(Features.class);

	/**
	 * @throws Exception
	 * 
	 */
	private Features() throws Exception {
		DatabaseConnectionsManager databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
		Connection connection = databaseConnectionsManager.takeConnection();
		try {
			fillFeaturesMap(connection);
		} catch (Exception e) {
			logger.fatal("Cannot get features from database.", e);
			throw new Exception("Fatal error.");
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
	}

	public static Features getInstance() throws Exception {
		if (instance == null)
			instance = new Features();
		return instance;
	}

	private void fillFeaturesMap(Connection connection) throws Exception {
		featuresMap = new HashMap<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getFeaturesList()}");
			ResultSet resultSet = statement.executeQuery();
			logger.info("Using the following features:");
			String name;
			String value;
			while (resultSet.next()) {
				name = resultSet.getString("name");
				value = resultSet.getString("value");
				featuresMap.put(name, value);
				logger.info("Feature name: " + name + " value: " + value);
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getFeaturesList().", e);
			throw new Exception("Cannot get features list from database.");
		} finally {
			statement.close();
		}
	}

	public String getFeature(String name) {
		return featuresMap.get(name);
	}

	public List<String> getFeaturesList(String name) {
		List<String> featuresList = new ArrayList<>();
		Set<String> keysSet = featuresMap.keySet();
		for (String key : keysSet) {
			if (key.startsWith(name))
				featuresList.add(featuresMap.get(key));
		}
		return featuresList;
	}
}
