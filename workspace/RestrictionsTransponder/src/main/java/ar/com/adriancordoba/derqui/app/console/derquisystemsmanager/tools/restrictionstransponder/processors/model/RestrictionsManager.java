/*
 * 		RestrictionsManager.java
 *   Copyright (C) 2019  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		RestrictionsManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Nov 6, 2019
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.restrictionstransponder.processors.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class RestrictionsManager {
	private Map<String, Integer> restrictionsMap;
	private static final Logger logger = LogManager.getLogger(RestrictionsManager.class);
		
	public RestrictionsManager(Connection connection) throws Exception {
		restrictionsMap = new HashMap<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getSubscriberRestrictionsList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String origination = resultSet.getString("originationName");
				String termination = resultSet.getString("terminationName");
				restrictionsMap.put(termination + origination, id);
			}
		} catch (SQLException e) {
			logger.error("Cannot get subscriber restrictions list from database.", e);
			throw new Exception("Cannot get subscriber restrictions list from database.");
		} finally {
			statement.close();
		}
	}

	public int getRestrictionId(String name) {
		return restrictionsMap.get(name);
	}
}
