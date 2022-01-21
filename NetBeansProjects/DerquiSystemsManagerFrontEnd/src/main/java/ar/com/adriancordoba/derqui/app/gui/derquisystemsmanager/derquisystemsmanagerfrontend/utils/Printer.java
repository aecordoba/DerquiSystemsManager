/*
 * 		Printer.java
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
 * 		Printer.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		May 11, 2018
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.LinkedHashMap;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class Printer implements Printable {
    private LinkedHashMap<String, String> content;
    private static final String FONT_NAME = "serif";
    private static final int FONT_SIZE = 12;

    private static final Logger logger = LogManager.getLogger(Printer.class);

    public Printer(LinkedHashMap<String, String> content) {
        this.content = content;
    }

    public Printer() {
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        drawContent(graphics);

        return PAGE_EXISTS;
    }

    public void setContent(LinkedHashMap<String, String> content) {
        this.content = content;
    }

    private void drawContent(Graphics graphics) {
        FontMetrics metrics = graphics.getFontMetrics(new Font (FONT_NAME, Font.BOLD, FONT_SIZE));
        Font normalFont = new Font (FONT_NAME, Font.PLAIN, FONT_SIZE);
        Font boldFont = new Font (FONT_NAME, Font.BOLD, FONT_SIZE);

        Set<String> keysSet = content.keySet();
        String[] keysArray = new String[keysSet.size()];
        keysSet.toArray(keysArray);
        int y = 20;

        graphics.setFont(boldFont);
        graphics.drawString(keysArray[0], 10, y);
        graphics.setFont(normalFont);
        graphics.drawString(content.get(keysArray[0]), metrics.stringWidth(keysArray[0]) + 15, y);
        y += 10;
        graphics.drawString("-------------------------------------------------------------------------------------------------------------------", 10, y);
        y += 20;

        int advance = getAdvance(metrics, keysSet) + 20;
        for (int i = 1; i < keysArray.length; i++) {
            String key = keysArray[i];
            String value = (content.get(key) == null) ? "" : content.get(key);
            graphics.setFont(boldFont);
            graphics.drawString(key, 10, y);
            graphics.setFont(normalFont);
            graphics.drawString(value, advance, y);
            y += 20;
        }
    }
    
    private int getAdvance(FontMetrics metrics, Set<String> keysSet){
        String maxLengthString = "";
        int maxLength = maxLengthString.length();
        for (String key : keysSet){
            int keyLength = key.length();
            if(keyLength > maxLength){
                maxLengthString = key;
                maxLength = maxLengthString.length();
            }
        }
        return metrics.stringWidth(maxLengthString);
    }
}
