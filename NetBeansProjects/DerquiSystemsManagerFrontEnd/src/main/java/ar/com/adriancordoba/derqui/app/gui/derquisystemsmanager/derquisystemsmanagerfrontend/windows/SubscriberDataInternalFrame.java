/*
 * 		SubscriberDataInternalFrame.java
 *   Copyright (C) 2017  Adrián E. Córdoba [software.asia@gmail.com]
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
 * 		SubscriberDataInternalFrame.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Nov 29, 2017
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors.SubscriberBroadbandStateName;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors.SubscriberStateName;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriber.Subscriber;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.SubscriberData;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.SubscriberService;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.managers.SubscriberBroadbandStatesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.managers.SubscriberServicesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.managers.SubscriberStatesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.restriction.OriginationRestriction;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.restriction.SubscriberRestriction;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.restriction.TerminationRestriction;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.restriction.managers.SubscriberRestrictionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.state.SubscriberBroadbandState;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.User;
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
public class SubscriberDataInternalFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form SubscriberDataInternalFrame
     */
    public SubscriberDataInternalFrame(User user) {
        this.user = user;
        if (user.hasRole("subscriber-data-operator"))
            privilegedUser = true;
        createComponentsModels();

        initComponents();

        Connection connection = null;
        try {
            databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
            connection = databaseConnectionsManager.takeConnection();

            fillSubscribersListModel(connection);
            fillComboBoxesModels();

            open = true;
        } catch (Exception exception) {
            logger.error("Cannot fill subscribers list.", exception);
            JOptionPane.showMessageDialog(this, bundle.getString("SubscribersDataInternalFrame.subscribersList.problem"), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            databaseConnectionsManager.returnConnection(connection);
        }
    }

    private void createComponentsModels() {
        subscribersListModel = new DefaultListModel<>();
        terminationRestrictionComboBoxModel = new DefaultComboBoxModel<>();
        originationRestrictionComboBoxModel = new DefaultComboBoxModel<>();
    }

    private void fillSubscribersListModel(Connection connection) throws Exception {
        subscribersListModel.removeAllElements();
        List<Subscriber> subscribersList = Subscriber.getSubscribersDataList(connection);
        for (Subscriber subscriber : subscribersList) {
            subscribersListModel.addElement(subscriber);
        }
    }

    private void fillComboBoxesModels() {
        fillComboBoxModel(SubscriberRestrictionsManager.getTerminationRestrictionsList(), terminationRestrictionComboBoxModel);
        fillComboBoxModel(SubscriberRestrictionsManager.getOriginationRestrictionsList(), originationRestrictionComboBoxModel);
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
        broadbandStateButtonGroup = new javax.swing.ButtonGroup();
        subscribersListScrollPane = new javax.swing.JScrollPane();
        subscribersList = new javax.swing.JList<>();
        subscriberLinePanel = new javax.swing.JPanel();
        lineNumberLabel = new javax.swing.JLabel();
        lineNumberTextField = new javax.swing.JTextField();
        holderLabel = new javax.swing.JLabel();
        holderTextField = new javax.swing.JTextField();
        subscriberDataPanel = new javax.swing.JPanel();
        lineTypeLabel = new javax.swing.JLabel();
        lineTypeTextField = new javax.swing.JTextField();
        terminationRestrictionPanel = new javax.swing.JPanel();
        terminationRestrictionLabel = new javax.swing.JLabel();
        terminationRestrictionComboBox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        terminationRestrictionTextArea = new javax.swing.JTextArea();
        originationRestrictionPanel = new javax.swing.JPanel();
        originationRestrictionLabel = new javax.swing.JLabel();
        originationRestrictionComboBox = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        originationRestrictionTextArea = new javax.swing.JTextArea();
        ServicesPanel = new javax.swing.JPanel();
        clipCheckBox = new javax.swing.JCheckBox();
        clirCheckBox = new javax.swing.JCheckBox();
        cfCheckBox = new javax.swing.JCheckBox();
        cwCheckBox = new javax.swing.JCheckBox();
        broadbandPanel = new javax.swing.JPanel();
        broadbandWiredCheckBox = new javax.swing.JCheckBox();
        broadbandEnabledRadioButton = new javax.swing.JRadioButton();
        broadbandDebtorRadioButton = new javax.swing.JRadioButton();
        broadbandDisabledRadioButton = new javax.swing.JRadioButton();
        informationPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        informationTextArea = new javax.swing.JTextArea();
        buttonsPanel = new javax.swing.JPanel();
        changeButton = new javax.swing.JButton();
        queryButton = new javax.swing.JButton();
        statePanel = new javax.swing.JPanel();
        enabledRadioButton = new javax.swing.JRadioButton();
        debtorRadioButton = new javax.swing.JRadioButton();
        disabledRadioButton = new javax.swing.JRadioButton();

        setClosable(true);
        setIconifiable(true);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("i18n/SubscriberDataInternalFrameBundle"); // NOI18N
        setTitle(bundle.getString("SubscriberDataInternalFrame.title")); // NOI18N
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
        subscribersListScrollPane.setViewportView(subscribersList);

        subscriberLinePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("SubscriberDataInternalFrame.SubscriberLinePanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        lineNumberLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lineNumberLabel.setText(bundle.getString("SubscriberDataInternalFrame.lineNumberLabel.text")); // NOI18N

        lineNumberTextField.setEditable(false);
        lineNumberTextField.setText(bundle.getString("SubscriberDataInternalFrame.lineNumberTextField.text")); // NOI18N

        holderLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        holderLabel.setText(bundle.getString("SubscriberDataInternalFrame.holderLabel.text")); // NOI18N

        holderTextField.setEditable(false);
        holderTextField.setText(bundle.getString("SubscriberDataInternalFrame.holderTextField.text")); // NOI18N

        javax.swing.GroupLayout subscriberLinePanelLayout = new javax.swing.GroupLayout(subscriberLinePanel);
        subscriberLinePanel.setLayout(subscriberLinePanelLayout);
        subscriberLinePanelLayout.setHorizontalGroup(
            subscriberLinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subscriberLinePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subscriberLinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lineNumberLabel)
                    .addComponent(holderLabel))
                .addGap(26, 26, 26)
                .addGroup(subscriberLinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(holderTextField)
                    .addGroup(subscriberLinePanelLayout.createSequentialGroup()
                        .addComponent(lineNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 96, Short.MAX_VALUE)))
                .addContainerGap())
        );
        subscriberLinePanelLayout.setVerticalGroup(
            subscriberLinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subscriberLinePanelLayout.createSequentialGroup()
                .addGroup(subscriberLinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lineNumberLabel)
                    .addComponent(lineNumberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(subscriberLinePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(holderLabel)
                    .addComponent(holderTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        subscriberDataPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("SubscriberDataInternalFrame.SubscriberDataPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        lineTypeLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        lineTypeLabel.setText(bundle.getString("SubscriberDataInternalFrame.lineTypeLabel.text")); // NOI18N

        lineTypeTextField.setEditable(false);
        lineTypeTextField.setText(bundle.getString("SubscriberDataInternalFrame.lineTypeTextField.text")); // NOI18N

        terminationRestrictionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("SubscriberDataInternalFrame.SubscriberDataPanel.TerminationRestriction.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        terminationRestrictionLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        terminationRestrictionLabel.setText(bundle.getString("SubscriberDataInternalFrame.terminationRestrictionLabel.text")); // NOI18N

        terminationRestrictionComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        terminationRestrictionComboBox.setModel(terminationRestrictionComboBoxModel);
        terminationRestrictionComboBox.setEnabled(false);
        terminationRestrictionComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                terminationRestrictionComboBoxActionPerformed(evt);
            }
        });

        terminationRestrictionTextArea.setEditable(false);
        terminationRestrictionTextArea.setColumns(10);
        terminationRestrictionTextArea.setLineWrap(true);
        terminationRestrictionTextArea.setRows(1);
        jScrollPane1.setViewportView(terminationRestrictionTextArea);

        javax.swing.GroupLayout terminationRestrictionPanelLayout = new javax.swing.GroupLayout(terminationRestrictionPanel);
        terminationRestrictionPanel.setLayout(terminationRestrictionPanelLayout);
        terminationRestrictionPanelLayout.setHorizontalGroup(
            terminationRestrictionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(terminationRestrictionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(terminationRestrictionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(terminationRestrictionPanelLayout.createSequentialGroup()
                        .addComponent(terminationRestrictionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(terminationRestrictionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        terminationRestrictionPanelLayout.setVerticalGroup(
            terminationRestrictionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(terminationRestrictionPanelLayout.createSequentialGroup()
                .addGroup(terminationRestrictionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(terminationRestrictionLabel)
                    .addComponent(terminationRestrictionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        originationRestrictionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("SubscriberDataInternalFrame.SubscriberDataPanel.OriginationRestriction.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        originationRestrictionLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        originationRestrictionLabel.setText(bundle.getString("SubscriberDataInternalFrame.originationRestrictionLabel.text")); // NOI18N

        originationRestrictionComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        originationRestrictionComboBox.setModel(originationRestrictionComboBoxModel);
        originationRestrictionComboBox.setEnabled(false);
        originationRestrictionComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                originationRestrictionComboBoxActionPerformed(evt);
            }
        });

        originationRestrictionTextArea.setEditable(false);
        originationRestrictionTextArea.setColumns(10);
        originationRestrictionTextArea.setLineWrap(true);
        originationRestrictionTextArea.setRows(3);
        jScrollPane2.setViewportView(originationRestrictionTextArea);

        javax.swing.GroupLayout originationRestrictionPanelLayout = new javax.swing.GroupLayout(originationRestrictionPanel);
        originationRestrictionPanel.setLayout(originationRestrictionPanelLayout);
        originationRestrictionPanelLayout.setHorizontalGroup(
            originationRestrictionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(originationRestrictionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(originationRestrictionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                    .addGroup(originationRestrictionPanelLayout.createSequentialGroup()
                        .addComponent(originationRestrictionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(originationRestrictionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        originationRestrictionPanelLayout.setVerticalGroup(
            originationRestrictionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(originationRestrictionPanelLayout.createSequentialGroup()
                .addGroup(originationRestrictionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(originationRestrictionLabel)
                    .addComponent(originationRestrictionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        ServicesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("SubscriberDataInternalFrame.SubscriberDataPanel.Services.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        clipCheckBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        clipCheckBox.setText(bundle.getString("SubscriberDataInternalFrame.clipCheckBox.text")); // NOI18N
        clipCheckBox.setEnabled(false);

        clirCheckBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        clirCheckBox.setText(bundle.getString("SubscriberDataInternalFrame.clirCheckBox.text")); // NOI18N
        clirCheckBox.setEnabled(false);

        cfCheckBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cfCheckBox.setText(bundle.getString("SubscriberDataInternalFrame.cfCheckBox.text")); // NOI18N
        cfCheckBox.setEnabled(false);

        cwCheckBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cwCheckBox.setText(bundle.getString("SubscriberDataInternalFrame.cwCheckBox.text")); // NOI18N
        cwCheckBox.setEnabled(false);

        javax.swing.GroupLayout ServicesPanelLayout = new javax.swing.GroupLayout(ServicesPanel);
        ServicesPanel.setLayout(ServicesPanelLayout);
        ServicesPanelLayout.setHorizontalGroup(
            ServicesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ServicesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ServicesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clipCheckBox)
                    .addComponent(cfCheckBox))
                .addGroup(ServicesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ServicesPanelLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(clirCheckBox))
                    .addGroup(ServicesPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cwCheckBox)))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        ServicesPanelLayout.setVerticalGroup(
            ServicesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ServicesPanelLayout.createSequentialGroup()
                .addGroup(ServicesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clipCheckBox)
                    .addComponent(clirCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ServicesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cfCheckBox)
                    .addComponent(cwCheckBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        broadbandPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("SubscriberDataInternalFrame.SubscriberDataPanel.Broadband.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        broadbandWiredCheckBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        broadbandWiredCheckBox.setText(bundle.getString("SubscriberDataInternalFrame.broadbandWiredCheckBox.text")); // NOI18N
        broadbandWiredCheckBox.setEnabled(false);
        broadbandWiredCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                broadbandWiredCheckBoxActionPerformed(evt);
            }
        });

        broadbandStateButtonGroup.add(broadbandEnabledRadioButton);
        broadbandEnabledRadioButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        broadbandEnabledRadioButton.setText(bundle.getString("SubscriberDataInternalFrame.broadbandEnabledRadioButton.text")); // NOI18N
        broadbandEnabledRadioButton.setEnabled(false);

        broadbandStateButtonGroup.add(broadbandDebtorRadioButton);
        broadbandDebtorRadioButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        broadbandDebtorRadioButton.setText(bundle.getString("SubscriberDataInternalFrame.broadbandDebtorRadioButton.text")); // NOI18N
        broadbandDebtorRadioButton.setEnabled(false);

        broadbandStateButtonGroup.add(broadbandDisabledRadioButton);
        broadbandDisabledRadioButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        broadbandDisabledRadioButton.setText(bundle.getString("SubscriberDataInternalFrame.broadbandDisabledRadioButton.text")); // NOI18N
        broadbandDisabledRadioButton.setEnabled(false);

        javax.swing.GroupLayout broadbandPanelLayout = new javax.swing.GroupLayout(broadbandPanel);
        broadbandPanel.setLayout(broadbandPanelLayout);
        broadbandPanelLayout.setHorizontalGroup(
            broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(broadbandPanelLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(broadbandWiredCheckBox)
                .addGap(18, 18, 18)
                .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(broadbandPanelLayout.createSequentialGroup()
                        .addComponent(broadbandDisabledRadioButton)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(broadbandPanelLayout.createSequentialGroup()
                        .addComponent(broadbandEnabledRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(broadbandDebtorRadioButton)
                        .addGap(26, 26, 26))))
        );
        broadbandPanelLayout.setVerticalGroup(
            broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(broadbandPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(broadbandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(broadbandPanelLayout.createSequentialGroup()
                        .addComponent(broadbandEnabledRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(broadbandDisabledRadioButton)
                        .addContainerGap(14, Short.MAX_VALUE))
                    .addGroup(broadbandPanelLayout.createSequentialGroup()
                        .addComponent(broadbandDebtorRadioButton)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(broadbandPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(broadbandWiredCheckBox)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        informationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("SubscriberDataInternalFrame.SubscriberInformationPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        informationTextArea.setColumns(30);
        informationTextArea.setLineWrap(true);
        informationTextArea.setRows(5);
        informationTextArea.setEnabled(false);
        jScrollPane3.setViewportView(informationTextArea);

        javax.swing.GroupLayout informationPanelLayout = new javax.swing.GroupLayout(informationPanel);
        informationPanel.setLayout(informationPanelLayout);
        informationPanelLayout.setHorizontalGroup(
            informationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(informationPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        informationPanelLayout.setVerticalGroup(
            informationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout subscriberDataPanelLayout = new javax.swing.GroupLayout(subscriberDataPanel);
        subscriberDataPanel.setLayout(subscriberDataPanelLayout);
        subscriberDataPanelLayout.setHorizontalGroup(
            subscriberDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subscriberDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(subscriberDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(informationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(subscriberDataPanelLayout.createSequentialGroup()
                        .addGroup(subscriberDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(ServicesPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(terminationRestrictionPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(subscriberDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(originationRestrictionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(broadbandPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(subscriberDataPanelLayout.createSequentialGroup()
                        .addComponent(lineTypeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lineTypeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        subscriberDataPanelLayout.setVerticalGroup(
            subscriberDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(subscriberDataPanelLayout.createSequentialGroup()
                .addGroup(subscriberDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lineTypeLabel)
                    .addComponent(lineTypeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(subscriberDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(originationRestrictionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(terminationRestrictionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(subscriberDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(broadbandPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ServicesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(informationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        changeButton.setText(bundle.getString("SubscriberDataInternalFrame.changeButton.text")); // NOI18N
        changeButton.setEnabled(false);
        changeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeButtonActionPerformed(evt);
            }
        });

        queryButton.setText(bundle.getString("SubscriberDataInternalFrame.queryButton.text")); // NOI18N
        queryButton.setEnabled(false);

        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(queryButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(changeButton)
                .addContainerGap())
        );
        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(changeButton)
                    .addComponent(queryButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("SubscriberDataInternalFrame.SubscriberDataPanel.State.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        stateButtonGroup.add(enabledRadioButton);
        enabledRadioButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        enabledRadioButton.setText(bundle.getString("SubscriberDataInternalFrame.enabledRadioButton.text")); // NOI18N
        enabledRadioButton.setEnabled(false);

        stateButtonGroup.add(debtorRadioButton);
        debtorRadioButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        debtorRadioButton.setText(bundle.getString("SubscriberDataInternalFrame.debtorRadioButton.text")); // NOI18N
        debtorRadioButton.setEnabled(false);

        stateButtonGroup.add(disabledRadioButton);
        disabledRadioButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        disabledRadioButton.setText(bundle.getString("SubscriberDataInternalFrame.disabledRadioButton.text")); // NOI18N
        disabledRadioButton.setEnabled(false);

        javax.swing.GroupLayout statePanelLayout = new javax.swing.GroupLayout(statePanel);
        statePanel.setLayout(statePanelLayout);
        statePanelLayout.setHorizontalGroup(
            statePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(enabledRadioButton)
                .addGap(18, 18, 18)
                .addComponent(debtorRadioButton)
                .addGap(18, 18, 18)
                .addComponent(disabledRadioButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        statePanelLayout.setVerticalGroup(
            statePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(enabledRadioButton)
                    .addComponent(debtorRadioButton)
                    .addComponent(disabledRadioButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(subscribersListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subscriberDataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(subscriberLinePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addComponent(statePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(subscribersListScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(subscriberLinePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(statePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(subscriberDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        open = false;
    }//GEN-LAST:event_formInternalFrameClosed

    private void terminationRestrictionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_terminationRestrictionComboBoxActionPerformed
        terminationRestrictionTextArea.setText(((TerminationRestriction) terminationRestrictionComboBox.getSelectedItem()).getDescription());
    }//GEN-LAST:event_terminationRestrictionComboBoxActionPerformed

    private void originationRestrictionComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_originationRestrictionComboBoxActionPerformed
        originationRestrictionTextArea.setText(((OriginationRestriction) originationRestrictionComboBox.getSelectedItem()).getDescription());
    }//GEN-LAST:event_originationRestrictionComboBoxActionPerformed

    private void subscribersListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_subscribersListMouseClicked
        clearForm();
        selectedSubscriber = subscribersList.getSelectedValue();
        Connection connection = null;
        try {
            databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
            connection = databaseConnectionsManager.takeConnection();
            selectedSubscriber.fillSubscriberData(connection);
            fillSubscriberDataForm();
            if (privilegedUser)
                enableChangeableComponents(true);
        } catch (Exception exception) {
            logger.error("Cannot get subscriber data.", exception);
            JOptionPane.showMessageDialog(this, bundle.getString("SubscribersDataInternalFrame.subscribersData.problem"), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            databaseConnectionsManager.returnConnection(connection);
        }
    }//GEN-LAST:event_subscribersListMouseClicked

    private void broadbandWiredCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_broadbandWiredCheckBoxActionPerformed
        if (broadbandWiredCheckBox.isSelected())
            enableBroadbandRadioButtons(true);
        else
            enableBroadbandRadioButtons(false);
    }//GEN-LAST:event_broadbandWiredCheckBoxActionPerformed

    private void changeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_changeButtonActionPerformed
        Connection connection = null;
        String subscriberNumber = selectedSubscriber.getPhoneNumber();
        try {
            databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
            connection = databaseConnectionsManager.takeConnection();
            SubscriberData selectedData = selectedSubscriber.getData();
            fillSubscriberData(selectedData);
            selectedData.update(connection, subscriberNumber, user.getId());
            JOptionPane.showMessageDialog(this, subscriberNumber + bundle.getString("SubscribersDataInternalFrame.updateSubscribersData"), bundle.getString("SubscribersDataInternalFrame.updateSubscribersData.title"), JOptionPane.INFORMATION_MESSAGE);
            subscribersList.clearSelection();
            clearForm();
        } catch (Exception exception) {
            logger.error("Couldn't update subscriber data. (" + subscriberNumber + ")", exception);
            JOptionPane.showMessageDialog(this, bundle.getString("SubscribersDataInternalFrame.updateSubscribersData.problem") + " " + subscriberNumber, "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            databaseConnectionsManager.returnConnection(connection);
        }
    }//GEN-LAST:event_changeButtonActionPerformed

    private void clearForm() {
        lineNumberTextField.setText("");
        holderTextField.setText("");
        stateButtonGroup.clearSelection();
        lineTypeTextField.setText("");
        terminationRestrictionComboBox.setSelectedIndex(0);
        originationRestrictionComboBox.setSelectedIndex(0);
        clipCheckBox.setSelected(false);
        clirCheckBox.setSelected(false);
        cfCheckBox.setSelected(false);
        cwCheckBox.setSelected(false);
        broadbandWiredCheckBox.setSelected(false);
        broadbandStateButtonGroup.clearSelection();
        informationTextArea.setText("");

        enableChangeableComponents(false);
    }

    private void fillSubscriberDataForm() {
        lineNumberTextField.setText(selectedSubscriber.getPhoneNumber());
        holderTextField.setText(selectedSubscriber.getPerson().toString());
        SubscriberData selectedSubscriberData = selectedSubscriber.getData();

        switch (selectedSubscriberData.getState().getName()) {
            case DEBTOR:
                debtorRadioButton.setSelected(true);
                break;
            case DISABLED:
                disabledRadioButton.setSelected(true);
                break;
            case ENABLED:
                enabledRadioButton.setSelected(true);
        }

        lineTypeTextField.setText(bundle.getString("SubscribersDataInternalFrame.subscribersLineClassType." + selectedSubscriberData.getLineClass().getType()));

        SubscriberRestriction selectedSubscriberRestriction = selectedSubscriberData.getRestriction();
        terminationRestrictionComboBox.setSelectedItem(selectedSubscriberRestriction.getTerminationRestriction());
        originationRestrictionComboBox.setSelectedItem(selectedSubscriberRestriction.getOriginationRestriction());

        List<SubscriberService> selectedSubscriberServicesList = selectedSubscriberData.getServicesList();
        for (SubscriberService service : selectedSubscriberServicesList) {
            switch (service.getName()) {
                case "clip":
                    clipCheckBox.setSelected(true);
                    break;
                case "clir":
                    clirCheckBox.setSelected(true);
                    break;
                case "cf":
                    cfCheckBox.setSelected(true);
                    break;
                case "cw":
                    cwCheckBox.setSelected(true);
            }

        }

        SubscriberBroadbandState broadbandState = selectedSubscriberData.getBroadbandState();
        if (broadbandState != null) {
            broadbandWiredCheckBox.setSelected(true);
            switch (broadbandState.getName()) {
                case ENABLED:
                    broadbandEnabledRadioButton.setSelected(true);
                    break;
                case DEBTOR:
                    broadbandDebtorRadioButton.setSelected(true);
                    break;
                case DISABLED:
                    broadbandDisabledRadioButton.setSelected(true);
            }
        }

        informationTextArea.setText(selectedSubscriberData.getInformation());
    }

    private void enableChangeableComponents(boolean enabled) {
        enabledRadioButton.setEnabled(enabled);
        debtorRadioButton.setEnabled(enabled);
        disabledRadioButton.setEnabled(enabled);
        terminationRestrictionComboBox.setEnabled(enabled);
        originationRestrictionComboBox.setEnabled(enabled);
        clipCheckBox.setEnabled(enabled);
        clirCheckBox.setEnabled(enabled);
        cfCheckBox.setEnabled(enabled);
        cwCheckBox.setEnabled(enabled);
        if (!enabled)
            enableBroadbandRadioButtons(false);
        else if (broadbandWiredCheckBox.isSelected())
            enableBroadbandRadioButtons(true);
        informationTextArea.setEnabled(enabled);
        changeButton.setEnabled(enabled);
    }

    private void enableBroadbandRadioButtons(boolean enabled) {
        broadbandEnabledRadioButton.setEnabled(enabled);
        broadbandDebtorRadioButton.setEnabled(enabled);
        broadbandDisabledRadioButton.setEnabled(enabled);
        if (!enabled)
            broadbandStateButtonGroup.clearSelection();
    }

    private void fillSubscriberData(SubscriberData data) {
        String state = null;
        if (enabledRadioButton.isSelected())
            state = "ENABLED";
        else if (debtorRadioButton.isSelected())
            state = "DEBTOR";
        else if (disabledRadioButton.isSelected())
            state = "DISABLED";
        data.setState(SubscriberStatesManager.getSubscriberState(SubscriberStateName.valueOf(state)));

        int terminationRestrictionId = ((TerminationRestriction) terminationRestrictionComboBox.getSelectedItem()).getId();
        int originationRestrictionId = ((OriginationRestriction) originationRestrictionComboBox.getSelectedItem()).getId();
        data.setRestriction(SubscriberRestrictionsManager.getSubscriberRestriction(terminationRestrictionId, originationRestrictionId));

        List<SubscriberService> servicesList = new ArrayList<>();
        if (clipCheckBox.isSelected())
            servicesList.add(SubscriberServicesManager.getSubscriberService("clip"));
        if (clirCheckBox.isSelected())
            servicesList.add(SubscriberServicesManager.getSubscriberService("clir"));
        if (cfCheckBox.isSelected())
            servicesList.add(SubscriberServicesManager.getSubscriberService("cf"));
        if (cwCheckBox.isSelected())
            servicesList.add(SubscriberServicesManager.getSubscriberService("cw"));
        data.setServicesList(servicesList);

        if (broadbandWiredCheckBox.isSelected()) {
            String broadbandState = null;
            if (broadbandEnabledRadioButton.isSelected())
                broadbandState = "ENABLED";
            else if (broadbandDebtorRadioButton.isSelected())
                broadbandState = "DEBTOR";
            else if (broadbandDisabledRadioButton.isSelected())
                broadbandState = "DISABLED";
            data.setBroadbandState(SubscriberBroadbandStatesManager.getSubscriberBroadbandState(SubscriberBroadbandStateName.valueOf(broadbandState)));
        }

        if (!informationTextArea.getText().isEmpty())
            data.setInformation(informationTextArea.getText());
        else
            data.setInformation(null);
    }

    public static boolean isOpen() {
        return open;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ServicesPanel;
    private javax.swing.JRadioButton broadbandDebtorRadioButton;
    private javax.swing.JRadioButton broadbandDisabledRadioButton;
    private javax.swing.JRadioButton broadbandEnabledRadioButton;
    private javax.swing.JPanel broadbandPanel;
    private javax.swing.ButtonGroup broadbandStateButtonGroup;
    private javax.swing.JCheckBox broadbandWiredCheckBox;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JCheckBox cfCheckBox;
    private javax.swing.JButton changeButton;
    private javax.swing.JCheckBox clipCheckBox;
    private javax.swing.JCheckBox clirCheckBox;
    private javax.swing.JCheckBox cwCheckBox;
    private javax.swing.JRadioButton debtorRadioButton;
    private javax.swing.JRadioButton disabledRadioButton;
    private javax.swing.JRadioButton enabledRadioButton;
    private javax.swing.JLabel holderLabel;
    private javax.swing.JTextField holderTextField;
    private javax.swing.JPanel informationPanel;
    private javax.swing.JTextArea informationTextArea;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lineNumberLabel;
    private javax.swing.JTextField lineNumberTextField;
    private javax.swing.JLabel lineTypeLabel;
    private javax.swing.JTextField lineTypeTextField;
    private javax.swing.JComboBox<OriginationRestriction> originationRestrictionComboBox;
    private javax.swing.JLabel originationRestrictionLabel;
    private javax.swing.JPanel originationRestrictionPanel;
    private javax.swing.JTextArea originationRestrictionTextArea;
    private javax.swing.JButton queryButton;
    private javax.swing.ButtonGroup stateButtonGroup;
    private javax.swing.JPanel statePanel;
    private javax.swing.JPanel subscriberDataPanel;
    private javax.swing.JPanel subscriberLinePanel;
    private javax.swing.JList<Subscriber> subscribersList;
    private javax.swing.JScrollPane subscribersListScrollPane;
    private javax.swing.JComboBox<TerminationRestriction> terminationRestrictionComboBox;
    private javax.swing.JLabel terminationRestrictionLabel;
    private javax.swing.JPanel terminationRestrictionPanel;
    private javax.swing.JTextArea terminationRestrictionTextArea;
    // End of variables declaration//GEN-END:variables

    private DatabaseConnectionsManager databaseConnectionsManager;
    private User user;
    private boolean privilegedUser;

    // Models.
    private DefaultListModel<Subscriber> subscribersListModel;
    private DefaultComboBoxModel<TerminationRestriction> terminationRestrictionComboBoxModel;
    private DefaultComboBoxModel<OriginationRestriction> originationRestrictionComboBoxModel;

    private Subscriber selectedSubscriber;

    private static boolean open;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/SubscriberDataInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(SubscriberDataInternalFrame.class);
}
