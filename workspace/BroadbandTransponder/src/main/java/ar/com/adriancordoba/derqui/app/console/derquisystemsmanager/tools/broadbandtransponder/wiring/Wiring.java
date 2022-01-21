/*
 * 		Wiring.java
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
 * 		Broadband.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Oct 2, 2019
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.broadbandtransponder.wiring;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.broadbandtransponder.subscribers.Subscriber;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Wiring {
	private Subscriber subscriber;
	private String cablePair;
	private int wiringId;
	private int distributorId;
	private int streetPairId;
	private int secondStreetPairId;
	private Broadband broadband;
	private String remarks;

	private static final Logger logger = LogManager.getLogger(Wiring.class);

	public void setWiringData(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getWiringByPhoneNumber(?, ?)}");
			statement.setInt(1, subscriber.getOfficeCode());
			statement.setString(2, String.valueOf(subscriber.getMcdu()));
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				wiringId = resultSet.getInt("wiringId");
				distributorId = resultSet.getInt("distributorId");
				streetPairId = resultSet.getInt("streetPairId");
				secondStreetPairId = resultSet.getInt("secondStreetpairId");
				remarks = resultSet.getString("remarks");
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getWiringByPhoneNumber() for subscriber " + subscriber, e);
			throw new Exception("Cannot get Wiring data from database.");
		} finally {
			statement.close();
		}
	}

	public void update(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call updateWiring(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			statement.setInt(1, wiringId);
			statement.setInt(2, distributorId);
			statement.setInt(3, streetPairId);
			statement.setInt(4, secondStreetPairId);
			statement.setInt(5, broadband.getBroadbandPortId());
			statement.setString(6, broadband.getUsername());
			statement.setString(7, broadband.getPassword());
			statement.setInt(8, 0);
			statement.setString(9, remarks);
			statement.setInt(10, 1);

			statement.execute();
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure updateWiring().", e);
			throw new Exception("Cannot modify wiring in database.");
		} finally {
			statement.close();
		}
	}

	@Override
	public String toString() {
		return "Wiring [subscriber=" + subscriber + ", broadband=" + broadband + "]";
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
	 * @return the cablePair
	 */
	public String getCablePair() {
		return cablePair;
	}

	/**
	 * @param cablePair the cablePair to set
	 */
	public void setCablePair(String cablePair) {
		this.cablePair = cablePair;
	}

	/**
	 * @return the wiringId
	 */
	public int getWiringId() {
		return wiringId;
	}

	/**
	 * @param wiringId the wiringId to set
	 */
	public void setWiringId(int wiringId) {
		this.wiringId = wiringId;
	}

	/**
	 * @return the distributorId
	 */
	public int getDistributorId() {
		return distributorId;
	}

	/**
	 * @param distributorId the distributorId to set
	 */
	public void setDistributorId(int distributorId) {
		this.distributorId = distributorId;
	}

	/**
	 * @return the streetPairId
	 */
	public int getStreetPairId() {
		return streetPairId;
	}

	/**
	 * @param streetPairId the streetPairId to set
	 */
	public void setStreetPairId(int streetPairId) {
		this.streetPairId = streetPairId;
	}

	/**
	 * @return the secondStreetPairId
	 */
	public int getSecondStreetPairId() {
		return secondStreetPairId;
	}

	/**
	 * @param secondStreetPairId the secondStreetPairId to set
	 */
	public void setSecondStreetPairId(int secondStreetPairId) {
		this.secondStreetPairId = secondStreetPairId;
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
}
