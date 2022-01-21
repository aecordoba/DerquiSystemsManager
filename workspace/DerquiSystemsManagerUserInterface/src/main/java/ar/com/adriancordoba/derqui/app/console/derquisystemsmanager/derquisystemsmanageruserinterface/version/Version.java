/*
 * 		Version.java
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
 * 		Version.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		May 28, 2018
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanageruserinterface.version;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Version {
	private static final String VERSION = "4.1.0";
	private static final String FRONT_END_REQUIRED_VERSION = "4.1.0";

	private static final Logger logger = LogManager.getLogger(Version.class);

	public static String getVersion() {
		return VERSION;
	}

	public static boolean isDependenciesVersionSatisfied() {
		boolean result = false;
		logger.info("Running DerquiSystemsManagerUserInterface version " + getVersion() + ".");

		boolean frontEndDependenciesSatisfied = ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.version.Version.isDependenciesVersionSatisfied();
		String frontEndVersion = ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.version.Version.getVersion();
		if (frontEndDependenciesSatisfied) {
			logger.info("Running DerquiSystemsManagerFrontEnd version " + frontEndVersion + ".");
			String[] requiredVersionArray = FRONT_END_REQUIRED_VERSION.split("\\.");
			String[] versionArray = frontEndVersion.split("\\.");
			if (Integer.parseInt(versionArray[0]) > Integer.parseInt(requiredVersionArray[0]))
				result = true;
			else if (Integer.parseInt(versionArray[0]) == Integer.parseInt(requiredVersionArray[0])) {
				if (Integer.parseInt(versionArray[1]) > Integer.parseInt(requiredVersionArray[1]))
					result = true;
				else if (Integer.parseInt(versionArray[1]) == Integer.parseInt(requiredVersionArray[1])) {
					if (Integer.parseInt(versionArray[2]) >= Integer.parseInt(requiredVersionArray[2]))
						result = true;
				}
			}
			if (!result)
				logger.error("Running DerquiSystemsManagerFrontEnd version " + frontEndVersion + ", but version " + FRONT_END_REQUIRED_VERSION + " is required.");
		}
		return result;
	}
}
