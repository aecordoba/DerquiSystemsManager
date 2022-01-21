/*
 * 		Processor.java
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
 * 		Processor.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jun 21, 2019
 */
package ar.com.adriancordoba.derqui.test.app.console.dsmmassivepopulation.dataaccess;

import java.sql.Connection;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public interface Processor {
	static final int AMOUNT = 7000;
	static final boolean DB_IMPACT = true;
	static final int BROADBAND_SUBSCRIBERS_RATE = 5;
	static final int OWNERS_SUBSCRIBERS_RATE = 9;
	boolean process(Connection connection);
}
