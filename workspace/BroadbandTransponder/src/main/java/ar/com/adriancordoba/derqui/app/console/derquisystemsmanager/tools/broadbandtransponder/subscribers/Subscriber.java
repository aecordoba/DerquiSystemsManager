/*
 * 		Subscriber.java
 *   Copyright (C) 2019  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		Subscriber.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Oct 3, 2019
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.broadbandtransponder.subscribers;

import java.util.Arrays;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Subscriber {
	private int officeCode;
	private char[] mcdu;

	@Override
	public String toString() {
		return "Subscriber [officeCode=" + officeCode + ", mcdu=" + Arrays.toString(mcdu) + "]";
	}

	/**
	 * @return the officeCode
	 */
	public int getOfficeCode() {
		return officeCode;
	}

	/**
	 * @param officeCode the officeCode to set
	 */
	public void setOfficeCode(int officeCode) {
		this.officeCode = officeCode;
	}

	/**
	 * @return the mcdu
	 */
	public char[] getMcdu() {
		return mcdu;
	}

	/**
	 * @param mcdu the mcdu to set
	 */
	public void setMcdu(char[] mcdu) {
		this.mcdu = mcdu;
	}
}
