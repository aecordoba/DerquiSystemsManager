/*
 * 		ServiceOrder.java
 *   Copyright (C) 2018  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		ServiceOrder.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Feb 9, 2018
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;

import ar.com.adriancordoba.app.console.commonservices.excel2007.ExcelFileManipulator;
import ar.com.adriancordoba.app.console.commonservices.excel2007.ExcelFileManipulator.SpanedCell;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors.Relationship;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.IdentificationType;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.Person;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.Address;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.managers.IdentificationsTypesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Phone;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.managers.OfficeCodesDataManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs.managers.RepairingTypesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriber.Subscriber;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.User;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class ServiceOrder {
	private int id;
	private Subscriber subscriber;
	private RepairingType repairingType;
	private String remarks;
	private String contact;
	private LocalDateTime creationTime;
	private User user;
	private List<Repairing> repairingsList = new ArrayList<>();

	private static ResourceBundle bundle = ResourceBundle.getBundle("i18n/WorkbooksMessagesBundle"); // NOI18N
	private static final Logger logger = LogManager.getLogger(ServiceOrder.class);

	/**
	 * 
	 */
	public ServiceOrder() {
	}

	/**
	 * @param subscriber
	 * @param repairingType
	 * @param remarks
	 * @param contact
	 * @param creationTime
	 * @param repairingsList
	 */
	public ServiceOrder(Subscriber subscriber, RepairingType repairingType, String remarks, String contact, LocalDateTime creationTime, List<Repairing> repairingsList) {
		this.subscriber = subscriber;
		this.repairingType = repairingType;
		this.remarks = remarks;
		this.contact = contact;
		this.creationTime = creationTime;
		this.repairingsList = repairingsList;
	}

	public ServiceOrder(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("serviceOrderId"));
		setSubscriber(new Subscriber(resultSet));
		setRepairingType(RepairingTypesManager.getRepairingType(resultSet.getInt("repairingTypeId")));
		setRemarks(resultSet.getString("serviceOrderRemarks"));
		setContact(resultSet.getString("contact"));
		setCreationTime(resultSet.getTimestamp("creationTime").toLocalDateTime());

		Person person = new Person();
		person.setFirstName(resultSet.getString("userFirstName"));
		person.setMiddleName(resultSet.getString("userMiddleName"));
		person.setLastName(resultSet.getString("userLastName"));
		User user = new User();
		user.setId(resultSet.getInt("userId"));
		user.setPerson(person);
		setUser(user);

		if (resultSet.getInt("repairingId") > 0)
			addRepairing(new Repairing(resultSet));
	}

	public static List<ServiceOrder> getServiceOrdersList(Connection connection) throws Exception {
		List<ServiceOrder> serviceOrdersList = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getServiceOrdersList()}");
			ResultSet resultSet = statement.executeQuery();
			serviceOrdersList = processResultSet(resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getServiceOrdersList().", e);
			throw new Exception("Cannot get service orders list from database.");
		} finally {
			statement.close();
		}
		return serviceOrdersList;
	}

	private static List<ServiceOrder> processResultSet(ResultSet resultSet) throws SQLException {
		List<ServiceOrder> serviceOrdersList = new ArrayList<>();
		while (resultSet.next()) {
			ServiceOrder serviceOrder = new ServiceOrder();
			serviceOrder.setId(resultSet.getInt("serviceOrderId"));

			Subscriber subscriber = new Subscriber();
			subscriber.setId(resultSet.getInt("subscriberId"));
			Address address = new Address();
			Phone phone = new Phone();
			phone.setNumber(resultSet.getString("phoneNumber"));
			phone.setOfficeCode(OfficeCodesDataManager.getOfficeCode(resultSet.getInt("officeCodeId")));
			address.setPhone(phone);
			subscriber.setAddress(address);
			serviceOrder.setSubscriber(subscriber);

			serviceOrdersList.add(serviceOrder);
		}
		return serviceOrdersList;
	}

	private static List<ServiceOrder> getServiceOrderDataList(Connection connection, int id) throws Exception {
		List<ServiceOrder> serviceOrderDataList = new ArrayList<>();
		CallableStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareCall("{call getServiceOrder(?)}");
			statement.setInt(1, id);
			resultSet = statement.executeQuery();
			while (resultSet.next()) {
				ServiceOrder serviceOrder = new ServiceOrder(resultSet);
				serviceOrderDataList.add(serviceOrder);
			}
			logger.debug("Service order " + id + " requested.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getServiceOrder().", e);
			throw new Exception("Cannot get service order (" + id + ") data list from database.");
		} finally {
			statement.close();
		}
		return serviceOrderDataList;
	}

	public static ServiceOrder getServiceOrderLastData(Connection connection, int id) throws Exception {
		ServiceOrder serviceOrderLastData = null;
		List<ServiceOrder> serviceOrderDataList = getServiceOrderDataList(connection, id);
		if (!serviceOrderDataList.isEmpty())
			serviceOrderLastData = serviceOrderDataList.get(serviceOrderDataList.size() - 1);
		return serviceOrderLastData;
	}

	public int insert(Connection connection) throws Exception {
		int serviceOrderId = 0;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call insertServiceOrder(?, ?, ?, ?, ?, ?, ?)}");
			Subscriber subscriber = getSubscriber();
			statement.setInt(1, subscriber.getId());
			statement.setInt(2, getRepairingType().getId());
			statement.setString(3, getRemarks());
			statement.setString(4, getContact());
			Repairman repairman = null;
			for (Repairing repairing : repairingsList)
				repairman = repairing.getRepairman();
			if (repairman != null)
				statement.setInt(5, repairman.getId());
			else
				statement.setInt(5, 0);
			statement.setInt(6, getUser().getId());
			statement.registerOutParameter(7, java.sql.Types.INTEGER);

			statement.execute();
			serviceOrderId = statement.getInt(7);
			logger.debug("Service order for subscriber " + subscriber + " was inserted in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure insertServiceOrder().", e);
			throw new Exception("Cannot insert service order in database.");
		} finally {
			statement.close();
		}
		return serviceOrderId;
	}

	public void modify(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call modifyServiceOrder(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			Subscriber subscriber = getSubscriber();
			statement.setInt(1, getId());
			statement.setInt(2, getRepairingType().getId());
			statement.setString(3, getRemarks());
			statement.setString(4, getContact());

			Repairing lastRepairing = getLastRepairing();
			RepairingCheck lastRepairingCheck = null;
			if (lastRepairing != null) {
				statement.setInt(5, lastRepairing.getRepairman().getId());
				boolean repaired = (lastRepairing.getRepairedDate() != null) ? true : false;
				statement.setBoolean(6, repaired);
				statement.setString(7, lastRepairing.getRepairmanRemarks());

				lastRepairingCheck = lastRepairing.getLastRepairingCheck();
			} else {
				statement.setInt(5, 0);
				statement.setBoolean(6, false);
				statement.setString(7, null);
			}

			if (lastRepairingCheck != null) {
				statement.setString(8, lastRepairingCheck.getRemarks());
				statement.setInt(9, lastRepairingCheck.getUser().getId());
				statement.setBoolean(10, lastRepairingCheck.isApproved());
			} else {
				statement.setString(8, null);
				statement.setInt(9, 0);
				statement.setBoolean(10, false);
			}

			statement.execute();
			logger.debug("Service order " + getId() + " for subscriber " + subscriber + " was modified in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure modifyServiceOrder().", e);
			throw new Exception("Cannot modify service order in database.");
		} finally {
			statement.close();
		}
	}

	public static Workbook getReport(Connection connection, int serviceOrderNumber) throws Exception {
		Workbook workbook = null;
		CallableStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareCall("{call getServiceOrderReportByNumber(?)}");
			statement.setInt(1, serviceOrderNumber);
			resultSet = statement.executeQuery();
			workbook = getReportByNumberWorkbook(bundle.getString("Workbooks.SERVICE_ORDER") + " " + String.valueOf(serviceOrderNumber), resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getServiceOrderReportByNumber().", e);
			throw new Exception("Cannot get service order report from database.");
		} finally {
			statement.close();
		}
		return workbook;
	}

	private static Workbook getReportByNumberWorkbook(String spreadsheetName, ResultSet resultSet) throws SQLException {
		ExcelFileManipulator manipulator = new ExcelFileManipulator(spreadsheetName);

		// Headers
		SpanedCell[] spanedHeaderCellsArray = { manipulator.new SpanedCell(bundle.getString("Workbooks.SUBSCRIBER"), 0, 3), manipulator.new SpanedCell(bundle.getString("Workbooks.SERVICE_ORDER"), 4, 8), manipulator.new SpanedCell(bundle.getString("Workbooks.REPAIR"), 9, 13), manipulator.new SpanedCell(bundle.getString("Workbooks.CHECK"), 14, 17) };
		manipulator.createSpanedCellsRow(spanedHeaderCellsArray, true);

		String[] headerCellsArray = { bundle.getString("Workbooks.COUNTRY_CODE"), bundle.getString("Workbooks.AREA_CODE"), bundle.getString("Workbooks.OFFICE_CODE"), bundle.getString("Workbooks.NUMBER"), bundle.getString("Workbooks.TYPE"), bundle.getString("Workbooks.REMARKS"), bundle.getString("Workbooks.CONTACT"), bundle.getString("Workbooks.CREATION"), bundle.getString("Workbooks.AUTHOR"), bundle.getString("Workbooks.TECHNICIAN"), bundle.getString("Workbooks.COMPANY"), bundle.getString("Workbooks.ASSIGNMENT"), bundle.getString("Workbooks.REPAIRED"), bundle.getString("Workbooks.REMARKS"), bundle.getString("Workbooks.STATUS"), bundle.getString("Workbooks.TIME"), bundle.getString("Workbooks.REMARKS"), bundle.getString("Workbooks.CHECKER") };
		manipulator.createCellsRow(headerCellsArray, true);

		// Content
		while (resultSet.next()) {
			String[] cellsArray = new String[18];
			cellsArray[0] = resultSet.getString("countryCode");
			cellsArray[1] = resultSet.getString("areaCode");
			cellsArray[2] = resultSet.getString("officeCode");
			cellsArray[3] = resultSet.getString("phoneNumber");

			cellsArray[4] = bundle.getString("Workbooks.repairingType." + RepairingTypesManager.getRepairingType(resultSet.getInt("repairingTypeId")).getName());
			cellsArray[5] = resultSet.getString("serviceOrderRemarks");
			cellsArray[6] = resultSet.getString("contact");
			cellsArray[7] = resultSet.getString("creationTime");
			cellsArray[8] = composeName(resultSet.getString("creatorFirstName"), resultSet.getString("creatorMiddleName"), resultSet.getString("creatorLastName"));

			cellsArray[9] = composeName(resultSet.getString("repairmanFirstName"), resultSet.getString("repairmanMiddleName"), resultSet.getString("repairmanLastName"));
			cellsArray[10] = resultSet.getString("repairsCompany");
			cellsArray[11] = resultSet.getString("assignmentTime");
			cellsArray[12] = resultSet.getString("repairedDate");
			cellsArray[13] = resultSet.getString("repairmanRemarks");

			String repairingCheckTime = resultSet.getString("repairingCheckTime");
			if (repairingCheckTime != null) {
				String status = (resultSet.getBoolean("approved")) ? bundle.getString("Workbooks.status.APPROVED") : bundle.getString("Workbooks.status.REJECTED");
				cellsArray[14] = status;
				cellsArray[15] = repairingCheckTime;
			} else {
				cellsArray[14] = "";
				cellsArray[15] = "";
			}
			cellsArray[16] = resultSet.getString("repairingCheckRemarks");
			cellsArray[17] = composeName(resultSet.getString("checkerFirstName"), resultSet.getString("checkerMiddleName"), resultSet.getString("checkerLastName"));

			manipulator.createCellsRow(cellsArray, false);
		}
		return manipulator.getWorkbook();
	}

	public static Workbook getReport(Connection connection, Phone phone) throws Exception {
		Workbook workbook = null;
		CallableStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareCall("{call getServiceOrdersReportByPhone(?, ?)}");
			statement.setInt(1, phone.getOfficeCode().getId());
			statement.setString(2, phone.getNumber());
			resultSet = statement.executeQuery();
			workbook = getReportByPhoneWorkbook(phone.getFullNumeration(), resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getServiceOrdersReportByPhone().", e);
			throw new Exception("Cannot get service orders report from database.");
		} finally {
			statement.close();
		}
		return workbook;
	}

	private static Workbook getReportByPhoneWorkbook(String spreadsheetName, ResultSet resultSet) throws SQLException {
		ExcelFileManipulator manipulator = new ExcelFileManipulator(spreadsheetName);

		// Headers
		SpanedCell[] spanedHeaderCellsArray = { manipulator.new SpanedCell(bundle.getString("Workbooks.SERVICE_ORDER"), 0, 5), manipulator.new SpanedCell(bundle.getString("Workbooks.REPAIR"), 6, 10), manipulator.new SpanedCell(bundle.getString("Workbooks.CHECK"), 11, 14) };
		manipulator.createSpanedCellsRow(spanedHeaderCellsArray, true);

		String[] headerCellsArray = { bundle.getString("Workbooks.NUMBER"), bundle.getString("Workbooks.TYPE"), bundle.getString("Workbooks.REMARKS"), bundle.getString("Workbooks.CONTACT"), bundle.getString("Workbooks.CREATION"), bundle.getString("Workbooks.AUTHOR"), bundle.getString("Workbooks.TECHNICIAN"), bundle.getString("Workbooks.COMPANY"), bundle.getString("Workbooks.ASSIGNMENT"), bundle.getString("Workbooks.REPAIRED"), bundle.getString("Workbooks.REMARKS"), bundle.getString("Workbooks.STATUS"), bundle.getString("Workbooks.TIME"), bundle.getString("Workbooks.REMARKS"), bundle.getString("Workbooks.CHECKER") };
		manipulator.createCellsRow(headerCellsArray, true);

		// Content
		while (resultSet.next()) {
			String[] cellsArray = new String[15];
			cellsArray[0] = resultSet.getString("serviceOrderId");
			cellsArray[1] = bundle.getString("Workbooks.repairingType." + RepairingTypesManager.getRepairingType(resultSet.getInt("repairingTypeId")).getName());
			cellsArray[2] = resultSet.getString("serviceOrderRemarks");
			cellsArray[3] = resultSet.getString("contact");
			cellsArray[4] = resultSet.getString("creationTime");
			cellsArray[5] = composeName(resultSet.getString("creatorFirstName"), resultSet.getString("creatorMiddleName"), resultSet.getString("creatorLastName"));

			cellsArray[6] = composeName(resultSet.getString("repairmanFirstName"), resultSet.getString("repairmanMiddleName"), resultSet.getString("repairmanLastName"));
			cellsArray[7] = resultSet.getString("repairsCompany");
			cellsArray[8] = resultSet.getString("assignmentTime");
			cellsArray[9] = resultSet.getString("repairedDate");
			cellsArray[10] = resultSet.getString("repairmanRemarks");

			String repairingCheckTime = resultSet.getString("repairingCheckTime");
			if (repairingCheckTime != null) {
				String status = (resultSet.getBoolean("approved")) ? bundle.getString("Workbooks.status.APPROVED") : bundle.getString("Workbooks.status.REJECTED");
				cellsArray[11] = status;
				cellsArray[12] = repairingCheckTime;
			} else {
				cellsArray[11] = "";
				cellsArray[12] = "";
			}
			cellsArray[13] = resultSet.getString("repairingCheckRemarks");
			cellsArray[14] = composeName(resultSet.getString("checkerFirstName"), resultSet.getString("checkerMiddleName"), resultSet.getString("checkerLastName"));

			manipulator.createCellsRow(cellsArray, false);
		}
		return manipulator.getWorkbook();
	}

	private static String composeName(String firstName, String middleName, String lastName) {
		StringBuilder builder = new StringBuilder();
		if (firstName != null)
			builder.append(firstName);
		if (middleName != null) {
			if (firstName != null)
				builder.append(" ");
			builder.append(middleName);
		}
		if (lastName != null) {
			if ((firstName != null) || (middleName != null))
				builder.append(" ");
			builder.append(lastName);
		}
		return builder.toString();
	}

	public void addRepairing(Repairing repairing) {
		repairingsList.add(repairing);
	}

	public Repairing getLastRepairing() {
		Repairing repairing = null;
		if (!repairingsList.isEmpty())
			repairing = repairingsList.get(repairingsList.size() - 1);
		return repairing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(String.valueOf(getId()));
		stringBuilder.append(" (");
		stringBuilder.append(getSubscriber().getAddress().getPhone());
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

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the repairingType
	 */
	public RepairingType getRepairingType() {
		return repairingType;
	}

	/**
	 * @param repairingType the repairingType to set
	 */
	public void setRepairingType(RepairingType repairingType) {
		this.repairingType = repairingType;
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
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * @return the creationTime
	 */
	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	/**
	 * @param creationTime the time to set
	 */
	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the repairingsList
	 */
	public List<Repairing> getRepairingsList() {
		return repairingsList;
	}

	/**
	 * @param repairingsList the repairingsList to set
	 */
	public void setRepairingsList(List<Repairing> repairingsList) {
		this.repairingsList = repairingsList;
	}
}
