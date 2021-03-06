/*
 * 		SearchNumberPanel.java
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
 * 		SearchNumberPanel.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Oct 17, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Area;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Country;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.OfficeCode;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.phone.Phone;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.PhonesAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.AreaCodeComboBoxRenderer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.CountryCodeComboBoxRenderer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.models.OfficeCodeComboBoxRenderer;
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
public class SearchNumberPanel extends javax.swing.JPanel {

    /**
     *
     * @param ownNumerationAnalyzer numeration.
     */
    public SearchNumberPanel(PhonesAnalyzer ownNumerationAnalyzer) {
        this.ownNumerationAnalyzer = ownNumerationAnalyzer;
        initializePanel();
    }

    public SearchNumberPanel(PhonesAnalyzer ownNumerationAnalyzer, int siteId) {
        this.ownNumerationAnalyzer = ownNumerationAnalyzer;
        this.siteId = siteId;
        initializePanel();
    }

    private void initializePanel() {
        createModels();
        createRenderers();
        fillComboBoxModels();
        initComponents();
    }

    private void createModels() {
        numbersListModel = new DefaultListModel();
        createComboBoxModels();
    }

    private void createComboBoxModels() {
        countryCodeComboBoxModel = new DefaultComboBoxModel<>();
        areaCodeComboBoxModel = new DefaultComboBoxModel<>();
        officeCodeComboBoxModel = new DefaultComboBoxModel<>();
    }

    private void createRenderers() {
        countryCodeComboBoxRenderer = new CountryCodeComboBoxRenderer();
        areaCodeComboBoxRenderer = new AreaCodeComboBoxRenderer();
        officeCodeComboBoxRenderer = new OfficeCodeComboBoxRenderer();
    }

    private void fillComboBoxModels() {
        fillComboBoxModel(ownNumerationAnalyzer.getOwnCountriesList(), countryCodeComboBoxModel);
        fillComboBoxModel(ownNumerationAnalyzer.getOwnAreasList(), areaCodeComboBoxModel);
        fillComboBoxModel(ownNumerationAnalyzer.getOwnOfficeCodesList(), officeCodeComboBoxModel);
    }

    /**
     *
     * @param <T>
     * @param list
     * @param model
     */
    private <T> void fillComboBoxModel(List<T> list, DefaultComboBoxModel<T> model) {
        for (T element : list)
            model.addElement(element);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        searchPanel = new javax.swing.JPanel();
        countryCodeLabel = new javax.swing.JLabel();
        officeCodeLabel = new javax.swing.JLabel();
        areaCodeLabel = new javax.swing.JLabel();
        officeCodeComboBox = new javax.swing.JComboBox<>();
        areaCodeComboBox = new javax.swing.JComboBox<>();
        searchButton = new javax.swing.JButton();
        countryCodeComboBox = new javax.swing.JComboBox<>();
        numbersScrollPane = new javax.swing.JScrollPane();
        numbersList = new javax.swing.JList<>();

        searchPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        countryCodeLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("i18n/AssignmentInternalFrameBundle"); // NOI18N
        countryCodeLabel.setText(bundle.getString("SearchNumberPanel.countryCodeLabel.text")); // NOI18N

        officeCodeLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        officeCodeLabel.setText(bundle.getString("SearchNumberPanel.officeCodeLabel.text")); // NOI18N

        areaCodeLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        areaCodeLabel.setText(bundle.getString("SearchNumberPanel.areaCodeLabel.text")); // NOI18N

        officeCodeComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        officeCodeComboBox.setModel(officeCodeComboBoxModel);
        officeCodeComboBox.setRenderer(officeCodeComboBoxRenderer);
        officeCodeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                officeCodeComboBoxActionPerformed(evt);
            }
        });

        areaCodeComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        areaCodeComboBox.setModel(areaCodeComboBoxModel);
        areaCodeComboBox.setRenderer(areaCodeComboBoxRenderer);
        areaCodeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                areaCodeComboBoxActionPerformed(evt);
            }
        });

        searchButton.setText(bundle.getString("SearchNumberPanel.searchButton.text")); // NOI18N
        searchButton.setEnabled(false);
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        countryCodeComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        countryCodeComboBox.setModel(countryCodeComboBoxModel);
        countryCodeComboBox.setRenderer(countryCodeComboBoxRenderer);
        countryCodeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countryCodeComboBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout searchPanelLayout = new javax.swing.GroupLayout(searchPanel);
        searchPanel.setLayout(searchPanelLayout);
        searchPanelLayout.setHorizontalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(searchPanelLayout.createSequentialGroup()
                        .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(countryCodeLabel)
                            .addComponent(areaCodeLabel)
                            .addComponent(officeCodeLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(countryCodeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(areaCodeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(officeCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(searchButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        searchPanelLayout.setVerticalGroup(
            searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(countryCodeLabel)
                    .addComponent(countryCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(areaCodeLabel)
                    .addComponent(areaCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(searchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(officeCodeLabel)
                    .addComponent(officeCodeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchButton)
                .addContainerGap())
        );

        numbersList.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        numbersList.setModel(numbersListModel);
        numbersScrollPane.setViewportView(numbersList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(searchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(numbersScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(searchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(numbersScrollPane))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void countryCodeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countryCodeComboBoxActionPerformed
        numbersListModel.removeAllElements();
        areaCodeComboBoxModel.removeAllElements();
        List<Area> areasList;
        if (countryCodeComboBox.getSelectedIndex() == 0) {
            areasList = ownNumerationAnalyzer.getOwnAreasList();
            searchButton.setEnabled(false);
        } else {
            Country selectedCountry = (Country) countryCodeComboBox.getSelectedItem();
            int selectedCountryId = selectedCountry.getId();
            areasList = ownNumerationAnalyzer.getOwnAreasList(selectedCountryId);
            searchButton.setEnabled(true);
        }
        fillComboBoxModel(areasList, areaCodeComboBoxModel);
    }//GEN-LAST:event_countryCodeComboBoxActionPerformed

    private void areaCodeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_areaCodeComboBoxActionPerformed
        numbersListModel.removeAllElements();
        officeCodeComboBoxModel.removeAllElements();
        List<OfficeCode> officeCodesList = new ArrayList<>();
        Area selectedArea = (Area) areaCodeComboBox.getSelectedItem();
        if (selectedArea != null) {
            int selectedAreaId = selectedArea.getId();
            officeCodesList = ownNumerationAnalyzer.getOwnOfficeCodesList(selectedAreaId);
        }
        fillComboBoxModel(officeCodesList, officeCodeComboBoxModel);
    }//GEN-LAST:event_areaCodeComboBoxActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        OfficeCode selectedOfficeCode = (OfficeCode) officeCodeComboBox.getSelectedItem();
        int selectedOfficeCodeId = selectedOfficeCode.getId();
        if (selectedOfficeCodeId != 0) {
            DatabaseConnectionsManager databaseConnectionsManager = null;
            Connection connection = null;
            try {
                databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
                connection = databaseConnectionsManager.takeConnection();

                List<Phone> phoneNumbersList = new ArrayList<>();

                if (siteId == 0) {
                    phoneNumbersList = Phone.getVacantPhonesList(connection, selectedOfficeCodeId);
                    logger.debug(phoneNumbersList.size() + " vacant phone numbers found in Office Code " + selectedOfficeCode + ".");
                } else {
                    phoneNumbersList = Phone.getNoWiredSubscribersList(connection, selectedOfficeCodeId, siteId);
                    logger.debug(phoneNumbersList.size() + " no wired subscribers found in Office Code " + selectedOfficeCode + ".");
                }

                fillVacantNumbersListModel(phoneNumbersList);
            } catch (Exception exception) {
                logger.error("Cannot fill numbers list.", exception);
                JOptionPane.showMessageDialog(this, bundle.getString("SearchNumberPanel.searchVacantNumbers.problem"), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void officeCodeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_officeCodeComboBoxActionPerformed
        numbersListModel.removeAllElements();
    }//GEN-LAST:event_officeCodeComboBoxActionPerformed

    private void fillVacantNumbersListModel(List<Phone> vacantPhonesList) {
        numbersListModel.removeAllElements();
        for (Phone vacantPhone : vacantPhonesList)
            numbersListModel.addElement(vacantPhone.getNumber());
    }

    public OfficeCode getOfficeCode() {
        OfficeCode selectedOfficeCode = null;
        if (officeCodeComboBox.getSelectedIndex() != 0)
            selectedOfficeCode = (OfficeCode) officeCodeComboBox.getSelectedItem();
        return selectedOfficeCode;
    }

    public String getVacantNumber() {
        String selectedNumber = null;
        if (numbersList.getSelectedIndex() != -1)
            selectedNumber = numbersList.getSelectedValue();
        return selectedNumber;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<Area> areaCodeComboBox;
    private javax.swing.JLabel areaCodeLabel;
    private javax.swing.JComboBox<Country> countryCodeComboBox;
    private javax.swing.JLabel countryCodeLabel;
    private javax.swing.JList<String> numbersList;
    private javax.swing.JScrollPane numbersScrollPane;
    private javax.swing.JComboBox<OfficeCode> officeCodeComboBox;
    private javax.swing.JLabel officeCodeLabel;
    private javax.swing.JButton searchButton;
    private javax.swing.JPanel searchPanel;
    // End of variables declaration//GEN-END:variables

    private PhonesAnalyzer ownNumerationAnalyzer;
    private int siteId;

    // Models.
    private DefaultComboBoxModel<Area> areaCodeComboBoxModel;
    private DefaultComboBoxModel<Country> countryCodeComboBoxModel;
    private DefaultComboBoxModel<OfficeCode> officeCodeComboBoxModel;
    private DefaultListModel numbersListModel;

    // Renderers.
    private CountryCodeComboBoxRenderer countryCodeComboBoxRenderer;
    private AreaCodeComboBoxRenderer areaCodeComboBoxRenderer;
    private OfficeCodeComboBoxRenderer officeCodeComboBoxRenderer;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/AssignmentInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(SearchNumberPanel.class);
}
