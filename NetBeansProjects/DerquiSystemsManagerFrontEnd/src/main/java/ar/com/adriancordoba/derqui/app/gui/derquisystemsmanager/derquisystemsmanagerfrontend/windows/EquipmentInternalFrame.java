/*
 * 		EquipmentInternalFrame.java
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
 * 		EquipmentInternalFrame.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Sep 11, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.descriptors.Technology;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.BlockPosition;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Distributor;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.Site;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.SwitchBlock;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.managers.SitesManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.managers.SwitchBlocksManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.EFrame;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.ELineModule;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.LineModule;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Neax61EEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Neax61SigmaELU;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Neax61SigmaEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.SigmaFrame;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.SigmaL3AddressEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.SigmaLineModule;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.ZhoneEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.User;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils.SitesAnalyzer;
import ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs.SwitchBlockPanel;
import java.sql.Connection;
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
public class EquipmentInternalFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form EquipmentInternalFrame
     */
    public EquipmentInternalFrame(User user) {
        this.user = user;
        if (user.hasRole("equipment-operator"))
            privilegedUser = true;

        databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
        switchBlocksListModel = new DefaultListModel<>();
        siteComboBoxModel = new DefaultComboBoxModel<>();

        initComponents();

        setNewFormStructure();

        sitesAnalyzer = new SitesAnalyzer();

        fillComboBoxModel(sitesAnalyzer.getSitesList(), siteComboBoxModel);
        fillSwitchBlocksListModel();

        open = true;

        technology = Technology.NEAX61E;
    }

    public static boolean isOpen() {
        return open;
    }

    private void fillSwitchBlocksListModel() {
        switchBlocksListModel.removeAllElements();
        List<SwitchBlock> switchBlocksList = SwitchBlocksManager.getSwitchBlocksList();
        for (SwitchBlock switchBlock : switchBlocksList) {
            switchBlocksListModel.addElement(switchBlock);
        }
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

        switchBlockName = blockTextField.getText().trim();
        if (switchBlockName.length() != 3) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.block.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            blockTextField.requestFocus();
            valid = false;
            return valid;
        }

        if (positionsTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.positions.empty"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            positionsTextField.requestFocus();
            valid = false;
            return valid;
        } else {
            try {
                positions = Integer.parseInt(positionsTextField.getText());
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.positions.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                positionsTextField.requestFocus();
                valid = false;
                return valid;
            }
        }

        description = descriptionTextArea.getText();
        if (description.length() > 100) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.description.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            descriptionTextArea.requestFocus();
            valid = false;
            return valid;
        }

        switch (technology) {
            case NEAX61E:
                valid = validateNeax61EEquipment();
                break;
            case NEAX61SIGMA:
            case NEAX61SIGMA_ELU:
                valid = validateNeax61SigmaEquipment();
                break;
            case ZHONE:
                valid = validateZhoneEquipment();
        }
        if (!valid) {
            return valid;
        }

        int availablePositions = positions;
        SwitchBlock switchBlock = SwitchBlocksManager.getSwitchBlock(site.getId(), "H" + switchBlockName.toUpperCase());
        if (switchBlock != null) {
            availablePositions = getAvailablePositions(switchBlock.getId());
        }

        int necessaryPositions = 0;
        if (technology != Technology.NEAX61SIGMA_ELU) {
            necessaryPositions = technology.getCableSize() * (Integer.parseInt(finalGroup) - Integer.parseInt(initialGroup) + 1);
        } else {
            necessaryPositions = technology.getCableSize();
        }

        if ((availablePositions - necessaryPositions) < 0) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.block.noAvailable"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            blockTextField.requestFocus();
            valid = false;
            return valid;
        }
        return valid;
    }

    private boolean validateNeax61EEquipment() {
        boolean result = true;

        spce = spceTextField.getText().trim();
        if (spce.length() != 2) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.spce.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            spceTextField.requestFocus();
            result = false;
            return result;
        }
        try {
            if (Integer.parseInt(spceTextField.getText()) < 0 || Integer.parseInt(spceTextField.getText()) > 21) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.spce.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                spceTextField.requestFocus();
                result = false;
                return result;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.spce.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            spceTextField.requestFocus();
            result = false;
            return result;
        }

        highway = highwayTextField.getText();
        if (highway.length() != 1) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.highway.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            highwayTextField.requestFocus();
            result = false;
            return result;
        }
        try {
            if (Integer.parseInt(highway) > 5) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.highway.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                highwayTextField.requestFocus();
                result = false;
                return result;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.highway.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            highwayTextField.requestFocus();
            result = false;
            return result;
        }

        subhighway = subhighwayTextField.getText();
        if (subhighway.length() != 1) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.subhighway.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            subhighwayTextField.requestFocus();
            result = false;
            return result;
        }
        try {
            if (Integer.parseInt(subhighway) > 3) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.subhighway.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                subhighwayTextField.requestFocus();
                result = false;
                return result;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.subhighway.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            subhighwayTextField.requestFocus();
            result = false;
            return result;
        }

        initialGroup = initialGroupTextField.getText();
        if (initialGroup.length() != 2) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.initialGroup.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            initialGroupTextField.requestFocus();
            result = false;
            return result;
        }
        try {
            if (Integer.parseInt(initialGroup) < 0 || Integer.parseInt(initialGroup) > 31) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.initialGroup.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                initialGroupTextField.requestFocus();
                result = false;
                return result;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.initialGroup.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            initialGroupTextField.requestFocus();
            result = false;
            return result;
        }

        finalGroup = finalGroupTextField.getText();
        if (finalGroup.length() != 2) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.finalGroup.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            finalGroupTextField.requestFocus();
            result = false;
            return result;
        }
        try {
            if (Integer.parseInt(initialGroup) < 0 || Integer.parseInt(initialGroup) > 31) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.finalGroup.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                initialGroupTextField.requestFocus();
                result = false;
                return result;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.finalGroup.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            initialGroupTextField.requestFocus();
            result = false;
            return result;
        }

        if (Integer.parseInt(initialGroup) > Integer.parseInt(finalGroup)) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.groupRange.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            initialGroupTextField.requestFocus();
            result = false;
            return result;
        }

        validateFrameAndModule();

        return result;
    }

    private boolean validateNeax61SigmaEquipment() {
        boolean result = true;

        spce = spceTextField.getText().trim();
        if (spce.length() != 2) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.tsw.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            spceTextField.requestFocus();
            result = false;
            return result;
        }
        try {
            if (Integer.parseInt(spceTextField.getText()) > 11 || Integer.parseInt(spceTextField.getText()) < 0) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.tsw.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                spceTextField.requestFocus();
                result = false;
                return result;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.tsw.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            spceTextField.requestFocus();
            result = false;
            return result;
        }

        highway = highwayTextField.getText();
        if (highway.length() != 2) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.kHighway.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            highwayTextField.requestFocus();
            result = false;
            return result;
        }
        try {
            if (Integer.parseInt(highway) > 99 || Integer.parseInt(highway) < 0) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.kHighway.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                highwayTextField.requestFocus();
                result = false;
                return result;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.kHighway.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            highwayTextField.requestFocus();
            result = false;
            return result;
        }

        subhighway = subhighwayTextField.getText();
        if (subhighway.length() != 2) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.pHighway.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            subhighwayTextField.requestFocus();
            result = false;
            return result;
        }
        try {
            if (Integer.parseInt(subhighway) < 0 || Integer.parseInt(subhighway) > 29) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.pHighway.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                subhighwayTextField.requestFocus();
                result = false;
                return result;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.pHighway.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            subhighwayTextField.requestFocus();
            result = false;
            return result;
        }

        initialGroup = initialGroupTextField.getText();
        String techFieldKey;
        if (technology == Technology.NEAX61SIGMA) {
            techFieldKey = "Row";
        } else {
            techFieldKey = "DTI";
        }
        if (initialGroup.length() != 1) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.initial" + techFieldKey + ".invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            initialGroupTextField.requestFocus();
            result = false;
            return result;
        }
        try {
            if (Integer.parseInt(initialGroup) > 3) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.initial" + techFieldKey + ".invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                initialGroupTextField.requestFocus();
                result = false;
                return result;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.initial" + techFieldKey + ".invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            initialGroupTextField.requestFocus();
            result = false;
            return result;
        }

        finalGroup = finalGroupTextField.getText();
        if (finalGroup.length() != 1) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.final" + techFieldKey + ".invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            finalGroupTextField.requestFocus();
            result = false;
            return result;
        }
        try {
            if (Integer.parseInt(finalGroup) > 3) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.final" + techFieldKey + ".invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                finalGroupTextField.requestFocus();
                result = false;
                return result;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.final" + techFieldKey + ".invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            finalGroupTextField.requestFocus();
            result = false;
            return result;
        }

        if (Integer.parseInt(initialGroup) > Integer.parseInt(finalGroup)) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog." + techFieldKey + "Range.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            initialGroupTextField.requestFocus();
            result = false;
            return result;
        }

        if (technology == Technology.NEAX61SIGMA) {
            result = validateFrameAndModule();
        } else {
            result = validateELUName();
        }

        return result;
    }

    private boolean validateFrameAndModule() {
        boolean result = true;

        frameName = frameTextField.getText().trim();
        if (frameName.isEmpty() || frameName.length() > 15) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.frame.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            frameTextField.requestFocus();
            result = false;
            return result;
        }

        moduleName = moduleTextField.getText().trim();
        if (moduleName.length() != 2) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.module.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            moduleTextField.requestFocus();
            result = false;
            return result;
        }
        try {
            if (Integer.parseInt(moduleName) < 0) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.module.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                moduleTextField.requestFocus();
                result = false;
                return result;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.module.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            moduleTextField.requestFocus();
            result = false;
            return result;
        }

        return result;
    }

    private boolean validateELUName() {
        boolean result = true;

        frameName = frameTextField.getText().trim();
        if (frameName.isEmpty() || frameName.length() > 8) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.eluName.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            frameTextField.requestFocus();
            result = false;
            return result;
        }

        return result;
    }

    private boolean validateZhoneEquipment() {
        boolean result = true;

        initialGroup = initialGroupTextField.getText();
        if (initialGroup.length() != 2) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.initialCable.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            initialGroupTextField.requestFocus();
            result = false;
            return result;
        }
        try {
            if (Integer.parseInt(initialGroup) < 0 || Integer.parseInt(initialGroup) > 31) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.initialCable.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                initialGroupTextField.requestFocus();
                result = false;
                return result;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.initialCable.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            initialGroupTextField.requestFocus();
            result = false;
            return result;
        }

        finalGroup = finalGroupTextField.getText();
        if (finalGroup.length() != 2) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.finalCable.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            finalGroupTextField.requestFocus();
            result = false;
            return result;
        }
        try {
            if (Integer.parseInt(finalGroup) < 0 || Integer.parseInt(finalGroup) > 31) {
                JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.finalCable.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                finalGroupTextField.requestFocus();
                result = false;
                return result;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.finalCable.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            finalGroupTextField.requestFocus();
            result = false;
            return result;
        }

        if (Integer.parseInt(initialGroup) > Integer.parseInt(finalGroup)) {
            JOptionPane.showMessageDialog(this, bundle.getString("ValidationDialog.cableRange.invalid"), bundle.getString("ValidationDialog.title"), JOptionPane.INFORMATION_MESSAGE);
            initialGroupTextField.requestFocus();
            result = false;
            return result;
        }

        return result;
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
        blockTextField.setText("");
        descriptionTextArea.setText("");
        positionsTextField.setText("");
        clearEquipment();
    }

    private void clearEquipment() {
        if (technology == Technology.NEAX61E || technology == Technology.NEAX61SIGMA || technology == Technology.NEAX61SIGMA_ELU) {
            spceTextField.setText("00");
        } else {
            spceTextField.setText("");
        }
        highwayTextField.setText("");
        subhighwayTextField.setText("");
        initialGroupTextField.setText("");
        finalGroupTextField.setText("");
        frameTextField.setText("");
        moduleTextField.setText("");
    }

    private int getAvailablePositions(int switchBlockId) {
        int availablePositions = -1;
        SwitchBlock switchBlock = SwitchBlocksManager.getSwitchBlock(switchBlockId);
        if (switchBlock != null) {
            availablePositions = SwitchBlocksManager.getSwitchBlock(switchBlockId).getPositions();

            Connection connection = null;
            try {
                connection = databaseConnectionsManager.takeConnection();
                List<Distributor> cablesList = Distributor.getDistributorList(connection, switchBlockId);

                for (Distributor cable : cablesList) {
                    if (cable.getNeax61EEquipment() != null) {
                        availablePositions = availablePositions - Technology.NEAX61E.getCableSize();
                    }
                    if (cable.getNeax61SigmaEquipment() != null) {
                        availablePositions = availablePositions - Technology.NEAX61SIGMA.getCableSize();
                    }
                    if (cable.getSigmaL3AddressEquipment() != null) {
                        availablePositions = availablePositions - Technology.NEAX61SIGMA_ELU.getCableSize();
                    }
                    if (cable.getZhoneEquipment() != null) {
                        availablePositions = availablePositions - Technology.ZHONE.getCableSize();
                    }
                }
                availablePositions = (availablePositions < 0) ? 0 : availablePositions;
            } catch (Exception ex) {
                String switchBlockName = switchBlock.getName();
                logger.error("Cannot calculate available positions for " + switchBlockName + ".", ex);
                JOptionPane.showMessageDialog(this, bundle.getString("EquipmentInternalFrame.availablePositions.problem") + " " + switchBlockName + ".", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }
        return availablePositions;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        switchButtonGroup = new javax.swing.ButtonGroup();
        switchBlocksScrollPane = new javax.swing.JScrollPane();
        switchBlocksList = new javax.swing.JList<>();
        distributorPanel = new javax.swing.JPanel();
        blockLabel = new javax.swing.JLabel();
        horizontalLabel = new javax.swing.JLabel();
        blockTextField = new javax.swing.JTextField();
        positionsLabel = new javax.swing.JLabel();
        positionsTextField = new javax.swing.JTextField();
        siteLabel = new javax.swing.JLabel();
        siteComboBox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        switchPanel = new javax.swing.JPanel();
        switchOptionsPanel = new javax.swing.JPanel();
        neax61eRadioButton = new javax.swing.JRadioButton();
        neax61sigmaRadioButton = new javax.swing.JRadioButton();
        neax61sigmaEluRadioButton = new javax.swing.JRadioButton();
        zhoneRadioButton = new javax.swing.JRadioButton();
        switchDataPanel = new javax.swing.JPanel();
        spceLabel = new javax.swing.JLabel();
        spceTextField = new javax.swing.JTextField();
        highwayLabel = new javax.swing.JLabel();
        highwayTextField = new javax.swing.JTextField();
        subhighwayLabel = new javax.swing.JLabel();
        subhighwayTextField = new javax.swing.JTextField();
        groupsLabel = new javax.swing.JLabel();
        initialGroupTextField = new javax.swing.JTextField();
        hyphenLabel = new javax.swing.JLabel();
        finalGroupTextField = new javax.swing.JTextField();
        frameLabel = new javax.swing.JLabel();
        frameTextField = new javax.swing.JTextField();
        moduleLabel = new javax.swing.JLabel();
        moduleTextField = new javax.swing.JTextField();
        buttonsPanel = new javax.swing.JPanel();
        createButton = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("i18n/EquipmentInternalFrameBundle"); // NOI18N
        setTitle(bundle.getString("EquipmentInternalFrame.title")); // NOI18N
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

        switchBlocksList.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        switchBlocksList.setModel(switchBlocksListModel);
        switchBlocksList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                switchBlocksListMouseClicked(evt);
            }
        });
        switchBlocksScrollPane.setViewportView(switchBlocksList);

        distributorPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("EquipmentInternalFrame.DistributorPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        blockLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        blockLabel.setText(bundle.getString("EquipmentInternalFrame.blockLabel.text")); // NOI18N

        horizontalLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        horizontalLabel.setText(bundle.getString("EquipmentInternalFrame.horizontalLabel.text")); // NOI18N

        blockTextField.setText(bundle.getString("EquipmentInternalFrame.blockTextField.text")); // NOI18N
        blockTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                blockTextFieldFocusLost(evt);
            }
        });

        positionsLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        positionsLabel.setText(bundle.getString("EquipmentInternalFrame.positionsLabel.text")); // NOI18N

        positionsTextField.setText(bundle.getString("EquipmentInternalFrame.positionsTextField.text")); // NOI18N

        siteLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        siteLabel.setText(bundle.getString("EquipmentInternalFrame.siteLabel.text")); // NOI18N

        siteComboBox.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        siteComboBox.setModel(siteComboBoxModel);
        siteComboBox.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                siteComboBoxFocusLost(evt);
            }
        });

        descriptionTextArea.setColumns(20);
        descriptionTextArea.setRows(5);
        jScrollPane1.setViewportView(descriptionTextArea);

        javax.swing.GroupLayout distributorPanelLayout = new javax.swing.GroupLayout(distributorPanel);
        distributorPanel.setLayout(distributorPanelLayout);
        distributorPanelLayout.setHorizontalGroup(
            distributorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(distributorPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(distributorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(distributorPanelLayout.createSequentialGroup()
                        .addGroup(distributorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(distributorPanelLayout.createSequentialGroup()
                                .addComponent(siteLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(siteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(distributorPanelLayout.createSequentialGroup()
                                .addComponent(positionsLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(positionsTextField)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(blockLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(horizontalLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(blockTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        distributorPanelLayout.setVerticalGroup(
            distributorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(distributorPanelLayout.createSequentialGroup()
                .addGroup(distributorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(siteLabel)
                    .addComponent(siteComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(blockLabel)
                    .addComponent(horizontalLabel)
                    .addComponent(blockTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(distributorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(positionsLabel)
                    .addComponent(positionsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        switchPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("EquipmentInternalFrame.SwitchPanel.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 12))); // NOI18N

        switchButtonGroup.add(neax61eRadioButton);
        neax61eRadioButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        neax61eRadioButton.setSelected(true);
        neax61eRadioButton.setText(bundle.getString("EquipmentInternalFrame.neax61eRadioButton.text")); // NOI18N
        neax61eRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                neax61eRadioButtonActionPerformed(evt);
            }
        });

        switchButtonGroup.add(neax61sigmaRadioButton);
        neax61sigmaRadioButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        neax61sigmaRadioButton.setText(bundle.getString("EquipmentInternalFrame.neax61sigmaRadioButton.text")); // NOI18N
        neax61sigmaRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                neax61sigmaRadioButtonActionPerformed(evt);
            }
        });

        switchButtonGroup.add(neax61sigmaEluRadioButton);
        neax61sigmaEluRadioButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        neax61sigmaEluRadioButton.setText(bundle.getString("EquipmentInternalFrame.neax61sigmaEluRadioButton.text")); // NOI18N
        neax61sigmaEluRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                neax61sigmaEluRadioButtonActionPerformed(evt);
            }
        });

        switchButtonGroup.add(zhoneRadioButton);
        zhoneRadioButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        zhoneRadioButton.setText(bundle.getString("EquipmentInternalFrame.zhoneRadioButton.text")); // NOI18N
        zhoneRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zhoneRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout switchOptionsPanelLayout = new javax.swing.GroupLayout(switchOptionsPanel);
        switchOptionsPanel.setLayout(switchOptionsPanelLayout);
        switchOptionsPanelLayout.setHorizontalGroup(
            switchOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(switchOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(neax61eRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(neax61sigmaRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(neax61sigmaEluRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                .addComponent(zhoneRadioButton)
                .addContainerGap())
        );
        switchOptionsPanelLayout.setVerticalGroup(
            switchOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(switchOptionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(switchOptionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(neax61eRadioButton)
                    .addComponent(neax61sigmaRadioButton)
                    .addComponent(zhoneRadioButton)
                    .addComponent(neax61sigmaEluRadioButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        spceLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        spceLabel.setText(bundle.getString("EquipmentInternalFrame.spceLabel.text")); // NOI18N

        spceTextField.setText(bundle.getString("EquipmentInternalFrame.spceTextField.text")); // NOI18N

        highwayLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        highwayLabel.setText(bundle.getString("EquipmentInternalFrame.highwayLabel.text")); // NOI18N

        highwayTextField.setText(bundle.getString("EquipmentInternalFrame.highwayTextField.text")); // NOI18N

        subhighwayLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        subhighwayLabel.setText(bundle.getString("EquipmentInternalFrame.subhighwayLabel.text")); // NOI18N

        subhighwayTextField.setText(bundle.getString("EquipmentInternalFrame.subhighwayTextField.text")); // NOI18N

        groupsLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        groupsLabel.setText(bundle.getString("EquipmentInternalFrame.groupsLabel.text")); // NOI18N

        initialGroupTextField.setText(bundle.getString("EquipmentInternalFrame.initialGroupTextField.text")); // NOI18N

        hyphenLabel.setText(bundle.getString("EquipmentInternalFrame.hyphenLabel.text")); // NOI18N

        finalGroupTextField.setText(bundle.getString("EquipmentInternalFrame.finalGroupTextField.text")); // NOI18N

        frameLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        frameLabel.setText(bundle.getString("EquipmentInternalFrame.frameLabel.text")); // NOI18N

        frameTextField.setText(bundle.getString("EquipmentInternalFrame.frameTextField.text")); // NOI18N

        moduleLabel.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        moduleLabel.setText(bundle.getString("EquipmentInternalFrame.moduleLabel.text")); // NOI18N

        moduleTextField.setText(bundle.getString("EquipmentInternalFrame.moduleTextField.text")); // NOI18N

        javax.swing.GroupLayout switchDataPanelLayout = new javax.swing.GroupLayout(switchDataPanel);
        switchDataPanel.setLayout(switchDataPanelLayout);
        switchDataPanelLayout.setHorizontalGroup(
            switchDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(switchDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(switchDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(switchDataPanelLayout.createSequentialGroup()
                        .addGroup(switchDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spceLabel)
                            .addComponent(highwayLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(switchDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(spceTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                            .addComponent(highwayTextField))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(subhighwayLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(subhighwayTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(switchDataPanelLayout.createSequentialGroup()
                        .addComponent(groupsLabel)
                        .addGap(26, 26, 26)
                        .addComponent(initialGroupTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hyphenLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(finalGroupTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(switchDataPanelLayout.createSequentialGroup()
                        .addComponent(frameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(frameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(moduleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(moduleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        switchDataPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {finalGroupTextField, initialGroupTextField});

        switchDataPanelLayout.setVerticalGroup(
            switchDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(switchDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(switchDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spceLabel)
                    .addComponent(spceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(switchDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(highwayLabel)
                    .addComponent(highwayTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(subhighwayLabel)
                    .addComponent(subhighwayTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(switchDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(groupsLabel)
                    .addComponent(initialGroupTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hyphenLabel)
                    .addComponent(finalGroupTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(switchDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(frameLabel)
                    .addComponent(frameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(moduleLabel)
                    .addComponent(moduleTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout switchPanelLayout = new javax.swing.GroupLayout(switchPanel);
        switchPanel.setLayout(switchPanelLayout);
        switchPanelLayout.setHorizontalGroup(
            switchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(switchPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(switchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(switchPanelLayout.createSequentialGroup()
                        .addComponent(switchDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(switchOptionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        switchPanelLayout.setVerticalGroup(
            switchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(switchPanelLayout.createSequentialGroup()
                .addComponent(switchOptionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(switchDataPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        createButton.setText(bundle.getString("EquipmentInternalFrame.createButton.text")); // NOI18N
        createButton.setEnabled(false);
        createButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buttonsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(createButton)
                .addContainerGap())
        );
        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(createButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(switchBlocksScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(distributorPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(switchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(switchBlocksScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(distributorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(switchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        open = false;
    }//GEN-LAST:event_formInternalFrameClosed

    private void neax61sigmaRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_neax61sigmaRadioButtonActionPerformed
        technology = Technology.NEAX61SIGMA;
        clearEquipment();
        enableSwitchPanelCommonComponents();
        spceLabel.setText(bundle.getString("EquipmentInternalFrame.tswLabel.text"));
        spceTextField.setText("00");
        highwayLabel.setText(bundle.getString("EquipmentInternalFrame.kHighwayLabel.text"));
        subhighwayLabel.setText(bundle.getString("EquipmentInternalFrame.pHighwayLabel.text"));
        groupsLabel.setText(bundle.getString("EquipmentInternalFrame.rowsLabel.text"));
    }//GEN-LAST:event_neax61sigmaRadioButtonActionPerformed

    private void neax61eRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_neax61eRadioButtonActionPerformed
        technology = Technology.NEAX61E;
        clearEquipment();
        enableSwitchPanelCommonComponents();
        spceLabel.setText(bundle.getString("EquipmentInternalFrame.spceLabel.text"));
        spceTextField.setText("00");
        highwayLabel.setText(bundle.getString("EquipmentInternalFrame.highwayLabel.text"));
        subhighwayLabel.setText(bundle.getString("EquipmentInternalFrame.subhighwayLabel.text"));
        groupsLabel.setText(bundle.getString("EquipmentInternalFrame.groupsLabel.text"));
    }//GEN-LAST:event_neax61eRadioButtonActionPerformed

    private void switchBlocksListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_switchBlocksListMouseClicked
        SwitchBlock selectedSwitchBlock = switchBlocksList.getSelectedValue();
        Connection connection = null;
        try {
            connection = databaseConnectionsManager.takeConnection();
            List<Distributor> distributorByBlockList = Distributor.getDistributorList(connection, selectedSwitchBlock.getId());
            String[][] data = null;
            Technology switchBlockTechnology = null;
            if (distributorByBlockList.get(0).getNeax61SigmaEquipment() != null) {
                switchBlockTechnology = Technology.NEAX61SIGMA;
                data = getNeax61SigmaEquipmentArray(distributorByBlockList);
            } else if (distributorByBlockList.get(0).getSigmaL3AddressEquipment() != null) {
                switchBlockTechnology = Technology.NEAX61SIGMA_ELU;
                data = getNeax61SigmaELUArray(distributorByBlockList);
            } else if (distributorByBlockList.get(0).getNeax61EEquipment() != null) {
                switchBlockTechnology = Technology.NEAX61E;
                data = getNeax61EEquipmentArray(distributorByBlockList);
            } else if (distributorByBlockList.get(0).getZhoneEquipment() != null) {
                switchBlockTechnology = Technology.ZHONE;
                data = getZhoneEquipmentArray(distributorByBlockList);
            }

            int availablePositions = getAvailablePositions(selectedSwitchBlock.getId());
            SwitchBlockPanel switchBlockPanel = new SwitchBlockPanel(selectedSwitchBlock.toString(), selectedSwitchBlock.getDescription(), data, selectedSwitchBlock.getPositions(), availablePositions, switchBlockTechnology);
            JOptionPane.showMessageDialog(this, switchBlockPanel, bundle.getString("SwitchBlockPanel.title"), JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            String switchBlockName = selectedSwitchBlock.getName();
            logger.error("Cannot get distributor list for " + switchBlockName + ".", ex);
            JOptionPane.showMessageDialog(this, bundle.getString("EquipmentInternalFrame.distributorList.problem") + " " + switchBlockName + ".", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            databaseConnectionsManager.returnConnection(connection);
        }

        switchBlocksList.clearSelection();
        distributorPanel.requestFocus();
    }//GEN-LAST:event_switchBlocksListMouseClicked

    private void zhoneRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zhoneRadioButtonActionPerformed
        technology = Technology.ZHONE;
        clearEquipment();
        spceLabel.setText("");
        spceLabel.setEnabled(false);
        spceTextField.setText("");
        spceTextField.setEnabled(false);
        highwayLabel.setText("");
        highwayLabel.setEnabled(false);
        highwayTextField.setEnabled(false);
        subhighwayLabel.setText("");
        subhighwayLabel.setEnabled(false);
        subhighwayTextField.setEnabled(false);
        groupsLabel.setText(bundle.getString("EquipmentInternalFrame.cablesLabel.text"));
        frameLabel.setText("");
        frameLabel.setEnabled(false);
        frameTextField.setEnabled(false);
        moduleLabel.setText("");
        moduleLabel.setEnabled(false);
        moduleTextField.setEnabled(false);
    }//GEN-LAST:event_zhoneRadioButtonActionPerformed

    private void createButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createButtonActionPerformed
        boolean valid = isValidForm();
        if (valid) {
            Connection connection = databaseConnectionsManager.takeConnection();
            Distributor distributor = new Distributor();
            BlockPosition blockPosition = new BlockPosition();
            distributor.setBlockPosition(blockPosition);
            SwitchBlock switchBlock = new SwitchBlock();
            switchBlock.setDescription(description);
            switchBlock.setName("H" + switchBlockName.toUpperCase());
            switchBlock.setSite(site);
            switchBlock.setPositions(positions);
            blockPosition.setSwitchBlock(switchBlock);
            try {
                switch (technology) {
                    case NEAX61E:
                        distributor.setNeax61EEquipment(getNeax61EEquipment());
                        distributor.insertNeax61EDistributor(connection);
                        break;
                    case NEAX61SIGMA:
                        distributor.setNeax61SigmaEquipment(getNeax61SigmaEquipment());
                        distributor.insertNeax61SigmaDistributor(connection);
                        break;
                    case NEAX61SIGMA_ELU:
                        distributor.setSigmaL3AddressEquipment(getSigmaL3AddressEquipment());
                        distributor.insertNeax61SigmaELUDistributor(connection);
                        break;
                    case ZHONE:
                        distributor.setZhoneEquipment(getZhoneEquipment());
                        distributor.insertZhoneDistributor(connection);
                        break;
                }
                JOptionPane.showMessageDialog(this, switchBlock + " " + bundle.getString("InsertDialog.confirmation"), bundle.getString("InsertDialog.title"), JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                SitesManager.fillSitesList();
                SwitchBlocksManager.loadSwitchBlocksList(connection);
                fillSwitchBlocksListModel();
            } catch (Exception exception) {
                logger.error(exception.getMessage(), exception);
                JOptionPane.showMessageDialog(this, switchBlock + " " + bundle.getString("InsertDialog.error"), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                databaseConnectionsManager.returnConnection(connection);
            }
        }
    }//GEN-LAST:event_createButtonActionPerformed

    private Neax61EEquipment getNeax61EEquipment() {
        Neax61EEquipment equipment = new Neax61EEquipment();
        equipment.setSpce(spce);
        equipment.setHighway(highway.charAt(0));
        equipment.setSubhighway(subhighway.charAt(0));
        equipment.setGroup(initialGroup);
        equipment.setLastGroup(finalGroup);
        ELineModule eLineModule = new ELineModule();
        eLineModule.setName(moduleName);
        EFrame eFrame = new EFrame();
        eFrame.setName(frameName.toUpperCase());
        eFrame.setSite(site);
        eLineModule.setFrame(eFrame);
        equipment.setELineModule(eLineModule);
        return equipment;
    }

    private Neax61SigmaEquipment getNeax61SigmaEquipment() {
        Neax61SigmaEquipment equipment = new Neax61SigmaEquipment();
        equipment.setTimeSwitch(spce);
        equipment.setkHighway(highway);
        equipment.setpHighway(subhighway);
        equipment.setRow(initialGroup.charAt(0));
        equipment.setLastRow(finalGroup.charAt(0));
        SigmaLineModule sigmaLineModule = new SigmaLineModule();
        sigmaLineModule.setName(moduleName);
        SigmaFrame sigmaFrame = new SigmaFrame();
        sigmaFrame.setName(frameName.toUpperCase());
        sigmaFrame.setSite(site);
        sigmaLineModule.setFrame(sigmaFrame);
        equipment.setSigmaLineModule(sigmaLineModule);
        return equipment;
    }

    private SigmaL3AddressEquipment getSigmaL3AddressEquipment() {
        SigmaL3AddressEquipment sigmaL3AddressEquipment = new SigmaL3AddressEquipment();
        Neax61SigmaELU sigmaELU = new Neax61SigmaELU();
        sigmaELU.setTimeSwitch(spce);
        sigmaELU.setkHighway(highway);
        sigmaELU.setpHighway(subhighway);
        sigmaELU.setName(frameName);
        for (int i = Integer.parseInt(initialGroup); i <= Integer.parseInt(finalGroup); i++) {
            sigmaELU.addDTI(i);
        }
        sigmaELU.setSite(site);
        sigmaL3AddressEquipment.setNeax61SigmaELU(sigmaELU);
        return sigmaL3AddressEquipment;
    }

    private ZhoneEquipment getZhoneEquipment() {
        ZhoneEquipment equipment = new ZhoneEquipment();
        equipment.setCable(initialGroup);
        equipment.setLastCable(finalGroup);
        return equipment;
    }

    private void blockTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_blockTextFieldFocusLost
        checkSwitchBlock();
    }//GEN-LAST:event_blockTextFieldFocusLost

    private void siteComboBoxFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_siteComboBoxFocusLost
        checkSwitchBlock();
    }//GEN-LAST:event_siteComboBoxFocusLost

    private void neax61sigmaEluRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_neax61sigmaEluRadioButtonActionPerformed
        technology = Technology.NEAX61SIGMA_ELU;
        clearEquipment();
        enableSwitchPanelCommonComponents();
        spceLabel.setText(bundle.getString("EquipmentInternalFrame.tswLabel.text"));
        spceTextField.setText("00");
        highwayLabel.setText(bundle.getString("EquipmentInternalFrame.kHighwayLabel.text"));
        subhighwayLabel.setText(bundle.getString("EquipmentInternalFrame.pHighwayLabel.text"));
        groupsLabel.setText(bundle.getString("EquipmentInternalFrame.dtisLabel.text"));
        frameLabel.setText(bundle.getString("EquipmentInternalFrame.eluLabel.text"));

        moduleLabel.setText("");
        moduleLabel.setEnabled(false);
        moduleTextField.setEnabled(false);

    }//GEN-LAST:event_neax61sigmaEluRadioButtonActionPerformed

    private void checkSwitchBlock() {
        if (((Site) siteComboBox.getSelectedItem()).getCode() != bundle.getString("EquipmentInternalFrame.Select") && !blockTextField.getText().isEmpty()) {
            StringBuilder blockName = new StringBuilder("H");
            blockName.append(blockTextField.getText().trim().toUpperCase());
            SwitchBlock switchBlock = SwitchBlocksManager.getSwitchBlock(((Site) siteComboBox.getSelectedItem()).getId(), blockName.toString());

            if (switchBlock != null) {
                int positions = switchBlock.getPositions();
                String description = switchBlock.getDescription();
                positionsTextField.setText(String.valueOf(positions));
                positionsTextField.setEnabled(false);
                descriptionTextArea.setText(description);
                descriptionTextArea.setEnabled(false);
            } else {
                positionsTextField.setText("");
                positionsTextField.setEnabled(true);
                descriptionTextArea.setText("");
                descriptionTextArea.setEnabled(true);
            }
        } else {
            positionsTextField.setText("");
            positionsTextField.setEnabled(true);
            descriptionTextArea.setText("");
            descriptionTextArea.setEnabled(true);
        }
    }

    private void enableSwitchPanelCommonComponents() {
        spceLabel.setEnabled(true);
        spceTextField.setEnabled(true);
        highwayLabel.setEnabled(true);
        highwayTextField.setEnabled(true);
        subhighwayLabel.setEnabled(true);
        subhighwayTextField.setEnabled(true);
        frameLabel.setText(bundle.getString("EquipmentInternalFrame.frameLabel.text"));
        frameLabel.setEnabled(true);
        frameTextField.setEnabled(true);
        moduleLabel.setText(bundle.getString("EquipmentInternalFrame.moduleLabel.text"));
        moduleLabel.setEnabled(true);
        moduleTextField.setEnabled(true);
    }

    private String[][] getNeax61SigmaEquipmentArray(List<Distributor> distributorList) {
        String[][] data = new String[distributorList.size()][6];
        for (int i = 0; i < distributorList.size(); i++) {
            Distributor neax61SigmaDistributor = distributorList.get(i);
            Neax61SigmaEquipment equipment = neax61SigmaDistributor.getNeax61SigmaEquipment();
            LineModule lineModule = equipment.getSigmaLineModule();
            data[i][0] = equipment.getTimeSwitch();
            data[i][1] = equipment.getkHighway();
            data[i][2] = equipment.getpHighway();
            data[i][3] = String.valueOf(equipment.getRow());
            data[i][4] = lineModule.getFrame().getName();
            data[i][5] = lineModule.toString();
        }
        return data;
    }

    private String[][] getNeax61SigmaELUArray(List<Distributor> distributorList) {
        String[][] data = new String[distributorList.size()][5];
        for (int i = 0; i < distributorList.size(); i++) {
            Distributor distributor = distributorList.get(i);
            Neax61SigmaELU sigmaELU = distributor.getSigmaL3AddressEquipment().getNeax61SigmaELU();
            data[i][0] = sigmaELU.getName();
            data[i][1] = sigmaELU.getTimeSwitch();
            data[i][2] = sigmaELU.getkHighway();
            data[i][3] = sigmaELU.getpHighway();
            data[i][4] = sigmaELU.getDTIsListString();
        }
        return data;
    }

    private String[][] getNeax61EEquipmentArray(List<Distributor> distributorList) {
        String[][] data = new String[distributorList.size()][6];
        for (int i = 0; i < distributorList.size(); i++) {
            Distributor neax61EDistributor = distributorList.get(i);
            Neax61EEquipment equipment = neax61EDistributor.getNeax61EEquipment();
            LineModule lineModule = equipment.getELineModule();
            data[i][0] = equipment.getSpce();
            data[i][1] = String.valueOf(equipment.getHighway());
            data[i][2] = String.valueOf(equipment.getSubhighway());
            data[i][3] = equipment.getGroup();
            data[i][4] = lineModule.getFrame().getName();
            data[i][5] = lineModule.toString();
        }
        return data;
    }

    private String[][] getZhoneEquipmentArray(List<Distributor> distributorList) {
        String[][] data = new String[distributorList.size()][1];
        for (int i = 0; i < distributorList.size(); i++) {
            Distributor zhoneDistributor = distributorList.get(i);
            ZhoneEquipment equipment = zhoneDistributor.getZhoneEquipment();
            data[i][0] = equipment.getCable();
        }
        return data;
    }

    private void setNewFormStructure() {
        if (privilegedUser)
            createButton.setEnabled(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel blockLabel;
    private javax.swing.JTextField blockTextField;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton createButton;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JPanel distributorPanel;
    private javax.swing.JTextField finalGroupTextField;
    private javax.swing.JLabel frameLabel;
    private javax.swing.JTextField frameTextField;
    private javax.swing.JLabel groupsLabel;
    private javax.swing.JLabel highwayLabel;
    private javax.swing.JTextField highwayTextField;
    private javax.swing.JLabel horizontalLabel;
    private javax.swing.JLabel hyphenLabel;
    private javax.swing.JTextField initialGroupTextField;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel moduleLabel;
    private javax.swing.JTextField moduleTextField;
    private javax.swing.JRadioButton neax61eRadioButton;
    private javax.swing.JRadioButton neax61sigmaEluRadioButton;
    private javax.swing.JRadioButton neax61sigmaRadioButton;
    private javax.swing.JLabel positionsLabel;
    private javax.swing.JTextField positionsTextField;
    private javax.swing.JComboBox<Site> siteComboBox;
    private javax.swing.JLabel siteLabel;
    private javax.swing.JLabel spceLabel;
    private javax.swing.JTextField spceTextField;
    private javax.swing.JLabel subhighwayLabel;
    private javax.swing.JTextField subhighwayTextField;
    private javax.swing.JList<SwitchBlock> switchBlocksList;
    private javax.swing.JScrollPane switchBlocksScrollPane;
    private javax.swing.ButtonGroup switchButtonGroup;
    private javax.swing.JPanel switchDataPanel;
    private javax.swing.JPanel switchOptionsPanel;
    private javax.swing.JPanel switchPanel;
    private javax.swing.JRadioButton zhoneRadioButton;
    // End of variables declaration//GEN-END:variables

    private DatabaseConnectionsManager databaseConnectionsManager;
    private User user;
    private boolean privilegedUser;

    private SitesAnalyzer sitesAnalyzer;
    private DefaultListModel<SwitchBlock> switchBlocksListModel;
    private DefaultComboBoxModel<Site> siteComboBoxModel;
    private Site site;
    private String switchBlockName;
    private int positions;
    private String description;
    private Technology technology;
    private String spce;
    private String highway;
    private String subhighway;
    private String initialGroup;
    private String finalGroup;
    private String frameName;
    private String moduleName;

    private static boolean open;

    private ResourceBundle bundle = ResourceBundle.getBundle("i18n/EquipmentInternalFrameBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(EquipmentInternalFrame.class);
}
