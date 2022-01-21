/*
 * 		DSLAMsBoardsManager.java
 *   Copyright (C) 2020  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		DSLAMsBoardsManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jan 6, 2020
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.managers;

import java.util.ArrayList;
import java.util.List;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMBoard;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class DSLAMsBoardsManager {
	private static List<DSLAMBoard> dslamsBoardsList = new ArrayList<>();

	public static DSLAMBoard getDSLAMBoard(int id) {
		DSLAMBoard dslamBoard = null;
		for (DSLAMBoard temp : dslamsBoardsList) {
			if (temp.getId() == id) {
				dslamBoard = temp;
				break;
			}
		}
		return dslamBoard;
	}

	public static void clearDSLAMsBoardsList() {
		dslamsBoardsList.clear();
	}

	public static void addDSLAMBoard(DSLAMBoard dslamBoard) {
		dslamsBoardsList.add(dslamBoard);
	}

	public static List<DSLAMBoard> getDSLAMsBoardsList() {
		return dslamsBoardsList;
	}
}
