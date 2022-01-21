/*
 * 		DSLAM.java
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
 * 		DSLAM.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jan 28, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.managers.DSLAMsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.router.Router;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Site;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.managers.SitesManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class DSLAM {
	private int id;
	private String name;
	private Site site;
	private InetAddress inetAddress;
	private Router router;
	private DSLAMModel model;
	private String remarks;
	private TreeMap<Integer, DSLAMBoard> boardsMap;

	private static final Logger logger = LogManager.getLogger(DSLAM.class);

	/**
	 * 
	 */
	public DSLAM() {
		boardsMap = new TreeMap<>();
	}

	/**
	 * @param name
	 * @param site
	 * @param inetAddress
	 * @param router
	 * @param model
	 * @param boardsMap
	 * @param remarks
	 */
	public DSLAM(String name, Site site, InetAddress inetAddress, Router router, DSLAMModel model, TreeMap<Integer, DSLAMBoard> boardsMap, String remarks) {
		this.name = name;
		this.site = site;
		this.inetAddress = inetAddress;
		this.router = router;
		this.model = model;
		this.boardsMap = boardsMap;
		this.remarks = remarks;
	}

	public DSLAM(ResultSet resultSet) throws SQLException, UnknownHostException {
		boardsMap = new TreeMap<>();
		setId(resultSet.getInt("dslamId"));
		setName(resultSet.getString("dslamName"));
		setSite(SitesManager.getSite(resultSet.getInt("dslamSiteId")));
		setInetAddress(InetAddress.getByName(resultSet.getString("dslamIpAddress")));
	}

	public DSLAM(ResultSet resultSet, boolean fromBoard) throws SQLException {
		if (fromBoard) {
			setId(resultSet.getInt("dslamId"));
			setName(resultSet.getString("dslamName"));
			setSite(SitesManager.getSite(resultSet.getInt("dslamSiteId")));
		} else {
			setId(resultSet.getInt("dslamId"));
			setName(resultSet.getString("dslamName"));
			setSite(SitesManager.getSite(resultSet.getInt("dslamSiteId")));
		}
	}

	public void insert(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call insertDSLAM(?, ?, ?, ?, ?, ?, ?)}");
			statement.setInt(1, getSite().getId());
			statement.setString(2, getName());
			statement.setString(3, getInetAddress().getHostAddress());
			statement.setInt(4, getRouter().getId());
			statement.setInt(5, getModel().getId());
			statement.setString(6, getRemarks());
			statement.registerOutParameter(7, Types.INTEGER);

			statement.execute();
			setId(statement.getInt(7));
			DSLAMsManager.addDSLAM(this);
			logger.debug("DSLAM " + getName() + " was inserted in database.");

			for (Integer slot : model.getSlotsList()) {
				DSLAMBoard board = (slot == 0) ? new DSLAMBoard() : boardsMap.get(slot);
				if (board != null)
					board.insert(connection, getId(), getName());
			}

		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure insertDSLAM().", e);
			throw new Exception("Cannot insert DSLAM in database.");
		} finally {
			statement.close();
		}
	}

	public void update(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call updateDSLAM(?, ?, ?, ?, ?, ?, ?)}");
			statement.setInt(1, getId());
			statement.setInt(2, getSite().getId());
			statement.setString(3, getName());
			statement.setString(4, getInetAddress().getHostAddress());
			statement.setInt(5, getRouter().getId());
			statement.setInt(6, getModel().getId());
			statement.setString(7, getRemarks());

			statement.execute();
			logger.debug("DSLAM " + getName() + " was updated in database.");

			for (Integer slot : model.getSlotsList()) {
				DSLAMBoard board = boardsMap.get(slot);
				if (board != null)
					board.update(connection, getId(), getName());
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure updateDSLAM().", e);
			throw new Exception("Cannot update DSLAM in database.");
		} finally {
			statement.close();
		}
	}

	public DSLAMBoard getDSLAMBoard(int slot) {
		return boardsMap.get(slot);
	}

	public void addDSLAMBoard(int slot, DSLAMBoard dslamBoard) {
		boardsMap.put(slot, dslamBoard);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName() + " (" + site.getCode() + ")";
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
	 * @return the site
	 */
	public Site getSite() {
		return site;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(Site site) {
		this.site = site;
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
	 * @return the router
	 */
	public Router getRouter() {
		return router;
	}

	/**
	 * @param router the router to set
	 */
	public void setRouter(Router router) {
		this.router = router;
	}

	/**
	 * @return the model
	 */
	public DSLAMModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(DSLAMModel model) {
		this.model = model;
	}

	/**
	 * @return the boardsMap
	 */
	public TreeMap<Integer, DSLAMBoard> getBoardsMap() {
		return boardsMap;
	}

	/**
	 * @param boardsMap the boardsMap to set
	 */
	public void setBoardsMap(TreeMap<Integer, DSLAMBoard> boardsMap) {
		this.boardsMap = boardsMap;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
