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
 * 		Wiring.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jun 21, 2019
 */
package ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.subscribersdata.wirings;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.Processor;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.broadband.Broadband;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Wiring implements Processor {

	@Override
	public boolean process(Connection connection) {
		boolean result = true;
		Statement statement = null;
		Broadband broadband = new Broadband();
		int broadbandId = 1;
		int wiringCount = 0;
		int wiringRecordCount = 0;
		try {
			statement = connection.createStatement();
			for (int wiringId = 1; wiringId < AMOUNT + 1; wiringId++) {
				String wiringQuery = null;
				String wiringRecordQuery = null;

				if (wiringId % BROADBAND_SUBSCRIBERS_RATE == 0) {
					wiringQuery = "INSERT INTO Wiring(id, distributorId, streetPairId, broadbandId) " + "VALUES(" + wiringId + ", " + wiringId + ", " + wiringId + ", " + broadbandId + ");";
					wiringRecordQuery = "INSERT INTO WiringRecord(distributorId, streetPairId, broadbandRecordId, userId) " + "VALUES(" + wiringId + ", " + wiringId + ", " + broadbandId++ + ", 1);";

				} else {
					wiringQuery = "INSERT INTO Wiring(id, distributorId, streetPairId) " + "VALUES(" + wiringId + ", " + wiringId + ", " + wiringId + ");";
					wiringRecordQuery = "INSERT INTO WiringRecord(distributorId, streetPairId, userId) " + "VALUES(" + wiringId + ", " + wiringId + ", 1);";
				}

				if (DB_IMPACT) {
					try {
						statement.execute(wiringQuery);
						wiringCount++;
					} catch (SQLException e) {
						result = false;
						System.out.println("Cannot exexute " + wiringQuery + " query.");
						e.printStackTrace();
					}
					try {
						statement.execute(wiringRecordQuery);
						wiringRecordCount++;
					} catch (SQLException e) {
						result = false;
						System.out.println("Cannot exexute " + wiringRecordQuery + " query.");
						e.printStackTrace();
					}
				} else {
					System.out.println(wiringQuery);
					System.out.println(wiringRecordQuery);
				}
			}
		} catch (SQLException e) {
			result = false;
			System.out.println("Cannot create Statement object. Cannot insert Wirings, WiringsRecord, Broadband and BroadbandRecord in DB.");
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				result = false;
				System.out.println("Cannot close Statement object.");
				e.printStackTrace();
			}
			System.out.println(wiringCount + " registers inserted into Wiring table.");
			System.out.println(wiringRecordCount + " registers inserted into WiringRecord table.");
		}
		return result;
	}
}
