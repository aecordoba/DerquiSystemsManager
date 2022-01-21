/*
 * 		RestrictionsProcessor.java
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
 * 		RestrictionsProcessor.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Nov 6, 2019
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.restrictionstransponder.processors.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.excel97.ExcelFileScanner;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class RestrictionsProcessor {
	private List<List<String>> excelContent;
	private List<Subscriber> subscribersList;
	private static final Logger logger = LogManager.getLogger(RestrictionsProcessor.class);

	public RestrictionsProcessor() throws Exception {
		ExcelFileScanner restrictionsScanner = new ExcelFileScanner();
		excelContent = restrictionsScanner.getExcelContent();
		process();
	}

	private void process() {
		subscribersList = new ArrayList<>();
		for (List<String> row : excelContent) {
			Subscriber subscriber = new Subscriber();
			String cell0 = row.get(0);
			subscriber.setNumber("4" + cell0.trim());
			if (row.size() > 3) {
				String cell3 = row.get(3);
				if (cell3 != null && !cell3.isEmpty()) {
					subscriber.setRestriction(cell3.trim());
				}
			}
			subscribersList.add(subscriber);
		}
		subscribersList.remove(0);
	}

	/**
	 * @return the subscribersList
	 */
	public List<Subscriber> getSubscribersList() {
		return subscribersList;
	}
}
