/*
 * 		BroadbandPortsAnalyzer.java
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
 * 		BroadbandPortsAnalyzer.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Mar 31, 2017
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.BroadbandPort;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAM;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMBoard;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.managers.DSLAMsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.router.Router;
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
public class BroadbandPortsAnalyzer {
    private List<BroadbandPort> freeBroadbandPortsList;
    private List<BroadbandPort> broadbandPortsList;
    private List<DSLAM> dslamsList;
    private List<DSLAMBoard> dslamsBoardsList;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/WiringInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(BroadbandPortsAnalyzer.class);

    public BroadbandPortsAnalyzer(Connection connection) throws Exception {
        try {
            freeBroadbandPortsList = BroadbandPort.getFreeBroadbandPorts(connection);
            broadbandPortsList = BroadbandPort.getBroadbandPorts(connection);
            dslamsList = DSLAMsManager.getDslamsList();
            dslamsBoardsList = BroadbandPort.getDSLAMsBoardsList();
        } catch (Exception ex) {
            logger.error("Cannot get broadband ports list from database.", ex);
            throw new Exception("Cannot continue anlyzing broadband ports.");
        }
    }

    public List<BroadbandPort> getFreeBroadbandPortsList() {
        return freeBroadbandPortsList;
    }

    public List<BroadbandPort> getFreeBroadbandPortsListByDSLAM(int dslamId) {
        List<BroadbandPort> freePortsList = new ArrayList<>();
        for (BroadbandPort broadbandPort : freeBroadbandPortsList) {
            if (DSLAMsManager.getDSLAMByBoard(broadbandPort.getDslamBoard().getId()).getId() == dslamId)
                freePortsList.add(broadbandPort);
        }
        return freePortsList;
    }

    public List<BroadbandPort> getFreeBroadbandPortsListByBoard(int dslamBoardId) {
        List<BroadbandPort> freePortsList = new ArrayList<>();
        for (BroadbandPort broadbandPort : freeBroadbandPortsList) {
            if (broadbandPort.getDslamBoard() != null && broadbandPort.getDslamBoard().getId() == dslamBoardId)
                freePortsList.add(broadbandPort);
        }
        return freePortsList;
    }

    public List<BroadbandPort> getBroadbandPortsListByDSLAM(int dslamId) {
        List<BroadbandPort> portsList = new ArrayList<>();
        for (BroadbandPort broadbandPort : broadbandPortsList) {
            if (DSLAMsManager.getDSLAMByBoard(broadbandPort.getDslamBoard().getId()).getId() == dslamId)
                portsList.add(broadbandPort);
        }
        return portsList;
    }

    public List<BroadbandPort> getBroadbandPortsListByBoard(int dslamBoardId) {
        List<BroadbandPort> portsList = new ArrayList<>();
        for (BroadbandPort broadbandPort : broadbandPortsList) {
            if (broadbandPort.getDslamBoard() != null && broadbandPort.getDslamBoard().getId() == dslamBoardId)
                portsList.add(broadbandPort);
        }
        return portsList;
    }

//    public List<DSLAM> getDslamsList() {
//        List<DSLAM> list = new ArrayList<>();
//        if (dslamsList.size() > 0) {
//            DSLAM selectDSLAM = new DSLAM();
//            selectDSLAM.setName(bundle.getString("WiringInternalFrame.Select"));
//            selectDSLAM.setId(0);
//            list.add(selectDSLAM);
//            for (DSLAM dslam : dslamsList)
//                list.add(dslam);
//        }
//        return list;
//    }
    public List<DSLAM> getDSLAMsList(int siteId) {
        List<DSLAM> dslamsBySiteIdList = new ArrayList<>();
        for (DSLAM dslam : dslamsList) {
            if (dslam.getSite().getId() == siteId)
                dslamsBySiteIdList.add(dslam);
        }
        if (dslamsBySiteIdList.size() > 0) {
            DSLAM selectDSLAM = new DSLAM();
            selectDSLAM.setName(bundle.getString("WiringInternalFrame.Select"));
            selectDSLAM.setId(0);
            dslamsBySiteIdList.add(0, selectDSLAM);
        }
        return dslamsBySiteIdList;
    }

    public List<DSLAMBoard> getDSLAMsBoardsList() {
        return dslamsBoardsList;
    }

    public List<DSLAMBoard> getDSLAMBoardsList(int dslamId) {
        List<DSLAMBoard> dslamBoardsListByDslam = new ArrayList<>();
        for (DSLAMBoard board : DSLAMsManager.getDSLAM(dslamId).getBoardsMap().values()) {
            if (board != null)
                dslamBoardsListByDslam.add(board);
        }
        return dslamBoardsListByDslam;
    }

//    public DSLAM getDSLAM(int boardId) {
//        DSLAM dslam = null;
//        for (DSLAMBoard board : dslamsBoardsList) {
//            if (board.getId() == boardId) {
//                dslam = DSLAMsManager.getDSLAMByBoard(board.getId());
//                break;
//            }
//        }
//        return dslam;
//    }

    public Router getRouter(int dslamId) {
        Router router = null;
        for (DSLAM dslam : dslamsList) {
            if (dslam.getId() == dslamId) {
                router = dslam.getRouter();
                break;
            }
        }
        return router;
    }
}
