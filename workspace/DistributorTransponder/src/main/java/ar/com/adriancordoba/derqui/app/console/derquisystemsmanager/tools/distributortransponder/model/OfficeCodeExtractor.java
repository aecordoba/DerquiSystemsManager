/*
 * 		OfficeCodeExtractor.java
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
 * 		OfficeCodeExtractor.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Aug 29, 2019
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.distributortransponder.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.configuration.Configuration;
import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.app.console.switching.subscribers.discoverers.SubscribersScanner;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class OfficeCodeExtractor {
	private List<OfficeCode> officeCodesList;
	private static CompositeConfiguration configuration;

	private static final Logger logger = LogManager.getLogger(OfficeCodeExtractor.class);

	/**
	 * @throws Exception 
	 * 
	 */
	public OfficeCodeExtractor(Connection connection) throws Exception {
		try {
			configuration = Configuration.getInstance("settings.xml");
		} catch (ConfigurationException ex) {
			logger.fatal("Cannot load configuration file.", ex);
			throw new Exception("Fatal error.");
		}
		int countryCode = configuration.getInt("distributor-transponder.country-code.code");
		int areaCode = configuration.getInt("distributor-transponder.country-code.area-code.code");
		
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getOfficeCodesList(?, ?)}");
			statement.setInt(1, countryCode);
			statement.setInt(2, areaCode);
			ResultSet resultSet = statement.executeQuery();
			officeCodesList = new ArrayList<>();
			while(resultSet.next()) {
				int id = resultSet.getInt("id");
				int code = resultSet.getInt("code");
				OfficeCode officeCode = new OfficeCode(id, code);
				officeCodesList.add(officeCode);
			}
			
			String officeCodes = "" + officeCodesList.get(0).getCode();
			for(int i = 1; i < officeCodesList.size(); i++) {
				officeCodes += ", " + officeCodesList.get(i).getCode();
			}
			logger.info("Office codes found in database: " + officeCodes + ".");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getOfficeCodesListList().", e);
			throw new Exception("Cannot get office codes list from database.");
		} finally {
			statement.close();
		}
	}
	
	/**
	 * @param code
	 * @return
	 */
	public int getOfficeCodeId(int code) {
		int id = 0;
		for(OfficeCode officeCode : officeCodesList) {
			if(officeCode.getCode() == code) {
				id = officeCode.getId();
				break;
			}
		}
		return id;
	}
}

class OfficeCode {
	private int id;
	private int code;

	/**
	 * @param id
	 * @param code
	 */
	public OfficeCode(int id, int code) {
		this.id = id;
		this.code = code;
	}

	/**
	 * 
	 */
	public OfficeCode() {
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