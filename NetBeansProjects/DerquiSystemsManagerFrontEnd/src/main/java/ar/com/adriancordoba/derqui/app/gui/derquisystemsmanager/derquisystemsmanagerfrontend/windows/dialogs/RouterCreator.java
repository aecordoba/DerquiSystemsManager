/*
 * 		RouterCreator.java
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
 * 		RouterCreator.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Feb 4, 2017
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.router.Router;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.router.RouterModel;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.StringFormat;
import java.awt.Component;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class RouterCreator {
    private Component parentComponent;
    private String name;
    private InetAddress inetAddress;
    private RouterModel model;
    private RouterCreatorPanel routerCreatorPanel;

    private List<String> errorMessagesList = new ArrayList<>();

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/DSLAMsInternalFrameBundle"); // NOI18N

    public RouterCreator(Component parentComponent) {
        this.parentComponent = parentComponent;
        routerCreatorPanel = new RouterCreatorPanel();
    }

    public boolean create() {
        boolean result = false;

        int option = JOptionPane.showConfirmDialog(parentComponent, routerCreatorPanel, bundle.getString("RouterCreatorDialog.title"), JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            if (isValidForm()) {
                Router router = new Router();
                router.setName(StringFormat.capitalize(name));
                router.setInetAddress(inetAddress);
                router.setModel(model);

                DatabaseConnectionsManager databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
                Connection connection = databaseConnectionsManager.takeConnection();
                try {
                    router.insert(connection);
                    result = true;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(routerCreatorPanel, bundle.getString("RouterCreatorPanel.error.message"), bundle.getString("RouterCreatorPanel.error.title"), JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                }
            } else
                JOptionPane.showMessageDialog(routerCreatorPanel, getErrorMessages(), bundle.getString("ValidationDialog.router.title"), JOptionPane.ERROR_MESSAGE);
        }
        return result;
    }

    private boolean isValidForm() {
        boolean valid = true;

        name = routerCreatorPanel.getName().trim();
        if (name.isEmpty() || name.length() > 45) {
            errorMessagesList.add(bundle.getString("ValidationDialog.routerName.invalid") + " " + name);
            valid = false;
        }

        if (routerCreatorPanel.getIPAddress().trim().isEmpty()) {
            errorMessagesList.add(bundle.getString("ValidationDialog.routerIPAddressEmpty.invalid") + " " + name);
            valid = false;
        } else {
            try {
                inetAddress = InetAddress.getByName(routerCreatorPanel.getIPAddress().trim());
            } catch (UnknownHostException ex) {
                errorMessagesList.add(bundle.getString("ValidationDialog.routerIPAddress.invalid") + " " + routerCreatorPanel.getIPAddress().trim());
                valid = false;
            }
        }

        model = routerCreatorPanel.getModel();
        if (model.getId() == 0) {
            errorMessagesList.add(bundle.getString("ValidationDialog.routerModel.invalid"));
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
