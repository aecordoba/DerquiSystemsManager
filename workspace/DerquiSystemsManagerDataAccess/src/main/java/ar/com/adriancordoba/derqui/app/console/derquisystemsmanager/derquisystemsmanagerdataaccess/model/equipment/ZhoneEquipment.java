/*
 * 		ZhoneEquipment.java
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
 * 		ZhoneEquipment.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Sep 13, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment;

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
public class ZhoneEquipment extends Equipment {
	private String cable;
	private String port;
	private Site site;

	private String lastCable;

	private static final Logger logger = LogManager.getLogger(ZhoneEquipment.class);

	/**
	 * 
	 */
	public ZhoneEquipment() {
		super();
	}

	/**
	 * @param id
	 * @param cable
	 * @param port
	 */
	public ZhoneEquipment(int id, String cable, String port, Site site) {
		super(id);
		this.cable = cable;
		this.port = port;
		this.site = site;
	}

	public ZhoneEquipment(ResultSet resultSet) throws SQLException {
		setCommonData(resultSet);
		setPort(resultSet.getString("zhonePort"));
	}

	public ZhoneEquipment(ResultSet resultSet, boolean subscriberData) throws SQLException {
		setCommonData(resultSet);
		if (subscriberData)
			setPort(resultSet.getString("zhonePort"));
		setSite(SitesManager.getSite(resultSet.getInt("zhoneSiteId")));
	}

	private void setCommonData(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("zhoneId"));
		setCable(resultSet.getString("zhoneCable"));
	}

	public void insert(Connection connection) throws Exception {
		// TODO Auto-generated method stub
	}

	public void update(Connection connection) throws Exception {
		// TODO Auto-generated method stub
	}

	public static List<Equipment> getVacantZhoneEquipment(Connection connection, int siteId) throws Exception {
		List<Equipment> vacantEquipmentList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getVacantZhoneEquipmentList(?)}");
			statement.setInt(1, siteId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				ZhoneEquipment equipment = new ZhoneEquipment(resultSet, false);
				equipment.setPort(resultSet.getString("zhonePort"));
				vacantEquipmentList.add(equipment);
			}
			logger.debug(vacantEquipmentList.size() + " ZHONE vacant equipment found in " + SitesManager.getSite(siteId).getName() + ".");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getVacantZhoneEquipmentList().", e);
			throw new Exception("Cannot get Zhone vacant equipment from database.");
		} finally {
			statement.close();
		}
		return vacantEquipmentList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return cable + port;
	}

	/**
	 * @return the cable
	 */
	public String getCable() {
		return cable;
	}

	/**
	 * @param cable the cable to set
	 */
	public void setCable(String cable) {
		this.cable = cable;
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
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
	 * @return the lastCable
	 */
	public String getLastCable() {
		return lastCable;
	}

	/**
	 * @param lastCable the lastCable to set
	 */
	public void setLastCable(String lastCable) {
		this.lastCable = lastCable;
	}
}
