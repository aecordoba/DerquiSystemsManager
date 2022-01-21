/*
 * 		Address.java
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
 * 		Address.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jul 23, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.managers.StreetsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Phone;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Address {
	private int id;
	private Street street;
	private String number; // number = "0" for unknown number (No Number).
	private String floor;
	private String apartment;
	private String zipCode;
	private Street street1;
	private Street street2;
	private int locationId;
	private Phone phone;

	/**
	 * 
	 */
	public Address() {
	}

	/**
	 * @param street
	 * @param number
	 * @param floor
	 * @param apartment
	 * @param zipCode
	 * @param betweenStreet1
	 * @param betweenStreet2
	 * @param locationId
	 * @param phone
	 */
	public Address(Street street, String number, String floor, String apartment, String zipCode, Street betweenStreet1, Street betweenStreet2, int locationId, Phone phone) {
		this.street = street;
		this.number = number;
		this.floor = floor;
		this.apartment = apartment;
		this.zipCode = zipCode;
		this.street1 = betweenStreet1;
		this.street2 = betweenStreet2;
		this.locationId = locationId;
		this.phone = phone;
	}

	public Address(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("addressId"));

		int streetId = resultSet.getInt("streetId");
		Street street = StreetsManager.getStreet(streetId);
		setStreet(street);

		setNumber(resultSet.getString("addressNumber"));
		setFloor(resultSet.getString("floor"));
		setApartment(resultSet.getString("apartment"));
		setZipCode(resultSet.getString("zipCode"));

		int street1Id = resultSet.getInt("street1Id");
		Street street1 = StreetsManager.getStreet(street1Id);
		setStreet1(street1);

		int street2Id = resultSet.getInt("street2Id");
		Street street2 = StreetsManager.getStreet(street2Id);
		setStreet2(street2);

		setLocationId(resultSet.getInt("locationId"));
		if (resultSet.getInt("phoneNumberId") != 0) {
			Phone phone = new Phone(resultSet, false);
			setPhone(phone);
		}
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
	 * @return the street
	 */
	public Street getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(Street street) {
		this.street = street;
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
	 * @return the floor
	 */
	public String getFloor() {
		return floor;
	}

	/**
	 * @param floor the floor to set
	 */
	public void setFloor(String floor) {
		this.floor = floor;
	}

	/**
	 * @return the apartment
	 */
	public String getApartment() {
		return apartment;
	}

	/**
	 * @param apartment the apartment to set
	 */
	public void setApartment(String apartment) {
		this.apartment = apartment;
	}

	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * @return the street1
	 */
	public Street getStreet1() {
		return street1;
	}

	/**
	 * @param street1 the betweenStreet1 to set
	 */
	public void setStreet1(Street street1) {
		this.street1 = street1;
	}

	/**
	 * @return the street2
	 */
	public Street getStreet2() {
		return street2;
	}

	/**
	 * @param street2 the street2 to set
	 */
	public void setStreet2(Street street2) {
		this.street2 = street2;
	}

	/**
	 * @return the locationId
	 */
	public int getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the phone
	 */
	public Phone getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(Phone phone) {
		this.phone = phone;
	}
}
