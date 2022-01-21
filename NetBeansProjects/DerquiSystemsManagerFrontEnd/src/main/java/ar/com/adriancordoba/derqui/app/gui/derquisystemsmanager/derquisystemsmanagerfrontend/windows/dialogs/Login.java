/*
 * 		Login.java
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
 * 		Login.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Jul 6, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.windows.dialogs;

import ar.com.adriancordoba.app.console.commonservices.database.DatabaseConnectionsManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.user.User;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class Login {
    private static ResourceBundle bundle = ResourceBundle.getBundle("i18n/LoginBundle"); // NOI18N
    private static final Logger logger = LogManager.getLogger(Login.class);

    public static User logUser() {
        User user = null;

        DatabaseConnectionsManager databaseConnectionsManager = DatabaseConnectionsManager.getInstance();
        Connection connection = databaseConnectionsManager.takeConnection();
        LoginPanel loginPanel = new LoginPanel();

        URL resource = Login.class.getResource("/images/rubik.jpg");
        ImageIcon icon = null;
        try {
            BufferedImage image = ImageIO.read(resource);
            icon = new ImageIcon(image);
        } catch (IOException ex) {
            logger.error("Cannot read icon image.", ex);
        }
        

        try {
            for (int i = 1; i <= 3; i++) {
                int option = JOptionPane.showConfirmDialog(null, loginPanel, bundle.getString("LoginDialog.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, icon);
                if (option == JOptionPane.OK_OPTION) {
                    String username = loginPanel.getUsername();
                    user = User.getUser(connection, username, loginPanel.getPassword());
                    if (user != null) {
                        logger.info("User " + user.getUsername() + " started a new session.");
                        if (!user.getUsername().equals("SMadmin")) {
                            if (!user.validate(connection)) {
                                logger.warn("User " + username + " has reached application's end of life.");
                                JOptionPane.showMessageDialog(loginPanel, bundle.getString("LoginDialog.endoflife"), bundle.getString("LoginDialog.error.title"), JOptionPane.ERROR_MESSAGE);
                                user = null;
                            }
                        }
                        break;
                    } else {
                        logger.warn("User " + username + " login failed. (#" + i + ")");
                        loginPanel.setFeedbackText(bundle.getString("LoginDialog.warning"));
                    }
                } else {
                    logger.warn("Login canceled.");
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("Cannot get User from database.", e);
            JOptionPane.showMessageDialog(null, bundle.getString("LoginDialog.error.message"), bundle.getString("LoginDialog.error.title"), JOptionPane.ERROR_MESSAGE);
        } finally {
            databaseConnectionsManager.returnConnection(connection);
        }

        return user;
    }
}
