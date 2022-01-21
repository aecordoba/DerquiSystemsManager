/*
 * 		SigmaL3AddressEquipment.java
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
 * 		SigmaL3AddressEquipment.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Aug 25, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Site;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.managers.SitesManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class SigmaL3AddressEquipment extends Equipment {
	private Neax61SigmaELU neax61SigmaELU;
	private int l3Address;

	private static final Logger logger = LogManager.getLogger(SigmaL3AddressEquipment.class);

	/**
	 * 
	 */
	public SigmaL3AddressEquipment() {
	}

	/**
	 * @param id
	 * @param neax61SigmaELU
	 * @param l3Address
	 */
	public SigmaL3AddressEquipment(int id, Neax61SigmaELU neax61SigmaELU, int l3Address) {
		super(id);
		this.neax61SigmaELU = neax61SigmaELU;
		this.l3Address = l3Address;
	}

	public SigmaL3AddressEquipment(ResultSet resultSet) throws SQLException {
		super(resultSet.getInt("sigmal3addrId"));
		setL3Address(resultSet.getInt("sigmal3addr"));
		int sigmaeluId = resultSet.getInt("sigmaeluId");
		Neax61SigmaELU neax61SigmaELU = Neax61SigmaELUsManager.getNeax61SigmaELU(sigmaeluId);
		if (neax61SigmaELU == null) {
			neax61SigmaELU = new Neax61SigmaELU(resultSet, false);
			Neax61SigmaELUsManager.addNeax61SigmaELU(neax61SigmaELU);
		}
		setNeax61SigmaELU(neax61SigmaELU);
	}

	public static List<Equipment> getVacantSigmaL3AddressEquipment(Connection connection, int siteId) throws Exception {
		List<Equipment> vacantEquipmentList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getVacantSigmaL3AddrEquipmentList(?)}");
			statement.setInt(1, siteId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				SigmaL3AddressEquipment equipment = new SigmaL3AddressEquipment();
				equipment.setId(resultSet.getInt("sigmal3addrId"));
				equipment.setL3Address(resultSet.getInt("sigmal3addr"));
				int sigmaeluId = resultSet.getInt("sigmaeluId");
				Neax61SigmaELU neax61SigmaELU = Neax61SigmaELUsManager.getNeax61SigmaELU(sigmaeluId);
				if (neax61SigmaELU == null) {
					neax61SigmaELU = new Neax61SigmaELU(resultSet, false);
					Neax61SigmaELUsManager.addNeax61SigmaELU(neax61SigmaELU);
				}
				equipment.setNeax61SigmaELU(neax61SigmaELU);
				vacantEquipmentList.add(equipment);
			}
			logger.debug(vacantEquipmentList.size() + " NEAX61Σ-ELU vacant equipment found in " + SitesManager.getSite(siteId).getName() + ".");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getVacantSigmaL3AddrEquipmentList().", e);
			throw new Exception("Cannot get NEAX61Σ-ELU vacant equipment from database.");
		} finally {
			statement.close();
		}
		return vacantEquipmentList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return neax61SigmaELU.getName() + ": " + String.format("%05d", l3Address);
	}

	/**
	 * @return the neax61SigmaELU
	 */
	public Neax61SigmaELU getNeax61SigmaELU() {
		return neax61SigmaELU;
	}

	/**
	 * @param neax61SigmaELU the neax61SigmaELU to set
	 */
	public void setNeax61SigmaELU(Neax61SigmaELU neax61SigmaELU) {
		this.neax61SigmaELU = neax61SigmaELU;
	}

	/**
	 * @return the l3Address
	 */
	public int getL3Address() {
		return l3Address;
	}

	/**
	 * @param l3Address the l3Address to set
	 */
	public void setL3Address(int l3Address) {
		this.l3Address = l3Address;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.
	 * derquisystemsmanagerdataaccess.model.Equipment#getSite()
	 */
	@Override
	public Site getSite() {
		return neax61SigmaELU.getSite();
	}

	private static class Neax61SigmaELUsManager {
		private static List<Neax61SigmaELU> sigmaELUsList = new ArrayList<>();

		public static Neax61SigmaELU getNeax61SigmaELU(int id) {
			Neax61SigmaELU sigmaELU = null;
			for (Neax61SigmaELU temp : sigmaELUsList) {
				if (temp.getId() == id) {
					sigmaELU = temp;
					break;
				}
			}
			return sigmaELU;
		}

		public static void clearNeax61SigmaELUsList() {
			sigmaELUsList.clear();
		}

		public static void addNeax61SigmaELU(Neax61SigmaELU sigmaELU) {
			sigmaELUsList.add(sigmaELU);
		}

		public List<Neax61SigmaELU> getNeax61SigmaELUsList() {
			return sigmaELUsList;
		}
	}
}
