/*
 * 		WiringInternalFrame.java
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
 * 		WiringInternalFrame.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Mar 5, 2017
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.Broadband;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.BroadbandPort;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAM;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMBoard;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.managers.DSLAMsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.modem.ModemModel;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Distributor;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Site;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Equipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.features.Features;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Area;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Country;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.OfficeCode;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Phone;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.User;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetCable;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetFrame;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetPair;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.Wiring;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.BroadbandPortsAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.CablesAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.ModemsModelsAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.PhonesAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.SitesAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.PairSelection;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.SearchNumber;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.CableComboBoxRenderer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.DSLAMBoardComboBoxRenderer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.DSLAMComboBoxRenderer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.PairComboBoxRenderer;
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
public class WiringInternalFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form WiringInternalFrame
     */
    public WiringInternalFrame(User user) {
        this.user = user;
        if (user.hasRole("wiring-operator"))
            privilegedUser = true;

        createComponentsModels();
        createComponentsRenderers();

        initComponents();

        setNewFormStructure();

        Connection connection = null;
        try {
            databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
            connection = databaseConnectionsManager.takeConnection();

            sitesAnalyzer = new SitesAnalyzer();
            cablesAnalyzer = new CablesAnalyzer(connection);
            ownNumerationAnalyzer = new PhonesAnalyzer();
            broadbandPortsAnalyzer = new BroadbandPortsAnalyzer(connection);
            modemsModelsAnalyzer = new ModemsModelsAnalyzer(connection);

            fillComboBoxesModels();
            fillWiringListModel(connection);

            open = true;
        } catch (Exception ex) {
            logger.error("Cannot fill combo boxes or list.", ex);
            JOptionPane.showMessageDialog(this, bundle.getString("WiringInternalFrame.comboBoxes.problem"), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            databaseConnectionsManager.returnConnection(connection);
        }
    }

    private void createComponentsModels() {
        wiringListModel = new DefaultListModel<>();
        siteComboBoxModel = new DefaultComboBoxModel<>();
        frameComboBoxModel = new DefaultComboBoxModel<>();
        cableComboBoxModel = new DefaultComboBoxModel<>();
        pairComboBoxModel = new DefaultComboBoxModel<>();
        dslamComboBoxModel = new DefaultComboBoxModel<>();
        slotComboBoxModel = new DefaultComboBoxModel<>();
        broadbandPortComboBoxModel = new DefaultComboBoxModel<>();
        modemModelComboBoxModel = new DefaultComboBoxModel<>();
    }

    private void createComponentsRenderers() {
        cableComboBoxRenderer = new CableComboBoxRenderer();
        pairComboBoxRenderer = new PairComboBoxRenderer();
        dslamComboBoxRenderer = new DSLAMComboBoxRenderer();
        dslamBoardComboBoxRenderer = new DSLAMBoardComboBoxRenderer();
    }

    private void fillComboBoxesModels() {
        fillComboBoxModel(sitesAnalyzer.getSitesList(), siteComboBoxModel);
        fillComboBoxModel(modemsModelsAnalyzer.getModemsModelsList(), modemModelComboBoxModel);
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

    private void fillWiringListModel(Connection connection) throws Exception {
        wiringListModel.removeAllElements();
        List<Wiring> wiringList = Wiring.getWiringList(connection);
        for (Wiring wiring : wiringList) {
            wiringListModel.addElement(wiring);
        }
    }

    private boolean isValidForm() {
        boolean valid = true;

        if (((Site) siteComboBox.getSelectedItem()).getId() == 0) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.site.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            siteComboBox.requestFocus();
            valid = false;
            return valid;
        }

        if (frameComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.frame.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            frameComboBox.requestFocus();
            valid = false;
            return valid;
        }

        if (cableComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.cable.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            cableComboBox.requestFocus();
            valid = false;
            return valid;
        }

        if (pairComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.pair.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            pairComboBox.requestFocus();
            valid = false;
            return valid;
        } else {
            streetPair = (StreetPair) pairComboBox.getSelectedItem();
        }

        if (phone == null) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.subscriber.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            subscriberSearchButton.requestFocus();
            valid = false;
            return valid;
        }

        username = usernameTextField.getText().trim();
        password = passwordTextField.getText().trim();
        if (dslamComboBox.getSelectedIndex() > 0) {
            broadbandPort = (BroadbandPort) broadbandPortComboBox.getSelectedItem();
        } else {
            broadbandPort = null;
        }

        if (broadbandPort != null) {
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.username.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                usernameTextField.requestFocus();
                valid = false;
                return valid;
            }
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.password.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                passwordTextField.requestFocus();
                valid = false;
                return valid;
            }
        }

        if (!username.isEmpty()) {
            if (!internetProviderTechnology && broadbandPort == null) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.broadbandPort.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                dslamComboBox.requestFocus();
                valid = false;
                return valid;
            }
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.password.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                passwordTextField.requestFocus();
                valid = false;
                return valid;
            }
        } else {
            username = null;
        }

        if (!password.isEmpty()) {
            if (!internetProviderTechnology && broadbandPort == null) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.broadbandPort.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                dslamComboBox.requestFocus();
                valid = false;
                return valid;
            }
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.username.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                usernameTextField.requestFocus();
                valid = false;
                return valid;
            }
        } else {
            password = null;
        }

        remarks = null;
        String remarksText = remarksTextArea.getText().trim();
        if (!remarksText.equals(bundle.getString("WiringInternalFrame.remarksTextArea.text")) && !remarksText.isEmpty()) {
            if (remarksText.length() > 100) {
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

        subscribersScrollPane = new javax.swing.JScrollPane();
        wiringList = new javax.swing.JList<>();
        streetPairPanel = new javax.swing.JPanel();
        siteLabel = new javax.swing.JLabel();
        siteComboBox = new javax.swing.JComboBox<>();
        frameLabel = new javax.swing.JLabel();
        frameComboBox = new javax.swing.JComboBox<>();
        cableLabel = new javax.swing.JLabel();
        cableComboBox = new javax.swing.JComboBox<>();
        pairLabel = new javax.swing.JLabel();
        pairComboBox = new javax.swing.JComboBox<>();
        secondStreetPairLabel = new javax.swing.JLabel();
        secondStreetPairButton = new javax.swing.JButton();
        subscriberPanel = new javax.swing.JPanel();
        phoneNumberLabel = new javax.swing.JLabel();
        plusLabel = new javax.swing.JLabel();
        areaCodeOpenLabel = new javax.swing.JLabel();
        areaCodeCloseLabel = new javax.swing.JLabel();
        numberTextField = new javax.swing.JTextField();
        equipmentLabel = new javax.swing.JLabel();
        equipmentTextField = new javax.swing.JTextField();
        subscriberSearchButton = new javax.swing.JButton();
        countryCodeTextField = new javax.swing.JTextField();
        areaCodeTextField = new javax.swing.JTextField();
        broadbandPanel = new javax.swing.JPanel();
        dslamLabel = new javax.swing.JLabel();
        dslamComboBox = new javax.swing.JComboBox<>();
        slotLabel = new javax.swing.JLabel();
        slotComboBox = new javax.swing.JComboBox<>();
        portLabel = new javax.swing.JLabel();
        broadbandPortComboBox = new javax.swing.JComboBox<>();
        usernameLabel = new javax.swing.JLabel();
        usernameTextField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordTextField = new javax.swing.JTextField();
        broadbandClearButton = new javax.swing.JButton();
        modemLabel = new javax.swing.JLabel();
        modemComboBox = new javax.swing.JComboBox<>();
        buttonsPanel = new javax.swing.JPanel();
        createButton = new javax.swing.JButton();
        newButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        remarksScrollPane = new javax.swing.JScrollPane();
        remarksTextArea = new javax.swing.JTextArea();

        setClosable(true);
        setIconifiable(true);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("i18n/WiringInternalFrameBundle"); // NOI18N
        setTitle(bundle.getString("WiringInternalFrame.title")); // NOI18N
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

        wiringList.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        wiringList.setModel(wiringListModel);
        wiringList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                wiringListMouseClicked(evt);
            }
        });
        subscribersScrollPane.setViewportView(wiringList);

        streetPairPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("WiringInternalFrame.streetPairPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        siteLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        siteLabel.setText(bundle.getString("WiringInternalFrame.siteLabel.text")); // NOI18N

        siteComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        siteComboBox.setModel(siteComboBoxModel);
        siteComboBox.setEnabled(false);
        siteComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siteComboBoxActionPerformed(evt);
            }
        });

        frameLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        frameLabel.setText(bundle.getString("WiringInternalFrame.frameLabel.text")); // NOI18N

        frameComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        frameComboBox.setModel(frameComboBoxModel);
        frameComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frameComboBoxActionPerformed(evt);
            }
        });

        cableLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cableLabel.setText(bundle.getString("WiringInternalFrame.cableLabel.text")); // NOI18N

        cableComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cableComboBox.setModel(cableComboBoxModel);
        cableComboBox.setRenderer(cableComboBoxRenderer);
        cableComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cableComboBoxActionPerformed(evt);
            }
        });

        pairLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        pairLabel.setText(bundle.getString("WiringInternalFrame.pairLabel.text")); // NOI18N

        pairComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        pairComboBox.setModel(pairComboBoxModel);
        pairComboBox.setRenderer(pairComboBoxRenderer);

        secondStreetPairLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        secondStreetPairLabel.setText(bundle.getString("WiringInternalFrame.secondStreetPairLabel.text")); // NOI18N

        secondStreetPairButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        secondStreetPairButton.setText(bundle.getString("WiringInternalFrame.secondStreetPairButton.text")); // NOI18N
        secondStreetPairButton.setEnabled(false);
        secondStreetPairButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                secondStreetPairButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout streetPairPanelLayout = new javax.swing.GroupLayout(streetPairPanel);
        streetPairPanel.setLayout(streetPairPanelLayout);
        streetPairPanelLayout.setHorizontalGroup(
            streetPairPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(streetPairPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(streetPairPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(streetPairPanelLayout.createSequentialGroup()
                        .addComponent(siteLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(siteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(streetPairPanelLayout.createSequentialGroup()
                        .addComponent(frameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frameComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(streetPairPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(streetPairPanelLayout.createSequentialGroup()
                        .addComponent(cableLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cableComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pairLabel))
                    .addComponent(secondStreetPairLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(streetPairPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(streetPairPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pairComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(24, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, streetPairPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(secondStreetPairButton)
                        .addContainerGap())))
        );
        streetPairPanelLayout.setVerticalGroup(
            streetPairPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(streetPairPanelLayout.createSequentialGroup()
                .addGroup(streetPairPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(secondStreetPairLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(streetPairPanelLayout.createSequentialGroup()
                        .addGroup(streetPairPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(siteLabel)
                            .addComponent(siteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(streetPairPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(secondStreetPairButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(streetPairPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frameLabel)
                    .addComponent(frameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cableLabel)
                    .addComponent(cableComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pairLabel)
                    .addComponent(pairComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        subscriberPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("WiringInternalFrame.subscriberPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        phoneNumberLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        phoneNumberLabel.setText(bundle.getString("WiringInternalFrame.phoneNumberLabel.text")); // NOI18N

        plusLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        plusLabel.setText(bundle.getString("WiringInternalFrame.plusLabel.text")); // NOI18N

        areaCodeOpenLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        areaCodeOpenLabel.setText(bundle.getString("WiringInternalFrame.areaCodeOpenLabel.text")); // NOI18N

        areaCodeCloseLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        areaCodeCloseLabel.setText(bundle.getString("WiringInternalFrame.areaCodeCloseLabel.text")); // NOI18N

        numberTextField.setEditable(false);
        numberTextField.setText(bundle.getString("WiringInternalFrame.numberTextField.text")); // NOI18N

        equipmentLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        equipmentLabel.setText(bundle.getString("WiringInternalFrame.equipmentLabel.text")); // NOI18N

        equipmentTextField.setEditable(false);
        equipmentTextField.setText(bundle.getString("WiringInternalFrame.equipmentTextField.text")); // NOI18N

        subscriberSearchButton.setText(bundle.getString("WiringInternalFrame.subscriberSearchButton.text")); // NOI18N
        subscriberSearchButton.setEnabled(false);
        subscriberSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subscriberSearchButtonActionPerformed(evt);
            }
        });

        countryCodeTextField.setEditable(false);
        countryCodeTextField.setText(bundle.getString("WiringInternalFrame.countryCodeTextField.text")); // NOI18N

        areaCodeTextField.setEditable(false);
        areaCodeTextField.setText(bundle.getString("WiringInternalFrame.areaCodeTextField.text")); // NOI18N

        javax.swing.GroupLayout subscriberPanelLayout = new javax.swing.GroupLayout(subscriberPanel);
        subscriberPanel.setLayout(subscriberPanelLayout);
        subscriberPanelLayout.setHorizontalGroup(
            subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subscriberPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(subscriberPanelLayout.createSequentialGroup()
                        .addComponent(phoneNumberLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(plusLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(countryCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(areaCodeOpenLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(areaCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8))
                    .addGroup(subscriberPanelLayout.createSequentialGroup()
                        .addComponent(equipmentLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(equipmentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(subscriberPanelLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(areaCodeCloseLabel)
                        .addGap(24, 24, 24)
                        .addComponent(numberTextField))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, subscriberPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(subscriberSearchButton)))
                .addContainerGap())
        );
        subscriberPanelLayout.setVerticalGroup(
            subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subscriberPanelLayout.createSequentialGroup()
                .addGroup(subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phoneNumberLabel)
                    .addComponent(plusLabel)
                    .addComponent(areaCodeOpenLabel)
                    .addComponent(areaCodeCloseLabel)
                    .addComponent(numberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(countryCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(areaCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(equipmentLabel)
                    .addComponent(equipmentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subscriberSearchButton)))
        );

        broadbandPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("WiringInternalFrame.broadbandPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        dslamLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        dslamLabel.setText(bundle.getString("WiringInternalFrame.dslamLabel.text")); // NOI18N

        dslamComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        dslamComboBox.setModel(dslamComboBoxModel);
        dslamComboBox.setEnabled(false);
        dslamComboBox.setRenderer(dslamComboBoxRenderer);
        dslamComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dslamComboBoxActionPerformed(evt);
            }
        });

        slotLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        slotLabel.setText(bundle.getString("WiringInternalFrame.slotLabel.text")); // NOI18N

        slotComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        slotComboBox.setModel(slotComboBoxModel);
        slotComboBox.setEnabled(false);
        slotComboBox.setRenderer(dslamBoardComboBoxRenderer);
        slotComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slotComboBoxActionPerformed(evt);
            }
        });

        portLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        portLabel.setText(bundle.getString("WiringInternalFrame.portLabel.text")); // NOI18N

        broadbandPortComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        broadbandPortComboBox.setModel(broadbandPortComboBoxModel);
        broadbandPortComboBox.setEnabled(false);

        usernameLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        usernameLabel.setText(bundle.getString("WiringInternalFrame.usernameLabel.text")); // NOI18N

        usernameTextField.setText(bundle.getString("WiringInternalFrame.usernameTextField.text")); // NOI18N
        usernameTextField.setEnabled(false);

        passwordLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        passwordLabel.setText(bundle.getString("WiringInternalFrame.passwordLabel.text")); // NOI18N

        passwordTextField.setText(bundle.getString("WiringInternalFrame.passwordTextField.text")); // NOI18N
        passwordTextField.setEnabled(false);

        broadbandClearButton.setText(bundle.getString("WiringInternalFrame.broadbandClearButton.text")); // NOI18N
        broadbandClearButton.setEnabled(false);
        broadbandClearButton.setMaximumSize(new java.awt.Dimension(68, 27));
        broadbandClearButton.setMinimumSize(new java.awt.Dimension(68, 27));
        broadbandClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                broadbandClearButtonActionPerformed(evt);
            }
        });

        modemLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        modemLabel.setText(bundle.getString("WiringInternalFrame.modemLabel.text")); // NOI18N

        modemComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        modemComboBox.setModel(modemModelComboBoxModel);
        modemComboBox.setEnabled(false);

        javax.swing.GroupLayout broadbandPanelLayout = new javax.swing.GroupLayout(broadbandPanel);
        broadbandPanel.setLayout(broadbandPanelLayout);
        broadbandPanelLayout.setHorizontalGroup(
            broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(broadbandPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(broadbandPanelLayout.createSequentialGroup()
                        .addComponent(dslamLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dslamComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(slotLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(slotComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(portLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(broadbandPortComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(broadbandPanelLayout.createSequentialGroup()
                        .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(broadbandPanelLayout.createSequentialGroup()
                                .addComponent(modemLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(modemComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, broadbandPanelLayout.createSequentialGroup()
                                .addComponent(usernameLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(passwordLabel)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(passwordTextField)
                            .addGroup(broadbandPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(broadbandClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        broadbandPanelLayout.setVerticalGroup(
            broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(broadbandPanelLayout.createSequentialGroup()
                .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dslamLabel)
                    .addComponent(dslamComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(slotLabel)
                    .addComponent(slotComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portLabel)
                    .addComponent(broadbandPortComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(usernameLabel)
                    .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(broadbandClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(modemLabel)
                    .addComponent(modemComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        createButton.setText(bundle.getString("WiringInternalFrame.createButton.text")); // NOI18N
        createButton.setEnabled(false);
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        newButton.setText(bundle.getString("WiringInternalFrame.newButton.text")); // NOI18N
        newButton.setEnabled(false);
        newButton.setMaximumSize(new java.awt.Dimension(68, 27));
        newButton.setMinimumSize(new java.awt.Dimension(68, 27));
        newButton.setPreferredSize(new java.awt.Dimension(68, 27));
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        removeButton.setText(bundle.getString("WiringInternalFrame.removeButton.text")); // NOI18N
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
                .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        remarksTextArea.setColumns(20);
        remarksTextArea.setForeground(new java.awt.Color(204, 204, 204));
        remarksTextArea.setRows(5);
        remarksTextArea.setText(bundle.getString("WiringInternalFrame.remarksTextArea.text")); // NOI18N
        remarksTextArea.setEnabled(false);
        remarksTextArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                remarksTextAreaFocusGained(evt);
            }
        });
        remarksScrollPane.setViewportView(remarksTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subscribersScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(subscriberPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(broadbandPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(remarksScrollPane)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(streetPairPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subscribersScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(streetPairPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(subscriberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(broadbandPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(remarksScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void siteComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siteComboBoxActionPerformed
        frameComboBoxModel.removeAllElements();
        dslamComboBoxModel.removeAllElements();

        phone = null;
        distributor = null;
        fillSubscriberComponents();
        subscriberSearchButton.setEnabled(false);

        enableStreetPairComponents(false);

        remarksTextArea.setForeground(new java.awt.Color(204, 204, 204));
        remarksTextArea.setText(bundle.getString("WiringInternalFrame.remarksTextArea.text"));

        if (siteComboBox.getSelectedIndex() > 0) {
            int selectedSiteId = ((Site) siteComboBox.getSelectedItem()).getId();
            fillComboBoxModel(cablesAnalyzer.getStreetFramesList(selectedSiteId), frameComboBoxModel);
            enableStreetPairComponents(true);

            subscriberSearchButton.setEnabled(true);
        }

        enableBroadbandComponents();
    }//GEN-LAST:event_siteComboBoxActionPerformed

    private void frameComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frameComboBoxActionPerformed
        cableComboBoxModel.removeAllElements();

        List<StreetCable> streetCablesList = new ArrayList<>();
        if (frameComboBox.getSelectedItem() != null) {
            int selectedFrameId = ((StreetFrame) frameComboBox.getSelectedItem()).getId();
            if (selectedFrameId != 0) {
                streetCablesList = cablesAnalyzer.getStreetCablesList(selectedFrameId);
            } else {
                StreetCable selectCable = new StreetCable();
                selectCable.setName(bundle.getString("WiringInternalFrame.Select.short").charAt(0));
                streetCablesList.add(selectCable);
                for (StreetCable streetCable : cablesAnalyzer.getStreetCablesList()) {
                    streetCablesList.add(streetCable);
                }
            }
        }

        fillComboBoxModel(streetCablesList, cableComboBoxModel);
    }//GEN-LAST:event_frameComboBoxActionPerformed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        open = false;
    }//GEN-LAST:event_formInternalFrameClosed

    private void cableComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cableComboBoxActionPerformed
        pairComboBoxModel.removeAllElements();
        StreetCable selectedCable = (StreetCable) cableComboBox.getSelectedItem();
        if (selectedCable != null && selectedCable.getName() != bundle.getString("WiringInternalFrame.Select.short").charAt(0)) {
            fillComboBoxModel(cablesAnalyzer.getFreeStreetPairsList(selectedCable.getId()), pairComboBoxModel);
        }
    }//GEN-LAST:event_cableComboBoxActionPerformed

    private void subscriberSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subscriberSearchButtonActionPerformed
        SearchNumber searchNumber = new SearchNumber(this, ownNumerationAnalyzer);
        phone = searchNumber.search(((Site) siteComboBox.getSelectedItem()).getId());
        fillSubscriberComponents();
    }//GEN-LAST:event_subscriberSearchButtonActionPerformed

    private void dslamComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dslamComboBoxActionPerformed
        slotComboBoxModel.removeAllElements();

        List<DSLAMBoard> dslamBoardsList = new ArrayList<>();
        if ((dslamComboBox.getSelectedItem()) != null) {
            int dslamId = ((DSLAM) dslamComboBox.getSelectedItem()).getId();
            if (dslamId != 0) {
                dslamBoardsList = broadbandPortsAnalyzer.getDSLAMBoardsList(dslamId);
            } else {
                DSLAMBoard selectDSLAMBoard = new DSLAMBoard();
                selectDSLAMBoard.setSlot(0);
                selectDSLAMBoard.setId(0);
                dslamBoardsList.add(selectDSLAMBoard);
                for (DSLAMBoard dslamBoard : broadbandPortsAnalyzer.getDSLAMsBoardsList()) {
                    dslamBoardsList.add(dslamBoard);
                }
            }
        }
        fillComboBoxModel(dslamBoardsList, slotComboBoxModel);
    }//GEN-LAST:event_dslamComboBoxActionPerformed

    private void slotComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slotComboBoxActionPerformed
        broadbandPortComboBoxModel.removeAllElements();

        if (slotComboBox.getSelectedItem() == null) {
            DSLAM selectedDSLAM = (DSLAM) dslamComboBox.getSelectedItem();
            if (selectedDSLAM != null && broadbandPortsAnalyzer.getDSLAMBoardsList(selectedDSLAM.getId()) == null) {
                fillComboBoxModel(broadbandPortsAnalyzer.getFreeBroadbandPortsListByDSLAM(selectedDSLAM.getId()), broadbandPortComboBoxModel);
            }
        } else {
            DSLAMBoard selectedDSLAMBoard = (DSLAMBoard) slotComboBox.getSelectedItem();
            int selectedDSLAMBoardId = selectedDSLAMBoard.getId();
            if (selectedDSLAMBoardId != 0) {
                fillComboBoxModel(broadbandPortsAnalyzer.getFreeBroadbandPortsListByBoard(selectedDSLAMBoardId), broadbandPortComboBoxModel);
            }
        }
    }//GEN-LAST:event_slotComboBoxActionPerformed

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        if (isValidForm()) {
            Connection connection = databaseConnectionsManager.takeConnection();
            if (wiringList.getSelectedIndex() == -1) {
                Wiring wiring = new Wiring();
                fillWiringData(wiring);
                try {
                    wiring.insert(connection, user.getId());
                    JOptionPane.showMessageDialog(this, phone + " " + bundle.getString("InsertDialog.confirmation"), bundle.getString("InsertDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    renewFormData();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this, phone + " " + bundle.getString("InsertDialog.error"), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                }
            } else {
                Wiring selectedWiring = wiringList.getSelectedValue();
                fillWiringData(selectedWiring);
                try {
                    selectedWiring.update(connection, user.getId());
                    JOptionPane.showMessageDialog(this, phone + " " + bundle.getString("ModifyDialog.confirmation"), bundle.getString("ModifyDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    renewFormData();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this, phone + " " + bundle.getString("ModifyDialog.error"), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                }
            }
        }
    }//GEN-LAST:event_createButtonActionPerformed

    private void fillWiringData(Wiring wiring) {
        distributor.getBlockPosition().getSwitchBlock().setSite((Site) siteComboBox.getSelectedItem());
        wiring.setDistributor(distributor);
        wiring.setStreetPair(streetPair);

        Broadband broadband = null;
        if (username != null) {
            broadband = new Broadband();
            broadband.setUsername(username);
            broadband.setPassword(password);
            broadband.setBroadbandPort(broadbandPort);

            ModemModel selectedModem = (ModemModel) modemComboBox.getSelectedItem();
            if (selectedModem.getId() != 0) {
                broadband.setModemModel(selectedModem);
            }
        }
        wiring.setBroadband(broadband);

        wiring.setRemarks(remarks);
    }

    private void renewFormData() throws Exception {
        Connection connection = null;
        try {
            databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
            connection = databaseConnectionsManager.takeConnection();

            clearForm();
            fillWiringListModel(connection);
            setNewFormStructure();

            cablesAnalyzer = new CablesAnalyzer(connection);
            broadbandPortsAnalyzer = new BroadbandPortsAnalyzer(connection);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("Cannot renew form data.");
        } finally {
            databaseConnectionsManager.returnConnection(connection);
        }
    }

    private void clearForm() {
        siteComboBox.setSelectedIndex(0);
        siteComboBox.setEnabled(true);

        frameComboBoxModel.removeAllElements();
        cableComboBoxModel.removeAllElements();
        pairComboBoxModel.removeAllElements();
        secondStreetPairLabel.setText("");
        secondStreetPairButton.setText(bundle.getString("WiringInternalFrame.secondStreetPairButton.text"));
        secondStreetPairButton.setEnabled(false);

        countryCodeTextField.setText("");
        areaCodeTextField.setText("");
        numberTextField.setText("");
        equipmentTextField.setText("");

        dslamComboBoxModel.removeAllElements();
        slotComboBoxModel.removeAllElements();
        broadbandPortComboBoxModel.removeAllElements();
        usernameTextField.setText("");
        passwordTextField.setText("");
        modemComboBox.setSelectedIndex(0);

        remarksTextArea.setForeground(new java.awt.Color(204, 204, 204));
        remarksTextArea.setText(bundle.getString("WiringInternalFrame.remarksTextArea.text"));
    }

    private void broadbandClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_broadbandClearButtonActionPerformed
        clearUserComponents();
        enableBroadbandComponents();
        usernameTextField.requestFocus();
    }//GEN-LAST:event_broadbandClearButtonActionPerformed

    private void remarksTextAreaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remarksTextAreaFocusGained
        if (remarksTextArea.getText().equals(bundle.getString("WiringInternalFrame.remarksTextArea.text")))
            remarksTextArea.setText("");
        remarksTextArea.setForeground(Color.BLACK);
    }//GEN-LAST:event_remarksTextAreaFocusGained

    private void wiringListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_wiringListMouseClicked
        clearForm();
        Wiring selectedWiring = wiringList.getSelectedValue();

        StreetPair selectedStreetPair = selectedWiring.getStreetPair();
        StreetCable selectedStreetCable = selectedStreetPair.getStreetCable();
        StreetFrame selectedStreetFrame = selectedStreetCable.getStreetFrame();
        Site selectedSite = selectedStreetFrame.getSite();

        siteComboBoxModel.setSelectedItem(selectedSite);
        siteComboBox.setEnabled(false);

        frameComboBoxModel.removeAllElements();
        fillComboBoxModel(cablesAnalyzer.getStreetFramesList(selectedSite.getId()), frameComboBoxModel);
        enableStreetPairComponents(true);

        frameComboBoxModel.setSelectedItem(selectedStreetFrame);
        cableComboBoxModel.setSelectedItem(selectedStreetCable);
        pairComboBoxModel.setSelectedItem(selectedStreetPair);
        setSecondPairComponents(selectedWiring);

        phone = selectedWiring.getSubscriber().getAddress().getPhone();
        Area selectedArea = phone.getOfficeCode().getArea();
        Country selectedCountry = selectedArea.getCountry();
        countryCodeTextField.setText(String.valueOf(selectedCountry.getCode()));
        areaCodeTextField.setText(String.valueOf(selectedArea.getCode()));
        numberTextField.setText(phone.toString());

        distributor = selectedWiring.getDistributor();
        Equipment selectedEquipment = distributor.getEquipment();
        equipmentTextField.setText(selectedEquipment.toString());
        try {
            internetProviderTechnology = Features.getInstance().getFeaturesList("internetProviderTechnology").contains(selectedEquipment.getClass().getSimpleName());
        } catch (Exception ex) {
            logger.error("Cannot read Internet providers technologies.", ex);
            JOptionPane.showMessageDialog(this, bundle.getString("WiringInternalFrame.internetProviderTechnology.problem"), "Error", JOptionPane.ERROR_MESSAGE);
        }

        Broadband selectedBroadband = selectedWiring.getBroadband();
        if (selectedBroadband != null) {
            BroadbandPort selectedBroadbandPort = selectedBroadband.getBroadbandPort();
            DSLAMBoard selectedDSLAMBoard = selectedBroadbandPort.getDslamBoard();
            DSLAM selectedDSLAM = DSLAMsManager.getDSLAMByBoard(selectedDSLAMBoard.getId());
            dslamComboBoxModel.setSelectedItem(selectedDSLAM);
            if (selectedDSLAMBoard.getSlot() != 0) {
                slotComboBoxModel.setSelectedItem(selectedDSLAMBoard);
            }
            broadbandPortComboBoxModel.setSelectedItem(selectedBroadbandPort);
            enableDSLAMComponents(true);
            usernameTextField.setText(selectedBroadband.getUsername());
            passwordTextField.setText(selectedBroadband.getPassword());
            enableUserComponents(true);

            ModemModel selectedModemModel = selectedBroadband.getModemModel();
            if (selectedModemModel != null) {
                modemModelComboBoxModel.setSelectedItem(selectedModemModel);
            }
        }

        remarksTextArea.setForeground(Color.BLACK);
        remarksTextArea.setText(selectedWiring.getRemarks());

        setModifyFormStructure();
    }//GEN-LAST:event_wiringListMouseClicked

    private void setSecondPairComponents(Wiring selectedWiring) {
        StreetPair selectedSecondStreetPair = selectedWiring.getSecondStreetPair();
        if (selectedSecondStreetPair != null) {
            StreetCable selectedSecondStreetCable = selectedSecondStreetPair.getStreetCable();
            StreetFrame selectedSecondStreetFrame = selectedSecondStreetCable.getStreetFrame();

            StringBuilder secondStreetPair = new StringBuilder(bundle.getString("WiringInternalFrame.secondStreetPair.text"));
            secondStreetPair.append(" ");
            secondStreetPair.append(selectedSecondStreetFrame.getName());
            secondStreetPair.append(selectedSecondStreetCable.getName());
            secondStreetPair.append(String.format("%04d", selectedSecondStreetPair.getPair()));
            secondStreetPairLabel.setText(secondStreetPair.toString());

            secondStreetPairButton.setText(bundle.getString("WiringInternalFrame.secondStreetPairButton.Remove.text"));
        } else {
            secondStreetPairLabel.setText("");
            secondStreetPairButton.setText(bundle.getString("WiringInternalFrame.secondStreetPairButton.text"));
        }
        secondStreetPairButton.setEnabled(true);
    }

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        clearForm();
        setNewFormStructure();
    }//GEN-LAST:event_newButtonActionPerformed

    private void setNewFormStructure() {
        wiringList.clearSelection();
        createButton.setText(bundle.getString("WiringInternalFrame.createButton.text"));
        if (privilegedUser) {
            siteComboBox.setEnabled(true);
            createButton.setEnabled(true);
            remarksTextArea.setEnabled(true);
        }

        newButton.setEnabled(false);
        removeButton.setEnabled(false);
    }

    private void setModifyFormStructure() {
        createButton.setText(bundle.getString("WiringInternalFrame.modifyButton.text"));
        subscriberSearchButton.setEnabled(false);
        newButton.setEnabled(true);
        if (privilegedUser) {
            removeButton.setEnabled(true);
        }
    }

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        if (isValidForm()) {
            int response = JOptionPane.showConfirmDialog(this, bundle.getString("DeleteDialog.answer") + " " + phone + "?", bundle.getString("DeleteDialog.title"), JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                Connection connection = databaseConnectionsManager.takeConnection();
                Wiring selectedWiring = wiringList.getSelectedValue();
                fillWiringData(selectedWiring);
                try {
                    selectedWiring.delete(connection, user.getId());
                    JOptionPane.showMessageDialog(this, phone + " " + bundle.getString("DeleteDialog.confirmation"), bundle.getString("DeleteDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    renewFormData();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this, phone + " " + bundle.getString("DeleteDialog.error"), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                }
            }
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    private void secondStreetPairButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_secondStreetPairButtonActionPerformed
        Wiring selectedWiring = wiringList.getSelectedValue();
        StreetPair secondStreetPair = selectedWiring.getSecondStreetPair();
        Connection connection;
        if (secondStreetPair == null) {
            PairSelection pairSelection = new PairSelection(this);
            StreetPair selectedSecondStreetPair = pairSelection.getStreetPair(selectedWiring.getStreetPair().getStreetCable().getStreetFrame().getSite().getId(), cablesAnalyzer);

            if (selectedSecondStreetPair != null) {
                selectedWiring.setSecondStreetPair(selectedSecondStreetPair);
                connection = databaseConnectionsManager.takeConnection();
                try {
                    selectedWiring.insertSecondWiring(connection, user.getId());
                    JOptionPane.showMessageDialog(this, selectedSecondStreetPair + " " + bundle.getString("InserSecondPairDialog.confirmation") + " " + phone + ".", bundle.getString("InserSecondPairDialog.tile"), JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this, selectedSecondStreetPair + " " + bundle.getString("InserSecondPairDialog.error") + " " + phone + ".", "Error", JOptionPane.ERROR_MESSAGE);
                    selectedWiring.setSecondStreetPair(null);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                }
            }
        } else {
            int response = JOptionPane.showConfirmDialog(this, bundle.getString("DeleteSecondPairDialog.answer") + " " + phone + "?", bundle.getString("DeleteSecondPairDialog.title"), JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                connection = databaseConnectionsManager.takeConnection();
                try {
                    selectedWiring.deleteSecondWiring(connection, user.getId());
                    selectedWiring.setSecondStreetPair(null);
                    JOptionPane.showMessageDialog(this, bundle.getString("DeleteSecondPairDialog.confirmation") + " " + phone + ".", bundle.getString("DeleteSecondPairDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this, bundle.getString("DeleteSecondPairDialog.error") + " " + phone + ".", "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                }
            }
        }
        setSecondPairComponents(selectedWiring);
    }//GEN-LAST:event_secondStreetPairButtonActionPerformed

    private void enableBroadbandComponents() {
        enableDSLAMComponents(false);
        enableUserComponents(false);
        if (siteComboBox.getSelectedIndex() > 0) {
            if (distributor != null && internetProviderTechnology)
                enableUserComponents(true);
            else {
                int selectedSiteId = ((Site) siteComboBox.getSelectedItem()).getId();
                List<DSLAM> dslamsList = broadbandPortsAnalyzer.getDSLAMsList(selectedSiteId);
                if (dslamsList.size() > 0) {
                    enableDSLAMComponents(true);
                    enableUserComponents(true);
                    dslamComboBox.removeAllItems();
                    fillComboBoxModel(dslamsList, dslamComboBoxModel);
                }
            }
        }
    }

    private void enableDSLAMComponents(boolean enabled) {
        dslamComboBox.setEnabled(enabled);
        slotComboBox.setEnabled(enabled);
        broadbandPortComboBox.setEnabled(enabled);
    }

    private void enableUserComponents(boolean enabled) {
        usernameTextField.setEnabled(enabled);
        passwordTextField.setEnabled(enabled);
        modemComboBox.setEnabled(enabled);
        if (privilegedUser) {
            broadbandClearButton.setEnabled(enabled);
        }
    }

    private void clearUserComponents() {
        usernameTextField.setText("");
        passwordTextField.setText("");
        if (modemModelComboBoxModel.getSize() > 0) {
            modemComboBox.setSelectedIndex(0);
        }
    }

    private void fillSubscriberComponents() {
        if (phone == null) {
            countryCodeTextField.setText("");
            areaCodeTextField.setText("");
            numberTextField.setText("");
            equipmentTextField.setText("");
        } else {
            OfficeCode selectedOfficeCode = phone.getOfficeCode();
            Area selectedArea = selectedOfficeCode.getArea();
            Country selectedCountry = selectedArea.getCountry();
            countryCodeTextField.setText(String.valueOf(selectedCountry.getCode()));
            areaCodeTextField.setText(String.valueOf(selectedArea.getCode()));
            numberTextField.setText(phone.toString());

            Connection connection = null;
            try {
                databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
                connection = databaseConnectionsManager.takeConnection();

                distributor = Distributor.getDistributor(connection, selectedOfficeCode.getId(), phone.getNumber());
                Equipment equipment = distributor.getEquipment();
                equipmentTextField.setText(String.valueOf(equipment.toString()));
                internetProviderTechnology = Features.getInstance().getFeaturesList("internetProviderTechnology").contains(distributor.getEquipment().getClass().getSimpleName());
            } catch (Exception ex) {
                logger.error("Cannot get distributor data.", ex);
                JOptionPane.showMessageDialog(this, bundle.getString("WiringInternalFrame.equipmentData.problem"), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }
    }

    private void enableStreetPairComponents(boolean enabled) {
        frameComboBox.setEnabled(enabled);
        cableComboBox.setEnabled(enabled);
        pairComboBox.setEnabled(enabled);
    }

    public static boolean isOpen() {
        return open;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel areaCodeCloseLabel;
    private javax.swing.JLabel areaCodeOpenLabel;
    private javax.swing.JTextField areaCodeTextField;
    private javax.swing.JButton broadbandClearButton;
    private javax.swing.JPanel broadbandPanel;
    private javax.swing.JComboBox<BroadbandPort> broadbandPortComboBox;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JComboBox<StreetCable> cableComboBox;
    private javax.swing.JLabel cableLabel;
    private javax.swing.JTextField countryCodeTextField;
    private javax.swing.JButton createButton;
    private javax.swing.JComboBox<DSLAM> dslamComboBox;
    private javax.swing.JLabel dslamLabel;
    private javax.swing.JLabel equipmentLabel;
    private javax.swing.JTextField equipmentTextField;
    private javax.swing.JComboBox<StreetFrame> frameComboBox;
    private javax.swing.JLabel frameLabel;
    private javax.swing.JComboBox<ModemModel> modemComboBox;
    private javax.swing.JLabel modemLabel;
    private javax.swing.JButton newButton;
    private javax.swing.JTextField numberTextField;
    private javax.swing.JComboBox<StreetPair> pairComboBox;
    private javax.swing.JLabel pairLabel;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JTextField passwordTextField;
    private javax.swing.JLabel phoneNumberLabel;
    private javax.swing.JLabel plusLabel;
    private javax.swing.JLabel portLabel;
    private javax.swing.JScrollPane remarksScrollPane;
    private javax.swing.JTextArea remarksTextArea;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton secondStreetPairButton;
    private javax.swing.JLabel secondStreetPairLabel;
    private javax.swing.JComboBox<Site> siteComboBox;
    private javax.swing.JLabel siteLabel;
    private javax.swing.JComboBox<DSLAMBoard> slotComboBox;
    private javax.swing.JLabel slotLabel;
    private javax.swing.JPanel streetPairPanel;
    private javax.swing.JPanel subscriberPanel;
    private javax.swing.JButton subscriberSearchButton;
    private javax.swing.JScrollPane subscribersScrollPane;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTextField usernameTextField;
    private javax.swing.JList<Wiring> wiringList;
    // End of variables declaration//GEN-END:variables

    private DatabaseConnectionsManager databaseConnectionsManager;
    private User user;
    private boolean privilegedUser;
    private static boolean open;
    private SitesAnalyzer sitesAnalyzer;
    private CablesAnalyzer cablesAnalyzer;
    private PhonesAnalyzer ownNumerationAnalyzer;
    private BroadbandPortsAnalyzer broadbandPortsAnalyzer;
    private ModemsModelsAnalyzer modemsModelsAnalyzer;

    // Models.
    private DefaultListModel<Wiring> wiringListModel;
    private DefaultComboBoxModel<Site> siteComboBoxModel;
    private DefaultComboBoxModel<StreetFrame> frameComboBoxModel;
    private DefaultComboBoxModel<StreetCable> cableComboBoxModel;
    private DefaultComboBoxModel<StreetPair> pairComboBoxModel;
    private DefaultComboBoxModel<DSLAM> dslamComboBoxModel;
    private DefaultComboBoxModel<DSLAMBoard> slotComboBoxModel;
    private DefaultComboBoxModel<BroadbandPort> broadbandPortComboBoxModel;
    private DefaultComboBoxModel<ModemModel> modemModelComboBoxModel;

    // Renderers.
    private CableComboBoxRenderer cableComboBoxRenderer;
    private PairComboBoxRenderer pairComboBoxRenderer;
    private DSLAMComboBoxRenderer dslamComboBoxRenderer;
    private DSLAMBoardComboBoxRenderer dslamBoardComboBoxRenderer;

    // Form data.
    private StreetPair streetPair;
    private Phone phone;
    private Distributor distributor;
    private boolean internetProviderTechnology;
    private String username;
    private String password;
    private BroadbandPort broadbandPort;
    private String remarks;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/WiringInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(WiringInternalFrame.class);
}
