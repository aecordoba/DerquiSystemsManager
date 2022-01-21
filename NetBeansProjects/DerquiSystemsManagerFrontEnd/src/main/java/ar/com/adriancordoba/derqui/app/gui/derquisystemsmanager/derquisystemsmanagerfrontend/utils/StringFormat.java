/*
 * 		StringFormat.java
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
 * 		StringFormater.java
 *  Adrián E. Córdoba [software.asia@gmail.com] 		Aug 8, 2016
 */
package ar.com.adriancordoba.derqui.app.gui.derquisystemsmanager.derquisystemsmanagerfrontend.utils;

/**
 *
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 */
public class StringFormat {
    public static boolean isNoun(String noun) {
        boolean result = true;
        String[] nounsArray = noun.split("\\s+");
        for (String str : nounsArray) {
            for (int i = 0; i < str.length(); i++) {
                char character = str.charAt(i);
                if (!Character.isLetter(character)) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public static boolean isInteger(String integer) {
        boolean result = true;
        for (int i = 0; i < integer.length(); i++) {
            char character = integer.charAt(i);
            if (!Character.isDigit(character)) {
                result = false;
                break;
            }
        }
        return result;
    }

    public static boolean isPhoneNumber(String number) {
        boolean result = true;
        int length = number.length();
        if (length > 5) {
            for (int i = 0; i < length; i++) {
                char character = number.charAt(i);
                if (!Character.isDigit(character) && character != ' ' && character != '-')
                    result = false;
                else if ((character == ' ' || character == '-') && i != length - 5)
                    result = false;
            }
        } else
            result = false;
        return result;
    }

    public static String capitalizeNames(String names) {
        String result = null;
        if (names != null && !names.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            String[] namesArray = names.split("\\s+");
            int length = namesArray.length;
            for (int i = 0; i < (length - 1); i++) {
                stringBuilder.append(namesArray[i].substring(0, 1).toUpperCase());
                stringBuilder.append(namesArray[i].substring(1));
                stringBuilder.append(" ");
            }
            stringBuilder.append(namesArray[length - 1].substring(0, 1).toUpperCase());
            stringBuilder.append(namesArray[length - 1].substring(1));
            result = stringBuilder.toString();
        }
        return result;
    }

    public static String capitalize(String str) {
        String result = null;
        if (str != null)
            result = str.toUpperCase();
        return result;
    }

    public static char capitalize(char character){
        return Character.toUpperCase(character);
    }
    
    public static String getNumber(String phoneNumber) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < phoneNumber.length(); i++) {
            char character = phoneNumber.charAt(i);
            if (Character.isDigit(character))
                stringBuilder.append(character);
        }
        return stringBuilder.toString();
    }
}
