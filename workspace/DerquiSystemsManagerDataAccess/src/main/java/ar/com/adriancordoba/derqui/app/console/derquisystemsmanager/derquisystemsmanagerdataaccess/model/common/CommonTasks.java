/*
 * 		CommonTasks.java
 *   Copyright (C) 2016  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		CommonTasks.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Nov 15, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.common;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class CommonTasks {
	private static final Logger logger = LogManager.getLogger(CommonTasks.class);

	public static void fillOwnNumerationTables(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call fillOwnNumerationTables()}");
			statement.execute();
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure fillOwnNumerationTables().", e);
			throw new Exception("Cannot fill own numeration tables.");
		} finally {
			statement.close();
		}
	}

	public static void clearOwnNumerationTables(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call clearOwnNumerationTables()}");
			statement.execute();
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure clearOwnNumerationTables().", e);
			throw new Exception("Cannot clear own numeration tables.");
		} finally {
			statement.close();
		}
	}
}
