/*
 * 		MainWindowJFrame.java
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
 * 		MainFrame.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Jun 20, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.Broadband;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.person.Person;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Phone;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.repairs.ServiceOrder;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriber.Subscriber;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.SubscriberData;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.User;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetCable;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.Wiring;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.AreaCreator;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.CableSelection;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.DateSelection;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.DatesPeriod;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.Login;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.OwnPhoneNumber;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.SearchPerson;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.ServiceOrderQuery;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.UserCreator;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     *
     * @param user
     */
    public MainFrame(User user) {
        UIManager.put("ComboBox.disabledForeground", Color.BLACK);
        URL resource = getClass().getResource("/images/rubik.jpg");
        try {
            BufferedImage image = ImageIO.read(resource);
            setIconImage(image);
        } catch (IOException ex) {
            logger.error("Cannot read icon image.", ex);
        }
        
        this.user = user;
        databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
        initComponents();
        userLabel.setText(user.getUsername());

        enableTools();
    }

    private void enableTools() {
        if (user.hasRole("administrator")) {
            // Menus.
            descriptorsMenu.setEnabled(true);
            managementMenu.setEnabled(true);

            // Menu items
            addAreaMenuItem.setEnabled(true);
            addUserMenuItem.setEnabled(true);
        }

        if (user.hasRole("assignment-operator") || user.hasRole("assignment-observer")) {
            // Menus
            reportsMenu.setEnabled(true);
            subscribersMenu.setEnabled(true);
            // Menu items
            subscriberRecordMenuItem.setEnabled(true);
            // Buttons.
            assignmentButton.setEnabled(true);
        }

        if (user.hasRole("cables-pairs-operator") || user.hasRole("cables-pairs-observer")) {
            // Buttons
            cablesPairsButton.setEnabled(true);
        }

        if (user.hasRole("dslams-operator") || user.hasRole("dslams-observer")) {
            // Buttons.
            dslamsButton.setEnabled(true);
        }

        if (user.hasRole("equipment-operator") || user.hasRole("equipment-observer")) {
            // Buttons.
            equipmentButton.setEnabled(true);
        }

        if (user.hasRole("people-operator") || user.hasRole("people-observer")) {
            // Buttons.
            peopleButton.setEnabled(true);
        }

        if (user.hasRole("repairs-operator") || user.hasRole("repairs-observer")) {
            // Menus
            reportsMenu.setEnabled(true);
            repairsMenu.setEnabled(true);
            // Menu items
            serviceOrderMenuItem.setEnabled(true);
            repairsBySubscriberMenuItem.setEnabled(true);
            // Buttons.
            repairsButton.setEnabled(true);
        }

        if (user.hasRole("subscriber-data-operator") || user.hasRole("subscriber-data-observer")) {
            // Menus
            reportsMenu.setEnabled(true);
            subscribersMenu.setEnabled(true);
            // Menu items
            subscriberDataRecordMenuItem.setEnabled(true);
            modifiedSubscribersDataMenuItem.setEnabled(true);
            subscribersDataModificationsMenuItem.setEnabled(true);
            // Buttons.
            subscriberDataButton.setEnabled(true);
        }

        if (user.hasRole("wiring-operator") || user.hasRole("wiring-observer")) {
            // Menus
            reportsMenu.setEnabled(true);
            broadbandMenu.setEnabled(true);
            wiringMenu.setEnabled(true);
            // Menu items
            broadbandInstallationMenuItem.setEnabled(true);
            broadbandUninstallationMenuItem.setEnabled(true);
            installedBroadbandMenuItem.setEnabled(true);
            notInstalledBroadbandMenuItem.setEnabled(true);
//            wiringRecordMenuItem.setEnabled(true);
            wiringSummaryMenuItem.setEnabled(true);
            // Buttons.
            wiringButton.setEnabled(true);
            distributionButton.setEnabled(true);
        }

        if (user.hasRole("broadband-observer")) {
            broadbandButton.setEnabled(true);
        }
    }

    private void disableTools() {
        // Menus.
        descriptorsMenu.setEnabled(false);
        reportsMenu.setEnabled(false);
        subscribersMenu.setEnabled(false);
        broadbandMenu.setEnabled(false);
        wiringMenu.setEnabled(false);
        repairsMenu.setEnabled(false);
        managementMenu.setEnabled(false);

        // Menu items.
        addAreaMenuItem.setEnabled(false);
        subscriberRecordMenuItem.setEnabled(false);
        subscriberDataRecordMenuItem.setEnabled(false);
        modifiedSubscribersDataMenuItem.setEnabled(false);
        subscribersDataModificationsMenuItem.setEnabled(false);
        broadbandInstallationMenuItem.setEnabled(false);
        broadbandUninstallationMenuItem.setEnabled(false);
        installedBroadbandMenuItem.setEnabled(false);
        notInstalledBroadbandMenuItem.setEnabled(false);
//        wiringRecordMenuItem.setEnabled(false);
        wiringSummaryMenuItem.setEnabled(false);
        serviceOrderMenuItem.setEnabled(false);
        repairsBySubscriberMenuItem.setEnabled(false);

        // Buttons.
        peopleButton.setEnabled(false);
        cablesPairsButton.setEnabled(false);
        equipmentButton.setEnabled(false);
        assignmentButton.setEnabled(false);
        dslamsButton.setEnabled(false);
        wiringButton.setEnabled(false);
        distributionButton.setEnabled(false);
        broadbandButton.setEnabled(false);
        subscriberDataButton.setEnabled(false);
        repairsButton.setEnabled(false);

        // Internal frames.
        if (PeopleInternalFrame.isOpen())
            peopleInternalFrame.dispose();
        if (CablesPairsInternalFrame.isOpen())
            cablesPairsInternalFrame.dispose();
        if (EquipmentInternalFrame.isOpen())
            equipmentInternalFrame.dispose();
        if (AssignmentInternalFrame.isOpen())
            assignmentInternalFrame.dispose();
        if (DSLAMsInternalFrame.isOpen())
            dslamsInternalFrame.dispose();
        if (WiringInternalFrame.isOpen())
            wiringInternalFrame.dispose();
        if (DistributionInternalFrame.isOpen())
            distributorInternalFrame.dispose();
        if (BroadbandInternalFrame.isOpen())
            broadbandInternalFrame.dispose();
        if (SubscriberDataInternalFrame.isOpen())
            subscriberDataInternalFrame.dispose();
        if (RepairsInternalFrame.isOpen())
            repairsInternalFrame.dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        statusPanel = new javax.swing.JPanel();
        userLabel = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();
        peopleButton = new javax.swing.JButton();
        cablesPairsButton = new javax.swing.JButton();
        equipmentButton = new javax.swing.JButton();
        assignmentButton = new javax.swing.JButton();
        dslamsButton = new javax.swing.JButton();
        wiringButton = new javax.swing.JButton();
        subscriberDataButton = new javax.swing.JButton();
        systemManagerSeparator = new javax.swing.JSeparator();
        subscriberDataSeparator = new javax.swing.JSeparator();
        repairsButton = new javax.swing.JButton();
        distributionButton = new javax.swing.JButton();
        broadbandButton = new javax.swing.JButton();
        workAreaDesktopPane = new javax.swing.JDesktopPane();
        mainMenuBar = new javax.swing.JMenuBar();
        sessionMenu = new javax.swing.JMenu();
        logInMenuItem = new javax.swing.JMenuItem();
        logOutMenuItem = new javax.swing.JMenuItem();
        descriptorsMenu = new javax.swing.JMenu();
        addAreaMenuItem = new javax.swing.JMenuItem();
        managementMenu = new javax.swing.JMenu();
        addUserMenuItem = new javax.swing.JMenuItem();
        reportsMenu = new javax.swing.JMenu();
        broadbandMenu = new javax.swing.JMenu();
        broadbandInstallationMenuItem = new javax.swing.JMenuItem();
        broadbandUninstallationMenuItem = new javax.swing.JMenuItem();
        installedBroadbandMenuItem = new javax.swing.JMenuItem();
        notInstalledBroadbandMenuItem = new javax.swing.JMenuItem();
        repairsMenu = new javax.swing.JMenu();
        serviceOrderMenuItem = new javax.swing.JMenuItem();
        repairsBySubscriberMenuItem = new javax.swing.JMenuItem();
        subscribersMenu = new javax.swing.JMenu();
        subscriberRecordMenuItem = new javax.swing.JMenuItem();
        subscriberDataRecordMenuItem = new javax.swing.JMenuItem();
        modifiedSubscribersDataMenuItem = new javax.swing.JMenuItem();
        subscribersDataModificationsMenuItem = new javax.swing.JMenuItem();
        wiringMenu = new javax.swing.JMenu();
        wiringSummaryMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("i18n/MainFrameBundle"); // NOI18N
        setTitle(bundle.getString("MainFrame.title_1")); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        statusPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        userLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statusPanelLayout.createSequentialGroup()
                .addContainerGap(918, Short.MAX_VALUE)
                .addComponent(userLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(userLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        buttonsPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));

        peopleButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        peopleButton.setText(bundle.getString("MainFrame.peopleButton.text_1")); // NOI18N
        peopleButton.setEnabled(false);
        peopleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                peopleButtonActionPerformed(evt);
            }
        });

        cablesPairsButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cablesPairsButton.setText(bundle.getString("MainFrame.cablesPairsButton.text")); // NOI18N
        cablesPairsButton.setEnabled(false);
        cablesPairsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cablesPairsButtonActionPerformed(evt);
            }
        });

        equipmentButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        equipmentButton.setText(bundle.getString("MainFrame.equipmentButton.text")); // NOI18N
        equipmentButton.setEnabled(false);
        equipmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                equipmentButtonActionPerformed(evt);
            }
        });

        assignmentButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        assignmentButton.setText(bundle.getString("MainFrame.assignmentButton.text")); // NOI18N
        assignmentButton.setEnabled(false);
        assignmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assignmentButtonActionPerformed(evt);
            }
        });

        dslamsButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        dslamsButton.setText(bundle.getString("MainFrame.dslamsButton.text")); // NOI18N
        dslamsButton.setEnabled(false);
        dslamsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dslamsButtonActionPerformed(evt);
            }
        });

        wiringButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        wiringButton.setText(bundle.getString("MainFrame.wiringButton.text")); // NOI18N
        wiringButton.setEnabled(false);
        wiringButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wiringButtonActionPerformed(evt);
            }
        });

        subscriberDataButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        subscriberDataButton.setText(bundle.getString("MainFrame.subscriberDataButton.text")); // NOI18N
        subscriberDataButton.setEnabled(false);
        subscriberDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subscriberDataButtonActionPerformed(evt);
            }
        });

        repairsButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        repairsButton.setText(bundle.getString("MainFrame.repairsButton.text")); // NOI18N
        repairsButton.setEnabled(false);
        repairsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repairsButtonActionPerformed(evt);
            }
        });

        distributionButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        distributionButton.setText(bundle.getString("MainFrame.distributionButton.text")); // NOI18N
        distributionButton.setEnabled(false);
        distributionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                distributionButtonActionPerformed(evt);
            }
        });

        broadbandButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        broadbandButton.setText(bundle.getString("MainFrame.broadbandButton.text")); // NOI18N
        broadbandButton.setEnabled(false);
        broadbandButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                broadbandButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(equipmentButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cablesPairsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(peopleButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(assignmentButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dslamsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(wiringButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subscriberDataButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subscriberDataSeparator, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(systemManagerSeparator)
                    .addComponent(repairsButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(distributionButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(broadbandButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(peopleButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cablesPairsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(equipmentButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(assignmentButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dslamsButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(wiringButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(distributionButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(broadbandButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(systemManagerSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(subscriberDataButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(subscriberDataSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(repairsButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        buttonsPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {subscriberDataSeparator, systemManagerSeparator});

        javax.swing.GroupLayout workAreaDesktopPaneLayout = new javax.swing.GroupLayout(workAreaDesktopPane);
        workAreaDesktopPane.setLayout(workAreaDesktopPaneLayout);
        workAreaDesktopPaneLayout.setHorizontalGroup(
            workAreaDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1004, Short.MAX_VALUE)
        );
        workAreaDesktopPaneLayout.setVerticalGroup(
            workAreaDesktopPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 636, Short.MAX_VALUE)
        );

        sessionMenu.setText(bundle.getString("MainFrame.sessionMenu.text_1")); // NOI18N

        logInMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        logInMenuItem.setText(bundle.getString("MainFrame.logInMenuItem.text_1")); // NOI18N
        logInMenuItem.setEnabled(false);
        logInMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logInMenuItemActionPerformed(evt);
            }
        });
        sessionMenu.add(logInMenuItem);

        logOutMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        logOutMenuItem.setText(bundle.getString("MainFrame.logOutMenuItem.text_1")); // NOI18N
        logOutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutMenuItemActionPerformed(evt);
            }
        });
        sessionMenu.add(logOutMenuItem);

        mainMenuBar.add(sessionMenu);

        descriptorsMenu.setText(bundle.getString("MainFrame.descriptorsMenu.text")); // NOI18N
        descriptorsMenu.setEnabled(false);

        addAreaMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        addAreaMenuItem.setText(bundle.getString("MainFrame.addAreaMenuItem.text")); // NOI18N
        addAreaMenuItem.setEnabled(false);
        addAreaMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAreaMenuItemActionPerformed(evt);
            }
        });
        descriptorsMenu.add(addAreaMenuItem);

        mainMenuBar.add(descriptorsMenu);

        managementMenu.setText(bundle.getString("MainFrame.managementMenu.text")); // NOI18N
        managementMenu.setEnabled(false);

        addUserMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        addUserMenuItem.setText(bundle.getString("MainFrame.addUserMenuItem.text")); // NOI18N
        addUserMenuItem.setEnabled(false);
        addUserMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserMenuItemActionPerformed(evt);
            }
        });
        managementMenu.add(addUserMenuItem);

        mainMenuBar.add(managementMenu);

        reportsMenu.setText(bundle.getString("MainFrame.reportsMenu.text")); // NOI18N
        reportsMenu.setEnabled(false);

        broadbandMenu.setText(bundle.getString("MainFrame.broadbandMenu.text")); // NOI18N
        broadbandMenu.setEnabled(false);

        broadbandInstallationMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        broadbandInstallationMenuItem.setText(bundle.getString("MainFrame.broadbandInstallationMenuItem.text")); // NOI18N
        broadbandInstallationMenuItem.setEnabled(false);
        broadbandInstallationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                broadbandInstallationMenuItemActionPerformed(evt);
            }
        });
        broadbandMenu.add(broadbandInstallationMenuItem);

        broadbandUninstallationMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        broadbandUninstallationMenuItem.setText(bundle.getString("MainFrame.broadbandUninstallationMenuItem.text")); // NOI18N
        broadbandUninstallationMenuItem.setEnabled(false);
        broadbandUninstallationMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                broadbandUninstallationMenuItemActionPerformed(evt);
            }
        });
        broadbandMenu.add(broadbandUninstallationMenuItem);

        installedBroadbandMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        installedBroadbandMenuItem.setText(bundle.getString("MainFrame.installedBroadbandMenuItem.text")); // NOI18N
        installedBroadbandMenuItem.setEnabled(false);
        installedBroadbandMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                installedBroadbandMenuItemActionPerformed(evt);
            }
        });
        broadbandMenu.add(installedBroadbandMenuItem);

        notInstalledBroadbandMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        notInstalledBroadbandMenuItem.setText(bundle.getString("MainFrame.notInstalledBroadbandMenuItem.text")); // NOI18N
        notInstalledBroadbandMenuItem.setEnabled(false);
        notInstalledBroadbandMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notInstalledBroadbandMenuItemActionPerformed(evt);
            }
        });
        broadbandMenu.add(notInstalledBroadbandMenuItem);

        reportsMenu.add(broadbandMenu);

        repairsMenu.setText(bundle.getString("MainFrame.repairsMenu.text")); // NOI18N
        repairsMenu.setEnabled(false);

        serviceOrderMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        serviceOrderMenuItem.setText(bundle.getString("MainFrame.serviceOrderMenuItem.text")); // NOI18N
        serviceOrderMenuItem.setEnabled(false);
        serviceOrderMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serviceOrderMenuItemActionPerformed(evt);
            }
        });
        repairsMenu.add(serviceOrderMenuItem);

        repairsBySubscriberMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        repairsBySubscriberMenuItem.setText(bundle.getString("MainFrame.repairsBySubscriberMenuItem.text")); // NOI18N
        repairsBySubscriberMenuItem.setEnabled(false);
        repairsBySubscriberMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                repairsBySubscriberMenuItemActionPerformed(evt);
            }
        });
        repairsMenu.add(repairsBySubscriberMenuItem);

        reportsMenu.add(repairsMenu);

        subscribersMenu.setText(bundle.getString("MainFrame.subscribersMenu.text")); // NOI18N
        subscribersMenu.setEnabled(false);

        subscriberRecordMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        subscriberRecordMenuItem.setText(bundle.getString("MainFrame.subscriberRecordMenuItem.text")); // NOI18N
        subscriberRecordMenuItem.setEnabled(false);
        subscriberRecordMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subscriberRecordMenuItemActionPerformed(evt);
            }
        });
        subscribersMenu.add(subscriberRecordMenuItem);

        subscriberDataRecordMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        subscriberDataRecordMenuItem.setText(bundle.getString("MainFrame.subscriberDataRecordMenuItem.text")); // NOI18N
        subscriberDataRecordMenuItem.setEnabled(false);
        subscriberDataRecordMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subscriberDataRecordMenuItemActionPerformed(evt);
            }
        });
        subscribersMenu.add(subscriberDataRecordMenuItem);

        modifiedSubscribersDataMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        modifiedSubscribersDataMenuItem.setText(bundle.getString("MainFrame.modifiedSubscribersDataMenuItem.text")); // NOI18N
        modifiedSubscribersDataMenuItem.setEnabled(false);
        modifiedSubscribersDataMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modifiedSubscribersDataMenuItemActionPerformed(evt);
            }
        });
        subscribersMenu.add(modifiedSubscribersDataMenuItem);

        subscribersDataModificationsMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        subscribersDataModificationsMenuItem.setText(bundle.getString("MainFrame.subscribersDataModificationsMenuItem.text")); // NOI18N
        subscribersDataModificationsMenuItem.setEnabled(false);
        subscribersDataModificationsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subscribersDataModificationsMenuItemActionPerformed(evt);
            }
        });
        subscribersMenu.add(subscribersDataModificationsMenuItem);

        reportsMenu.add(subscribersMenu);

        wiringMenu.setText(bundle.getString("MainFrame.wiringMenu.text")); // NOI18N
        wiringMenu.setEnabled(false);

        wiringSummaryMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        wiringSummaryMenuItem.setText(bundle.getString("MainFrame.wiringSummaryMenuItem.text")); // NOI18N
        wiringSummaryMenuItem.setEnabled(false);
        wiringSummaryMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wiringSummaryMenuItemActionPerformed(evt);
            }
        });
        wiringMenu.add(wiringSummaryMenuItem);

        reportsMenu.add(wiringMenu);

        mainMenuBar.add(reportsMenu);

        helpMenu.setText(bundle.getString("MainFrame.helpMenu.text")); // NOI18N

        aboutMenuItem.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        aboutMenuItem.setText(bundle.getString("MainFrame.aboutMenuItem.text")); // NOI18N
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        helpMenu.add(aboutMenuItem);

        mainMenuBar.add(helpMenu);

        setJMenuBar(mainMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(statusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(workAreaDesktopPane)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(workAreaDesktopPane)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
//        DatabaseConnectionsManager databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
        if (!databaseConnectionsManager.closeConnections()) {
            logger.error("Any connections could not be closed.");
        }

        logger.info("End of Derqui Systems Manager.");
    }//GEN-LAST:event_formWindowClosing

    private void logOutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutMenuItemActionPerformed
        logger.info(user.getUsername() + " was loged out.");
        user = null;
        disableTools();
        userLabel.setText("");
        logInMenuItem.setEnabled(true);
        logOutMenuItem.setEnabled(false);
    }//GEN-LAST:event_logOutMenuItemActionPerformed

    private void logInMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logInMenuItemActionPerformed
        user = Login.logUser();
        if (user != null) {
            logger.info("User " + user + " is logged in.");
            enableTools();
            userLabel.setText(user.getUsername());
            logInMenuItem.setEnabled(false);
            logOutMenuItem.setEnabled(true);
        } else {
//            DatabaseConnectionsManager databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
            if (!databaseConnectionsManager.closeConnections()) {
                logger.error("Any connections could not be closed.");
            }

            logger.info("End of Derqui Systems Manager.");
            this.dispose();
        }
    }//GEN-LAST:event_logInMenuItemActionPerformed

    private void peopleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_peopleButtonActionPerformed
        if (!PeopleInternalFrame.isOpen()) {
            peopleInternalFrame = new PeopleInternalFrame(user);
            workAreaDesktopPane.add(peopleInternalFrame);
            peopleInternalFrame.setVisible(true);
            logger.debug("People internal frame opened.");
        }
    }//GEN-LAST:event_peopleButtonActionPerformed

    private void cablesPairsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cablesPairsButtonActionPerformed
        if (!CablesPairsInternalFrame.isOpen()) {
            cablesPairsInternalFrame = new CablesPairsInternalFrame(user);
            workAreaDesktopPane.add(cablesPairsInternalFrame);
            cablesPairsInternalFrame.setVisible(true);
            logger.debug("Cables and Pairs internal frame opened.");
        }
    }//GEN-LAST:event_cablesPairsButtonActionPerformed

    private void equipmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_equipmentButtonActionPerformed
        if (!EquipmentInternalFrame.isOpen()) {
            equipmentInternalFrame = new EquipmentInternalFrame(user);
            workAreaDesktopPane.add(equipmentInternalFrame);
            equipmentInternalFrame.setVisible(true);
            logger.debug("Equipment internal frame opened.");
        }
     }//GEN-LAST:event_equipmentButtonActionPerformed

    private void assignmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assignmentButtonActionPerformed
        if (!AssignmentInternalFrame.isOpen()) {
            try {
                assignmentInternalFrame = new AssignmentInternalFrame(user);
                workAreaDesktopPane.add(assignmentInternalFrame);
                assignmentInternalFrame.setVisible(true);
                logger.debug("Assignment internal frame opened.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.Assignment.problem"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_assignmentButtonActionPerformed

    private void dslamsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dslamsButtonActionPerformed
        if (!DSLAMsInternalFrame.isOpen()) {
            try {
                dslamsInternalFrame = new DSLAMsInternalFrame(user);
                workAreaDesktopPane.add(dslamsInternalFrame);
                dslamsInternalFrame.setVisible(true);
                logger.debug("DSLAMs internal frame opened.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.DSLAMs.problem"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_dslamsButtonActionPerformed

    private void wiringButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wiringButtonActionPerformed
        if (!WiringInternalFrame.isOpen()) {
            try {
                wiringInternalFrame = new WiringInternalFrame(user);
                workAreaDesktopPane.add(wiringInternalFrame);
                wiringInternalFrame.setVisible(true);
                logger.debug("Wiring internal frame opened.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.Wiring.problem"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_wiringButtonActionPerformed

    private void addAreaMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAreaMenuItemActionPerformed
        AreaCreator areaCreator = new AreaCreator(this);
        areaCreator.create();
    }//GEN-LAST:event_addAreaMenuItemActionPerformed

    private void subscriberRecordMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subscriberRecordMenuItemActionPerformed
        OwnPhoneNumber subscriberRecordNumber = new OwnPhoneNumber(this);
        Phone phone = subscriberRecordNumber.getPhone();
        if (phone != null) {
            String fullNumber = phone.getFullNumeration() + ".";

            Connection connection = null;
            try {
                connection = databaseConnectionsManager.takeConnection();
                Workbook workbook = Subscriber.getRecord(connection, phone);
                logger.debug("Subscriber record was required for " + fullNumber);

                File file = new File(bundle.getString("Filename.Subscriber_Record") + ".xlsx");
                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.close();

                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);

            } catch (Exception exception) {
                logger.error("Cannot get subscriber record for " + fullNumber, exception);
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.SubscriberRecord.problem") + " " + fullNumber, "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }
    }//GEN-LAST:event_subscriberRecordMenuItemActionPerformed

    private void addUserMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserMenuItemActionPerformed
        SearchPerson searchPerson = new SearchPerson(this);
        Person person = searchPerson.search();
        if (person != null) {
            UserCreator userCreator = new UserCreator(this, person);
            userCreator.create();
        }

    }//GEN-LAST:event_addUserMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        if (!AboutInternalFrame.isOpen()) {
            aboutInternalFrame = new AboutInternalFrame();
            workAreaDesktopPane.add(aboutInternalFrame);
            aboutInternalFrame.setVisible(true);
            logger.debug("About internal frame opened.");
        }
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void broadbandInstallationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_broadbandInstallationMenuItemActionPerformed
        DatesPeriod datesPeriod = new DatesPeriod(this);
        datesPeriod.getDatesPeriod(bundle.getString("DatesPeriodPanel.BroadbandInstallation.title"));
        LocalDate toDate = null;
        LocalDate fromDate = datesPeriod.getFromDate();
        if (fromDate != null) {
            toDate = datesPeriod.getToDate();
            Connection connection = null;
            try {
                connection = databaseConnectionsManager.takeConnection();
                Workbook workbook = Broadband.getBroadbandInstallations(connection, fromDate, toDate);
                logger.debug("Broadband installation from " + fromDate + " to " + toDate + " was required.");

                File file = new File(bundle.getString("Filename.Broadband_Installation") + ".xlsx");
                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.close();

                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);

            } catch (Exception exception) {
                logger.error("Cannot get broadband installation  from " + fromDate + " to " + toDate + ".", exception);
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.BroadbandInstallation.problem") + " " + fromDate + "/" + toDate + ".", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }
    }//GEN-LAST:event_broadbandInstallationMenuItemActionPerformed

    private void broadbandUninstallationMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_broadbandUninstallationMenuItemActionPerformed
        DatesPeriod datesPeriod = new DatesPeriod(this);
        datesPeriod.getDatesPeriod(bundle.getString("DatesPeriodPanel.BroadbandUninstallation.title"));
        LocalDate fromDate = datesPeriod.getFromDate();
        LocalDate toDate = null;
        if (fromDate != null) {
            toDate = datesPeriod.getToDate();
            Connection connection = null;
            try {
                connection = databaseConnectionsManager.takeConnection();
                Workbook workbook = Broadband.getBroadbandUninstallations(connection, fromDate, toDate);
                logger.debug("Broadband uninstallation from " + fromDate + " to " + toDate + " was required.");

                File file = new File(bundle.getString("Filename.Broadband_Unistallation") + ".xlsx");
                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.close();

                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);

            } catch (Exception exception) {
                logger.error("Cannot get broadband uninstallation from " + fromDate + " to " + toDate + ".", exception);
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.BroadbandUninstallation.problem") + " " + fromDate + "/" + toDate + ".", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }
    }//GEN-LAST:event_broadbandUninstallationMenuItemActionPerformed

    private void installedBroadbandMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_installedBroadbandMenuItemActionPerformed
        DateSelection dateSelection = new DateSelection(this);
        dateSelection.getSelectedDate(bundle.getString("DateSelectionPanel.InstalledBroadband.title"));
        LocalDate date = dateSelection.getDate();
        if (date != null) {
            Connection connection = null;
            try {
                connection = databaseConnectionsManager.takeConnection();
                Workbook workbook = Broadband.getInstalledBroadband(connection, date);
                logger.debug("Installed broadband on " + date + " was required.");

                File file = new File(bundle.getString("Filename.Installed_Broadband") + ".xlsx");
                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.close();

                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);

            } catch (Exception exception) {
                logger.error("Cannot get installed broadband on " + date + ".", exception);
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.InstalledBroadband.problem") + " " + date + ".", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }
    }//GEN-LAST:event_installedBroadbandMenuItemActionPerformed

    private void notInstalledBroadbandMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notInstalledBroadbandMenuItemActionPerformed
        DateSelection dateSelection = new DateSelection(this);
        dateSelection.getSelectedDate(bundle.getString("DateSelectionPanel.NotInstalledBroadband.title"));
        LocalDate date = dateSelection.getDate();
        if (date != null) {
            Connection connection = null;
            try {
                connection = databaseConnectionsManager.takeConnection();
                Workbook workbook = Broadband.getNotInstalledBroadband(connection, date);
                logger.debug("Not installed broadband on " + date + " was required.");

                File file = new File(bundle.getString("Filename.Not_Installed_Broadband") + ".xlsx");
                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.close();

                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);

            } catch (Exception exception) {
                logger.error("Cannot get not installed broadband on " + date + ".", exception);
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.NotInstalledBroadband.problem") + " " + date + ".", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }
    }//GEN-LAST:event_notInstalledBroadbandMenuItemActionPerformed

    private void wiringSummaryMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wiringSummaryMenuItemActionPerformed
        CableSelection cableSelection = new CableSelection(this);
        StreetCable selectedCable = cableSelection.getSelectedCable(bundle.getString("CableSelectionPanel.title"));
        if (selectedCable != null) {
            Connection connection = null;
            try {
                connection = databaseConnectionsManager.takeConnection();
                Workbook workbook = Wiring.getWiringListByStreetCable(connection, selectedCable);
                logger.debug("Wiring summary for " + selectedCable + " cable was required.");

                File file = new File(bundle.getString("Filename.Wiring_Summary") + ".xlsx");
                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.close();

                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);

            } catch (Exception exception) {
                logger.error("Cannot get wiring summary for " + selectedCable + " cable.", exception);
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.WiringSummary.problem") + " " + selectedCable + " cable.", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }
    }//GEN-LAST:event_wiringSummaryMenuItemActionPerformed

    private void subscriberDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subscriberDataButtonActionPerformed
        if (!SubscriberDataInternalFrame.isOpen()) {
            try {
                subscriberDataInternalFrame = new SubscriberDataInternalFrame(user);
                workAreaDesktopPane.add(subscriberDataInternalFrame);
                subscriberDataInternalFrame.setVisible(true);
                logger.debug("Subscriber Data internal frame opened.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.SubscriberData.problem"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_subscriberDataButtonActionPerformed

    private void subscriberDataRecordMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subscriberDataRecordMenuItemActionPerformed
        OwnPhoneNumber subscriberRecordNumber = new OwnPhoneNumber(this);
        Phone phone = subscriberRecordNumber.getPhone();
        if (phone != null) {
            String fullNumber = phone.getFullNumeration() + ".";

            Connection connection = null;
            try {
                connection = databaseConnectionsManager.takeConnection();
                Workbook workbook = SubscriberData.getRecord(connection, phone);
                logger.debug("Subscriber data record was required for " + fullNumber);

                File file = new File(bundle.getString("Filename.Subscriber_Data_Record") + ".xlsx");
                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.close();

                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);

            } catch (Exception exception) {
                logger.error("Cannot get subscriber data record for " + fullNumber, exception);
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.SubscriberDataRecord.problem") + " " + fullNumber, "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }
    }//GEN-LAST:event_subscriberDataRecordMenuItemActionPerformed

    private void modifiedSubscribersDataMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modifiedSubscribersDataMenuItemActionPerformed
        DatesPeriod datesPeriod = new DatesPeriod(this);
        datesPeriod.getDatesPeriod(bundle.getString("DatesPeriodPanel.ModifiedSubscribersData.title"));
        LocalDate toDate = null;
        LocalDate fromDate = datesPeriod.getFromDate();
        if (fromDate != null) {
            toDate = datesPeriod.getToDate();
            Connection connection = null;
            try {
                connection = databaseConnectionsManager.takeConnection();
                Workbook workbook = SubscriberData.getModified(connection, fromDate, toDate);
                logger.debug("MOdified subscribers data from " + fromDate + " to " + toDate + " was required.");

                File file = new File(bundle.getString("Filename.Modified_Subscribers_Data") + ".xlsx");
                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.close();

                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);

            } catch (Exception exception) {
                logger.error("Cannot get modified subscribers data from " + fromDate + " to " + toDate + ".", exception);
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.ModifiedSubscribersData.problem") + " " + fromDate + "/" + toDate + ".", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }
    }//GEN-LAST:event_modifiedSubscribersDataMenuItemActionPerformed

    private void subscribersDataModificationsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subscribersDataModificationsMenuItemActionPerformed
        DatesPeriod datesPeriod = new DatesPeriod(this);
        datesPeriod.getDatesPeriod(bundle.getString("DatesPeriodPanel.SubscribersDataModifications.title"));
        LocalDate toDate = null;
        LocalDate fromDate = datesPeriod.getFromDate();
        if (fromDate != null) {
            toDate = datesPeriod.getToDate();
            Connection connection = null;
            try {
                connection = databaseConnectionsManager.takeConnection();
                Workbook workbook = SubscriberData.getModifications(connection, fromDate, toDate);
                logger.debug("Subscribers data modifications from " + fromDate + " to " + toDate + " was required.");

                File file = new File(bundle.getString("Filename.Subscribers_Data_Modifications") + ".xlsx");
                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.close();

                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);

            } catch (Exception exception) {
                logger.error("Cannot get subscribers data modifications from " + fromDate + " to " + toDate + ".", exception);
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.SubscribersDataModifications.problem") + " " + fromDate + "/" + toDate + ".", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }
    }//GEN-LAST:event_subscribersDataModificationsMenuItemActionPerformed

    private void repairsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repairsButtonActionPerformed
        if (!RepairsInternalFrame.isOpen()) {
            try {
                repairsInternalFrame = new RepairsInternalFrame(user);
                workAreaDesktopPane.add(repairsInternalFrame);
                repairsInternalFrame.setVisible(true);
                logger.debug("Repairs internal frame opened.");
            } catch (Exception ex) {
                logger.error("Cannot open Repairs internal frame.", ex);
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.Repairs.problem"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_repairsButtonActionPerformed

    private void serviceOrderMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serviceOrderMenuItemActionPerformed
        ServiceOrderQuery serviceOrderQuery = new ServiceOrderQuery(this);
        int serviceOrderNumber = serviceOrderQuery.getNumber();
        if (serviceOrderNumber != 0) {
            Connection connection = null;
            try {
                connection = databaseConnectionsManager.takeConnection();
                Workbook workbook = ServiceOrder.getReport(connection, serviceOrderNumber);
                logger.debug("Service order report was required for service order number " + serviceOrderNumber + ".");

                File file = new File(bundle.getString("Filename.Service_Order_Report") + ".xlsx");
                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.close();

                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);

            } catch (Exception exception) {
                logger.error("Cannot get service order report for " + serviceOrderNumber, exception);
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.ServiceOrderReport.problem") + " " + serviceOrderNumber + ".", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }
    }//GEN-LAST:event_serviceOrderMenuItemActionPerformed

    private void repairsBySubscriberMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_repairsBySubscriberMenuItemActionPerformed
        OwnPhoneNumber subscriber = new OwnPhoneNumber(this);
        Phone phone = subscriber.getPhone();
        if (phone != null) {
            String fullNumber = phone.getFullNumeration() + ".";

            Connection connection = null;
            try {
                connection = databaseConnectionsManager.takeConnection();
                Workbook workbook = ServiceOrder.getReport(connection, phone);
                logger.debug("Service order report was required for subscriber " + fullNumber);

                File file = new File(bundle.getString("Filename.Repairs_Report") + ".xlsx");
                FileOutputStream out = new FileOutputStream(file);
                workbook.write(out);
                out.close();

                Desktop desktop = Desktop.getDesktop();
                desktop.open(file);

            } catch (Exception exception) {
                logger.error("Cannot get service orders report for subscriber " + fullNumber, exception);
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.ServiceOrdersReport.problem") + " " + fullNumber, "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }
    }//GEN-LAST:event_repairsBySubscriberMenuItemActionPerformed

    private void distributionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_distributionButtonActionPerformed
        if (!DistributionInternalFrame.isOpen()) {
            try {
                distributorInternalFrame = new DistributionInternalFrame(user);
                workAreaDesktopPane.add(distributorInternalFrame);
                distributorInternalFrame.setVisible(true);
                logger.debug("Distribution internal frame opened.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.Distribution.problem"), "Error", JOptionPane.ERROR_MESSAGE);
                logger.error("Cannot open DistributionInternalFrame.", ex);
            }
        }
    }//GEN-LAST:event_distributionButtonActionPerformed

    private void broadbandButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_broadbandButtonActionPerformed
        if (!BroadbandInternalFrame.isOpen()) {
            try {
                broadbandInternalFrame = new BroadbandInternalFrame(user);
                workAreaDesktopPane.add(broadbandInternalFrame);
                broadbandInternalFrame.setVisible(true);
                logger.debug("Broadband internal frame opened.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, bundle.getString("MainFrame.Broadband.problem"), "Error", JOptionPane.ERROR_MESSAGE);
                logger.error("Cannot open BroadbandInternalFrame.", ex);
            }
        }
    }//GEN-LAST:event_broadbandButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem addAreaMenuItem;
    private javax.swing.JMenuItem addUserMenuItem;
    private javax.swing.JButton assignmentButton;
    private javax.swing.JButton broadbandButton;
    private javax.swing.JMenuItem broadbandInstallationMenuItem;
    private javax.swing.JMenu broadbandMenu;
    private javax.swing.JMenuItem broadbandUninstallationMenuItem;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton cablesPairsButton;
    private javax.swing.JMenu descriptorsMenu;
    private javax.swing.JButton distributionButton;
    private javax.swing.JButton dslamsButton;
    private javax.swing.JButton equipmentButton;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem installedBroadbandMenuItem;
    private javax.swing.JMenuItem logInMenuItem;
    private javax.swing.JMenuItem logOutMenuItem;
    private javax.swing.JMenuBar mainMenuBar;
    private javax.swing.JMenu managementMenu;
    private javax.swing.JMenuItem modifiedSubscribersDataMenuItem;
    private javax.swing.JMenuItem notInstalledBroadbandMenuItem;
    private javax.swing.JButton peopleButton;
    private javax.swing.JButton repairsButton;
    private javax.swing.JMenuItem repairsBySubscriberMenuItem;
    private javax.swing.JMenu repairsMenu;
    private javax.swing.JMenu reportsMenu;
    private javax.swing.JMenuItem serviceOrderMenuItem;
    private javax.swing.JMenu sessionMenu;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JButton subscriberDataButton;
    private javax.swing.JMenuItem subscriberDataRecordMenuItem;
    private javax.swing.JSeparator subscriberDataSeparator;
    private javax.swing.JMenuItem subscriberRecordMenuItem;
    private javax.swing.JMenuItem subscribersDataModificationsMenuItem;
    private javax.swing.JMenu subscribersMenu;
    private javax.swing.JSeparator systemManagerSeparator;
    private javax.swing.JLabel userLabel;
    private javax.swing.JButton wiringButton;
    private javax.swing.JMenu wiringMenu;
    private javax.swing.JMenuItem wiringSummaryMenuItem;
    private javax.swing.JDesktopPane workAreaDesktopPane;
    // End of variables declaration//GEN-END:variables

    private User user;
    private DatabaseConnectionsManager databaseConnectionsManager;
    private PeopleInternalFrame peopleInternalFrame;
    private CablesPairsInternalFrame cablesPairsInternalFrame;
    private EquipmentInternalFrame equipmentInternalFrame;
    private AssignmentInternalFrame assignmentInternalFrame;
    private DSLAMsInternalFrame dslamsInternalFrame;
    private WiringInternalFrame wiringInternalFrame;
    private DistributionInternalFrame distributorInternalFrame;
    private BroadbandInternalFrame broadbandInternalFrame;
    private AboutInternalFrame aboutInternalFrame;
    private SubscriberDataInternalFrame subscriberDataInternalFrame;
    private RepairsInternalFrame repairsInternalFrame;

    private static final Logger logger = LogManager.getLogger(MainFrame.class);
    private final ResourceBundle bundle = ResourceBundle.getBundle("i18n/MainFrameBundle"); // NOI18N
}
