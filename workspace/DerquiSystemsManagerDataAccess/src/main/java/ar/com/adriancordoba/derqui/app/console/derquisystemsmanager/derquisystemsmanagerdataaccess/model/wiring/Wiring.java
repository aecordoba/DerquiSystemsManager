/*
 * 		Wiring.java
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
 * 		Wiring.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Apr 13, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring;

import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;

import ar.com.adriancordoba.app.console.commonservices.excel2007.ExcelFileManipulator;
import ar.com.adriancordoba.app.console.commonservices.excel2007.ExcelFileManipulator.SpanedCell;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.Broadband;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Distributor;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.managers.SitesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.Address;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Area;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.OfficeCode;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Phone;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriber.Subscriber;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.SubscriberData;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.managers.SubscriberBroadbandStatesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.state.SubscriberBroadbandState;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Wiring {
	private int id;
	private Distributor distributor;
	private StreetPair streetPair;
	private StreetPair secondStreetPair;
	private Broadband broadband;
	private String remarks;
	private Subscriber subscriber;

	private static ResourceBundle bundle = ResourceBundle.getBundle("i18n/WorkbooksMessagesBundle"); // NOI18N
	private static final Logger logger = LogManager.getLogger(Wiring.class);

	/**
	 * 
	 */
	public Wiring() {
	}

	/**
	 * @param id
	 * @param distributor
	 * @param streetPair
	 * @param secondStreetPair
	 * @param broadband
	 * @param remarks
	 * @param phone
	 */
	public Wiring(int id, Distributor distributor, StreetPair streetPair, StreetPair secondStreetPair, Broadband broadband, String remarks, Subscriber subscriber) {
		this.id = id;
		this.distributor = distributor;
		this.streetPair = streetPair;
		this.secondStreetPair = secondStreetPair;
		this.broadband = broadband;
		this.remarks = remarks;
		this.subscriber = subscriber;
	}

	public static List<Wiring> getWiringList(Connection connection) throws Exception {
		List<Wiring> wiringList = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getWiringList()}");
			ResultSet resultSet = statement.executeQuery();
			wiringList = processResultSet(resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getWiringList().", e);
			throw new Exception("Cannot get wiring list from database.");
		} finally {
			statement.close();
		}
		return wiringList;
	}

	private static List<Wiring> processResultSet(ResultSet resultSet) throws SQLException, UnknownHostException {
		List<Wiring> wiringList = new ArrayList<>();
		while (resultSet.next()) {
			Wiring wiring = new Wiring();
			wiring.setId(resultSet.getInt("wiringId"));
			wiring.setDistributor(new Distributor(resultSet));
			wiring.setStreetPair(new StreetPair(resultSet, false));
			if (resultSet.getInt("secondStreetPairId") > 0)
				wiring.setSecondStreetPair(new StreetPair(resultSet, true));
			if (resultSet.getInt("broadbandId") > 0)
				wiring.setBroadband(new Broadband(resultSet));
			
			Phone phone = new Phone(resultSet, false);
			Address address = new Address();
			address.setPhone(phone);
			Subscriber subscriber = new Subscriber();
			subscriber.setAddress(address);
			wiring.setSubscriber(subscriber);

			wiring.setRemarks(resultSet.getString("wiringRemarks"));

			wiringList.add(wiring);
		}
		return wiringList;
	}

	public void update(Connection connection, int userId) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call updateWiring(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			statement.setInt(1, getId());
			statement.setInt(2, distributor.getId());
			statement.setInt(3, streetPair.getId());
			if (secondStreetPair != null)
				statement.setInt(4, secondStreetPair.getId());
			else
				statement.setInt(4, 0);

			statement.setInt(5, 0);
			statement.setString(6, null);
			statement.setString(7, null);
			statement.setInt(8, 0);

			if (broadband != null) {
				if (broadband.getBroadbandPort() != null)
					statement.setInt(5, broadband.getBroadbandPort().getId());
				statement.setString(6, broadband.getUsername());
				statement.setString(7, broadband.getPassword());
				if (broadband.getModemModel() != null)
					statement.setInt(8, broadband.getModemModel().getId());
			}

			statement.setString(9, getRemarks());
			statement.setInt(10, userId);

			statement.execute();

			String broadbandLog = (broadband != null) ? " (with broadband)" : " (without broadband)";
			logger.debug("Distributor " + distributor + " / Street Pair " + streetPair + broadbandLog + " was modified in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure updateWiring().", e);
			throw new Exception("Cannot modify wiring in database.");
		} finally {
			statement.close();
		}
	}

	public void insert(Connection connection, int userId) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call insertWiring(?, ?, ?, ?, ?, ?, ?, ?)}");

			statement.setInt(1, distributor.getId());
			statement.setInt(2, streetPair.getId());

			statement.setInt(3, 0);
			statement.setString(4, null);
			statement.setString(5, null);
			statement.setInt(6, 0);

			if (broadband != null) {
				if (broadband.getBroadbandPort() != null)
					statement.setInt(3, broadband.getBroadbandPort().getId());
				statement.setString(4, broadband.getUsername());
				statement.setString(5, broadband.getPassword());
				if (broadband.getModemModel() != null)
					statement.setInt(6, broadband.getModemModel().getId());
			}

			statement.setString(7, getRemarks());
			statement.setInt(8, userId);

			statement.execute();

			String broadbandLog = (broadband != null) ? " (with broadband)" : " (without broadband)";
			logger.debug("Distributor " + distributor + " / Street Pair " + streetPair + broadbandLog + " was inserted in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure insertWiring().", e);
			throw new Exception("Cannot insert wiring in database.");
		} finally {
			statement.close();
		}
	}

	public void insertSecondWiring(Connection connection, int userId) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call insertSecondWiring(?, ?, ?)}");

			statement.setInt(1, distributor.getId());
			statement.setInt(2, secondStreetPair.getId());
			statement.setInt(3, userId);
			statement.execute();
			logger.debug("Second Street Pair " + secondStreetPair + " was inserted in database for " + subscriber.getPhoneNumber() + ".");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure insertSecondWiring().", e);
			throw new Exception("Cannot insert second wiring in database.");
		} finally {
			statement.close();
		}
	}

	public void deleteSecondWiring(Connection connection, int userId) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call deleteSecondWiring(?, ?)}");

			statement.setInt(1, id);
			statement.setInt(2, userId);
			statement.execute();
			logger.debug("Second Street Pair was deleted in database for " + subscriber.getPhoneNumber() + ".");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure deleteSecondWiring().", e);
			throw new Exception("Cannot delete second wiring in database.");
		} finally {
			statement.close();
		}
	}

	public void delete(Connection connection, int userId) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call deleteWiring(?, ?, ?)}");
			statement.setInt(1, getId());
			statement.setString(2, getRemarks());
			statement.setInt(3, userId);
			statement.execute();

			String broadbandLog = (broadband != null) ? " (with broadband)" : " (without broadband)";
			logger.debug("Distributor " + distributor + " / Street Pair " + streetPair + broadbandLog + " was deleted from database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure deleteWiring().", e);
			throw new Exception("Cannot delete wiring in database.");
		} finally {
			statement.close();
		}
	}

	public static Workbook getRecord(Connection connection, Phone phone) throws Exception {
		Workbook workbook = null;
		OfficeCode officeCode = phone.getOfficeCode();
		Area area = officeCode.getArea();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getWiringRecordList(?, ?, ?, ?)}");
			statement.setInt(1, area.getCountry().getCode());
			statement.setInt(2, area.getCode());
			statement.setInt(3, officeCode.getCode());
			statement.setString(4, phone.getNumber());
			ResultSet resultSet = statement.executeQuery();

			workbook = getWiringRecordWorkbook(phone, resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getWiringRecordList().", e);
			throw new Exception("Cannot get wiring record list from database.");
		} finally {
			statement.close();
		}

		return workbook;
	}

	private static Workbook getWiringRecordWorkbook(Phone phone, ResultSet resultSet) throws SQLException {
		ExcelFileManipulator manipulator = new ExcelFileManipulator(phone.getFullNumeration());

		// Headers
		SpanedCell[] spanedHeaderCellsArray = { manipulator.new SpanedCell(bundle.getString("Workbooks.SWITCH_BLOCK"), 0, 2), manipulator.new SpanedCell("NEAX61Σ", 3, 9), manipulator.new SpanedCell("NEAX61Σ-ELU", 10, 14), manipulator.new SpanedCell("NEAX61E", 15, 22), manipulator.new SpanedCell("ZHONE", 23, 24), manipulator.new SpanedCell(bundle.getString("Workbooks.CABLE_PAIR"), 25, 27), manipulator.new SpanedCell(bundle.getString("Workbooks.SECOND_CABLE_PAIR"), 28, 30), manipulator.new SpanedCell(bundle.getString("Workbooks.BROADBAND"), 31, 38), manipulator.new SpanedCell(bundle.getString("Workbooks.MODIFICATION"), 39, 43) };
		manipulator.createSpanedCellsRow(spanedHeaderCellsArray, true);

		String[] headerCellsArray = { bundle.getString("Workbooks.SITE"), bundle.getString("Workbooks.BLOCK"), bundle.getString("Workbooks.POSITION"), "TSW", "KHW", "PHW", "ROW", "COLUMN", bundle.getString("Workbooks.FRAME"), "LM", "TSW", "KHW", "PHW", bundle.getString("Workbooks.NAME"), "L3ADDR", "SPCE", "HW", "SHW", "GR", "SW", "LVL", bundle.getString("Workbooks.FRAME"), "LM", bundle.getString("Workbooks.CABLE"), bundle.getString("Workbooks.PORT"), bundle.getString("Workbooks.FRAME"), bundle.getString("Workbooks.CABLE"), bundle.getString("Workbooks.PAIR"), bundle.getString("Workbooks.FRAME"), bundle.getString("Workbooks.CABLE"), bundle.getString("Workbooks.PAIR"), bundle.getString("Workbooks.ROUTER"), bundle.getString("Workbooks.USERNAME"), bundle.getString("Workbooks.PASSWORD"), bundle.getString("Workbooks.MODEM_TYPE"), bundle.getString("Workbooks.MODEM_MODEL"), "DSLAM", bundle.getString("Workbooks.SLOT"), bundle.getString("Workbooks.PORT"), bundle.getString("Workbooks.USERNAME"), bundle.getString("Workbooks.FIRST_NAME"), bundle.getString("Workbooks.LAST_NAME"), bundle.getString("Workbooks.TIME"), bundle.getString("Workbooks.REMARKS") };
		manipulator.createCellsRow(headerCellsArray, true);

		// Content
		while (resultSet.next()) {
			String[] cellsArray = new String[44];

			int siteId = resultSet.getInt("sigmaSiteId");
			if (siteId == 0) {
				siteId = resultSet.getInt("eSiteId");
				if (siteId == 0) {
					siteId = resultSet.getInt("zhoneSiteId");
					if (siteId == 0)
						siteId = resultSet.getInt("sigmaeluSiteId");
				}
			}
			cellsArray[0] = SitesManager.getSite(siteId).getCode();
			cellsArray[1] = resultSet.getString("switchBlockName");
			cellsArray[2] = resultSet.getString("blockPositionsPosition");
			cellsArray[3] = resultSet.getString("timeSwitch");
			cellsArray[4] = resultSet.getString("kHighway");
			cellsArray[5] = resultSet.getString("pHighway");
			cellsArray[6] = resultSet.getString("row");
			cellsArray[7] = resultSet.getString("column");
			cellsArray[8] = resultSet.getString("sigmaFrameName");
			cellsArray[9] = resultSet.getString("sigmaLineModuleName");
			cellsArray[10] = resultSet.getString("sigmaeluTimeSwitch");
			cellsArray[11] = resultSet.getString("sigmaeluKHighway");
			cellsArray[12] = resultSet.getString("sigmaeluPHighway");
			cellsArray[13] = resultSet.getString("sigmaeluName");

			cellsArray[14] = (cellsArray[13] != null) ? String.format("%05d", resultSet.getInt("sigmal3addr")) : "";

			cellsArray[15] = resultSet.getString("spce");
			cellsArray[16] = resultSet.getString("highway");
			cellsArray[17] = resultSet.getString("subhighway");
			cellsArray[18] = resultSet.getString("group");
			cellsArray[19] = resultSet.getString("switch");
			cellsArray[20] = resultSet.getString("level");
			cellsArray[21] = resultSet.getString("eFrameName");
			cellsArray[22] = resultSet.getString("eLineModuleName");
			cellsArray[23] = resultSet.getString("zhoneCable");
			cellsArray[24] = resultSet.getString("zhonePort");
			cellsArray[25] = resultSet.getString("streetFrameName");
			cellsArray[26] = resultSet.getString("streetCableName");

			int pair = resultSet.getInt("streetPairsPair");
			String pairString = (pair == 0) ? "" : String.format("%1$04d", pair);
			cellsArray[27] = pairString;

			cellsArray[28] = resultSet.getString("secondStreetFrameName");
			cellsArray[29] = resultSet.getString("secondStreetCableName");

			int secondPair = resultSet.getInt("secondStreetPairsPair");
			String secondPairString = (secondPair == 0) ? "" : String.format("%1$04d", secondPair);
			cellsArray[30] = secondPairString;

			cellsArray[31] = resultSet.getString("routerName");
			cellsArray[32] = resultSet.getString("broadbandUsername");
			cellsArray[33] = resultSet.getString("broadbandPassword");
			cellsArray[34] = resultSet.getString("modemTypeName");
			cellsArray[35] = resultSet.getString("modemModelName");
			cellsArray[36] = resultSet.getString("dslamName");
			cellsArray[37] = resultSet.getString("boardSlot");
			cellsArray[38] = resultSet.getString("broadbandPort");
			cellsArray[39] = resultSet.getString("username");
			cellsArray[40] = resultSet.getString("firstName");
			cellsArray[41] = resultSet.getString("lastName");
			cellsArray[42] = resultSet.getString("time");
			cellsArray[43] = resultSet.getString("remarks");

			manipulator.createCellsRow(cellsArray, false);
		}

		return manipulator.getWorkbook();
	}

	public static Workbook getWiringListByStreetCable(Connection connection, StreetCable streetCable) throws Exception {
		Workbook workbook = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getWiringListByStreetCable(?)}");
			statement.setInt(1, streetCable.getId());
			ResultSet resultSet = statement.executeQuery();

			workbook = getWiringListByStreetCableWorkbook(streetCable, resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getWiringRecordList().", e);
			throw new Exception("Cannot get wiring record list from database.");
		} finally {
			statement.close();
		}

		return workbook;
	}

	private static Workbook getWiringListByStreetCableWorkbook(StreetCable streetCable, ResultSet resultSet) throws SQLException {
		ExcelFileManipulator manipulator = new ExcelFileManipulator(streetCable.toString());

		// Headers
		SpanedCell[] spanedHeaderCellsArray = { manipulator.new SpanedCell(bundle.getString("Workbooks.STREET_CABLE"), 0, 3), manipulator.new SpanedCell(bundle.getString("Workbooks.PHONE_NUMBER"), 4, 7), manipulator.new SpanedCell(bundle.getString("Workbooks.BROADBAND"), 8, 10), manipulator.new SpanedCell(bundle.getString("Workbooks.SWITCH_BLOCK"), 11, 12) };
		manipulator.createSpanedCellsRow(spanedHeaderCellsArray, true);

		String[] headerCellsArray = { bundle.getString("Workbooks.SITE"), bundle.getString("Workbooks.FRAME"), bundle.getString("Workbooks.CABLE"), bundle.getString("Workbooks.PAIR"), bundle.getString("Workbooks.COUNTRY_CODE"), bundle.getString("Workbooks.AREA_CODE"), bundle.getString("Workbooks.OFFICE_CODE"), bundle.getString("Workbooks.NUMBER"), "DSLAM", bundle.getString("Workbooks.SLOT"), bundle.getString("Workbooks.PORT"), bundle.getString("Workbooks.BLOCK"), bundle.getString("Workbooks.POSITION") };
		manipulator.createCellsRow(headerCellsArray, true);

		// Content
		while (resultSet.next()) {
			String[] cellsArray = new String[13];

			cellsArray[0] = SitesManager.getSite(resultSet.getInt("siteId")).getCode();
			cellsArray[1] = resultSet.getString("frameName");
			cellsArray[2] = resultSet.getString("cableName");

			int pair = resultSet.getInt("pair");
			String pairString = (pair == 0) ? "" : String.format("%1$04d", pair);
			cellsArray[3] = pairString;

			cellsArray[4] = resultSet.getString("countryCode");
			cellsArray[5] = resultSet.getString("areaCode");
			cellsArray[6] = resultSet.getString("officeCode");
			cellsArray[7] = resultSet.getString("number");
			cellsArray[8] = resultSet.getString("dslamName");
			cellsArray[9] = resultSet.getString("boardSlot");
			cellsArray[10] = resultSet.getString("broadbandPort");
			cellsArray[11] = resultSet.getString("switchBlockName");
			cellsArray[12] = resultSet.getString("blockPosition");

			manipulator.createCellsRow(cellsArray, false);
		}

		return manipulator.getWorkbook();
	}

	public static Wiring getDistributionWiringByPhoneNumber(Connection connection, int officeCodeId, String mcdu) throws Exception {
		Wiring wiring = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getDistributionWiringByPhoneNumber(?, ?)}");
			statement.setInt(1, officeCodeId);
			statement.setString(2, mcdu);
			ResultSet resultSet = statement.executeQuery();
			List<Wiring> wiringList = processResultSet(resultSet);
			if (!wiringList.isEmpty())
				wiring = wiringList.get(0);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getDistributionWiringByPhoneNumber().", e);
			throw new Exception("Cannot get distribution wiring from database.");
		} finally {
			statement.close();
		}
		return wiring;
	}

	public static Wiring getDistributionWiringByBlockPosition(Connection connection, int switchBlockId, String position) throws Exception {
		Wiring wiring = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getDistributionWiringByBlockPosition(?, ?)}");
			statement.setInt(1, switchBlockId);
			statement.setString(2, position);
			ResultSet resultSet = statement.executeQuery();
			List<Wiring> wiringList = processResultSet(resultSet);
			if (!wiringList.isEmpty())
				wiring = wiringList.get(0);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getDistributionWiringByBlockPosition().", e);
			throw new Exception("Cannot get distribution wiring from database.");
		} finally {
			statement.close();
		}
		return wiring;
	}

	public static Wiring getDistributionWiringByBroadbandPort(Connection connection, int broadbandPortId) throws Exception {
		Wiring wiring = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getDistributionWiringByBroadbandPort(?)}");
			statement.setInt(1, broadbandPortId);
			ResultSet resultSet = statement.executeQuery();
			List<Wiring> wiringList = processResultSet(resultSet);
			if (!wiringList.isEmpty())
				wiring = wiringList.get(0);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getDistributionWiringByBroadbandPort().", e);
			throw new Exception("Cannot get distribution wiring from database.");
		} finally {
			statement.close();
		}
		return wiring;
	}

	public static Wiring getDistributionWiringByStreetPair(Connection connection, int streetPairId) throws Exception {
		Wiring wiring = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getDistributionWiringByStreetPair(?)}");
			statement.setInt(1, streetPairId);
			ResultSet resultSet = statement.executeQuery();
			List<Wiring> wiringList = processResultSet(resultSet);
			if (!wiringList.isEmpty())
				wiring = wiringList.get(0);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getDistributionWiringByStreetPair().", e);
			throw new Exception("Cannot get distribution wiring from database.");
		} finally {
			statement.close();
		}
		return wiring;
	}

	public static Wiring getBroadbandByPhoneNumber(Connection connection, int officeCodeId, String mcdu) throws Exception {
		Wiring wiring = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getBroadbandByPhoneNumber(?, ?)}");
			statement.setInt(1, officeCodeId);
			statement.setString(2, mcdu);
			ResultSet resultSet = statement.executeQuery();
			List<Wiring> wiringList = processBroadbandResultSet(resultSet);
			if (!wiringList.isEmpty())
				wiring = wiringList.get(0);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getBroadbandByPhoneNumber().", e);
			throw new Exception("Cannot get broadband data from database.");
		} finally {
			statement.close();
		}
		return wiring;
	}

	public static Wiring getBroadbandByBroadbandPort(Connection connection, int broadbandPortId) throws Exception {
		Wiring wiring = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getBroadbandByBroadbandPort(?)}");
			statement.setInt(1, broadbandPortId);
			ResultSet resultSet = statement.executeQuery();
			List<Wiring> wiringList = processBroadbandResultSet(resultSet);
			if (!wiringList.isEmpty())
				wiring = wiringList.get(0);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getBroadbandByBroadbandPort().", e);
			throw new Exception("Cannot get broadband data from database.");
		} finally {
			statement.close();
		}
		return wiring;
	}

	private static List<Wiring> processBroadbandResultSet(ResultSet resultSet) throws SQLException, UnknownHostException {
		List<Wiring> wiringList = new ArrayList<>();
		while (resultSet.next()) {
			Wiring wiring = new Wiring();
			wiring.setId(resultSet.getInt("wiringId"));
			if (resultSet.getInt("broadbandId") > 0) {
				Broadband broadband = new Broadband(resultSet);
				wiring.setBroadband(broadband);
			}
			
			Phone phone = new Phone(resultSet, false);
			Address address = new Address();
			address.setPhone(phone);
			Subscriber subscriber = new Subscriber();
			subscriber.setAddress(address);
			SubscriberBroadbandState broadbandState = SubscriberBroadbandStatesManager.getSubscriberBroadbandState(resultSet.getInt("broadbandStateId"));
			SubscriberData subscriberData = new SubscriberData();
			subscriberData.setBroadbandState(broadbandState);
			subscriber.setData(subscriberData);
			wiring.setSubscriber(subscriber);

			wiringList.add(wiring);
		}
		return wiringList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return subscriber.getPhoneNumber();
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
	 * @return the distributor
	 */
	public Distributor getDistributor() {
		return distributor;
	}

	/**
	 * @param distributor the distributor to set
	 */
	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}

	/**
	 * @return the streetPair
	 */
	public StreetPair getStreetPair() {
		return streetPair;
	}

	/**
	 * @param streetPair the streetPair to set
	 */
	public void setStreetPair(StreetPair streetPair) {
		this.streetPair = streetPair;
	}

	/**
	 * @return the secondStreetPair
	 */
	public StreetPair getSecondStreetPair() {
		return secondStreetPair;
	}

	/**
	 * @param secondStreetPair the secondStreetPair to set
	 */
	public void setSecondStreetPair(StreetPair secondStreetPair) {
		this.secondStreetPair = secondStreetPair;
	}

	/**
	 * @return the broadband
	 */
	public Broadband getBroadband() {
		return broadband;
	}

	/**
	 * @param broadband the broadband to set
	 */
	public void setBroadband(Broadband broadband) {
		this.broadband = broadband;
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
	 * @return the subscriber
	 */
	public Subscriber getSubscriber() {
		return subscriber;
	}

	/**
	 * @param subscriber the subscriber to set
	 */
	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}
}
