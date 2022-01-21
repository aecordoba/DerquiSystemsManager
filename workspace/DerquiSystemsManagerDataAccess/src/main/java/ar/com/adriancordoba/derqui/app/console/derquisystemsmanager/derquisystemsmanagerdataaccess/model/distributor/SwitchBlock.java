/*
 * 		SwitchBlock.java
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
 * 		SwitchBlock.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Sep 14, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor;

import java.sql.ResultSet;
import java.sql.SQLException;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.managers.SitesManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class SwitchBlock {
	private int id;
	private String name;
	private String description;
	private Site site;
	private int positions;

	/**
	 * @throws Exception
	 * 
	 */
	public SwitchBlock() {
	}

	/**
	 * @param id
	 * @param name
	 * @param description
	 * @param site
	 * @param positions
	 * @throws Exception
	 */
	public SwitchBlock(int id, String name, String description, Site site, int positions) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.site = site;
		this.positions = positions;
	}

	public SwitchBlock(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("id"));
		setName(resultSet.getString("name"));
		setDescription(resultSet.getString("description"));
		setPositions(resultSet.getInt("positions"));
		setSite(SitesManager.getSite(resultSet.getInt("siteId")));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(getName());
		stringBuilder.append(" (");
		if (site != null)
			stringBuilder.append(getSite().getCode());
		stringBuilder.append(")");
		return stringBuilder.toString();
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
	 * @return the positions
	 */
	public int getPositions() {
		return positions;
	}

	/**
	 * @param positions the positions to set
	 */
	public void setPositions(int positions) {
		this.positions = positions;
	}
}
