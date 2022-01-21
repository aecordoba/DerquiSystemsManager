/*
 * 		CablesPairsInternalFrame.java
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
 * 		CablesPairsInternalFrame.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Aug 22, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Site;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.User;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetCable;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetFrame;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.wiring.StreetPair;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.CablesAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.SitesAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.StringFormat;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.PairsTableModel;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.PairsTableRenderer;
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
public class CablesPairsInternalFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form CablesPairsInternalFrame
     */
    public CablesPairsInternalFrame(User user) {
        this.user = user;
        if (user.hasRole("cables-pairs-operator"))
            privilegedUser = true;
        cablesListModel = new DefaultListModel<>();
        siteComboBoxModel = new DefaultComboBoxModel<>();
        frameComboBoxModel = new DefaultComboBoxModel<>();

        pairsTableModel = new PairsTableModel(getTableHeaders(), new ArrayList<StreetPair>(), privilegedUser);
        pairsTableRenderer = new PairsTableRenderer();

        initComponents();
        pairsListTable.setDefaultRenderer(Object.class, pairsTableRenderer);

        setNewFormStructure();

        Connection connection = null;
        try {
            databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
            connection = databaseConnectionsManager.takeConnection();

            cablesAnalyzer = new CablesAnalyzer(connection);
            sitesAnalyzer = new SitesAnalyzer();
            fillCablesListModel();
            fillComboBoxModel(sitesAnalyzer.getSitesList(), siteComboBoxModel);

            open = true;
        } catch (Exception ex) {
            logger.error("Cannot fill combo boxes or list.", ex);
            JOptionPane.showMessageDialog(this, bundle.getString("CablesPairsInternalFrame.comboBoxes.problem"), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            databaseConnectionsManager.returnConnection(connection);
        }
    }

    public static boolean isOpen() {
        return open;
    }

    private String[] getTableHeaders() {
        String[] headersArray = new String[4];
        for (int i = 0; i < headersArray.length; i++) {
            headersArray[i] = bundle.getString("CablesPairsInternalFrame.pairsListTable.column" + (i + 1));
        }
        return headersArray;
    }

    private void fillCablesListModel() throws Exception {
        cablesListModel.removeAllElements();
        List<StreetCable> streetCablesList = cablesAnalyzer.getStreetCablesList();
        for (StreetCable cable : streetCablesList) {
            cablesListModel.addElement(cable);
        }
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

    private void clearForm() {
        siteComboBox.setSelectedIndex(0);
        cableTextField.setText("");
        pairsTextField.setText("");
        descriptionTextArea.setText("");
        pairsTableModel.clearData();
    }

    private boolean isValidForm() {
        boolean valid = true;

        site = (Site) siteComboBox.getSelectedItem();
        if (site.getId() == 0) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.site.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            siteComboBox.requestFocus();
            valid = false;
            return valid;
        }

        cableName = StringFormat.capitalize(cableTextField.getText().trim());
        if (cableName.length() != 1 || !Character.isLetter(cableName.charAt(0))) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.cableName.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            cableTextField.requestFocus();
            valid = false;
            return valid;
        }

        if ((cablesList.getSelectedIndex() < 0) && (cablesAnalyzer.getStreetCable(((StreetFrame) frameComboBox.getSelectedItem()).getId(), cableName.charAt(0)) != null)) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.cableName.exists"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            cableTextField.requestFocus();
            valid = false;
            return valid;
        }

        try {
            pairs = Integer.valueOf(pairsTextField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.cablePairs.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            pairsTextField.requestFocus();
            valid = false;
            return valid;
        }

        cableDescription = descriptionTextArea.getText().trim();
        if (cableDescription.length() > 100) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.cableDescription.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            descriptionTextArea.requestFocus();
            valid = false;
            return valid;
        }

        pairsList = pairsTableModel.getData();
        for (StreetPair pair : pairsList) {
            String remarks = pair.getRemarks();
            if (remarks != null && remarks.length() > 100) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.pairRemarks.invalid") + pair.getPair(), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                pairsListTable.requestFocus();
                valid = false;
                return valid;
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

        cablesListScrollPane = new javax.swing.JScrollPane();
        cablesList = new javax.swing.JList<>();
        pairsListScrollPane = new javax.swing.JScrollPane();
        pairsListTable = new javax.swing.JTable();
        dataPanel = new javax.swing.JPanel();
        siteLabel = new javax.swing.JLabel();
        siteComboBox = new javax.swing.JComboBox<>();
        frameLabel = new javax.swing.JLabel();
        frameComboBox = new javax.swing.JComboBox<>();
        cableLabel = new javax.swing.JLabel();
        newButton = new javax.swing.JButton();
        createButton = new javax.swing.JButton();
        cableTextField = new javax.swing.JTextField();
        pairsLabel = new javax.swing.JLabel();
        pairsTextField = new javax.swing.JTextField();
        descriptionLabel = new javax.swing.JLabel();
        descriptionScrollPane = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();

        setClosable(true);
        setIconifiable(true);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("i18n/CablesPairsInternalFrameBundle"); // NOI18N
        setTitle(bundle.getString("CablesPairsInternalFrame.title")); // NOI18N
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

        cablesList.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cablesList.setModel(cablesListModel);
        cablesList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cablesListMouseClicked(evt);
            }
        });
        cablesListScrollPane.setViewportView(cablesList);

        pairsListTable.setModel(pairsTableModel);
        pairsListScrollPane.setViewportView(pairsListTable);

        siteLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        siteLabel.setText(bundle.getString("CablesPairsInternalFrame.siteLabel.text")); // NOI18N

        siteComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        siteComboBox.setModel(siteComboBoxModel);
        siteComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siteComboBoxActionPerformed(evt);
            }
        });

        frameLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        frameLabel.setText(bundle.getString("CablesPairsInternalFrame.frameLabel.text")); // NOI18N

        frameComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        frameComboBox.setModel(frameComboBoxModel);
        frameComboBox.setEnabled(false);

        cableLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        cableLabel.setText(bundle.getString("CablesPairsInternalFrame.cableLabel.text")); // NOI18N

        newButton.setText(bundle.getString("CablesPairsInternalFrame.newButton.text")); // NOI18N
        newButton.setEnabled(false);
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        createButton.setText(bundle.getString("CablesPairsInternalFrame.createButton.text")); // NOI18N
        createButton.setEnabled(false);
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        cableTextField.setEditable(false);
        cableTextField.setText(bundle.getString("CablesPairsInternalFrame.cableTextField.text")); // NOI18N

        pairsLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        pairsLabel.setText(bundle.getString("CablesPairsInternalFrame.pairsLabel.text")); // NOI18N

        pairsTextField.setEditable(false);
        pairsTextField.setText(bundle.getString("CablesPairsInternalFrame.pairsTextField.text")); // NOI18N

        descriptionLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        descriptionLabel.setText(bundle.getString("CablesPairsInternalFrame.descriptionLabel.text")); // NOI18N

        descriptionTextArea.setEditable(false);
        descriptionTextArea.setColumns(20);
        descriptionTextArea.setRows(5);
        descriptionScrollPane.setViewportView(descriptionTextArea);

        javax.swing.GroupLayout dataPanelLayout = new javax.swing.GroupLayout(dataPanel);
        dataPanel.setLayout(dataPanelLayout);
        dataPanelLayout.setHorizontalGroup(
            dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addComponent(siteLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(siteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(frameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cableLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cableTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addComponent(pairsLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pairsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(descriptionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dataPanelLayout.createSequentialGroup()
                                .addComponent(newButton, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(createButton))
                            .addComponent(descriptionScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(95, Short.MAX_VALUE))
        );

        dataPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {createButton, newButton});

        dataPanelLayout.setVerticalGroup(
            dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(siteLabel)
                    .addComponent(siteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(frameLabel)
                    .addComponent(frameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cableLabel)
                    .addComponent(cableTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dataPanelLayout.createSequentialGroup()
                        .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pairsLabel)
                            .addComponent(pairsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(descriptionLabel))
                        .addGap(0, 49, Short.MAX_VALUE))
                    .addComponent(descriptionScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newButton)
                    .addComponent(createButton))
                .addContainerGap())
        );

        dataPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {createButton, newButton});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(cablesListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pairsListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(pairsListScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cablesListScrollPane))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        open = false;
    }//GEN-LAST:event_formInternalFrameClosed

    private void cablesListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cablesListMouseClicked
        clearForm();
        StreetCable selectedCable = cablesList.getSelectedValue();
        StreetFrame frame = selectedCable.getStreetFrame();
        siteComboBoxModel.setSelectedItem(frame.getSite());
        frameComboBoxModel.setSelectedItem(frame);
        cableTextField.setText(String.valueOf(selectedCable.getName()));
        pairsTextField.setText(String.valueOf(selectedCable.getPairs()));
        descriptionTextArea.setText(selectedCable.getDescription());
        pairsTableModel.setData(cablesAnalyzer.getStreetPairsList(selectedCable.getId()));

        setModifyFormStructure();
    }//GEN-LAST:event_cablesListMouseClicked

    private void setModifyFormStructure() {
        createButton.setText(bundle.getString("CablesPairsInternalFrame.modifyButton.text"));
        newButton.setEnabled(true);

        siteComboBox.setEnabled(false);
        frameComboBox.setEnabled(false);
        cableTextField.setEditable(false);
        pairsTextField.setEditable(false);
    }

    private void setNewFormStructure() {
        createButton.setText(bundle.getString("CablesPairsInternalFrame.createButton.text"));
        newButton.setEnabled(false);

        if (privilegedUser) {
            createButton.setEnabled(true);
            siteComboBox.setEnabled(true);
            frameComboBox.setEnabled(false);
            cableTextField.setEditable(true);
            pairsTextField.setEditable(true);
            descriptionTextArea.setEditable(true);
            cablesList.clearSelection();
        }
    }

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed
        clearForm();
        setNewFormStructure();
    }//GEN-LAST:event_newButtonActionPerformed

    private void siteComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_siteComboBoxActionPerformed
        frameComboBoxModel.removeAllElements();
        if (siteComboBox.getSelectedIndex() > 0) {
            int selectedSiteId = ((Site) siteComboBox.getSelectedItem()).getId();
            fillComboBoxModel(cablesAnalyzer.getStreetFramesList(selectedSiteId), frameComboBoxModel);
            frameComboBox.setEnabled(true);
        } else if (siteComboBox.getSelectedIndex() == 0) {
            frameComboBox.setEnabled(false);
        }
    }//GEN-LAST:event_siteComboBoxActionPerformed

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        boolean valid = isValidForm();
        if (valid) {
            Connection connection = databaseConnectionsManager.takeConnection();
            if (cablesList.getSelectedIndex() == -1) {
                StreetCable streetCable = new StreetCable();
                fillStreetCableData(streetCable);
                try {
                    streetCable.insert(connection);
                    JOptionPane.showMessageDialog(this, streetCable + " " + bundle.getString("InsertDialog.confirmation"), bundle.getString("InsertDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    cablesAnalyzer = new CablesAnalyzer(connection);
                    fillCablesListModel();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this, streetCable + " " + bundle.getString("InsertDialog.error"), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                }
            } else {
                StreetCable selectedCable = cablesList.getSelectedValue();
                selectedCable.setDescription(cableDescription);
                try {
                    selectedCable.update(connection);
                    for (StreetPair pair : pairsTableModel.getData()) {
                        pair.update(connection);
                    }
                    JOptionPane.showMessageDialog(this, selectedCable + " " + bundle.getString("ModifyDialog.confirmation"), bundle.getString("ModifyDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    cablesList.clearSelection();
                    setNewFormStructure();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                    JOptionPane.showMessageDialog(this, selectedCable + " " + bundle.getString("ModifyDialog.error"), "Error", JOptionPane.ERROR_MESSAGE);
                } finally {
                    databaseConnectionsManager.returnConnection(connection);
                }
            }
        }
    }//GEN-LAST:event_createButtonActionPerformed

    private void fillStreetCableData(StreetCable cable) {
        cable.setName(cableName.charAt(0));
        streetFrame = (StreetFrame) frameComboBox.getSelectedItem();
        cable.setStreetFrame(streetFrame);
        cable.setDescription(cableDescription);
        cable.setPairs(pairs);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel cableLabel;
    private javax.swing.JTextField cableTextField;
    private javax.swing.JList<StreetCable> cablesList;
    private javax.swing.JScrollPane cablesListScrollPane;
    private javax.swing.JButton createButton;
    private javax.swing.JPanel dataPanel;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JScrollPane descriptionScrollPane;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JComboBox<StreetFrame> frameComboBox;
    private javax.swing.JLabel frameLabel;
    private javax.swing.JButton newButton;
    private javax.swing.JLabel pairsLabel;
    private javax.swing.JScrollPane pairsListScrollPane;
    private javax.swing.JTable pairsListTable;
    private javax.swing.JTextField pairsTextField;
    private javax.swing.JComboBox<Site> siteComboBox;
    private javax.swing.JLabel siteLabel;
    // End of variables declaration//GEN-END:variables

    private DatabaseConnectionsManager databaseConnectionsManager;
    private User user;
    private boolean privilegedUser;
    private static boolean open;
    private CablesAnalyzer cablesAnalyzer;
    private SitesAnalyzer sitesAnalyzer;

    // Models.
    private DefaultListModel<StreetCable> cablesListModel;
    private DefaultComboBoxModel<Site> siteComboBoxModel;
    private DefaultComboBoxModel<StreetFrame> frameComboBoxModel;
    private PairsTableModel pairsTableModel;
    private PairsTableRenderer pairsTableRenderer;

    // Assignment form.
    private Site site;
    private StreetFrame streetFrame;
    private String cableName;
    private String cableDescription;
    private List<StreetPair> pairsList;
    private int pairs;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/CablesPairsInternalFrameBundle"); // I18N
    private static final Logger logger = LogManager.getLogger(CablesPairsInternalFrame.class);
}
