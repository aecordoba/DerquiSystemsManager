/*
 * 		ELineTrunkFrame.java
 *   Copyright (C) 2016  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		ELineTrunkFrame.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Sep 20, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment;

import java.sql.ResultSet;
import java.sql.SQLException;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.managers.SitesManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class EFrame extends Frame {

	public EFrame() {
		super();
	}

	/**
	 * 
	 * @param resultSet
	 * @throws SQLException
	 */
	public EFrame(ResultSet resultSet) throws SQLException {
		super();
		setId(resultSet.getInt("eFrameId"));
		setName(resultSet.getString("eFrameName"));
		setSite(SitesManager.getSite(resultSet.getInt("eFrameSiteId")));
	}
}
