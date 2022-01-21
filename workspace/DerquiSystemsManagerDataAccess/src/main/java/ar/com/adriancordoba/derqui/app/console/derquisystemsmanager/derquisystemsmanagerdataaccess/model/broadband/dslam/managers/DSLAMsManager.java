/*
 * 		DSLAMsManager.java
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
 * 		DSLAMsManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Nov 18, 2019
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.managers;

import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAM;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMBoard;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMModel;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.router.Router;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class DSLAMsManager {
	private static List<DSLAM> dslamsList = new ArrayList<>();
	private static final Logger logger = LogManager.getLogger(DSLAMsManager.class);

	static {
		fillDSLAMsList();
	}

	public static void fillDSLAMsList() {
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		CallableStatement statement = null;
		dslamsList.clear();
		DSLAMsBoardsManager.clearDSLAMsBoardsList();
		try {
			databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
			connection = databaseConnectionsManager.takeConnection();
			statement = connection.prepareCall("{call getDSLAMsList()}");
			ResultSet resultSet = statement.executeQuery();
			processResultSet(resultSet);
			logger.debug(dslamsList.size() + " DSLAM(s) found.");
		} catch (Exception ex) {
			logger.error("Cannot execute stored procedure getDSLAMsList().", ex);
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
	}

	private static void processResultSet(ResultSet resultSet) throws SQLException, UnknownHostException {
		while (resultSet.next()) {
			int dslamId = resultSet.getInt("dslamId");
			DSLAM dslam = getDSLAM(dslamId);
			if (dslam == null) {
				dslam = new DSLAM(resultSet);
				// DSLAM model.
				int dslamModelId = resultSet.getInt("dslamModelId");
				DSLAMModel dslamModel = DSLAMsModelsManager.getDSLAMModel(dslamModelId);
				if (dslamModel == null) {
					dslamModel = new DSLAMModel(resultSet);
					DSLAMsModelsManager.addDSLAMModel(dslamModel);
				}
				dslam.setModel(dslamModel);
				// DSLAM boards (empty).
				for (Integer slot : dslamModel.getSlotsList())
					dslam.addDSLAMBoard(slot, null);
				// DSLAM router.
				int routerId = resultSet.getInt("routerId");
				Router router = RoutersManager.getRouter(routerId);
				if (router == null) {
					router = new Router(resultSet);
					RoutersManager.addRouter(router);
				}
				dslam.setRouter(router);

				dslam.setRemarks(resultSet.getString("dslamRemarks"));
				dslamsList.add(dslam);
			}
			// DSLAM boards.
			int boardId = resultSet.getInt("boardId");
			if (boardId != 0) {
				DSLAMBoard dslamBoard = DSLAMsBoardsManager.getDSLAMBoard(boardId);
				if (dslamBoard == null) {
					dslamBoard = new DSLAMBoard(resultSet);
					DSLAMsBoardsManager.addDSLAMBoard(dslamBoard);
				}
				dslam.addDSLAMBoard(dslamBoard.getSlot(), dslamBoard);
			}
		}
	}

	public static DSLAM getDSLAM(int id) {
		DSLAM dslam = null;
		for (DSLAM temp : dslamsList) {
			if (temp.getId() == id) {
				dslam = temp;
				break;
			}
		}
		return dslam;
	}

	public static DSLAM getDSLAMByBoard(int boardId) {
		DSLAM dslam = null;
		for (DSLAM temp : dslamsList) {
			TreeMap<Integer, DSLAMBoard> boardsMap = temp.getBoardsMap();
			for (int slot : boardsMap.keySet()) {
				DSLAMBoard board = boardsMap.get(slot);
				if (board != null && board.getId() == boardId) {
					dslam = temp;
					break;
				}
			}
			if (dslam != null)
				break;
		}
		return dslam;
	}

	public static void clearDSLAMsList() {
		dslamsList.clear();
	}

	public static void addDSLAM(DSLAM dslam) {
		dslamsList.add(dslam);
	}

	/**
	 * @return the dslamsList
	 */
	public static List<DSLAM> getDslamsList() {
		return dslamsList;
	}

	private static class DSLAMsModelsManager {
		private static List<DSLAMModel> dslamsModelsList = new ArrayList<>();

		public static DSLAMModel getDSLAMModel(int id) {
			DSLAMModel dslamModel = null;
			for (DSLAMModel temp : dslamsModelsList) {
				if (temp.getId() == id) {
					dslamModel = temp;
					break;
				}
			}
			return dslamModel;
		}

		public static void clearDSLAMsModelsList() {
			dslamsModelsList.clear();
		}

		public static void addDSLAMModel(DSLAMModel dslamModel) {
			dslamsModelsList.add(dslamModel);
		}

		public List<DSLAMModel> getDSLAMsModelsList() {
			return dslamsModelsList;
		}
	}
}
