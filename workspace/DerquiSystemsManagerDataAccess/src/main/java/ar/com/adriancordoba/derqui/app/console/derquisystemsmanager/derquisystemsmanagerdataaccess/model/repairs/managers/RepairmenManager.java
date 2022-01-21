/*
 * 		RepairmenManager.java
 *   Copyright (C) 2018  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		RepairmenManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jan 26, 2018
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs.managers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs.Repairman;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs.RepairsCompany;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class RepairmenManager {
	private static List<Repairman> repairmenList;
	private static List<RepairsCompany> repairsCompaniesList;
	private static final Logger logger = LogManager.getLogger(RepairmenManager.class);

	static {
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
		connection = databaseConnectionsManager.takeConnection();
		try {
			loadRepairmenData(connection);
		} catch (Exception e) {
			logger.error("Cannot fill repairmen data from database.", e);
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
	}

	private static void loadRepairmenData(Connection connection) throws Exception {
		repairmenList = new ArrayList<>();
		repairsCompaniesList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getRepairmenDataList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				Repairman repairman = new Repairman(resultSet);
				addRepairman(repairman);
			}
			logger.info(repairmenList.size() + " repairmen found.");
			logger.info(repairsCompaniesList.size() + " repairs companies found.");
		} catch (SQLException e) {
			logger.error("Cannot execute getRepairmenDataList().", e);
			throw new Exception("Cannot get repairmen data list from database.");
		} finally {
			statement.close();
		}
	}

	public static Repairman getRepairman(int id) {
		Repairman repairman = null;
		for (Repairman temp : repairmenList) {
			if (temp.getId() == id) {
				repairman = temp;
				break;
			}
		}
		return repairman;
	}

	public static List<Repairman> getRepairmenList(int repairsCompanyId) {
		List<Repairman> list = new ArrayList<>();
		for (Repairman repairman : repairmenList) {
			if (repairman.getRepairsCompany().getId() == repairsCompanyId)
				list.add(repairman);
		}
		return list;
	}

	public static RepairsCompany getRepairsCompany(int id) {
		RepairsCompany repairsCompany = null;
		for (RepairsCompany temp : repairsCompaniesList) {
			if (temp.getId() == id) {
				repairsCompany = temp;
				break;
			}
		}
		return repairsCompany;
	}

	public static void addRepairman(Repairman repairman) {
		repairmenList.add(repairman);
	}

	public static void addRepairsCompany(RepairsCompany repairsCompany) {
		repairsCompaniesList.add(repairsCompany);
	}

	/**
	 * @return the repairmenList
	 */
	public static List<Repairman> getRepairmenList() {
		return repairmenList;
	}

	/**
	 * @param repairmenList the repairmenList to set
	 */
	public static void setRepairmenList(List<Repairman> repairmenList) {
		RepairmenManager.repairmenList = repairmenList;
	}

	/**
	 * @return the repairsCompaniesList
	 */
	public static List<RepairsCompany> getRepairsCompaniesList() {
		return repairsCompaniesList;
	}

	/**
	 * @param repairsCompaniesList the repairsCompaniesList to set
	 */
	public static void setRepairsCompaniesList(List<RepairsCompany> repairsCompaniesList) {
		RepairmenManager.repairsCompaniesList = repairsCompaniesList;
	}
}
