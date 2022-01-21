/*
 * 		StreetsManager.java
 *   Copyright (C) 2017  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		StreetsManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jul 10, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.managers;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.IdentificationType;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.Street;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class StreetsManager {
	private static List<Street> streetsList = new ArrayList<>();
	
	private static final Logger logger = LogManager.getLogger(StreetsManager.class);

	static {
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		try {
			databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
			connection = databaseConnectionsManager.takeConnection();
			streetsList = Street.getStreetsList(connection);
			logger.debug(streetsList.size() + " streets found.");
		} catch (Exception ex) {
			logger.error("StreetsManager couldn't fill streets list.");
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
	}
	
	public static Street getStreet(int id){
		Street street= null;
		for (Street temp : streetsList){
			if(temp.getId() == id){
				street = temp;
				break;
			}
		}
		return street;
	}
	
	public static void clearStreetsList(){
		streetsList.clear();
	}
	
	public static void addStreet(Street street){
		streetsList.add(street);
	}
	
	public static List<Street> getStreetsList(){
		return streetsList;
	}
}
