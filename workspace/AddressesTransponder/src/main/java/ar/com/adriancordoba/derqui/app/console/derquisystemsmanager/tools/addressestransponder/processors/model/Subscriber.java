/*
 * 		Subscriber.java
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
 * 		Subscriber.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Nov 1, 2019
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.addressestransponder.processors.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Subscriber {
	private String number;
	private Owner owner;
	private Address address;
	private int officeCodeId;
	private String mcdu;
	private static final Logger logger = LogManager.getLogger(Subscriber.class);

	public void update(Connection connection) throws Exception {
		int[] indexes = getSubscriberId(connection);

		if (indexes[0] != 0) {
			CallableStatement statement = null;
			try {
				statement = connection.prepareCall("{call updateSubscriber2(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

				statement.setInt(1, indexes[0]);
				statement.setInt(2, indexes[1]);
				statement.setString(3, "OWNER");
				statement.setInt(4, 0);
				statement.setString(5, owner.getFirstName());
				statement.setString(6, owner.getMiddleName());
				statement.setString(7, owner.getLastName());
				statement.setInt(8, 0);
				statement.setInt(9, 0);
				statement.setInt(10, 0);
				statement.setInt(11, Street.getId((address.getStreet().getName())));
				statement.setString(12, address.getNumber());
				statement.setString(13, null);
				statement.setString(14, null);
				statement.setInt(15, 0);
				statement.setInt(16, 0);
				statement.setString(17, null);
				if (officeCodeId == 3)
					statement.setInt(18, 12);
				else
					statement.setInt(18, 2);
				statement.setString(19, null);
				statement.setInt(20, 1);

				statement.execute();
			} catch (SQLException e) {
				logger.error("Cannot execute stored procedure updateSubscriber2().", e);
				throw new Exception("Cannot modify subscriber in database for subscriber " + number + ".");
			} finally {
				statement.close();
			}
		} else
			logger.warn("Not assigned subscriber: " + number + ".");
	}

	private int[] getSubscriberId(Connection connection) throws SQLException {
		int[] indexes = new int[2];
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getSubscriberId(?, ?)}");
			statement.setInt(1, officeCodeId);
			statement.setString(2, mcdu);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				indexes[0] = resultSet.getInt("id");
				indexes[1] = resultSet.getInt("distributorId");
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getSubscriberId() for " + number + ".", e);
		} finally {
			statement.close();
		}
		return indexes;
	}

	@Override
	public String toString() {
		return "Subscriber [number=" + number + ", owner=" + owner + ", address=" + address + ", officeCodeId=" + officeCodeId + ", mcdu=" + mcdu + "]";
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
		String officeCode = number.substring(0, 3);
		if (officeCode.equals("448"))
			officeCodeId = 1;
		if (officeCode.equals("447"))
			officeCodeId = 2;
		if (officeCode.equals("449"))
			officeCodeId = 3;
		if (officeCode.equals("454"))
			officeCodeId = 4;
		mcdu = number.substring(3);
	}

	/**
	 * @return the owner
	 */
	public Owner getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Owner owner) {
		this.owner = owner;
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
	 * @return the officeCode
	 */
	public int getOfficeCodeId() {
		return officeCodeId;
	}

	/**
	 * @return the mcdu
	 */
	public String getMcdu() {
		return mcdu;
	}
}
