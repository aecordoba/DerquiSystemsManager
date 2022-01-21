/*
 * 		RepairingCheck.java
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
 * 		RepairingCheck.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Feb 10, 2018
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.User;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.managers.UsersManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class RepairingCheck {
	private int id;
	private User user;
	private String remarks;
	private Boolean approved;
	private LocalDateTime time;

	/**
	 * 
	 */
	public RepairingCheck() {
	}

	/**
	 * @param user
	 * @param remarks
	 * @param approved
	 * @param time
	 */
	public RepairingCheck(User user, String remarks, Boolean approved, LocalDateTime time) {
		this.user = user;
		this.remarks = remarks;
		this.approved = approved;
		this.time = time;
	}

	public RepairingCheck(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("repairingCheckId"));
		setUser(UsersManager.getUser(resultSet.getInt("userId")));
		setRemarks(resultSet.getString("repairingCheckRemarks"));
		Timestamp time = resultSet.getTimestamp("repairingCheckTime");
		if (time != null) {
			setTime(time.toLocalDateTime());
			setApproved(resultSet.getBoolean("approved"));
		}
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
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
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

	/**
	 * @return the approved
	 */
	public Boolean isApproved() {
		return approved;
	}

	/**
	 * @param approved the approved to set
	 */
	public void setApproved(Boolean approved) {
		this.approved = approved;
	}

	/**
	 * @return the time
	 */
	public LocalDateTime getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(LocalDateTime time) {
		this.time = time;
	}
}
