/*
 * 		RepairsInternalFrame.java
 *   Copyright (C) 2018  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		RepairsInternalFrame.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Jan 22, 2018
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.Broadband;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.BroadbandPort;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAM;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMBoard;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.managers.DSLAMsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.BlockPosition;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Site;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.SwitchBlock;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.Person;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.Address;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.Location;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.Street;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.address.managers.LocationsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Phone;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs.Repairing;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs.RepairingCheck;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs.RepairingType;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs.Repairman;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs.ServiceOrder;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs.managers.RepairingTypesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs.managers.RepairmenManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriber.Subscriber;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.User;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetCable;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetFrame;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetPair;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.Wiring;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.Printer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.OwnPhoneNumber;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.RepairingTypeComboBoxRenderer;
import java.awt.Color;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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
public class RepairsInternalFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form RepairsInternalFrame
     *
     * @param user
     */
    public RepairsInternalFrame(User user) {
        this.user = user;
        if (user.hasRole("repairs-operator"))
            privilegedUser = true;

        createComponentsModels();
        repairingTypeComboBoxRenderer = new RepairingTypeComboBoxRenderer();

        initComponents();

        databaseConnectionsManager = DatabaseConnectionsManager.getInstance();

        fillServiceOrdersJListModel();
        fillComboBoxesModels();

        if (privilegedUser) {
            lineSelectionButton.setEnabled(true);
        }

        open = true;
    }

    private void createComponentsModels() {
        serviceOrdersJListModel = new DefaultListModel<>();
        repairingTypeComboBoxModel = new DefaultComboBoxModel<>();
        repairmanComboBoxModel = new DefaultComboBoxModel<>();
    }

    private void fillServiceOrdersJListModel() {
        serviceOrdersJListModel.removeAllElements();

        Connection connection = databaseConnectionsManager.takeConnection();
        try {
            serviceOrdersList = ServiceOrder.getServiceOrdersList(connection);
            for (ServiceOrder serviceOrder : serviceOrdersList) {
                serviceOrdersJListModel.addElement(serviceOrder);
            }
        } catch (Exception ex) {
            logger.error("Cannot fill service orders list.", ex);
            JOptionPane.showMessageDialog(this, bundle.getString("RepairsInternalFrame.serviceOrderList.problem"), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            databaseConnectionsManager.returnConnection(connection);
        }
    }

    private void fillComboBoxesModels() {
        fillRepairingTypeComboBoxModel();
        fillRepairmanComboBoxModel(RepairmenManager.getRepairmenList());
    }

    private void fillRepairingTypeComboBoxModel() {
        List<RepairingType> repairingTypesList = new ArrayList<>();
        RepairingType selectRepairingType = new RepairingType();
        selectRepairingType.setId(0);
        selectRepairingType.setName(bundle.getString("RepairsInternalFrame.Select"));
        repairingTypesList.add(selectRepairingType);
        for (RepairingType repairingType : RepairingTypesManager.getRepairingTypesList()) {
            repairingTypesList.add(repairingType);
        }
        fillComboBoxModel(repairingTypesList, repairingTypeComboBoxModel);
    }

    private void fillRepairmanComboBoxModel(List<Repairman> repairmenList) {
        List<Repairman> list = new ArrayList<>();
        repairmanComboBoxModel.removeAllElements();

        Repairman selectRepairman = new Repairman();
        selectRepairman.setId(0);
        Person person = new Person();
        person.setLastName(bundle.getString("RepairsInternalFrame.Select"));
        selectRepairman.setPerson(person);
        list.add(selectRepairman);
        for (Repairman repairman : repairmenList) {
            list.add(repairman);
        }
        fillComboBoxModel(list, repairmanComboBoxModel);
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

    private void enableComponents(boolean enabled) {
        enableRepairingComponents(enabled);
        enableRepairmanComponents(enabled);
        enableCheckComponents(enabled);
        enableButtons(enabled);
    }

    private void enableRepairingComponents(boolean enabled) {
        repairingTypeComboBox.setEnabled(enabled);
        repairingTypeRemarksTextArea.setEnabled(enabled);
        contactTextField.setEnabled(enabled);
    }

    private void enableRepairmanComponents(boolean enabled) {
        repairmanComboBox.setEnabled(enabled);
        if (repairmanComboBox.getSelectedIndex() > 0)
            repairedCheckBox.setEnabled(enabled);
        repairmanRemarksTextArea.setEnabled(enabled);
    }

    private void enableCheckComponents(boolean enabled) {
        checkedRadioButton.setEnabled(enabled);
        rejectedRadioButton.setEnabled(enabled);
        checkRemarksTextArea.setEnabled(enabled);
    }

    private void enableButtons(boolean enabled) {
        printButton.setEnabled(enabled);
        newButton.setEnabled(enabled);
        createButton.setEnabled(enabled);
    }

    private void clearForm() {
        setTitle(bundle.getString("RepairsInternalFrame.title"));

        clearSubscriberComponents();
        clearServiceOrderComponents();
        clearRepairingComponents();
        clearCheckComponents();
    }

    private void clearSubscriberComponents() {
        lineTextField.setText("");
        holderTextField.setText("");
        broadbandCheckBox.setSelected(false);
        addressTextField.setText("");
        betweenTextField.setText("");
        locationTextField.setText("");
    }

    private void clearServiceOrderComponents() {
        repairingTypeComboBox.setSelectedIndex(0);
        setRepairingTypeRemarks(null);
        contactTextField.setText("");
    }

    private void clearRepairingComponents() {
        repairmanComboBox.setSelectedIndex(0);
        repairsCompanyTextField.setText("");
        setRepairmanRemarks(null);
        repairedCheckBox.setSelected(false);
        repairedDateLabel.setText("");
    }

    private void clearCheckComponents() {
        checkButtonGroup.clearSelection();
        checkedTimeLabel.setText("");
        setCheckRemarks(null);
    }

    private boolean isValidForm() {
        boolean valid = true;

        repairingType = (RepairingType) repairingTypeComboBox.getSelectedItem();
        if (repairingType.getId() == 0) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.repairingType.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            repairingTypeComboBox.requestFocus();
            valid = false;
            return valid;
        }

        repairingTypeRemarks = null;
        String repairingTypeRemarksText = repairingTypeRemarksTextArea.getText().trim();
        if (!repairingTypeRemarksText.equals(bundle.getString("RepairsInternalFrame.repairingTypeRemarksTextArea.text")) && !repairingTypeRemarksText.isEmpty()) {
            if (repairingTypeRemarksText.length() > 250) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.repairingTypeRemarks.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                repairingTypeRemarksTextArea.requestFocus();
                valid = false;
                return valid;
            } else {
                repairingTypeRemarks = repairingTypeRemarksText;
            }
        }

        contact = null;
        String contactText = contactTextField.getText().trim();
        if (!contactText.isEmpty()) {
            if (contactText.length() > 45) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.contact.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                contactTextField.requestFocus();
                valid = false;
                return valid;
            } else {
                contact = contactText;
            }
        }

        repairmanRemarks = null;
        String repairmanRemarksText = repairmanRemarksTextArea.getText().trim();
        if (!repairmanRemarksText.equals(bundle.getString("RepairsInternalFrame.repairmanRemarksTextArea.text")) && !repairmanRemarksText.isEmpty()) {
            if (repairmanRemarksText.length() > 100) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.repairmanRemarks.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                repairmanRemarksTextArea.requestFocus();
                valid = false;
                return valid;
            } else {
                repairmanRemarks = repairmanRemarksText;
            }
        }

        checkRemarks = null;
        String checkRemarksText = checkRemarksTextArea.getText().trim();
        if (!checkRemarksText.equals(bundle.getString("RepairsInternalFrame.checkRemarksTextArea.text")) && !checkRemarksText.isEmpty()) {
            if (checkRemarksText.length() > 100) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.checkRemarks.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                checkRemarksTextArea.requestFocus();
                valid = false;
                return valid;
            } else {
                checkRemarks = checkRemarksText;
            }
        }

        return valid;
    }

    private void setFormComponents(ServiceOrder serviceOrder) {
        setTitle(bundle.getString("RepairsInternalFrame.title") + " - " + bundle.getString("RepairsInternalFrame.serviceOrderPanel.title") + ": " + serviceOrder.getId() + " (" + serviceOrder.getUser().getPerson().getFullName() + " - " + serviceOrder.getCreationTime() + ")");
        setSubscriberComponents(serviceOrder);
        setRepairingComponents(serviceOrder);

        Repairing repairing = serviceOrder.getLastRepairing();
        if (repairing != null) {
            setRepairmanComponents(repairing);

            RepairingCheck repairingCheck = repairing.getLastRepairingCheck();
            if (repairingCheck != null)
                setRepairingCheckComponents(repairingCheck);
        }
    }

    private void setSubscriberComponents(ServiceOrder serviceOrder) {
        Subscriber subscriber = serviceOrder.getSubscriber();
        lineTextField.setText(subscriber.getAddress().getPhone().getFullNumeration());
        Person holder = subscriber.getPerson();
        if (holder != null)
            holderTextField.setText(subscriber.getPerson().getFullName());
        broadbandCheckBox.setSelected(subscriber.isBroadband());
        setAddressComponents(subscriber.getAddress());
    }

    private void setAddressComponents(Address address) {
        addressTextField.setText(getAddressContent(address));
        betweenTextField.setText(getBetweenContent(address));
        locationTextField.setText(getLocationContent(address));
    }

    private String getAddressContent(Address address) {
        StringBuilder addressBuilder = new StringBuilder();

        Street street = address.getStreet();
        if (street != null) {
            addressBuilder.append(street);
            addressBuilder.append(" ");
            String addressNumber = address.getNumber();
            if (addressNumber != null) {
                if (addressNumber.equals("0")) {
                    addressBuilder.append(bundle.getString("RepairsInternalFrame.Address.NoNumber"));
                } else {
                    addressBuilder.append(addressNumber);
                }
            }
            String floor = address.getFloor();
            String apartment = address.getApartment();
            if (floor != null || apartment != null) {
                addressBuilder.append(", ");
                if (floor != null) {
                    addressBuilder.append(floor);
                    addressBuilder.append(" ");
                    addressBuilder.append(bundle.getString("RepairsInternalFrame.Address.Floor"));
                    addressBuilder.append(" ");
                }
                if (apartment != null) {
                    addressBuilder.append(bundle.getString("RepairsInternalFrame.Address.Apartment"));
                    addressBuilder.append(" ");
                    addressBuilder.append(apartment);
                }
            }
        }
        return addressBuilder.toString();
    }

    private String getBetweenContent(Address address) {
        StringBuilder betweenBuilder = new StringBuilder();
        Street street1 = address.getStreet1();
        if (street1 != null) {
            betweenBuilder.append(street1.getName());
        }
        betweenBuilder.append("/");
        Street street2 = address.getStreet2();
        if (street2 != null) {
            betweenBuilder.append(street2.getName());
        }
        return betweenBuilder.toString();
    }

    private String getLocationContent(Address address) {
        StringBuilder locationBuilder = new StringBuilder();
        int locationId = address.getLocationId();
        if (locationId != 0) {
            Location location = LocationsManager.getLocation(locationId);
            locationBuilder.append(location.getName());
            locationBuilder.append(", ");
            locationBuilder.append(location.getCity().getName());
        }
        return locationBuilder.toString();
    }

    private void setRepairingComponents(ServiceOrder serviceOrder) {
        repairingTypeComboBox.setSelectedItem(serviceOrder.getRepairingType());
        setRepairingTypeRemarks(serviceOrder.getRemarks());
        contactTextField.setText(serviceOrder.getContact());
    }

    private void setRepairmanComponents(Repairing repairing) {
        Repairman repairman = repairing.getRepairman();
        repairmanComboBox.setSelectedItem(repairman);
        repairsCompanyTextField.setText(repairman.getRepairsCompany().getName());
        LocalDate repairedDate = repairing.getRepairedDate();
        if (repairedDate != null) {
            repairedCheckBox.setSelected(true);
            repairedDateLabel.setText(repairedDate.toString());
        }
        setRepairmanRemarks(repairing.getRepairmanRemarks());
    }

    private void setRepairingCheckComponents(RepairingCheck repairingCheck) {
        Boolean check = repairingCheck.isApproved();
        if (check != null) {
            if (check)
                checkedRadioButton.setSelected(true);
            else
                rejectedRadioButton.setSelected(true);
            checkedTimeLabel.setText(repairingCheck.getTime().toString());
            setCheckRemarks(repairingCheck.getRemarks());
        }
    }

    private void setRepairingTypeRemarks(String remarks) {
        if (remarks != null) {
            repairingTypeRemarksTextArea.setForeground(Color.BLACK);
            repairingTypeRemarksTextArea.setText(remarks);
        } else {
            repairingTypeRemarksTextArea.setForeground(new java.awt.Color(204, 204, 204));
            repairingTypeRemarksTextArea.setText(bundle.getString("RepairsInternalFrame.repairingTypeRemarksTextArea.text"));
        }
    }

    private void setRepairmanRemarks(String remarks) {
        if (remarks != null) {
            repairmanRemarksTextArea.setForeground(Color.BLACK);
            repairmanRemarksTextArea.setText(remarks);
        } else {
            repairmanRemarksTextArea.setForeground(new java.awt.Color(204, 204, 204));
            repairmanRemarksTextArea.setText(bundle.getString("RepairsInternalFrame.repairmanRemarksTextArea.text"));
        }
    }

    private void setCheckRemarks(String remarks) {
        if (remarks != null) {
            checkRemarksTextArea.setForeground(Color.BLACK);
            checkRemarksTextArea.setText(remarks);
        } else {
            checkRemarksTextArea.setForeground(new java.awt.Color(204, 204, 204));
            checkRemarksTextArea.setText(bundle.getString("RepairsInternalFrame.checkRemarksTextArea.text"));
        }
    }

    private void setButtonsConfiguration() {
        if (serviceOrdersJList.getSelectedIndex() == -1)
            createButton.setText(bundle.getString("RepairsInternalFrame.createButton.text"));
        else
            createButton.setText(bundle.getString("RepairsInternalFrame.modifyButton.text"));
    }

    public static boolean isOpen() {
        return open;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        checkButtonGroup = new javax.swing.ButtonGroup();
        repairsScrollPane = new javax.swing.JScrollPane();
        serviceOrdersJList = new javax.swing.JList<>();
        repairsPanel = new javax.swing.JPanel();
        subscriberPanel = new javax.swing.JPanel();
        lineLabel = new javax.swing.JLabel();
        lineTextField = new javax.swing.JTextField();
        holderLabel = new javax.swing.JLabel();
        holderTextField = new javax.swing.JTextField();
        lineSelectionButton = new javax.swing.JButton();
        addressPanel = new javax.swing.JPanel();
        addressLabel = new javax.swing.JLabel();
        betweenLabel = new javax.swing.JLabel();
        betweenTextField = new javax.swing.JTextField();
        locationLabel = new javax.swing.JLabel();
        locationTextField = new javax.swing.JTextField();
        addressTextField = new javax.swing.JTextField();
        broadbandCheckBox = new javax.swing.JCheckBox();
        serviceOrderPanel = new javax.swing.JPanel();
        repairingTypeLabel = new javax.swing.JLabel();
        repairingTypeComboBox = new javax.swing.JComboBox<>();
        remarksScrollPane = new javax.swing.JScrollPane();
        repairingTypeRemarksTextArea = new javax.swing.JTextArea();
        contactLabel = new javax.swing.JLabel();
        contactTextField = new javax.swing.JTextField();
        repairmanPanel = new javax.swing.JPanel();
        technicianLabel = new javax.swing.JLabel();
        repairmanComboBox = new javax.swing.JComboBox<>();
        repairsCompanyLabel = new javax.swing.JLabel();
        repairedCheckBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        repairmanRemarksTextArea = new javax.swing.JTextArea();
        repairsCompanyTextField = new javax.swing.JTextField();
        repairedDateLabel = new javax.swing.JLabel();
        checkPanel = new javax.swing.JPanel();
        checkedRadioButton = new javax.swing.JRadioButton();
        rejectedRadioButton = new javax.swing.JRadioButton();
        checkedTimeLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        checkRemarksTextArea = new javax.swing.JTextArea();
        buttonsPanel = new javax.swing.JPanel();
        createButton = new javax.swing.JButton();
        newButton = new javax.swing.JButton();
        printButton = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("i18n/RepairsInternalFrameBundle"); // NOI18N
        setTitle(bundle.getString("RepairsInternalFrame.title")); // NOI18N
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

        serviceOrdersJList.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        serviceOrdersJList.setModel(serviceOrdersJListModel);
        serviceOrdersJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                serviceOrdersJListMouseClicked(evt);
            }
        });
        repairsScrollPane.setViewportView(serviceOrdersJList);

        subscriberPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("RepairsInternalFrame.subscriberPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        lineLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lineLabel.setText(bundle.getString("RepairsInternalFrame.lineLabel.text")); // NOI18N

        lineTextField.setEditable(false);
        lineTextField.setText(bundle.getString("RepairsInternalFrame.lineTextField.text")); // NOI18N

        holderLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        holderLabel.setText(bundle.getString("RepairsInternalFrame.holderLabel.text")); // NOI18N

        holderTextField.setEditable(false);
        holderTextField.setText(bundle.getString("RepairsInternalFrame.holderTextField.text")); // NOI18N

        lineSelectionButton.setText(bundle.getString("RepairsInternalFrame.lineSelectionButton.text")); // NOI18N
        lineSelectionButton.setEnabled(false);
        lineSelectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lineSelectionButtonActionPerformed(evt);
            }
        });

        addressPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("RepairsInternalFrame.addressPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        addressLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        addressLabel.setText(bundle.getString("RepairsInternalFrame.addressLabel.text")); // NOI18N

        betweenLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        betweenLabel.setText(bundle.getString("RepairsInternalFrame.betweenLabel.text")); // NOI18N

        betweenTextField.setEditable(false);
        betweenTextField.setText(bundle.getString("RepairsInternalFrame.betweenTextField.text")); // NOI18N

        locationLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        locationLabel.setText(bundle.getString("RepairsInternalFrame.locationLabel.text")); // NOI18N

        locationTextField.setEditable(false);
        locationTextField.setText(bundle.getString("RepairsInternalFrame.locationTextField.text")); // NOI18N

        addressTextField.setEditable(false);
        addressTextField.setText(bundle.getString("RepairsInternalFrame.addressTextField.text")); // NOI18N

        javax.swing.GroupLayout addressPanelLayout = new javax.swing.GroupLayout(addressPanel);
        addressPanel.setLayout(addressPanelLayout);
        addressPanelLayout.setHorizontalGroup(
            addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addressPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(locationLabel)
                        .addComponent(betweenLabel))
                    .addComponent(addressLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(betweenTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                    .addComponent(locationTextField)
                    .addComponent(addressTextField))
                .addContainerGap())
        );
        addressPanelLayout.setVerticalGroup(
            addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addressPanelLayout.createSequentialGroup()
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(addressLabel)
                    .addComponent(addressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(betweenLabel)
                    .addComponent(betweenTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addressPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(locationLabel)
                    .addComponent(locationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        broadbandCheckBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        broadbandCheckBox.setText(bundle.getString("RepairsInternalFrame.broadbandCheckBox.text")); // NOI18N
        broadbandCheckBox.setEnabled(false);

        javax.swing.GroupLayout subscriberPanelLayout = new javax.swing.GroupLayout(subscriberPanel);
        subscriberPanel.setLayout(subscriberPanelLayout);
        subscriberPanelLayout.setHorizontalGroup(
            subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subscriberPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(subscriberPanelLayout.createSequentialGroup()
                            .addComponent(holderLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(holderTextField))
                        .addGroup(subscriberPanelLayout.createSequentialGroup()
                            .addComponent(lineLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(lineTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(lineSelectionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(broadbandCheckBox))
                .addGap(18, 18, 18)
                .addComponent(addressPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        subscriberPanelLayout.setVerticalGroup(
            subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subscriberPanelLayout.createSequentialGroup()
                .addGroup(subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(subscriberPanelLayout.createSequentialGroup()
                        .addGroup(subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lineLabel)
                            .addComponent(lineTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lineSelectionButton))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(holderLabel)
                            .addComponent(holderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(broadbandCheckBox))
                    .addComponent(addressPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        serviceOrderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("RepairsInternalFrame.serviceOrderPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        repairingTypeLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        repairingTypeLabel.setText(bundle.getString("RepairsInternalFrame.repairingTypeLabel.text")); // NOI18N

        repairingTypeComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        repairingTypeComboBox.setModel(repairingTypeComboBoxModel);
        repairingTypeComboBox.setEnabled(false);
        repairingTypeComboBox.setRenderer(repairingTypeComboBoxRenderer);
        repairingTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repairingTypeComboBoxActionPerformed(evt);
            }
        });

        repairingTypeRemarksTextArea.setColumns(20);
        repairingTypeRemarksTextArea.setForeground(new java.awt.Color(204, 204, 204));
        repairingTypeRemarksTextArea.setLineWrap(true);
        repairingTypeRemarksTextArea.setRows(5);
        repairingTypeRemarksTextArea.setText(bundle.getString("RepairsInternalFrame.repairingTypeRemarksTextArea.text")); // NOI18N
        repairingTypeRemarksTextArea.setEnabled(false);
        repairingTypeRemarksTextArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                repairingTypeRemarksTextAreaFocusGained(evt);
            }
        });
        remarksScrollPane.setViewportView(repairingTypeRemarksTextArea);

        contactLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        contactLabel.setText(bundle.getString("RepairsInternalFrame.contactLabel.text")); // NOI18N

        contactTextField.setText(bundle.getString("RepairsInternalFrame.contactTextField.text")); // NOI18N
        contactTextField.setEnabled(false);

        repairmanPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("RepairsInternalFrame.repairmanPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        technicianLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        technicianLabel.setText(bundle.getString("RepairsInternalFrame.technicianLabel.text")); // NOI18N

        repairmanComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        repairmanComboBox.setModel(repairmanComboBoxModel);
        repairmanComboBox.setEnabled(false);
        repairmanComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repairmanComboBoxActionPerformed(evt);
            }
        });

        repairsCompanyLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        repairsCompanyLabel.setText(bundle.getString("RepairsInternalFrame.repairsCompanyLabel.text")); // NOI18N

        repairedCheckBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        repairedCheckBox.setText(bundle.getString("RepairsInternalFrame.repairedCheckBox.text")); // NOI18N
        repairedCheckBox.setEnabled(false);

        repairmanRemarksTextArea.setColumns(20);
        repairmanRemarksTextArea.setForeground(new java.awt.Color(204, 204, 204));
        repairmanRemarksTextArea.setLineWrap(true);
        repairmanRemarksTextArea.setRows(5);
        repairmanRemarksTextArea.setText(bundle.getString("RepairsInternalFrame.repairmanRemarksTextArea.text")); // NOI18N
        repairmanRemarksTextArea.setEnabled(false);
        repairmanRemarksTextArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                repairmanRemarksTextAreaFocusGained(evt);
            }
        });
        jScrollPane1.setViewportView(repairmanRemarksTextArea);

        repairsCompanyTextField.setEditable(false);
        repairsCompanyTextField.setText(bundle.getString("RepairsInternalFrame.repairsCompanyTextField.text")); // NOI18N

        repairedDateLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        repairedDateLabel.setText(bundle.getString("RepairsInternalFrame.repairedDateLabel.text")); // NOI18N

        javax.swing.GroupLayout repairmanPanelLayout = new javax.swing.GroupLayout(repairmanPanel);
        repairmanPanel.setLayout(repairmanPanelLayout);
        repairmanPanelLayout.setHorizontalGroup(
            repairmanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, repairmanPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(repairmanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(repairmanPanelLayout.createSequentialGroup()
                        .addGroup(repairmanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(technicianLabel)
                            .addComponent(repairsCompanyLabel))
                        .addGap(18, 18, 18)
                        .addGroup(repairmanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(repairmanComboBox, 0, 168, Short.MAX_VALUE)
                            .addComponent(repairsCompanyTextField))
                        .addGap(18, 18, 18)
                        .addGroup(repairmanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(repairedCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(repairedDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(90, 90, 90))
        );
        repairmanPanelLayout.setVerticalGroup(
            repairmanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(repairmanPanelLayout.createSequentialGroup()
                .addGroup(repairmanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(technicianLabel)
                    .addComponent(repairmanComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(repairedCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(repairmanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(repairedDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(repairmanPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(repairsCompanyLabel)
                        .addComponent(repairsCompanyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        checkPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("RepairsInternalFrame.checkPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        checkButtonGroup.add(checkedRadioButton);
        checkedRadioButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        checkedRadioButton.setText(bundle.getString("RepairsInternalFrame.checkedRadioButton.text")); // NOI18N
        checkedRadioButton.setEnabled(false);
        checkedRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkRadioButtonActionPerformed(evt);
            }
        });

        checkButtonGroup.add(rejectedRadioButton);
        rejectedRadioButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        rejectedRadioButton.setText(bundle.getString("RepairsInternalFrame.rejectedRadioButton.text")); // NOI18N
        rejectedRadioButton.setEnabled(false);
        rejectedRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkRadioButtonActionPerformed(evt);
            }
        });

        checkedTimeLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        checkedTimeLabel.setText(bundle.getString("RepairsInternalFrame.checkedTimeLabel.text")); // NOI18N

        checkRemarksTextArea.setColumns(20);
        checkRemarksTextArea.setForeground(new java.awt.Color(204, 204, 204));
        checkRemarksTextArea.setLineWrap(true);
        checkRemarksTextArea.setRows(5);
        checkRemarksTextArea.setText(bundle.getString("RepairsInternalFrame.checkRemarksTextArea.text")); // NOI18N
        checkRemarksTextArea.setEnabled(false);
        checkRemarksTextArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                checkRemarksTextAreaFocusGained(evt);
            }
        });
        jScrollPane2.setViewportView(checkRemarksTextArea);

        javax.swing.GroupLayout checkPanelLayout = new javax.swing.GroupLayout(checkPanel);
        checkPanel.setLayout(checkPanelLayout);
        checkPanelLayout.setHorizontalGroup(
            checkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(checkPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(checkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(checkPanelLayout.createSequentialGroup()
                        .addComponent(checkedRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(rejectedRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(checkedTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        checkPanelLayout.setVerticalGroup(
            checkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(checkPanelLayout.createSequentialGroup()
                .addGroup(checkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkedTimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(checkPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(checkedRadioButton)
                        .addComponent(rejectedRadioButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout serviceOrderPanelLayout = new javax.swing.GroupLayout(serviceOrderPanel);
        serviceOrderPanel.setLayout(serviceOrderPanelLayout);
        serviceOrderPanelLayout.setHorizontalGroup(
            serviceOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, serviceOrderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(serviceOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(serviceOrderPanelLayout.createSequentialGroup()
                        .addComponent(checkPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(32, 32, 32))
                    .addGroup(serviceOrderPanelLayout.createSequentialGroup()
                        .addGroup(serviceOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, serviceOrderPanelLayout.createSequentialGroup()
                                .addComponent(repairingTypeLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(repairingTypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, serviceOrderPanelLayout.createSequentialGroup()
                                .addGroup(serviceOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(remarksScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, serviceOrderPanelLayout.createSequentialGroup()
                                        .addComponent(contactLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(contactTextField)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(repairmanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        serviceOrderPanelLayout.setVerticalGroup(
            serviceOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serviceOrderPanelLayout.createSequentialGroup()
                .addGroup(serviceOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(serviceOrderPanelLayout.createSequentialGroup()
                        .addGroup(serviceOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(repairingTypeLabel)
                            .addComponent(repairingTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(remarksScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(serviceOrderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(contactTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(contactLabel)))
                    .addComponent(repairmanPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checkPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        createButton.setText(bundle.getString("RepairsInternalFrame.createButton.text")); // NOI18N
        createButton.setEnabled(false);
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        newButton.setText(bundle.getString("RepairsInternalFrame.newButton.text")); // NOI18N
        newButton.setEnabled(false);
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        printButton.setText(bundle.getString("RepairsInternalFrame.printButton.text")); // NOI18N
        printButton.setEnabled(false);
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(printButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(newButton)
                .addGap(18, 18, 18)
                .addComponent(createButton)
                .addContainerGap())
        );
        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(createButton)
                    .addComponent(newButton)
                    .addComponent(printButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout repairsPanelLayout = new javax.swing.GroupLayout(repairsPanel);
        repairsPanel.setLayout(repairsPanelLayout);
        repairsPanelLayout.setHorizontalGroup(
            repairsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(repairsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(repairsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(serviceOrderPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subscriberPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        repairsPanelLayout.setVerticalGroup(
            repairsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(repairsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subscriberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(serviceOrderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(repairsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(repairsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(repairsScrollPane)
                    .addComponent(repairsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void repairmanComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repairmanComboBoxActionPerformed
        if (repairmanComboBox.getSelectedIndex() > 0) {
            Repairman selectedRepairman = (Repairman) repairmanComboBox.getSelectedItem();
            repairsCompanyTextField.setText(selectedRepairman.getRepairsCompany().getName());
        } else
            repairsCompanyTextField.setText("");

        repairedCheckBox.setSelected(false);
        repairedCheckBox.setEnabled(false);
        repairedDateLabel.setText("");
        clearCheckComponents();
        enableCheckComponents(false);
        printButton.setEnabled(false);
    }//GEN-LAST:event_repairmanComboBoxActionPerformed

    // Only privileged users.
    private void lineSelectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lineSelectionButtonActionPerformed
        clearForm();
        enableComponents(false);
        enableButtons(false);

        OwnPhoneNumber lineNumber = new OwnPhoneNumber(this);
        Phone phone = lineNumber.getPhone();
        if (phone != null) {
            Connection connection = databaseConnectionsManager.takeConnection();
            try {
                subscriber = Subscriber.getSubscriberInformation(connection, phone);
                if (subscriber != null) {
                    ServiceOrder serviceOrder = getServiceOrder(subscriber.getId());
                    if (serviceOrder == null) {
                        lineTextField.setText(phone.getFullNumeration());
                        Person holder = subscriber.getPerson();
                        if (holder != null)
                            holderTextField.setText(subscriber.getPerson().getFullName());
                        broadbandCheckBox.setSelected(subscriber.isBroadband());
                        setAddressComponents(subscriber.getAddress());

                        lineSelectionButton.setEnabled(false);
                        newButton.setEnabled(true);
                        enableRepairingComponents(true);

                    } else {
                        JOptionPane.showMessageDialog(this, bundle.getString("RepairsInternalFrame.serviceOrderExists") + " " + phone.getFullNumeration() + ": " + serviceOrder.getId(), bundle.getString("RepairsInternalFrame.Information"), JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    logger.debug(phone.getFullNumeration() + " information searched, but it isn't assigned.");
                    JOptionPane.showMessageDialog(this, phone.getFullNumeration() + " " + bundle.getString("RepairsInternalFrame.subscriberInformation.notAssigned"), "Error", JOptionPane.WARNING_MESSAGE);
                }

            } catch (Exception ex) {
                logger.error("Couldn't get subscriber information for " + phone.getFullNumeration() + ".", ex);
                JOptionPane.showMessageDialog(this, bundle.getString("RepairsInternalFrame.subscriberInformation.problem") + " " + phone.getFullNumeration() + ".", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {

                databaseConnectionsManager.returnConnection(connection);
            }
        }
    }//GEN-LAST:event_lineSelectionButtonActionPerformed

    private ServiceOrder getServiceOrder(int subscriberId) {
        ServiceOrder serviceOrder = null;
        for (ServiceOrder temp : serviceOrdersList) {
            if (temp.getSubscriber().getId() == subscriberId) {
                serviceOrder = temp;
                break;
            }
        }
        return serviceOrder;
    }

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        open = false;
    }//GEN-LAST:event_formInternalFrameClosed

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        if (isValidForm()) {
            Connection connection = databaseConnectionsManager.takeConnection();
            ServiceOrder serviceOrder = null;
            if (serviceOrdersJList.getSelectedIndex() == -1) {
                serviceOrder = new ServiceOrder();
                fillServiceOrderData(serviceOrder);
                int serviceOrderId = 0;
                try {
                    serviceOrderId = serviceOrder.insert(connection);
                    JOptionPane.showMessageDialog(this, bundle.getString("InsertDialog.confirmation1") + " " + serviceOrderId + " " + bundle.getString("InsertDialog.confirmation2"), bundle.getString("InsertDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this, bundle.getString("InsertDialog.error") + " " + serviceOrder.getSubscriber(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                }

            } else {
                serviceOrder = serviceOrdersJList.getSelectedValue();
                fillServiceOrderData(serviceOrder);
                try {
                    serviceOrder.modify(connection);
                    JOptionPane.showMessageDialog(this, bundle.getString("ModifyDialog.confirmation1") + " " + serviceOrder.getId() + " " + bundle.getString("ModifyDialog.confirmation2"), bundle.getString("ModifyDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this, bundle.getString("ModifyDialog.error") + " " + serviceOrder.getId(), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                }
            }
            renewFormData();
        }
    }//GEN-LAST:event_createButtonActionPerformed

    private void fillServiceOrderData(ServiceOrder serviceOrder) {
        serviceOrder.setSubscriber(subscriber);
        serviceOrder.setRepairingType(repairingType);
        serviceOrder.setRemarks(repairingTypeRemarks);
        serviceOrder.setContact(contact);
        serviceOrder.setUser(user);

        if (repairmanComboBox.getSelectedIndex() > 0) {
            Repairing repairing = new Repairing();
            repairing.setRepairman((Repairman) repairmanComboBox.getSelectedItem());
            repairing.setRepairmanRemarks(repairmanRemarks);
            if (repairedCheckBox.isSelected())
                repairing.setRepairedDate(LocalDate.now());
            serviceOrder.addRepairing(repairing);
        }

        if (checkedRadioButton.isSelected() || rejectedRadioButton.isSelected()) {
            RepairingCheck repairingCheck = new RepairingCheck();
            if (checkedRadioButton.isSelected())
                repairingCheck.setApproved(true);
            else
                repairingCheck.setApproved(false);
            repairingCheck.setRemarks(checkRemarks);
            repairingCheck.setUser(user);
            serviceOrder.getLastRepairing().addRepairingCheck(repairingCheck);
        }
    }

    private void renewFormData() {
        fillServiceOrdersJListModel();
        clearForm();
        enableComponents(false);
        enableButtons(false);

        if (privilegedUser) {
            lineSelectionButton.setEnabled(true);
        }
    }

    private void repairingTypeRemarksTextAreaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_repairingTypeRemarksTextAreaFocusGained
        repairingTypeRemarksTextArea.setText("");
        repairingTypeRemarksTextArea.setForeground(Color.BLACK);
    }//GEN-LAST:event_repairingTypeRemarksTextAreaFocusGained

    private void repairmanRemarksTextAreaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_repairmanRemarksTextAreaFocusGained
        repairmanRemarksTextArea.setText("");
        repairmanRemarksTextArea.setForeground(Color.BLACK);
    }//GEN-LAST:event_repairmanRemarksTextAreaFocusGained

    private void checkRemarksTextAreaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_checkRemarksTextAreaFocusGained
        checkRemarksTextArea.setText("");
        checkRemarksTextArea.setForeground(Color.BLACK);
    }//GEN-LAST:event_checkRemarksTextAreaFocusGained

    private void serviceOrdersJListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_serviceOrdersJListMouseClicked
        selectedServiceOrder = serviceOrdersJList.getSelectedValue();
        Connection connection = databaseConnectionsManager.takeConnection();
        clearForm();
        enableComponents(false);
        enableButtons(false);
        setButtonsConfiguration();
        try {
            selectedServiceOrder = ServiceOrder.getServiceOrderLastData(connection, selectedServiceOrder.getId());
            setFormComponents(selectedServiceOrder);
            lineSelectionButton.setEnabled(false);
            if (privilegedUser) {
                enableRepairingComponents(true);
                if (repairmanComboBox.getSelectedIndex() > 0)
                    repairedCheckBox.setEnabled(true);
            }
            if (repairedCheckBox.isSelected() && user.hasRole("repairs-checker"))
                enableCheckComponents(true);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, bundle.getString("RepairsInternalFrame.serviceOrderData.problem"), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            databaseConnectionsManager.returnConnection(connection);
        }

        printButton.setEnabled(true);
    }//GEN-LAST:event_serviceOrdersJListMouseClicked

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        serviceOrdersJList.clearSelection();
        selectedServiceOrder = null;
        clearForm();
        enableComponents(false);
        setButtonsConfiguration();
        enableButtons(false);
        lineSelectionButton.setEnabled(true);
    }//GEN-LAST:event_newButtonActionPerformed

    private void checkRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkRadioButtonActionPerformed
        checkRemarksTextArea.setEnabled(true);
    }//GEN-LAST:event_checkRadioButtonActionPerformed

    private void repairingTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repairingTypeComboBoxActionPerformed
        if (serviceOrdersJList.getSelectedIndex() != -1) {  // Modification.
            if (repairingTypeComboBox.getSelectedIndex() > 0) {
                if (privilegedUser) {
                    enableButtons(true);
                    enableRepairmanComponents(true);
                }
            } else {
                createButton.setEnabled(false);
                enableRepairmanComponents(false);
            }
        } else {                                            // New service order.
            printButton.setEnabled(false);
            if (repairingTypeComboBox.getSelectedIndex() > 0) {
                createButton.setEnabled(true);
                enableRepairmanComponents(true);
            } else {
                createButton.setEnabled(false);
                enableRepairmanComponents(false);
            }
        }

        printButton.setEnabled(false);
    }//GEN-LAST:event_repairingTypeComboBoxActionPerformed

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        LinkedHashMap<String, String> content;
        try {
            content = getContent(selectedServiceOrder);
            if (content != null) {
                Printer printer = new Printer();
                printer.setContent(content);
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintable(printer);
                boolean ok = job.printDialog();
                if (ok) {
                    try {
                        job.print();
                        logger.info("Service order " + selectedServiceOrder.getId() + " printed.");
                    } catch (PrinterException ex) {
                        logger.error("Couldn't print service order " + selectedServiceOrder.getId() + ".", ex);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Cannot print data.");
        }

    }//GEN-LAST:event_printButtonActionPerformed

    private LinkedHashMap<String, String> getContent(ServiceOrder serviceOrder) throws Exception {
        LinkedHashMap<String, String> content = null;
        Subscriber subscriber = serviceOrder.getSubscriber();

        Connection connection = databaseConnectionsManager.takeConnection();
        try {
            Wiring wiring = subscriber.getWiring(connection);

            content = new LinkedHashMap<>();
            Address address = subscriber.getAddress();
            Repairing lastRepairing = serviceOrder.getLastRepairing();

            content.put(bundle.getString("RepairsInternalFrame.printable.serviceOrderNumber"), String.valueOf(serviceOrder.getId()) + " (" + serviceOrder.getUser().getPerson().getFullName() + " - " + serviceOrder.getCreationTime().toString() + ")");
            content.put(bundle.getString("RepairsInternalFrame.printable.phoneNumber"), subscriber.getPhoneNumber());
            Person person = subscriber.getPerson();
            if (person != null)
                content.put(bundle.getString("RepairsInternalFrame.printable.holder"), subscriber.getPerson().getFullName());
            content.put(bundle.getString("RepairsInternalFrame.printable.address"), getAddressContent(address));
            content.put(bundle.getString("RepairsInternalFrame.printable.between"), getBetweenContent(address));
            content.put(bundle.getString("RepairsInternalFrame.printable.location"), getLocationContent(address));
            content.put(bundle.getString("RepairsInternalFrame.printable.repairingType"), bundle.getString("RepairsInternalFrame.repairingType." + serviceOrder.getRepairingType().getName()));
            content.put(bundle.getString("RepairsInternalFrame.printable.repairingRemarks"), serviceOrder.getRemarks());
            content.put(bundle.getString("RepairsInternalFrame.printable.contact"), serviceOrder.getContact());
            if (lastRepairing != null) {
                Repairman repairman = lastRepairing.getRepairman();
                content.put(bundle.getString("RepairsInternalFrame.printable.technician"), repairman.getPerson().getFullName());
                content.put(bundle.getString("RepairsInternalFrame.printable.company"), repairman.getRepairsCompany().getName());
                content.put(bundle.getString("RepairsInternalFrame.printable.repairmanRemarks"), lastRepairing.getRepairmanRemarks());
            }

            StreetPair streetPair = wiring.getStreetPair();
            String cablePair = null;
            if (streetPair != null) {
                StreetCable streetCable = streetPair.getStreetCable();
                StreetFrame streetFrame = streetCable.getStreetFrame();
                Site site = streetFrame.getSite();
                content.put(bundle.getString("RepairsInternalFrame.printable.site"), site.getName());
                cablePair = String.valueOf(streetFrame.getName()) + String.valueOf(streetCable.getName() + " " + String.format("%1$04d", streetPair.getPair()));
            }

            content.put(bundle.getString("RepairsInternalFrame.printable.technology"), subscriber.getTechnology().getName());
            BlockPosition blockPosition = wiring.getDistributor().getBlockPosition();
            SwitchBlock switchBlock = blockPosition.getSwitchBlock();
            content.put(bundle.getString("RepairsInternalFrame.printable.switchBlockPosiiton"), switchBlock.getName() + " " + blockPosition.getPosition());

            Broadband broadband = wiring.getBroadband();
            if (broadband != null) {
                StringBuilder broadbandBuilder = new StringBuilder("DSLAM: ");
                BroadbandPort broadbandPort = broadband.getBroadbandPort();
                DSLAMBoard dslamBoard = broadbandPort.getDslamBoard();
                if (dslamBoard != null) {
                    broadbandBuilder.append(DSLAMsManager.getDSLAMByBoard(dslamBoard.getId()));
//                    broadbandBuilder.append(dslamBoard.getRemarks());
                    broadbandBuilder.append(" / " + bundle.getString("RepairsInternalFrame.printable.broadbandSlot"));
                    broadbandBuilder.append(" ");
                    broadbandBuilder.append(dslamBoard.getSlot());
                } else
                    broadbandBuilder.append(DSLAMsManager.getDSLAMByBoard(broadbandPort.getDslamBoard().getId()).getName());

                broadbandBuilder.append(" / ");
                broadbandBuilder.append(bundle.getString("RepairsInternalFrame.printable.broadbandPort"));
                broadbandBuilder.append(" ");
                broadbandBuilder.append(broadbandPort.getPort());
                content.put(bundle.getString("RepairsInternalFrame.printable.broadband"), broadbandBuilder.toString());
            }

            if (cablePair != null)
                content.put(bundle.getString("RepairsInternalFrame.printable.cablePair"), cablePair);

        } catch (Exception ex) {
            logger.error("Cannot get wiring data for subscriber " + subscriber.getPhoneNumber() + ".", ex);
            JOptionPane.showMessageDialog(this, bundle.getString("RepairsInternalFrame.serviceOrderPrint.wiringProblem") + " " + subscriber.getPhoneNumber() + ".", "Error", JOptionPane.ERROR_MESSAGE);
            throw new Exception("Cannot fill content for print.");
        } finally {
            databaseConnectionsManager.returnConnection(connection);
        }

        return content;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel addressLabel;
    private javax.swing.JPanel addressPanel;
    private javax.swing.JTextField addressTextField;
    private javax.swing.JLabel betweenLabel;
    private javax.swing.JTextField betweenTextField;
    private javax.swing.JCheckBox broadbandCheckBox;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.ButtonGroup checkButtonGroup;
    private javax.swing.JPanel checkPanel;
    private javax.swing.JTextArea checkRemarksTextArea;
    private javax.swing.JRadioButton checkedRadioButton;
    private javax.swing.JLabel checkedTimeLabel;
    private javax.swing.JLabel contactLabel;
    private javax.swing.JTextField contactTextField;
    private javax.swing.JButton createButton;
    private javax.swing.JLabel holderLabel;
    private javax.swing.JTextField holderTextField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lineLabel;
    private javax.swing.JButton lineSelectionButton;
    private javax.swing.JTextField lineTextField;
    private javax.swing.JLabel locationLabel;
    private javax.swing.JTextField locationTextField;
    private javax.swing.JButton newButton;
    private javax.swing.JButton printButton;
    private javax.swing.JRadioButton rejectedRadioButton;
    private javax.swing.JScrollPane remarksScrollPane;
    private javax.swing.JCheckBox repairedCheckBox;
    private javax.swing.JLabel repairedDateLabel;
    private javax.swing.JComboBox<RepairingType> repairingTypeComboBox;
    private javax.swing.JLabel repairingTypeLabel;
    private javax.swing.JTextArea repairingTypeRemarksTextArea;
    private javax.swing.JComboBox<Repairman> repairmanComboBox;
    private javax.swing.JPanel repairmanPanel;
    private javax.swing.JTextArea repairmanRemarksTextArea;
    private javax.swing.JLabel repairsCompanyLabel;
    private javax.swing.JTextField repairsCompanyTextField;
    private javax.swing.JPanel repairsPanel;
    private javax.swing.JScrollPane repairsScrollPane;
    private javax.swing.JPanel serviceOrderPanel;
    private javax.swing.JList<ServiceOrder> serviceOrdersJList;
    private javax.swing.JPanel subscriberPanel;
    private javax.swing.JLabel technicianLabel;
    // End of variables declaration//GEN-END:variables
    private DatabaseConnectionsManager databaseConnectionsManager;
    private User user;
    private boolean privilegedUser;

    // Models.
    private DefaultListModel<ServiceOrder> serviceOrdersJListModel;
    private DefaultComboBoxModel<RepairingType> repairingTypeComboBoxModel;
    private DefaultComboBoxModel<Repairman> repairmanComboBoxModel;

    // Renderers.
    private RepairingTypeComboBoxRenderer repairingTypeComboBoxRenderer;

    // Repairing form.
    private List<ServiceOrder> serviceOrdersList;
    private Subscriber subscriber;
    private RepairingType repairingType;
    private String repairingTypeRemarks;
    private String contact;
    private String repairmanRemarks;

    // Repairing Check form.
    private String checkRemarks;

    private ServiceOrder selectedServiceOrder;

    private static boolean open;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/RepairsInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(RepairsInternalFrame.class
    );
}
