/*
 * 		SubscriberBroadbandState.java
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
 * 		SubscriberBroadbandState.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Dec 9, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.state;

import java.sql.ResultSet;
import java.sql.SQLException;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors.SubscriberBroadbandStateName;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors.SubscriberStateName;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class SubscriberBroadbandState {
	private int id;
	private SubscriberBroadbandStateName name;
	private String description;

	/**
	 * 
	 */
	public SubscriberBroadbandState() {
	}

	/**
	 * @param name
	 * @param description
	 */
	public SubscriberBroadbandState(SubscriberBroadbandStateName name, String description) {
		this.name = name;
		this.description = description;
	}

	public SubscriberBroadbandState(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("id"));
		setName(SubscriberBroadbandStateName.valueOf(resultSet.getString("name").toUpperCase()));
		setDescription(resultSet.getString("description"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SubscriberBroadbandState [id=" + id + ", name=" + name + ", description=" + description + "]";
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
	public SubscriberBroadbandStateName getName() {
		return name;
	}

	/**
	 * @param state the state to set
	 */
	public void setName(SubscriberBroadbandStateName name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
