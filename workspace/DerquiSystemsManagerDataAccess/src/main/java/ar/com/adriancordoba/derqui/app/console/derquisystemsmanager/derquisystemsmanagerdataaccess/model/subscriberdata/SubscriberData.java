/*
 * 		SubscriberData.java
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
 * 		SubscriberData.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Nov 30, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ar.com.adriancordoba.app.console.commonservices.configuration.Configuration;
import ar.com.adriancordoba.app.console.commonservices.excel2007.ExcelFileManipulator;
import ar.com.adriancordoba.app.console.commonservices.excel2007.ExcelFileManipulator.SpanedCell;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors.SubscriberBroadbandStateName;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors.SubscriberLineClassType;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors.SubscriberStateName;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors.Technology;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.IdentificationType;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.Street;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.managers.StreetsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Area;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.OfficeCode;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Phone;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.managers.SubscriberBroadbandStatesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.managers.SubscriberLineClassesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.managers.SubscriberServicesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.managers.SubscriberStatesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.restriction.SubscriberRestriction;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.restriction.managers.SubscriberRestrictionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.state.SubscriberBroadbandState;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.state.SubscriberState;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class SubscriberData {
	private int id;
	private SubscriberLineClass lineClass;
	private SubscriberRestriction restriction;
	private SubscriberState state;
	private List<SubscriberService> servicesList;
	private SubscriberBroadbandState broadbandState;
	private String information;

	private static SubscriberData defaultSubscriberData;

	private static ResourceBundle bundle = ResourceBundle.getBundle("i18n/WorkbooksMessagesBundle"); // NOI18N
	private static final Logger logger = LogManager.getLogger(SubscriberData.class);

	/**
	 * 
	 */
	public SubscriberData() {
		servicesList = new ArrayList<>();
	}

	/**
	 * 
	 * @param lineClass
	 * @param restriction
	 * @param state
	 * @param servicesList
	 * @param broadbandState
	 * @param information
	 */
	public SubscriberData(SubscriberLineClass lineClass, SubscriberRestriction restriction, SubscriberState state, List<SubscriberService> servicesList, SubscriberBroadbandState broadbandState, String information) {
		this.lineClass = lineClass;
		this.restriction = restriction;
		this.state = state;
		this.servicesList = servicesList;
		this.broadbandState = broadbandState;
		this.information = information;
	}

	public void fillData(ResultSet resultSet) throws SQLException {
		setLineClass(SubscriberLineClassesManager.getSubscriberLineClass(resultSet.getInt("lineClassId")));
		setRestriction(SubscriberRestrictionsManager.getSubscriberRestriction(resultSet.getInt("restrictionId")));
		setState(SubscriberStatesManager.getSubscriberState(resultSet.getInt("stateId")));

		int serviceId = resultSet.getInt("serviceId");
		if (serviceId != 0)
			servicesList.add(SubscriberServicesManager.getSubscriberService(serviceId));

		int broadbandId = resultSet.getInt("broadbandId");
		if (broadbandId != 0)
			setBroadbandState(SubscriberBroadbandStatesManager.getSubscriberBroadbandState(resultSet.getInt("broadbandStateId")));

		setInformation(resultSet.getString("information"));
	}

	public void addService(SubscriberService service) {
		servicesList.add(service);
	}

	public static SubscriberData getDefaultSubscriberData() {
		if (defaultSubscriberData == null) {
			defaultSubscriberData = new SubscriberData();
			defaultSubscriberData.setLineClass(SubscriberLineClassesManager.getSubscriberLineClass(SubscriberLineClassType.valueOf("INDIVIDUAL")));
			defaultSubscriberData.setState(SubscriberStatesManager.getSubscriberState(SubscriberStateName.valueOf("ENABLED")));
			defaultSubscriberData.setRestriction(SubscriberRestrictionsManager.getSubscriberRestriction('0', "12"));

			try {
				CompositeConfiguration configuration = Configuration.getInstance("settings.xml");
				defaultSubscriberData.setLineClass(SubscriberLineClassesManager.getSubscriberLineClass(SubscriberLineClassType.valueOf(configuration.getString("derqui-systems-manager.default-subscriber-data.type").toUpperCase())));
				defaultSubscriberData.setState(SubscriberStatesManager.getSubscriberState(SubscriberStateName.valueOf(configuration.getString("derqui-systems-manager.default-subscriber-data.state").toUpperCase())));
				defaultSubscriberData.setRestriction(SubscriberRestrictionsManager.getSubscriberRestriction(configuration.getString("derqui-systems-manager.default-subscriber-data.termination-restriction").charAt(0), configuration.getString("derqui-systems-manager.default-subscriber-data.origination-restriction")));
			} catch (ConfigurationException e) {
				logger.error("It couldn't get Default Subscriber Data from settings file.");
			} finally {
				logger.info("Using Default Line Type: " + defaultSubscriberData.getLineClass().getType() + ".");
				logger.info("Using Default Line State: " + defaultSubscriberData.getState().getName() + ".");
				logger.info("Using Default Line Termination Restriction: " + defaultSubscriberData.getRestriction().getTerminationRestriction().getName() + ".");
				logger.info("Using Default Line OriginationRestriction: " + defaultSubscriberData.getRestriction().getOriginationRestriction().getName() + ".");
			}
		}

		return defaultSubscriberData;
	}

	public void update(Connection connection, String number, int userId) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call updateSubscriberData(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			statement.setInt(1, id);
			statement.setInt(2, lineClass.getId());
			statement.setInt(3, restriction.getId());
			statement.setInt(4, state.getId());

			if (broadbandState != null)
				statement.setInt(5, broadbandState.getId());
			else
				statement.setInt(5, 0);

			statement.setString(6, getInformation());

			if (servicesList.contains(SubscriberServicesManager.getSubscriberService("clip")))
				statement.setBoolean(7, true);
			else
				statement.setBoolean(7, false);
			if (servicesList.contains(SubscriberServicesManager.getSubscriberService("clir")))
				statement.setBoolean(8, true);
			else
				statement.setBoolean(8, false);
			if (servicesList.contains(SubscriberServicesManager.getSubscriberService("cf")))
				statement.setBoolean(9, true);
			else
				statement.setBoolean(9, false);
			if (servicesList.contains(SubscriberServicesManager.getSubscriberService("cw")))
				statement.setBoolean(10, true);
			else
				statement.setBoolean(10, false);

			statement.setInt(11, userId);

			statement.execute();
			logger.debug(number + " subscriber data was modified in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure updateSubscriberData().", e);
			throw new Exception("Cannot modify subscriber data in database.");
		} finally {
			statement.close();
		}
	}

	public static Workbook getRecord(Connection connection, Phone phone) throws Exception {
		Workbook workbook = null;
		OfficeCode officeCode = phone.getOfficeCode();
		Area area = officeCode.getArea();
		CallableStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareCall("{call getSubscriberDataRecordList(?, ?, ?, ?)}");
			statement.setInt(1, area.getCountry().getCode());
			statement.setInt(2, area.getCode());
			statement.setInt(3, officeCode.getCode());
			statement.setString(4, phone.getNumber());
			resultSet = statement.executeQuery();

			workbook = getSubscriberDataRecordWorkbook(phone.getFullNumeration(), resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getSubscriberDataRecordList().", e);
			throw new Exception("Cannot get subscriber data record list from database.");
		} finally {
			statement.close();
		}
		return workbook;
	}

	public static Workbook getModified(Connection connection, LocalDate startDate, LocalDate endDate) throws Exception {
		String startDateString = startDate.toString();
		String endDateString = endDate.toString();
		Workbook workbook = null;
		CallableStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareCall("{call getModifiedSubscribersDataList(?, ?)}");
			statement.setString(1, startDateString);
			statement.setString(2, endDateString);
			resultSet = statement.executeQuery();

			workbook = getModifiedSubscribersDataWorkbook(startDateString + "~" + endDateString, resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getModifiedSubscribersDataList().", e);
			throw new Exception("Cannot get modified subscribers data list from database.");
		} finally {
			statement.close();
		}
		return workbook;
	}

	private static Workbook getSubscriberDataRecordWorkbook(String spreadsheetName, ResultSet resultSet) throws SQLException {
		ExcelFileManipulator manipulator = new ExcelFileManipulator(spreadsheetName);

		// Headers
		SpanedCell[] spanedHeaderCellsArray = { manipulator.new SpanedCell(bundle.getString("Workbooks.LINE_CLASS"), 0, 0), manipulator.new SpanedCell(bundle.getString("Workbooks.TERMINATION"), 1, 2), manipulator.new SpanedCell(bundle.getString("Workbooks.ORIGINATION"), 3, 4), manipulator.new SpanedCell(bundle.getString("Workbooks.STATE"), 5, 6), manipulator.new SpanedCell(bundle.getString("Workbooks.ADDITIONAL_DATA"), 7, 8), manipulator.new SpanedCell(bundle.getString("Workbooks.MODIFICATION"), 9, 12) };
		manipulator.createSpanedCellsRow(spanedHeaderCellsArray, true);

		String[] headerCellsArray = { bundle.getString("Workbooks.TYPE"), bundle.getString("Workbooks.RESTRICTION"), bundle.getString("Workbooks.DESCRIPTION"), bundle.getString("Workbooks.RESTRICTION"), bundle.getString("Workbooks.DESCRIPTION"), bundle.getString("Workbooks.LINE"), bundle.getString("Workbooks.BROADBAND"), bundle.getString("Workbooks.SERVICES"), bundle.getString("Workbooks.INFORMATION"), bundle.getString("Workbooks.USERNAME"), bundle.getString("Workbooks.FIRST_NAME"), bundle.getString("Workbooks.LAST_NAME"), bundle.getString("Workbooks.TIME") };
		manipulator.createCellsRow(headerCellsArray, true);

		// Content
		while (resultSet.next()) {
			String[] cellsArray = new String[13];
			cellsArray[0] = bundle.getString("Workbooks.lineClassType." + resultSet.getString("lineClassType"));
			cellsArray[1] = resultSet.getString("terminationRestrictionName");
			cellsArray[2] = resultSet.getString("terminationRestrictionDescription");
			cellsArray[3] = resultSet.getString("originationRestrictionName");
			cellsArray[4] = resultSet.getString("originationRestrictionDescription");
			cellsArray[5] = bundle.getString("Workbooks.state." + resultSet.getString("stateName"));

			String broadbandStateName = resultSet.getString("broadbandStateName");
			cellsArray[6] = (broadbandStateName != null) ? bundle.getString("Workbooks.state." + broadbandStateName) : "";

			cellsArray[7] = resultSet.getString("services");
			cellsArray[8] = resultSet.getString("information");

			cellsArray[9] = resultSet.getString("username");
			cellsArray[10] = resultSet.getString("firstName");
			cellsArray[11] = resultSet.getString("lastName");
			cellsArray[12] = resultSet.getString("time");

			manipulator.createCellsRow(cellsArray, false);
		}
		return manipulator.getWorkbook();
	}

	private static Workbook getModifiedSubscribersDataWorkbook(String spreadsheetName, ResultSet resultSet) throws SQLException {
		ExcelFileManipulator manipulator = new ExcelFileManipulator(spreadsheetName);

		// Headers
		SpanedCell[] spanedHeaderCellsArray = { manipulator.new SpanedCell(bundle.getString("Workbooks.PHONE_NUMBER"), 0, 3), manipulator.new SpanedCell(bundle.getString("Workbooks.LINE"), 4, 10), manipulator.new SpanedCell(bundle.getString("Workbooks.BROADBAND"), 11, 13), manipulator.new SpanedCell(bundle.getString("Workbooks.MODIFICATION"), 14, 17) };
		manipulator.createSpanedCellsRow(spanedHeaderCellsArray, true);

		String[] headerCellsArray = { bundle.getString("Workbooks.COUNTRY_CODE"), bundle.getString("Workbooks.AREA_CODE"), bundle.getString("Workbooks.OFFICE_CODE"), bundle.getString("Workbooks.NUMBER"), bundle.getString("Workbooks.TECHNOLOGY"), bundle.getString("Workbooks.TYPE"), bundle.getString("Workbooks.TERMINATION_RESTRICTION"), bundle.getString("Workbooks.ORIGINATION_RESTRICTION"), bundle.getString("Workbooks.SERVICES"), bundle.getString("Workbooks.INFORMATION"), bundle.getString("Workbooks.STATE"), bundle.getString("Workbooks.ROUTER"), bundle.getString("Workbooks.USERNAME"), bundle.getString("Workbooks.STATE"), bundle.getString("Workbooks.USERNAME"), bundle.getString("Workbooks.FIRST_NAME"), bundle.getString("Workbooks.LAST_NAME"), bundle.getString("Workbooks.TIME") };
		manipulator.createCellsRow(headerCellsArray, true);

		// Content
		while (resultSet.next()) {
			String[] cellsArray = new String[18];
			cellsArray[0] = String.valueOf(resultSet.getInt("countryCode"));
			cellsArray[1] = String.valueOf(resultSet.getInt("areaCode"));
			cellsArray[2] = String.valueOf(resultSet.getInt("officeCode"));
			cellsArray[3] = resultSet.getString("number");

			Technology technology = null;
			if (resultSet.getInt("neax61sigmaId") > 0)
				technology = Technology.NEAX61SIGMA;
			if (resultSet.getInt("sigmal3addrId") > 0)
				technology = Technology.NEAX61SIGMA_ELU;
			if (resultSet.getInt("neax61eId") > 0)
				technology = Technology.NEAX61E;
			if (resultSet.getInt("zhoneId") > 0)
				technology = Technology.ZHONE;
			cellsArray[4] = technology.toString();

			cellsArray[5] = bundle.getString("Workbooks.lineClassType." + resultSet.getString("lineClassType"));
			cellsArray[6] = resultSet.getString("terminationRestrictionName");
			cellsArray[7] = resultSet.getString("originationRestrictionName");
			cellsArray[8] = resultSet.getString("services");
			cellsArray[9] = resultSet.getString("information");
			cellsArray[10] = bundle.getString("Workbooks.state." + resultSet.getString("stateName"));

			String routerName = resultSet.getString("routerName");
			if (routerName == null)
				routerName = resultSet.getString("boardDSLAMRouterName");
			cellsArray[11] = (routerName != null) ? routerName : "";
			String broadbandUsername = resultSet.getString("broadbandUsername");
			cellsArray[12] = (broadbandUsername != null) ? broadbandUsername : "";
			String broadbandStateName = resultSet.getString("broadbandStateName");
			cellsArray[13] = (broadbandStateName != null) ? bundle.getString("Workbooks.state." + broadbandStateName) : "";

			cellsArray[14] = resultSet.getString("username");
			cellsArray[15] = resultSet.getString("firstName");
			cellsArray[16] = resultSet.getString("lastName");
			cellsArray[17] = resultSet.getString("time");

			manipulator.createCellsRow(cellsArray, false);
		}
		return manipulator.getWorkbook();
	}

	public static Workbook getModifications(Connection connection, LocalDate startDate, LocalDate endDate) throws Exception {
		String startDateString = startDate.toString();
		String endDateString = endDate.toString();
		Workbook workbook = null;
		CallableStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareCall("{call getSubscribersDataModificationList(?, ?)}");
			statement.setString(1, startDateString);
			statement.setString(2, endDateString);
			resultSet = statement.executeQuery();
			workbook = getSubscribersDataModificationsWoorkbook(startDateString + "~" + endDateString, resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getSubscribersDataModificationList().", e);
			throw new Exception("Cannot get subscribers data modifications list from database.");
		} finally {
			statement.close();
		}
		return workbook;
	}

	private static Workbook getSubscribersDataModificationsWoorkbook(String spreadsheetName, ResultSet resultSet) throws SQLException {
		ExcelFileManipulator manipulator = new ExcelFileManipulator(spreadsheetName);

		// Headers
		SpanedCell[] spanedHeaderCellsArray = { manipulator.new SpanedCell(bundle.getString("Workbooks.PHONE_NUMBER"), 0, 3), manipulator.new SpanedCell(bundle.getString("Workbooks.LINE"), 4, 10), manipulator.new SpanedCell(bundle.getString("Workbooks.BROADBAND"), 11, 13), manipulator.new SpanedCell(bundle.getString("Workbooks.MODIFICATION"), 14, 17) };
		manipulator.createSpanedCellsRow(spanedHeaderCellsArray, true);

		String[] headerCellsArray = { bundle.getString("Workbooks.COUNTRY_CODE"), bundle.getString("Workbooks.AREA_CODE"), bundle.getString("Workbooks.OFFICE_CODE"), bundle.getString("Workbooks.NUMBER"), bundle.getString("Workbooks.TECHNOLOGY"), bundle.getString("Workbooks.TYPE"), bundle.getString("Workbooks.TERMINATION_RESTRICTION"), bundle.getString("Workbooks.ORIGINATION_RESTRICTION"), bundle.getString("Workbooks.SERVICES"), bundle.getString("Workbooks.INFORMATION"), bundle.getString("Workbooks.STATE"), bundle.getString("Workbooks.ROUTER"), bundle.getString("Workbooks.USERNAME"), bundle.getString("Workbooks.STATE"), bundle.getString("Workbooks.USERNAME"), bundle.getString("Workbooks.FIRST_NAME"), bundle.getString("Workbooks.LAST_NAME"), bundle.getString("Workbooks.TIME") };
		manipulator.createCellsRow(headerCellsArray, true);

		// Content
		List<SubscriberDataRowsPair> subscriberDataRowsPairsList = new ArrayList<>();
		while (resultSet.next()) {
			boolean inserted = false;
			int subscriberId = resultSet.getInt("subscriberId");
			for (SubscriberDataRowsPair subscriberDataRowsPair : subscriberDataRowsPairsList) {
				if (subscriberId == subscriberDataRowsPair.getSubscriberId()) {
					subscriberDataRowsPair.setLastSubscriberDataRow(new SubscriberDataRow(resultSet));
					inserted = true;
					break;
				}
			}
			if (!inserted) {
				SubscriberDataRowsPair subscriberDataRowsPair = new SubscriberDataRowsPair(new SubscriberDataRow(resultSet));
				subscriberDataRowsPairsList.add(subscriberDataRowsPair);
			}
		}

		for (SubscriberDataRowsPair subscriberDataRowsPair : subscriberDataRowsPairsList) {
			if (subscriberDataRowsPair.getLastSubscriberDataRow() == null)
				manipulator.createCellsRow(subscriberDataRowsPair.getFirstSubscriberDataRow().getCellsArray(), false);
			else
				manipulator.createCellsRow(subscriberDataRowsPair.getFirstSubscriberDataRow().getCellsArray(subscriberDataRowsPair.getLastSubscriberDataRow()), false);
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
	 * @return the lineClass
	 */
	public SubscriberLineClass getLineClass() {
		return lineClass;
	}

	/**
	 * @param lineClass the lineClass to set
	 */
	public void setLineClass(SubscriberLineClass lineClass) {
		this.lineClass = lineClass;
	}

	/**
	 * @return the restriction
	 */
	public SubscriberRestriction getRestriction() {
		return restriction;
	}

	/**
	 * @param restriction the restriction to set
	 */
	public void setRestriction(SubscriberRestriction restriction) {
		this.restriction = restriction;
	}

	/**
	 * @return the state
	 */
	public SubscriberState getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(SubscriberState state) {
		this.state = state;
	}

	/**
	 * @return the servicesList
	 */
	public List<SubscriberService> getServicesList() {
		return servicesList;
	}

	/**
	 * @param servicesList the servicesList to set
	 */
	public void setServicesList(List<SubscriberService> servicesList) {
		this.servicesList = servicesList;
	}

	/**
	 * @return the broadbandState
	 */
	public SubscriberBroadbandState getBroadbandState() {
		return broadbandState;
	}

	/**
	 * @param broadbandState the broadbandState to set
	 */
	public void setBroadbandState(SubscriberBroadbandState broadbandState) {
		this.broadbandState = broadbandState;
	}

	/**
	 * @return the information
	 */
	public String getInformation() {
		return information;
	}

	/**
	 * @param information the information to set
	 */
	public void setInformation(String information) {
		this.information = information;
	}
}

class SubscriberDataRow {
	private int subscriberId;
	private String countryCode;
	private String areaCode;
	private String officeCode;
	private String number;
	private Technology technology;
	private String lineClassType;
	private String terminationRestrictionName;
	private String originationRestrictionName;
	private List<String> servicesList;
	private String information;
	private String stateName;
	private String routerName;
	private String broadbandUsername;
	private String broadbandStateName;
	private String username;
	private String firstName;
	private String lastName;
	private String time;

	private static ResourceBundle bundle = ResourceBundle.getBundle("i18n/WorkbooksMessagesBundle"); // NOI18N

	/**
	 * 
	 */
	public SubscriberDataRow() {
		servicesList = new ArrayList<>();
	}

	/**
	 * @param subscriberId
	 * @param countryCode
	 * @param areaCode
	 * @param officeCode
	 * @param number
	 * @param technology
	 * @param lineClassType
	 * @param terminationRestrictionName
	 * @param originationRestrictionName
	 * @param servicesList
	 * @param information
	 * @param stateName
	 * @param routerName
	 * @param broadbandUsername
	 * @param broadbandStateName
	 * @param username
	 * @param firstName
	 * @param lastName
	 * @param time
	 */
	public SubscriberDataRow(int subscriberId, String countryCode, String areaCode, String officeCode, String number, Technology technology, String lineClassType, String terminationRestrictionName, String originationRestrictionName, List<String> servicesList, String information, String stateName, String routerName, String broadbandUsername, String broadbandStateName, String username, String firstName, String lastName, String time) {
		this.subscriberId = subscriberId;
		this.countryCode = countryCode;
		this.areaCode = areaCode;
		this.officeCode = officeCode;
		this.number = number;
		this.technology = technology;
		this.lineClassType = lineClassType;
		this.terminationRestrictionName = terminationRestrictionName;
		this.originationRestrictionName = originationRestrictionName;
		this.servicesList = servicesList;
		this.information = information;
		this.stateName = stateName;
		this.routerName = routerName;
		this.broadbandUsername = broadbandUsername;
		this.broadbandStateName = broadbandStateName;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.time = time;
	}

	public SubscriberDataRow(ResultSet resultSet) throws SQLException {
		subscriberId = resultSet.getInt("subscriberId");
		countryCode = resultSet.getString("countryCode");
		areaCode = resultSet.getString("areaCode");
		officeCode = resultSet.getString("officeCode");
		number = resultSet.getString("number");

		if (resultSet.getInt("neax61sigmaId") > 0)
			technology = Technology.NEAX61SIGMA;
		if (resultSet.getInt("sigmal3addrId") > 0)
			technology = Technology.NEAX61SIGMA_ELU;
		if (resultSet.getInt("neax61eId") > 0)
			technology = Technology.NEAX61E;
		if (resultSet.getInt("zhoneId") > 0)
			technology = Technology.ZHONE;

		lineClassType = resultSet.getString("lineClassType");
		terminationRestrictionName = resultSet.getString("terminationRestrictionName");
		originationRestrictionName = resultSet.getString("originationRestrictionName");

		servicesList = new ArrayList<>();
		String servicesString = resultSet.getString("services");
		if (servicesString != null) {
			String[] servicesArray = servicesString.split("/");
			for (int i = 0; i < servicesArray.length; i++)
				servicesList.add(servicesArray[i]);
		}

		information = resultSet.getString("information");
		stateName = resultSet.getString("stateName");

		routerName = resultSet.getString("routerName");

		broadbandUsername = resultSet.getString("broadbandUsername");
		broadbandStateName = resultSet.getString("broadbandStateName");

		username = resultSet.getString("username");
		firstName = resultSet.getString("firstName");
		lastName = resultSet.getString("lastName");
		time = resultSet.getString("time");
	}

	public String[] getCellsArray() {
		String[] cellsArray = new String[18];
		cellsArray[0] = countryCode;
		cellsArray[1] = areaCode;
		cellsArray[2] = officeCode;
		cellsArray[3] = number;
		cellsArray[4] = technology.toString();
		cellsArray[5] = bundle.getString("Workbooks.lineClassType." + lineClassType);
		cellsArray[6] = terminationRestrictionName;
		cellsArray[7] = originationRestrictionName;
		cellsArray[8] = getServicesListAsString();
		cellsArray[9] = information;
		cellsArray[10] = bundle.getString("Workbooks.state." + stateName);
		cellsArray[11] = routerName;
		cellsArray[12] = broadbandUsername;
		cellsArray[13] = (broadbandStateName != null) ? bundle.getString("Workbooks.state." + broadbandStateName) : "";
		cellsArray[14] = username;
		cellsArray[15] = firstName;
		cellsArray[16] = lastName;
		cellsArray[17] = time;

		return cellsArray;
	}

	public String[] getCellsArray(SubscriberDataRow subscriberDataRow) {
		String[] cellsArray = new String[18];
		cellsArray[0] = countryCode;
		cellsArray[1] = areaCode;
		cellsArray[2] = officeCode;
		cellsArray[3] = number;

		cellsArray[4] = subscriberDataRow.technology.toString();

		cellsArray[5] = "";
		if (!subscriberDataRow.lineClassType.equals(this.lineClassType))
			cellsArray[5] = bundle.getString("Workbooks.lineClassType." + subscriberDataRow.lineClassType);

		cellsArray[6] = "";
		if (!subscriberDataRow.terminationRestrictionName.equals(this.terminationRestrictionName))
			cellsArray[6] = subscriberDataRow.terminationRestrictionName;

		cellsArray[7] = "";
		if (!subscriberDataRow.originationRestrictionName.equals(this.originationRestrictionName))
			cellsArray[7] = subscriberDataRow.originationRestrictionName;

		StringBuilder servicesModification = new StringBuilder();
		for (String service : subscriberDataRow.servicesList) {
			if (!this.servicesList.contains(service)) {
				servicesModification.append("+");
				servicesModification.append(service);
			}
		}
		for (String service : this.servicesList) {
			if (!subscriberDataRow.servicesList.contains(service)) {
				servicesModification.append("-");
				servicesModification.append(service);
			}
		}
		cellsArray[8] = servicesModification.toString();

		cellsArray[9] = "";
		if ((subscriberDataRow.information != null) && (!subscriberDataRow.information.equals(this.information)))
			cellsArray[9] = subscriberDataRow.information;

		cellsArray[10] = "";
		if (!subscriberDataRow.stateName.equals(this.stateName)) {
			cellsArray[10] = bundle.getString("Workbooks.state." + subscriberDataRow.stateName);
			if (subscriberDataRow.stateName.equals(SubscriberStateName.ENABLED.toString())) {
				cellsArray[6] = subscriberDataRow.terminationRestrictionName;
				cellsArray[7] = subscriberDataRow.originationRestrictionName;
			}
		}

		cellsArray[11] = "";
		cellsArray[12] = "";
		cellsArray[13] = "";
		if (subscriberDataRow.broadbandStateName != null) {
			if (!subscriberDataRow.broadbandStateName.equals(this.broadbandStateName)) {
				cellsArray[11] = (subscriberDataRow.routerName != null) ? subscriberDataRow.routerName : this.routerName;
				cellsArray[12] = (subscriberDataRow.broadbandUsername != null) ? subscriberDataRow.broadbandUsername : this.broadbandUsername;
				cellsArray[13] = bundle.getString("Workbooks.state." + subscriberDataRow.broadbandStateName);
			}
		} else if (this.broadbandStateName != null) {
			cellsArray[11] = this.routerName;
			cellsArray[12] = this.broadbandUsername;
		}

		cellsArray[14] = subscriberDataRow.username;
		cellsArray[15] = subscriberDataRow.firstName;
		cellsArray[16] = subscriberDataRow.lastName;
		cellsArray[17] = subscriberDataRow.time;

		return cellsArray;
	}

	private String getServicesListAsString() {
		StringBuilder servicesListAsString = new StringBuilder();
		for (int i = 0; i < servicesList.size(); i++) {
			if (i == 0)
				servicesListAsString.append(servicesList.get(i));
			else {
				servicesListAsString.append("/");
				servicesListAsString.append(servicesList.get(i));
			}
		}
		return servicesListAsString.toString();
	}

	/**
	 * @return the subscriberId
	 */
	public int getSubscriberId() {
		return subscriberId;
	}

	/**
	 * @param subscriberId the subscriberId to set
	 */
	public void setSubscriberId(int subscriberId) {
		this.subscriberId = subscriberId;
	}

	/**
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * @param countryCode the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * @return the areaCode
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * @param areaCode the areaCode to set
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	/**
	 * @return the officeCode
	 */
	public String getOfficeCode() {
		return officeCode;
	}

	/**
	 * @param officeCode the officeCode to set
	 */
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
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
	}

	/**
	 * @return the technology
	 */
	public Technology getTechnology() {
		return technology;
	}

	/**
	 * @param technology the technology to set
	 */
	public void setTechnology(Technology technology) {
		this.technology = technology;
	}

	/**
	 * @return the lineClassType
	 */
	public String getLineClassType() {
		return lineClassType;
	}

	/**
	 * @param lineClassType the lineClassType to set
	 */
	public void setLineClassType(String lineClassType) {
		this.lineClassType = lineClassType;
	}

	/**
	 * @return the terminationRestrictionName
	 */
	public String getTerminationRestrictionName() {
		return terminationRestrictionName;
	}

	/**
	 * @param terminationRestrictionName the terminationRestrictionName to set
	 */
	public void setTerminationRestrictionName(String terminationRestrictionName) {
		this.terminationRestrictionName = terminationRestrictionName;
	}

	/**
	 * @return the originationRestrictionName
	 */
	public String getOriginationRestrictionName() {
		return originationRestrictionName;
	}

	/**
	 * @param originationRestrictionName the originationRestrictionName to set
	 */
	public void setOriginationRestrictionName(String originationRestrictionName) {
		this.originationRestrictionName = originationRestrictionName;
	}

	/**
	 * @return the servicesList
	 */
	public List<String> getServicesList() {
		return servicesList;
	}

	/**
	 * @param servicesList the servicesList to set
	 */
	public void setServicesList(List<String> servicesList) {
		this.servicesList = servicesList;
	}

	/**
	 * @return the information
	 */
	public String getInformation() {
		return information;
	}

	/**
	 * @param information the information to set
	 */
	public void setInformation(String information) {
		this.information = information;
	}

	/**
	 * @return the stateName
	 */
	public String getStateName() {
		return stateName;
	}

	/**
	 * @param stateName the stateName to set
	 */
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	/**
	 * @return the routerName
	 */
	public String getRouterName() {
		return routerName;
	}

	/**
	 * @param routerName the routerName to set
	 */
	public void setRouterName(String routerName) {
		this.routerName = routerName;
	}

	/**
	 * @return the broadbandUsername
	 */
	public String getBroadbandUsername() {
		return broadbandUsername;
	}

	/**
	 * @param broadbandUsername the broadbandUsername to set
	 */
	public void setBroadbandUsername(String broadbandUsername) {
		this.broadbandUsername = broadbandUsername;
	}

	/**
	 * @return the broadbandStateName
	 */
	public String getBroadbandStateName() {
		return broadbandStateName;
	}

	/**
	 * @param broadbandStateName the broadbandStateName to set
	 */
	public void setBroadbandStateName(String broadbandStateName) {
		this.broadbandStateName = broadbandStateName;
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		this.time = time;
	}
}

