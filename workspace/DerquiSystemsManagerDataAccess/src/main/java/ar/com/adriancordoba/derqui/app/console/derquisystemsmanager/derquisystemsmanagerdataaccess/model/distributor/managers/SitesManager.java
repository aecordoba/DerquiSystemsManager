/*
 * 		SitesManager.java
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
 * 		SitesManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Sep 17, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.managers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Distributor;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Site;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class SitesManager {
	private static List<Site> sitesList = new ArrayList<>();
	private static final Logger logger = LogManager.getLogger(SitesManager.class);

	static {
		fillSitesList();
	}

	public static void fillSitesList() {
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		try {
			databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
			connection = databaseConnectionsManager.takeConnection();
			sitesList = Site.getSitesList(connection);
			logger.debug(sitesList.size() + " site(s) found.");
		} catch (Exception ex) {
			logger.error("SitesManager couldn't fill sites list.");
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
	}

	public static Site getSite(int id) {
		Site site = null;
		for (Site temp : sitesList)
			if (temp.getId() == id) {
				site = temp;
				break;
			}
		return site;
	}

	private static void clearSitesList() {
		sitesList.clear();
	}

	public static void addSite(Site site) {
		sitesList.add(site);
	}

	/**
	 * @return the streetCablesList
	 */
	public static List<Site> getSitesList() {
		return sitesList;
	}
}
