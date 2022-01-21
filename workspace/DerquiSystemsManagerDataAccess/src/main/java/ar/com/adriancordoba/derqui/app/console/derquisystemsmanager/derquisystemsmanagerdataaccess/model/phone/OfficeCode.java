/*
 * 		OfficeCode.java
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
 * 		OfficeCode.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Oct 20, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class OfficeCode {
	private int id;
	private int code;
	private Area area;
	private boolean own;

	/**
	 * 
	 */
	public OfficeCode() {
	}

	/**
	 * @param id
	 * @param code
	 * @param area
	 * @param own
	 */
	public OfficeCode(int id, int code, Area area, boolean own) {
		this.id = id;
		this.code = code;
		this.area = area;
		this.own = own;
	}

	public OfficeCode(ResultSet resultSet, Area area) throws SQLException {
		setId(resultSet.getInt("officeCodeId"));
		setCode(resultSet.getInt("officeCode"));
		setArea(area);
		if(resultSet.getString("number") != null)
			own = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String result;
		if (code == 0)
			result = "OfficeCode [id=" + id + "]";
		else
			result = "OfficeCode [id=" + id + ": +" + area.getCountry().getCode() + " (" + area.getCode() + ") " + code + "]";
		return result;
	}

	public String getFullNumeration() {
		Country country = area.getCountry();
		return "+" + country.getCode() + "(" + area.getCode() + ")" + code;
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
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the area
	 */
	public Area getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(Area area) {
		this.area = area;
	}

	/**
	 * @return the own
	 */
	public boolean isOwn() {
		return own;
	}

	/**
	 * @param own the own to set
	 */
	public void setOwn(boolean own) {
		this.own = own;
	}
}
