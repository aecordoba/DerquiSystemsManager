/*
 * 		Owner.java
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
 * 		Owner.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Nov 1, 2019
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.addressestransponder.processors.model;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Owner {
	private String name;
	private String lastName;
	private String firstName;
	private String middleName;

	private String capitalizeNames(String names) {
		String result = null;
		if (names != null && !names.isEmpty()) {
			StringBuilder stringBuilder = new StringBuilder();
			String[] namesArray = names.split("\\s+");
			int length = namesArray.length;
			for (int i = 0; i < (length - 1); i++) {
				stringBuilder.append(namesArray[i].substring(0, 1).toUpperCase());
				stringBuilder.append(namesArray[i].substring(1).toLowerCase());
				stringBuilder.append(" ");
			}
			stringBuilder.append(namesArray[length - 1].substring(0, 1).toUpperCase());
			stringBuilder.append(namesArray[length - 1].substring(1).toLowerCase());
			result = stringBuilder.toString();
		}
		return result;
	}

	@Override
	public String toString() {
		return "Owner [name=" + name + ", lastName=" + lastName + ", firstName=" + firstName + ", middleName=" + middleName + "]";
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
		if (name.contains(",")) {
			String[] nameArray = name.split(",");
			lastName = capitalizeNames(nameArray[0].trim());
			if (nameArray.length > 1) {
				String[] rawNamesArray = nameArray[1].trim().split(" ");
				firstName = capitalizeNames(rawNamesArray[0]);
				if(rawNamesArray.length > 1) {
					middleName = new String();
					for(int i = 1; i < rawNamesArray.length; i++)
						middleName += " " + rawNamesArray[i];
					middleName = capitalizeNames(middleName.trim());
				}
				
			}
		} else
			lastName = name.trim();
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return middleName;
	}
}
