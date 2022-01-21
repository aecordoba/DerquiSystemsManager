/*
 * 		SitesAnalyzer.java
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
 * 		SiteAnalizer.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Sep 14, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Site;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.managers.SitesManager;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class SitesAnalyzer {
    private List<Site> sitesList;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/CablesPairsInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(SitesAnalyzer.class);

    public SitesAnalyzer() {
        sitesList = SitesManager.getSitesList();
    }

    public List<Site> getSitesList() {
        List<Site> list = new ArrayList<>();
        Site selectSite = new Site();
        selectSite.setId(0);
        selectSite.setCode(bundle.getString("CablesPairsInternalFrame.Select"));
        list.add(selectSite);
        for(Site site : sitesList)
            list.add(site);
        return list;
    }

    private Site getSite(int id) {
        Site site = null;
        for (Site tempSite : sitesList)
            if (tempSite.getId() == id) {
                site = tempSite;
                break;
            }
        return site;
    }

    public void setSitesList(List<Site> sitesList) {
        this.sitesList = sitesList;
    }
}
