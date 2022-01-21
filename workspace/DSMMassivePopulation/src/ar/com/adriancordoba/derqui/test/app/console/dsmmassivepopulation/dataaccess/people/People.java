/*
 * 		People.java
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
 * 		People.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jun 21, 2019
 */
package ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.people;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.MassivePopulation;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.Processor;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class People implements Processor {
	private static String firstNamePrefix = "Aaaa";
	private static String lastNamePrefix = "Bbbb";
	private char a1 = 'a';
	private char a2 = 'a';
	private char a3 = 'a';
	private char a4 = 'a';

	@Override
	public boolean process(Connection connection) {
		int count = 0;
		boolean result = true;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			for (int personId = 2; personId < AMOUNT + 2; personId++) {
				String firstName = firstNamePrefix + a1 + a2 + a3 + a4;
				String lastName = lastNamePrefix + a1 + a2 + a3 + a4++;
				String peopleQuery = "INSERT INTO People(id, firstName, lastName)" + "VALUES(" + personId + ", \"" + firstName + "\", \"" + lastName + "\");";
				if (DB_IMPACT)
					try {
						statement.execute(peopleQuery);
						count++;
					} catch (SQLException e) {
						result = false;
						System.out.println("Cannot exexute " + peopleQuery + " query.");
						e.printStackTrace();
					}
				else
					System.out.println(peopleQuery);

				if (a4 == '{') {
					a4 = 'a';
					a3++;
					if (a3 == '{') {
						a3 = 'a';
						a2++;
						if (a2 == '{') {
							a2 = 'a';
							a1++;
						}
					}
				}
			}
		} catch (SQLException e) {
			result = false;
			System.out.println("Cannot create Statement object. Cannot insert People in DB.");
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				result = false;
				System.out.println("Cannot close Statement object.");
				e.printStackTrace();
			}
			System.out.println(count + " registers inserted into People table.");
		}
		return result;
	}
}
