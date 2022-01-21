/*
 * 		Neax61SigmaEquipment.java
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
 * 		Neax61SigmaEquipment.java
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
public class Neax61SigmaEquipment extends Equipment {
	private String timeSwitch;
	private String kHighway;
	private String pHighway;
	private char row;
	private String column;
	private SigmaLineModule sigmaLineModule;

	private char lastRow;

	private static final Logger logger = LogManager.getLogger(Neax61SigmaEquipment.class);

	/**
	 * 
	 */
	public Neax61SigmaEquipment() {
		super();
	}

	/**
	 * @param id
	 * @param timeSwitch
	 * @param kHighway
	 * @param pHighway
	 * @param row
	 * @param column
	 * @param lineModule
	 */
	public Neax61SigmaEquipment(int id, String timeSwitch, String kHighway, String pHighway, char row, String column, SigmaLineModule sigmaLineModule) {
		super(id);
		this.timeSwitch = timeSwitch;
		this.kHighway = kHighway;
		this.pHighway = pHighway;
		this.row = row;
		this.column = column;
		this.sigmaLineModule = sigmaLineModule;
	}

	public Neax61SigmaEquipment(ResultSet resultSet) throws SQLException {
		setCommonData(resultSet);
		setColumn(resultSet.getString("column"));
	}

	public Neax61SigmaEquipment(ResultSet resultSet, boolean subscriberData) throws SQLException {
		setCommonData(resultSet);
		if (subscriberData)
			setColumn(resultSet.getString("column"));
		setDistributorData(resultSet);
	}

	private void setCommonData(ResultSet resultSet) throws SQLException{
		setId(resultSet.getInt("neax61sigmaId"));
		setTimeSwitch(resultSet.getString("timeSwitch"));
		setkHighway(resultSet.getString("kHighway"));
		setpHighway(resultSet.getString("pHighway"));
		setRow(resultSet.getString("row").charAt(0));
	}
	
	private void setDistributorData(ResultSet resultSet) throws SQLException {
		SigmaLineModule sigmaLineModule = SigmaLineModulesManager.getSigmaLineModule(resultSet.getInt("sigmaLineModuleId"));
		if (sigmaLineModule == null) {
			sigmaLineModule = new SigmaLineModule(resultSet);
			SigmaLineModulesManager.addSigmaLineModule(sigmaLineModule);
		}
		setSigmaLineModule(sigmaLineModule);
	}

	public static List<Equipment> getVacantNeax61SimaEquipment(Connection connection, int siteId) throws Exception {
		List<Equipment> vacantEquipmentList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getVacantNeax61SigmaEquipmentList(?)}");
			statement.setInt(1, siteId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Neax61SigmaEquipment equipment = new Neax61SigmaEquipment(resultSet, false);
				equipment.setColumn(resultSet.getString("column"));
				vacantEquipmentList.add(equipment);
			}
			logger.debug(vacantEquipmentList.size() + " NEAX61Σ vacant equipment found in " + SitesManager.getSite(siteId).getName() + ".");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getVacantNeax61SigmaEquipmentList().", e);
			throw new Exception("Cannot get NEAX61Sigma vacant equipment from database.");
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
		return sigmaLineModule.getFrame().getSite();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return timeSwitch + kHighway + pHighway + row + column;
	}

	/**
	 * @return the timeSwitch
	 */
	public String getTimeSwitch() {
		return timeSwitch;
	}

	/**
	 * @param timeSwitch the timeSwitch to set
	 */
	public void setTimeSwitch(String timeSwitch) {
		this.timeSwitch = timeSwitch;
	}

	/**
	 * @return the kHighway
	 */
	public String getkHighway() {
		return kHighway;
	}

	/**
	 * @param kHighway the kHighway to set
	 */
	public void setkHighway(String kHighway) {
		this.kHighway = kHighway;
	}

	/**
	 * @return the pHighway
	 */
	public String getpHighway() {
		return pHighway;
	}

	/**
	 * @param pHighway the pHighway to set
	 */
	public void setpHighway(String pHighway) {
		this.pHighway = pHighway;
	}

	/**
	 * @return the row
	 */
	public char getRow() {
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(char row) {
		this.row = row;
	}

	/**
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(String column) {
		this.column = column;
	}

	/**
	 * @return the lastRow
	 */
	public char getLastRow() {
		return lastRow;
	}

	/**
	 * @param lastRow the lastRow to set
	 */
	public void setLastRow(char lastRow) {
		this.lastRow = lastRow;
	}

	/**
	 * @return the lineModule
	 */
	public SigmaLineModule getSigmaLineModule() {
		return sigmaLineModule;
	}

	/**
	 * @param lineModule the lineModule to set
	 */
	public void setSigmaLineModule(SigmaLineModule sigmaLineModule) {
		this.sigmaLineModule = sigmaLineModule;
	}

	private static class SigmaLineModulesManager {
		private static List<SigmaLineModule> SigmaLineModulesList = new ArrayList<>();

		public static SigmaLineModule getSigmaLineModule(int id) {
			SigmaLineModule sigmaLineModule = null;
			for (SigmaLineModule temp : SigmaLineModulesList)
				if (temp.getId() == id) {
					sigmaLineModule = temp;
					break;
				}
			return sigmaLineModule;
		}

		private static void clearSigmaLineModulesList() {
			SigmaLineModulesList.clear();
		}

		public static void addSigmaLineModule(SigmaLineModule sigmaLineModule) {
			SigmaLineModulesList.add(sigmaLineModule);
		}

		/**
		 * @return the streetCablesList
		 */
		public static List<SigmaLineModule> getSigmaLineModulesList() {
			return SigmaLineModulesList;
		}
	}
}
