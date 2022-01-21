/*
 * 		UserCreator.java
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
 * 		UserCreator.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Jun 29, 2017
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.Person;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.Role;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.User;
import java.awt.Component;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class UserCreator {
    private Component parentComponent;
    private UserCreatorPanel userCreatorPanel;
    private Person person;
    private String username;
    private String password;
    private Role role;

    private List<String> errorMessagesList = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger(UserCreator.class);
    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/MainFrameBundle"); // NOI18N

    public UserCreator(Component parentComponent, Person person) {
        this.person = person;
        this.parentComponent = parentComponent;
        userCreatorPanel = new UserCreatorPanel(person, username, password, role);
    }

    public boolean create() {
        boolean result = false;

        int option = JOptionPane.showConfirmDialog(parentComponent, userCreatorPanel, bundle.getString("UserCreatorDialog.title"), JOptionPane.OK_CANCEL_OPTION);
        while (option == JOptionPane.OK_OPTION) {
            if (isValidForm()) {
                User user = new User();
                user.setPerson(person);
                user.setUsername(username);
                user.addRole(role);

                DatabaseConnectionsManager databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
                Connection connection = databaseConnectionsManager.takeConnection();
                try {
                    user.insert(connection, password);
                    logger.info(username + " user was created or modified.");
                    result = true;
                } catch (Exception ex) {
                    logger.error("Cannot create/modify user in database", ex);
                    JOptionPane.showMessageDialog(userCreatorPanel, bundle.getString("UserCreator.error.message"), bundle.getString("UserCreator.error.title"), JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                    break;
                }

            } else {
                JOptionPane.showMessageDialog(userCreatorPanel, getErrorMessages(), bundle.getString("UserCreator.ValidationDialog.title"), JOptionPane.ERROR_MESSAGE);
                errorMessagesList.clear();
                userCreatorPanel = new UserCreatorPanel(person, username, password, role);
                option = JOptionPane.showConfirmDialog(parentComponent, userCreatorPanel, bundle.getString("UserCreatorDialog.title"), JOptionPane.OK_CANCEL_OPTION);
            }

        }
        return result;
    }

    private boolean isValidForm() {
        boolean valid = true;

        username = userCreatorPanel.getUsername();
        if (username.length() < 6) {
            errorMessagesList.add(bundle.getString("UserCreator.ValidationDialog.username.invalid"));
            valid = false;
        }

        password = userCreatorPanel.getPassword();
        if (password.length() < 4) {
            errorMessagesList.add(bundle.getString("UserCreator.ValidationDialog.password.invalid"));
            valid = false;
        } else {
            if (!password.equals(userCreatorPanel.getConfirmPassword())) {
                errorMessagesList.add(bundle.getString("UserCreator.ValidationDialog.passwordMatching.invalid"));
                valid = false;
            }
        }

        role = userCreatorPanel.getSelectedRole();
        if (role.getId() == 0) {
            errorMessagesList.add(bundle.getString("UserCreator.ValidationDialog.role.invalid"));
            valid = false;
        }

        return valid;
    }

    private String getErrorMessages() {
        StringBuilder errorMessages = new StringBuilder();
        for (String errorMessage : errorMessagesList) {
            errorMessages.append(errorMessage);
            errorMessages.append("\n");
        }
        return errorMessages.toString();
    }
}
