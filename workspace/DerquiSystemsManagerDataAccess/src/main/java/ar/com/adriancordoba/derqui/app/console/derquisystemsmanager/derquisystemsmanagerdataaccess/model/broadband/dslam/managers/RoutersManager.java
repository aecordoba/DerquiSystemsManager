/*
 * 		RoutersManager.java
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
 * 		RoutersManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jan 6, 2020
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.managers;

import java.util.ArrayList;
import java.util.List;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.router.Router;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class RoutersManager {
	private static List<Router> routersList = new ArrayList<>();

	public static Router getRouter(int id) {
		Router router = null;
		for (Router temp : routersList) {
			if (temp.getId() == id) {
				router = temp;
				break;
			}
		}
		return router;
	}

	public static void clearRoutersList() {
		routersList.clear();
	}

	public static void addRouter(Router router) {
		routersList.add(router);
	}

	public List<Router> getRoutersList() {
		return routersList;
	}
}

