/*
 * 		DSLAMBoardComboBoxRenderer.java
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
 * 		DSLAMBoardComboBoxRenderer.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Apr 1, 2017
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMBoard;
import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class DSLAMBoardComboBoxRenderer extends DefaultListCellRenderer {
    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/WiringInternalFrameBundle"); // NOI18N

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value instanceof DSLAMBoard) {
            int slot = ((DSLAMBoard) value).getSlot();
            value = String.valueOf((slot == 0) ? bundle.getString("WiringInternalFrame.Select.short") : slot);
        }

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        return this;
    }
}
