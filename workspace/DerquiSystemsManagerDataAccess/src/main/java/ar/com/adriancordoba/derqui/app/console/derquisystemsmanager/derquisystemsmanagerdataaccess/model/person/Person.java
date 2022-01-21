/*
 * 		Person.java
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
 * 		Person.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jul 23, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.Address;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.Street;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.managers.IdentificationsTypesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.OfficeCode;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Phone;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.managers.OfficeCodesDataManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Person {
	private int id;
	private String firstName;
	private String middleName;
	private String lastName;
	private IdentificationType identificationType;
	private int identificationNumber;
	private Phone cellPhone;
	private Address address;

	private static final Logger logger = LogManager.getLogger(Person.class);

	/**
	 * 
	 */
	public Person() {
	}

	/**
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param identificationType
	 * @param identificationNumber
	 * @param cellPhone
	 * @param address
	 */
	public Person(String firstName, String middleName, String lastName, IdentificationType identificationType, int identificationNumber, Phone cellPhone, Address address) {
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.identificationType = identificationType;
		this.identificationNumber = identificationNumber;
		this.cellPhone = cellPhone;
		this.address = address;
	}

	public Person(ResultSet resultSet) throws SQLException {
		int ownerPersonId = resultSet.getInt("ownerPersonId");
		int assigneePersonId = resultSet.getInt("assigneePersonId");

		if (ownerPersonId != 0) {
			setId(ownerPersonId);
			setFirstName(resultSet.getString("ownerFirstName"));
			setMiddleName(resultSet.getString("ownerMiddleName"));
			setLastName(resultSet.getString("ownerLastName"));
		} else if (assigneePersonId != 0) {
			setId(ownerPersonId);
			setFirstName(resultSet.getString("assigneeFirstName"));
			setMiddleName(resultSet.getString("assigneeMiddleName"));
			setLastName(resultSet.getString("assigneeLastName"));
		}
	}

	public static List<Person> getPeopleList(Connection connection) throws Exception {
		List<Person> peopleList = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getPeopleList()}");
			ResultSet resultSet = statement.executeQuery();
			peopleList = processResultSet(resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getPeopleList().", e);
			throw new Exception("Cannot get people list from database.");
		} finally {
			statement.close();
		}
		return peopleList;
	}

	public static List<Person> getPeopleList(Connection connection, String firstName, String middleName, String lastName, int identificationTypeId, int identificationNumber) throws Exception {
		List<Person> peopleList = new ArrayList<Person>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getPeopleListByCriteria(?, ?, ?, ?, ?)}");
			statement.setString(1, firstName + "%");
			if (middleName.isEmpty())
				statement.setString(2, null);
			else
				statement.setString(2, middleName + "%");
			statement.setString(3, lastName + "%");
			statement.setInt(4, identificationTypeId);
			statement.setInt(5, identificationNumber);
			ResultSet resultSet = statement.executeQuery();
			peopleList = processResultSet(resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getPeopleList().", e);
			throw new Exception("Cannot get people list from database.");
		} finally {
			statement.close();
		}
		return peopleList;
	}

	/**
	 * Process the result set.
	 * @param resultSet Result set to process.
	 * @return People list.
	 * @throws SQLException
	 */
	private static List<Person> processResultSet(ResultSet resultSet) throws SQLException {
		List<Person> peopleList = new ArrayList<>();

		while (resultSet.next()) {
			Person person = new Person();

			person.setId(resultSet.getInt("personId"));
			person.setFirstName(resultSet.getString("firstName"));
			person.setMiddleName(resultSet.getString("middleName"));
			person.setLastName(resultSet.getString("lastname"));
			int identificationTypeId = resultSet.getInt("identificationTypeId");
			if (identificationTypeId != 0) {
				IdentificationType identificationType = IdentificationsTypesManager.getIdentificationType(identificationTypeId);
				person.setIdentificationType(identificationType);
				person.setIdentificationNumber(resultSet.getInt("identificationNumber"));
			}
			if (resultSet.getInt("cellPhoneNumberId") != 0) {
				Phone cellPhone = new Phone(resultSet, true);
				person.setCellPhone(cellPhone);
			}
			if (resultSet.getInt("addressId") != 0)
				person.setAddress(new Address(resultSet));

			peopleList.add(person);
		}

		return peopleList;
	}

	public void insert(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call insertPerson(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			statement.setString(1, getFirstName());
			statement.setString(2, getMiddleName());
			statement.setString(3, getLastName());
			if (identificationType != null) {
				statement.setInt(4, getIdentificationType().getId());
				statement.setInt(5, getIdentificationNumber());
			} else {
				statement.setInt(4, 0);
				statement.setInt(5, 0);
			}
			if (cellPhone != null) {
				OfficeCode cellPhoneOfficeCode = cellPhone.getOfficeCode();
				statement.setInt(6, cellPhoneOfficeCode.getArea().getId());
				statement.setInt(7, cellPhoneOfficeCode.getCode());
				statement.setString(8, cellPhone.getNumber());
			} else {
				statement.setInt(6, 0);
				statement.setInt(7, 0);
				statement.setString(8, null);
			}
			if (address != null) {
				Street street = address.getStreet();
				int streetId = (street == null) ? 0 : street.getId();
				statement.setInt(9, streetId);

				statement.setString(10, address.getNumber());
				statement.setString(11, address.getFloor());
				statement.setString(12, address.getApartment());

				Street street1 = address.getStreet1();
				int street1Id = (street1 == null) ? 0 : street1.getId();
				statement.setInt(13, street1Id);

				Street street2 = address.getStreet2();
				int street2Id = (street2 == null) ? 0 : street2.getId();
				statement.setInt(14, street2Id);

				statement.setString(15, address.getZipCode());
				statement.setInt(16, address.getLocationId());
				if (address.getPhone() != null) {
					Phone phone = address.getPhone();
					OfficeCode phoneOfficeCode = phone.getOfficeCode();
					statement.setInt(17, phoneOfficeCode.getArea().getId());
					statement.setInt(18, phoneOfficeCode.getCode());
					statement.setString(19, phone.getNumber());
				} else {
					statement.setInt(17, 0);
					statement.setInt(18, 0);
					statement.setString(19, null);
				}
			} else {
				statement.setInt(9, 0);
				statement.setString(10, null);
				statement.setString(11, null);
				statement.setString(12, null);
				statement.setString(13, null);
				statement.setString(14, null);
				statement.setString(15, null);
				statement.setInt(16, 0);
				statement.setInt(17, 0);
				statement.setInt(18, 0);
				statement.setString(19, null);
			}

			statement.execute();
			logger.debug(this + " was inserted in database.");
			OfficeCodesDataManager.reload(connection);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure insertPerson().", e);
			throw new Exception("Cannot insert person in database.");
		} finally {
			statement.close();
		}
	}

	public void update(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call updatePerson(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			statement.setInt(1, getId());
			statement.setString(2, getFirstName());
			statement.setString(3, getMiddleName());
			statement.setString(4, getLastName());
			if (identificationType != null)
				statement.setInt(5, getIdentificationType().getId());
			else
				statement.setInt(5, 0);
			statement.setInt(6, getIdentificationNumber());
			if (cellPhone != null) {
				OfficeCode cellPhoneOfficeCode = cellPhone.getOfficeCode();
				statement.setInt(7, cellPhoneOfficeCode.getArea().getId());
				statement.setInt(8, cellPhoneOfficeCode.getCode());
				statement.setString(9, cellPhone.getNumber());
			} else {
				statement.setInt(7, 0);
				statement.setInt(8, 0);
				statement.setString(9, null);
			}
			if (address != null) {
				statement.setInt(10, address.getId());

				Street street = address.getStreet();
				int streetId = (street == null) ? 0 : street.getId();
				statement.setInt(11, streetId);

				statement.setString(12, address.getNumber());
				statement.setString(13, address.getFloor());
				statement.setString(14, address.getApartment());

				Street street1 = address.getStreet1();
				int street1Id = (street1 == null) ? 0 : street1.getId();
				statement.setInt(15, street1Id);

				Street street2 = address.getStreet2();
				int street2Id = (street2 == null) ? 0 : street2.getId();
				statement.setInt(16, street2Id);

				statement.setString(17, address.getZipCode());
				statement.setInt(18, address.getLocationId());
				if (address.getPhone() != null) {
					Phone phone = address.getPhone();
					OfficeCode phoneOfficeCode = phone.getOfficeCode();
					statement.setInt(19, phoneOfficeCode.getArea().getId());
					statement.setInt(20, phoneOfficeCode.getCode());
					statement.setString(21, phone.getNumber());
				} else {
					statement.setInt(19, 0);
					statement.setInt(20, 0);
					statement.setString(21, null);
				}
			} else {
				statement.setInt(10, 0);
				statement.setInt(11, 0);
				statement.setString(12, null);
				statement.setString(13, null);
				statement.setString(14, null);
				statement.setString(15, null);
				statement.setString(16, null);
				statement.setString(17, null);
				statement.setInt(18, 0);
				statement.setInt(19, 0);
				statement.setInt(20, 0);
				statement.setString(21, null);
			}

			statement.execute();
			logger.debug(this + " was updated in database.");
			OfficeCodesDataManager.reload(connection);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure updatePerson().", e);
			throw new Exception("Cannot update person in database.");
		} finally {
			statement.close();
		}
	}

	public String getFullName() {
		StringBuilder stringBuilder = new StringBuilder();
		if (lastName != null)
			stringBuilder.append(getLastName());
		if ((firstName != null) || middleName != null) {
			stringBuilder.append(",");
			if (firstName != null)
				stringBuilder.append(" " + getFirstName());
			if (middleName != null)
				stringBuilder.append(" " + getMiddleName());
		}
		return stringBuilder.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getFullName());
		if (identificationNumber != 0)
			stringBuilder.append(" (" + getIdentificationType() + " " + getIdentificationNumber() + ")");
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
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
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
	 * @return the identificationType
	 */
	public IdentificationType getIdentificationType() {
		return identificationType;
	}

	/**
	 * @param identificationType the identificationType to set
	 */
	public void setIdentificationType(IdentificationType identificationType) {
		this.identificationType = identificationType;
	}

	/**
	 * @return the identificationNumber
	 */
	public int getIdentificationNumber() {
		return identificationNumber;
	}

	/**
	 * @param identificationNumber the identificationNumber to set
	 */
	public void setIdentificationNumber(int identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	/**
	 * @return the cellPhone
	 */
	public Phone getCellPhone() {
		return cellPhone;
	}

	/**
	 * @param cellPhone the cellPhone to set
	 */
	public void setCellPhone(Phone cellPhone) {
		this.cellPhone = cellPhone;
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
}
