/*
 * 		PairsProcessor.java
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
 * 		PairsProcessor.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Sep 2, 2019
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.distributortransponder.processors;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.excel97.ExcelFileScanner;
import ar.com.adriancordoba.app.console.switching.subscribers.Subscriber;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class PairsProcessor {
	private List<List<String>> excelContent;
	private static final Logger logger = LogManager.getLogger(PairsProcessor.class);

	/**
	 * Read Excel content into a list of lists of strings.
	 * @throws Exception
	 */
	public PairsProcessor() throws Exception {
		ExcelFileScanner pairsScanner = new ExcelFileScanner();
		excelContent = pairsScanner.getExcelContent();
	}

	public int getPairId(Subscriber subscriber, Connection connection) throws Exception {
		int pairId = 0;
		for (List<String> row : excelContent) {
			String cell0 = row.get(0);
			String excelSubscriberNumber = cell0.substring(0, cell0.indexOf('.'));
			if (subscriber.getPhoneNumber().substring(1).equals(excelSubscriberNumber)) {
				if (row.size() > 1 && !row.get(1).isEmpty() && row.get(1) != null) {
					String cell1 = row.get(1);
					char frame = cell1.charAt(0);
					char cable;
					int pair;
					try {
						if (frame == 'V') {
							cable = 'A';
							pair = Integer.parseInt(cell1.substring(1));
						} else {
							cable = row.get(1).charAt(1);
							pair = Integer.parseInt(cell1.substring(2));
						}
						pairId = getPairIdFromDatabase(connection, frame, cable, pair);
					} catch(NumberFormatException e) {
						logger.error("Cannot convert pair: " + cell1 + " for subscriber "+ subscriber.getPhoneNumber() + ".");
						pairId = -1;
					}catch (Exception e) {
						logger.error("Cannot get pair ID for susbcriber " + subscriber.getPhoneNumber() + ".", e);
						throw new Exception("Cannot get pair ID.");
					}
				}
				break;
			}
		}
		return pairId;
	}
	
	private int getPairIdFromDatabase(Connection connection, char frame, char cable, int pair) throws Exception {
		int pairId = 0;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getPairId(?, ?, ?)}");
			statement.setString(1, String.valueOf(frame));
			statement.setString(2, String.valueOf(cable));
			statement.setInt(3, (pair));
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next())
				pairId = resultSet.getInt("pairId");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getPairId() for frame " + frame + " cable " + cable + " pair " + pair + ".", e);
			throw new Exception("Cannot get pair ID from database.");
		} finally {
			statement.close();
		}
		return pairId;
	}
}
