/*
 * 		Subscriber.java
 *   Copyright (C) 2019  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		Subscriber.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Nov 6, 2019
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.restrictionstransponder.processors.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Subscriber {
	private String number;
	private int officeCodeId;
	private String mcdu;
	private String restriction;
	private static final Logger logger = LogManager.getLogger(Subscriber.class);

	public void update(Connection connection, RestrictionsManager restrictionsManager) throws Exception {
		int[] indexes = getSubscriberDataId(connection);

		if (indexes[0] != 0) {
			CallableStatement statement = null;
			try {
				statement = connection.prepareCall("{call updateSubscriberData(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

				statement.setInt(1, indexes[0]);
				statement.setInt(2, 1);
				statement.setInt(3, restrictionsManager.getRestrictionId(restriction));
				statement.setInt(4, 2);

				if (indexes[1] == 0)
					statement.setInt(5, 0);
				else
					statement.setInt(5, 2);

				statement.setString(6, null);
				statement.setBoolean(7, false);
				statement.setBoolean(8, false);
				statement.setBoolean(9, false);
				statement.setBoolean(10, false);

				statement.setInt(11, 1);

				statement.execute();
			} catch (SQLException e) {
				logger.error("Cannot execute stored procedure updateSubscriberData() for " + number + ".");
			} catch (Exception e) {
				logger.warn("Cannot get restriction for " + number + " with restriction " + restriction + ".");
			} finally {

				statement.close();
			}
		} else
			logger.warn("Not assigned subscriber: " + number + ".");
	}

	private int[] getSubscriberDataId(Connection connection) throws SQLException {
		int[] indexes = new int[3];
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getSubscriberDataId(?, ?)}");
			statement.setInt(1, officeCodeId);
			statement.setString(2, mcdu);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				indexes[0] = resultSet.getInt("dataId");
				indexes[1] = resultSet.getInt("broadbandId");
				indexes[2] = resultSet.getInt("broadbandStateId");
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getSubscriberDataId() for " + number + ".", e);
		} finally {
			statement.close();
		}
		return indexes;
	}

	@Override
	public String toString() {
		return "Subscriber [number=" + number + ", officeCodeId=" + officeCodeId + ", mcdu=" + mcdu + ", restriction=" + restriction + "]";
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
		String officeCode = number.substring(0, 3);
		if (officeCode.equals("448"))
			officeCodeId = 1;
		if (officeCode.equals("447"))
			officeCodeId = 2;
		if (officeCode.equals("449"))
			officeCodeId = 3;
		if (officeCode.equals("454"))
			officeCodeId = 4;
		mcdu = number.substring(3);
	}

	/**
	 * @return the restriction
	 */
	public String getRestriction() {
		return restriction;
	}

	/**
	 * @param restriction the restriction to set
	 */
	public void setRestriction(String restriction) {
		this.restriction = restriction;
	}

	/**
	 * @return the officeCodeId
	 */
	public int getOfficeCodeId() {
		return officeCodeId;
	}

	/**
	 * @return the mcdu
	 */
	public String getMcdu() {
		return mcdu;
	}
}
