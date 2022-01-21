/*
 * 		DSLAMBoardModelCellEditor.java
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
 * 		DSLAMBoardModelCellEditor.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Feb 11, 2017
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMBoardModel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class DSLAMBoardModelCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    private DSLAMBoardModel dslamBoardModel;
    private List<DSLAMBoardModel> dslamsBoardsModelsList;

    public DSLAMBoardModelCellEditor(List<DSLAMBoardModel> dslamsBoardsModelsList) {
        this.dslamsBoardsModelsList = dslamsBoardsModelsList;
    }

    @Override
    public Object getCellEditorValue() {
        return dslamBoardModel;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof DSLAMBoardModel)
            dslamBoardModel = (DSLAMBoardModel) value;

        JComboBox<DSLAMBoardModel> dslamBoardModelComboBox = new JComboBox<>();

        for (DSLAMBoardModel boardModel : dslamsBoardsModelsList)
            dslamBoardModelComboBox.addItem(boardModel);

        dslamBoardModelComboBox.setSelectedItem(dslamBoardModel);
        dslamBoardModelComboBox.addActionListener(this);

        return dslamBoardModelComboBox;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        JComboBox<DSLAMBoardModel> dslamBoardModelComboBox = (JComboBox<DSLAMBoardModel>) event.getSource();
        dslamBoardModel = (DSLAMBoardModel) dslamBoardModelComboBox.getSelectedItem();
    }
}
