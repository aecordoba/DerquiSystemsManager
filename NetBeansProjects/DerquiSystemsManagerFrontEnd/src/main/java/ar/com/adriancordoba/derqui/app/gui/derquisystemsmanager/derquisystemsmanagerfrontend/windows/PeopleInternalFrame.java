/*
 * 		PeopleInternalFrame.java
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
 * 		peopleInternalFrame.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Jul 9, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.IdentificationType;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.Person;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.Address;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.City;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.Location;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.State;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.Street;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.managers.LocationsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.managers.StreetsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.managers.IdentificationsTypesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Area;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Country;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.OfficeCode;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Phone;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.User;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.OfficeCodesAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.StringFormat;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.SearchPeople;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.AreaCodeComboBoxRenderer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.CountryCodeComboBoxRenderer;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class PeopleInternalFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form peopleInternalFrame
     *
     * @param user
     */
    public PeopleInternalFrame(User user) {
        this.user = user;
        if (user.hasRole("people-operator"))
            privilegedUser = true;
        createComponentsModels();
        createComponentsRenderes();

        initComponents();

        setNewFormStructure();

        Connection connection = null;
        try {
            databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
            connection = databaseConnectionsManager.takeConnection();

            officeCodesAnalyzer = new OfficeCodesAnalyzer();

            fillComboBoxesModels();
            fillPeopleListModel(connection);

            open = true;
        } catch (Exception ex) {
            logger.error("Cannot fill combo boxes or list.", ex);
            JOptionPane.showMessageDialog(this, bundle.getString("PeopleInternalFrame.comboBoxes.problem"), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            databaseConnectionsManager.returnConnection(connection);
        }
    }

    private void createComponentsModels() {
        peopleListModel = new DefaultListModel<>();
        identificationTypeComboBoxModel = new DefaultComboBoxModel<>();
        streetComboBoxModel = new DefaultComboBoxModel<>();
        street1ComboBoxModel = new DefaultComboBoxModel<>();
        street2ComboBoxModel = new DefaultComboBoxModel<>();
        locationComboBoxModel = new DefaultComboBoxModel<>();
        cityComboBoxModel = new DefaultComboBoxModel<>();
        stateComboBoxModel = new DefaultComboBoxModel<>();
        countryComboBoxModel = new DefaultComboBoxModel<>();
        cellPhoneAreaCodeComboBoxModel = new DefaultComboBoxModel<>();
        cellPhoneCountryCodeComboBoxModel = new DefaultComboBoxModel<>();
        phoneAreaCodeComboBoxModel = new DefaultComboBoxModel<>();
        phoneCountryCodeComboBoxModel = new DefaultComboBoxModel<>();
    }

    private void createComponentsRenderes() {
        cellPhoneCountryCodeComboBoxRenderer = new CountryCodeComboBoxRenderer();
        cellPhoneAreaCodeComboBoxRenderer = new AreaCodeComboBoxRenderer();
        phoneCountryCodeComboBoxRenderer = new CountryCodeComboBoxRenderer();
        phoneAreaCodeComboBoxRenderer = new AreaCodeComboBoxRenderer();
    }

    private void fillComboBoxesModels() {
        fillIdentificationTypeComboBoxModel();
        fillStreetComboBoxModels();
        fillCountryCodeComboBoxesModels();
        fillAreaCodeComboBoxesModel();
        fillLocationsComboBoxesModels();
    }

    private void fillIdentificationTypeComboBoxModel() {
        List<IdentificationType> identificationsTypesList = new ArrayList<>();
        IdentificationType selectIdentificationType = new IdentificationType();
        selectIdentificationType.setId(0);
        selectIdentificationType.setName(bundle.getString("PeopleInternalFrame.Select"));
        identificationsTypesList.add(selectIdentificationType);
        for (IdentificationType identificationType : IdentificationsTypesManager.getIdentificationsTypesList())
            identificationsTypesList.add(identificationType);
        fillComboBoxModel(identificationsTypesList, identificationTypeComboBoxModel);
    }

    private void fillStreetComboBoxModels() {
        List<Street> streetsList = new ArrayList<>();
        Street selectStreet = new Street();
        selectStreet.setId(0);
        selectStreet.setName(bundle.getString("PeopleInternalFrame.Select"));
        streetsList.add(selectStreet);
        for (Street street : StreetsManager.getStreetsList())
            streetsList.add(street);
        fillComboBoxModel(streetsList, streetComboBoxModel);
        fillComboBoxModel(streetsList, street1ComboBoxModel);
        fillComboBoxModel(streetsList, street2ComboBoxModel);
    }

    private void fillCountryCodeComboBoxesModels() {
        List<Country> countriesList = officeCodesAnalyzer.getCountriesList();
        for (Country country : countriesList) {
            cellPhoneCountryCodeComboBoxModel.addElement(country);
            phoneCountryCodeComboBoxModel.addElement(country);
        }
    }

    private void fillAreaCodeComboBoxesModel() {
        cellPhoneAreaCodeComboBoxModel.removeAllElements();
        phoneAreaCodeComboBoxModel.removeAllElements();
        List<Area> areasList = officeCodesAnalyzer.getAreasList();
        for (Area area : areasList) {
            cellPhoneAreaCodeComboBoxModel.addElement(area);
            phoneAreaCodeComboBoxModel.addElement(area);
        }
    }

    private void fillLocationsComboBoxesModels() {
        fillLocationComboBoxModel();
        fillCityComboBoxModel();
        fillStateComboBoxModel();
        fillCountryComboBoxModel();
    }

    private void fillLocationComboBoxModel() {
        List<Location> locationsList = new ArrayList<>();
        Location selectLocation = new Location();
        selectLocation.setId(0);
        selectLocation.setName(bundle.getString("PeopleInternalFrame.Select"));
        locationsList.add(selectLocation);
        for (Location location : LocationsManager.getLocationsList())
            locationsList.add(location);
        fillComboBoxModel(locationsList, locationComboBoxModel);
    }

    private void fillCityComboBoxModel() {
        List<City> citiesList = new ArrayList<>();
        City selectCity = new City();
        selectCity.setId(0);
        selectCity.setName(bundle.getString("PeopleInternalFrame.Select"));
        citiesList.add(selectCity);
        for (City city : LocationsManager.getCitiesList())
            citiesList.add(city);
        fillComboBoxModel(citiesList, cityComboBoxModel);
    }

    private void fillStateComboBoxModel() {
        List<State> statesList = new ArrayList<>();
        State selectState = new State();
        selectState.setId(0);
        selectState.setName(bundle.getString("PeopleInternalFrame.Select"));
        statesList.add(selectState);
        for (State state : LocationsManager.getStatesList())
            statesList.add(state);
        fillComboBoxModel(statesList, stateComboBoxModel);
    }

    private void fillCountryComboBoxModel() {
        List<Country> countriesList = new ArrayList<>();
        Country selectCountry = new Country();
        selectCountry.setId(0);
        selectCountry.setName(bundle.getString("PeopleInternalFrame.Select"));
        countriesList.add(selectCountry);
        for (Country country : LocationsManager.getCountriesList())
            countriesList.add(country);
        fillComboBoxModel(countriesList, countryComboBoxModel);
    }

    /**
     *
     * @param <T>
     * @param list
     * @param model
     */
    private <T> void fillComboBoxModel(List<T> list, DefaultComboBoxModel<T> model) {
        model.removeAllElements();
        for (T element : list)
            model.addElement(element);
    }

    private void fillPeopleListModel(Connection connection) throws Exception {
        peopleListModel.removeAllElements();
        List<Person> peopleList = Person.getPeopleList(connection);
        for (Person person : peopleList)
            peopleListModel.addElement(person);
    }

    private void clearForm() {
        firstNameTextField.setText("");
        middleNameTextField.setText("");
        lastNameTextField.setText("");
        identificationTypeComboBox.setSelectedIndex(0);
        identificationNumberTextField.setText("");
        cellPhoneCountryCodeComboBox.setSelectedIndex(0);
        cellPhoneAreaCodeComboBox.setSelectedIndex(0);
        cellPhoneNumberTextField.setText("");
        streetComboBox.setSelectedIndex(0);
        addressNumberTextField.setText("");
        floorTextField.setText("");
        apartmentTextField.setText("");
        zipCodeTextField.setText("");
        street1ComboBox.setSelectedIndex(0);
        street2ComboBox.setSelectedIndex(0);
        locationComboBox.setSelectedIndex(0);
        cityComboBox.setSelectedIndex(0);
        stateComboBox.setSelectedIndex(0);
        countryComboBox.setSelectedIndex(0);
        phoneCountryCodeComboBox.setSelectedIndex(0);
        phoneAreaCodeComboBox.setSelectedIndex(0);
        phoneNumberTextField.setText("");
    }

    private void setNewFormStructure() {
        peopleList.clearSelection();
        createButton.setText(bundle.getString("PeopleInternalFrame.createButton.text"));
        if (privilegedUser)
            createButton.setEnabled(true);
        newButton.setEnabled(false);
        searchButton.setEnabled(true);
    }

    private void setModifyFormStructure() {
        createButton.setText(bundle.getString("PeopleInternalFrame.modifyButton.text"));
        newButton.setEnabled(true);
        searchButton.setEnabled(false);
    }

    private boolean isValidForm() {
        boolean valid = true;

        firstName = firstNameTextField.getText().trim();
        if (firstName.isEmpty() || !StringFormat.isNoun(firstName)) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.firstName.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            firstNameTextField.requestFocus();
            valid = false;
            return valid;
        }

        middleName = middleNameTextField.getText().trim();
        if (!middleName.isEmpty() && !StringFormat.isNoun(middleName)) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.middleName.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            middleNameTextField.requestFocus();
            valid = false;
            return valid;
        }

        lastName = lastNameTextField.getText().trim();
        if (lastName.isEmpty() || !StringFormat.isNoun(lastName)) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.lastName.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            lastNameTextField.requestFocus();
            valid = false;
            return valid;
        }

        identificationNumber = identificationNumberTextField.getText().trim();
        if (!identificationNumber.isEmpty()) {
            identificationTypeId = ((IdentificationType) identificationTypeComboBoxModel.getSelectedItem()).getId();
            identificationTypeName = identificationTypeComboBox.getSelectedItem().toString();
            if (!StringFormat.isInteger(identificationNumber)) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.identificationNumber.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                identificationNumberTextField.requestFocus();
                valid = false;
                return valid;
            }
            if (identificationTypeId == 0) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.identificationType.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                identificationTypeComboBox.requestFocus();
                valid = false;
                return valid;
            }
        } else
            identificationNumber = null;

        String tempCellPhone = cellPhoneNumberTextField.getText().trim();
        if (!tempCellPhone.isEmpty()) {
            if (StringFormat.isPhoneNumber(tempCellPhone)) {
                cellPhoneNumber = StringFormat.getNumber(tempCellPhone);
                if (cellPhoneCountryCodeComboBox.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.cellPhoneCountryCode.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    cellPhoneCountryCodeComboBox.requestFocus();
                    valid = false;
                    return valid;
                }
                if (cellPhoneAreaCodeComboBox.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.cellPhoneAreaCode.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    cellPhoneAreaCodeComboBox.requestFocus();
                    valid = false;
                    return valid;
                } else
                    cellPhoneArea = (Area) cellPhoneAreaCodeComboBox.getSelectedItem();

                if ((cellPhoneNumber.length() + (String.valueOf(cellPhoneArea.getCode())).length()) != 10) {
                    JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.cellPhoneNumber.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    cellPhoneNumberTextField.requestFocus();
                    valid = false;
                    return valid;
                }
            } else {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.cellPhoneNumber.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                cellPhoneNumberTextField.requestFocus();
                valid = false;
                return valid;
            }
        } else
            cellPhoneNumber = null;

        // Address validation
        street = (streetComboBox.getSelectedIndex() > 0) ? (Street) streetComboBox.getSelectedItem() : null;
        addressNumber = addressNumberTextField.getText().trim();
        floor = floorTextField.getText().trim();
        apartment = apartmentTextField.getText().trim();
        zipCode = zipCodeTextField.getText().trim();
        street1 = (street1ComboBox.getSelectedIndex() > 0) ? (Street) street1ComboBox.getSelectedItem() : null;
        street2 = (street2ComboBox.getSelectedIndex() > 0) ? (Street) street2ComboBox.getSelectedItem() : null;
        locationId = ((Location) locationComboBox.getSelectedItem()).getId();

        if ((!addressNumber.isEmpty() || !floor.isEmpty() || !apartment.isEmpty() || !zipCode.isEmpty() || (street1 != null) || (street2 != null) || (locationId != 0)) && (street == null)) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.address.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            streetComboBox.requestFocus();
            valid = false;
            return valid;
        }

        if (street != null) {
            if (addressNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.addressNumber.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                addressNumberTextField.requestFocus();
                valid = false;
                return valid;
            } else {
                try {
                    Integer.parseInt(addressNumber);
                } catch (NumberFormatException exception) {
                    if (!addressNumber.equals(bundle.getString("PeopleInternalFrame.Address.NoNumber"))) {
                        JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.addressNumber.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                        addressNumberTextField.requestFocus();
                        valid = false;
                        return valid;
                    } else
                        addressNumber = "0";
                }
            }

            if (street1 != null && street1.equals(street)) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.address.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                street1ComboBox.requestFocus();
                valid = false;
                return valid;
            }

            if (street2 != null && (street2.equals(street1) || street2.equals(street))) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.address.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                street2ComboBox.requestFocus();
                valid = false;
                return valid;
            }

            if (locationId == 0) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.location.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                locationComboBox.requestFocus();
                valid = false;
                return valid;
            }
        }

        String tempPhone = phoneNumberTextField.getText().trim();
        if (!tempPhone.isEmpty()) {
            if (StringFormat.isPhoneNumber(tempPhone)) {
                phoneNumber = StringFormat.getNumber(tempPhone);
                if (phoneCountryCodeComboBox.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.phoneCountryCode.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    phoneCountryCodeComboBox.requestFocus();
                    valid = false;
                    return valid;
                }
                if (phoneAreaCodeComboBox.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.phoneAreaCode.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    phoneAreaCodeComboBox.requestFocus();
                    valid = false;
                    return valid;
                } else
                    phoneArea = (Area) phoneAreaCodeComboBox.getSelectedItem();
                if ((phoneNumber.length() + (String.valueOf(phoneArea.getCode())).length()) != 10) {
                    JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.phoneNumber.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    phoneNumberTextField.requestFocus();
                    valid = false;
                    return valid;
                }
            } else {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.phoneNumber.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                phoneNumberTextField.requestFocus();
                valid = false;
                return valid;
            }
        } else
            phoneNumber = null;

        return valid;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        peopleListScrollPane = new javax.swing.JScrollPane();
        peopleList = new javax.swing.JList<>();
        personPanel = new javax.swing.JPanel();
        firstNameTextField = new javax.swing.JTextField();
        firstNameLabel = new javax.swing.JLabel();
        middleNameLabel = new javax.swing.JLabel();
        middleNameTextField = new javax.swing.JTextField();
        lastNameLabel = new javax.swing.JLabel();
        lastNameTextField = new javax.swing.JTextField();
        identificationPanel = new javax.swing.JPanel();
        identificationTypeComboBox = new javax.swing.JComboBox<>();
        identificationNumberLabel = new javax.swing.JLabel();
        identificationNumberTextField = new javax.swing.JTextField();
        cellPhonePanel = new javax.swing.JPanel();
        cellPhoneCountryCodeComboBox = new javax.swing.JComboBox<>();
        cellPhoneAreaCodeComboBox = new javax.swing.JComboBox<>();
        plusLabel = new javax.swing.JLabel();
        areaCodeOpenLabel = new javax.swing.JLabel();
        areaCodeCloseLabel = new javax.swing.JLabel();
        cellPhoneNumberTextField = new javax.swing.JTextField();
        addressPanel = new javax.swing.JPanel();
        addressLabel = new javax.swing.JLabel();
        addressNumberTextField = new javax.swing.JTextField();
        betweenLabel = new javax.swing.JLabel();
        andLabel = new javax.swing.JLabel();
        zipCodeLabel = new javax.swing.JLabel();
        zipCodeTextField = new javax.swing.JTextField();
        locationLabel = new javax.swing.JLabel();
        locationComboBox = new javax.swing.JComboBox<>();
        stateLabel = new javax.swing.JLabel();
        stateComboBox = new javax.swing.JComboBox<>();
        countryLabel = new javax.swing.JLabel();
        countryComboBox = new javax.swing.JComboBox<>();
        floorLabel = new javax.swing.JLabel();
        floorTextField = new javax.swing.JTextField();
        apartmentLabel = new javax.swing.JLabel();
        apartmentTextField = new javax.swing.JTextField();
        cityLabel = new javax.swing.JLabel();
        cityComboBox = new javax.swing.JComboBox<>();
        streetComboBox = new javax.swing.JComboBox<>();
        street1ComboBox = new javax.swing.JComboBox<>();
        street2ComboBox = new javax.swing.JComboBox<>();
        phonePanel = new javax.swing.JPanel();
        phoneCountryCodeComboBox = new javax.swing.JComboBox<>();
        phoneAreaCodeComboBox = new javax.swing.JComboBox<>();
        plusLabel1 = new javax.swing.JLabel();
        areaCodeOpenLabel1 = new javax.swing.JLabel();
        areaCodeCloseLabel1 = new javax.swing.JLabel();
        phoneNumberTextField = new javax.swing.JTextField();
        buttonsPanel = new javax.swing.JPanel();
        searchButton = new javax.swing.JButton();
        newButton = new javax.swing.JButton();
        createButton = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("i18n/PeopleInternalFrameBundle"); // NOI18N
        setTitle(bundle.getString("PeopleInternalFrame.title")); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        peopleList.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        peopleList.setModel(peopleListModel);
        peopleList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                peopleListMouseClicked(evt);
            }
        });
        peopleListScrollPane.setViewportView(peopleList);

        firstNameLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        firstNameLabel.setText(bundle.getString("PeopleInternalFrame.firstNameLabel.text")); // NOI18N

        middleNameLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        middleNameLabel.setText(bundle.getString("PeopleInternalFrame.middleNameLabel.text")); // NOI18N

        lastNameLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lastNameLabel.setText(bundle.getString("PeopleInternalFrame.lastNameLabel.text")); // NOI18N

        identificationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("PeopleInternalFrame.PersonPanel.IdentificationPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        identificationTypeComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        identificationTypeComboBox.setModel(identificationTypeComboBoxModel);

        identificationNumberLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        identificationNumberLabel.setText(bundle.getString("PeopleInternalFrame.identificationNumberLabel.text")); // NOI18N

        javax.swing.GroupLayout identificationPanelLayout = new javax.swing.GroupLayout(identificationPanel);
        identificationPanel.setLayout(identificationPanelLayout);
        identificationPanelLayout.setHorizontalGroup(
            identificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(identificationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(identificationTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(identificationNumberLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(identificationNumberTextField)
                .addGap(18, 18, 18))
        );
        identificationPanelLayout.setVerticalGroup(
            identificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(identificationPanelLayout.createSequentialGroup()
                .addGroup(identificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(identificationTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(identificationNumberLabel)
                    .addComponent(identificationNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        cellPhonePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("PeopleInternalFrame.PersonPanel.CellPhonePanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        cellPhoneCountryCodeComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cellPhoneCountryCodeComboBox.setModel(cellPhoneCountryCodeComboBoxModel);
        cellPhoneCountryCodeComboBox.setRenderer(cellPhoneCountryCodeComboBoxRenderer);
        cellPhoneCountryCodeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cellPhoneCountryCodeComboBoxActionPerformed(evt);
            }
        });

        cellPhoneAreaCodeComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cellPhoneAreaCodeComboBox.setModel(cellPhoneAreaCodeComboBoxModel);
        cellPhoneAreaCodeComboBox.setRenderer(cellPhoneAreaCodeComboBoxRenderer);

        plusLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        plusLabel.setText(bundle.getString("PeopleInternalFrame.plusLabel.text")); // NOI18N

        areaCodeOpenLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        areaCodeOpenLabel.setText(bundle.getString("PeopleInternalFrame.areaCodeOpenLabel.text")); // NOI18N

        areaCodeCloseLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        areaCodeCloseLabel.setText(bundle.getString("PeopleInternalFrame.areaCodeCloseLabel.text")); // NOI18N

        javax.swing.GroupLayout cellPhonePanelLayout = new javax.swing.GroupLayout(cellPhonePanel);
        cellPhonePanel.setLayout(cellPhonePanelLayout);
        cellPhonePanelLayout.setHorizontalGroup(
            cellPhonePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cellPhonePanelLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(plusLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cellPhoneCountryCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(areaCodeOpenLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cellPhoneAreaCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(areaCodeCloseLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cellPhoneNumberTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );
        cellPhonePanelLayout.setVerticalGroup(
            cellPhonePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cellPhonePanelLayout.createSequentialGroup()
                .addGroup(cellPhonePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(cellPhonePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cellPhoneCountryCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(plusLabel))
                    .addGroup(cellPhonePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cellPhoneNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(areaCodeCloseLabel)
                        .addComponent(cellPhoneAreaCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(areaCodeOpenLabel)))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        addressPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("PeopleInternalFrame.PersonPanel.AddressPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        addressLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        addressLabel.setText(bundle.getString("PeopleInternalFrame.addressLabel.text")); // NOI18N

        betweenLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        betweenLabel.setText(bundle.getString("PeopleInternalFrame.betweenLabel.text")); // NOI18N

        andLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        andLabel.setText(bundle.getString("PeopleInternalFrame.andLabel.text")); // NOI18N

        zipCodeLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        zipCodeLabel.setText(bundle.getString("PeopleInternalFrame.zipCodeLabel.text")); // NOI18N

        locationLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        locationLabel.setText(bundle.getString("PeopleInternalFrame.locationLabel.text")); // NOI18N

        locationComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        locationComboBox.setModel(locationComboBoxModel);
        locationComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationComboBoxActionPerformed(evt);
            }
        });

        stateLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        stateLabel.setText(bundle.getString("PeopleInternalFrame.stateLabel.text")); // NOI18N

        stateComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        stateComboBox.setModel(stateComboBoxModel);

        countryLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        countryLabel.setText(bundle.getString("PeopleInternalFrame.countryLabel.text")); // NOI18N

        countryComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        countryComboBox.setModel(countryComboBoxModel);

        floorLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        floorLabel.setText(bundle.getString("PeopleInternalFrame.floorLabel.text")); // NOI18N

        apartmentLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        apartmentLabel.setText(bundle.getString("PeopleInternalFrame.apartmentLabel.text")); // NOI18N

        cityLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cityLabel.setText(bundle.getString("PeopleInternalFrame.cityLabel.text")); // NOI18N

        cityComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cityComboBox.setModel(cityComboBoxModel);

        streetComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        streetComboBox.setModel(streetComboBoxModel);

        street1ComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        street1ComboBox.setModel(street1ComboBoxModel);

        street2ComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        street2ComboBox.setModel(street2ComboBoxModel);

        javax.swing.GroupLayout addressPanelLayout = new javax.swing.GroupLayout(addressPanel);
        addressPanel.setLayout(addressPanelLayout);
        addressPanelLayout.setHorizontalGroup(
            addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addressPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addressPanelLayout.createSequentialGroup()
                        .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(betweenLabel)
                            .addComponent(locationLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(street1ComboBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(addressPanelLayout.createSequentialGroup()
                                .addComponent(locationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cityLabel)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(countryComboBox, 0, 221, Short.MAX_VALUE)
                            .addGroup(addressPanelLayout.createSequentialGroup()
                                .addComponent(cityComboBox, 0, 213, Short.MAX_VALUE)
                                .addGap(8, 8, 8))
                            .addGroup(addressPanelLayout.createSequentialGroup()
                                .addComponent(andLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(street2ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(addressPanelLayout.createSequentialGroup()
                        .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addressPanelLayout.createSequentialGroup()
                                .addComponent(stateLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(countryLabel))
                            .addGroup(addressPanelLayout.createSequentialGroup()
                                .addComponent(addressLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(streetComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(addressNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(addressPanelLayout.createSequentialGroup()
                        .addComponent(floorLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(floorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(apartmentLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(apartmentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(zipCodeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(zipCodeTextField)
                        .addGap(6, 6, 6)))
                .addContainerGap())
        );
        addressPanelLayout.setVerticalGroup(
            addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addressPanelLayout.createSequentialGroup()
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressLabel)
                    .addComponent(addressNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(streetComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(floorLabel)
                    .addComponent(floorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(apartmentLabel)
                    .addComponent(apartmentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zipCodeLabel)
                    .addComponent(zipCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(betweenLabel)
                        .addComponent(street1ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(street2ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(andLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(locationLabel)
                    .addComponent(locationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cityLabel)
                    .addComponent(cityComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(stateLabel)
                    .addComponent(stateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(countryLabel)
                    .addComponent(countryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        phonePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("PeopleInternalFrame.phonePanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        phoneCountryCodeComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        phoneCountryCodeComboBox.setModel(phoneCountryCodeComboBoxModel);
        phoneCountryCodeComboBox.setRenderer(phoneCountryCodeComboBoxRenderer);
        phoneCountryCodeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneCountryCodeComboBoxActionPerformed(evt);
            }
        });

        phoneAreaCodeComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        phoneAreaCodeComboBox.setModel(phoneAreaCodeComboBoxModel);
        phoneAreaCodeComboBox.setRenderer(phoneAreaCodeComboBoxRenderer);

        plusLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        plusLabel1.setText(bundle.getString("PeopleInternalFrame.plusLabel1.text")); // NOI18N

        areaCodeOpenLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        areaCodeOpenLabel1.setText(bundle.getString("PeopleInternalFrame.areaCodeOpenLabel1.text")); // NOI18N

        areaCodeCloseLabel1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        areaCodeCloseLabel1.setText(bundle.getString("PeopleInternalFrame.areaCodeCloseLabel1.text")); // NOI18N

        javax.swing.GroupLayout phonePanelLayout = new javax.swing.GroupLayout(phonePanel);
        phonePanel.setLayout(phonePanelLayout);
        phonePanelLayout.setHorizontalGroup(
            phonePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(phonePanelLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(plusLabel1)
                .addGap(6, 6, 6)
                .addComponent(phoneCountryCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(areaCodeOpenLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(phoneAreaCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(areaCodeCloseLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(phoneNumberTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addContainerGap())
        );
        phonePanelLayout.setVerticalGroup(
            phonePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(phonePanelLayout.createSequentialGroup()
                .addGroup(phonePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(plusLabel1)
                    .addComponent(phoneCountryCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(areaCodeOpenLabel1)
                    .addComponent(phoneAreaCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(areaCodeCloseLabel1)
                    .addComponent(phoneNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        searchButton.setText(bundle.getString("PeopleInternalFrame.searchButton.text")); // NOI18N
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        newButton.setText(bundle.getString("PeopleInternalFrame.newButton.text")); // NOI18N
        newButton.setEnabled(false);
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        createButton.setText(bundle.getString("PeopleInternalFrame.createButton.text")); // NOI18N
        createButton.setEnabled(false);
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(searchButton)
                .addGap(18, 18, 18)
                .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(createButton)
                .addContainerGap())
        );

        buttonsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {createButton, newButton, searchButton});

        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchButton)
                    .addComponent(newButton)
                    .addComponent(createButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout personPanelLayout = new javax.swing.GroupLayout(personPanel);
        personPanel.setLayout(personPanelLayout);
        personPanelLayout.setHorizontalGroup(
            personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(personPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(phonePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(identificationPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, personPanelLayout.createSequentialGroup()
                        .addGroup(personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(firstNameLabel)
                            .addComponent(lastNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(personPanelLayout.createSequentialGroup()
                                .addComponent(lastNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(personPanelLayout.createSequentialGroup()
                                .addComponent(firstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(middleNameLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(middleNameTextField))))
                    .addComponent(cellPhonePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addressPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        personPanelLayout.setVerticalGroup(
            personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(personPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstNameLabel)
                    .addComponent(firstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(middleNameLabel)
                    .addComponent(middleNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(personPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lastNameLabel)
                    .addComponent(lastNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(identificationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cellPhonePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addressPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(phonePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(peopleListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(personPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(peopleListScrollPane)
                    .addComponent(personPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        open = false;
    }//GEN-LAST:event_formInternalFrameClosed

    private void peopleListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_peopleListMouseClicked
        clearForm();
        Person selectedPerson = peopleList.getSelectedValue();
        firstNameTextField.setText(selectedPerson.getFirstName());
        middleNameTextField.setText(selectedPerson.getMiddleName());
        lastNameTextField.setText(selectedPerson.getLastName());

        if (selectedPerson.getIdentificationType() != null)
            identificationTypeComboBoxModel.setSelectedItem(selectedPerson.getIdentificationType());
        int identificationNumber = selectedPerson.getIdentificationNumber();
        if (identificationNumber != 0) {
            identificationNumberTextField.setText(String.valueOf(identificationNumber));
        }

        Phone cellPhone = selectedPerson.getCellPhone();
        if (cellPhone != null && cellPhone.getId() != 0) {
            OfficeCode cellPhoneOfficeCode = officeCodesAnalyzer.getOfficeCode(cellPhone.getOfficeCode().getId());
            Area area = cellPhoneOfficeCode.getArea();
            cellPhoneCountryCodeComboBoxModel.setSelectedItem(area.getCountry());
            cellPhoneAreaCodeComboBoxModel.setSelectedItem(area);
            cellPhoneNumberTextField.setText(cellPhoneOfficeCode.getCode() + "-" + cellPhone.getNumber());
        }

        Address address = selectedPerson.getAddress();
        if (address != null && address.getId() != 0) {
            Street street = address.getStreet();
            if (street != null)
                streetComboBox.setSelectedItem(address.getStreet());

            String number = address.getNumber();
            if ((number != null) && (number.equals("0")))
                number = bundle.getString("PeopleInternalFrame.Address.NoNumber");
            addressNumberTextField.setText(number);

            floorTextField.setText(address.getFloor());
            apartmentTextField.setText(address.getApartment());
            zipCodeTextField.setText(address.getZipCode());

            Street street1 = address.getStreet1();
            if (street1 != null)
                street1ComboBox.setSelectedItem(street1);

            Street street2 = address.getStreet2();
            if (street2 != null)
                street2ComboBox.setSelectedItem(street2);

            int locationId = address.getLocationId();
            if (locationId != 0) {
                Location location = LocationsManager.getLocation(locationId);
                City city = location.getCity();
                State state = city.getState();
                Country country = state.getCountry();
                locationComboBoxModel.setSelectedItem(location);
                cityComboBoxModel.setSelectedItem(city);
                stateComboBoxModel.setSelectedItem(state);
                countryComboBoxModel.setSelectedItem(country);
            }

            Phone phone = address.getPhone();
            if (phone != null && phone.getId() != 0) {
                OfficeCode phoneOfficeCode = officeCodesAnalyzer.getOfficeCode(phone.getOfficeCode().getId());
                Area area = phoneOfficeCode.getArea();
                phoneCountryCodeComboBoxModel.setSelectedItem(area.getCountry());
                phoneAreaCodeComboBoxModel.setSelectedItem(area);
                phoneNumberTextField.setText(phoneOfficeCode.getCode() + "-" + phone.getNumber());
            }
        }
        setModifyFormStructure();
    }//GEN-LAST:event_peopleListMouseClicked

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        DatabaseConnectionsManager databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
        Connection connection = null;
        try {
            SearchPeople searchPeople = new SearchPeople(this, identificationTypeComboBoxModel);
            connection = databaseConnectionsManager.takeConnection();

            List<Person> peopleFoundList = searchPeople.search(connection);
            if (peopleFoundList != null) {
                peopleListModel.clear();
                for (Person person : peopleFoundList) {
                    peopleListModel.addElement(person);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            logger.error("Cannot use Search People dialog.", ex);
        } finally {
            databaseConnectionsManager.returnConnection(connection);

        }
        clearForm();
        setNewFormStructure();
    }//GEN-LAST:event_searchButtonActionPerformed

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        clearForm();
        setNewFormStructure();
    }//GEN-LAST:event_newButtonActionPerformed

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        boolean valid = isValidForm();
        if (valid) {
            Connection connection = databaseConnectionsManager.takeConnection();
            if (peopleList.getSelectedIndex() == -1) {
                Person person = new Person();
                fillPersonData(person);
                try {
                    person.insert(connection);
                    JOptionPane.showMessageDialog(this, person + " " + bundle.getString("InsertDialog.confirmation"), bundle.getString("InsertDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    renewFormData(connection);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this, person + " " + bundle.getString("InsertDialog.error"), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                }
            } else {
                Person selectedPerson = peopleList.getSelectedValue();
                fillPersonData(selectedPerson);
                try {
                    selectedPerson.update(connection);
                    JOptionPane.showMessageDialog(this, selectedPerson + " " + bundle.getString("ModifyDialog.confirmation"), bundle.getString("ModifyDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    renewFormData(connection);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this, selectedPerson + " " + bundle.getString("ModifyDialog.error"), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                }
            }
        }
    }//GEN-LAST:event_createButtonActionPerformed

    private void renewFormData(Connection connection) throws Exception {
        clearForm();
        try {
            officeCodesAnalyzer = new OfficeCodesAnalyzer();
            fillPeopleListModel(connection);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("Cannot renew form data.");
        }
        setNewFormStructure();
    }

    private void fillPersonData(Person person) {
        person.setFirstName(StringFormat.capitalizeNames(firstName));
        person.setMiddleName(StringFormat.capitalizeNames(middleName));
        person.setLastName(StringFormat.capitalizeNames(lastName));

        if (identificationNumber != null) {
            IdentificationType identificationType;
            if (person.getIdentificationType() == null) {
                identificationType = new IdentificationType();
                person.setIdentificationType(identificationType);
            } else
                identificationType = person.getIdentificationType();
            identificationType.setId(identificationTypeId);
            identificationType.setName(identificationTypeName);
            person.setIdentificationNumber(Integer.parseInt(identificationNumber));
        } else {
            person.setIdentificationType(null);
            person.setIdentificationNumber(0);
        }

        Phone cellPhone;
        if (cellPhoneNumber != null) {
            if (person.getCellPhone() == null) {
                cellPhone = new Phone();
                person.setCellPhone(cellPhone);
            } else
                cellPhone = person.getCellPhone();
            int code = Integer.parseInt(cellPhoneNumber.substring(0, cellPhoneNumber.length() - 4));
            OfficeCode officeCode = officeCodesAnalyzer.getOfficeCode(code, cellPhoneArea.getCode());
            if (officeCode == null) {
                officeCode = new OfficeCode();
                officeCode.setCode(code);
                officeCode.setArea(cellPhoneArea);
            }
            cellPhone.setOfficeCode(officeCode);
            cellPhone.setNumber(cellPhoneNumber.substring(cellPhoneNumber.length() - 4));
        } else
            person.setCellPhone(null);

        Address newAddress;
        if (person.getAddress() == null) {
            newAddress = new Address();
            person.setAddress(newAddress);
        } else
            newAddress = person.getAddress();
        if (street != null) {
            newAddress.setStreet(street);
            newAddress.setNumber(addressNumber);
            if (!floor.isEmpty())
                newAddress.setFloor(floor);
            if (!apartment.isEmpty())
                newAddress.setApartment(apartment);
            if (!zipCode.isEmpty())
                newAddress.setZipCode(StringFormat.capitalize(zipCode));
            newAddress.setStreet1(street1);
            newAddress.setStreet2(street2);
            newAddress.setLocationId(locationId);
        } else {
            newAddress = person.getAddress();
            if (newAddress != null) {
                newAddress.setStreet(null);
                newAddress.setNumber(null);
                newAddress.setFloor(null);
                newAddress.setApartment(null);
                newAddress.setZipCode(null);
                newAddress.setStreet1(null);
                newAddress.setStreet2(null);
                newAddress.setLocationId(0);
            }
        }

        Phone phone;
        if (phoneNumber != null) {
            if (newAddress.getPhone() == null) {
                phone = new Phone();
                newAddress.setPhone(phone);
            } else
                phone = newAddress.getPhone();
            int code = Integer.parseInt(phoneNumber.substring(0, phoneNumber.length() - 4));
            OfficeCode officeCode = officeCodesAnalyzer.getOfficeCode(code, phoneArea.getCode());
            if (officeCode == null) {
                officeCode = new OfficeCode();
                officeCode.setCode(code);
                officeCode.setArea(phoneArea);
            }
            phone.setOfficeCode(officeCode);
            phone.setNumber(phoneNumber.substring(phoneNumber.length() - 4));
        } else
            newAddress.setPhone(null);
    }

    private void locationComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_locationComboBoxActionPerformed
        if (locationComboBox.getSelectedIndex() > 0) {
            Location location = (Location) locationComboBox.getSelectedItem();
            City city = location.getCity();
            State state = city.getState();
            Country country = state.getCountry();
            cityComboBoxModel.setSelectedItem(city);
            stateComboBoxModel.setSelectedItem(state);
            countryComboBoxModel.setSelectedItem(country);
        } else if (open) {
            cityComboBox.setSelectedIndex(0);
            stateComboBox.setSelectedIndex(0);
            countryComboBox.setSelectedIndex(0);
        }
    }//GEN-LAST:event_locationComboBoxActionPerformed

    private void cellPhoneCountryCodeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cellPhoneCountryCodeComboBoxActionPerformed
        cellPhoneAreaCodeComboBoxModel.removeAllElements();
        Country selectedCellPhoneCountry = (Country) cellPhoneCountryCodeComboBox.getSelectedItem();
        if (selectedCellPhoneCountry != null) {
            List<Area> areasList = officeCodesAnalyzer.getAreasList(selectedCellPhoneCountry.getId());
            for (Area area : areasList)
                cellPhoneAreaCodeComboBoxModel.addElement(area);
        }
    }//GEN-LAST:event_cellPhoneCountryCodeComboBoxActionPerformed

    private void phoneCountryCodeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneCountryCodeComboBoxActionPerformed
        phoneAreaCodeComboBoxModel.removeAllElements();
        Country selectedPhoneCountry = (Country) phoneCountryCodeComboBox.getSelectedItem();
        if (selectedPhoneCountry != null) {
            List<Area> areasList = officeCodesAnalyzer.getAreasList(selectedPhoneCountry.getId());
            for (Area area : areasList)
                phoneAreaCodeComboBoxModel.addElement(area);
        }
    }//GEN-LAST:event_phoneCountryCodeComboBoxActionPerformed

    public static boolean isOpen() {
        return open;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addressLabel;
    private javax.swing.JTextField addressNumberTextField;
    private javax.swing.JPanel addressPanel;
    private javax.swing.JLabel andLabel;
    private javax.swing.JLabel apartmentLabel;
    private javax.swing.JTextField apartmentTextField;
    private javax.swing.JLabel areaCodeCloseLabel;
    private javax.swing.JLabel areaCodeCloseLabel1;
    private javax.swing.JLabel areaCodeOpenLabel;
    private javax.swing.JLabel areaCodeOpenLabel1;
    private javax.swing.JLabel betweenLabel;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JComboBox<Area> cellPhoneAreaCodeComboBox;
    private javax.swing.JComboBox<Country> cellPhoneCountryCodeComboBox;
    private javax.swing.JTextField cellPhoneNumberTextField;
    private javax.swing.JPanel cellPhonePanel;
    private javax.swing.JComboBox<City> cityComboBox;
    private javax.swing.JLabel cityLabel;
    private javax.swing.JComboBox<Country> countryComboBox;
    private javax.swing.JLabel countryLabel;
    private javax.swing.JButton createButton;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JTextField firstNameTextField;
    private javax.swing.JLabel floorLabel;
    private javax.swing.JTextField floorTextField;
    private javax.swing.JLabel identificationNumberLabel;
    private javax.swing.JTextField identificationNumberTextField;
    private javax.swing.JPanel identificationPanel;
    private javax.swing.JComboBox<IdentificationType> identificationTypeComboBox;
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JTextField lastNameTextField;
    private javax.swing.JComboBox<Location> locationComboBox;
    private javax.swing.JLabel locationLabel;
    private javax.swing.JLabel middleNameLabel;
    private javax.swing.JTextField middleNameTextField;
    private javax.swing.JButton newButton;
    private javax.swing.JList<Person> peopleList;
    private javax.swing.JScrollPane peopleListScrollPane;
    private javax.swing.JPanel personPanel;
    private javax.swing.JComboBox<Area> phoneAreaCodeComboBox;
    private javax.swing.JComboBox<Country> phoneCountryCodeComboBox;
    private javax.swing.JTextField phoneNumberTextField;
    private javax.swing.JPanel phonePanel;
    private javax.swing.JLabel plusLabel;
    private javax.swing.JLabel plusLabel1;
    private javax.swing.JButton searchButton;
    private javax.swing.JComboBox<State> stateComboBox;
    private javax.swing.JLabel stateLabel;
    private javax.swing.JComboBox<Street> street1ComboBox;
    private javax.swing.JComboBox<Street> street2ComboBox;
    private javax.swing.JComboBox<Street> streetComboBox;
    private javax.swing.JLabel zipCodeLabel;
    private javax.swing.JTextField zipCodeTextField;
    // End of variables declaration//GEN-END:variables

    private DatabaseConnectionsManager databaseConnectionsManager;
    private User user;
    private boolean privilegedUser;
    private OfficeCodesAnalyzer officeCodesAnalyzer;

    // Models.
    private DefaultListModel<Person> peopleListModel;
    private DefaultComboBoxModel<IdentificationType> identificationTypeComboBoxModel;
    private DefaultComboBoxModel<Street> streetComboBoxModel;
    private DefaultComboBoxModel<Street> street1ComboBoxModel;
    private DefaultComboBoxModel<Street> street2ComboBoxModel;
    private DefaultComboBoxModel<Area> cellPhoneAreaCodeComboBoxModel;
    private DefaultComboBoxModel<Country> cellPhoneCountryCodeComboBoxModel;
    private DefaultComboBoxModel<Location> locationComboBoxModel;
    private DefaultComboBoxModel<City> cityComboBoxModel;
    private DefaultComboBoxModel<State> stateComboBoxModel;
    private DefaultComboBoxModel<Country> countryComboBoxModel;
    private DefaultComboBoxModel<Area> phoneAreaCodeComboBoxModel;
    private DefaultComboBoxModel<Country> phoneCountryCodeComboBoxModel;

    // Renderers.
    CountryCodeComboBoxRenderer cellPhoneCountryCodeComboBoxRenderer;
    AreaCodeComboBoxRenderer cellPhoneAreaCodeComboBoxRenderer;
    CountryCodeComboBoxRenderer phoneCountryCodeComboBoxRenderer;
    AreaCodeComboBoxRenderer phoneAreaCodeComboBoxRenderer;

    // Person form.
    private String firstName;
    private String middleName;
    private String lastName;
    private int identificationTypeId;
    private String identificationTypeName;
    private String identificationNumber;
    private Area cellPhoneArea;
    private String cellPhoneNumber;
    private Street street;
    private String addressNumber;
    private String floor;
    private String apartment;
    private String zipCode;
    private Street street1;
    private Street street2;
    private int locationId;
    private Area phoneArea;
    private String phoneNumber;

    private static boolean open;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/PeopleInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(PeopleInternalFrame.class);
}
