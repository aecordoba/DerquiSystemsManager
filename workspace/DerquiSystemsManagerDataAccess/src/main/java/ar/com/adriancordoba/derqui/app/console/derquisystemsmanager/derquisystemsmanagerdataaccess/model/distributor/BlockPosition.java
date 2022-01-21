/*
 * 		BlockPosition.java
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
 * 		BlockPosition.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Sep 14, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.managers.SwitchBlocksManager;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class BlockPosition {
	private int id;
	private String position;
	private SwitchBlock switchBlock;

	/**
	 * 
	 */
	public BlockPosition() {
	}

	/**
	 * @param id
	 * @param position
	 * @param switchBlock
	 */
	public BlockPosition(int id, String position, SwitchBlock switchBlock) {
		this.id = id;
		this.position = position;
		this.switchBlock = switchBlock;
	}

	public BlockPosition(ResultSet resultSet, boolean wiringData) throws SQLException {
		if (wiringData) {
			setId(resultSet.getInt("blockPositionId"));
			setPosition(resultSet.getString("blockPositionsPosition"));
		}

		SwitchBlock switchBlock = SwitchBlocksManager.getSwitchBlock(resultSet.getInt("switchBlockId"));
		setSwitchBlock(switchBlock);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(switchBlock.toString());
		stringBuilder.append(" - ");
		stringBuilder.append(position);
		return stringBuilder.toString();
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the switchBlock
	 */
	public SwitchBlock getSwitchBlock() {
		return switchBlock;
	}

	/**
	 * @param switchBlock the switchBlock to set
	 */
	public void setSwitchBlock(SwitchBlock switchBlock) {
		this.switchBlock = switchBlock;
	}
}
