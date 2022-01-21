/*
 * 		AddressesProcessor.java
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
 * 		AddressesProcessor.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Nov 1, 2019
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.addressestransponder.processors.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.excel97.ExcelFileScanner;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class AddressesProcessor {
	private List<List<String>> excelContent;
	private List<Subscriber> subscribersList;
	private static final Logger logger = LogManager.getLogger(AddressesProcessor.class);

	public AddressesProcessor() throws Exception {
		ExcelFileScanner addressesScanner = new ExcelFileScanner();
		excelContent = addressesScanner.getExcelContent();
		process();
	}

	private void process() {
		subscribersList = new ArrayList<>();
		for (List<String> row : excelContent) {
			Subscriber subscriber = new Subscriber();
			String cell0 = row.get(0);
			cell0 = cell0.substring(0, cell0.indexOf('.'));
			subscriber.setNumber("4" + cell0);
			if (row.size() > 3) {
				String cell2 = row.get(2);
				if (cell2 != null && !cell2.isEmpty()) {
					Owner owner = new Owner();
					owner.setName(cell2.trim());
					subscriber.setOwner(owner);

					String cell3 = row.get(3).trim();
					if (cell3 != null && !cell3.isEmpty()) {
						Address address = new Address();
						String streetName = (cell3.endsWith("UF")) ? cell3.substring(0, cell3.indexOf(" UF")) : cell3;
						Street street = new Street();
						street.setName(streetName);
						address.setStreet(street);

						if (row.size() > 4) {
							String cell4 = row.get(4);
							if (cell4.indexOf('.') != -1)
								cell4 = cell4.substring(0, cell4.indexOf('.'));
							try {
								Integer.parseInt(cell4);
								if (!cell4.isEmpty()) {
									address.setNumber(cell4);
								} else {
									address.setNumber("0");
								}
								subscriber.setAddress(address);
								subscribersList.add(subscriber);
							} catch (NumberFormatException exception) {
								logger.error("Cannot process subscriber " + cell0 + " from Excel file.");
							}
						} else
							address.setNumber("0");

					}
				}
			}
		}
	}
	
	/**
	 * @return the subscribersList
	 */
	public List<Subscriber> getSubscribersList() {
		return subscribersList;
	}
}
