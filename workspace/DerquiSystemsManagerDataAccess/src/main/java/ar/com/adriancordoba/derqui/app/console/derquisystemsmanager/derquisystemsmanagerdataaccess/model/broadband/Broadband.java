/*
 * 		Broadband.java
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
 * 		Broadband.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Apr 13, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband;

import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;

import ar.com.adriancordoba.app.console.commonservices.excel2007.ExcelFileManipulator;
import ar.com.adriancordoba.app.console.commonservices.excel2007.ExcelFileManipulator.SpanedCell;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.modem.ModemModel;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.Wiring;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Broadband {
	private int id;
	private String username;
	private String password;
	private BroadbandPort broadbandPort;
	private ModemModel modemModel;

	private static ResourceBundle bundle = ResourceBundle.getBundle("i18n/WorkbooksMessagesBundle"); // NOI18N
	private static final Logger logger = LogManager.getLogger(Wiring.class);

	/**
	 * 
	 */
	public Broadband() {
	}

	/**
	 * @param username
	 * @param password
	 * @param broadbandPort
	 * @param modemModel
	 */
	public Broadband(String username, String password, BroadbandPort broadbandPort, ModemModel modemModel) {
		this.username = username;
		this.password = password;
		this.broadbandPort = broadbandPort;
		this.modemModel = modemModel;
	}

	public Broadband(ResultSet resultSet) throws SQLException, UnknownHostException {
		setId(resultSet.getInt("broadbandId"));
		setUsername(resultSet.getString("broadbandUsername"));
		setPassword(resultSet.getString("broadbandPassword"));
		if (resultSet.getInt("broadbandPortId") > 0)
			setBroadbandPort(new BroadbandPort(resultSet));
		if (resultSet.getInt("modemModelId") > 0)
			setModemModel(new ModemModel(resultSet));
	}

	public static Workbook getBroadbandInstallations(Connection connection, LocalDate startDate, LocalDate endDate) throws Exception {
		String startDateString = startDate.toString();
		String endDateString = endDate.toString();
		Workbook workbook = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getBroadbandInstallationsList(?, ?)}");
			statement.setString(1, startDateString);
			statement.setString(2, endDateString);
			ResultSet resultSet = statement.executeQuery();

			workbook = getBroadbandInstallationsWorkbook(startDateString + "~" + endDateString, resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getBroadbandInstallationsList().", e);
			throw new Exception("Cannot get broadband installations list from database.");
		} finally {
			statement.close();
		}
		return workbook;
	}

	public static Workbook getBroadbandUninstallations(Connection connection, LocalDate startDate, LocalDate endDate) throws Exception {
		String startDateString = startDate.toString();
		String endDateString = endDate.toString();
		Workbook workbook = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getBroadbandUninstallationsList(?, ?)}");
			statement.setString(1, startDateString);
			statement.setString(2, endDateString);
			ResultSet resultSet = statement.executeQuery();

			workbook = getBroadbandInstallationsWorkbook(startDateString + "~" + endDateString, resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getBroadbandUninstallationsList().", e);
			throw new Exception("Cannot get broadband uninstallations list from database.");
		} finally {
			statement.close();
		}
		return workbook;
	}

	public static Workbook getInstalledBroadband(Connection connection, LocalDate targetDate) throws Exception {
		String targetDateString = targetDate.toString();
		Workbook workbook = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getInstalledBroadbandList(?)}");
			statement.setString(1, targetDateString);
			ResultSet resultSet = statement.executeQuery();

			workbook = getSubscribersWorkbook(targetDateString, resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getInstalledBroadbandList().", e);
			throw new Exception("Cannot get installed broadband list from database.");
		} finally {
			statement.close();
		}
		return workbook;
	}

	public static Workbook getNotInstalledBroadband(Connection connection, LocalDate targetDate) throws Exception {
		String targetDateString = targetDate.toString();
		Workbook workbook = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getNotInstalledBroadbandList(?)}");
			statement.setString(1, targetDateString);
			ResultSet resultSet = statement.executeQuery();

			workbook = getSubscribersWorkbook(targetDateString, resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getNotInstalledBroadbandList().", e);
			throw new Exception("Cannot get not installed broadband list from database.");
		} finally {
			statement.close();
		}
		return workbook;
	}

	private static Workbook getBroadbandInstallationsWorkbook(String spreadsheetName, ResultSet resultSet) throws SQLException {
		ExcelFileManipulator manipulator = new ExcelFileManipulator(spreadsheetName);

		// Headers
		SpanedCell[] spanedHeaderCellsArray = { manipulator.new SpanedCell(bundle.getString("Workbooks.PHONE_NUMBER"), 0, 3), manipulator.new SpanedCell(bundle.getString("Workbooks.SWITCH_BLOCK"), 4, 6), manipulator.new SpanedCell(bundle.getString("Workbooks.BROADBAND"), 7, 12), manipulator.new SpanedCell(bundle.getString("Workbooks.CABLE_PAIR"), 13, 15), manipulator.new SpanedCell(bundle.getString("Workbooks.MODIFICATION"), 16, 16) };
		manipulator.createSpanedCellsRow(spanedHeaderCellsArray, true);

		String[] headerCellsArray = { bundle.getString("Workbooks.COUNTRY_CODE"), bundle.getString("Workbooks.AREA_CODE"), bundle.getString("Workbooks.OFFICE_CODE"), bundle.getString("Workbooks.NUMBER"), bundle.getString("Workbooks.SITE"), bundle.getString("Workbooks.BLOCK"), bundle.getString("Workbooks.POSITION"), bundle.getString("Workbooks.ROUTER"), bundle.getString("Workbooks.USERNAME"), bundle.getString("Workbooks.PASSWORD"), "DSLAM", bundle.getString("Workbooks.SLOT"), bundle.getString("Workbooks.PORT"), bundle.getString("Workbooks.FRAME"), bundle.getString("Workbooks.CABLE"), bundle.getString("Workbooks.PAIR"), bundle.getString("Workbooks.TIME") };
		manipulator.createCellsRow(headerCellsArray, true);

		// Content
		while (resultSet.next()) {
			String[] cellsArray = new String[17];
			cellsArray[0] = String.valueOf(resultSet.getInt("countryCode"));
			cellsArray[1] = String.valueOf(resultSet.getInt("areaCode"));
			cellsArray[2] = String.valueOf(resultSet.getInt("officeCode"));
			cellsArray[3] = resultSet.getString("number");
			cellsArray[4] = resultSet.getString("site");
			cellsArray[5] = resultSet.getString("switchBlock");
			cellsArray[6] = resultSet.getString("switchBlockPosition");
			cellsArray[7] = resultSet.getString("routerName");
			cellsArray[8] = resultSet.getString("broadbandUsername");
			cellsArray[9] = resultSet.getString("broadbandPassword");
			cellsArray[10] = resultSet.getString("dslamName");
			cellsArray[11] = resultSet.getString("boardSlot");
			cellsArray[12] = resultSet.getString("broadbandPort");
			cellsArray[13] = resultSet.getString("streetFrameName");
			cellsArray[14] = resultSet.getString("streetCableName");
			cellsArray[15] = resultSet.getString("streetPairsPair");
			cellsArray[16] = resultSet.getString("time");

			manipulator.createCellsRow(cellsArray, false);
		}
		return manipulator.getWorkbook();
	}

	private static Workbook getSubscribersWorkbook(String spreadsheetName, ResultSet resultSet) throws SQLException {
		ExcelFileManipulator manipulator = new ExcelFileManipulator(spreadsheetName);

		// Headers
		String[] headerCellsArray = { bundle.getString("Workbooks.COUNTRY_CODE"), bundle.getString("Workbooks.AREA_CODE"), bundle.getString("Workbooks.OFFICE_CODE"), bundle.getString("Workbooks.NUMBER"), bundle.getString("Workbooks.TIME") };
		manipulator.createCellsRow(headerCellsArray, true);

		// Content
		while (resultSet.next()) {
			String[] cellsArray = new String[5];
			cellsArray[0] = String.valueOf(resultSet.getInt("countryCode"));
			cellsArray[1] = String.valueOf(resultSet.getInt("areaCode"));
			cellsArray[2] = String.valueOf(resultSet.getInt("officeCode"));
			cellsArray[3] = resultSet.getString("number");
			cellsArray[4] = resultSet.getString("time");

			manipulator.createCellsRow(cellsArray, false);
		}

		return manipulator.getWorkbook();
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

	/**
	 * @return the broadbandPort
	 */
	public BroadbandPort getBroadbandPort() {
		return broadbandPort;
	}

	/**
	 * @param broadbandPort the broadbandPort to set
	 */
	public void setBroadbandPort(BroadbandPort broadbandPort) {
		this.broadbandPort = broadbandPort;
	}

	/**
	 * @return the modemModel
	 */
	public ModemModel getModemModel() {
		return modemModel;
	}

	/**
	 * @param modemModel the modemModel to set
	 */
	public void setModemModel(ModemModel modemModel) {
		this.modemModel = modemModel;
	}
}
