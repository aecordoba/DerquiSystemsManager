/*
 * 		DSLAMBoard.java
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
 * 		DSLAMBoard.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Feb 8, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMModel.DSLAMsManufacturersManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.managers.DSLAMsManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class DSLAMBoard {
	private int id;
	private DSLAMBoardModel model;
	private int slot;
	private String remarks;

	private static final Logger logger = LogManager.getLogger(DSLAMBoard.class);

	/**
	 * 
	 */
	public DSLAMBoard() {
	}

	/**
	 * @param model
	 * @param slot
	 * @param remarks
	 */
	public DSLAMBoard(DSLAMBoardModel model, int slot, String remarks) {
		this.model = model;
		this.slot = slot;
		this.remarks = remarks;
	}

	public DSLAMBoard(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("boardId"));
		int boardModelId = resultSet.getInt("boardModelId");
		DSLAMBoardModel dslamBoardModel = DSLAMsBoardsModelsManager.getDSLAMBoardModel(boardModelId);
		if (dslamBoardModel == null) {
			dslamBoardModel = new DSLAMBoardModel(resultSet);
			DSLAMsBoardsModelsManager.addDSLAMBoardModel(dslamBoardModel);
		}
		setModel(dslamBoardModel);

		setSlot(resultSet.getInt("boardSlot"));
		setRemarks(resultSet.getString("boardRemarks"));
	}

	public void insert(Connection connection, int dslamId, String dslamName) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call insertDSLAMBoard(?, ?, ?, ?, ?)}");
			statement.setInt(1, dslamId);

			int modelId = 0;
			if (model != null)
				modelId = getModel().getId();
			statement.setInt(2, modelId);

			statement.setInt(3, getSlot());

			int ports = 0;
			if (getSlot() == 0)
				ports = DSLAMsManager.getDSLAM(dslamId).getModel().getPorts();
			else
				ports = getModel().getPorts();
			statement.setInt(4, ports);

			statement.setString(5, getRemarks());

			statement.execute();
			logger.debug("DSLAM " + dslamName + " - slot " + slot + " board was inserted in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure insertDSLAMBoard().", e);
			throw new Exception("Cannot insert DSLAM in database.");
		} finally {
			statement.close();
		}
	}

	public void update(Connection connection, int dslamId, String dslamName) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call updateDSLAMBoard(?, ?, ?, ?, ?, ?)}");
			statement.setInt(1, getId());
			statement.setInt(2, dslamId);

			int modelId = 0;
			if (model != null)
				modelId = getModel().getId();
			statement.setInt(3, modelId);

			statement.setInt(4, getSlot());

			int ports = 0;
			if (getSlot() == 0)
				ports = DSLAMsManager.getDSLAM(dslamId).getModel().getPorts();
			else
				ports = getModel().getPorts();
			statement.setInt(5, ports);

			statement.setString(6, getRemarks());

			statement.execute();
			logger.debug("DSLAM " + dslamName + " - slot " + slot + " board was updated in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure updateDSLAMBoard().", e);
			throw new Exception("Cannot update DSLAM in database.");
		} finally {
			statement.close();
		}
	}

	/*
	 * (non-Javadoc) }
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DSLAMBoard [model=" + model + ", slot=" + slot + "]";
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
	 * @return the model
	 */
	public DSLAMBoardModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(DSLAMBoardModel model) {
		this.model = model;
	}

	/**
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * @param slot the slot to set
	 */
	public void setSlot(int slot) {
		this.slot = slot;
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

	private static class DSLAMsBoardsModelsManager {
		private static List<DSLAMBoardModel> dslamsBoardsModelsList = new ArrayList<>();

		public static DSLAMBoardModel getDSLAMBoardModel(int id) {
			DSLAMBoardModel dslamBoardModel = null;
			for (DSLAMBoardModel temp : dslamsBoardsModelsList) {
				if (temp.getId() == id) {
					dslamBoardModel = temp;
					break;
				}
			}
			return dslamBoardModel;
		}

		public static void clearDSLAMsBoardsModelsList() {
			dslamsBoardsModelsList.clear();
		}

		public static void addDSLAMBoardModel(DSLAMBoardModel dslamBoardModel) {
			dslamsBoardsModelsList.add(dslamBoardModel);
		}

		public static List<DSLAMBoardModel> getDSLAMsBoardsModelsList() {
			return dslamsBoardsModelsList;
		}
	}
}
