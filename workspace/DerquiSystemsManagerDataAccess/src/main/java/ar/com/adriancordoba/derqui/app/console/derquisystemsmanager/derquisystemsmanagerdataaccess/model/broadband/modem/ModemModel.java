/*
 * 		ModemModel.java
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
 * 		ModemModel.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Apr 5, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.modem;

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
public class ModemModel {
	private int id;
	private String name;
	private ModemType type;
	private ModemManufacturer manufacturer;

	private static final Logger logger = LogManager.getLogger(ModemModel.class);

	/**
	 * 
	 */
	public ModemModel() {
	}

	/**
	 * @param name
	 * @param type
	 * @param manufacturer
	 */
	public ModemModel(String name, ModemType type, ModemManufacturer manufacturer) {
		this.name = name;
		this.type = type;
		this.manufacturer = manufacturer;
	}

	public ModemModel(ResultSet resultSet) throws SQLException{
		setId(resultSet.getInt("modemModelId"));
		setName(resultSet.getString("modemModelName"));
		
		int modemTypeId = resultSet.getInt("modemTypeId");
		ModemType modemType = ModemsTypesManager.getModemType(modemTypeId);
		if (modemType == null) {
			modemType = new ModemType(resultSet);
			ModemsTypesManager.addModemType(modemType);
		}
		setType(modemType);
	}
	
	public static List<ModemModel> getModemsModelsList(Connection connection) throws Exception {
		List<ModemModel> modemsModelsList = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getModemsModelsList()}");
			ResultSet resultSet = statement.executeQuery();
			modemsModelsList = processResultSet(resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getModemsModelsList().", e);
			throw new Exception("Cannot get modems models list from database.");
		} finally {
			statement.close();
		}
		return modemsModelsList;
	}

	private static List<ModemModel> processResultSet(ResultSet resultSet) throws SQLException {
		List<ModemModel> modemsModelsList = new ArrayList<>();
		while (resultSet.next()) {
			ModemModel modemModel = new ModemModel(resultSet);

			int manufacturerId = resultSet.getInt("modemManufacturerId");
			ModemManufacturer modemManufacturer = ModemsManufacturersManager.getModemManufacturer(manufacturerId);
			if (modemManufacturer == null) {
				modemManufacturer = new ModemManufacturer(resultSet);
				ModemsManufacturersManager.addModemManufacturer(modemManufacturer);
			}
			modemModel.setManufacturer(modemManufacturer);

			modemsModelsList.add(modemModel);
		}
		return modemsModelsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		if (manufacturer != null) {
			str.append(manufacturer.toString());
			str.append(" ");
		}
		str.append(getName());
		if (type != null) {
			str.append(" (");
			str.append(type.toString());
			str.append(")");
		}
		return str.toString();
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
	 * @return the type
	 */
	public ModemType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ModemType type) {
		this.type = type;
	}

	/**
	 * @return the manufacturer
	 */
	public ModemManufacturer getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param manufacturer the manufacturer to set
	 */
	public void setManufacturer(ModemManufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	private static class ModemsTypesManager {
		private static List<ModemType> modemsTypesList = new ArrayList<>();

		public static ModemType getModemType(int id) {
			ModemType modemType = null;
			for (ModemType temp : modemsTypesList) {
				if (temp.getId() == id) {
					modemType = temp;
					break;
				}
			}
			return modemType;
		}

		public static void clearModemsTypesList() {
			modemsTypesList.clear();
		}

		public static void addModemType(ModemType modemType) {
			modemsTypesList.add(modemType);
		}

		public static List<ModemType> getModemsTypesList() {
			return modemsTypesList;
		}
	}

	private static class ModemsManufacturersManager {
		private static List<ModemManufacturer> modemsManufacturersList = new ArrayList<>();

		public static ModemManufacturer getModemManufacturer(int id) {
			ModemManufacturer modemManufacturer = null;
			for (ModemManufacturer temp : modemsManufacturersList) {
				if (temp.getId() == id) {
					modemManufacturer = temp;
					break;
				}
			}
			return modemManufacturer;
		}

		public static void clearModemsManufacturersList() {
			modemsManufacturersList.clear();
		}

		public static void addModemManufacturer(ModemManufacturer modemManufacturer) {
			modemsManufacturersList.add(modemManufacturer);
		}

		public static List<ModemManufacturer> getModemsManufacturersList() {
			return modemsManufacturersList;
		}
	}
}
