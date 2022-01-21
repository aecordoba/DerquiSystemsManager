/*
 * 		AreaCreator.java
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
 * 		AreaCreator.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		May 2, 2017
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Area;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Country;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.managers.OfficeCodesDataManager;
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
public class AreaCreator {
    private Component parentComponent;
    private List<Area> areasList;
    private List<Country> countriesList;
    private AreaCreatorPanel areaCreatorPanel;
    private int countryCode;
    private String countryName;
    private int areaCode;
    private String areaName;

    private List<String> errorMessagesList = new ArrayList<>();

    private static final Logger logger = LogManager.getLogger(AreaCreator.class);
    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/MainFrameBundle"); // NOI18N

    public AreaCreator(Component parentComponent) {
        this.parentComponent = parentComponent;
        areasList = OfficeCodesDataManager.getAreasList();
        countriesList = OfficeCodesDataManager.getCountriesList();
        areaCreatorPanel = new AreaCreatorPanel(countryCode, countryName, areaCode, areaName, countriesList, areasList);
    }

    public boolean create() {
        boolean result = false;

        int option = JOptionPane.showConfirmDialog(parentComponent, areaCreatorPanel, bundle.getString("AreaCreatorDialog.title"), JOptionPane.OK_CANCEL_OPTION);
        while (option == JOptionPane.OK_OPTION) {
            if (isValidForm()) {
                Country country = new Country();
                country.setCode(countryCode);
                country.setName(countryName);
                Area area = new Area();
                area.setCode(areaCode);
                area.setName(areaName);
                area.setCountry(country);

                DatabaseConnectionsManager databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
                Connection connection = databaseConnectionsManager.takeConnection();
                try {
                    area.insert(connection);
                    logger.info(areaName + " area was created.");
                    result = true;
                } catch (Exception ex) {
                    logger.error("Cannot create area in database", ex);
                    JOptionPane.showMessageDialog(areaCreatorPanel, bundle.getString("AreaCreator.error.message"), bundle.getString("AreaCreator.error.title"), JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                    break;
                }

            } else {
                JOptionPane.showMessageDialog(areaCreatorPanel, getErrorMessages(), bundle.getString("AreaCreator.ValidationDialog.title"), JOptionPane.ERROR_MESSAGE);
                errorMessagesList.clear();
                areaCreatorPanel = new AreaCreatorPanel(countryCode, countryName, areaCode, areaName, countriesList, areasList);
                option = JOptionPane.showConfirmDialog(parentComponent, areaCreatorPanel, bundle.getString("AreaCreatorDialog.title"), JOptionPane.OK_CANCEL_OPTION);
            }
        }
        return result;
    }

    private boolean isValidForm() {
        boolean valid = true;

        String countryCodeStr = areaCreatorPanel.getCountryCode();
        if (!countryCodeStr.isEmpty()) {
            try {
                countryCode = Integer.parseInt(countryCodeStr);
            } catch (NumberFormatException exception) {
                errorMessagesList.add(bundle.getString("AreaCreator.ValidationDialog.countryCode.invalid"));
                valid = false;
            }
        } else {
            countryCode = 0;
            errorMessagesList.add(bundle.getString("AreaCreator.ValidationDialog.countryCode.invalid"));
            valid = false;
        }

        countryName = areaCreatorPanel.getCountryName();
        if (countryName == null || countryName.isEmpty()) {
            errorMessagesList.add(bundle.getString("AreaCreator.ValidationDialog.countryName.invalid"));
            valid = false;
        }

        String areaCodeStr = areaCreatorPanel.getAreaCode();
        if (!areaCodeStr.isEmpty()) {
            try {
                areaCode = Integer.parseInt(areaCodeStr);
            } catch (NumberFormatException exception) {
                errorMessagesList.add(bundle.getString("AreaCreator.ValidationDialog.areaCode.invalid"));
                valid = false;
            }
        } else {
            areaCode = 0;
            errorMessagesList.add(bundle.getString("AreaCreator.ValidationDialog.areaCode.invalid"));
            valid = false;
        }

        areaName = areaCreatorPanel.getAreaName();
        if (areaName == null || areaName.isEmpty()) {
            errorMessagesList.add(bundle.getString("AreaCreator.ValidationDialog.areaName.invalid"));
            valid = false;
        }

        if ((areaCode != 0) && (countryCode != 0)) {
            if (areaCodeExists()) {
                errorMessagesList.add(bundle.getString("AreaCreator.ValidationDialog.areaCode.exists"));
                valid = false;
            }
        }

        return valid;
    }

    private boolean areaCodeExists() {
        boolean exists = false;
        for (Area area : areasList) {
            if ((area.getCode() == areaCode) && (area.getCountry().getCode() == countryCode)) {
                exists = true;
                break;
            }
        }
        return exists;
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
