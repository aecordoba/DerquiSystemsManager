/*
 * 		Owners.java
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
 * 		Owners.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jun 21, 2019
 */
package ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.owners;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.MassivePopulation;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.Processor;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Owners implements Processor {

	@Override
	public boolean process(Connection connection) {
		boolean result = true;
		Statement statement = null;
		int ownersCount = 0;
		int assigneesCount = 0;
		try {
			statement = connection.createStatement();
			for (int subscriberId = 1; subscriberId < AMOUNT + 1; subscriberId++) {
				if (subscriberId % OWNERS_SUBSCRIBERS_RATE == 0) {
					String assigneesQuery = "INSERT INTO Assignees(subscriberId, personId) " + "VALUES(" + subscriberId + ", " + (subscriberId + 1) + ");";
					if (DB_IMPACT)
						try {
							statement.execute(assigneesQuery);
							assigneesCount++;
						} catch (SQLException e) {
							result = false;
							System.out.println("Cannot exexute " + assigneesQuery + " query.");
							e.printStackTrace();
						}
					else
						System.out.println(assigneesQuery);
				} else {
					String ownersQuery = "INSERT INTO Owners(subscriberId, personId) " + "VALUES(" + subscriberId + ", " + (subscriberId + 1) + ");";
					if (DB_IMPACT)
						try {
							statement.execute(ownersQuery);
							ownersCount++;
						} catch (SQLException e) {
							result = false;
							System.out.println("Cannot exexute " + ownersQuery + " query.");
							e.printStackTrace();
						}
					else
						System.out.println(ownersQuery);
				}
			}
		} catch (SQLException e) {
			result = false;
			System.out.println("Cannot create Statement object. Cannot insert Owners and Assignees in DB.");
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				result = false;
				System.out.println("Cannot close Statement object.");
				e.printStackTrace();
			}
			System.out.println(ownersCount + " registers inserted into Owners table.");
			System.out.println(assigneesCount + " registers inserted into Assignees table.");
		}
		return result;
	}

}
