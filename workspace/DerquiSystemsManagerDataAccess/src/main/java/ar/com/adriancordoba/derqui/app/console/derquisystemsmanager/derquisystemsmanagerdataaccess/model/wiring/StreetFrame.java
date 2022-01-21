/*
 * 		StreetFrame.java
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
 * 		StreetFrame.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Aug 22, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Site;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.managers.SitesManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class StreetFrame {
	private int id;
	private Site site;
	private char name;
	private String description;

	private static final Logger logger = LogManager.getLogger(StreetFrame.class);

	/**
	 * 
	 */
	public StreetFrame() {
	}

	/**
	 * @param id
	 * @param site
	 * @param name
	 * @param description
	 * @param streetCablesList
	 */
	public StreetFrame(int id, Site site, char name, String description) {
		this.id = id;
		this.site = site;
		this.name = name;
		this.description = description;
	}

	public StreetFrame(ResultSet resultSet, boolean isSecondPair, boolean wiringData) throws SQLException {
		id = (isSecondPair) ? resultSet.getInt("secondStreetFrameId") : resultSet.getInt("streetFrameId");
		name = (isSecondPair) ? resultSet.getString("secondStreetFrameName").charAt(0) : resultSet.getString("streetFrameName").charAt(0);
		if (!wiringData)
			setDescription(resultSet.getString("streetFrameDescription"));
		setSite(SitesManager.getSite(resultSet.getInt("siteId")));
	}

	public static List<StreetFrame> getStreetFramesList(Connection connection) throws Exception {
		List<StreetFrame> streetFramesList = new ArrayList<StreetFrame>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getStreetFramesList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				StreetFrame streetFrame = new StreetFrame();
				streetFrame.setId(resultSet.getInt("streetFrameId"));
				streetFrame.setName(resultSet.getString("streetFrameName").charAt(0));
				streetFrame.setDescription(resultSet.getString("streetFrameDescription"));
				streetFrame.setSite(SitesManager.getSite(resultSet.getInt("siteId")));
				streetFramesList.add(streetFrame);
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getStreetFramesList().", e);
			throw new Exception("Cannot get street frames list from database.");
		} finally {
			statement.close();
		}
		return streetFramesList;
	}

	public String toString() {
		return String.valueOf(name);
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
	 * @return the site
	 */
	public Site getSite() {
		return site;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(Site site) {
		this.site = site;
	}

	/**
	 * @return the name
	 */
	public char getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(char name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
