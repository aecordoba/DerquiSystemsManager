/*
 * 		BroadbandInternalFrame.java
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
 * 		BroadbandInternalFrame.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Oct 25, 2019
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.Broadband;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.BroadbandPort;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAM;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMBoard;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.managers.DSLAMsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.router.Router;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Site;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Area;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Country;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.OfficeCode;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Phone;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.state.SubscriberBroadbandState;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.User;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.Wiring;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.BroadbandPortsAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.PhonesAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.SitesAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.DSLAMBoardComboBoxRenderer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.DSLAMComboBoxRenderer;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class BroadbandInternalFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form BroadbandInternalFrame
     */
    public BroadbandInternalFrame(User user) {
        this.user = user;

        createComponentsModels();
        createComponentsRenderers();

        initComponents();

        Connection connection = null;
        try {
            databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
            connection = databaseConnectionsManager.takeConnection();

            sitesAnalyzer = new SitesAnalyzer();
            ownNumerationAnalyzer = new PhonesAnalyzer();
            broadbandPortsAnalyzer = new BroadbandPortsAnalyzer(connection);

            fillComboBoxModel(sitesAnalyzer.getSitesList(), siteComboBoxModel);
            setCountryCodeTextField();
            setAreaCodeTextField();

            isDisplayingData = false;
            open = true;
        } catch (Exception ex) {
            logger.error("Cannot fill combo boxes.", ex);
            JOptionPane.showMessageDialog(this, bundle.getString("BroadbandInternalFrame.comboBoxes.problem"), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            databaseConnectionsManager.returnConnection(connection);
        }

    }

    private void createComponentsModels() {
        siteComboBoxModel = new DefaultComboBoxModel<>();
        dslamComboBoxModel = new DefaultComboBoxModel<>();
        slotComboBoxModel = new DefaultComboBoxModel<>();
        broadbandPortComboBoxModel = new DefaultComboBoxModel<>();
    }

    private void createComponentsRenderers() {
        dslamComboBoxRenderer = new DSLAMComboBoxRenderer();
        dslamBoardComboBoxRenderer = new DSLAMBoardComboBoxRenderer();
    }

    private void setCountryCodeTextField() {
        List<Country> ownCountriesList = ownNumerationAnalyzer.getOwnCountriesList();
        if (ownCountriesList.size() == 2) {
            country = ownCountriesList.get(1);
            countryCodeTextField.setText(String.valueOf(country.getCode()));
        } else
            countryCodeTextField.setEditable(true);
    }

    private void setAreaCodeTextField() {
        List<Area> ownAreasList = ownNumerationAnalyzer.getOwnAreasList();
        if (ownAreasList.size() == 2) {
            area = ownAreasList.get(1);
            areaCodeTextField.setText(String.valueOf(area.getCode()));
        } else
            areaCodeTextField.setEditable(true);
    }

    private boolean isValidForm() {
        boolean valid = true;

        // Number text field validation.
        String numberString = numberTextField.getText();
        if (!numberString.isEmpty()) {
            try {
                String[] numbersArray = new String[2];

                if ((numberString.contains("-") && numberString.split("-").length == 2) || !numberString.contains("-")) {
                    if (numberString.contains("-"))
                        numbersArray = numberString.split("-");
                    else {
                        numbersArray[0] = numberString.substring(0, numberString.length() - 4);
                        numbersArray[1] = numberString.substring(numberString.length() - 4);
                    }
                    Integer.parseInt(numbersArray[1]);

                    List<OfficeCode> ownOfficeCodesList = ownNumerationAnalyzer.getOwnOfficeCodesList(area.getId());
                    for (OfficeCode temp : ownOfficeCodesList) {
                        if (Integer.parseInt(numbersArray[0]) == temp.getCode()) {
                            officeCode = temp;
                            break;
                        }
                    }

                    if (officeCode != null && numbersArray[1].length() == 4) {
                        mcdu = numbersArray[1];
                    } else {
                        JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.number.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                        valid = false;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.number.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    valid = false;
                }

            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.number.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                valid = false;
            }
        } else if (dslamComboBox.getSelectedIndex() > 0)
            broadbandPort = (BroadbandPort) broadbandPortComboBox.getSelectedItem();

        return valid;
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        stateButtonGroup = new javax.swing.ButtonGroup();
        subscriberPanel = new javax.swing.JPanel();
        numberLabel = new javax.swing.JLabel();
        plusLabel = new javax.swing.JLabel();
        countryCodeTextField = new javax.swing.JTextField();
        areaCodeOpenLabel = new javax.swing.JLabel();
        areaCodeTextField = new javax.swing.JTextField();
        areaCodeCloseLabel = new javax.swing.JLabel();
        numberTextField = new javax.swing.JTextField();
        broadbandPanel = new javax.swing.JPanel();
        siteLabel = new javax.swing.JLabel();
        siteComboBox = new javax.swing.JComboBox<>();
        dslamLabel = new javax.swing.JLabel();
        dslamComboBox = new javax.swing.JComboBox<>();
        slotLabel = new javax.swing.JLabel();
        slotComboBox = new javax.swing.JComboBox<>();
        portLabel = new javax.swing.JLabel();
        broadbandPortComboBox = new javax.swing.JComboBox<>();
        routerLabel = new javax.swing.JLabel();
        routerTextField = new javax.swing.JTextField();
        enabledRadioButton = new javax.swing.JRadioButton();
        debtorRadioButton = new javax.swing.JRadioButton();
        usernameLabel = new javax.swing.JLabel();
        usernameTextField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordTextField = new javax.swing.JTextField();
        disabledRadioButton = new javax.swing.JRadioButton();
        buttonsPanel = new javax.swing.JPanel();
        searchButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("i18n/BroadbandInternalFrameBundle"); // NOI18N
        setTitle(bundle.getString("BroadbandInternalFrame.title")); // NOI18N
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

        subscriberPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("BroadbandInternalFrame.subscriberPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        numberLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        numberLabel.setText(bundle.getString("BroadbandInternalFrame.numberLabel.text")); // NOI18N

        plusLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        plusLabel.setText(bundle.getString("BroadbandInternalFrame.plusLabel.text")); // NOI18N

        countryCodeTextField.setEditable(false);
        countryCodeTextField.setText(bundle.getString("BroadbandInternalFrame.countryCodeTextField.text")); // NOI18N

        areaCodeOpenLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        areaCodeOpenLabel.setText(bundle.getString("BroadbandInternalFrame.areaCodeOpenLabel.text")); // NOI18N

        areaCodeTextField.setEditable(false);
        areaCodeTextField.setText(bundle.getString("BroadbandInternalFrame.areaCodeTextField.text")); // NOI18N

        areaCodeCloseLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        areaCodeCloseLabel.setText(bundle.getString("BroadbandInternalFrame.areaCodeCloseLabel.text")); // NOI18N

        numberTextField.setText(bundle.getString("BroadbandInternalFrame.numberTextField.text")); // NOI18N
        numberTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                numberTextFieldActionPerformed(evt);
            }
        });
        numberTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                numberTextFieldKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout subscriberPanelLayout = new javax.swing.GroupLayout(subscriberPanel);
        subscriberPanel.setLayout(subscriberPanelLayout);
        subscriberPanelLayout.setHorizontalGroup(
            subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subscriberPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(numberLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(plusLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(countryCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(areaCodeOpenLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(areaCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(areaCodeCloseLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        subscriberPanelLayout.setVerticalGroup(
            subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subscriberPanelLayout.createSequentialGroup()
                .addGroup(subscriberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numberLabel)
                    .addComponent(plusLabel)
                    .addComponent(countryCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(areaCodeOpenLabel)
                    .addComponent(areaCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(areaCodeCloseLabel)
                    .addComponent(numberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        broadbandPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("BroadbandInternalFrame.broadbandPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        siteLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        siteLabel.setText(bundle.getString("BroadbandInternalFrame.siteLabel.text")); // NOI18N

        siteComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        siteComboBox.setModel(siteComboBoxModel);
        siteComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siteComboBoxActionPerformed(evt);
            }
        });

        dslamLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        dslamLabel.setText(bundle.getString("BroadbandInternalFrame.dslamLabel.text")); // NOI18N

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
        slotLabel.setText(bundle.getString("BroadbandInternalFrame.slotLabel.text")); // NOI18N

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
        portLabel.setText(bundle.getString("BroadbandInternalFrame.portLabel.text")); // NOI18N

        broadbandPortComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        broadbandPortComboBox.setModel(broadbandPortComboBoxModel);
        broadbandPortComboBox.setEnabled(false);

        routerLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        routerLabel.setText(bundle.getString("BroadbandInternalFrame.routerLabel.text")); // NOI18N

        routerTextField.setEditable(false);
        routerTextField.setText(bundle.getString("BroadbandInternalFrame.routerTextField.text")); // NOI18N

        stateButtonGroup.add(enabledRadioButton);
        enabledRadioButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        enabledRadioButton.setText(bundle.getString("BroadbandInternalFrame.enabledRadioButton.text")); // NOI18N
        enabledRadioButton.setEnabled(false);

        stateButtonGroup.add(debtorRadioButton);
        debtorRadioButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        debtorRadioButton.setText(bundle.getString("BroadbandInternalFrame.debtorRadioButton.text")); // NOI18N
        debtorRadioButton.setEnabled(false);

        usernameLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        usernameLabel.setText(bundle.getString("BroadbandInternalFrame.usernameLabel.text")); // NOI18N

        usernameTextField.setEditable(false);
        usernameTextField.setText(bundle.getString("BroadbandInternalFrame.usernameTextField.text")); // NOI18N

        passwordLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        passwordLabel.setText(bundle.getString("BroadbandInternalFrame.passwordLabel.text")); // NOI18N

        passwordTextField.setEditable(false);
        passwordTextField.setText(bundle.getString("BroadbandInternalFrame.passwordTextField.text")); // NOI18N

        stateButtonGroup.add(disabledRadioButton);
        disabledRadioButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        disabledRadioButton.setText(bundle.getString("BroadbandInternalFrame.disabledRadioButton.text")); // NOI18N
        disabledRadioButton.setEnabled(false);

        javax.swing.GroupLayout broadbandPanelLayout = new javax.swing.GroupLayout(broadbandPanel);
        broadbandPanel.setLayout(broadbandPanelLayout);
        broadbandPanelLayout.setHorizontalGroup(
            broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(broadbandPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(broadbandPanelLayout.createSequentialGroup()
                        .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, broadbandPanelLayout.createSequentialGroup()
                                .addComponent(routerLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(routerTextField))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, broadbandPanelLayout.createSequentialGroup()
                                .addComponent(dslamLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dslamComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(broadbandPanelLayout.createSequentialGroup()
                                .addComponent(slotLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(slotComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(portLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(broadbandPanelLayout.createSequentialGroup()
                                .addComponent(disabledRadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(enabledRadioButton)))
                        .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(broadbandPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(broadbandPortComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, broadbandPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(debtorRadioButton)
                                .addContainerGap())))
                    .addGroup(broadbandPanelLayout.createSequentialGroup()
                        .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(broadbandPanelLayout.createSequentialGroup()
                                .addComponent(siteLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(siteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(broadbandPanelLayout.createSequentialGroup()
                                .addComponent(usernameLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(passwordLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(passwordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(41, Short.MAX_VALUE))))
        );
        broadbandPanelLayout.setVerticalGroup(
            broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(broadbandPanelLayout.createSequentialGroup()
                .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(siteLabel)
                    .addComponent(siteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dslamLabel)
                    .addComponent(dslamComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(slotLabel)
                    .addComponent(slotComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portLabel)
                    .addComponent(broadbandPortComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(debtorRadioButton, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(routerLabel)
                        .addComponent(routerTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(enabledRadioButton)
                        .addComponent(disabledRadioButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(usernameLabel)
                    .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(usernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(passwordLabel)
                        .addComponent(passwordTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        searchButton.setText(bundle.getString("BroadbandInternalFrame.searchButton.text")); // NOI18N
        searchButton.setEnabled(false);
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        clearButton.setText(bundle.getString("BroadbandInternalFrame.clearButton.text")); // NOI18N
        clearButton.setEnabled(false);
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(clearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchButton)
                    .addComponent(clearButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subscriberPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(broadbandPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subscriberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(broadbandPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        open = false;
    }//GEN-LAST:event_formInternalFrameClosed

    private void siteComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siteComboBoxActionPerformed
        dslamComboBoxModel.removeAllElements();

        if (siteComboBox.getSelectedIndex() > 0) {
            int selectedSiteId = ((Site) siteComboBox.getSelectedItem()).getId();
            // DSLAM combobox.
            List<DSLAM> dslamsList = broadbandPortsAnalyzer.getDSLAMsList(selectedSiteId);
            if (dslamsList.size() > 0) {
                dslamComboBox.removeAllItems();
                fillComboBoxModel(dslamsList, dslamComboBoxModel);
            }
        }
        setComponentsConfiguration();
    }//GEN-LAST:event_siteComboBoxActionPerformed

    private void numberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numberTextFieldActionPerformed
        if (countryCodeTextField.getText().length() + areaCodeTextField.getText().length() + numberTextField.getText().length() > 11)
            searchButtonActionPerformed(evt);
    }//GEN-LAST:event_numberTextFieldActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        if (isValidForm()) {
            Connection connection = databaseConnectionsManager.takeConnection();
            Wiring wiring = null;
            String errorData = null;
            try {
                if (!numberTextField.getText().isEmpty()) {
                    errorData = officeCode.getCode() + "-" + mcdu;
                    wiring = Wiring.getBroadbandByPhoneNumber(connection, officeCode.getId(), mcdu);
                } else if (dslamComboBox.getSelectedIndex() > 0) {
                    StringBuilder stringBuilder = new StringBuilder(((DSLAM) dslamComboBox.getSelectedItem()).getName());
                    if (slotComboBox.getSelectedItem() != null) {
                        stringBuilder.append(" Slot ");
                        stringBuilder.append(((DSLAMBoard) slotComboBox.getSelectedItem()).getSlot());
                    }
                    stringBuilder.append(" Port ");
                    stringBuilder.append(broadbandPort);
                    errorData = stringBuilder.toString();
                    wiring = Wiring.getBroadbandByBroadbandPort(connection, broadbandPort.getId());
                }

                if (wiring != null)
                    fillComponentsData(wiring);
            } catch (Exception ex) {
                logger.error("Cannot get broadband data for " + errorData + ".", ex);
                JOptionPane.showMessageDialog(this, bundle.getString("Search.error") + " " + errorData + ".", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void fillComponentsData(Wiring wiring) {
        clearForm();
        isDisplayingData = true;

        // Phone number.
        Phone phone = wiring.getSubscriber().getAddress().getPhone();
        OfficeCode officeCode = phone.getOfficeCode();
        Area area = officeCode.getArea();
        Country country = area.getCountry();
        countryCodeTextField.setText(String.valueOf(country.getCode()));
        areaCodeTextField.setText(String.valueOf(area.getCode()));
        numberTextField.setText(officeCode.getCode() + "-" + phone.getNumber());
        numberTextField.setEditable(false);
        // Broadband.
        Broadband broadband = wiring.getBroadband();
        if (broadband != null) {
            BroadbandPort broadbandPort = broadband.getBroadbandPort();
            if (broadbandPort != null) {
                DSLAM dslam = DSLAMsManager.getDSLAMByBoard(broadbandPort.getDslamBoard().getId());
                DSLAMBoard dslamBoard = null;
                dslamBoard = broadbandPort.getDslamBoard();
                dslam = DSLAMsManager.getDSLAMByBoard(dslamBoard.getId());
                // Site.
                siteComboBox.setSelectedItem(dslam.getSite());
                siteComboBox.setEnabled(false);

                dslamComboBoxModel.setSelectedItem(dslam);
                slotComboBoxModel.setSelectedItem(dslamBoard);
                broadbandPortComboBoxModel.setSelectedItem(broadbandPort);

                routerTextField.setText(dslam.getRouter().getName());

                SubscriberBroadbandState broadbandState = wiring.getSubscriber().getData().getBroadbandState();
                switch (broadbandState.getName()) {
                    case DEBTOR:
                        debtorRadioButton.setSelected(true);
                        break;
                    case DISABLED:
                        disabledRadioButton.setSelected(true);
                        break;
                    case ENABLED:
                        enabledRadioButton.setSelected(true);
                }

                usernameTextField.setText(broadband.getUsername());
                passwordTextField.setText(broadband.getPassword());
            }
        } else
            dslamComboBoxModel.removeAllElements();
        dslamComboBox.setEnabled(false);

        clearButton.setEnabled(true);
        searchButton.setEnabled(false);
    }

    private void numberTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numberTextFieldKeyPressed
        if (countryCodeTextField.getText().length() + areaCodeTextField.getText().length() + numberTextField.getText().length() > 10)
            searchButton.setEnabled(true);
    }//GEN-LAST:event_numberTextFieldKeyPressed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clearForm();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void dslamComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dslamComboBoxActionPerformed
        slotComboBoxModel.removeAllElements();
        broadbandPortComboBoxModel.removeAllElements();

        List<DSLAMBoard> dslamBoardsList = new ArrayList<>();
        if ((dslamComboBox.getSelectedItem()) != null) {
            int dslamId = ((DSLAM) dslamComboBox.getSelectedItem()).getId();
            if (dslamId != 0) {
                dslamBoardsList = broadbandPortsAnalyzer.getDSLAMBoardsList(dslamId);

                if (dslamBoardsList.isEmpty())
                    fillComboBoxModel(broadbandPortsAnalyzer.getBroadbandPortsListByDSLAM(dslamId), broadbandPortComboBoxModel);

            }
        }
        fillComboBoxModel(dslamBoardsList, slotComboBoxModel);
        setComponentsConfiguration();
    }//GEN-LAST:event_dslamComboBoxActionPerformed

    private void slotComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slotComboBoxActionPerformed
        broadbandPortComboBoxModel.removeAllElements();

        if (slotComboBox.getSelectedItem() == null) {
            DSLAM selectedDSLAM = (DSLAM) dslamComboBox.getSelectedItem();
            if (selectedDSLAM != null && broadbandPortsAnalyzer.getDSLAMBoardsList(selectedDSLAM.getId()) == null) {
                fillComboBoxModel(broadbandPortsAnalyzer.getBroadbandPortsListByDSLAM(selectedDSLAM.getId()), broadbandPortComboBoxModel);
            }
        } else {
            DSLAMBoard selectedDSLAMBoard = (DSLAMBoard) slotComboBox.getSelectedItem();
            int selectedDSLAMBoardId = selectedDSLAMBoard.getId();
            if (selectedDSLAMBoardId != 0) {
                fillComboBoxModel(broadbandPortsAnalyzer.getBroadbandPortsListByBoard(selectedDSLAMBoardId), broadbandPortComboBoxModel);
            }
        }
    }//GEN-LAST:event_slotComboBoxActionPerformed

    private void setComponentsConfiguration() {
        if (!isDisplayingData) {
            Site selectedSite = (Site) siteComboBox.getSelectedItem();
            if (selectedSite != null && selectedSite.getId() != 0) {
                enableNumberComponent(false);
                if (broadbandPortsAnalyzer.getDSLAMsList(selectedSite.getId()).size() > 0)
                    dslamComboBox.setEnabled(true);
                else
                    dslamComboBox.setEnabled(false);
                searchButton.setEnabled(false);

                // DSLAM components.
                DSLAM selectedDSLAM = (DSLAM) dslamComboBox.getSelectedItem();
                if (selectedDSLAM != null && selectedDSLAM.getId() != 0) {
                    enableDSLAMComponents(true);
                    searchButton.setEnabled(true);
                } else {
                    slotComboBox.setEnabled(false);
                    broadbandPortComboBox.setEnabled(false);
                }

            } else {
                enableNumberComponent(true);
                enableDSLAMComponents(false);
                searchButton.setEnabled(false);
            }
        }
    }

    private void enableNumberComponent(boolean enabled) {
        numberTextField.setText("");
        numberTextField.setEditable(enabled);
    }

    private void enableDSLAMComponents(boolean enabled) {
        dslamComboBox.setEnabled(enabled);
        if (enabled && slotComboBoxModel.getSize() > 0)
            slotComboBox.setEnabled(true);
        else
            slotComboBox.setEnabled(false);
        broadbandPortComboBox.setEnabled(enabled);
    }

    private void clearForm() {
        isDisplayingData = false;

        setCountryCodeTextField();
        setAreaCodeTextField();
        numberTextField.setText("");
        numberTextField.setEditable(false);
        clearButton.setEnabled(false);

        siteComboBox.setSelectedIndex(0);
        siteComboBox.setEnabled(true);
        routerTextField.setText("");
        usernameTextField.setText("");
        passwordTextField.setText("");
        stateButtonGroup.clearSelection();
        setComponentsConfiguration();
    }

    public static boolean isOpen() {
        return open;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel areaCodeCloseLabel;
    private javax.swing.JLabel areaCodeOpenLabel;
    private javax.swing.JTextField areaCodeTextField;
    private javax.swing.JPanel broadbandPanel;
    private javax.swing.JComboBox<BroadbandPort> broadbandPortComboBox;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton clearButton;
    private javax.swing.JTextField countryCodeTextField;
    private javax.swing.JRadioButton debtorRadioButton;
    private javax.swing.JRadioButton disabledRadioButton;
    private javax.swing.JComboBox<DSLAM> dslamComboBox;
    private javax.swing.JLabel dslamLabel;
    private javax.swing.JRadioButton enabledRadioButton;
    private javax.swing.JLabel numberLabel;
    private javax.swing.JTextField numberTextField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JTextField passwordTextField;
    private javax.swing.JLabel plusLabel;
    private javax.swing.JLabel portLabel;
    private javax.swing.JLabel routerLabel;
    private javax.swing.JTextField routerTextField;
    private javax.swing.JButton searchButton;
    private javax.swing.JComboBox<Site> siteComboBox;
    private javax.swing.JLabel siteLabel;
    private javax.swing.JComboBox<DSLAMBoard> slotComboBox;
    private javax.swing.JLabel slotLabel;
    private javax.swing.ButtonGroup stateButtonGroup;
    private javax.swing.JPanel subscriberPanel;
    private javax.swing.JLabel usernameLabel;
    private javax.swing.JTextField usernameTextField;
    // End of variables declaration//GEN-END:variables

    private DatabaseConnectionsManager databaseConnectionsManager;
    private User user;
    private static boolean open;

    private boolean isDisplayingData;
    private SitesAnalyzer sitesAnalyzer;
    private PhonesAnalyzer ownNumerationAnalyzer;
    private BroadbandPortsAnalyzer broadbandPortsAnalyzer;

    // Models.
    private DefaultComboBoxModel<Site> siteComboBoxModel;
    private DefaultComboBoxModel<DSLAM> dslamComboBoxModel;
    private DefaultComboBoxModel<DSLAMBoard> slotComboBoxModel;
    private DefaultComboBoxModel<BroadbandPort> broadbandPortComboBoxModel;

    // Renderers.
    private DSLAMComboBoxRenderer dslamComboBoxRenderer;
    private DSLAMBoardComboBoxRenderer dslamBoardComboBoxRenderer;

    // Form data.
    private Country country;
    private Area area;
    private OfficeCode officeCode;
    private String mcdu;
    private BroadbandPort broadbandPort;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/BroadbandInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(BroadbandInternalFrame.class);

}