class SubscriberDataRowsPair {
	SubscriberDataRow firstSubscriberDataRow;
	SubscriberDataRow lastSubscriberDataRow;

	/**
	 * @param firstSubscriberDataRow
	 */
	public SubscriberDataRowsPair(SubscriberDataRow firstSubscriberDataRow) {
		this.firstSubscriberDataRow = firstSubscriberDataRow;
	}

	/**
	 * @param firstSubscriberDataRow
	 * @param lastSubscriberDataRow
	 */
	public SubscriberDataRowsPair(SubscriberDataRow firstSubscriberDataRow, SubscriberDataRow lastSubscriberDataRow) {
		this.firstSubscriberDataRow = firstSubscriberDataRow;
		this.lastSubscriberDataRow = lastSubscriberDataRow;
	}

	public int getSubscriberId() {
		return firstSubscriberDataRow.getSubscriberId();
	}

	/**
	 * @return the firstSubscriberDataRow
	 */
	public SubscriberDataRow getFirstSubscriberDataRow() {
		return firstSubscriberDataRow;
	}

	/**
	 * @param firstSubscriberDataRow the firstSubscriberDataRow to set
	 */
	public void setFirstSubscriberDataRow(SubscriberDataRow firstSubscriberDataRow) {
		this.firstSubscriberDataRow = firstSubscriberDataRow;
	}

	/**
	 * @return the lastSubscriberDataRow
	 */
	public SubscriberDataRow getLastSubscriberDataRow() {
		return lastSubscriberDataRow;
	}

	/**
	 * @param lastSubscriberDataRow the lastSubscriberDataRow to set
	 */
	public void setLastSubscriberDataRow(SubscriberDataRow lastSubscriberDataRow) {
		this.lastSubscriberDataRow = lastSubscriberDataRow;
	}
}