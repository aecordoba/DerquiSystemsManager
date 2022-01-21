/*
 * 		Router.java
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
 * 		Router.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jan 28, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.router;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.OfficeCode;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Router {
	private int id;
	private String name;
	private InetAddress inetAddress;
	private RouterModel model;

	private static final Logger logger = LogManager.getLogger(Router.class);

	/**
	 * 
	 */
	public Router() {
	}

	/**
	 * @param name
	 * @param inetAddress
	 * @param model
	 */
	public Router(String name, InetAddress inetAddress, RouterModel model) {
		this.name = name;
		this.inetAddress = inetAddress;
		this.model = model;
	}

	public Router(ResultSet resultSet) throws SQLException, UnknownHostException {
		setId(resultSet.getInt("routerId"));
		setName(resultSet.getString("routerName"));
		setInetAddress(InetAddress.getByName(resultSet.getString("routerIpAddress")));
		int routerModelId = resultSet.getInt("routerModelId");
		RouterModel routerModel = RoutersModelsManager.getRouterModel(routerModelId);
		if (routerModel == null) {
			routerModel = new RouterModel(resultSet);
			RoutersModelsManager.addRouterModel(routerModel);
		}
		setModel(routerModel);
	}

	public static List<Router> getRoutersList(Connection connection) throws Exception {
		List<Router> routersList = null;
		CallableStatement statement = null;
		RoutersModelsManager.clearRoutersModelsList();
		RouterModel.clearRoutersManufacturersList();
		try {
			statement = connection.prepareCall("{call getRoutersList()}");
			ResultSet resultSet = statement.executeQuery();
			routersList = processResultSet(resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getRoutersList().", e);
			throw new Exception("Cannot get routers list from database.");
		} finally {
			statement.close();
		}
		return routersList;
	}

	private static List<Router> processResultSet(ResultSet resultSet) throws SQLException, UnknownHostException {
		List<Router> routersList = new ArrayList<>();
		while (resultSet.next()) {
			Router router = new Router(resultSet);
			routersList.add(router);
		}
		return routersList;
	}

	public void insert(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call insertRouter(?, ?, ?)}");
			statement.setString(1, getName());
			statement.setString(2, getInetAddress().getHostAddress());
			statement.setInt(3, getModel().getId());

			statement.execute();
			logger.debug(this + " was inserted in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure insertRouter().", e);
			throw new Exception("Cannot insert router in database.");
		} finally {
			statement.close();
		}
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
	 * @return the inetAddress
	 */
	public InetAddress getInetAddress() {
		return inetAddress;
	}

	/**
	 * @param inetAddress the inetAddress to set
	 */
	public void setInetAddress(InetAddress inetAddress) {
		this.inetAddress = inetAddress;
	}

	/**
	 * @return the model
	 */
	public RouterModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(RouterModel model) {
		this.model = model;
	}

	public static List<RouterModel> getRoutersModelsList() {
		return RoutersModelsManager.getRoutersModelsList();
	}

	public static List<RouterManufacturer> getRoutersManufacturersList() {
		return RouterModel.getRoutersManufacturersList();
	}

	private static class RoutersModelsManager {
		private static List<RouterModel> routersModelsList = new ArrayList<>();

		public static RouterModel getRouterModel(int id) {
			RouterModel routerModel = null;
			for (RouterModel temp : routersModelsList) {
				if (temp.getId() == id) {
					routerModel = temp;
					break;
				}
			}
			return routerModel;
		}

		public static void clearRoutersModelsList() {
			routersModelsList.clear();
		}

		public static void addRouterModel(RouterModel routerModel) {
			routersModelsList.add(routerModel);
		}

		public static List<RouterModel> getRoutersModelsList() {
			return routersModelsList;
		}
	}
}
