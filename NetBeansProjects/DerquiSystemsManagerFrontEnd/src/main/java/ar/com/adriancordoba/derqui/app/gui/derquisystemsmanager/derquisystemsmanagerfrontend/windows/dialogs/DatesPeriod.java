/*
 * 		DatesPeriod.java
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
 * 		DatesPeriod.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Jul 5, 2017
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs;

import java.awt.Component;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class DatesPeriod {
    private Component parentComponent;
    private DatesPeriodPanel datesPeriodPanel;
    private LocalDate fromDate;
    private LocalDate toDate;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/MainFrameBundle"); // NOI18N

    public DatesPeriod(Component parentComponent) {
        this.parentComponent = parentComponent;
        datesPeriodPanel = new DatesPeriodPanel();
    }

    public void getDatesPeriod(String panelTitle) {
        int option = JOptionPane.showConfirmDialog(parentComponent, datesPeriodPanel, panelTitle, JOptionPane.OK_CANCEL_OPTION);
        while (option == JOptionPane.OK_OPTION) {
            if (isValidForm())
                break;
            else {
                JOptionPane.showMessageDialog(datesPeriodPanel, bundle.getString("DatesPeriodPanel.ValidationDialog.message"), bundle.getString("DatesPeriodPanel.ValidationDialog.title"), JOptionPane.ERROR_MESSAGE);
                fromDate = null;
                toDate = null;
                option = JOptionPane.showConfirmDialog(parentComponent, datesPeriodPanel, panelTitle, JOptionPane.OK_CANCEL_OPTION);
            }
        }
    }

    private boolean isValidForm() {
        boolean valid = true;
        try {
            fromDate = LocalDate.of(datesPeriodPanel.getFromYear(), datesPeriodPanel.getFromMonth(), datesPeriodPanel.getFromDay());
            toDate = LocalDate.of(datesPeriodPanel.getToYear(), datesPeriodPanel.getToMonth(), datesPeriodPanel.getToDay());
            if (fromDate.compareTo(toDate) >= 0)
                valid = false;
        } catch (DateTimeException exception) {
            valid = false;
        }
        return valid;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }
}
