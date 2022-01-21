/*
 * 		DSLAMBoardsTableModel.java
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
 * 		DSLAMsBoardsTableModel.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Feb 10, 2017
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMBoard;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMBoardModel;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class DSLAMBoardsTableModel extends AbstractTableModel {
    private String[] columnNames;
    private TreeMap<Integer, DSLAMBoard> data;
    private boolean editable;

    public DSLAMBoardsTableModel(String[] columnNames, TreeMap<Integer, DSLAMBoard> data, boolean editable) {
        super();
        this.columnNames = columnNames;
        this.data = data;
        this.editable = editable;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value = null;
        DSLAMBoard board = null;
        int slot = 0;
        int tableIndex = 0;
        for (int key : data.keySet()) {
            if (tableIndex == rowIndex) {
                slot = key;
                board = data.get(key);
                break;
            }
            tableIndex++;
        }

        switch (columnIndex) {
            case 0:
                value = slot;
                break;
            case 1:
                value = (board != null) ? board.getModel() : new DSLAMBoardModel();
                break;
            case 2:
                value = (board != null) ? board.getModel().getPorts() : 0;
                break;
            case 3:
                String remarks = (board != null) ? board.getRemarks() : null;
                value = (remarks != null) ? remarks : new String();
                break;
        }

        return value;
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if (editable && (col == 1 || col == 3))
            return true;
        else
            return false;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        DSLAMBoard board = null;
        int slot = 0;
        int tableIndex = 0;
        for (int key : data.keySet()) {
            if (tableIndex == row) {
                slot = key;
                board = data.get(key);
                if (board == null) {
                    board = new DSLAMBoard();
                    board.setSlot(slot);
                    data.put(slot, board);
                }
                break;
            }
            tableIndex++;
        }

        switch (col) {
            case 1:
                board.setModel((DSLAMBoardModel) value);
                break;
            case 3:
                board.setRemarks((String) value);
                break;
        }

        fireTableCellUpdated(row, col);
    }

    public void setData(TreeMap<Integer, DSLAMBoard> boardsMap) {
        clearData();
        data = boardsMap;
    }

    public TreeMap<Integer, DSLAMBoard> getData() {
        return data;
    }

    public void clearData() {
        int rows = data.size();
        data = new TreeMap<>();
        fireTableRowsDeleted(0, rows);
    }
}
