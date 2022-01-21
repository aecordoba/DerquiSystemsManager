/*
 * 		RouterModel.java
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
 * 		RouterModel.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jan 28, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.router;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class RouterModel {
	private int id;
	private String name;
	private int ports;
	private RouterManufacturer manufacturer;

	private static final Logger logger = LogManager.getLogger(RouterModel.class);

	/**
	 * 
	 */
	public RouterModel() {
	}

	/**
	 * @param name
	 * @param ports
	 * @param manufacturer
	 */
	public RouterModel(String name, int ports, RouterManufacturer manufacturer) {
		this.name = name;
		this.ports = ports;
		this.manufacturer = manufacturer;
	}

	public RouterModel(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("routerModelId"));
		setName(resultSet.getString("routerModelName"));
		setPorts(resultSet.getInt("routerModelPorts"));
		int routerManufacturerId = resultSet.getInt("routerManufacturerId");
		RouterManufacturer routerManufacturer = RoutersManufacturersManager.getRouterManufacturer(routerManufacturerId);
		if (routerManufacturer == null) {
			routerManufacturer = new RouterManufacturer(resultSet);
			RoutersManufacturersManager.addRouterManufacturer(routerManufacturer);
		}
		setManufacturer(routerManufacturer);
	}

	public static List<RouterModel> getRoutersModelsList(Connection connection) throws Exception{
		List<RouterModel> routersModelsList = null;
		CallableStatement statement = null;
		clearRoutersManufacturersList();
		try {
			statement = connection.prepareCall("{call getRoutersModelsList()}");
			ResultSet resultSet = statement.executeQuery();
			routersModelsList = processResultSet(resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getRoutersModelsList().", e);
			throw new Exception("Cannot get routers models list from database.");
		} finally {
			statement.close();
		}
		return routersModelsList;
	}
	
	private static List<RouterModel> processResultSet(ResultSet resultSet) throws SQLException{
		List<RouterModel> routersModelsList = new ArrayList<>();
		while(resultSet.next()){
			RouterModel routerModel = new RouterModel(resultSet);
			routersModelsList.add(routerModel);
		}
		return routersModelsList;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the ports
	 */
	public int getPorts() {
		return ports;
	}

	/**
	 * @param ports the ports to set
	 */
	public void setPorts(int ports) {
		this.ports = ports;
	}

	/**
	 * @return the manufacturer
	 */
	public RouterManufacturer getManufacturer() {
		return manufacturer;
	}

	/**
	 * @param manufacturer the manufacturer to set
	 */
	public void setManufacturer(RouterManufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public static List<RouterManufacturer> getRoutersManufacturersList() {
		return RoutersManufacturersManager.getRoutersManufacturersList();
	}

	public static void clearRoutersManufacturersList() {
		RoutersManufacturersManager.clearRoutersManufacturersList();
	}

	private static class RoutersManufacturersManager {
		private static List<RouterManufacturer> routersManufacturersList = new ArrayList<>();

		public static RouterManufacturer getRouterManufacturer(int id) {
			RouterManufacturer routerManufacturer = null;
			for (RouterManufacturer temp : routersManufacturersList) {
				if (temp.getId() == id) {
					routerManufacturer = temp;
					break;
				}
			}
			return routerManufacturer;
		}

		public static void clearRoutersManufacturersList() {
			routersManufacturersList.clear();
		}

		public static void addRouterManufacturer(RouterManufacturer routerManufacturer) {
			routersManufacturersList.add(routerManufacturer);
		}

		public static List<RouterManufacturer> getRoutersManufacturersList() {
			return routersManufacturersList;
		}
	}
}
