/*
 * 		SearchEquipment.java
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
 * 		SearchEquipment.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Nov 24, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Equipment;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.SitesAnalyzer;
import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class SearchEquipment {
    private Component parentComponent;
    private SitesAnalyzer sitesAnalyzer;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/AssignmentInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(SearchEquipment.class);

    public SearchEquipment(Component parentComponent, SitesAnalyzer sitesAnalyzer) {
        this.parentComponent = parentComponent;
        this.sitesAnalyzer = sitesAnalyzer;
    }

    public Equipment search() {
        Equipment selectedEquipment = null;
        SearchEquipmentPanel searchEquipmentPanel = new SearchEquipmentPanel(sitesAnalyzer);
        int option = JOptionPane.showConfirmDialog(parentComponent, searchEquipmentPanel, bundle.getString("SearchEquipmentDialog.title"), JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
            selectedEquipment = searchEquipmentPanel.getVacantEquipment();
        return selectedEquipment;
    }
}
