/*
 * 		Neax61EEquipment.java
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
 * 		Neax61EEquipment.java
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
public class Neax61EEquipment extends Equipment {
	private String spce;
	private char highway;
	private char subhighway;
	private String group;
	private char sw;
	private char level;
	private ELineModule eLineModule;

	private String lastGroup;

	private static final Logger logger = LogManager.getLogger(Neax61EEquipment.class);

	/**
	 * 
	 */
	public Neax61EEquipment() {
		super();
	}

	/**
	 * @param id
	 * @param spce
	 * @param highway
	 * @param subhighway
	 * @param group
	 * @param sw
	 * @param level
	 * @param eLineModule
	 */
	public Neax61EEquipment(int id, String spce, char highway, char subhighway, String group, char sw, char level, ELineModule eLineModule) {
		super(id);
		this.spce = spce;
		this.highway = highway;
		this.subhighway = subhighway;
		this.group = group;
		this.sw = sw;
		this.level = level;
		this.eLineModule = eLineModule;
	}

	public Neax61EEquipment(ResultSet resultSet) throws SQLException {
		setCommonData(resultSet);
		setSw(resultSet.getString("switch").charAt(0));
		setLevel(resultSet.getString("level").charAt(0));
	}

	public Neax61EEquipment(ResultSet resultSet, boolean subscriberData) throws SQLException {
		setCommonData(resultSet);
		if (subscriberData) {
			setSw(resultSet.getString("switch").charAt(0));
			setLevel(resultSet.getString("level").charAt(0));
		}
		setDistributorData(resultSet);
	}

	private void setCommonData(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("neax61eId"));
		setSpce(resultSet.getString("spce"));
		setHighway(resultSet.getString("highway").charAt(0));
		setSubhighway(resultSet.getString("subhighway").charAt(0));
		setGroup(resultSet.getString("group"));
	}

	private void setDistributorData(ResultSet resultSet) throws SQLException {
		ELineModule lineModule = ELineModulesManager.getELineModule(resultSet.getInt("eLineModuleId"));
		if (lineModule == null) {
			lineModule = new ELineModule(resultSet);
			ELineModulesManager.addELineModule(lineModule);
		}
		setELineModule(lineModule);
	}

	public static List<Equipment> getVacantNeax61EEquipment(Connection connection, int siteId) throws Exception {
		List<Equipment> vacantEquipmentList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getVacantNeax61EEquipmentList(?)}");
			statement.setInt(1, siteId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Neax61EEquipment equipment = new Neax61EEquipment(resultSet, false);
				equipment.setSw(resultSet.getString("switch").charAt(0));
				equipment.setLevel(resultSet.getString("level").charAt(0));
				vacantEquipmentList.add(equipment);
			}
			logger.debug(vacantEquipmentList.size() + " NEAX61E vacant equipment found in " + SitesManager.getSite(siteId).getName() + ".");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getVacantNeax61EEquipmentList().", e);
			throw new Exception("Cannot get NEAX61E vacant equipment from database.");
		} finally {
			statement.close();
		}
		return vacantEquipmentList;
	}

	public void insert(Connection connection) throws Exception {
		// TODO Auto-generated method stub

	}

	public void update(Connection connection) throws Exception {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.
	 * derquisystemsmanagerdataaccess.model.Equipment#getSite()
	 */
	@Override
	public Site getSite() {
		return eLineModule.getFrame().getSite();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return spce + highway + subhighway + group + sw + level;
	}

	/**
	 * @return the spce
	 */
	public String getSpce() {
		return spce;
	}

	/**
	 * @param spce the spce to set
	 */
	public void setSpce(String spce) {
		this.spce = spce;
	}

	/**
	 * @return the highway
	 */
	public char getHighway() {
		return highway;
	}

	/**
	 * @param highway the highway to set
	 */
	public void setHighway(char highway) {
		this.highway = highway;
	}

	/**
	 * @return the subhighway
	 */
	public char getSubhighway() {
		return subhighway;
	}

	/**
	 * @param subhighway the subhighway to set
	 */
	public void setSubhighway(char subhighway) {
		this.subhighway = subhighway;
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the sw
	 */
	public char getSw() {
		return sw;
	}

	/**
	 * @param sw the sw to set
	 */
	public void setSw(char sw) {
		this.sw = sw;
	}

	/**
	 * @return the level
	 */
	public char getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(char level) {
		this.level = level;
	}

	/**
	 * @return the eLineModule
	 */
	public ELineModule getELineModule() {
		return eLineModule;
	}

	/**
	 * @param eLineModule the eLineModule to set
	 */
	public void setELineModule(ELineModule eLineModule) {
		this.eLineModule = eLineModule;
	}

	/**
	 * @return the lastGroup
	 */
	public String getLastGroup() {
		return lastGroup;
	}

	/**
	 * @param lastGroup the lastGroup to set
	 */
	public void setLastGroup(String lastGroup) {
		this.lastGroup = lastGroup;
	}

	private static class ELineModulesManager {
		private static List<ELineModule> eLineModulesList = new ArrayList<>();

		public static ELineModule getELineModule(int id) {
			ELineModule eLineModule = null;
			for (ELineModule temp : eLineModulesList)
				if (temp.getId() == id) {
					eLineModule = temp;
					break;
				}
			return eLineModule;
		}

		private static void clearELineModulesList() {
			eLineModulesList.clear();
		}

		public static void addELineModule(ELineModule eLineModule) {
			eLineModulesList.add(eLineModule);
		}

		/**
		 * @return the streetCablesList
		 */
		public static List<ELineModule> getELineModulesList() {
			return eLineModulesList;
		}
	}
}
