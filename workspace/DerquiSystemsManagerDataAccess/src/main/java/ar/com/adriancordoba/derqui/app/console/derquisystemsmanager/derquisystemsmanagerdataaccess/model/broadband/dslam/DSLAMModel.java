/*
 * 		DSLAMModel.java
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
 * 		DSLAMModel.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jan 28, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class DSLAMModel {
	private int id;
	private String name;
	private DSLAMManufacturer manufacturer;
	private List<Integer> slotsList;
	private int ports;

	private static final Logger logger = LogManager.getLogger(DSLAMModel.class);

	/**
	 * 
	 */
	public DSLAMModel() {
	}

	/**
	 * @param name
	 * @param manufacturer
	 * @param slotsList
	 * @param ports
	 */
	public DSLAMModel(String name, DSLAMManufacturer manufacturer, List<Integer> slotsList, int ports) {
		this.name = name;
		this.manufacturer = manufacturer;
		this.slotsList = slotsList;
		this.ports = ports;
	}

	public DSLAMModel(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("dslamModelId"));
		setName(resultSet.getString("dslamModelName"));
		setSlotsList(resultSet.getString("dslamModelSlots"));
		setPorts(resultSet.getInt("dslamModelPorts"));
		int dslamManufacturerId = resultSet.getInt("dslamManufacturerId");
		DSLAMManufacturer dslamManufacturer = DSLAMsManufacturersManager.getDSLAMManufacturer(dslamManufacturerId);
		if (dslamManufacturer == null) {
			dslamManufacturer = new DSLAMManufacturer(resultSet, false);
			DSLAMsManufacturersManager.addDSLAMManufacturer(dslamManufacturer);
		}
		setManufacturer(dslamManufacturer);
	}

	public static List<DSLAMModel> getDSLAMsModelsList(Connection connection) throws Exception {
		List<DSLAMModel> dslamsModelsList = null;
		CallableStatement statement = null;
		DSLAMsManufacturersManager.clearDSLAMsManufacturersList();
		try {
			statement = connection.prepareCall("{call getDSLAMsModelsList()}");
			ResultSet resultSet = statement.executeQuery();
			dslamsModelsList = processResultSet(resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getDSLAMsModelsList().", e);
			throw new Exception("Cannot get DSLAMs models list from database.");
		} finally {
			statement.close();
		}
		return dslamsModelsList;
	}

	private static List<DSLAMModel> processResultSet(ResultSet resultSet) throws SQLException {
		List<DSLAMModel> dslamsModelsList = new ArrayList<>();
		while (resultSet.next()) {
			DSLAMModel dslamModel = new DSLAMModel(resultSet);
			dslamsModelsList.add(dslamModel);
		}
		return dslamsModelsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
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
	 * @return the manufacturer
	 */
	public DSLAMManufacturer getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param manufacturer the manufacturer to set
	 */
	public void setManufacturer(DSLAMManufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * @return the slotsList
	 */
	public List<Integer> getSlotsList() {
		return slotsList;
	}

	/**
	 * @param slotsList the slotsList to set
	 */
	public void setSlotsList(List<Integer> slotsList) {
		this.slotsList = slotsList;
	}

	public void setSlotsList(String slotsString) {
		slotsList = new ArrayList<>();
		String[] slotsArray = slotsString.split("-");
		for (String slotString : slotsArray) {
			int slot = Integer.parseInt(slotString);
			slotsList.add(slot);
		}
	}

	/**
	 * @return the ports
	 */
	public int getPorts() {
		return ports;
	}

	/**
	 * @param ports the ports to set
	 */
	public void setPorts(int ports) {
		this.ports = ports;
	}

	public static List<DSLAMManufacturer> GetDSLAMsManufacturersList() {
		return DSLAMsManufacturersManager.getDSLAMsManufacturersList();
	}

	public static class DSLAMsManufacturersManager {
		private static List<DSLAMManufacturer> dslamsManufacturersList = new ArrayList<>();

		public static DSLAMManufacturer getDSLAMManufacturer(int id) {
			DSLAMManufacturer dslamManufacturer = null;
			for (DSLAMManufacturer temp : dslamsManufacturersList) {
				if (temp.getId() == id) {
					dslamManufacturer = temp;
					break;
				}
			}
			return dslamManufacturer;
		}

		public static void clearDSLAMsManufacturersList() {
			dslamsManufacturersList.clear();
		}

		public static void addDSLAMManufacturer(DSLAMManufacturer dslamManufacturer) {
			dslamsManufacturersList.add(dslamManufacturer);
		}

		public static List<DSLAMManufacturer> getDSLAMsManufacturersList() {
			return dslamsManufacturersList;
		}
	}
}
