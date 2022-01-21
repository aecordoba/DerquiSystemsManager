/*
 * 		Phone.java
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
 * 		Phone.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jul 23, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.managers.OfficeCodesDataManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Phone {
	private int id;
	private String number;
	private OfficeCode officeCode;

	private static final Logger logger = LogManager.getLogger(Phone.class);

	/**
	 * 
	 */
	public Phone() {
	}

	/**
	 * @param number
	 * @param officeCode
	 */
	public Phone(String number, OfficeCode officeCode) {
		this.number = number;
		this.officeCode = officeCode;
	}

	public Phone(ResultSet resultSet, boolean isCellPhone) throws SQLException {
		if (isCellPhone) {
			setId(resultSet.getInt("cellPhoneNumberId"));
			setNumber(resultSet.getString("cellPhoneNumber"));

			int cellOfficeCodeId = resultSet.getInt("cellPhoneOfficeCodeId");
			OfficeCode cellOfficeCode = OfficeCodesDataManager.getOfficeCode(cellOfficeCodeId);
			setOfficeCode(cellOfficeCode);
		} else {
			setId(resultSet.getInt("phoneNumberId"));
			setNumber(resultSet.getString("phoneNumber"));

			int phoneOfficeCodeId = resultSet.getInt("phoneOfficeCodeId");
			OfficeCode phoneOfficeCode = OfficeCodesDataManager.getOfficeCode(phoneOfficeCodeId);
			setOfficeCode(phoneOfficeCode);
		}
	}

	public static List<Phone> getNoWiredSubscribersList(Connection connection, int officeCodeId, int siteId) throws Exception {
		List<Phone> noWiredSubscribersList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getNoWiredSubscribersList(?, ?)}");
			statement.setInt(1, officeCodeId);
			statement.setInt(2, siteId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Phone subscriber = new Phone();
				subscriber.setNumber(resultSet.getString("number"));
				subscriber.setOfficeCode(OfficeCodesDataManager.getOfficeCode(officeCodeId));
				noWiredSubscribersList.add(subscriber);
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getNoWiredSubscribersList().", e);
			throw new Exception("Cannot get no wired subscribers list from database.");
		} finally {
			statement.close();
		}
		return noWiredSubscribersList;
	}

	public static List<Phone> getVacantPhonesList(Connection connection, int officeCodeId) throws Exception {
		List<Phone> vacantPhonesList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getVacantNumbersList(?)}");
			statement.setInt(1, officeCodeId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Phone phone = new Phone();
				phone.setNumber(resultSet.getString("number"));
				phone.setOfficeCode(OfficeCodesDataManager.getOfficeCode(officeCodeId));
				vacantPhonesList.add(phone);
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getVacantNumbersList().", e);
			throw new Exception("Cannot get vacant numbers list from database.");
		} finally {
			statement.close();
		}
		return vacantPhonesList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return officeCode.getCode() + "-" + number;
	}

	public String getFullNumeration() {
		return officeCode.getFullNumeration() + "-" + number;
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
	 * @return the officeCode
	 */
	public OfficeCode getOfficeCode() {
		return officeCode;
	}

	/**
	 * @param officeCode the officeCode to set
	 */
	public void setOfficeCode(OfficeCode officeCode) {
		this.officeCode = officeCode;
	}
}
