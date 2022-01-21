/*
 * 		ServiceOrderQuery.java
 *   Copyright (C) 2017  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		ServiceOrderQuery.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		May 17, 2018
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs;

import java.awt.Component;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class ServiceOrderQuery {
    private Component parentComponent;
    private int serviceOrderNumber;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/RepairsInternalFrameBundle"); // NOI18N

    public ServiceOrderQuery(Component parentComponent) {
        this.parentComponent = parentComponent;
    }

    public int getNumber() {
        int number = 0;
        ServiceOrderQueryPanel serviceOrderQueryPanel = new ServiceOrderQueryPanel();
        int option = JOptionPane.showConfirmDialog(parentComponent, serviceOrderQueryPanel, bundle.getString("ServiceOrderQueryDialog.title"), JOptionPane.OK_CANCEL_OPTION);
        while (option == JOptionPane.OK_OPTION) {
            try {
                number = Integer.parseInt(serviceOrderQueryPanel.getServiceOrderNumber());
                if (number == 0)
                    JOptionPane.showMessageDialog(serviceOrderQueryPanel, bundle.getString("ServiceOrderQueryDialog.ValidationProblem.Number"), bundle.getString("ServiceOrderQueryDialog.ValidationProblem.title"), JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(serviceOrderQueryPanel, bundle.getString("ServiceOrderQueryDialog.ValidationProblem.Number"), bundle.getString("ServiceOrderQueryDialog.ValidationProblem.title"), JOptionPane.ERROR_MESSAGE);
            }

            if (number == 0)
                option = JOptionPane.showConfirmDialog(parentComponent, serviceOrderQueryPanel, bundle.getString("ServiceOrderQueryDialog.title"), JOptionPane.OK_CANCEL_OPTION);
            else
                break;
        }
        return number;
    }
}
