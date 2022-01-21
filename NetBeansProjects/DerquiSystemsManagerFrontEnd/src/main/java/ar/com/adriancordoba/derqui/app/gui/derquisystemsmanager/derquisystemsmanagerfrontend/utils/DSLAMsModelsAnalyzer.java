/*
 * 		DSLAMsModelsAnalyzer.java
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
 * 		DSLAMModelsAnalyzer.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Jan 28, 2017
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMManufacturer;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMModel;
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
public class DSLAMsModelsAnalyzer {
    private List<DSLAMModel> dslamsModelsList;
    private List<DSLAMManufacturer> dslamsManufacturersList;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/DSLAMsInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(DSLAMsModelsAnalyzer.class);

    public DSLAMsModelsAnalyzer(Connection connection) throws Exception {
        dslamsModelsList = DSLAMModel.getDSLAMsModelsList(connection);
        DSLAMModel selectDSLAMModel = new DSLAMModel();
        selectDSLAMModel.setId(0);
        selectDSLAMModel.setName(bundle.getString("DSLAMsInternalFrame.Select"));
        dslamsModelsList.add(0, selectDSLAMModel);

        dslamsManufacturersList = DSLAMModel.GetDSLAMsManufacturersList();
        dslamsManufacturersList.sort(null);
        DSLAMManufacturer selectDSLAMManufacturer = new DSLAMManufacturer();
        selectDSLAMManufacturer.setId(0);
        selectDSLAMManufacturer.setName(bundle.getString("DSLAMsInternalFrame.Select"));
        dslamsManufacturersList.add(0, selectDSLAMManufacturer);
    }

    public List<DSLAMModel> getDSLAMsModelsList() {
        return dslamsModelsList;
    }

    public List<DSLAMModel> getDSLAMsModelsList(int manufacturerId) {
        List<DSLAMModel> modelsList = new ArrayList<>();
        if (manufacturerId != 0) {

            for (int i = 1; i < dslamsModelsList.size(); i++) {
                DSLAMModel model = dslamsModelsList.get(i);
                if (model.getManufacturer().getId() == manufacturerId)
                    modelsList.add(model);
            }
        } else
            modelsList = dslamsModelsList;
        return modelsList;
    }

    public List<DSLAMModel> getDSLAMsModelsList(int manufacturerId, int portsLimit) {
        List<DSLAMModel> modelsList = new ArrayList<>();
        for (DSLAMModel temp : getDSLAMsModelsList(manufacturerId)) {
            if (temp.getPorts() >= portsLimit)
                modelsList.add(temp);
        }
        return modelsList;
    }

    public List<DSLAMManufacturer> getDSLAMsManufacturersList() {
        return dslamsManufacturersList;
    }
}
