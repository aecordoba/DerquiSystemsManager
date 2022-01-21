/*
 * 		ModemsModelsAnalyzer.java
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
 * 		ModemsModelsAnalyzer.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Apr 6, 2017
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.modem.ModemModel;
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
public class ModemsModelsAnalyzer {
    private List<ModemModel> modemsModelsList;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/WiringInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(BroadbandPortsAnalyzer.class);

    public ModemsModelsAnalyzer(Connection connection) throws Exception {
        try {
            modemsModelsList = ModemModel.getModemsModelsList(connection);
        } catch (Exception ex) {
            logger.error("Cannot get modems models list from database.", ex);
            throw new Exception("Cannot continue anlyzing modems models.");
        }
    }

    public List<ModemModel> getModemsModelsList() {
        List<ModemModel> modemsList = new ArrayList<>();
        ModemModel unknownModem = new ModemModel();
        unknownModem.setId(0);
        unknownModem.setName(bundle.getString("WiringInternalFrame.modem.unknown"));
        modemsList.add(unknownModem);
        for (ModemModel modemModel : modemsModelsList)
            modemsList.add(modemModel);
        return modemsList;
    }
}
