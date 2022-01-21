/*
 * 		SearchPerson.java
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
 * 		SearchPerson.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Dec 9, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.Person;
import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class SearchPerson {
    private Component parentComponent;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/AssignmentInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(SearchPerson.class);

    public SearchPerson(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    public Person search() {
        Person selectedPerson = null;
        SearchPersonPanel searchPersonPanel = new SearchPersonPanel();
        int option = JOptionPane.showConfirmDialog(parentComponent, searchPersonPanel, bundle.getString("SearchPersonDialog.title"), JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION)
            selectedPerson = searchPersonPanel.getSelectedPerson();
        return selectedPerson;
    }
}
