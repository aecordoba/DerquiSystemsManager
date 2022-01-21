/*
 * 		Broadband.java
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
 * 		Broadband.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Oct 2, 2019
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.broadbandtransponder.wiring;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.broadbandtransponder.processors.MainProcessor;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Broadband {
	private int broadbandPortId;
	private String dslam;
	private int slot;
	private int port;
	private String username;
	private String password;

	private static final Logger logger = LogManager.getLogger(Broadband.class);

	@Override
	public String toString() {
		return "Broadband [dslam=" + dslam + ", slot=" + slot + ", port=" + port + ", username=" + username + ", password=" + password + "]";
	}

	/**
	 * @return the broadbandPortId
	 */
	public int getBroadbandPortId() {
		return broadbandPortId;
	}

	/**
	 * @param broadbandPortId the broadbandPortId to set
	 * @throws Exception 
	 */
	public void setBroadbandPortId(Connection connection) throws Exception {
		int broadbandPortId = 0;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getBroadbandPortId(?, ?, ?)}");
			statement.setString(1, String.valueOf(dslam));
			statement.setInt(2, slot);
			statement.setInt(3, (port));
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				broadbandPortId = resultSet.getInt("id");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getBroadbandPortId() for DSLAM " + dslam + " slot " + slot + " pair " + port + ".", e);
			throw new Exception("Cannot get Broadband Port from database.");
		} finally {
			statement.close();
		}
		this.broadbandPortId = broadbandPortId;
	}

	/**
	 * @return the dslam
	 */
	public String getDslam() {
		return dslam;
	}

	/**
	 * @param dslam the dslam to set
	 */
	public void setDslam(String dslam) {
		this.dslam = dslam;
	}

	/**
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * @param slot the slot to set
	 */
	public void setSlot(int slot) {
		this.slot = slot;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
