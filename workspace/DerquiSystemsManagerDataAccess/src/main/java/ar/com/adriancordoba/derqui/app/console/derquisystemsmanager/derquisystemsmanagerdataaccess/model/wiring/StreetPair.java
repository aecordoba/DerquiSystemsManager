/*
 * 		StreetPair.java
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
 * 		StreetPair.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Aug 23, 2016
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

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class StreetPair {
	private int id;
	private StreetCable streetCable;
	private int pair;
	private boolean available;
	private String remarks;
	private int wiringId; // Pair used.

	private static final Logger logger = LogManager.getLogger(StreetPair.class);

	/**
	 * 
	 */
	public StreetPair() {
	}

	/**
	 * @param id
	 * @param cableId
	 * @param pair
	 * @param available
	 * @param rmarks
	 * @param cable
	 */
	public StreetPair(int id, StreetCable streetCable, int pair, boolean available, String remarks, int wiringId) {
		this.id = id;
		this.streetCable = streetCable;
		this.pair = pair;
		this.available = available;
		this.remarks = remarks;
		this.wiringId = wiringId;
	}

	public StreetPair(ResultSet resultSet, boolean isSecondPair) throws SQLException {
		id = (isSecondPair) ? resultSet.getInt("secondStreetPairId") : resultSet.getInt("streetPairId");

		int streetCableId = (isSecondPair) ? resultSet.getInt("secondStreetCableId") : resultSet.getInt("streetCableId");
		StreetCable streetCable = StreetCablesManager.getStreetCable(streetCableId);
		if (streetCable == null) {
			streetCable = new StreetCable(resultSet, isSecondPair, true);
			StreetCablesManager.addStreetCable(streetCable);
		}
		setStreetCable(streetCable);

		pair = (isSecondPair) ? resultSet.getInt("secondStreetPairsPair") : resultSet.getInt("streetPairsPair");
	}

	public static List<StreetPair> getStreetPairsList(Connection connection) throws Exception {
		List<StreetPair> streetPairsList = new ArrayList<>();
		StreetCablesManager.clearStreetCablesList();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getStreetPairsList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				StreetPair streetPair = new StreetPair();
				streetPair.setId(resultSet.getInt("streetPairId"));
				int streetCableId = resultSet.getInt("streetCableId");
				StreetCable streetCable = StreetCablesManager.getStreetCable(streetCableId);
				if (streetCable == null) {
					streetCable = new StreetCable(resultSet, false, false);
					StreetCablesManager.addStreetCable(streetCable);
				}
				streetPair.setStreetCable(streetCable);

				streetPair.setPair(resultSet.getInt("pair"));
				streetPair.setAvailable(resultSet.getBoolean("available"));
				streetPair.setRemarks(resultSet.getString("remarks"));
				streetPair.setWiringId(resultSet.getInt("wiringId"));
				streetPairsList.add(streetPair);
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getStreetPairsList().", e);
			throw new Exception("Cannot get street pairs list from database.");
		} finally {
			statement.close();
		}
		return streetPairsList;
	}

	public void update(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call updateStreetPair(?, ?, ?)}");
			statement.setInt(1, getId());
			statement.setBoolean(2, isAvailable());
			statement.setString(3, getRemarks());
			statement.execute();
			logger.debug(this + " was updated in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure updateStreetPair().", e);
			throw new Exception("Cannot update street pair in database.");
		} finally {
			statement.close();
		}
	}

	public static List<StreetCable> getStreetCablesList() {
		return StreetCablesManager.getStreetCablesList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StreetFrame frame = getStreetCable().getStreetFrame();
		StringBuilder stringBuilder = new StringBuilder("Site ");
		stringBuilder.append(frame.getSite().getCode());
		stringBuilder.append(" Frame ");
		stringBuilder.append(frame.getName());
		stringBuilder.append(" Cable ");
		stringBuilder.append(getStreetCable().getName());
		stringBuilder.append(" Pair ");
		stringBuilder.append(getPair());
		return stringBuilder.toString();
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
	 * @return the cableId
	 */
	public StreetCable getStreetCable() {
		return streetCable;
	}

	/**
	 * @param cableId the cableId to set
	 */
	public void setStreetCable(StreetCable streetCable) {
		this.streetCable = streetCable;
	}

	/**
	 * @return the pair
	 */
	public int getPair() {
		return pair;
	}

	/**
	 * @param pair the pair to set
	 */
	public void setPair(int pair) {
		this.pair = pair;
	}

	/**
	 * @return the available
	 */
	public boolean isAvailable() {
		return available;
	}

	/**
	 * @param available the available to set
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the wiringId
	 */
	public int getWiringId() {
		return wiringId;
	}

	/**
	 * @param wiringId the wiringId to set
	 */
	public void setWiringId(int wiringId) {
		this.wiringId = wiringId;
	}

	private static class StreetCablesManager {
		private static List<StreetCable> streetCablesList = new ArrayList<>();

		public static StreetCable getStreetCable(int id) {
			StreetCable streetCable = null;
			for (StreetCable temp : streetCablesList)
				if (temp.getId() == id) {
					streetCable = temp;
					break;
				}
			return streetCable;
		}

		private static void clearStreetCablesList() {
			streetCablesList.clear();
		}

		public static void addStreetCable(StreetCable streetCable) {
			streetCablesList.add(streetCable);
		}

		/**
		 * @return the streetCablesList
		 */
		public static List<StreetCable> getStreetCablesList() {
			return streetCablesList;
		}
	}
}
