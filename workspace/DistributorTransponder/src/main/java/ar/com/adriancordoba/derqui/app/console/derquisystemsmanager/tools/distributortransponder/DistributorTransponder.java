/*
 * 		DistributorTransponder.java
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
 * 		DistributorTransponder.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Aug 29, 2019
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.distributortransponder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.logging.Logging;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.tools.distributortransponder.processors.MainProcessor;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class DistributorTransponder {
	private static final Logger logger;
	
	static {
		Logging.configure();
		logger = LogManager.getLogger(DistributorTransponder.class);
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		logger.info("Distributor Transponder is starting.");
		try {
			MainProcessor mainProcessor = new MainProcessor();
			
			if(mainProcessor.process())
				logger.info("All process were executed properly.");
			else
				logger.error("Some processes failed");
		} catch (Exception e) {
			logger.error("Could not execute Main Process properly.");
		}
		logger.info("Distributor Transponder end.");

	}

}
