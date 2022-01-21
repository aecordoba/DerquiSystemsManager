/*
 * 		Addresses.java
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
 * 		Addresses.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jun 21, 2019
 */
package ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.addresses;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.MassivePopulation;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.Processor;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.streets.Streets;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Addresses implements Processor {

	@Override
	public boolean process(Connection connection) {
		int count = 0;
		boolean result = true;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			for (int addressId = 2; addressId < AMOUNT + 2; addressId++) {
				String addressesQuery = "INSERT INTO Addresses(id, streetId, number, locationId, phoneNumberId) " + "VALUES(" + addressId + ", " + (int) (Math.random() * Streets.namesArray.length + 1) + ", " + String.valueOf((int) (Math.random() * 9999 + 1)) + ", 2, " + (addressId + 1) + ");";
				if (DB_IMPACT)
					try {
						statement.execute(addressesQuery);
						count++;
					} catch (SQLException e) {
						result = false;
						System.out.println("Cannot exexute " + addressesQuery + " query.");
						e.printStackTrace();
					}
				else
					System.out.println(addressesQuery);
			}
		} catch (SQLException e) {
			result = false;
			System.out.println("Cannot create Statement object. Cannot insert Addresses in DB.");
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				result = false;
				System.out.println("Cannot close Statement object.");
				e.printStackTrace();
			}
			System.out.println(count + " registers inserted in Addresses table.");
		}
		return result;
	}

}
