/*
 * 		Streets.java
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
 * 		Streets.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jun 21, 2019
 */
package ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.streets;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.MassivePopulation;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.Processor;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Streets implements Processor {
	public static String[] namesArray = { "DORREGO", "LAMADRID", "SARMIENTO", "BUENOS AIRES", "ENSENADA", "BELGRANO", "TUCUMAN", "CASEROS", "RIVADAVIA", "PALACIOS" };

	@Override
	public boolean process(Connection connection) {
		int count = 0;
		boolean result = true;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			for (int id = 1; id <= namesArray.length; id++) {
				String streetsQuery = "INSERT INTO Streets(id, name, code) " + "VALUES(" + id + ", \"" + namesArray[id - 1] + "\", \"" + namesArray[id - 1].substring(0, 3) + "\");";
				if (DB_IMPACT)
					try {
						statement.execute(streetsQuery);
						count++;
					} catch (SQLException e) {
						result = false;
						System.out.println("Cannot exexute " + streetsQuery + " query.");
						e.printStackTrace();
					}

				else
					System.out.println(streetsQuery);
			}
		} catch (SQLException e) {
			result = false;
			System.out.println("Cannot create Statement object. Cannot insert Streets in DB.");
			e.printStackTrace();
		} finally {
			try {
				statement.close();
			} catch (SQLException e) {
				result = false;
				System.out.println("Cannot close Statement object.");
				e.printStackTrace();
			}
			System.out.println(count + " registers inserted in Streets table.");
		}
		return result;
	}
}
