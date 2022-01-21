/*
 * 		DistributionInternalFrame.java
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
 * 		DistributionInternalFrame.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Sep 8, 2019
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.Broadband;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.BroadbandPort;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAM;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMBoard;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.managers.DSLAMsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.BlockPosition;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Distributor;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Site;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.SwitchBlock;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.ELineModule;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Equipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Frame;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Neax61EEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Neax61SigmaELU;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Neax61SigmaEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.SigmaL3AddressEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.SigmaLineModule;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.ZhoneEquipment;
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
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.PhonesAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.Printer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.SitesAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.SwitchBlocksAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.CableComboBoxRenderer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.DSLAMBoardComboBoxRenderer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.DSLAMComboBoxRenderer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.PairComboBoxRenderer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.SwitchBlockComboBoxRenderer;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class DistributionInternalFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form DistributorInternalFrame
     */
    public DistributionInternalFrame(User user) {
        this.user = user;

        createComponentsModels();
        createComponentsRenderers();

        initComponents();

        Connection connection = null;
        try {
            databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
            connection = databaseConnectionsManager.takeConnection();

            sitesAnalyzer = new SitesAnalyzer();
            cablesAnalyzer = new CablesAnalyzer(connection);
            ownNumerationAnalyzer = new PhonesAnalyzer();
            broadbandPortsAnalyzer = new BroadbandPortsAnalyzer(connection);
            switchBlocksAnalyzer = new SwitchBlocksAnalyzer();

            fillComboBoxModel(sitesAnalyzer.getSitesList(), siteComboBoxModel);
            setCountryCodeTextField();
            setAreaCodeTextField();

            isDisplayingData = false;
            open = true;
        } catch (Exception ex) {
            logger.error("Cannot fill combo boxes.", ex);
            JOptionPane.showMessageDialog(this, bundle.getString("DistributionInternalFrame.comboBoxes.problem"), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            databaseConnectionsManager.returnConnection(connection);
        }
    }

    private void createComponentsModels() {
        siteComboBoxModel = new DefaultComboBoxModel<>();
        frameComboBoxModel = new DefaultComboBoxModel<>();
        cableComboBoxModel = new DefaultComboBoxModel<>();
        pairComboBoxModel = new DefaultComboBoxModel<>();
        dslamComboBoxModel = new DefaultComboBoxModel<>();
        slotComboBoxModel = new DefaultComboBoxModel<>();
        broadbandPortComboBoxModel = new DefaultComboBoxModel<>();
        switchBlockComboBoxModel = new DefaultComboBoxModel<>();
    }

    private void createComponentsRenderers() {
        cableComboBoxRenderer = new CableComboBoxRenderer();
        pairComboBoxRenderer = new PairComboBoxRenderer();
        dslamComboBoxRenderer = new DSLAMComboBoxRenderer();
        dslamBoardComboBoxRenderer = new DSLAMBoardComboBoxRenderer();
        switchBlockComboBoxRenderer = new SwitchBlockComboBoxRenderer();
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonsPanel = new javax.swing.JPanel();
        searchButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        printButton = new javax.swing.JButton();
        subscriberPanel = new javax.swing.JPanel();
        numberLabel = new javax.swing.JLabel();
        plusLabel = new javax.swing.JLabel();
        countryCodeTextField = new javax.swing.JTextField();
        areaCodeOpenLabel = new javax.swing.JLabel();
        areaCodeTextField = new javax.swing.JTextField();
        areaCodeCloseLabel = new javax.swing.JLabel();
        numberTextField = new javax.swing.JTextField();
        sitePanel = new javax.swing.JPanel();
        equipmentLabel = new javax.swing.JLabel();
        equipmentTextField = new javax.swing.JTextField();
        moduleTextField = new javax.swing.JTextField();
        switchBlockPanel = new javax.swing.JPanel();
        blockLabel = new javax.swing.JLabel();
        blockComboBox = new javax.swing.JComboBox<>();
        positionLabel = new javax.swing.JLabel();
        positionTextField = new javax.swing.JTextField();
        siteLabel = new javax.swing.JLabel();
        siteComboBox = new javax.swing.JComboBox<>();
        broadbandPanel = new javax.swing.JPanel();
        dslamLabel = new javax.swing.JLabel();
        dslamComboBox = new javax.swing.JComboBox<>();
        slotLabel = new javax.swing.JLabel();
        slotComboBox = new javax.swing.JComboBox<>();
        portLabel = new javax.swing.JLabel();
        broadbandPortComboBox = new javax.swing.JComboBox<>();
        streetPairPanel = new javax.swing.JPanel();
        frameLabel = new javax.swing.JLabel();
        frameComboBox = new javax.swing.JComboBox<>();
        cableLabel = new javax.swing.JLabel();
        cableComboBox = new javax.swing.JComboBox<>();
        pairLabel = new javax.swing.JLabel();
        secondPairjLabel = new javax.swing.JLabel();
        secondPairTextField = new javax.swing.JTextField();
        remarksScrollPane = new javax.swing.JScrollPane();
        remarksTextArea = new javax.swing.JTextArea();
        pairComboBox = new javax.swing.JComboBox<>();
        technologyLabel = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("i18n/DistributionInternalFrameBundle"); // NOI18N
        setTitle(bundle.getString("DistributionInternalFrame.title")); // NOI18N
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

        searchButton.setText(bundle.getString("DistributionInternalFrame.searchButton.text")); // NOI18N
        searchButton.setEnabled(false);
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        clearButton.setText(bundle.getString("DistributionInternalFrame.clearButton.text")); // NOI18N
        clearButton.setEnabled(false);
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });

        printButton.setText(bundle.getString("DistributionInternalFrame.printButton.text")); // NOI18N
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
                .addComponent(clearButton)
                .addGap(18, 18, 18)
                .addComponent(searchButton)
                .addGap(20, 20, 20))
        );
        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchButton)
                    .addComponent(clearButton)
                    .addComponent(printButton))
                .addContainerGap())
        );

        subscriberPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("DistributionInternalFrame.subscriberPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        numberLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        numberLabel.setText(bundle.getString("DistributionInternalFrame.numberLabel.text")); // NOI18N

        plusLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        plusLabel.setText(bundle.getString("DistributionInternalFrame.plusLabel.text")); // NOI18N

        countryCodeTextField.setEditable(false);

        areaCodeOpenLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        areaCodeOpenLabel.setText(bundle.getString("DistributionInternalFrame.areaCodeOpenLabel.text")); // NOI18N

        areaCodeTextField.setEditable(false);

        areaCodeCloseLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        areaCodeCloseLabel.setText(bundle.getString("DistributionInternalFrame.areaCodeCloseLabel.text")); // NOI18N

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
                .addComponent(countryCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(areaCodeOpenLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(areaCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(areaCodeCloseLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        sitePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("DistributionInternalFrame.sitePanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        equipmentLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        equipmentLabel.setText(bundle.getString("DistributionInternalFrame.equipmentLabel.text")); // NOI18N

        equipmentTextField.setEditable(false);

        moduleTextField.setEditable(false);

        switchBlockPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("DistributionInternalFrame.switchBlockPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        blockLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        blockLabel.setText(bundle.getString("DistributionInternalFrame.blockLabel.text")); // NOI18N

        blockComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        blockComboBox.setModel(switchBlockComboBoxModel);
        blockComboBox.setEnabled(false);
        blockComboBox.setRenderer(switchBlockComboBoxRenderer);
        blockComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blockComboBoxActionPerformed(evt);
            }
        });

        positionLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        positionLabel.setText(bundle.getString("DistributionInternalFrame.positionLabel.text")); // NOI18N

        positionTextField.setEditable(false);
        positionTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                positionTextFieldActionPerformed(evt);
            }
        });
        positionTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                positionTextFieldKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout switchBlockPanelLayout = new javax.swing.GroupLayout(switchBlockPanel);
        switchBlockPanel.setLayout(switchBlockPanelLayout);
        switchBlockPanelLayout.setHorizontalGroup(
            switchBlockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(switchBlockPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(blockLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blockComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(positionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(positionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        switchBlockPanelLayout.setVerticalGroup(
            switchBlockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(switchBlockPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(blockLabel)
                .addComponent(blockComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(positionLabel)
                .addComponent(positionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        siteLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        siteLabel.setText(bundle.getString("DistributionInternalFrame.siteLabel.text")); // NOI18N

        siteComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        siteComboBox.setModel(siteComboBoxModel);
        siteComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siteComboBoxActionPerformed(evt);
            }
        });

        broadbandPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("DistributionInternalFrame.broadbandPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        dslamLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        dslamLabel.setText(bundle.getString("DistributionInternalFrame.dslamLabel.text")); // NOI18N

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
        slotLabel.setText(bundle.getString("DistributionInternalFrame.slotLabel.text")); // NOI18N

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
        portLabel.setText(bundle.getString("DistributionInternalFrame.portLabel.text")); // NOI18N

        broadbandPortComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        broadbandPortComboBox.setModel(broadbandPortComboBoxModel);
        broadbandPortComboBox.setEnabled(false);

        javax.swing.GroupLayout broadbandPanelLayout = new javax.swing.GroupLayout(broadbandPanel);
        broadbandPanel.setLayout(broadbandPanelLayout);
        broadbandPanelLayout.setHorizontalGroup(
            broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(broadbandPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dslamLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dslamComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(slotLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(slotComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(portLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(broadbandPortComboBox, 0, 76, Short.MAX_VALUE)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        streetPairPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("DistributionInternalFrame.streetPairPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        frameLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        frameLabel.setText(bundle.getString("DistributionInternalFrame.frameLabel.text")); // NOI18N

        frameComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        frameComboBox.setModel(frameComboBoxModel);
        frameComboBox.setEnabled(false);
        frameComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frameComboBoxActionPerformed(evt);
            }
        });

        cableLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cableLabel.setText(bundle.getString("DistributionInternalFrame.cableLabel.text")); // NOI18N

        cableComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cableComboBox.setModel(cableComboBoxModel);
        cableComboBox.setEnabled(false);
        cableComboBox.setRenderer(cableComboBoxRenderer);
        cableComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cableComboBoxActionPerformed(evt);
            }
        });

        pairLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        pairLabel.setText(bundle.getString("DistributionInternalFrame.pairLabel.text")); // NOI18N

        secondPairjLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        secondPairjLabel.setText(bundle.getString("DistributionInternalFrame.secondPairjLabel.text")); // NOI18N

        secondPairTextField.setEditable(false);

        remarksTextArea.setEditable(false);
        remarksTextArea.setColumns(20);
        remarksTextArea.setRows(5);
        remarksScrollPane.setViewportView(remarksTextArea);

        pairComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        pairComboBox.setModel(pairComboBoxModel);
        pairComboBox.setEnabled(false);
        pairComboBox.setRenderer(pairComboBoxRenderer);

        javax.swing.GroupLayout streetPairPanelLayout = new javax.swing.GroupLayout(streetPairPanel);
        streetPairPanel.setLayout(streetPairPanelLayout);
        streetPairPanelLayout.setHorizontalGroup(
            streetPairPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(streetPairPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(streetPairPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(remarksScrollPane, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(streetPairPanelLayout.createSequentialGroup()
                        .addGroup(streetPairPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(streetPairPanelLayout.createSequentialGroup()
                                .addComponent(secondPairjLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(secondPairTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(streetPairPanelLayout.createSequentialGroup()
                                .addComponent(frameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(frameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cableLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cableComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pairLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pairComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        streetPairPanelLayout.setVerticalGroup(
            streetPairPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(streetPairPanelLayout.createSequentialGroup()
                .addGroup(streetPairPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frameLabel)
                    .addComponent(frameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cableLabel)
                    .addComponent(cableComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pairLabel)
                    .addComponent(pairComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(streetPairPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(secondPairjLabel)
                    .addComponent(secondPairTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(remarksScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                .addContainerGap())
        );

        technologyLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        technologyLabel.setText(bundle.getString("DistributionInternalFrame.technologyLabel.text")); // NOI18N

        javax.swing.GroupLayout sitePanelLayout = new javax.swing.GroupLayout(sitePanel);
        sitePanel.setLayout(sitePanelLayout);
        sitePanelLayout.setHorizontalGroup(
            sitePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sitePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(sitePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(switchBlockPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(broadbandPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(streetPairPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(sitePanelLayout.createSequentialGroup()
                        .addGroup(sitePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(sitePanelLayout.createSequentialGroup()
                                .addComponent(equipmentLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(equipmentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(sitePanelLayout.createSequentialGroup()
                                .addComponent(siteLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(siteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(sitePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(moduleTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
                            .addComponent(technologyLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        sitePanelLayout.setVerticalGroup(
            sitePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sitePanelLayout.createSequentialGroup()
                .addGroup(sitePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(siteLabel)
                    .addComponent(siteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(technologyLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(sitePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(equipmentLabel)
                    .addComponent(equipmentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(moduleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(switchBlockPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(broadbandPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(streetPairPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subscriberPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sitePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subscriberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sitePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        } else if (blockComboBox.getSelectedIndex() != 0) {
            switchBlock = (SwitchBlock) blockComboBox.getSelectedItem();
            position = positionTextField.getText().toUpperCase();
            char[] positionArray = position.toCharArray();
            try {
                if (positionArray.length == 3 || positionArray.length == 4) {
                    position = (position.length() == 3) ? "0" + position : position;
                    if (positionArray[positionArray.length - 2] < 'A' || positionArray[positionArray.length - 2] > 'G') {
                        JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.position.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                        valid = false;
                    } else if (positionArray[positionArray.length - 1] < 'B' || positionArray[positionArray.length - 1] > 'H') {
                        JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.position.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                        valid = false;
                    } else if (Integer.parseInt(position.substring(0, position.length() - 2)) > 31 || Integer.parseInt(position.substring(0, position.length() - 2)) < 1) {
                        JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.position.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                        valid = false;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.position.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    valid = false;
                }
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.position.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                valid = false;
            }
        } else if (dslamComboBox.getSelectedIndex() > 0)
            broadbandPort = (BroadbandPort) broadbandPortComboBox.getSelectedItem();
        else if (pairComboBox.getSelectedIndex() > 0)
            streetPair = (StreetPair) pairComboBox.getSelectedItem();

        return valid;
    }

    private void siteComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siteComboBoxActionPerformed
        frameComboBoxModel.removeAllElements();
        dslamComboBoxModel.removeAllElements();
        switchBlockComboBoxModel.removeAllElements();

        if (siteComboBox.getSelectedIndex() > 0) {
            int selectedSiteId = ((Site) siteComboBox.getSelectedItem()).getId();
            // Frame combobox.
            List<StreetFrame> streetFramesList = new ArrayList<>();
            StreetFrame selectStreetFrame = new StreetFrame();
            selectStreetFrame.setId(0);
            selectStreetFrame.setName(bundle.getString("DistributionInternalFrame.Select.short").charAt(0));
            streetFramesList.add(selectStreetFrame);
            for (StreetFrame streetFrame : cablesAnalyzer.getStreetFramesList(selectedSiteId))
                streetFramesList.add(streetFrame);

            fillComboBoxModel(streetFramesList, frameComboBoxModel);
            // DSLAM combobox.
            List<DSLAM> dslamsList = broadbandPortsAnalyzer.getDSLAMsList(selectedSiteId);
            if (dslamsList.size() > 0) {
                dslamComboBox.removeAllItems();
                fillComboBoxModel(dslamsList, dslamComboBoxModel);
            }
            // SwitchBlock combobox.
            List<SwitchBlock> switchBlocksList = new ArrayList<>();
            SwitchBlock selectSwitchBlock = new SwitchBlock();
            selectSwitchBlock.setId(0);
            switchBlocksList.add(selectSwitchBlock);
            for (SwitchBlock switchBlock : switchBlocksAnalyzer.getSwitchBlocksList(selectedSiteId))
                switchBlocksList.add(switchBlock);
            fillComboBoxModel(switchBlocksList, switchBlockComboBoxModel);
        }
        setComponentsConfiguration();
    }//GEN-LAST:event_siteComboBoxActionPerformed

    private void frameComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frameComboBoxActionPerformed
        cableComboBoxModel.removeAllElements();

        List<StreetCable> streetCablesList = new ArrayList<>();
        if (frameComboBox.getSelectedItem() != null) {
            int selectedFrameId = ((StreetFrame) frameComboBox.getSelectedItem()).getId();
            if (selectedFrameId != 0) {
                streetCablesList = cablesAnalyzer.getStreetCablesList(selectedFrameId);
                fillComboBoxModel(streetCablesList, cableComboBoxModel);
            }
        }

        setComponentsConfiguration();
    }//GEN-LAST:event_frameComboBoxActionPerformed

    private void cableComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cableComboBoxActionPerformed
        pairComboBoxModel.removeAllElements();
        StreetCable selectedCable = (StreetCable) cableComboBox.getSelectedItem();
        if (selectedCable != null && selectedCable.getName() != bundle.getString("DistributionInternalFrame.Select.short").charAt(0)) {
            fillComboBoxModel(cablesAnalyzer.getStreetPairsList(selectedCable.getId()), pairComboBoxModel);
        }
    }//GEN-LAST:event_cableComboBoxActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        if (isValidForm()) {
            Connection connection = databaseConnectionsManager.takeConnection();
            Wiring wiring = null;
            String errorData = null;
            try {
                if (!numberTextField.getText().isEmpty()) {
                    errorData = officeCode.getCode() + "-" + mcdu;
                    wiring = Wiring.getDistributionWiringByPhoneNumber(connection, officeCode.getId(), mcdu);
                } else if (!positionTextField.getText().isEmpty()) {
                    errorData = switchBlock.getName() + " " + position;
                    wiring = Wiring.getDistributionWiringByBlockPosition(connection, switchBlock.getId(), position);
                } else if (dslamComboBox.getSelectedIndex() > 0) {
                    StringBuilder stringBuilder = new StringBuilder(((DSLAM) dslamComboBox.getSelectedItem()).getName());
                    if (slotComboBox.getSelectedItem() != null) {
                        stringBuilder.append(" Slot ");
                        stringBuilder.append(((DSLAMBoard) slotComboBox.getSelectedItem()).getSlot());
                    }
                    stringBuilder.append(" Port ");
                    stringBuilder.append(broadbandPort);
                    errorData = stringBuilder.toString();
                    wiring = Wiring.getDistributionWiringByBroadbandPort(connection, broadbandPort.getId());
                } else if (frameComboBox.getSelectedIndex() > 0) {
                    errorData = streetPair.toString();
                    wiring = Wiring.getDistributionWiringByStreetPair(connection, streetPair.getId());
                }

                if (wiring != null)
                    fillComponentsData(wiring);
            } catch (Exception ex) {
                logger.error("Cannot get distribution wiring for " + errorData + ".", ex);
                JOptionPane.showMessageDialog(this, bundle.getString("Search.error") + " " + errorData + ".", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void numberTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_numberTextFieldKeyPressed
        if (countryCodeTextField.getText().length() + areaCodeTextField.getText().length() + numberTextField.getText().length() > 10)
            searchButton.setEnabled(true);
    }//GEN-LAST:event_numberTextFieldKeyPressed

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        open = false;
    }//GEN-LAST:event_formInternalFrameClosed

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

    private void blockComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blockComboBoxActionPerformed
        setComponentsConfiguration();
    }//GEN-LAST:event_blockComboBoxActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clearForm();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        LinkedHashMap<String, String> content;
        try {
            content = getContent();
            if (content != null) {
                Printer printer = new Printer();
                printer.setContent(content);
                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintable(printer);
                boolean ok = job.printDialog();
                if (ok) {
                    try {
                        job.print();
                        logger.info("Distribution for " + numberTextField.getText() + " printed.");
                    } catch (PrinterException ex) {
                        logger.error("Couldn't print distribution for " + numberTextField.getText() + ".", ex);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Cannot print data.", ex);
        }
    }//GEN-LAST:event_printButtonActionPerformed

    private void numberTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numberTextFieldActionPerformed
        if (countryCodeTextField.getText().length() + areaCodeTextField.getText().length() + numberTextField.getText().length() > 11)
            searchButtonActionPerformed(evt);
    }//GEN-LAST:event_numberTextFieldActionPerformed

    private void positionTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_positionTextFieldActionPerformed
        if (positionTextField.getText().length() == 4)
            searchButtonActionPerformed(evt);
    }//GEN-LAST:event_positionTextFieldActionPerformed

    private void positionTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_positionTextFieldKeyPressed
        if (positionTextField.getText().length() > 2)
            searchButton.setEnabled(true);
    }//GEN-LAST:event_positionTextFieldKeyPressed

    private LinkedHashMap<String, String> getContent() throws Exception {
        LinkedHashMap<String, String> content = new LinkedHashMap<>();

        content.put(bundle.getString("DistributionInternalFrame.printable.subscriberNumber"), numberTextField.getText());
        content.put(bundle.getString("DistributionInternalFrame.printable.site"), ((Site) siteComboBox.getSelectedItem()).getName() + "  (" + technologyLabel.getText() + ")");
        content.put(bundle.getString("DistributionInternalFrame.printable.equipment"), equipmentTextField.getText() + "  (" + moduleTextField.getText() + ")");
        content.put(bundle.getString("DistributionInternalFrame.printable.switchBlock"), ((SwitchBlock) blockComboBox.getSelectedItem()).getName() + " " + positionTextField.getText());
        // Broadband.
        DSLAM selectedDSLAM = (DSLAM) dslamComboBox.getSelectedItem();
        if (selectedDSLAM != null)
            content.put(bundle.getString("DistributionInternalFrame.printable.broadband"), selectedDSLAM.getName() + "-" + ((DSLAMBoard) slotComboBox.getSelectedItem()).getSlot() + "-" + ((BroadbandPort) broadbandPortComboBox.getSelectedItem()).getPort());
        // Street pair.
        StreetPair streetPair = (StreetPair) pairComboBox.getSelectedItem();
        StreetCable streetCable = streetPair.getStreetCable();
        StreetFrame streetFrame = streetCable.getStreetFrame();
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(streetFrame.getName()));
        stringBuilder.append(streetCable.getName());
        stringBuilder.append(String.format("%04d", streetPair.getPair()));
        content.put(bundle.getString("DistributionInternalFrame.printable.cablePair"), stringBuilder.toString());
        // Second street pair.
        String secondPair = secondPairTextField.getText();
        if (!secondPair.isEmpty())
            content.put(bundle.getString("DistributionInternalFrame.printable.secondPair"), secondPair);

        return content;
    }

    private void setComponentsConfiguration() {
        if (!isDisplayingData) {
            Site selectedSite = (Site) siteComboBox.getSelectedItem();
            if (selectedSite != null && selectedSite.getId() != 0) {
                enableNumberComponent(false);
                blockComboBox.setEnabled(true);
                if (broadbandPortsAnalyzer.getDSLAMsList(selectedSite.getId()).size() > 0)
                    dslamComboBox.setEnabled(true);
                else
                    dslamComboBox.setEnabled(false);
                frameComboBox.setEnabled(true);
                searchButton.setEnabled(false);

                // Switch Block components.
                SwitchBlock selectedSwitchBlock = (SwitchBlock) blockComboBox.getSelectedItem();
                if (selectedSwitchBlock != null && selectedSwitchBlock.getId() != 0) {
                    enableSwitchBlockComponents(true);
                    enableDSLAMComponents(false);
                    enableStreetPairComponents(false);
                } else {
                    positionTextField.setText("");
                    positionTextField.setEditable(false);
                    // DSLAM components.
                    DSLAM selectedDSLAM = (DSLAM) dslamComboBox.getSelectedItem();
                    if (selectedDSLAM != null && selectedDSLAM.getId() != 0) {
                        enableSwitchBlockComponents(false);
                        enableDSLAMComponents(true);
                        enableStreetPairComponents(false);
                        searchButton.setEnabled(true);
                    } else {
                        slotComboBox.setEnabled(false);
                        broadbandPortComboBox.setEnabled(false);
                        // Street Pair components
                        StreetFrame selectedFrame = (StreetFrame) frameComboBox.getSelectedItem();
                        if (selectedFrame != null && selectedFrame.getId() != 0) {
                            enableSwitchBlockComponents(false);
                            enableDSLAMComponents(false);
                            enableStreetPairComponents(true);
                            searchButton.setEnabled(true);
                        } else {
                            cableComboBox.setEnabled(false);
                            pairComboBox.setEnabled(false);
                        }
                    }
                }

            } else {
                enableNumberComponent(true);
                enableSwitchBlockComponents(false);
                enableDSLAMComponents(false);
                enableStreetPairComponents(false);
                searchButton.setEnabled(false);
            }
        }
    }

    private void enableNumberComponent(boolean enabled) {
        numberTextField.setText("");
        numberTextField.setEditable(enabled);
    }

    private void enableSwitchBlockComponents(boolean enabled) {
        blockComboBox.setEnabled(enabled);
        positionTextField.setText("");
        positionTextField.setEditable(enabled);
    }

    private void enableDSLAMComponents(boolean enabled) {
        dslamComboBox.setEnabled(enabled);
        if (enabled && slotComboBoxModel.getSize() > 0)
            slotComboBox.setEnabled(true);
        else
            slotComboBox.setEnabled(false);
        broadbandPortComboBox.setEnabled(enabled);
    }

    private void enableStreetPairComponents(boolean enabled) {
        frameComboBox.setEnabled(enabled);
        cableComboBox.setEnabled(enabled);
        pairComboBox.setEnabled(enabled);
    }

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
        // Site.
        Distributor distributor = wiring.getDistributor();
        Equipment equipment = distributor.getEquipment();
        siteComboBox.setSelectedItem(equipment.getSite());
        siteComboBox.setEnabled(false);
        // Equipment.
        equipmentTextField.setText(equipment.toString());
        String module = null;
        String technology = null;
        if (equipment instanceof Neax61SigmaEquipment) {
            SigmaLineModule lineModule = ((Neax61SigmaEquipment) equipment).getSigmaLineModule();
            Frame frame = lineModule.getFrame();
            module = frame.getName() + " - " + lineModule.toString();
            technology = "NEAX61Σ";
        } else if (equipment instanceof Neax61EEquipment) {
            ELineModule lineModule = ((Neax61EEquipment) equipment).getELineModule();
            Frame frame = lineModule.getFrame();
            module = frame.getName() + " - " + lineModule.toString();
            technology = "NEAX61E";
        } else if (equipment instanceof SigmaL3AddressEquipment) {
            Neax61SigmaELU neax61SigmaELU = ((SigmaL3AddressEquipment) equipment).getNeax61SigmaELU();
            module = neax61SigmaELU.toString();
            technology = "NEAX61Σ";
        } else if (equipment instanceof ZhoneEquipment) {
            module = "-";
            technology = "Zhone";
        }
        moduleTextField.setText(module);
        technologyLabel.setText(technology);
        // Switch Block.
        BlockPosition blockPosition = distributor.getBlockPosition();
        SwitchBlock switchBlock = blockPosition.getSwitchBlock();
        blockComboBox.setSelectedItem(switchBlock);
        positionTextField.setText(blockPosition.getPosition());
        blockComboBox.setEnabled(false);
        positionTextField.setEditable(false);
        // Broadband.
        Broadband broadband = wiring.getBroadband();
        if (broadband != null) {
            BroadbandPort broadbandPort = broadband.getBroadbandPort();
            DSLAM dslam = DSLAMsManager.getDSLAMByBoard(broadbandPort.getDslamBoard().getId());
            DSLAMBoard dslamBoard = broadbandPort.getDslamBoard();
            dslam = DSLAMsManager.getDSLAMByBoard(dslamBoard.getId());
            dslamComboBoxModel.setSelectedItem(dslam);
            slotComboBoxModel.setSelectedItem(dslamBoard);
            broadbandPortComboBoxModel.setSelectedItem(broadbandPort);
        } else
            dslamComboBoxModel.removeAllElements();
        dslamComboBox.setEnabled(false);
        // Street Pair.
        StreetPair streetPair = wiring.getStreetPair();
        StreetCable streetCable = streetPair.getStreetCable();
        StreetFrame streetFrame = streetCable.getStreetFrame();
        frameComboBoxModel.setSelectedItem(streetFrame);
        cableComboBoxModel.setSelectedItem(streetCable);
        pairComboBoxModel.setSelectedItem(streetPair);
        StreetPair secondStreetPair = wiring.getSecondStreetPair();
        if (secondStreetPair != null) {
            StreetCable secondStreetCable = secondStreetPair.getStreetCable();
            StreetFrame secondStreetFrame = secondStreetCable.getStreetFrame();
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(secondStreetFrame.getName()));
            stringBuilder.append(secondStreetCable.getName());
            stringBuilder.append(String.format("%04d", secondStreetPair.getPair()));
            secondPairTextField.setText(stringBuilder.toString());
        }
        frameComboBox.setEnabled(false);

        remarksTextArea.setText(wiring.getRemarks());

        clearButton.setEnabled(true);
        printButton.setEnabled(true);
        searchButton.setEnabled(false);
    }

    private void clearForm() {
        isDisplayingData = false;

        setCountryCodeTextField();
        setAreaCodeTextField();
        numberTextField.setText("");
        numberTextField.setEditable(false);
        equipmentTextField.setText("");
        moduleTextField.setText("");
        technologyLabel.setText("");
        secondPairTextField.setText("");
        remarksTextArea.setText("");
        clearButton.setEnabled(false);
        printButton.setEnabled(false);

        siteComboBox.setSelectedIndex(0);
        siteComboBox.setEnabled(true);
        setComponentsConfiguration();
    }

    public static boolean isOpen() {
        return open;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel areaCodeCloseLabel;
    private javax.swing.JLabel areaCodeOpenLabel;
    private javax.swing.JTextField areaCodeTextField;
    private javax.swing.JComboBox<SwitchBlock> blockComboBox;
    private javax.swing.JLabel blockLabel;
    private javax.swing.JPanel broadbandPanel;
    private javax.swing.JComboBox<BroadbandPort> broadbandPortComboBox;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JComboBox<StreetCable> cableComboBox;
    private javax.swing.JLabel cableLabel;
    private javax.swing.JButton clearButton;
    private javax.swing.JTextField countryCodeTextField;
    private javax.swing.JComboBox<DSLAM> dslamComboBox;
    private javax.swing.JLabel dslamLabel;
    private javax.swing.JLabel equipmentLabel;
    private javax.swing.JTextField equipmentTextField;
    private javax.swing.JComboBox<StreetFrame> frameComboBox;
    private javax.swing.JLabel frameLabel;
    private javax.swing.JTextField moduleTextField;
    private javax.swing.JLabel numberLabel;
    private javax.swing.JTextField numberTextField;
    private javax.swing.JComboBox<StreetPair> pairComboBox;
    private javax.swing.JLabel pairLabel;
    private javax.swing.JLabel plusLabel;
    private javax.swing.JLabel portLabel;
    private javax.swing.JLabel positionLabel;
    private javax.swing.JTextField positionTextField;
    private javax.swing.JButton printButton;
    private javax.swing.JScrollPane remarksScrollPane;
    private javax.swing.JTextArea remarksTextArea;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField secondPairTextField;
    private javax.swing.JLabel secondPairjLabel;
    private javax.swing.JComboBox<Site> siteComboBox;
    private javax.swing.JLabel siteLabel;
    private javax.swing.JPanel sitePanel;
    private javax.swing.JComboBox<DSLAMBoard> slotComboBox;
    private javax.swing.JLabel slotLabel;
    private javax.swing.JPanel streetPairPanel;
    private javax.swing.JPanel subscriberPanel;
    private javax.swing.JPanel switchBlockPanel;
    private javax.swing.JLabel technologyLabel;
    // End of variables declaration//GEN-END:variables

    private DatabaseConnectionsManager databaseConnectionsManager;
    private User user;
    private static boolean open;
    private boolean isDisplayingData;
    private SitesAnalyzer sitesAnalyzer;
    private CablesAnalyzer cablesAnalyzer;
    private PhonesAnalyzer ownNumerationAnalyzer;
    private BroadbandPortsAnalyzer broadbandPortsAnalyzer;
    private SwitchBlocksAnalyzer switchBlocksAnalyzer;

    // Models.
    private DefaultComboBoxModel<Site> siteComboBoxModel;
    private DefaultComboBoxModel<StreetFrame> frameComboBoxModel;
    private DefaultComboBoxModel<StreetCable> cableComboBoxModel;
    private DefaultComboBoxModel<StreetPair> pairComboBoxModel;
    private DefaultComboBoxModel<DSLAM> dslamComboBoxModel;
    private DefaultComboBoxModel<DSLAMBoard> slotComboBoxModel;
    private DefaultComboBoxModel<BroadbandPort> broadbandPortComboBoxModel;
    private DefaultComboBoxModel<SwitchBlock> switchBlockComboBoxModel;

    // Renderers.
    private CableComboBoxRenderer cableComboBoxRenderer;
    private PairComboBoxRenderer pairComboBoxRenderer;
    private DSLAMComboBoxRenderer dslamComboBoxRenderer;
    private DSLAMBoardComboBoxRenderer dslamBoardComboBoxRenderer;
    private SwitchBlockComboBoxRenderer switchBlockComboBoxRenderer;

    // Form data.
    private Country country;
    private Area area;
    private OfficeCode officeCode;
    private String mcdu;
    private SwitchBlock switchBlock;
    private String position;
    private BroadbandPort broadbandPort;
    private StreetPair streetPair;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/DistributionInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(DistributionInternalFrame.class);
}
