/*
 * 		AssignmentInternalFrame.java
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
 * 		AssignmentInternalFrame.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Oct 13, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors.Relationship;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Site;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Equipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Neax61EEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Neax61SigmaEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.SigmaL3AddressEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.ZhoneEquipment;
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
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriber.Subscriber;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.SubscriberData;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.User;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.PhonesAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.SitesAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.StringFormat;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.SearchEquipment;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.SearchNumber;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.SearchPerson;
import java.awt.Color;
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
public class AssignmentInternalFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form AssignmentInternalFrame
     */
    public AssignmentInternalFrame(User user) throws Exception {
        this.user = user;
        if (user.hasRole("assignment-operator"))
            privilegedUser = true;
        createComponentsModels();

        initComponents();

        setNewFormStructure();

        Connection connection = null;
        try {
            databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
            connection = databaseConnectionsManager.takeConnection();

            sitesAnalyzer = new SitesAnalyzer();
            ownNumerationAnalyzer = new PhonesAnalyzer();

            fillComboBoxesModels();
            fillSubscribersListModel(connection);

            open = true;
        } catch (Exception exception) {
            logger.error("Cannot fill combo boxes or list.", exception);
            JOptionPane.showMessageDialog(this, bundle.getString("AssignmentInternalFrame.comboBoxes.problem"), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            databaseConnectionsManager.returnConnection(connection);
        }
    }

    private void createComponentsModels() {
        subscribersListModel = new DefaultListModel<>();
        identificationTypeComboBoxModel = new DefaultComboBoxModel<>();
        streetComboBoxModel = new DefaultComboBoxModel<>();
        street1ComboBoxModel = new DefaultComboBoxModel<>();
        street2ComboBoxModel = new DefaultComboBoxModel<>();
        locationComboBoxModel = new DefaultComboBoxModel<>();
        cityComboBoxModel = new DefaultComboBoxModel<>();
        stateComboBoxModel = new DefaultComboBoxModel<>();
        countryComboBoxModel = new DefaultComboBoxModel<>();
    }

    private void fillComboBoxesModels() {
        fillIdentificationTypeComboBoxModel();
        fillStreetComboBoxModels();
        fillLocationsComboBoxesModels();
    }

    private void fillIdentificationTypeComboBoxModel() {
        List<IdentificationType> identificationsTypesList = new ArrayList<>();
        IdentificationType selectIdentificationType = new IdentificationType();
        selectIdentificationType.setId(0);
        selectIdentificationType.setName(bundle.getString("AssignmentInternalFrame.Select"));
        identificationsTypesList.add(selectIdentificationType);
        for (IdentificationType identificationType : IdentificationsTypesManager.getIdentificationsTypesList()) {
            identificationsTypesList.add(identificationType);
        }
        fillComboBoxModel(identificationsTypesList, identificationTypeComboBoxModel);
    }

    private void fillStreetComboBoxModels() {
        List<Street> streetsList = new ArrayList<>();
        Street selectStreet = new Street();
        selectStreet.setId(0);
        selectStreet.setName(bundle.getString("AssignmentInternalFrame.Select"));
        streetsList.add(selectStreet);
        for (Street street : StreetsManager.getStreetsList()) {
            streetsList.add(street);
        }
        fillComboBoxModel(streetsList, streetComboBoxModel);
        fillComboBoxModel(streetsList, street1ComboBoxModel);
        fillComboBoxModel(streetsList, street2ComboBoxModel);
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
        selectLocation.setName(bundle.getString("AssignmentInternalFrame.Select"));
        locationsList.add(selectLocation);
        for (Location location : LocationsManager.getLocationsList())
            locationsList.add(location);
        fillComboBoxModel(locationsList, locationComboBoxModel);
    }

    private void fillCityComboBoxModel() {
        List<City> citiesList = new ArrayList<>();
        City selectCity = new City();
        selectCity.setId(0);
        selectCity.setName(bundle.getString("AssignmentInternalFrame.Select"));
        citiesList.add(selectCity);
        for (City city : LocationsManager.getCitiesList())
            citiesList.add(city);
        fillComboBoxModel(citiesList, cityComboBoxModel);
    }

    private void fillStateComboBoxModel() {
        List<State> statesList = new ArrayList<>();
        State selectState = new State();
        selectState.setId(0);
        selectState.setName(bundle.getString("AssignmentInternalFrame.Select"));
        statesList.add(selectState);
        for (State state : LocationsManager.getStatesList())
            statesList.add(state);
        fillComboBoxModel(statesList, stateComboBoxModel);
    }

    private void fillCountryComboBoxModel() {
        List<Country> countriesList = new ArrayList<>();
        Country selectCountry = new Country();
        selectCountry.setId(0);
        selectCountry.setName(bundle.getString("AssignmentInternalFrame.Select"));
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
        for (T element : list) {
            model.addElement(element);
        }
    }

    private void fillSubscribersListModel(Connection connection) throws Exception {
        subscribersListModel.removeAllElements();
        List<Subscriber> subscribersList = Subscriber.getSubscribersList(connection);
        for (Subscriber subscriber : subscribersList) {
            subscribersListModel.addElement(subscriber);
        }
    }

    private boolean isValidForm() {
        boolean valid = true;

        if (phone == null) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.phone.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            phoneButton.requestFocus();
            valid = false;
            return valid;
        }

        if (equipment == null) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.equipment.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            equipmentButton.requestFocus();
            valid = false;
            return valid;
        }

        if (person == null) {
            firstName = firstNameTextField.getText().trim();
            lastName = lastNameTextField.getText().trim();
            identificationNumber = identificationNumberTextField.getText().trim();

            if (!firstName.isEmpty() || !lastName.isEmpty() || identificationTypeComboBox.getSelectedIndex() != 0 || !identificationNumber.isEmpty()) {
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

                if (lastName.isEmpty() || !StringFormat.isNoun(lastName)) {
                    JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.lastName.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    lastNameTextField.requestFocus();
                    valid = false;
                    return valid;
                }

                if (!identificationNumber.isEmpty()) {
                    identificationType = ((IdentificationType) identificationTypeComboBoxModel.getSelectedItem());
                    if (!StringFormat.isInteger(identificationNumber)) {
                        JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.identificationNumber.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                        identificationNumberTextField.requestFocus();
                        valid = false;
                        return valid;
                    }
                    if (identificationType.getId() == 0) {
                        JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.identificationType.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                        identificationTypeComboBox.requestFocus();
                        valid = false;
                        return valid;
                    }
                } else {
                    identificationNumber = null;
                }
            }
        }

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
            addressNumberTextField.requestFocus();
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
                    if (!addressNumber.equals(bundle.getString("AssignmentInternalFrame.Address.NoNumber"))) {
                        JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.addressNumber.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                        addressNumberTextField.requestFocus();
                        valid = false;
                        return valid;
                    } else {
                        addressNumber = "0";
                    }
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

        remarks = null;
        String remarksText = remarksTextArea.getText().trim();
        if (!remarksText.equals(bundle.getString("AssignmentInternalFrame.remarksTextArea.text")) && !remarksText.isEmpty()) {
            if (remarksText.length() > 250) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.remarks.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                remarksTextArea.requestFocus();
                valid = false;
                return valid;
            } else {
                remarks = remarksText;
            }
        }

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

        holderButtonGroup = new javax.swing.ButtonGroup();
        subscribersScrollPane = new javax.swing.JScrollPane();
        subscribersList = new javax.swing.JList<>();
        subscriberPanel = new javax.swing.JPanel();
        lineEquipmentLabel = new javax.swing.JLabel();
        lineEquipmentTextField = new javax.swing.JTextField();
        siteLabel = new javax.swing.JLabel();
        plusLabel = new javax.swing.JLabel();
        areaCodeOpenLabel = new javax.swing.JLabel();
        areaCodeCloseLabel = new javax.swing.JLabel();
        subscriberNumberTextField = new javax.swing.JTextField();
        phoneButton = new javax.swing.JButton();
        equipmentButton = new javax.swing.JButton();
        siteTextField = new javax.swing.JTextField();
        countryCodeTextField = new javax.swing.JTextField();
        areaCodeTextField = new javax.swing.JTextField();
        addressPanel = new javax.swing.JPanel();
        addressLabel = new javax.swing.JLabel();
        addressNumberTextField = new javax.swing.JTextField();
        floorLabel = new javax.swing.JLabel();
        floorTextField = new javax.swing.JTextField();
        apartmentLabel = new javax.swing.JLabel();
        apartmentTextField = new javax.swing.JTextField();
        zipCodeLabel = new javax.swing.JLabel();
        zipCodeTextField = new javax.swing.JTextField();
        betweenLabel = new javax.swing.JLabel();
        andLabel = new javax.swing.JLabel();
        locationLabel = new javax.swing.JLabel();
        locationComboBox = new javax.swing.JComboBox<>();
        cityLabel = new javax.swing.JLabel();
        cityComboBox = new javax.swing.JComboBox<>();
        stateLabel = new javax.swing.JLabel();
        stateComboBox = new javax.swing.JComboBox<>();
        countryLabel = new javax.swing.JLabel();
        countryComboBox = new javax.swing.JComboBox<>();
        clearAddressButton = new javax.swing.JButton();
        streetComboBox = new javax.swing.JComboBox<>();
        street1ComboBox = new javax.swing.JComboBox<>();
        street2ComboBox = new javax.swing.JComboBox<>();
        holderPanel = new javax.swing.JPanel();
        holderOptionPanel = new javax.swing.JPanel();
        assigneeRadioButton = new javax.swing.JRadioButton();
        ownerRadioButton = new javax.swing.JRadioButton();
        firstNameLabel = new javax.swing.JLabel();
        firstNameTextField = new javax.swing.JTextField();
        middleNameLabel = new javax.swing.JLabel();
        middleNameTextField = new javax.swing.JTextField();
        lastNameLabel = new javax.swing.JLabel();
        lastNameTextField = new javax.swing.JTextField();
        identificationPanel = new javax.swing.JPanel();
        identificationTypeComboBox = new javax.swing.JComboBox<>();
        identificationNumberLabel = new javax.swing.JLabel();
        identificationNumberTextField = new javax.swing.JTextField();
        searchPersonButton = new javax.swing.JButton();
        clearPersonButton = new javax.swing.JButton();
        buttonsPanel = new javax.swing.JPanel();
        createButton = new javax.swing.JButton();
        newButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        remarksTextArea = new javax.swing.JTextArea();

        setClosable(true);
        setIconifiable(true);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("i18n/AssignmentInternalFrameBundle"); // NOI18N
        setTitle(bundle.getString("AssignmentInternalFrame.title")); // NOI18N
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

        subscribersList.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        subscribersList.setModel(subscribersListModel);
        subscribersList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                subscribersListMouseClicked(evt);
            }
        });
        subscribersScrollPane.setViewportView(subscribersList);

        subscriberPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("AssignmentInternalFrame.subscriberPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        lineEquipmentLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lineEquipmentLabel.setText(bundle.getString("AssignmentInternalFrame.lineEquipmentLabel.text")); // NOI18N

        lineEquipmentTextField.setEditable(false);
        lineEquipmentTextField.setText(bundle.getString("AssignmentInternalFrame.lineEquipmentTextField.text")); // NOI18N

        siteLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        siteLabel.setText(bundle.getString("AssignmentInternalFrame.siteLabel.text")); // NOI18N

        plusLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        plusLabel.setText(bundle.getString("AssignmentInternalFrame.plusLabel.text")); // NOI18N

        areaCodeOpenLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        areaCodeOpenLabel.setText(bundle.getString("AssignmentInternalFrame.areaCodeOpenLabel.text")); // NOI18N

        areaCodeCloseLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        areaCodeCloseLabel.setText(bundle.getString("AssignmentInternalFrame.areaCodeCloseLabel.text")); // NOI18N

        subscriberNumberTextField.setEditable(false);
        subscriberNumberTextField.setText(bundle.getString("AssignmentInternalFrame.subscriberNumberTextField.text")); // NOI18N

        phoneButton.setText(bundle.getString("AssignmentInternalFrame.phoneButton.text")); // NOI18N
        phoneButton.setEnabled(false);
        phoneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneButtonActionPerformed(evt);
            }
        });

        equipmentButton.setText(bundle.getString("AssignmentInternalFrame.equipmentButton.text")); // NOI18N
        equipmentButton.setEnabled(false);
        equipmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                equipmentButtonActionPerformed(evt);
            }
        });

        siteTextField.setEditable(false);
        siteTextField.setText(bundle.getString("AssignmentInternalFrame.siteTextField.text")); // NOI18N

        countryCodeTextField.setEditable(false);
        countryCodeTextField.setText(bundle.getString("AssignmentInternalFrame.countryCodeTextField.text")); // NOI18N

        areaCodeTextField.setEditable(false);
        areaCodeTextField.setText(bundle.getString("AssignmentInternalFrame.areaCodeTextField.text")); // NOI18N

        javax.swing.GroupLayout subscriberPanelLayout = new javax.swing.GroupLayout(subscriberPanel);
        subscriberPanel.setLayout(subscriberPanelLayout);
        subscriberPanelLayout.setHorizontalGroup(
            subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subscriberPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(subscriberPanelLayout.createSequentialGroup()
                        .addComponent(lineEquipmentLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lineEquipmentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(siteLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(siteTextField))
                    .addGroup(subscriberPanelLayout.createSequentialGroup()
                        .addComponent(plusLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(countryCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(areaCodeOpenLabel)
                        .addGap(11, 11, 11)
                        .addComponent(areaCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(areaCodeCloseLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(subscriberNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(phoneButton)
                    .addComponent(equipmentButton))
                .addContainerGap())
        );
        subscriberPanelLayout.setVerticalGroup(
            subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subscriberPanelLayout.createSequentialGroup()
                .addGroup(subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(plusLabel)
                    .addComponent(areaCodeOpenLabel)
                    .addComponent(areaCodeCloseLabel)
                    .addComponent(subscriberNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneButton)
                    .addComponent(countryCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(areaCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lineEquipmentLabel)
                    .addComponent(lineEquipmentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(siteLabel)
                    .addComponent(equipmentButton)
                    .addComponent(siteTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        addressPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("AssignmentInternalFrame.addressPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        addressLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        addressLabel.setText(bundle.getString("AssignmentInternalFrame.addressLabel.text")); // NOI18N

        addressNumberTextField.setText(bundle.getString("AssignmentInternalFrame.addressNumberTextField.text")); // NOI18N

        floorLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        floorLabel.setText(bundle.getString("AssignmentInternalFrame.floorLabel.text")); // NOI18N

        floorTextField.setText(bundle.getString("AssignmentInternalFrame.floorTextField.text")); // NOI18N

        apartmentLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        apartmentLabel.setText(bundle.getString("AssignmentInternalFrame.apartmentLabel.text")); // NOI18N

        apartmentTextField.setText(bundle.getString("AssignmentInternalFrame.apartmentTextField.text")); // NOI18N

        zipCodeLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        zipCodeLabel.setText(bundle.getString("AssignmentInternalFrame.zipCodeLabel.text")); // NOI18N

        zipCodeTextField.setText(bundle.getString("AssignmentInternalFrame.zipCodeTextField.text")); // NOI18N

        betweenLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        betweenLabel.setText(bundle.getString("AssignmentInternalFrame.betweenLabel.text")); // NOI18N

        andLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        andLabel.setText(bundle.getString("AssignmentInternalFrame.andLabel.text")); // NOI18N

        locationLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        locationLabel.setText(bundle.getString("AssignmentInternalFrame.locationLabel.text")); // NOI18N

        locationComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        locationComboBox.setModel(locationComboBoxModel);
        locationComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationComboBoxActionPerformed(evt);
            }
        });

        cityLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cityLabel.setText(bundle.getString("AssignmentInternalFrame.cityLabel.text")); // NOI18N

        cityComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cityComboBox.setModel(cityComboBoxModel);

        stateLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        stateLabel.setText(bundle.getString("AssignmentInternalFrame.stateLabel.text")); // NOI18N

        stateComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        stateComboBox.setModel(stateComboBoxModel);

        countryLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        countryLabel.setText(bundle.getString("AssignmentInternalFrame.countryLabel.text")); // NOI18N

        countryComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        countryComboBox.setModel(countryComboBoxModel);

        clearAddressButton.setText(bundle.getString("AssignmentInternalFrame.clearAddressButton.text")); // NOI18N
        clearAddressButton.setEnabled(false);
        clearAddressButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearAddressButtonActionPerformed(evt);
            }
        });

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
                            .addGroup(addressPanelLayout.createSequentialGroup()
                                .addComponent(betweenLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(street1ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(andLabel))
                            .addGroup(addressPanelLayout.createSequentialGroup()
                                .addComponent(locationLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(locationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cityLabel)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cityComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(street2ComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(addressPanelLayout.createSequentialGroup()
                        .addComponent(stateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stateComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(countryLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(countryComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(addressPanelLayout.createSequentialGroup()
                        .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(addressPanelLayout.createSequentialGroup()
                                .addComponent(floorLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(floorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(apartmentLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(apartmentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(addressPanelLayout.createSequentialGroup()
                                .addComponent(addressLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(streetComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(addressPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 27, Short.MAX_VALUE)
                                .addComponent(zipCodeLabel)
                                .addGap(23, 23, 23)
                                .addComponent(zipCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 9, Short.MAX_VALUE))
                            .addGroup(addressPanelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(addressNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(clearAddressButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        addressPanelLayout.setVerticalGroup(
            addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addressPanelLayout.createSequentialGroup()
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addressLabel)
                    .addComponent(addressNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearAddressButton)
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
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(betweenLabel)
                    .addComponent(andLabel)
                    .addComponent(street1ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(street2ComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(countryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        holderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("AssignmentInternalFrame.holderPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        holderButtonGroup.add(assigneeRadioButton);
        assigneeRadioButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        assigneeRadioButton.setText(bundle.getString("AssignmentInternalFrame.assigneeRadioButton.text")); // NOI18N

        holderButtonGroup.add(ownerRadioButton);
        ownerRadioButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        ownerRadioButton.setSelected(true);
        ownerRadioButton.setText(bundle.getString("AssignmentInternalFrame.ownerRadioButton.text")); // NOI18N

        javax.swing.GroupLayout holderOptionPanelLayout = new javax.swing.GroupLayout(holderOptionPanel);
        holderOptionPanel.setLayout(holderOptionPanelLayout);
        holderOptionPanelLayout.setHorizontalGroup(
            holderOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(holderOptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ownerRadioButton)
                .addGap(29, 29, 29)
                .addComponent(assigneeRadioButton)
                .addContainerGap(12, Short.MAX_VALUE))
        );
        holderOptionPanelLayout.setVerticalGroup(
            holderOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(holderOptionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(holderOptionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ownerRadioButton)
                    .addComponent(assigneeRadioButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        firstNameLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        firstNameLabel.setText(bundle.getString("AssignmentInternalFrame.firstNameLabel.text")); // NOI18N

        firstNameTextField.setText(bundle.getString("AssignmentInternalFrame.firstNameTextField.text")); // NOI18N

        middleNameLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        middleNameLabel.setText(bundle.getString("AssignmentInternalFrame.middleNameLabel.text")); // NOI18N

        middleNameTextField.setText(bundle.getString("AssignmentInternalFrame.middleNameTextField.text")); // NOI18N

        lastNameLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lastNameLabel.setText(bundle.getString("AssignmentInternalFrame.lastNameLabel.text")); // NOI18N

        lastNameTextField.setText(bundle.getString("AssignmentInternalFrame.lastNameTextField.text")); // NOI18N

        identificationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("AssignmentInternalFrame.identificationPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        identificationTypeComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        identificationTypeComboBox.setModel(identificationTypeComboBoxModel);

        identificationNumberLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        identificationNumberLabel.setText(bundle.getString("AssignmentInternalFrame.identificationNumberLabel.text")); // NOI18N

        identificationNumberTextField.setText(bundle.getString("AssignmentInternalFrame.identificationNumberTextField.text")); // NOI18N

        javax.swing.GroupLayout identificationPanelLayout = new javax.swing.GroupLayout(identificationPanel);
        identificationPanel.setLayout(identificationPanelLayout);
        identificationPanelLayout.setHorizontalGroup(
            identificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(identificationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(identificationTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(identificationNumberLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(identificationNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        identificationPanelLayout.setVerticalGroup(
            identificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(identificationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(identificationTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(identificationNumberLabel)
                .addComponent(identificationNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        searchPersonButton.setText(bundle.getString("AssignmentInternalFrame.searchPersonButton.text")); // NOI18N
        searchPersonButton.setEnabled(false);
        searchPersonButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchPersonButtonActionPerformed(evt);
            }
        });

        clearPersonButton.setText(bundle.getString("AssignmentInternalFrame.clearPersonButton.text")); // NOI18N
        clearPersonButton.setEnabled(false);
        clearPersonButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearPersonButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout holderPanelLayout = new javax.swing.GroupLayout(holderPanel);
        holderPanel.setLayout(holderPanelLayout);
        holderPanelLayout.setHorizontalGroup(
            holderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(holderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(holderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(identificationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(holderPanelLayout.createSequentialGroup()
                        .addGroup(holderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(holderPanelLayout.createSequentialGroup()
                                .addGroup(holderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, holderPanelLayout.createSequentialGroup()
                                        .addComponent(lastNameLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lastNameTextField))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, holderPanelLayout.createSequentialGroup()
                                        .addComponent(firstNameLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(firstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(middleNameLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(middleNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(holderPanelLayout.createSequentialGroup()
                                .addComponent(holderOptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(searchPersonButton)
                                .addGap(18, 18, 18)
                                .addComponent(clearPersonButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );

        holderPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {clearPersonButton, searchPersonButton});

        holderPanelLayout.setVerticalGroup(
            holderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(holderPanelLayout.createSequentialGroup()
                .addGroup(holderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(holderOptionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(holderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(searchPersonButton)
                        .addComponent(clearPersonButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(holderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstNameLabel)
                    .addComponent(firstNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(middleNameLabel)
                    .addComponent(middleNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(holderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lastNameLabel)
                    .addComponent(lastNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(identificationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        createButton.setText(bundle.getString("AssignmentInternalFrame.createButton.text")); // NOI18N
        createButton.setEnabled(false);
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        newButton.setText(bundle.getString("AssignmentInternalFrame.newButton.text")); // NOI18N
        newButton.setEnabled(false);
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        removeButton.setText(bundle.getString("AssignmentInternalFrame.removeButton.text")); // NOI18N
        removeButton.setEnabled(false);
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(removeButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(newButton)
                .addGap(18, 18, 18)
                .addComponent(createButton)
                .addContainerGap())
        );

        buttonsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {createButton, newButton});

        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createButton)
                    .addComponent(newButton)
                    .addComponent(removeButton))
                .addContainerGap())
        );

        remarksTextArea.setColumns(20);
        remarksTextArea.setForeground(new java.awt.Color(204, 204, 204));
        remarksTextArea.setRows(5);
        remarksTextArea.setText(bundle.getString("AssignmentInternalFrame.remarksTextArea.text")); // NOI18N
        remarksTextArea.setEnabled(false);
        remarksTextArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                remarksTextAreaFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(remarksTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subscribersScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addressPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(holderPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subscriberPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subscribersScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(subscriberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(holderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addressPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        open = false;
    }//GEN-LAST:event_formInternalFrameClosed

    private void phoneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneButtonActionPerformed
        SearchNumber searchNumber = new SearchNumber(this, ownNumerationAnalyzer);
        phone = searchNumber.search(0);
        if (phone != null) {
            OfficeCode selectedOfficeCode = phone.getOfficeCode();
            Area selectedArea = selectedOfficeCode.getArea();
            Country selectedCountry = selectedArea.getCountry();
            countryCodeTextField.setText(String.valueOf(selectedCountry.getCode()));
            areaCodeTextField.setText(String.valueOf(selectedArea.getCode()));
            subscriberNumberTextField.setText(phone.toString());
        }

    }//GEN-LAST:event_phoneButtonActionPerformed

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

    private void equipmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_equipmentButtonActionPerformed
        SearchEquipment searchEquipment = new SearchEquipment(this, sitesAnalyzer);
        equipment = searchEquipment.search();
        if (equipment != null) {
            lineEquipmentTextField.setText(equipment.toString());
            Site selectedSite = null;
            if (equipment instanceof Neax61EEquipment) {
                selectedSite = ((Neax61EEquipment) equipment).getELineModule().getFrame().getSite();
            } else if (equipment instanceof Neax61SigmaEquipment) {
                selectedSite = ((Neax61SigmaEquipment) equipment).getSigmaLineModule().getFrame().getSite();
            } else if (equipment instanceof SigmaL3AddressEquipment) {
                selectedSite = ((SigmaL3AddressEquipment) equipment).getSite();
            } else if (equipment instanceof ZhoneEquipment) {
                selectedSite = ((ZhoneEquipment) equipment).getSite();
            }

            siteTextField.setText(selectedSite.getCode());
        }
    }//GEN-LAST:event_equipmentButtonActionPerformed

    private void searchPersonButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchPersonButtonActionPerformed
        SearchPerson searchPerson = new SearchPerson(this);
        person = searchPerson.search();
        if (person != null) {
            Relationship selectedRelationship = (ownerRadioButton.isSelected()) ? Relationship.OWNER : Relationship.ASSIGNEE;
            clearPersonForm();
            clearAddressForm();
            if (selectedRelationship == Relationship.OWNER) {
                ownerRadioButton.setSelected(true);
            } else if (selectedRelationship == Relationship.ASSIGNEE) {
                assigneeRadioButton.setSelected(true);
            }

            firstNameTextField.setText(person.getFirstName());
            middleNameTextField.setText(person.getMiddleName());
            lastNameTextField.setText(person.getLastName());
            if (person.getIdentificationType() != null) {
                identificationTypeComboBoxModel.setSelectedItem(person.getIdentificationType());
            }
            int identificationNumber = person.getIdentificationNumber();
            if (identificationNumber != 0) {
                identificationNumberTextField.setText(String.valueOf(identificationNumber));
            }

            address = person.getAddress();
            if (address != null && address.getId() != 0) {
                // Modification for one person, several assignment.
                address.setId(0);

                streetComboBox.setSelectedItem(address.getStreet());
                String number = address.getNumber();
                if (number != null && number.equals("0"))
                    number = bundle.getString("AssignmentInternalFrame.Address.NoNumber");
                addressNumberTextField.setText(number);
                floorTextField.setText(address.getFloor());
                apartmentTextField.setText(address.getApartment());
                zipCodeTextField.setText(address.getZipCode());
                if (address.getStreet1() != null) {
                    street1ComboBox.setSelectedItem(address.getStreet1());
                }
                if (address.getStreet2() != null) {
                    street2ComboBox.setSelectedItem(address.getStreet2());
                }

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
            }

            setEditableHolderForm(false);
            setEditableAddressForm(false);
        }
    }//GEN-LAST:event_searchPersonButtonActionPerformed

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        boolean valid = isValidForm();
        if (valid) {
            Connection connection = databaseConnectionsManager.takeConnection();
            if (subscribersList.getSelectedIndex() == -1) {
                Subscriber subscriber = new Subscriber();
                fillSubscriberData(subscriber);
                subscriber.setData(SubscriberData.getDefaultSubscriberData());
                try {
                    subscriber.insert(connection, user.getId());
                    JOptionPane.showMessageDialog(this, subscriber + " " + bundle.getString("InsertDialog.confirmation"), bundle.getString("InsertDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    renewFormData(connection);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this, subscriber + " " + bundle.getString("InsertDialog.error"), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                }
            } else {
                Subscriber subscriber = subscribersList.getSelectedValue();
                fillSubscriberData(subscriber);
                try {
                    subscriber.update(connection, user.getId());
                    JOptionPane.showMessageDialog(this, subscriber + " " + bundle.getString("ModifyDialog.confirmation"), bundle.getString("ModifyDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    renewFormData(connection);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this, subscriber + " " + bundle.getString("ModifyDialog.error"), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                }
            }
        }
    }//GEN-LAST:event_createButtonActionPerformed

    private void fillSubscriberData(Subscriber subscriber) {
        subscriber.setEquipment(equipment);

        if (address == null) {
            address = new Address();
            if (street != null) {
                address.setStreet(street);
                address.setNumber(addressNumber);
                if (!floor.isEmpty()) {
                    address.setFloor(floor);
                }
                if (!apartment.isEmpty()) {
                    address.setApartment(apartment);
                }
                if (!zipCode.isEmpty()) {
                    address.setZipCode(StringFormat.capitalize(zipCode));
                }
                address.setStreet1(street1);
                address.setStreet2(street2);
                address.setLocationId(locationId);
            } else {
                address.setStreet(null);
                address.setNumber(null);
                address.setFloor(null);
                address.setApartment(null);
                address.setZipCode(null);
                address.setStreet1(null);
                address.setStreet2(null);
                address.setLocationId(0);
            }
        }
        address.setPhone(phone);
        subscriber.setAddress(address);

        if (person == null) {
            if (!firstName.isEmpty()) {
                person = new Person();
                person.setFirstName(StringFormat.capitalizeNames(firstName));
                person.setMiddleName(StringFormat.capitalizeNames(middleName));
                person.setLastName(StringFormat.capitalizeNames(lastName));
                if (identificationNumber != null) {
                    person.setIdentificationType(identificationType);
                    person.setIdentificationNumber(Integer.parseInt(identificationNumber));
                } else {
                    person.setIdentificationType(null);
                    person.setIdentificationNumber(0);
                }
            }
        }
        subscriber.setPerson(person);

        if (ownerRadioButton.isSelected()) {
            subscriber.setRelationship(Relationship.OWNER);
        } else {
            subscriber.setRelationship(Relationship.ASSIGNEE);
        }

        subscriber.setRemarks(remarks);
    }

    private void renewFormData(Connection connection) throws Exception {
        clearForm();
        try {
            fillSubscribersListModel(connection);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("Cannot renew form data.");
        }
        setNewFormStructure();
    }

    private void clearForm() {
        countryCodeTextField.setText("");
        areaCodeTextField.setText("");
        subscriberNumberTextField.setText("");
        lineEquipmentTextField.setText("");
        siteTextField.setText("");
        ownerRadioButton.setSelected(true);
        clearPersonButtonActionPerformed(null);
        clearAddressButtonActionPerformed(null);

        phone = null;
        equipment = null;
        person = null;
        address = null;
    }

    private void setNewFormStructure() {
        createButton.setText(bundle.getString("AssignmentInternalFrame.createButton.text"));
        newButton.setEnabled(false);
        removeButton.setEnabled(false);

        if (privilegedUser) {
            phoneButton.setEnabled(true);
            equipmentButton.setEnabled(true);
            createButton.setEnabled(true);
            searchPersonButton.setEnabled(true);
            remarksTextArea.setEnabled(true);
        }

        remarksTextArea.setForeground(new java.awt.Color(204, 204, 204));
        remarksTextArea.setText(bundle.getString("AssignmentInternalFrame.remarksTextArea.text")); // NOI18N

        phoneButton.requestFocus();
    }

    private void setModifyFormStructure() {
        createButton.setText(bundle.getString("AssignmentInternalFrame.modifyButton.text"));
        phoneButton.setEnabled(false);
        searchPersonButton.setEnabled(false);
        newButton.setEnabled(true);
        if (privilegedUser)
            remarksTextArea.setEnabled(true);

        setEditableHolderForm(false);
        setEditableAddressForm(false);
    }

    private void clearPersonButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearPersonButtonActionPerformed
        person = null;
        clearPersonForm();
        address = null;
        clearAddressForm();
        setEditableHolderForm(true);
        setEditableAddressForm(true);
        firstNameTextField.requestFocus();
    }//GEN-LAST:event_clearPersonButtonActionPerformed

    private void clearAddressButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearAddressButtonActionPerformed
        address = null;
        clearAddressForm();
        setEditableAddressForm(true);
        addressNumberTextField.requestFocus();
    }//GEN-LAST:event_clearAddressButtonActionPerformed

    private void subscribersListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subscribersListMouseClicked
        clearForm();

        Subscriber selectedSubscriber = subscribersList.getSelectedValue();
        address = selectedSubscriber.getAddress();
        phone = address.getPhone();
        OfficeCode selectedOfficeCode = phone.getOfficeCode();
        Area selectedArea = selectedOfficeCode.getArea();
        equipment = selectedSubscriber.getEquipment();
        person = selectedSubscriber.getPerson();

        countryCodeTextField.setText(String.valueOf(selectedArea.getCountry().getCode()));
        areaCodeTextField.setText(String.valueOf(selectedArea.getCode()));
        subscriberNumberTextField.setText(phone.toString());

        lineEquipmentTextField.setText(equipment.toString());
        siteTextField.setText(equipment.getSite().getCode());

        if (selectedSubscriber.getRelationship() == Relationship.ASSIGNEE) {
            assigneeRadioButton.setSelected(true);
        } else {
            ownerRadioButton.setSelected(true);
        }

        if (person != null) {
            firstNameTextField.setText(person.getFirstName());
            middleNameTextField.setText(person.getMiddleName());
            lastNameTextField.setText(person.getLastName());
            if (person.getIdentificationType() != null) {
                identificationTypeComboBoxModel.setSelectedItem(person.getIdentificationType());
            }
            int selectedIdentificationNumber = person.getIdentificationNumber();
            if (selectedIdentificationNumber != 0) {
                identificationNumberTextField.setText(String.valueOf(selectedIdentificationNumber));
            }
        }

        Street selectedStreet = address.getStreet();
        if (selectedStreet != null) {
            streetComboBox.setSelectedItem(selectedStreet);

            String number = address.getNumber();
            if ((number != null) && (number.equals("0")))
                number = bundle.getString("AssignmentInternalFrame.Address.NoNumber");
            addressNumberTextField.setText(number);

            floorTextField.setText(address.getFloor());
            apartmentTextField.setText(address.getApartment());
            zipCodeTextField.setText(address.getZipCode());
            if (address.getStreet1() != null) {
                street1ComboBox.setSelectedItem(address.getStreet1());
            }
            if (address.getStreet2() != null) {
                street2ComboBox.setSelectedItem(address.getStreet2());
            }

            int selectedLocationId = address.getLocationId();
            if (selectedLocationId != 0) {
                Location location = LocationsManager.getLocation(selectedLocationId);
                City city = location.getCity();
                State state = city.getState();
                Country country = state.getCountry();
                locationComboBoxModel.setSelectedItem(location);
                cityComboBoxModel.setSelectedItem(city);
                stateComboBoxModel.setSelectedItem(state);
                countryComboBoxModel.setSelectedItem(country);
            }
        }

        remarksTextArea.setForeground(Color.BLACK);
        remarksTextArea.setText(selectedSubscriber.getRemarks());

        if (privilegedUser && !selectedSubscriber.isWired()) {
            equipmentButton.setEnabled(true);
            removeButton.setEnabled(true);
        } else {
            equipmentButton.setEnabled(false);
            removeButton.setEnabled(false);
        }

        setModifyFormStructure();
    }//GEN-LAST:event_subscribersListMouseClicked

    private void remarksTextAreaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remarksTextAreaFocusGained
        remarksTextArea.setText("");
        remarksTextArea.setForeground(Color.BLACK);
    }//GEN-LAST:event_remarksTextAreaFocusGained

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        subscribersList.clearSelection();
        clearForm();
        setNewFormStructure();
    }//GEN-LAST:event_newButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        boolean valid = isValidForm();
        if (valid) {
            int response = JOptionPane.showConfirmDialog(this, bundle.getString("RemoveDialog.answer") + " " + phone + "?", bundle.getString("RemoveDialog.title"), JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                Connection connection = databaseConnectionsManager.takeConnection();
                Subscriber subscriber = subscribersList.getSelectedValue();
                fillSubscriberData(subscriber);
                try {
                    subscriber.removeAssignment(connection, user.getId());
                    JOptionPane.showMessageDialog(this, subscriber + " " + bundle.getString("RemoveDialog.confirmation"), bundle.getString("RemoveDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    renewFormData(connection);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this, subscriber + " " + bundle.getString("RemoveDialog.error"), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                }
            }
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    private void clearPersonForm() {
        ownerRadioButton.setSelected(true);
        firstNameTextField.setText("");
        middleNameTextField.setText("");
        lastNameTextField.setText("");
        identificationTypeComboBox.setSelectedIndex(0);
        identificationNumberTextField.setText("");
    }

    private void clearAddressForm() {
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
    }

    private void setEditableHolderForm(boolean editable) {
        ownerRadioButton.setEnabled(editable);
        assigneeRadioButton.setEnabled(editable);
        firstNameTextField.setEditable(editable);
        middleNameTextField.setEditable(editable);
        lastNameTextField.setEditable(editable);
        identificationTypeComboBox.setEnabled(editable);
        identificationNumberTextField.setEditable(editable);

        if (privilegedUser) {
            clearPersonButton.setEnabled(!editable);
            searchPersonButton.setEnabled(editable);
        }
    }

    private void setEditableAddressForm(boolean editable) {
        streetComboBox.setEnabled(editable);
        addressNumberTextField.setEditable(editable);
        floorTextField.setEditable(editable);
        apartmentTextField.setEditable(editable);
        zipCodeTextField.setEditable(editable);
        street1ComboBox.setEnabled(editable);
        street2ComboBox.setEnabled(editable);
        locationComboBox.setEnabled(editable);
        cityComboBox.setEnabled(editable);
        stateComboBox.setEnabled(editable);
        countryComboBox.setEnabled(editable);

        if (privilegedUser) {
            clearAddressButton.setEnabled(!editable);
        }
    }

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
    private javax.swing.JLabel areaCodeOpenLabel;
    private javax.swing.JTextField areaCodeTextField;
    private javax.swing.JRadioButton assigneeRadioButton;
    private javax.swing.JLabel betweenLabel;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JComboBox<City> cityComboBox;
    private javax.swing.JLabel cityLabel;
    private javax.swing.JButton clearAddressButton;
    private javax.swing.JButton clearPersonButton;
    private javax.swing.JTextField countryCodeTextField;
    private javax.swing.JComboBox<Country> countryComboBox;
    private javax.swing.JLabel countryLabel;
    private javax.swing.JButton createButton;
    private javax.swing.JButton equipmentButton;
    private javax.swing.JLabel firstNameLabel;
    private javax.swing.JTextField firstNameTextField;
    private javax.swing.JLabel floorLabel;
    private javax.swing.JTextField floorTextField;
    private javax.swing.ButtonGroup holderButtonGroup;
    private javax.swing.JPanel holderOptionPanel;
    private javax.swing.JPanel holderPanel;
    private javax.swing.JLabel identificationNumberLabel;
    private javax.swing.JTextField identificationNumberTextField;
    private javax.swing.JPanel identificationPanel;
    private javax.swing.JComboBox<IdentificationType> identificationTypeComboBox;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lastNameLabel;
    private javax.swing.JTextField lastNameTextField;
    private javax.swing.JLabel lineEquipmentLabel;
    private javax.swing.JTextField lineEquipmentTextField;
    private javax.swing.JComboBox<Location> locationComboBox;
    private javax.swing.JLabel locationLabel;
    private javax.swing.JLabel middleNameLabel;
    private javax.swing.JTextField middleNameTextField;
    private javax.swing.JButton newButton;
    private javax.swing.JRadioButton ownerRadioButton;
    private javax.swing.JButton phoneButton;
    private javax.swing.JLabel plusLabel;
    private javax.swing.JTextArea remarksTextArea;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton searchPersonButton;
    private javax.swing.JLabel siteLabel;
    private javax.swing.JTextField siteTextField;
    private javax.swing.JComboBox<State> stateComboBox;
    private javax.swing.JLabel stateLabel;
    private javax.swing.JComboBox<Street> street1ComboBox;
    private javax.swing.JComboBox<Street> street2ComboBox;
    private javax.swing.JComboBox<Street> streetComboBox;
    private javax.swing.JTextField subscriberNumberTextField;
    private javax.swing.JPanel subscriberPanel;
    private javax.swing.JList<Subscriber> subscribersList;
    private javax.swing.JScrollPane subscribersScrollPane;
    private javax.swing.JLabel zipCodeLabel;
    private javax.swing.JTextField zipCodeTextField;
    // End of variables declaration//GEN-END:variables

    private DatabaseConnectionsManager databaseConnectionsManager;
    private User user;
    private boolean privilegedUser;
    private SitesAnalyzer sitesAnalyzer;
    private PhonesAnalyzer ownNumerationAnalyzer;

    // Models.
    private DefaultListModel<Subscriber> subscribersListModel;
    private DefaultComboBoxModel<IdentificationType> identificationTypeComboBoxModel;
    private DefaultComboBoxModel<Street> streetComboBoxModel;
    private DefaultComboBoxModel<Street> street1ComboBoxModel;
    private DefaultComboBoxModel<Street> street2ComboBoxModel;
    private DefaultComboBoxModel<Location> locationComboBoxModel;
    private DefaultComboBoxModel<City> cityComboBoxModel;
    private DefaultComboBoxModel<State> stateComboBoxModel;
    private DefaultComboBoxModel<Country> countryComboBoxModel;

    // Assignment form.
    private Phone phone;
    private Equipment equipment;
    private Person person;
    private Address address;
    private String addressNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private IdentificationType identificationType;
    private String identificationNumber;
    private Street street;
    private String floor;
    private String apartment;
    private String zipCode;
    private Street street1;
    private Street street2;
    private int locationId;
    private String remarks;

    private static boolean open;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/AssignmentInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(AssignmentInternalFrame.class);
}
