/*
 * 		ConnectionsManager.java
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
 * 		ConnectionsManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jun 18, 2019
 */
package ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class ConnectionsManager {
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String connectionString = "jdbc:mysql://170.231.179.246:3306/ac1";
	private String user = "smapp";
	private String password = "smapp123";

	public Connection getConnection() throws Exception {
		Connection connection = null;

		try {
			Class.forName(driver).newInstance();
			connection = DriverManager.getConnection(connectionString, user, password);
		} catch (Exception ex) {
			throw new Exception("No database connection.", ex);
		}
		return connection;
	}

}
