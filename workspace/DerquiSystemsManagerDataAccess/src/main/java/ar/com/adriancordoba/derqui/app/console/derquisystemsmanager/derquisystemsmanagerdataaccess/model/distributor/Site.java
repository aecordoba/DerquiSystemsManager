/*
 * 		Site.java
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
 * 		Site.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Aug 22, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors.Technology;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Site {
	private int id;
	private String name;
	private String code;
	private List<Technology> technologiesList = new ArrayList<>();

	private static final Logger logger = LogManager.getLogger(Site.class);

	/**
	 * 
	 */
	public Site() {
	}

	/**
	 * @param id
	 * @param name
	 * @param code
	 */
	public Site(int id, String name, String code) {
		this.id = id;
		this.name = name;
		this.code = code;
	}

	public static List<Site> getSitesList(Connection connection) throws Exception {
		List<Site> sitesList = new ArrayList<Site>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getSitesList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Site site = new Site();
				site.setId(resultSet.getInt("siteId"));
				site.setName(resultSet.getString("siteName"));
				site.setCode(resultSet.getString("siteCode"));

				List<Technology> technologiesList = new ArrayList<>();
				if (resultSet.getBoolean("NEAX61E"))
					technologiesList.add(Technology.NEAX61E);
				if (resultSet.getBoolean("NEAX61SIGMA"))
					technologiesList.add(Technology.NEAX61SIGMA);
				if (resultSet.getBoolean("NEAX61SIGMAELU"))
					technologiesList.add(Technology.NEAX61SIGMA_ELU);
				if (resultSet.getBoolean("ZHONE"))
					technologiesList.add(Technology.ZHONE);
				site.setTechnologiesList(technologiesList);

				sitesList.add(site);
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getSitesList().", e);
			throw new Exception("Cannot get sites list from database.");
		} finally {
			statement.close();
		}
		return sitesList;
	}

	public String toString() {
		return code;
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
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the technologiesList
	 */
	public List<Technology> getTechnologiesList() {
		return technologiesList;
	}

	/**
	 * @param technologiesList the technologiesList to set
	 */
	public void setTechnologiesList(List<Technology> technologiesList) {
		this.technologiesList = technologiesList;
	}
}
