/*
 * 		PhoneNumbers.java
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
 * 		PhoneNumbers.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jun 21, 2019
 */
package ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.phonenumbers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.MassivePopulation;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.Processor;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class PhoneNumbers implements Processor {

	@Override
	public boolean process(Connection connection) {
		boolean result = true;
		Statement statement = null;
		int count = 0;

		try {
			statement = connection.createStatement();
			for (int number = 3; number < AMOUNT + 3; number++) {
				String phoneNumbersQuery = "INSERT INTO PhoneNumbers(id, officeCodeId, number) " + "VALUES(" + number + ", 1, \"" + String.format("%04d", number - 3) + "\");";
				if (DB_IMPACT)
					try {
						statement.execute(phoneNumbersQuery);
						count++;
					} catch (SQLException e) {
						result = false;
						System.out.println("Cannot exexute " + phoneNumbersQuery + " query.");
						e.printStackTrace();
					}
				else
					System.out.println(phoneNumbersQuery);
			}
		} catch (SQLException e) {
			result = false;
			System.out.println("Cannot create Statement object. Cannot insert PhoneNumbers in DB.");
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				result = false;
				System.out.println("Cannot close Statement object.");
				e.printStackTrace();
			}
			System.out.println(count + " registers inserted in PhoneNumbers table.");
		}
		return result;
	}
}
