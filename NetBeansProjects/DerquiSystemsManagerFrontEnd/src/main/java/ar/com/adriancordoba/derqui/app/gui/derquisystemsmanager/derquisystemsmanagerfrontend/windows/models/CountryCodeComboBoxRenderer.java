/*
 * 		CountryCodeComboBoxRenderer.java
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
 * 		CountryCodeComboBoxRenderer.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Oct 25, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Country;
import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class CountryCodeComboBoxRenderer extends DefaultListCellRenderer {
    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/PeopleInternalFrameBundle");

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Country) {
            if (((Country) value).getId() == 0)
                setText(bundle.getString("PeopleInternalFrame.Select"));
            else
                setText(String.valueOf(((Country) value).getCode()));
        }
        return this;
    }
}
