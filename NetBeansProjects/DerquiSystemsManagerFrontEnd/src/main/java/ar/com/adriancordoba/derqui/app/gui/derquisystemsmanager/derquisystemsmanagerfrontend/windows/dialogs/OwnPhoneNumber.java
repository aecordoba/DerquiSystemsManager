/*
 * 		OwnPhoneNumber.java
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
 * 		OwnPhoneNumber.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Jun 19, 2017
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.OfficeCode;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Phone;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.PhonesAnalyzer;
import java.awt.Component;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class OwnPhoneNumber {
    private PhonesAnalyzer ownNumerationAnalyzer;
    private Component parentComponent;
    private OfficeCode officeCode;
    private String number;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/MainFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(OwnPhoneNumber.class);

    public OwnPhoneNumber(Component component) {
        this.parentComponent = component;
        ownNumerationAnalyzer = new PhonesAnalyzer();
    }

    public Phone getPhone() {
        Phone phone = null;
        OwnPhoneNumberPanel ownPhoneNumberPanel = new OwnPhoneNumberPanel(ownNumerationAnalyzer);
        int option = JOptionPane.showConfirmDialog(parentComponent, ownPhoneNumberPanel, bundle.getString("OwnPhoneNumberDialog.title"), JOptionPane.OK_CANCEL_OPTION);
        while (option == JOptionPane.OK_OPTION) {
            if (isValidForm(ownPhoneNumberPanel)) {
                phone = new Phone(number, officeCode);
                break;
            } else
                option = JOptionPane.showConfirmDialog(parentComponent, ownPhoneNumberPanel, bundle.getString("OwnPhoneNumberDialog.title"), JOptionPane.OK_CANCEL_OPTION);
        }
        return phone;
    }

    private boolean isValidForm(OwnPhoneNumberPanel ownPhoneNumberPanel) {
        boolean valid = true;
        officeCode = ownPhoneNumberPanel.getSelectedOfficeCode();
        number = ownPhoneNumberPanel.getSelectedNumber();
        String ownNumber = (officeCode.getId() == 0) ? number : officeCode.getFullNumeration() + "-" + number;
        ownNumber = "'" + ownNumber + "'";
        try {
            int num = Integer.parseInt(number);
            if (number.length() != 4 || num < 0 || num > 9999) {
                JOptionPane.showMessageDialog(ownPhoneNumberPanel, ownNumber + " " + bundle.getString("OwnPhoneNumberPanel.ValidationProblem.Number"), bundle.getString("OwnPhoneNumberDialog.ValidationProblem.title"), JOptionPane.ERROR_MESSAGE);
                valid = false;
            } else {
                if (!isOwnPhoneNumber(ownNumerationAnalyzer.getOwnNumbersList(officeCode.getId()), number)) {
                    JOptionPane.showMessageDialog(ownPhoneNumberPanel, ownNumber + " " + bundle.getString("OwnPhoneNumberPanel.ValidationProblem.NotOwnNumber"), bundle.getString("OwnPhoneNumberDialog.ValidationProblem.title"), JOptionPane.ERROR_MESSAGE);
                    valid = false;
                }
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(ownPhoneNumberPanel, ownNumber + " " + bundle.getString("OwnPhoneNumberPanel.ValidationProblem.Number"), bundle.getString("OwnPhoneNumberDialog.ValidationProblem.title"), JOptionPane.ERROR_MESSAGE);
            valid = false;
        }

        return valid;
    }

    private boolean isOwnPhoneNumber(List<String> ownNumbersList, String number) {
        boolean result = false;
        for (String ownNumber : ownNumbersList) {
            if (ownNumber == null || number.startsWith(ownNumber)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
