/*
 * 		PhonesAnalyzer.java
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
 * 		PhoneAnalyzer.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Nov 11, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Area;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Country;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.OfficeCode;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Phone;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.managers.OfficeCodesDataManager;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class PhonesAnalyzer {

    public List<Phone> getPhonesList() {
        return OfficeCodesDataManager.getOwnNumerationList();
    }

    public List<OfficeCode> getOfficeCodesList() {
        List<OfficeCode> officeCodesList = new ArrayList<>();
        OfficeCode selectOfficeCode = new OfficeCode();
        selectOfficeCode.setId(0);
        officeCodesList.add(selectOfficeCode);
        for (OfficeCode officeCode : OfficeCodesDataManager.getOfficeCodesList())
            officeCodesList.add(officeCode);
        return officeCodesList;
    }

    public List<OfficeCode> getOfficeCodesList(int areaId) {
        List<OfficeCode> list = new ArrayList<>();
        if (areaId == 0)
            list = getOfficeCodesList();
        else {
            for (OfficeCode officeCode : OfficeCodesDataManager.getOfficeCodesList()) {
                if ((officeCode.getArea() != null) && (officeCode.getArea().getId() == areaId))
                    list.add(officeCode);
            }
        }
        return list;
    }

    public OfficeCode getFirstOfficeCode(int areaId) {
        OfficeCode officeCode = null;
        for (OfficeCode temp : OfficeCodesDataManager.getOfficeCodesList()) {
            if ((temp.getArea() != null) && (temp.getArea().getId() == areaId)) {
                officeCode = temp;
                break;
            }
        }
        return officeCode;
    }

    public List<OfficeCode> getOwnOfficeCodesList() {
        List<OfficeCode> ownOfficeCodesList = new ArrayList<>();
        OfficeCode selectOfficeCode = new OfficeCode();
        selectOfficeCode.setId(0);
        ownOfficeCodesList.add(selectOfficeCode);
        for (OfficeCode officeCode : OfficeCodesDataManager.getOwnOfficeCodesList())
            ownOfficeCodesList.add(officeCode);
        return ownOfficeCodesList;
    }

    public List<OfficeCode> getOwnOfficeCodesList(int areaId) {
        List<OfficeCode> ownOfficeCodesList = new ArrayList<>();
        if (areaId == 0)
            ownOfficeCodesList = getOwnOfficeCodesList();
        else {
            for (OfficeCode officeCode : OfficeCodesDataManager.getOwnOfficeCodesList())
                if (officeCode.getArea().getId() == areaId)
                    ownOfficeCodesList.add(officeCode);
        }
        return ownOfficeCodesList;
    }

    public List<Area> getAreasList() {
        List<Area> list = new ArrayList<>();
        Area selectArea = new Area();
        selectArea.setId(0);
        list.add(selectArea);
        for (Area area : OfficeCodesDataManager.getAreasList())
            list.add(area);
        return list;
    }

    public List<Area> getOwnAreasList() {
        List<Area> list = new ArrayList<>();
        Area selectArea = new Area();
        selectArea.setId(0);
        list.add(selectArea);
        for (Area area : OfficeCodesDataManager.getOwnAreasList())
            list.add(area);
        return list;
    }

    public List<Area> getAreasList(int countryId) {
        List<Area> list = new ArrayList<>();
        if (countryId == 0)
            list = getAreasList();
        else {
            for (Area area : OfficeCodesDataManager.getAreasList())
                if (area.getCountry().getId() == countryId)
                    list.add(area);
        }
        return list;
    }

    public List<Area> getOwnAreasList(int countryId) {
        List<Area> list = new ArrayList<>();
        if (countryId == 0)
            list = getAreasList();
        else {
            for (Area area : OfficeCodesDataManager.getOwnAreasList())
                if (area.getCountry().getId() == countryId)
                    list.add(area);
        }
        return list;
    }

    public Area getFirstArea(int countryId) {
        Area area = null;
        for (Area temp : OfficeCodesDataManager.getAreasList()) {
            if ((temp.getCountry() != null) && (temp.getCountry().getId() == countryId)) {
                area = temp;
                break;
            }
        }
        return area;
    }

    public List<Country> getCountriesList() {
        List<Country> list = new ArrayList<>();
        Country selectCountry = new Country();
        selectCountry.setId(0);
        list.add(selectCountry);
        for (Country country : OfficeCodesDataManager.getCountriesList())
            list.add(country);
        return list;
    }

    public List<Country> getOwnCountriesList() {
        List<Country> list = new ArrayList<>();
        Country selectCountry = new Country();
        selectCountry.setId(0);
        list.add(selectCountry);
        for (Country country : OfficeCodesDataManager.getOwnCountriesList())
            list.add(country);
        return list;
    }

    public List<String> getOwnNumbersList(int officeCodeId) {
        List<String> ownNumbersList = new ArrayList<>();
        for (Phone phone : OfficeCodesDataManager.getOwnNumerationList()) {
            if (phone.getOfficeCode().getId() == officeCodeId) {
                ownNumbersList.add(phone.getNumber());
            }
        }
        return ownNumbersList;
    }
}
