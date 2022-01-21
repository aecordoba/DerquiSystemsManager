/*
 * 		CableSelection.java
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
 * 		CableSelection.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Jul 22, 2017
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetCable;
import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class CableSelection {
    private Component parentComponent;
    private CableSelectionPanel cableSelectionPanel;
    private StreetCable streetCable;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/MainFrameBundle"); // NOI18N

    public CableSelection(Component parentComponent) {
        this.parentComponent = parentComponent;
        cableSelectionPanel = new CableSelectionPanel();
    }

    public StreetCable getSelectedCable(String panelTitle) {
        int option = JOptionPane.showConfirmDialog(parentComponent, cableSelectionPanel, panelTitle, JOptionPane.OK_CANCEL_OPTION);
        while (option == JOptionPane.OK_OPTION) {
            if (isValidForm())
                break;
            else {
                JOptionPane.showMessageDialog(cableSelectionPanel, bundle.getString("CableSelectionPanel.ValidationDialog.message"), bundle.getString("CableSelectionPanel.ValidationDialog.title"), JOptionPane.ERROR_MESSAGE);
                option = JOptionPane.showConfirmDialog(parentComponent, cableSelectionPanel, panelTitle, JOptionPane.OK_CANCEL_OPTION);
            }
        }
        return streetCable;
    }

    private boolean isValidForm() {
        boolean valid = true;
        streetCable = cableSelectionPanel.getCable();
        if (streetCable == null)
            valid = false;
        return valid;
    }
}
