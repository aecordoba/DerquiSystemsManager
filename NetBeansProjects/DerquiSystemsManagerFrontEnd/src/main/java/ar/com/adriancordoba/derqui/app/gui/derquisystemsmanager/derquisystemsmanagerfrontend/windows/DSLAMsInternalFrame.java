/*
 * 		DSLAMsInternalFrame.java
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
 * 		DSLAMsInternalFrame.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Jan 25, 2017
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAM;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMBoard;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMBoardModel;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMManufacturer;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.DSLAMModel;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.dslam.managers.DSLAMsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.broadband.router.Router;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Site;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.User;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.BroadbandPortsAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.DSLAMsBoardsModelsAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.DSLAMsModelsAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.SitesAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.StringFormat;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.RouterCreator;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.DSLAMBoardModelCellEditor;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.DSLAMBoardModelCellRenderer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.DSLAMBoardsTableModel;
import java.awt.Color;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class DSLAMsInternalFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form DSLAMsInternalFrame
     */
    public DSLAMsInternalFrame(User user) {
        this.user = user;
        if (user.hasRole("dslams-operator"))
            privilegedUser = true;
        createComponentsModels();

        initComponents();

        setNewFormStructure();

        Connection connection = null;
        try {
            databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
            connection = databaseConnectionsManager.takeConnection();

            sitesAnalyzer = new SitesAnalyzer();
            broadbandPortsAnalyzer = new BroadbandPortsAnalyzer(connection);
            dslamsModelsAnalyzer = new DSLAMsModelsAnalyzer(connection);
            dslamsBoardsModelsAnalyzer = new DSLAMsBoardsModelsAnalyzer(connection);
            boardsListTable.setDefaultRenderer(DSLAMBoardModel.class, new DSLAMBoardModelCellRenderer());
            boardsListTable.setDefaultEditor(DSLAMBoardModel.class, new DSLAMBoardModelCellEditor(dslamsBoardsModelsAnalyzer.getDSLAMsBoardsModelsList()));

            fillComboBoxesModels(connection);
            fillDSLAMsListModel();

            open = true;
        } catch (Exception exception) {
            logger.error("Cannot fill combo boxes or list.", exception);
            JOptionPane.showMessageDialog(this, bundle.getString("DSLAMsInternalFrame.comboBoxes.problem"), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            databaseConnectionsManager.returnConnection(connection);
        }
    }

    private void createComponentsModels() {
        dslamsListModel = new DefaultListModel<>();
        siteComboBoxModel = new DefaultComboBoxModel<>();
        manufacturerComboBoxModel = new DefaultComboBoxModel<>();
        modelComboBoxModel = new DefaultComboBoxModel<>();
        routerComboBoxModel = new DefaultComboBoxModel<>();
        dslamBoardsTableModel = new DSLAMBoardsTableModel(getTableHeaders(), new TreeMap<Integer, DSLAMBoard>(), privilegedUser);
    }

    private String[] getTableHeaders() {
        String[] headersArray = new String[4];
        for (int i = 0; i < headersArray.length; i++) {
            headersArray[i] = bundle.getString("DSLAMsInternalFrame.boardsListTable.column" + (i + 1));
        }
        return headersArray;
    }

    private void fillComboBoxesModels(Connection connection) throws Exception {
        fillComboBoxModel(sitesAnalyzer.getSitesList(), siteComboBoxModel);
        fillComboBoxModel(dslamsModelsAnalyzer.getDSLAMsManufacturersList(), manufacturerComboBoxModel);
        fillRouterComboBoxModel(connection);
    }

    private void fillRouterComboBoxModel(Connection connection) throws Exception {
        routerComboBox.removeAllItems();
        List<Router> routersList = Router.getRoutersList(connection);
        Router selectRouter = new Router();
        selectRouter.setId(0);
        selectRouter.setName(bundle.getString("DSLAMsInternalFrame.Select"));
        routersList.add(0, selectRouter);
        fillComboBoxModel(routersList, routerComboBoxModel);
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

    private void fillDSLAMsListModel() throws Exception {
        dslamsListModel.removeAllElements();
        for (DSLAM dslam : DSLAMsManager.getDslamsList()) {
            dslamsListModel.addElement(dslam);
        }
    }

    private boolean isValidForm() {
        boolean valid = true;

        name = nameTextField.getText().trim();
        if (name.isEmpty() || name.length() > 10) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.name.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            nameTextField.requestFocus();
            valid = false;
            return valid;
        }

        site = (Site) siteComboBox.getSelectedItem();
        if (site.getId() == 0) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.site.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            siteComboBox.requestFocus();
            valid = false;
            return valid;
        }

        if (ipAddressTextField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.ipAddress.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            ipAddressTextField.requestFocus();
            valid = false;
            return valid;
        } else {
            try {
                inetAddress = InetAddress.getByName(ipAddressTextField.getText().trim());
            } catch (UnknownHostException ex) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.ipAddress.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                ipAddressTextField.requestFocus();
                valid = false;
                return valid;
            }
        }

        dslamModel = (DSLAMModel) dslamModelComboBox.getSelectedItem();
        if (dslamModel == null || dslamModel.getName() == bundle.getString("DSLAMsInternalFrame.Select")) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.dslamModel.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            dslamModelComboBox.requestFocus();
            valid = false;
            return valid;
        }

        boardsMap = dslamBoardsTableModel.getData();
        
        for (int slot : boardsMap.keySet()) {
            DSLAMBoard dslamBoard = boardsMap.get(slot);

            // DSLAM board model validation.
            if (dslamBoard != null) {
                if (dslamsList.getSelectedIndex() != -1) {
                    DSLAMBoard oldBoard = dslamsList.getSelectedValue().getBoardsMap().get(slot);
                    if (oldBoard != null && oldBoard.getModel().getPorts() > dslamBoard.getModel().getPorts()) {
                        JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.boardModel.invalid") + " " + slot + ".", bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                        TreeMap<Integer, DSLAMBoard> dslamModelBoardsMap = getDSLAMModelBoardsMap(dslamModel);
                        Set slotsSet = dslamModelBoardsMap.keySet();
                        for (int i = 0; i < slotsSet.size(); i++) {
                            if (dslamModelBoardsMap.get(i).getSlot() == slot) {
                                boardsListTable.changeSelection(i, 1, false, false);
                                break;
                            }
                        }
                        boardsListTable.requestFocus();
                        valid = false;
                        return valid;
                    }
                }

                // DSLAM board remarks validation.
                String dslamBoardRemarks = dslamBoard.getRemarks();
                if (dslamBoardRemarks != null && dslamBoardRemarks.length() > 100) {
                    JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.boardRemarks.invalid") + " " + slot + ".", bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    TreeMap<Integer, DSLAMBoard> dslamModelBoardsMap = getDSLAMModelBoardsMap(dslamModel);
                    Set slotsSet = dslamModelBoardsMap.keySet();
                    for (int i = 0; i < slotsSet.size(); i++) {
                        if (dslamModelBoardsMap.get(i).getSlot() == slot) {
                            boardsListTable.changeSelection(i, 3, false, false);
                            break;
                        }
                    }
                    boardsListTable.requestFocus();
                    valid = false;
                    return valid;
                }
            }
        }

        remarks = null;
        String remarksText = remarksTextArea.getText().trim();
        if (!remarksText.equals(bundle.getString("DSLAMsInternalFrame.remarksTextArea.text")) && !remarksText.isEmpty()) {
            if (remarksText.length() > 100) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.remarks.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                remarksTextArea.requestFocus();
                valid = false;
                return valid;
            } else {
                remarks = remarksText;
            }
        }

        router = (Router) routerComboBox.getSelectedItem();
        if (router.getName() == bundle.getString("DSLAMsInternalFrame.Select")) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.router.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            routerComboBox.requestFocus();
            valid = false;
            return valid;
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

        dslamsScrollPane = new javax.swing.JScrollPane();
        dslamsList = new javax.swing.JList<>();
        dslamPanel = new javax.swing.JPanel();
        boardsListPanel = new javax.swing.JPanel();
        boardsListScrollPane = new javax.swing.JScrollPane();
        boardsListTable = new javax.swing.JTable();
        manufacturerLabel = new javax.swing.JLabel();
        manufacturerComboBox = new javax.swing.JComboBox<>();
        dslamModelLabel = new javax.swing.JLabel();
        dslamModelComboBox = new javax.swing.JComboBox<>();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        siteLabel = new javax.swing.JLabel();
        siteComboBox = new javax.swing.JComboBox<>();
        ipAddressLabel = new javax.swing.JLabel();
        ipAddressTextField = new javax.swing.JTextField();
        remarksScrollPane = new javax.swing.JScrollPane();
        remarksTextArea = new javax.swing.JTextArea();
        portsLabel = new javax.swing.JLabel();
        routerPanel = new javax.swing.JPanel();
        routerComboBox = new javax.swing.JComboBox<>();
        newRouterButton = new javax.swing.JButton();
        buttonsPanel = new javax.swing.JPanel();
        createButton = new javax.swing.JButton();
        newButton = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("i18n/DSLAMsInternalFrameBundle"); // NOI18N
        setTitle(bundle.getString("DSLAMsInternalFrame.title")); // NOI18N
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

        dslamsList.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        dslamsList.setModel(dslamsListModel);
        dslamsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dslamsListMouseClicked(evt);
            }
        });
        dslamsScrollPane.setViewportView(dslamsList);

        dslamPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("DSLAMsInternalFrame.dslamPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        boardsListPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("DSLAMsInternalFrame.dslamPanel.BoardsPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        boardsListTable.setModel(dslamBoardsTableModel);
        boardsListTable.setCellSelectionEnabled(true);
        boardsListScrollPane.setViewportView(boardsListTable);

        javax.swing.GroupLayout boardsListPanelLayout = new javax.swing.GroupLayout(boardsListPanel);
        boardsListPanel.setLayout(boardsListPanelLayout);
        boardsListPanelLayout.setHorizontalGroup(
            boardsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(boardsListPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(boardsListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                .addContainerGap())
        );
        boardsListPanelLayout.setVerticalGroup(
            boardsListPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(boardsListPanelLayout.createSequentialGroup()
                .addComponent(boardsListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 5, Short.MAX_VALUE))
        );

        manufacturerLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        manufacturerLabel.setText(bundle.getString("DSLAMsInternalFrame.manufacturerLabel.text")); // NOI18N

        manufacturerComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        manufacturerComboBox.setModel(manufacturerComboBoxModel);
        manufacturerComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manufacturerComboBoxActionPerformed(evt);
            }
        });

        dslamModelLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        dslamModelLabel.setText(bundle.getString("DSLAMsInternalFrame.dslamModelLabel.text")); // NOI18N

        dslamModelComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        dslamModelComboBox.setModel(modelComboBoxModel);
        dslamModelComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dslamModelComboBoxActionPerformed(evt);
            }
        });

        nameLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        nameLabel.setText(bundle.getString("DSLAMsInternalFrame.nameLabel.text")); // NOI18N

        nameTextField.setText(bundle.getString("DSLAMsInternalFrame.nameTextField.text")); // NOI18N

        siteLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        siteLabel.setText(bundle.getString("DSLAMsInternalFrame.siteLabel.text")); // NOI18N

        siteComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        siteComboBox.setModel(siteComboBoxModel);

        ipAddressLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        ipAddressLabel.setText(bundle.getString("DSLAMsInternalFrame.ipAddressLabel.text")); // NOI18N

        ipAddressTextField.setText(bundle.getString("DSLAMsInternalFrame.ipAddressTextField.text")); // NOI18N

        remarksTextArea.setColumns(20);
        remarksTextArea.setForeground(new java.awt.Color(204, 204, 204));
        remarksTextArea.setRows(5);
        remarksTextArea.setText(bundle.getString("DSLAMsInternalFrame.remarksTextArea.text")); // NOI18N
        remarksTextArea.setEnabled(false);
        remarksTextArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                remarksTextAreaFocusGained(evt);
            }
        });
        remarksScrollPane.setViewportView(remarksTextArea);

        portsLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        portsLabel.setText(bundle.getString("DSLAMsInternalFrame.portsLabel.text.noports")); // NOI18N

        javax.swing.GroupLayout dslamPanelLayout = new javax.swing.GroupLayout(dslamPanel);
        dslamPanel.setLayout(dslamPanelLayout);
        dslamPanelLayout.setHorizontalGroup(
            dslamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dslamPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dslamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(boardsListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(remarksScrollPane)
                    .addGroup(dslamPanelLayout.createSequentialGroup()
                        .addGroup(dslamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dslamPanelLayout.createSequentialGroup()
                                .addComponent(nameLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(siteLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(siteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(dslamPanelLayout.createSequentialGroup()
                                .addComponent(ipAddressLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ipAddressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(dslamPanelLayout.createSequentialGroup()
                                .addComponent(manufacturerLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(manufacturerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(dslamPanelLayout.createSequentialGroup()
                                .addComponent(dslamModelLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dslamModelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(portsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        dslamPanelLayout.setVerticalGroup(
            dslamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dslamPanelLayout.createSequentialGroup()
                .addGroup(dslamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(siteLabel)
                    .addComponent(siteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dslamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ipAddressLabel)
                    .addComponent(ipAddressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dslamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(manufacturerLabel)
                    .addComponent(manufacturerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dslamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dslamModelLabel)
                    .addComponent(dslamModelComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(boardsListPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(remarksScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        routerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("DSLAMsInternalFrame.routerPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        routerComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        routerComboBox.setModel(routerComboBoxModel);

        newRouterButton.setText(bundle.getString("DSLAMsInternalFrame.newRouterButton.text")); // NOI18N
        newRouterButton.setEnabled(false);
        newRouterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newRouterButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout routerPanelLayout = new javax.swing.GroupLayout(routerPanel);
        routerPanel.setLayout(routerPanelLayout);
        routerPanelLayout.setHorizontalGroup(
            routerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(routerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(routerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(newRouterButton, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        routerPanelLayout.setVerticalGroup(
            routerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(routerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(routerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(newRouterButton))
        );

        createButton.setText(bundle.getString("DSLAMsInternalFrame.createButton.text")); // NOI18N
        createButton.setEnabled(false);
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        newButton.setText(bundle.getString("DSLAMsInternalFrame.newButton.text")); // NOI18N
        newButton.setEnabled(false);
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(newButton))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dslamsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dslamPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(routerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dslamsScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dslamPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(routerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        open = false;
    }//GEN-LAST:event_formInternalFrameClosed

    private void newRouterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newRouterButtonActionPerformed
        RouterCreator routerCreator = new RouterCreator(this);
        boolean result = routerCreator.create();
        if (result) {
            Connection connection = databaseConnectionsManager.takeConnection();
            try {
                fillRouterComboBoxModel(connection);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, bundle.getString("DSLAMsInternalFrame.comboBoxes.problem"), "Error", JOptionPane.ERROR_MESSAGE);
                this.dispose();
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }

    }//GEN-LAST:event_newRouterButtonActionPerformed

    private void manufacturerComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manufacturerComboBoxActionPerformed
        modelComboBoxModel.removeAllElements();
        int selectedManufacturerId = ((DSLAMManufacturer) manufacturerComboBox.getSelectedItem()).getId();
        if (dslamsList.getSelectedIndex() != -1) {
            fillComboBoxModel(dslamsModelsAnalyzer.getDSLAMsModelsList(selectedManufacturerId, dslamsList.getSelectedValue().getModel().getPorts()), modelComboBoxModel);
        } else {
            fillComboBoxModel(dslamsModelsAnalyzer.getDSLAMsModelsList(selectedManufacturerId), modelComboBoxModel);
        }
    }//GEN-LAST:event_manufacturerComboBoxActionPerformed

    private void dslamModelComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dslamModelComboBoxActionPerformed
        dslamBoardsTableModel.clearData();

        DSLAMModel selectedDSLAMModel = (DSLAMModel) dslamModelComboBox.getSelectedItem();
        if (selectedDSLAMModel != null) {
            DSLAMManufacturer manufacturer = selectedDSLAMModel.getManufacturer();
            if (manufacturer != null) {
                boardsListTable.setDefaultEditor(DSLAMBoardModel.class, new DSLAMBoardModelCellEditor(dslamsBoardsModelsAnalyzer.getDSLAMsBoardsModelsList(manufacturer.getId())));
            }

            int ports = selectedDSLAMModel.getPorts();
            if (ports > 0) {
                portsLabel.setText("(" + ports + " " + bundle.getString("DSLAMsInternalFrame.portsLabel.text.ports") + ".)");
            } else {
                portsLabel.setText(bundle.getString("DSLAMsInternalFrame.portsLabel.text.noports"));
            }
        }

        if (dslamModelComboBox.getSelectedIndex() > 0) {
            dslamBoardsTableModel.setData(getDSLAMModelBoardsMap(selectedDSLAMModel));
        }
    }//GEN-LAST:event_dslamModelComboBoxActionPerformed

    private TreeMap<Integer, DSLAMBoard> getDSLAMModelBoardsMap(DSLAMModel dslamModel) {
        TreeMap<Integer, DSLAMBoard> dslamModelMap = new TreeMap<>();
        for (int slot : dslamModel.getSlotsList())
            dslamModelMap.put(slot, null);
        return dslamModelMap;
    }

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        boolean valid = isValidForm();
        if (valid) {
            Connection connection = databaseConnectionsManager.takeConnection();
            if (dslamsList.getSelectedIndex() == -1) {
                DSLAM dslam = new DSLAM();
                fillDSLAMData(dslam);
                try {
                    dslam.insert(connection);
                    JOptionPane.showMessageDialog(this, dslam + " " + bundle.getString("InsertDialog.confirmation"), bundle.getString("InsertDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    renewFormData();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this, dslam + " " + bundle.getString("InsertDialog.error"), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                }
            } else {
                DSLAM selectedDSLAM = dslamsList.getSelectedValue();
                fillDSLAMData(selectedDSLAM);
                try {
                    selectedDSLAM.update(connection);
                    JOptionPane.showMessageDialog(this, selectedDSLAM + " " + bundle.getString("UpdatetDialog.confirmation"), bundle.getString("UpdatetDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    renewFormData();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this, selectedDSLAM + " " + bundle.getString("UpdateDialog.error"), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                }
            }
        }
    }//GEN-LAST:event_createButtonActionPerformed

    private void remarksTextAreaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_remarksTextAreaFocusGained
        remarksTextArea.setText("");
        remarksTextArea.setForeground(Color.BLACK);
    }//GEN-LAST:event_remarksTextAreaFocusGained

    private void dslamsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dslamsListMouseClicked
        clearForm();
        DSLAM selectedDSLAM = dslamsList.getSelectedValue();
        nameTextField.setText(selectedDSLAM.getName());
        siteComboBoxModel.setSelectedItem(selectedDSLAM.getSite());
        ipAddressTextField.setText(selectedDSLAM.getInetAddress().getHostAddress());
        dslamModel = selectedDSLAM.getModel();
        DSLAMManufacturer dslamManufacturer = dslamModel.getManufacturer();
        manufacturerComboBoxModel.setSelectedItem(dslamManufacturer);
        modelComboBoxModel.removeAllElements();
        fillComboBoxModel(dslamsModelsAnalyzer.getDSLAMsModelsList(dslamManufacturer.getId(), dslamModel.getPorts()), modelComboBoxModel);
        modelComboBoxModel.setSelectedItem(dslamModel);

        if (dslamModel.getSlotsList().size() > 1)
            dslamBoardsTableModel.setData(selectedDSLAM.getBoardsMap());

        remarksTextArea.setForeground(Color.BLACK);
        remarksTextArea.setText(selectedDSLAM.getRemarks());

        routerComboBoxModel.setSelectedItem(selectedDSLAM.getRouter());

        setModifyFormStructure();
    }//GEN-LAST:event_dslamsListMouseClicked

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        clearForm();
        setNewFormStructure();
    }//GEN-LAST:event_newButtonActionPerformed

    private void setNewFormStructure() {
        createButton.setText(bundle.getString("DSLAMsInternalFrame.createButton.text"));
        if (privilegedUser) {
            createButton.setEnabled(true);
            newRouterButton.setEnabled(true);
            remarksTextArea.setEnabled(true);
        }
        newButton.setEnabled(false);
        dslamsList.clearSelection();
    }

    private void setModifyFormStructure() {
        createButton.setText(bundle.getString("DSLAMsInternalFrame.modifyButton.text"));
        newButton.setEnabled(true);
    }

    private void fillDSLAMData(DSLAM dslam) {
        dslam.setName(StringFormat.capitalize(name));
        dslam.setSite(site);
        dslam.setInetAddress(inetAddress);
        dslam.setBoardsMap(boardsMap);
        dslam.setRouter(router);
        dslam.setModel(dslamModel);
        dslam.setRemarks(remarks);
    }

    private void renewFormData() throws Exception {
        try {
            clearForm();
            DSLAMsManager.fillDSLAMsList();
            fillDSLAMsListModel();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("Cannot renew form data.");
        }
        setNewFormStructure();
    }

    private void clearForm() {
        nameTextField.setText("");
        siteComboBox.setSelectedIndex(0);
        ipAddressTextField.setText("");
        manufacturerComboBox.setSelectedIndex(0);

        modelComboBoxModel.removeAllElements();
        fillComboBoxModel(dslamsModelsAnalyzer.getDSLAMsModelsList(), modelComboBoxModel);
        dslamModelComboBox.setSelectedIndex(0);

        dslamBoardsTableModel.clearData();
        remarksTextArea.setForeground(new java.awt.Color(204, 204, 204));
        remarksTextArea.setText(bundle.getString("DSLAMsInternalFrame.remarksTextArea.text"));
        routerComboBox.setSelectedIndex(0);
    }

    public static boolean isOpen() {
        return open;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel boardsListPanel;
    private javax.swing.JScrollPane boardsListScrollPane;
    private javax.swing.JTable boardsListTable;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton createButton;
    private javax.swing.JComboBox<DSLAMModel> dslamModelComboBox;
    private javax.swing.JLabel dslamModelLabel;
    private javax.swing.JPanel dslamPanel;
    private javax.swing.JList<DSLAM> dslamsList;
    private javax.swing.JScrollPane dslamsScrollPane;
    private javax.swing.JLabel ipAddressLabel;
    private javax.swing.JTextField ipAddressTextField;
    private javax.swing.JComboBox<DSLAMManufacturer> manufacturerComboBox;
    private javax.swing.JLabel manufacturerLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton newButton;
    private javax.swing.JButton newRouterButton;
    private javax.swing.JLabel portsLabel;
    private javax.swing.JScrollPane remarksScrollPane;
    private javax.swing.JTextArea remarksTextArea;
    private javax.swing.JComboBox<Router> routerComboBox;
    private javax.swing.JPanel routerPanel;
    private javax.swing.JComboBox<Site> siteComboBox;
    private javax.swing.JLabel siteLabel;
    // End of variables declaration//GEN-END:variables

    private DatabaseConnectionsManager databaseConnectionsManager;
    private User user;
    private boolean privilegedUser;
    private SitesAnalyzer sitesAnalyzer;
    private BroadbandPortsAnalyzer broadbandPortsAnalyzer;
    private DSLAMsModelsAnalyzer dslamsModelsAnalyzer;
    private DSLAMsBoardsModelsAnalyzer dslamsBoardsModelsAnalyzer;

    // Models.
    private DefaultListModel<DSLAM> dslamsListModel;
    private DefaultComboBoxModel<Site> siteComboBoxModel;
    private DefaultComboBoxModel<DSLAMManufacturer> manufacturerComboBoxModel;
    private DefaultComboBoxModel<DSLAMModel> modelComboBoxModel;
    private DefaultComboBoxModel<Router> routerComboBoxModel;
    private DSLAMBoardsTableModel dslamBoardsTableModel;

    private String name;
    private Site site;
    private InetAddress inetAddress;
    private Router router;
    private DSLAMModel dslamModel;
    private String remarks;
    private TreeMap<Integer, DSLAMBoard> boardsMap;
    private TreeMap<Integer, DSLAMBoard>  selectedDSLAMBoardsMap;

    private static boolean open;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/DSLAMsInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(DSLAMsInternalFrame.class);
}
