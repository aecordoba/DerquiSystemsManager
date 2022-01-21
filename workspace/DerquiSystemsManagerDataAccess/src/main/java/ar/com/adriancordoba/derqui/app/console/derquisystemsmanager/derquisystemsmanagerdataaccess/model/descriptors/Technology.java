/*
 * 		Technology.java
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
 * 		Technology.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Sep 30, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public enum Technology {
	NEAX61SIGMA("Neax61Σ", 32), NEAX61SIGMA_ELU("Neax61Σ_ELU", 120), NEAX61E("Neax61E", 32), ZHONE("Zhone", 48);
	private String name;
	private int cableSize;

	private Technology(String name, int cableSize) {
		this.name = name;
		this.cableSize = cableSize;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the cableSize
	 */
	public int getCableSize() {
		return cableSize;
	}
}
