/*
 * 		PairsTableModel.java
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
 * 		PairsTableModel.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Aug 31, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetPair;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class PairsTableModel extends AbstractTableModel {

    private String[] columnNames;
    private List<StreetPair> data;
    private boolean editable;

    public PairsTableModel(String[] columnNames, List<StreetPair> data, boolean editable) {
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
        StreetPair pair = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                value = String.format("%1$04d", pair.getPair());
                break;
            case 1:
                value = new Boolean(pair.isAvailable());
                break;
            case 2:
                String remarks = pair.getRemarks();
                value = (remarks != null) ? remarks : new String();
                break;
            case 3:
                boolean used = true;
                if (pair.getWiringId() == 0) {
                    used = false;
                }
                value = new Boolean(used);
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
        if (editable && ((col == 1 && data.get(row).getWiringId() == 0) || col == 2)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        StreetPair pair = data.get(row);
        switch (col) {
            case 0:
                pair.setPair((Integer) value);
                break;
            case 1:
                pair.setAvailable((Boolean) value);
                break;
            case 2:
                String remarks = null;
                if (!((String) value).isEmpty()) {
                    remarks = (String) value;
                }
                pair.setRemarks(remarks);
                break;
            case 3:
                if ((Boolean) value == false) {
                    pair.setWiringId(0);
                }
                break;
        }

        fireTableCellUpdated(row, col);
    }

    public void setData(List<StreetPair> pairsList) {
        clearData();
        data = pairsList;
    }

    public List<StreetPair> getData() {
        return data;
    }

    public void clearData() {
        int rows = data.size();
        data = new ArrayList<>();
        fireTableRowsDeleted(0, rows);
    }
}
