/*
 * 		SearchPeople.java
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
 * 		SearchPeople.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Aug 5, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.IdentificationType;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.Person;
import java.awt.Component;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class SearchPeople {
    private DefaultComboBoxModel<IdentificationType> identificationTypeComboBoxModel;
    private Component parentComponent;
    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/PeopleInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(SearchPeople.class);

    public SearchPeople(Component parentComponent, DefaultComboBoxModel<IdentificationType> identificationTypeComboBoxModel) {
        this.parentComponent = parentComponent;
        this.identificationTypeComboBoxModel = identificationTypeComboBoxModel;
    }

    public List<Person> search(Connection connection) throws Exception {
        List<Person> peopleList = null;

        SearchPeoplePanel searchPeoplePanel = new SearchPeoplePanel(identificationTypeComboBoxModel);
        try {
            int option = JOptionPane.showConfirmDialog(parentComponent, searchPeoplePanel, bundle.getString("SearchPeopleDialog.title"), JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) 
                peopleList = Person.getPeopleList(connection, searchPeoplePanel.getFirstName(), searchPeoplePanel.getMiddleName(), searchPeoplePanel.getLastName(), searchPeoplePanel.getIdentificationType(), searchPeoplePanel.getIdentificationNumber());
        } catch (NumberFormatException exception) {
            logger.warn("Cannot search for people." + exception.getMessage());
            JOptionPane.showMessageDialog(searchPeoplePanel, bundle.getString("ValidationDialog.identificationNumber.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
        }

        return peopleList;
    }
}
