/*
 * 		SearchNumber.java
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
 * 		SearchNumber.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Oct 17, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.OfficeCode;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Phone;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.PhonesAnalyzer;
import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class SearchNumber {
    private Component parentComponent;
    private PhonesAnalyzer ownNumerationAnalyzer;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/AssignmentInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(SearchNumber.class);

    public SearchNumber(Component parentComponent, PhonesAnalyzer ownNumerationAnalyzer) {
        this.parentComponent = parentComponent;
        this.ownNumerationAnalyzer = ownNumerationAnalyzer;
    }

    public Phone search(int siteId) {
        Phone phone = null;
        SearchNumberPanel searchNumberPanel;
        if (siteId == 0) {
            searchNumberPanel = new SearchNumberPanel(ownNumerationAnalyzer);
        } else {
            searchNumberPanel = new SearchNumberPanel(ownNumerationAnalyzer, siteId);
        }
        int option = JOptionPane.showConfirmDialog(parentComponent, searchNumberPanel, bundle.getString("SearchNumberDialog.title"), JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            OfficeCode officeCode = searchNumberPanel.getOfficeCode();
            String number = searchNumberPanel.getVacantNumber();
            if (officeCode != null && number != null) {
                phone = new Phone();
                phone.setOfficeCode(officeCode);
                phone.setNumber(number);
            }
        }
        return phone;
    }
}
