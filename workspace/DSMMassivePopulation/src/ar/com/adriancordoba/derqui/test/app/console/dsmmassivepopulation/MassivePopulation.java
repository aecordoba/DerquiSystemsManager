/*
 * 		MassivePopulation.java
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
 * 		MassivePopulation.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jun 18, 2019
 */
package ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation;

import java.sql.Connection;
import java.sql.SQLException;

import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.ConnectionsManager;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.Processor;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.addresses.Addresses;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.broadband.Broadband;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.owners.Owners;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.people.People;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.phonenumbers.PhoneNumbers;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.streets.Streets;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.subscribers.Subscribers;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.subscribersdata.SubscribersData;
import ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess.subscribersdata.wirings.Wiring;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class MassivePopulation {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Connection connection = null;
		try {
			connection = (new ConnectionsManager()).getConnection();

			// People
			Processor processor = new People();
			if (processor.process(connection))
				System.out.println("People process ends with no problem.");
			else
				System.out.println("Problems were found while People was inserted in DB.");

			// Streets
			processor = new Streets();
			if (processor.process(connection))
				System.out.println("Streets process ends with no problem.");
			else
				System.out.println("Problems were found while Streets were inserted in DB.");

			// PhoneNumbers
			processor = new PhoneNumbers();
			if (processor.process(connection))
				System.out.println("PhoneNumbers process ends with no problem.");
			else
				System.out.println("Problems were found while PhoneNumbers were inserted in DB.");

			// Addresses
			processor = new Addresses();
			if (processor.process(connection))
				System.out.println("Addresses process ends with no problem..");
			else
				System.out.println("Problems were found while Addresses were inserted in DB.");

			// SubscribersData
			processor = new SubscribersData();
			if (processor.process(connection))
				System.out.println("SubscribersData process ends with no problem.");
			else
				System.out.println("Problems were found while SubscribersData and SubscribersDataRecord were inserted in DB.");

			// Subscribers
			processor = new Subscribers();
			if (processor.process(connection))
				System.out.println("Subscribers process ends with no problem.");
			else
				System.out.println("Problems were found while Subscribers and SubscribersRecord were inserted in DB.");

			// Owners
			processor = new Owners();
			if (processor.process(connection))
				System.out.println("Owners process ends with no problem.");
			else
				System.out.println("Problems were found while Owners and Assignees were inserted in DB.");

			// Broadband
			processor = new Broadband();
			if (processor.process(connection))
				System.out.println("Broadband process ends with no problem.");
			else
				System.out.println("Problems were found while Broadband and BroadbandRecord were inserted in DB.");

			// Wiring
			processor = new Wiring();
			if (processor.process(connection))
				System.out.println("Wiring process ends with no problem.");
			else
				System.out.println("Problems were found while Wiring and WiringRecord were inserted in DB.");
		} catch (Exception e) {
			System.out.println("Cannot get a connection to database.");
			System.out.println("Process fail.");
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				System.out.println("Cannot close connection to database.");
				e.printStackTrace();
			}
			System.out.println("Main process end.");
		}
	}
}
