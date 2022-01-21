/*
 * 		Repairman.java
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
 * 		Repairman.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jan 26, 2018
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs;

import java.sql.ResultSet;
import java.sql.SQLException;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.Person;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs.managers.RepairmenManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Repairman {
	private int id;
	private Person person;
	private RepairsCompany repairsCompany;
	private String services;

	/**
	 * @param person
	 * @param repairsCompany
	 * @param service
	 */
	public Repairman(Person person, RepairsCompany repairsCompany, String services) {
		this.person = person;
		this.repairsCompany = repairsCompany;
		this.services = services;
	}

	/**
	 * 
	 */
	public Repairman() {
	}

	public Repairman(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("repairmanId"));
		
		Person person = new Person();
		person.setFirstName(resultSet.getString("firstName"));
		person.setMiddleName(resultSet.getString("middleName"));
		person.setLastName(resultSet.getString("lastName"));
		setPerson(person);
		
		setServices(resultSet.getString("services"));
		
		int repairsCompanyId = resultSet.getInt("repairsCompanyId");
		RepairsCompany repairsCompany = RepairmenManager.getRepairsCompany(repairsCompanyId);
		if(repairsCompany == null) {
			repairsCompany = new RepairsCompany(resultSet);
			RepairmenManager.addRepairsCompany(repairsCompany);
		}
		setRepairsCompany(repairsCompany);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return person.toString();
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
	 * @return the person
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person the person to set
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * @return the repairsCompany
	 */
	public RepairsCompany getRepairsCompany() {
		return repairsCompany;
	}

	/**
	 * @param repairsCompany the repairsCompany to set
	 */
	public void setRepairsCompany(RepairsCompany repairsCompany) {
		this.repairsCompany = repairsCompany;
	}

	/**
	 * @return the services
	 */
	public String getServices() {
		return services;
	}

	/**
	 * @param services the services to set
	 */
	public void setServices(String services) {
		this.services = services;
	}
}
