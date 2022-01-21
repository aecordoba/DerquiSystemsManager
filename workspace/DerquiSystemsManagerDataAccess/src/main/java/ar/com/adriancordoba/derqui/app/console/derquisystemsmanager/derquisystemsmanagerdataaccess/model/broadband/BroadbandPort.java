/*
 * 		BroadbandPort.java
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
 * 		BroadbandPort.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Mar 28, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband;

import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAM;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMBoard;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.managers.DSLAMsBoardsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.managers.DSLAMsManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class BroadbandPort {
	private int id;
	private int port;
	private DSLAMBoard dslamBoard;
	private boolean available;

	private static final Logger logger = LogManager.getLogger(BroadbandPort.class);

	/**
	 * 
	 */
	public BroadbandPort() {
	}

	/**
	 * @param port
	 * @param dslamBoard
	 * @param available
	 */
	public BroadbandPort(int port, DSLAMBoard dslamBoard, boolean available) {
		this.port = port;
		this.dslamBoard = dslamBoard;
		this.available = available;
	}

	public BroadbandPort(ResultSet resultSet) throws SQLException, UnknownHostException {
		setId(resultSet.getInt("broadbandPortId"));
		setPort(resultSet.getInt("broadbandPort"));

		int broadbandBoardId = resultSet.getInt("boardId");
		DSLAMBoard dslamBoard = DSLAMsBoardsManager.getDSLAMBoard(broadbandBoardId);
		setDslamBoard(dslamBoard);
	}

	public static List<BroadbandPort> getFreeBroadbandPorts(Connection connection) throws Exception {
		List<BroadbandPort> freeBroadbandPortsList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getFreeBroadbandPortsList()}");
			ResultSet resultSet = statement.executeQuery();
			freeBroadbandPortsList = processResultSet(resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getFreeBroadbandPortsList().", e);
			throw new Exception("Cannot get free broadband ports list from database.");
		} finally {
			statement.close();
		}
		return freeBroadbandPortsList;
	}

	public static List<BroadbandPort> getBroadbandPorts(Connection connection) throws Exception {
		List<BroadbandPort> broadbandPortsList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getBroadbandPortsList()}");
			ResultSet resultSet = statement.executeQuery();
			broadbandPortsList = processResultSet(resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getBroadbandPortsList().", e);
			throw new Exception("Cannot get broadband ports list from database.");
		} finally {
			statement.close();
		}
		return broadbandPortsList;
	}

	private static List<BroadbandPort> processResultSet(ResultSet resultSet) throws SQLException {
		List<BroadbandPort> broadbandPortsList = new ArrayList<>();
		while (resultSet.next()) {
			BroadbandPort broadbandPort = new BroadbandPort();
			broadbandPort.setId(resultSet.getInt("broadbandPortId"));
			broadbandPort.setPort(resultSet.getInt("broadbandPort"));

			int dslamId = resultSet.getInt("dslamId");
			DSLAM dslam = DSLAMsManager.getDSLAM(dslamId);
			int boardId = resultSet.getInt("boardId");
			DSLAMBoard dslamBoard = DSLAMsBoardsManager.getDSLAMBoard(boardId);
			broadbandPort.setDslamBoard(dslamBoard);

			broadbandPort.setAvailable(resultSet.getBoolean("broadbandPortAvailable"));
			broadbandPortsList.add(broadbandPort);
		}
		return broadbandPortsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.valueOf(port);
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
	 * @return the dslamBoard
	 */
	public DSLAMBoard getDslamBoard() {
		return dslamBoard;
	}

	/**
	 * @param dslamBoard the dslamBoard to set
	 */
	public void setDslamBoard(DSLAMBoard dslamBoard) {
		this.dslamBoard = dslamBoard;
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

	public static List<DSLAMBoard> getDSLAMsBoardsList() {
		return DSLAMsBoardsManager.getDSLAMsBoardsList();
	}
}
