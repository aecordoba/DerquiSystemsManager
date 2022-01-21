/*
 * 		Subscriber.java
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
 * 		Subscriber.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Dec 17, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriber;

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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ar.com.adriancordoba.app.console.commonservices.excel2007.ExcelFileManipulator;
import ar.com.adriancordoba.app.console.commonservices.excel2007.ExcelFileManipulator.SpanedCell;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.Broadband;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.BroadbandPort;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAM;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMBoard;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors.Relationship;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors.Technology;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.BlockPosition;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Distributor;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.SwitchBlock;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.managers.SitesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Equipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Neax61EEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Neax61SigmaEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.SigmaL3AddressEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.ZhoneEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.IdentificationType;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.Person;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.Address;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.Street;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.managers.StreetsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.managers.IdentificationsTypesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Area;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.OfficeCode;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Phone;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.managers.OfficeCodesDataManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.SubscriberData;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.managers.SubscriberServicesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.restriction.SubscriberRestriction;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetCable;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetFrame;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetPair;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.Wiring;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Subscriber {
	private int id;
	private Equipment equipment;
	private Address address;
	private Person person;
	private Relationship relationship;
	private SubscriberData data;
	private String remarks;

	private boolean wired;
	private boolean broadband;

	private static ResourceBundle bundle = ResourceBundle.getBundle("i18n/WorkbooksMessagesBundle"); // NOI18N
	private static final Logger logger = LogManager.getLogger(Subscriber.class);

	/**
	 * 
	 */
	public Subscriber() {
	}

	/**
	 * @param equipment
	 * @param address
	 * @param person
	 * @param relationship
	 * @param data
	 * @param remarks
	 */
	public Subscriber(Equipment equipment, Address address, Person person, Relationship relationship, SubscriberData data, String remarks) {
		this.equipment = equipment;
		this.address = address;
		this.person = person;
		this.relationship = relationship;
		this.data = data;
		this.remarks = remarks;
	}

	public Subscriber(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("subscriberId"));
		setAddress(new Address(resultSet));

		if (resultSet.getString("ownerPersonId") != null) {
			setRelationship(Relationship.OWNER);
			setPerson(getOwnerPerson(resultSet));
		} else if (resultSet.getString("assigneePersonId") != null) {
			setRelationship(Relationship.ASSIGNEE);
			setPerson(getAssigneePerson(resultSet));
		}

		if (resultSet.getInt("broadbandId") != 0)
			setBroadband(true);
	}

	public static List<Subscriber> getSubscribersList(Connection connection) throws Exception {
		List<Subscriber> subscribersList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getSubscribersList()}");
			ResultSet resultSet = statement.executeQuery();
			subscribersList = processResultSet(resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getSubscribersList().", e);
			throw new Exception("Cannot get subscribers list from database.");
		} finally {
			statement.close();
		}
		return subscribersList;
	}

	private static List<Subscriber> processResultSet(ResultSet resultSet) throws SQLException {
		List<Subscriber> subscribersList = new ArrayList<>();

		while (resultSet.next()) {
			Subscriber subscriber = new Subscriber();
			subscriber.setId(resultSet.getInt("subscriberId"));

			Equipment equipment = null;
			if (resultSet.getInt("neax61sigmaId") != 0)
				equipment = new Neax61SigmaEquipment(resultSet, true);
			if (resultSet.getInt("sigmal3addrId") != 0)
				equipment = new SigmaL3AddressEquipment(resultSet);
			else if (resultSet.getInt("neax61eId") != 0)
				equipment = new Neax61EEquipment(resultSet, true);
			else if (resultSet.getInt("zhoneId") != 0)
				equipment = new ZhoneEquipment(resultSet, true);
			subscriber.setEquipment(equipment);

			subscriber.setAddress(new Address(resultSet));

			if (resultSet.getString("ownerPersonId") != null) {
				subscriber.setRelationship(Relationship.OWNER);
				subscriber.setPerson(getOwnerPerson(resultSet));
			} else if (resultSet.getString("assigneePersonId") != null) {
				subscriber.setRelationship(Relationship.ASSIGNEE);
				subscriber.setPerson(getAssigneePerson(resultSet));
			}

			subscriber.setRemarks(resultSet.getString("remarks"));
			subscribersList.add(subscriber);

			if (resultSet.getInt("wiringId") > 0)
				subscriber.setWired(true);
		}
		return subscribersList;
	}

	private static Person getOwnerPerson(ResultSet resultSet) throws SQLException {
		Person person = new Person();
		person.setId(resultSet.getInt("ownerPersonId"));
		person.setFirstName(resultSet.getString("ownerFirstName"));
		person.setMiddleName(resultSet.getString("ownerMiddleName"));
		person.setLastName(resultSet.getString("ownerLastName"));
		int identificationTypeId = resultSet.getInt("ownerIdentificationTypeId");
		if (identificationTypeId != 0) {
			IdentificationType identificationType = IdentificationsTypesManager.getIdentificationType(identificationTypeId);
			person.setIdentificationType(identificationType);
			person.setIdentificationNumber(resultSet.getInt("ownerIdentificationNumber"));
		}
		return person;
	}

	private static Person getAssigneePerson(ResultSet resultSet) throws SQLException {
		Person person = new Person();
		person.setId(resultSet.getInt("assigneePersonId"));
		person.setFirstName(resultSet.getString("assigneeFirstName"));
		person.setMiddleName(resultSet.getString("assigneeMiddleName"));
		person.setLastName(resultSet.getString("assigneeLastName"));
		int identificationTypeId = resultSet.getInt("assigneeIdentificationTypeId");
		if (identificationTypeId != 0) {
			IdentificationType identificationType = IdentificationsTypesManager.getIdentificationType(identificationTypeId);
			person.setIdentificationType(identificationType);
			person.setIdentificationNumber(resultSet.getInt("assigneeIdentificationNumber"));
		}
		return person;
	}

	public static List<Subscriber> getSubscribersDataList(Connection connection) throws Exception {
		List<Subscriber> subscribersList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getSubscribersDataList()}");
			ResultSet resultSet = statement.executeQuery();
			subscribersList = processDataResultSet(resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getSubscribersDataList().", e);
			throw new Exception("Cannot get subscribers data list from database.");
		} finally {
			statement.close();
		}
		return subscribersList;
	}

	private static List<Subscriber> processDataResultSet(ResultSet resultSet) throws SQLException {
		List<Subscriber> subscribersList = new ArrayList<>();

		while (resultSet.next()) {
			Subscriber subscriber = new Subscriber();
			subscriber.setId(resultSet.getInt("subscriberId"));

			SubscriberData data = new SubscriberData();
			data.setId(resultSet.getInt("dataId"));
			subscriber.setData(data);

			Address address = new Address();
			Phone phone = new Phone();
			phone.setId(resultSet.getInt("phoneNumberId"));
			phone.setNumber(resultSet.getString("phoneNumber"));
			phone.setOfficeCode(OfficeCodesDataManager.getOfficeCode(resultSet.getInt("phoneOfficeCodeId")));
			address.setPhone(phone);
			subscriber.setAddress(address);

			subscribersList.add(subscriber);
		}
		return subscribersList;
	}

	public void fillSubscriberData(Connection connection) throws Exception {
		CallableStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareCall("{call getSubscriberData(?)}");
			statement.setInt(1, getId());
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				setPerson(new Person(resultSet));
				data.fillData(resultSet);
				while (resultSet.next()) {
					int serviceId = resultSet.getInt("serviceId");
					if (serviceId != 0)
						data.getServicesList().add(SubscriberServicesManager.getSubscriberService(serviceId));
				}
			}
			logger.debug("Subscriber data requested for " + getPhoneNumber() + ".");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getSubscriberData().", e);
			throw new Exception("Cannot get subscriber data (" + getPhoneNumber() + ") from database.");
		} finally {
			statement.close();
		}
	}

	public static Subscriber getSubscriberInformation(Connection connection, Phone phone) throws Exception {
		Subscriber subscriber = null;
		CallableStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareCall("{call getSubscriberInformation(?, ?)}");
			statement.setInt(1, phone.getOfficeCode().getId());
			statement.setString(2, phone.getNumber());
			resultSet = statement.executeQuery();
			if (resultSet.next()) {
				subscriber = new Subscriber(resultSet);
				logger.debug("Subscriber information requested for " + phone.getFullNumeration() + ".");
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getSubscriberInformation().", e);
			throw new Exception("Cannot get subscriber information (" + phone.getFullNumeration() + ") from database.");
		} finally {
			statement.close();
		}
		return subscriber;
	}

	public void insert(Connection connection, int userId) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call insertSubscriber(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			Phone phone = address.getPhone();
			statement.setInt(1, phone.getOfficeCode().getId());
			statement.setString(2, phone.getNumber());

			statement.setString(3, getTechnology().toString());
			statement.setInt(4, equipment.getId());

			statement.setString(5, relationship.toString());

			if (person != null) {
				int personId = person.getId();
				statement.setInt(6, personId);
				statement.setString(7, person.getFirstName());
				statement.setString(8, person.getMiddleName());
				statement.setString(9, person.getLastName());
				IdentificationType identificationType = person.getIdentificationType();
				if (identificationType != null) {
					statement.setInt(10, person.getIdentificationType().getId());
					statement.setInt(11, person.getIdentificationNumber());
				} else {
					statement.setInt(10, 0);
					statement.setInt(11, 0);
				}
			} else {
				statement.setInt(6, 0);
				statement.setString(7, null);
				statement.setString(8, null);
				statement.setString(9, null);
				statement.setInt(10, 0);
				statement.setInt(11, 0);
			}

			int addressId = address.getId();
			statement.setInt(12, addressId);

			Street street = address.getStreet();
			int streetId = (street == null) ? 0 : street.getId();
			statement.setInt(13, streetId);

			statement.setString(14, address.getNumber());
			statement.setString(15, address.getFloor());
			statement.setString(16, address.getApartment());

			Street street1 = address.getStreet1();
			int street1Id = (street1 == null) ? 0 : street1.getId();
			statement.setInt(17, street1Id);

			Street street2 = address.getStreet2();
			int street2Id = (street2 == null) ? 0 : street2.getId();
			statement.setInt(18, street2Id);

			statement.setString(19, address.getZipCode());
			statement.setInt(20, address.getLocationId());

			statement.setString(21, getRemarks());

			statement.setString(22, data.getLineClass().getType().toString().toUpperCase());
			SubscriberRestriction restriction = data.getRestriction();
			statement.setString(23, String.valueOf(restriction.getTerminationRestriction().getName()));
			statement.setString(24, restriction.getOriginationRestriction().getName());
			statement.setString(25, data.getState().getName().toString().toUpperCase());

			statement.setInt(26, userId);

			statement.execute();
			logger.debug(this + " was inserted in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure insertSubscriber().", e);
			throw new Exception("Cannot insert subscriber in database.");
		} finally {
			statement.close();
		}
	}

	public void update(Connection connection, int userId) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call updateSubscriber(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			statement.setInt(1, getId());

			statement.setString(2, getTechnology().toString());
			statement.setInt(3, equipment.getId());

			statement.setString(4, relationship.toString());

			if (person != null) {
				int personId = person.getId();
				statement.setInt(5, personId);
				statement.setString(6, person.getFirstName());
				statement.setString(7, person.getMiddleName());
				statement.setString(8, person.getLastName());
				IdentificationType identificationType = person.getIdentificationType();
				if (identificationType != null) {
					statement.setInt(9, person.getIdentificationType().getId());
					statement.setInt(10, person.getIdentificationNumber());
				} else {
					statement.setInt(9, 0);
					statement.setInt(10, 0);
				}
			} else {
				statement.setInt(5, 0);
				statement.setString(6, null);
				statement.setString(7, null);
				statement.setString(8, null);
				statement.setInt(9, 0);
				statement.setInt(10, 0);
			}

			statement.setInt(11, address.getId());

			Street street = address.getStreet();
			int streetId = (street == null) ? 0 : street.getId();
			statement.setInt(12, streetId);

			statement.setString(13, address.getNumber());
			statement.setString(14, address.getFloor());
			statement.setString(15, address.getApartment());

			Street street1 = address.getStreet1();
			int street1Id = (street1 == null) ? 0 : street1.getId();
			statement.setInt(16, street1Id);

			Street street2 = address.getStreet2();
			int street2Id = (street2 == null) ? 0 : street2.getId();
			statement.setInt(17, street2Id);

			statement.setString(18, address.getZipCode());
			statement.setInt(19, address.getLocationId());

			statement.setString(20, getRemarks());

			statement.setInt(21, userId);

			statement.execute();
			logger.debug(this + " was modified in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure updateSubscriber().", e);
			throw new Exception("Cannot modify subscriber in database.");
		} finally {
			statement.close();
		}
	}

	public void removeAssignment(Connection connection, int userId) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call removeAssignment(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			statement.setInt(1, getId());

			statement.setString(2, relationship.toString());

			if (person != null) {
				int personId = person.getId();
				statement.setInt(3, personId);
				statement.setString(4, person.getFirstName());
				statement.setString(5, person.getMiddleName());
				statement.setString(6, person.getLastName());
				IdentificationType identificationType = person.getIdentificationType();
				if (identificationType != null) {
					statement.setInt(7, person.getIdentificationType().getId());
					statement.setInt(8, person.getIdentificationNumber());
				} else {
					statement.setInt(7, 0);
					statement.setInt(8, 0);
				}
			} else {
				statement.setInt(3, 0);
				statement.setString(4, null);
				statement.setString(5, null);
				statement.setString(6, null);
				statement.setInt(7, 0);
				statement.setInt(8, 0);
			}

			statement.setInt(9, address.getId());

			Street street = address.getStreet();
			int streetId = (street == null) ? 0 : street.getId();
			statement.setInt(10, streetId);

			statement.setString(11, address.getNumber());
			statement.setString(12, address.getFloor());
			statement.setString(13, address.getApartment());

			Street street1 = address.getStreet1();
			int street1Id = (street1 == null) ? 0 : street1.getId();
			statement.setInt(14, street1Id);

			Street street2 = address.getStreet2();
			int street2Id = (street2 == null) ? 0 : street2.getId();
			statement.setInt(15, street2Id);

			statement.setString(16, address.getZipCode());
			statement.setInt(17, address.getLocationId());

			statement.setString(18, getRemarks());

			statement.setInt(19, userId);

			statement.execute();
			logger.debug("Assignemt for " + this + " was removed from database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure removeAssignment().", e);
			throw new Exception("Cannot remove assignment from database.");
		} finally {
			statement.close();
		}
	}

	public Technology getTechnology() {
		Technology technology = null;
		if (equipment instanceof Neax61EEquipment)
			technology = Technology.NEAX61E;
		else if (equipment instanceof Neax61SigmaEquipment)
			technology = Technology.NEAX61SIGMA;
		else if (equipment instanceof SigmaL3AddressEquipment)
			technology = Technology.NEAX61SIGMA_ELU;
		else if (equipment instanceof ZhoneEquipment)
			technology = Technology.ZHONE;
		return technology;
	}

	public static Workbook getRecord(Connection connection, Phone phone) throws Exception {
		Workbook workbook = null;
		OfficeCode officeCode = phone.getOfficeCode();
		Area area = officeCode.getArea();
		CallableStatement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.prepareCall("{call getSubscriberRecordList(?, ?, ?, ?)}");
			statement.setInt(1, area.getCountry().getCode());
			statement.setInt(2, area.getCode());
			statement.setInt(3, officeCode.getCode());
			statement.setString(4, phone.getNumber());
			resultSet = statement.executeQuery();

			workbook = getSubscriberRecordWorkbook(phone, resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getSubscriberRecordList().", e);
			throw new Exception("Cannot get subscriber record list from database.");
		} finally {
			statement.close();
		}

		return workbook;
	}

	private static Workbook getSubscriberRecordWorkbook(Phone phone, ResultSet resultSet) throws SQLException {
		ExcelFileManipulator manipulator = new ExcelFileManipulator(phone.getFullNumeration());

		// Headers
		SpanedCell[] spanedHeaderCellsArray = { manipulator.new SpanedCell(bundle.getString("Workbooks.SWITCH_BLOCK"), 0, 2), manipulator.new SpanedCell("NEAX61Σ", 3, 9), manipulator.new SpanedCell("NEAX61Σ-ELU", 10, 14), manipulator.new SpanedCell("NEAX61E", 15, 22), manipulator.new SpanedCell("ZHONE", 23, 24), manipulator.new SpanedCell(bundle.getString("Workbooks.ADDRESS"), 25, 35), manipulator.new SpanedCell(bundle.getString("Workbooks.OWNER"), 36, 40), manipulator.new SpanedCell(bundle.getString("Workbooks.ASSIGNEE"), 41, 45), manipulator.new SpanedCell("", 46, 46), manipulator.new SpanedCell(bundle.getString("Workbooks.MODIFICATION"), 47, 50) };

		manipulator.createSpanedCellsRow(spanedHeaderCellsArray, true);

		String[] headerCellsArray = { bundle.getString("Workbooks.SITE"), bundle.getString("Workbooks.BLOCK"), bundle.getString("Workbooks.POSITION"), "TSW", "KHW", "PHW", "ROW", "COLUMN", bundle.getString("Workbooks.FRAME"), "LM", "TSW", "KHW", "PHW", bundle.getString("Workbooks.NAME"), "L3ADDR", "SPCE", "HW", "SHW", "GR", "SW", "LVL", bundle.getString("Workbooks.FRAME"), "LM", bundle.getString("Workbooks.CABLE"), bundle.getString("Workbooks.PORT"), bundle.getString("Workbooks.STREET"), bundle.getString("Workbooks.NUMBER"), bundle.getString("Workbooks.FLOOR"), bundle.getString("Workbooks.APARTMENT"), bundle.getString("Workbooks.STREET_1"), bundle.getString("Workbooks.STREET_2"), bundle.getString("Workbooks.ZIP_CODE"), bundle.getString("Workbooks.LOCATION"), bundle.getString("Workbooks.CITY"), bundle.getString("Workbooks.STATE"), bundle.getString("Workbooks.COUNTRY"), bundle.getString("Workbooks.FIRST_NAME"), bundle.getString("Workbooks.MIDDLE_NAME"), bundle.getString("Workbooks.LAST_NAME"), bundle.getString("Workbooks.IDENTIFICATION_TYPE"), bundle.getString("Workbooks.IDENTIFICATION_NUMBER"), bundle.getString("Workbooks.FIRST_NAME"), bundle.getString("Workbooks.MIDDLE_NAME"), bundle.getString("Workbooks.LAST_NAME"), bundle.getString("Workbooks.IDENTIFICATION_TYPE"), bundle.getString("Workbooks.IDENTIFICATION_NUMBER"), bundle.getString("Workbooks.REMARKS"), bundle.getString("Workbooks.USERNAME"), bundle.getString("Workbooks.FIRST_NAME"), bundle.getString("Workbooks.LAST_NAME"), bundle.getString("Workbooks.TIME") };
		manipulator.createCellsRow(headerCellsArray, true);

		// Content
		while (resultSet.next()) {
			String[] cellsArray = new String[51];

			cellsArray[0] = resultSet.getString("siteCode");
			cellsArray[1] = resultSet.getString("switchBlockName");
			cellsArray[2] = resultSet.getString("blockPosition");

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

			cellsArray[14] = (cellsArray[11] != null) ? String.format("%05d", resultSet.getInt("sigmal3addr")) : "";

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

			Street street = StreetsManager.getStreet(resultSet.getInt("streetId"));
			cellsArray[25] = (street != null) ? street.getName() : "";

			cellsArray[26] = resultSet.getString("addressNumber");
			cellsArray[27] = resultSet.getString("floor");
			cellsArray[28] = resultSet.getString("apartment");

			Street street1 = StreetsManager.getStreet(resultSet.getInt("street1Id"));
			cellsArray[29] = (street1 != null) ? street1.getName() : "";

			Street street2 = StreetsManager.getStreet(resultSet.getInt("street2Id"));
			cellsArray[30] = (street2 != null) ? street2.getName() : "";

			cellsArray[31] = resultSet.getString("zipCode");
			cellsArray[32] = resultSet.getString("location");
			cellsArray[33] = resultSet.getString("city");
			cellsArray[34] = resultSet.getString("state");
			cellsArray[35] = resultSet.getString("country");
			cellsArray[36] = resultSet.getString("ownerFirstName");
			cellsArray[37] = resultSet.getString("ownerMiddleName");
			cellsArray[38] = resultSet.getString("ownerLastName");
			cellsArray[39] = resultSet.getString("ownerIdentificationType");
			cellsArray[40] = resultSet.getString("ownerIdentificationNumber");
			cellsArray[41] = resultSet.getString("assigneeFirstName");
			cellsArray[42] = resultSet.getString("assigneeMiddleName");
			cellsArray[43] = resultSet.getString("assigneeLastName");
			cellsArray[44] = resultSet.getString("assigneeIdentificationType");
			cellsArray[45] = resultSet.getString("assigneeIdentificationNumber");
			cellsArray[46] = resultSet.getString("remarks");
			cellsArray[47] = resultSet.getString("username");
			cellsArray[48] = resultSet.getString("firstName");
			cellsArray[49] = resultSet.getString("lastName");
			cellsArray[50] = resultSet.getString("time");

			manipulator.createCellsRow(cellsArray, false);
		}
		return manipulator.getWorkbook();
	}

	public Wiring getWiring(Connection connection) throws Exception {
		Wiring wiring = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getWiring(?)}");
			statement.setInt(1, getId());
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				wiring = new Wiring();
				wiring.setId(resultSet.getInt("wiringId"));

				Distributor distributor = new Distributor();
				distributor.setId(resultSet.getInt("distributorId"));
				int neax61sigmaId = resultSet.getInt("neax61sigmaId");
				if (neax61sigmaId != 0) {
					Neax61SigmaEquipment neax61SigmaEquipment = new Neax61SigmaEquipment();
					neax61SigmaEquipment.setId(neax61sigmaId);
					setEquipment(neax61SigmaEquipment);
					distributor.setNeax61SigmaEquipment(neax61SigmaEquipment);
				}
				int sigmal3addrId = resultSet.getInt("sigmal3addrId");
				if (sigmal3addrId != 0) {
					SigmaL3AddressEquipment sigmaL3AddressEquipment = new SigmaL3AddressEquipment();
					sigmaL3AddressEquipment.setId(sigmal3addrId);
					setEquipment(sigmaL3AddressEquipment);
					distributor.setSigmaL3AddressEquipment(sigmaL3AddressEquipment);
				}
				int neax61eId = resultSet.getInt("neax61eId");
				if (neax61eId != 0) {
					Neax61EEquipment neax61EEquipment = new Neax61EEquipment();
					neax61EEquipment.setId(neax61eId);
					setEquipment(neax61EEquipment);
					distributor.setNeax61EEquipment(neax61EEquipment);
				}
				int zhoneId = resultSet.getInt("zhoneId");
				if (zhoneId != 0) {
					ZhoneEquipment zhoneEquipment = new ZhoneEquipment();
					zhoneEquipment.setId(zhoneId);
					setEquipment(zhoneEquipment);
					distributor.setZhoneEquipment(zhoneEquipment);
				}

				SwitchBlock switchBlock = new SwitchBlock();
				switchBlock.setId(resultSet.getInt("switchBlockId"));
				switchBlock.setName(resultSet.getString("switchBlockName"));
				BlockPosition blockPosition = new BlockPosition();
				blockPosition.setId(resultSet.getInt("blockPositionId"));
				blockPosition.setSwitchBlock(switchBlock);
				blockPosition.setPosition(resultSet.getString("blockPosition"));
				distributor.setBlockPosition(blockPosition);

				wiring.setDistributor(distributor);

				Broadband broadband = null;
				BroadbandPort broadbandPort = null;
				int broadbandPortId = resultSet.getInt("broadbandPortId");
				if (broadbandPortId != 0) {
					broadband = new Broadband();
					broadbandPort = new BroadbandPort();
					broadbandPort.setId(broadbandPortId);
					broadbandPort.setPort(resultSet.getInt("broadbandPort"));

					DSLAMBoard dslamBoard = null;
					int boardId = resultSet.getInt("boardId");
					if (boardId != 0) {
						dslamBoard = new DSLAMBoard();
						dslamBoard.setId(boardId);
						dslamBoard.setSlot(resultSet.getInt("boardSlot"));
						broadbandPort.setDslamBoard(dslamBoard);

						DSLAM dslam = new DSLAM();
						dslam.setId(resultSet.getInt("dslamId"));
						String dslamName = resultSet.getString("dslamName");
						dslam.setName(dslamName);
					}
					broadband.setBroadbandPort(broadbandPort);
				}

				wiring.setBroadband(broadband);

				if (wiring.getId() != 0) {
					StreetFrame streetFrame = new StreetFrame();
					streetFrame.setId(resultSet.getInt("streetFrameId"));
					streetFrame.setSite(SitesManager.getSite(resultSet.getInt("siteId")));
					streetFrame.setName(resultSet.getString("streetFrameName").charAt(0));
					StreetCable streetCable = new StreetCable();
					streetCable.setId(resultSet.getInt("streetCableId"));
					streetCable.setStreetFrame(streetFrame);
					streetCable.setName(resultSet.getString("streetCableName").charAt(0));
					StreetPair streetPair = new StreetPair();
					streetPair.setId(resultSet.getInt("streetPairId"));
					streetPair.setPair(resultSet.getInt("streetPair"));
					streetPair.setStreetCable(streetCable);

					wiring.setStreetPair(streetPair);
				}
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getWiring().", e);
			throw new Exception("Cannot get wiring for " + getPhoneNumber() + " from database.");
		} finally {
			statement.close();
		}

		return wiring;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder subscriber = new StringBuilder(address.getPhone().getFullNumeration());
		if (equipment != null) {
			subscriber.append(" (");
			if (equipment instanceof SigmaL3AddressEquipment)
				subscriber.append(equipment);
			else {
				subscriber.append("el=");
				subscriber.append(equipment);
			}
			subscriber.append(")");
		}
		return subscriber.toString();
	}

	public String getPhoneNumber() {
		return address.getPhone().getFullNumeration();
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
	 * @return the equipment
	 */
	public Equipment getEquipment() {
		return equipment;
	}

	/**
	 * @param equipment the equipment to set
	 */
	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @return the person
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person the person to set
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * @return the relationship
	 */
	public Relationship getRelationship() {
		return relationship;
	}

	/**
	 * @param relationship the relationship to set
	 */
	public void setRelationship(Relationship relationship) {
		this.relationship = relationship;
	}

	/**
	 * @return the data
	 */
	public SubscriberData getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(SubscriberData data) {
		this.data = data;
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
	 * @return the wired
	 */
	public boolean isWired() {
		return wired;
	}

	/**
	 * @param wired the wired to set
	 */
	public void setWired(boolean wired) {
		this.wired = wired;
	}

	/**
	 * @return the broadband
	 */
	public boolean isBroadband() {
		return broadband;
	}

	/**
	 * @param broadband the broadband to set
	 */
	public void setBroadband(boolean broadband) {
		this.broadband = broadband;
	}
}
