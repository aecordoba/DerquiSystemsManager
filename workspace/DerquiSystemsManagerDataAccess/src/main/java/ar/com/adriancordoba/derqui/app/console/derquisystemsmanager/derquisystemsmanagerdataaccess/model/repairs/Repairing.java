/*
 * 		Repairing.java
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
 * 		Repairing.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jan 28, 2018
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs.managers.RepairmenManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Repairing {
	private int id;
	private Repairman repairman;
	private String repairmanRemarks;
	private LocalDateTime assignmentTime;
	private LocalDate repairedDate;
	private List<RepairingCheck> repairingChecksList = new ArrayList<>();

	private static final Logger logger = LogManager.getLogger(Repairing.class);

	/**
	 * 
	 */
	public Repairing() {
	}

	/**
	 * @param serviceOrder
	 * @param repairman
	 * @param repairmanRemarks
	 * @param assignmentTime
	 * @param repairedDate
	 */
	public Repairing(Repairman repairman, String repairmanRemarks, LocalDateTime assignmentTime, LocalDate repairedDate) {
		this.repairman = repairman;
		this.repairmanRemarks = repairmanRemarks;
		this.assignmentTime = assignmentTime;
		this.repairedDate = repairedDate;
	}

	public Repairing(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("repairingId"));
		setRepairman(RepairmenManager.getRepairman(resultSet.getInt("repairmanId")));
		setRepairmanRemarks(resultSet.getString("repairmanRemarks"));
		Timestamp time = resultSet.getTimestamp("assignmentTime");
		if (time != null)
			setAssignmentTime(time.toLocalDateTime());
		Date date = resultSet.getDate("repairedDate");
		if (date != null)
			setRepairedDate(date.toLocalDate());
		if(resultSet.getInt("repairingCheckId") > 0)
			addRepairingCheck(new RepairingCheck(resultSet));
	}

	public void addRepairingCheck(RepairingCheck repairingCheck) {
		repairingChecksList.add(repairingCheck);
	}

	public RepairingCheck getLastRepairingCheck() {
		RepairingCheck repairingCheck = null;
		if (!repairingChecksList.isEmpty())
			repairingCheck = repairingChecksList.get(repairingChecksList.size() - 1);
		return repairingCheck;
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
	 * @return the repairman
	 */
	public Repairman getRepairman() {
		return repairman;
	}

	/**
	 * @param repairman the repairman to set
	 */
	public void setRepairman(Repairman repairman) {
		this.repairman = repairman;
	}

	/**
	 * @return the repairmanRemarks
	 */
	public String getRepairmanRemarks() {
		return repairmanRemarks;
	}

	/**
	 * @param repairmanRemarks the repairmanRemarks to set
	 */
	public void setRepairmanRemarks(String repairmanRemarks) {
		this.repairmanRemarks = repairmanRemarks;
	}

	/**
	 * @return the assignmentTime
	 */
	public LocalDateTime getAssignmentTime() {
		return assignmentTime;
	}

	/**
	 * @param assignmentTime the assignmentTime to set
	 */
	public void setAssignmentTime(LocalDateTime assignmentTime) {
		this.assignmentTime = assignmentTime;
	}

	/**
	 * @return the repairedDate
	 */
	public LocalDate getRepairedDate() {
		return repairedDate;
	}

	/**
	 * @param repairedDate the repairedDate to set
	 */
	public void setRepairedDate(LocalDate repairedDate) {
		this.repairedDate = repairedDate;
	}

	/**
	 * @return the repairingChecksList
	 */
	public List<RepairingCheck> getRepairingChecksList() {
		return repairingChecksList;
	}

	/**
	 * @param repairingChecksList the repairingChecksList to set
	 */
	public void setRepairingChecksList(List<RepairingCheck> repairingChecksList) {
		this.repairingChecksList = repairingChecksList;
	}
}
