/*
 * 		User.java
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
 * 		User.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Jul 16, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.app.console.commonservices.encryption.Encryptor;
import ar.com.adriancordoba.app.console.commonservices.encryption.EncryptorSimpleFactory;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.Person;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.managers.RolesManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class User {
	private int id;
	private String username;
	private Person person;
	private List<Role> rolesList = new ArrayList<>();

	private static final Logger logger = LogManager.getLogger(User.class);

	public User() {
	}

	public User(String username, Person person, List<Role> rolesList) {
		this.username = username;
		this.person = person;
		this.rolesList = rolesList;
	}

	public static User getUser(Connection connection, String username, String password) throws Exception {
		User user = null;
		EncryptorSimpleFactory encryptorFactory = new EncryptorSimpleFactory();
		Encryptor encryptor = encryptorFactory.getEncryptor(EncryptorSimpleFactory.EncryptType.SHA512);

		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getUser(?, ?)}");
			statement.setString(1, username);
			statement.setString(2, encryptor.encrypt(password));
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				user = new User();
				user.setId(resultSet.getInt("userId"));

				Person person = new Person();
				person.setId(resultSet.getInt("personId"));
				person.setFirstName(resultSet.getString("firstName"));
				person.setMiddleName(resultSet.getString("middleName"));
				person.setLastName(resultSet.getString("lastName"));
				user.setPerson(person);
				user.setUsername(username);

				user.addRole(RolesManager.getRole(resultSet.getInt("roleId")));
				while (resultSet.next())
					user.addRole(RolesManager.getRole(resultSet.getInt("roleId")));
			}
		} catch (SQLException ex) {
			logger.error("Cannot execute stored procedure getUser().", ex);
			throw new Exception("Cannot get User from database.");
		} finally {
			statement.close();
		}
		return user;
	}

	public void insert(Connection connection, String password) throws Exception {
		EncryptorSimpleFactory encryptorFactory = new EncryptorSimpleFactory();
		Encryptor encryptor = encryptorFactory.getEncryptor(EncryptorSimpleFactory.EncryptType.SHA512);

		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call insertUser(?, ?, ?, ?)}");
			statement.setString(1, getUsername());
			statement.setString(2, encryptor.encrypt(password));
			statement.setInt(3, getPerson().getId());
			statement.setInt(4, getRolesList().get(getRolesList().size() - 1).getId());

			statement.execute();
			logger.debug("User " + getUsername() + " was inserted or modified in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure insertUser().", e);
			throw new Exception("Cannot insert user in database.");
		} finally {
			statement.close();
		}
	}

	public boolean addRole(Role role) {
		boolean result = false;
		if (!hasRole(role)) {
			rolesList.add(role);
			result = true;
		}
		return result;
	}

	public boolean hasRole(Role role) {
		return rolesList.contains(role);
	}

	public boolean hasRole(String roleName) {
		boolean result = false;
		for (Role role : rolesList) {
			if (role.getName().equals(roleName)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public boolean validate(Connection connection) throws Exception {
		boolean result = false;
		LocalDate databaseDate = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getTime()}");
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				databaseDate = resultSet.getDate("time").toLocalDate();
				if (LocalDate.now().compareTo(databaseDate.plusDays(45)) < 0)
					result = true;
			}
		} catch (SQLException ex) {
			logger.error("Cannot execute stored procedure getTime().", ex);
			throw new Exception("Cannot get time from database.");
		} finally {
			statement.close();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(username);
		stringBuilder.append(" (");
		stringBuilder.append(rolesList.get(0).getName());
		for (int i = 1; i < rolesList.size(); i++) {
			stringBuilder.append("/");
			stringBuilder.append(rolesList.get(i).getName());
		}
		stringBuilder.append(")");
		return stringBuilder.toString();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<Role> getRolesList() {
		return rolesList;
	}

	public void setRolesList(List<Role> rolesList) {
		this.rolesList = rolesList;
	}
}
