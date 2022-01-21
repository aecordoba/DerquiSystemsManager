/*
 * 		Subscribers.java
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
 * 		Subscribers.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jun 21, 2019
 */
package ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.subscribers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.MassivePopulation;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.Processor;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Subscribers implements Processor {

	@Override
	public boolean process(Connection connection) {
		boolean result = true;
		Statement statement = null;
		int subscribersCount = 0;
		int subscribersRecordCount = 0;
		try {
			statement = connection.createStatement();
			for (int subscriberId = 1; subscriberId < AMOUNT + 1; subscriberId++) {
				String subscribersQuery = "INSERT INTO Subscribers(id, addressId, distributorId, dataId) " + "VALUES(" + subscriberId + ", " + (subscriberId + 1) + ", " + subscriberId + ", " + subscriberId + ");";
				String subscribersRecordQuery = null;
				if (subscriberId % OWNERS_SUBSCRIBERS_RATE != 0)
					subscribersRecordQuery = "INSERT INTO SubscribersRecord(addressId, distributorId, ownerId, userId) " + "VALUES(" + (subscriberId + 1) + ", " + subscriberId + ", " + (subscriberId + 1) + ", 1);";
				else
					subscribersRecordQuery = "INSERT INTO SubscribersRecord(addressId, distributorId, assigneeId, userId) " + "VALUES(" + (subscriberId + 1) + ", " + subscriberId + ", " + (subscriberId + 1) + ", 1);";
				if (DB_IMPACT) {
					try {
						statement.execute(subscribersQuery);
						subscribersCount++;
					} catch (SQLException e) {
						result = false;
						System.out.println("Cannot exexute " + subscribersQuery + " query.");
						e.printStackTrace();
					}
					try {
						statement.execute(subscribersRecordQuery);
						subscribersRecordCount++;
					} catch (SQLException e) {
						result = false;
						System.out.println("Cannot exexute " + subscribersRecordQuery + " query.");
						e.printStackTrace();
					}
				} else {
					System.out.println(subscribersQuery);
					System.out.println(subscribersRecordQuery);
				}
			}
		} catch (SQLException e) {
			result = false;
			System.out.println("Cannot create Statement object. Cannot insert Subscribers and SubscribersData in DB.");
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				result = false;
				System.out.println("Cannot close Statement object.");
				e.printStackTrace();
			}
			System.out.println(subscribersCount + " registers inserted into Subscribers table.");
			System.out.println(subscribersRecordCount + " registers inserted into SubscribersRecord table.");
		}
		return result;
	}

}
