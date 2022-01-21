/*
 * 		DSLAMBoardModel.java
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
 * 		DSLAMBoardModel.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Feb 8, 2017
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

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMModel.DSLAMsManufacturersManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class DSLAMBoardModel {
	private int id;
	private String name;
	private DSLAMManufacturer manufacturer;
	private int ports;
	private String description;

	private static final Logger logger = LogManager.getLogger(DSLAMBoardModel.class);

	/**
	 * 
	 */
	public DSLAMBoardModel() {
	}

	/**
	 * @param name
	 * @param manufacturer
	 * @param ports
	 * @param description
	 */
	public DSLAMBoardModel(String name, DSLAMManufacturer manufacturer, int ports, String description) {
		this.name = name;
		this.manufacturer = manufacturer;
		this.ports = ports;
		this.description = description;
	}

	public DSLAMBoardModel(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("boardModelId"));
		setName(resultSet.getString("boardModelName"));

		int boardManufacturerId = resultSet.getInt("boardManufacturerId");
		DSLAMManufacturer dslamBoardModelManufacturer = DSLAMsManufacturersManager.getDSLAMManufacturer(boardManufacturerId);
		if (dslamBoardModelManufacturer == null) {
			dslamBoardModelManufacturer = new DSLAMManufacturer(resultSet, true);
			DSLAMsManufacturersManager.addDSLAMManufacturer(dslamBoardModelManufacturer);
		}
		setManufacturer(dslamBoardModelManufacturer);

		setPorts(resultSet.getInt("boardModelPorts"));
		setDescription(resultSet.getString("boardModelDescription"));
	}

	public static List<DSLAMBoardModel> getDSLAMsBoardsModelsList(Connection connection) throws Exception {
		List<DSLAMBoardModel> dslamsBoardsModelsList = null;
		CallableStatement statement = null;
		// DSLAMsManufacturersManager.clearDSLAMsManufacturersList();
		try {
			statement = connection.prepareCall("{call getDSLAMsBoardsModelsList()}");
			ResultSet resultSet = statement.executeQuery();
			dslamsBoardsModelsList = processResultSet(resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getDSLAMsBoardsModelsList().", e);
			throw new Exception("Cannot get DSLAMs boards models list from database.");
		} finally {
			statement.close();
		}
		return dslamsBoardsModelsList;
	}

	private static List<DSLAMBoardModel> processResultSet(ResultSet resultSet) throws SQLException {
		List<DSLAMBoardModel> dslamsBoardsModelsList = new ArrayList<>();
		while (resultSet.next()) {
			DSLAMBoardModel dslamBoardModel = new DSLAMBoardModel();
			dslamBoardModel.setId(resultSet.getInt("dslamBoardModelId"));
			dslamBoardModel.setName(resultSet.getString("dslamBoardModelName"));
			int dslamBoardModelManufacturerId = resultSet.getInt("dslamBoardModelManufacturerId");
			DSLAMManufacturer dslamManufacturer = DSLAMsManufacturersManager.getDSLAMManufacturer(dslamBoardModelManufacturerId);
			if (dslamManufacturer == null) {
				dslamManufacturer = new DSLAMManufacturer(resultSet, true);
				DSLAMsManufacturersManager.addDSLAMManufacturer(dslamManufacturer);
			}
			dslamBoardModel.setManufacturer(dslamManufacturer);
			dslamBoardModel.setPorts(resultSet.getInt("dslamBoardModelPorts"));
			dslamBoardModel.setDescription(resultSet.getString("dslamBoardModelDescription"));
			dslamsBoardsModelsList.add(dslamBoardModel);
		}
		return dslamsBoardsModelsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
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
