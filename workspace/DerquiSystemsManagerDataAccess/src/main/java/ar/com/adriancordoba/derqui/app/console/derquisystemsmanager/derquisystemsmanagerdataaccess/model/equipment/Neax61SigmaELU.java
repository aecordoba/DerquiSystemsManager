/*
 * 		Neax61SigmaELU.java
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
 * 		Neax61SigmaELU.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Aug 25, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Site;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.managers.SitesManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Neax61SigmaELU {
	private int id;
	private String name;
	private String timeSwitch;
	private String kHighway;
	private String pHighway;
	private List<Integer> dtisList = new ArrayList<>();
	private Site site;

	/**
	 * 
	 */
	public Neax61SigmaELU() {
	}

	/**
	 * @param id
	 * @param name
	 * @param timeSwitch
	 * @param kHighway
	 * @param pHighway
	 * @param dtisList
	 * @param site
	 */
	public Neax61SigmaELU(int id, String name, String timeSwitch, String kHighway, String pHighway, List<Integer> dtisList, Site site) {
		this.id = id;
		this.name = name;
		this.timeSwitch = timeSwitch;
		this.kHighway = kHighway;
		this.pHighway = pHighway;
		this.dtisList = dtisList;
		this.site = site;
	}

	public Neax61SigmaELU(ResultSet resultSet, boolean fullData) throws SQLException {
		setId(resultSet.getInt("sigmaeluId"));
		setName(resultSet.getString("sigmaeluName"));
		setTimeSwitch(resultSet.getString("sigmaeluTimeSwitch"));
		setkHighway(resultSet.getString("sigmaeluKHighway"));
		setpHighway(resultSet.getString("sigmaeluPHighway"));
		if (fullData) {
			String dtis = resultSet.getString("dtisList");
			String[] dtisArray = dtis.split(",");
			for (String dti : dtisArray)
				dtisList.add(Integer.parseInt(dti));
		}
		setSite(SitesManager.getSite(resultSet.getInt("sigmaeluSiteId")));
	}

	public void addDTI(Integer dti) {
		dtisList.add(dti);
	}

	public String getDTIsListString() {
		StringBuilder dtisString = new StringBuilder();
		dtisString.append(dtisList.get(0));
		for (int i = 1; i < dtisList.size(); i++) {
			dtisString.append("-");
			dtisString.append(dtisList.get(i));
		}
		return dtisString.toString();
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(timeSwitch);
		stringBuilder.append(kHighway);
		stringBuilder.append(pHighway);
		if(dtisList.size() > 0) {
			stringBuilder.append(" (DTI ");
			for(int i = 0; i < dtisList.size(); i++) {
				stringBuilder.append(dtisList.get(i));
				if(i < dtisList.size() - 1)
					stringBuilder.append("/");
			}
			stringBuilder.append(")");
		}
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
	 * @return the timeSwitch
	 */
	public String getTimeSwitch() {
		return timeSwitch;
	}

	/**
	 * @param timeSwitch the timeSwitch to set
	 */
	public void setTimeSwitch(String timeSwitch) {
		this.timeSwitch = timeSwitch;
	}

	/**
	 * @return the kHighway
	 */
	public String getkHighway() {
		return kHighway;
	}

	/**
	 * @param kHighway the kHighway to set
	 */
	public void setkHighway(String kHighway) {
		this.kHighway = kHighway;
	}

	/**
	 * @return the pHighway
	 */
	public String getpHighway() {
		return pHighway;
	}

	/**
	 * @param pHighway the pHighway to set
	 */
	public void setpHighway(String pHighway) {
		this.pHighway = pHighway;
	}

	/**
	 * @return the dtisList
	 */
	public List<Integer> getDTIsList() {
		return dtisList;
	}

	/**
	 * @param dtisList the dtisList to set
	 */
	public void setDTIsList(List<Integer> dtisList) {
		this.dtisList = dtisList;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(Site site) {
		this.site = site;
	}

	/**
	 * @return the site
	 */
	public Site getSite() {
		return site;
	}
}
