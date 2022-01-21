/*
 * 		SigmaLineModule.java
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
 * 		SigmaLineModule.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Sep 19, 2016
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
public class SigmaLineModule extends LineModule {

	public SigmaLineModule() {
		super();
	}

	/**
	 * 
	 * @param resultSet
	 * @throws SQLException
	 */
	public SigmaLineModule(ResultSet resultSet) throws SQLException {
		super();
		setId(resultSet.getInt("sigmaLineModuleId"));
		setName(resultSet.getString("sigmaLineModuleName"));
		SigmaFrame sigmaFrame = SigmaFramesManager.getSigmaFrame(resultSet.getInt("sigmaFrameId"));
		if (sigmaFrame == null) {
			sigmaFrame = new SigmaFrame(resultSet);
			SigmaFramesManager.addSigmaFrame(sigmaFrame);
		}
		setFrame(sigmaFrame);
	}

	private static class SigmaFramesManager {
		private static List<SigmaFrame> sigmaFramesList = new ArrayList<>();

		public static SigmaFrame getSigmaFrame(int id) {
			SigmaFrame sigmaFrame = null;
			for (SigmaFrame temp : sigmaFramesList)
				if (temp.getId() == id) {
					sigmaFrame = temp;
					break;
				}
			return sigmaFrame;
		}

		private static void clearSigmaFramesList() {
			sigmaFramesList.clear();
		}

		public static void addSigmaFrame(SigmaFrame sigmaFrame) {
			sigmaFramesList.add(sigmaFrame);
		}

		/**
		 * @return the streetCablesList
		 */
		public static List<SigmaFrame> getSigmaFramesList() {
			return sigmaFramesList;
		}
	}
}
