/*
 * 		CablesAnalyzer.java
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
 * 		CablesAnalizer.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Aug 24, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetCable;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetFrame;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetPair;
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
public class CablesAnalyzer {
    private List<StreetFrame> streetFramesList;
    private List<StreetCable> streetCablesList;
    private List<StreetPair> streetPairsList;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/CablesPairsInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(CablesAnalyzer.class);

    public CablesAnalyzer(Connection connection) throws Exception {
        try {
            streetPairsList = StreetPair.getStreetPairsList(connection);
            streetCablesList = StreetPair.getStreetCablesList();
            streetFramesList = StreetFrame.getStreetFramesList(connection);

        } catch (Exception ex) {
            logger.error("Cannot get street cables list from database.", ex);
            throw new Exception("Cannot fill street cables list.");
        }
    }

    public List<StreetFrame> getStreetFramesList() {
        return streetFramesList;
    }

    public List<StreetFrame> getStreetFramesList(int siteId) {
        List<StreetFrame> framesList = new ArrayList<>();
        for (StreetFrame frame : streetFramesList) {
            if (frame.getSite().getId() == siteId)
                framesList.add(frame);
        }
        return framesList;
    }

    public void setStreetFramesList(List<StreetFrame> streetFramesList) {
        this.streetFramesList = streetFramesList;
    }

    public List<StreetCable> getStreetCablesList() {
        return streetCablesList;
    }

    public List<StreetCable> getStreetCablesList(int frameId) {
        List<StreetCable> cablesList = new ArrayList<>();
        for (StreetCable streetCable : streetCablesList) {
            if (streetCable.getStreetFrame() != null && streetCable.getStreetFrame().getId() == frameId)
                cablesList.add(streetCable);
        }
        return cablesList;
    }

    public StreetCable getStreetCable(int frameId, char name) {
        StreetCable streetCable = null;
        for (StreetCable temp : streetCablesList)
            if (temp.getName() == name && temp.getStreetFrame().getId() == frameId) {
                streetCable = temp;
                break;
            }
        return streetCable;
    }

    public void setStreetCablesList(List<StreetCable> streetCablesList) {
        this.streetCablesList = streetCablesList;
    }

    public List<StreetPair> getStreetPairsList() {
        return streetPairsList;
    }

    public List<StreetPair> getStreetPairsList(int streetCableId) {
        List<StreetPair> pairsList = new ArrayList<>();
        for (StreetPair streetPair : streetPairsList)
            if (streetPair.getStreetCable().getId() == streetCableId)
                pairsList.add(streetPair);
        return pairsList;
    }

    public List<StreetPair> getFreeStreetPairsList(int streetCableId) {
        List<StreetPair> pairsList = new ArrayList<>();
        for (StreetPair streetPair : getStreetPairsList(streetCableId))
            if (streetPair.isAvailable() && streetPair.getWiringId() == 0)
                pairsList.add(streetPair);
        return pairsList;
    }

    public StreetPair getStreetPair(int id) {
        StreetPair streetPair = null;
        for (StreetPair tempStreetPair : streetPairsList)
            if (tempStreetPair.getId() == id) {
                streetPair = tempStreetPair;
                break;
            }
        return streetPair;
    }

    public void setStreetPairsList(List<StreetPair> streetPairsList) {
        this.streetPairsList = streetPairsList;
    }
}
