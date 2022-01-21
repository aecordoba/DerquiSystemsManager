/*
 * 		SubscriberRestrictionsManager.java
 *   Copyright (C) 2017  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		SubscriberRestrictionsManager.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Nov 28, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.restriction.managers;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.restriction.OriginationRestriction;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.restriction.SubscriberRestriction;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.restriction.TerminationRestriction;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class SubscriberRestrictionsManager {
	private static List<SubscriberRestriction> subscriberRestrictionsList;
	private static final Logger logger = LogManager.getLogger(SubscriberRestrictionsManager.class);

	static {
		Connection connection = null;
		DatabaseConnectionsManager databaseConnectionsManager = null;
		try {
			databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
			connection = databaseConnectionsManager.takeConnection();
			getSubscriberRestrictionsList(connection);
			logger.debug(subscriberRestrictionsList.size() + " subscriber restrictions found.");
			logger.debug(getTerminationRestrictionsList().size() + " subscriber termination restrictions found.");
			logger.debug(getOriginationRestrictionsList().size() + " subscriber origination restrictions found.");
		} catch (Exception ex) {
			logger.error("SubscriberRestrictionsManager couldn't fill subscriber restrictions list.");
		} finally {
			databaseConnectionsManager.returnConnection(connection);
		}
	}

	private static void getSubscriberRestrictionsList(Connection connection) throws Exception {
		subscriberRestrictionsList = new ArrayList<>();
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getSubscriberRestrictionsList()}");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				SubscriberRestriction subscriberRestriction = new SubscriberRestriction();
				subscriberRestriction.setId(resultSet.getInt("id"));

				TerminationRestriction terminationRestriction = TerminationRestrictionsManager.getTerminationRestriction(resultSet.getInt("terminationRestrictionId"));
				if (terminationRestriction == null) {
					terminationRestriction = new TerminationRestriction(resultSet);
					TerminationRestrictionsManager.addTerminationRestriction(terminationRestriction);
				}
				subscriberRestriction.setTerminationRestriction(terminationRestriction);

				OriginationRestriction originationRestriction = OriginationRestrictionsManager.getOriginationRestriction(resultSet.getInt("originationRestrictionId"));
				if (originationRestriction == null) {
					originationRestriction = new OriginationRestriction(resultSet);
					OriginationRestrictionsManager.addOriginationRestriction(originationRestriction);
				}
				subscriberRestriction.setOriginationRestriction(originationRestriction);

				subscriberRestrictionsList.add(subscriberRestriction);
			}
		} catch (SQLException e) {
			logger.error("Cannot get subscriber restrictions list from database.", e);
			throw new Exception("Cannot get subscriber restrictions list from database.");
		} finally {
			statement.close();
		}
	}

	public static SubscriberRestriction getSubscriberRestriction(int id) {
		SubscriberRestriction subscribernRestriction = null;
		for (SubscriberRestriction temp : subscriberRestrictionsList) {
			if (temp.getId() == id) {
				subscribernRestriction = temp;
				break;
			}
		}
		return subscribernRestriction;
	}

	public static SubscriberRestriction getSubscriberRestriction(int terminationRestrictionId, int originationRestrictionId) {
		SubscriberRestriction subscriberRestriction = null;
		for (SubscriberRestriction temp : subscriberRestrictionsList) {
			if ((temp.getOriginationRestriction().getId() == originationRestrictionId) && (temp.getTerminationRestriction().getId() == terminationRestrictionId)) {
				subscriberRestriction = temp;
				break;
			}
		}
		return subscriberRestriction;
	}

	public static SubscriberRestriction getSubscriberRestriction(char terminationRestrictionName, String originationRestrictionName) {
		SubscriberRestriction subscriberRestriction = null;
		for (SubscriberRestriction temp : subscriberRestrictionsList) {
			if ((temp.getTerminationRestriction().getName() == terminationRestrictionName) && (temp.getOriginationRestriction().getName().equals(originationRestrictionName))) {
				subscriberRestriction = temp;
				break;
			}
		}
		return subscriberRestriction;
	}

	/**
	 * @return the subscriberRestrictionsList
	 */
	public static List<SubscriberRestriction> getSubscriberRestrictionsList() {
		return subscriberRestrictionsList;
	}

	public static List<TerminationRestriction> getTerminationRestrictionsList() {
		return TerminationRestrictionsManager.getTerminationRestrictionsList();
	}

	public static TerminationRestriction getTerminationRestriction(int id) {
		return TerminationRestrictionsManager.getTerminationRestriction(id);
	}

	public static TerminationRestriction getTerminationRestriction(char name) {
		return TerminationRestrictionsManager.getTerminationRestriction(name);
	}

	public static List<OriginationRestriction> getOriginationRestrictionsList() {
		return OriginationRestrictionsManager.getOriginationRestrictionsList();
	}

	public static OriginationRestriction getOriginationRestriction(int id) {
		return OriginationRestrictionsManager.getOriginationRestriction(id);
	}

	public static OriginationRestriction getOriginationRestriction(String name) {
		return OriginationRestrictionsManager.getOriginationRestriction(name);
	}

	/**
	 * 
	 * @author Adrián E. Córdoba [software.asia@gmail.com]
	 *
	 */
	private static class TerminationRestrictionsManager {
		private static List<TerminationRestriction> terminationRestrictionsList = new ArrayList<>();

		public static void addTerminationRestriction(TerminationRestriction terminationRestriction) {
			terminationRestrictionsList.add(terminationRestriction);
		}

		public static TerminationRestriction getTerminationRestriction(int id) {
			TerminationRestriction terminationRestriction = null;
			for (TerminationRestriction temp : terminationRestrictionsList) {
				if (temp.getId() == id) {
					terminationRestriction = temp;
					break;
				}
			}
			return terminationRestriction;
		}

		public static TerminationRestriction getTerminationRestriction(char name) {
			TerminationRestriction terminationRestriction = null;
			for (TerminationRestriction temp : terminationRestrictionsList) {
				if (temp.getName() == name) {
					terminationRestriction = temp;
					break;
				}
			}
			return terminationRestriction;
		}

		/**
		 * @return the terminationRestrictionsList
		 */
		public static List<TerminationRestriction> getTerminationRestrictionsList() {
			return terminationRestrictionsList;
		}
	}

	/**
	 * 
	 * @author Adrián E. Córdoba [software.asia@gmail.com]
	 *
	 */
	private static class OriginationRestrictionsManager {
		private static List<OriginationRestriction> originationRestrictionsList = new ArrayList<>();

		public static void addOriginationRestriction(OriginationRestriction originationRestriction) {
			originationRestrictionsList.add(originationRestriction);
		}

		public static OriginationRestriction getOriginationRestriction(int id) {
			OriginationRestriction originationRestriction = null;
			for (OriginationRestriction temp : originationRestrictionsList) {
				if (temp.getId() == id) {
					originationRestriction = temp;
					break;
				}
			}
			return originationRestriction;
		}

		public static OriginationRestriction getOriginationRestriction(String name) {
			OriginationRestriction originationRestriction = null;
			for (OriginationRestriction temp : originationRestrictionsList) {
				if (temp.getName().equals(name)) {
					originationRestriction = temp;
					break;
				}
			}
			return originationRestriction;
		}

		/**
		 * @return the originationRestrictionsList
		 */
		public static List<OriginationRestriction> getOriginationRestrictionsList() {
			return originationRestrictionsList;
		}
	}
}
