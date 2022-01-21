/*
 * 		SwitchBlocksAnalyzer.java
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
 * 		SwitchBlocksAnalyzer.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Sep 17, 2019
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.SwitchBlock;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.managers.SwitchBlocksManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class SwitchBlocksAnalyzer {
    private List<SwitchBlock> switchBlocksList;

    public SwitchBlocksAnalyzer() {
        switchBlocksList = SwitchBlocksManager.getSwitchBlocksList();
    }

    public List<SwitchBlock> getSwitchBlocksList(int siteId) {
        List<SwitchBlock> blocksList = new ArrayList<>();
        for (SwitchBlock temp : switchBlocksList) {
            if (temp.getSite().getId() == siteId)
                blocksList.add(temp);
        }
        return blocksList;
    }
}
