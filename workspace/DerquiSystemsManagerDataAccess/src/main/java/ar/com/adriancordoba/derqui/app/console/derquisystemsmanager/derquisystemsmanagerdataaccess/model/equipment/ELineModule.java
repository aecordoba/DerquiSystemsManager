/*
 * 		ELineModule.java
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
 * 		ELineModule.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Sep 20, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class ELineModule extends LineModule {
	
	public ELineModule(){
		super();
	}
	
	/**
	 * 
	 * @param resultSet
	 * @throws SQLException
	 */
	public ELineModule(ResultSet resultSet) throws SQLException {
		super();
		setId(resultSet.getInt("eLineModuleId"));
		setName(resultSet.getString("eLineModuleName"));
		EFrame eFrame = EFramesManager.getEFrame(resultSet.getInt("eFrameId"));
		if (eFrame == null) {
			eFrame = new EFrame(resultSet);
			EFramesManager.addEFrame(eFrame);
		}
		setFrame(eFrame);
	}
	
	private static class EFramesManager {
		private static List<EFrame> eFramesList = new ArrayList<>();

		public static EFrame getEFrame(int id) {
			EFrame eFrame = null;
			for (EFrame temp : eFramesList)
				if (temp.getId() == id) {
					eFrame = temp;
					break;
				}
			return eFrame;
		}

		private static void clearEFramesList() {
			eFramesList.clear();
		}

		public static void addEFrame(EFrame eFrame) {
			eFramesList.add(eFrame);
		}

		/**
		 * @return the streetCablesList
		 */
		public static List<EFrame> getFramesList() {
			return eFramesList;
		}
	}
}
