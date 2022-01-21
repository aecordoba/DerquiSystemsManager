/*
 * 		OfficeCodesAnalyzer.java
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
 * 		OfficeCodesAnalyzer.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Oct 17, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Area;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Country;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.OfficeCode;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.managers.OfficeCodesDataManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class OfficeCodesAnalyzer {

    public OfficeCode getOfficeCode(int id) {
        OfficeCode officeCode = null;
        for (OfficeCode temp : OfficeCodesDataManager.getOfficeCodesList()) {
            if (temp.getId() == id) {
                officeCode = temp;
                break;
            }
        }
        return officeCode;
    }

    public OfficeCode getOfficeCode(int code, int areaId) {
        OfficeCode officeCode = null;
        for (OfficeCode temp : OfficeCodesDataManager.getOfficeCodesList()) {
            if (temp.getCode() == code && temp.getArea().getId() == areaId) {
                officeCode = temp;
                break;
            }
        }
        return officeCode;
    }

    /**
     * @return the areasList
     */
    public List<Area> getAreasList() {
        List<Area> list = new ArrayList<>();
        Area selectArea = new Area();
        selectArea.setId(0);
        list.add(selectArea);
        for (Area area : OfficeCodesDataManager.getAreasList())
            list.add(area);
        return list;
    }

    public List<Area> getAreasList(int countryId) {
        List<Area> list = new ArrayList<>();
        if (countryId == 0)
            list = getAreasList();
        else {
            Area selectArea = new Area();
            selectArea.setId(0);
            list.add(selectArea);
            for (Area area : OfficeCodesDataManager.getAreasList()) {
                if (area.getCountry().getId() == countryId)
                    list.add(area);
            }
        }
        return list;
    }

    /**
     * @return the countriesList
     */
    public List<Country> getCountriesList() {
        List<Country> list = new ArrayList<>();
        Country selectCountry = new Country();
        selectCountry.setId(0);
        list.add(selectCountry);
        for (Country country : OfficeCodesDataManager.getCountriesWithAreasList())
            list.add(country);
        return list;
    }
}
