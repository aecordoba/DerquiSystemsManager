/*
 * 		SubscribersData.java
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
 * 		SubscribersData.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jun 21, 2019
 */
package ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.subscribersdata;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.MassivePopulation;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.Processor;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class SubscribersData implements Processor {

	@Override
	public boolean process(Connection connection) {
		boolean result = true;
		Statement statement = null;
		int subscriberDataCount = 0;
		int subscriberDataRecordCount = 0;
		try {
			statement = connection.createStatement();
			for (int subscriberDataId = 1; subscriberDataId < AMOUNT + 1; subscriberDataId++) {
				String subscribersDataQuery = null;
				String subscribersDataRecordQuery = null;
				if (subscriberDataId % BROADBAND_SUBSCRIBERS_RATE != 0) {
					subscribersDataQuery = "INSERT INTO SubscribersData(id, lineClassId, restrictionId, stateId) " + "VALUES(" + subscriberDataId + ", 1, 13, 1);";
					subscribersDataRecordQuery = "INSERT INTO SubscribersDataRecord(dataId, lineClassId, restrictionId, stateId, userId) " + "VALUES(" + subscriberDataId + ", 1, 13, 1, 1);";
				} else {
					subscribersDataQuery = "INSERT INTO SubscribersData(id, lineClassId, restrictionId, stateId, broadbandStateId) " + "VALUES(" + subscriberDataId + ", 1, 13, 1, 1);";
					subscribersDataRecordQuery = "INSERT INTO SubscribersDataRecord(dataId, lineClassId, restrictionId, stateId, broadbandStateId, userId) " + "VALUES(" + subscriberDataId + ", 1, 13, 1, 1, 1);";
				}
				if (DB_IMPACT) {
					try {
						statement.execute(subscribersDataQuery);
						subscriberDataCount++;
					} catch (SQLException e) {
						result = false;
						System.out.println("Cannot exexute " + subscribersDataQuery + " query.");
						e.printStackTrace();
					}
					try {
						statement.execute(subscribersDataRecordQuery);
						subscriberDataRecordCount++;
					} catch (SQLException e) {
						result = false;
						System.out.println("Cannot exexute " + subscribersDataRecordQuery + " query.");
						e.printStackTrace();
					}
				} else {
					System.out.println(subscribersDataQuery);
					System.out.println(subscribersDataRecordQuery);
				}

			}
		} catch (SQLException e) {
			result = false;
			System.out.println("Cannot create Statement object. Cannot insert SubscribersData and SubscribersDataRecord in DB.");
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				result = false;
				System.out.println("Cannot close Statement object.");
				e.printStackTrace();
			}
			System.out.println(subscriberDataCount + " registers inserted into SubsciberData table.");
			System.out.println(subscriberDataRecordCount + " registers inserted into SubscriberDataRecord table.");
		}
		return result;
	}

}
