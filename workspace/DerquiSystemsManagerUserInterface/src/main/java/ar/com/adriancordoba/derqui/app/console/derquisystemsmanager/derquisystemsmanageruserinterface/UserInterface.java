/*
 * 		UserInterface.java
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
 * 		UserInterface.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jul 16, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanageruserinterface;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.logging.Logging;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanageruserinterface.process.MainProcess;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class UserInterface {
	private static final Logger logger;

	static {
		Logging.configure();
		logger = LogManager.getLogger(UserInterface.class);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("Derqui Systems Manager is starting...");
		MainProcess mainProcess;
		try {
			mainProcess = new MainProcess();
			if (!mainProcess.process())
				logger.info("End of Derqui Systems Manager.");
		} catch (Exception e) {
			logger.fatal("Derqui Systems Manager ends with fatal errors.", e);
		}
	}
}
