/*
 * 		PairSelection.java
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
 * 		PairSelection.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Sep 12, 2019
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetPair;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.CablesAnalyzer;
import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class PairSelection {
    private Component parentComponent;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/WiringInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(SearchNumber.class);

    public PairSelection(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    public StreetPair getStreetPair(int siteId, CablesAnalyzer cablesAnalizer) {
        StreetPair streetPair = null;
        PairSelectionPanel pairSelectionPanel = new PairSelectionPanel(siteId, cablesAnalizer);
        int option = JOptionPane.showConfirmDialog(parentComponent, pairSelectionPanel, bundle.getString("PairSelectionDialog.title"), JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            streetPair = pairSelectionPanel.getStreetPair();
        }
        return streetPair;
    }
}
