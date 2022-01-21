/*
 * 		Broadband.java
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
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jun 25, 2019
 */
package ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.broadband;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.MassivePopulation;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.Processor;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Broadband implements Processor {
	private char u1 = 'A';
	private char u2 = 'A';
	private char u3 = 'A';
	private char u4 = 'A';

	@Override
	public boolean process(Connection connection) {
		boolean result = true;
		Statement statement = null;
		int broadbandCount = 0;
		int broadbandRecordCount = 0;
		try {
			statement = connection.createStatement();
			for (int broadbandId = 1; broadbandId < (AMOUNT / BROADBAND_SUBSCRIBERS_RATE) + 1; broadbandId++) {
				String username = "u" + u1 + u2 + u3 + u4;
				String password = "p" + u1 + u2 + u3 + u4++;
				String broadbandQuery = "INSERT INTO Broadband(id, username, password, portId) " + "VALUES(" + broadbandId + ", \"" + username + "\", \"" + password + "\", " + broadbandId + ");";
				String broadbandRecordQuery = "INSERT INTO BroadbandRecord(id, username, password, portId, userId) " + "VALUES(" + broadbandId + ", \"" + username + "\", \"" + password + "\", " + broadbandId + ", 1);";
				if (DB_IMPACT) {
					try {
						statement.execute(broadbandQuery);
						broadbandCount++;
					} catch (SQLException e) {
						result = false;
						System.out.println("Cannot exexute " + broadbandQuery + " query.");
						e.printStackTrace();
					}
					try {
						statement.execute(broadbandRecordQuery);
						broadbandRecordCount++;
					} catch (SQLException e) {
						result = false;
						System.out.println("Cannot exexute " + broadbandRecordQuery + " query.");
						e.printStackTrace();
					}
				} else {
					System.out.println(broadbandQuery);
					System.out.println(broadbandRecordQuery);
				}

				if (u4 == '[') {
					u4 = 'A';
					u3++;
					if (u3 == '[') {
						u3 = 'A';
						u2++;
						if (u2 == '[') {
							u2 = 'A';
							u1++;
						}
					}
				}
			}
		} catch (SQLException e) {
			result = false;
			System.out.println("Cannot create Statement object. Cannot insert Broadband and BroadbandRecord in DB.");
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				result = false;
				System.out.println("Cannot close Statement object.");
				e.printStackTrace();
			}
			System.out.println(broadbandCount + " registers inserted into Broadband table.");
			System.out.println(broadbandRecordCount + " registers inserted into BroadbandRecord table.");
		}
		return result;
	}
}
