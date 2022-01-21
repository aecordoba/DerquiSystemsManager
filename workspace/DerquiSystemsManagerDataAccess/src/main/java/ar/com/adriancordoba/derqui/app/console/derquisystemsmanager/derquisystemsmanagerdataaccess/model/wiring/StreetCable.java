/*
 * 		StreetCable.java
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
 * 		StreetCable.java
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

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class StreetCable {
	private int id;
	private StreetFrame streetFrame;
	private char name;
	private int pairs;
	private String description;
	private static final Logger logger = LogManager.getLogger(StreetCable.class);

	/**
	 * 
	 */
	public StreetCable() {
	}

	/**
	 * 
	 * @param id
	 * @param streetFrame
	 * @param name
	 * @param pairs
	 * @param description
	 * @param streetPairsList
	 */
	public StreetCable(int id, StreetFrame streetFrame, char name, int pairs, String description) {
		this.id = id;
		this.streetFrame = streetFrame;
		this.name = name;
		this.pairs = pairs;
		this.description = description;
	}

	public StreetCable(ResultSet resultSet, boolean isSecondPair, boolean wiringData) throws SQLException {
		id = (isSecondPair) ? resultSet.getInt("secondStreetCableId") : resultSet.getInt("streetCableId");
		name = (isSecondPair) ? resultSet.getString("secondStreetCableName").charAt(0) : resultSet.getString("streetCableName").charAt(0);

		if (!wiringData) {
			setPairs(resultSet.getInt("pairs"));
			setDescription(resultSet.getString("streetCableDescription"));
		}

		int streetFrameId = (isSecondPair) ? resultSet.getInt("secondStreetFrameId") : resultSet.getInt("streetFrameId");
		StreetFrame streetFrame = StreetFramesManager.getStreetFrame(streetFrameId);
		if (streetFrame == null) {
			streetFrame = new StreetFrame(resultSet, isSecondPair, wiringData);
			StreetFramesManager.addStreetFrame(streetFrame);
		}
		setStreetFrame(streetFrame);
	}

	public static List<StreetCable> getStreetCablesList(Connection connection) throws Exception {
		List<StreetCable> streetCablesList = new ArrayList<StreetCable>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getStreetCablesList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				StreetCable streetCable = new StreetCable();
				streetCable.setId(resultSet.getInt("streetCableId"));
				streetCable.setName(resultSet.getString("streetCableName").charAt(0));
				streetCable.setPairs(resultSet.getInt("pairs"));
				streetCable.setDescription(resultSet.getString("streetCableDescription"));
				streetCablesList.add(streetCable);
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getStreetCablesList().", e);
			throw new Exception("Cannot get street cables list from database.");
		} finally {
			statement.close();
		}
		return streetCablesList;
	}

	public void insert(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call insertStreetCable(?, ?, ?, ?)}");
			statement.setString(1, String.valueOf(getName()));
			statement.setInt(2, getStreetFrame().getId());
			statement.setInt(3, getPairs());
			statement.setString(4, getDescription());
			statement.execute();
			logger.debug(this + " was inserted in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure insertStreetCable().", e);
			throw new Exception("Cannot insert street cable in database.");
		} finally {
			statement.close();
		}
	}

	public void update(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call updateStreetCable(?, ?)}");
			statement.setInt(1, getId());
			statement.setString(2, getDescription());
			statement.execute();
			logger.debug(this + " was updated in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure updateStreetCable().", e);
			throw new Exception("Cannot update street cable in database.");
		} finally {
			statement.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getStreetFrame().getName());
		stringBuilder.append(getName());
		stringBuilder.append(" (");
		stringBuilder.append(getStreetFrame().getSite().getCode());
		stringBuilder.append(")");
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
	 * @return the frameId
	 */
	public StreetFrame getStreetFrame() {
		return streetFrame;
	}

	/**
	 * @param frameId the frameId to set
	 */
	public void setStreetFrame(StreetFrame streetFrame) {
		this.streetFrame = streetFrame;
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
	 * @return the pairs
	 */
	public int getPairs() {
		return pairs;
	}

	/**
	 * @param pairs the pairs to set
	 */
	public void setPairs(int pairs) {
		this.pairs = pairs;
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

	private static class StreetFramesManager {
		private static List<StreetFrame> streetFramesList = new ArrayList<>();

		public static StreetFrame getStreetFrame(int id) {
			StreetFrame streetFrame = null;
			for (StreetFrame temp : streetFramesList)
				if (temp.getId() == id) {
					streetFrame = temp;
					break;
				}
			return streetFrame;
		}

		public static void addStreetFrame(StreetFrame streetFrame) {
			streetFramesList.add(streetFrame);
		}
	}
}
