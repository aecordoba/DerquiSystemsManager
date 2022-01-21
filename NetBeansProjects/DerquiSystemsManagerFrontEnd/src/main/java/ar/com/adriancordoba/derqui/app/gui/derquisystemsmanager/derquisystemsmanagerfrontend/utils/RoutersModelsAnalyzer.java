/*
 * 		RoutersModelsAnalyzer.java
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
 * 		RoutersAnalyzer.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Feb 4, 2017
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.router.RouterManufacturer;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.router.RouterModel;
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
public class RoutersModelsAnalyzer {
    private List<RouterModel> routersModelsList;
    private List<RouterManufacturer> routersManufacturersList;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/DSLAMsInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(DSLAMsModelsAnalyzer.class);

    public RoutersModelsAnalyzer(Connection connection) throws Exception {
        routersModelsList = RouterModel.getRoutersModelsList(connection);
        RouterModel selectRouterModel = new RouterModel();
        selectRouterModel.setId(0);
        selectRouterModel.setName(bundle.getString("DSLAMsInternalFrame.Select"));
        routersModelsList.add(0, selectRouterModel);

        routersManufacturersList = RouterModel.getRoutersManufacturersList();
        RouterManufacturer selectRouterManufacturer = new RouterManufacturer();
        selectRouterManufacturer.setId(0);
        selectRouterManufacturer.setName(bundle.getString("DSLAMsInternalFrame.Select"));
        routersManufacturersList.add(0, selectRouterManufacturer);
    }

    public List<RouterModel> getRoutersModelsList() {
        return routersModelsList;
    }

    public List<RouterModel> getRoutersModelsList(int manufacturerId) {
        List<RouterModel> modelsList = new ArrayList<>();
        for (int i = 1; i < routersModelsList.size(); i++) {
            RouterModel routerModel = routersModelsList.get(i);
            if (routerModel.getManufacturer().getId() == manufacturerId)
                modelsList.add(routerModel);
        }
        return modelsList;
    }

    public List<RouterManufacturer> getRoutersManufacturersList() {
        return routersManufacturersList;
    }
}
