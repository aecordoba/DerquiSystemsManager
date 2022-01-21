/*
 * 		DSLAMsBoardsModelsAnalyzer.java
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
 * 		DSLAMsBoardsModelsAnalyzer.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Feb 11, 2017
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMBoardModel;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class DSLAMsBoardsModelsAnalyzer {
    private List<DSLAMBoardModel> dslamsBoardsModelsList;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/DSLAMsInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(DSLAMsBoardsModelsAnalyzer.class);

    public DSLAMsBoardsModelsAnalyzer(Connection connection) throws Exception {
        dslamsBoardsModelsList = DSLAMBoardModel.getDSLAMsBoardsModelsList(connection);
        DSLAMBoardModel selectBoardModel = new DSLAMBoardModel();
        selectBoardModel.setId(0);
        selectBoardModel.setName(bundle.getString("DSLAMsInternalFrame.BoradModel.Select"));
        dslamsBoardsModelsList.add(0, selectBoardModel);
    }

    public List<DSLAMBoardModel> getDSLAMsBoardsModelsList() {
        return dslamsBoardsModelsList;
    }

    public List<DSLAMBoardModel> getDSLAMsBoardsModelsList(int manufacturerId) {
        List<DSLAMBoardModel> modelsList = new ArrayList<>();
        modelsList.add(dslamsBoardsModelsList.get(0));
        for (int i = 1; i < dslamsBoardsModelsList.size(); i++) {
            DSLAMBoardModel model = dslamsBoardsModelsList.get(i);
            if (model.getManufacturer().getId() == manufacturerId)
                modelsList.add(model);
        }
        return modelsList;
    }
}
