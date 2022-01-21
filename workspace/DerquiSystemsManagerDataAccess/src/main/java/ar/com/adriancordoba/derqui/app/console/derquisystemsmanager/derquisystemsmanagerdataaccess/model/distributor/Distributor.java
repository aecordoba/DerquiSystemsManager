/*
 * 		Distributor.java
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
 * 		Distributor.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Sep 16, 2016
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.distributor.managers.SwitchBlocksManager;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Equipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.LineModule;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Neax61EEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Neax61SigmaELU;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.Neax61SigmaEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.SigmaL3AddressEquipment;
import ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.equipment.ZhoneEquipment;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class Distributor {
	private int id;
	private BlockPosition blockPosition;
	private Neax61EEquipment neax61EEquipment;
	private Neax61SigmaEquipment neax61SigmaEquipment;
	private SigmaL3AddressEquipment sigmaL3AddressEquipment;
	private ZhoneEquipment zhoneEquipment;
	private boolean available;

	private static final Logger logger = LogManager.getLogger(Distributor.class);

	/**
	 * 
	 */
	public Distributor() {
	}

	/**
	 * @param id
	 * @param blockPosition
	 * @param neax61eEquipment
	 * @param neax61SigmaEquipment
	 * @param zhoneEquipment
	 * @param available
	 */
	public Distributor(int id, BlockPosition blockPosition, Neax61EEquipment neax61eEquipment, Neax61SigmaEquipment neax61SigmaEquipment, SigmaL3AddressEquipment sigmaL3Address, ZhoneEquipment zhoneEquipment, boolean available) {
		this.id = id;
		this.blockPosition = blockPosition;
		this.neax61EEquipment = neax61eEquipment;
		this.neax61SigmaEquipment = neax61SigmaEquipment;
		this.sigmaL3AddressEquipment = sigmaL3Address;
		this.zhoneEquipment = zhoneEquipment;
		this.available = available;
	}

	public Distributor(ResultSet resultSet) throws SQLException {
		setId(resultSet.getInt("distributorId"));
		setBlockPosition(new BlockPosition(resultSet, true));

		int neax61sigmaId = resultSet.getInt("neax61sigmaId");
		if (neax61sigmaId != 0) {
			Neax61SigmaEquipment neax61SigmaEquipment = new Neax61SigmaEquipment(resultSet, true);
			setNeax61SigmaEquipment(neax61SigmaEquipment);
		}
		int sigmal3addrId = resultSet.getInt("sigmal3addrId");
		if (sigmal3addrId != 0) {
			SigmaL3AddressEquipment neax61SigmaEquipment = new SigmaL3AddressEquipment(resultSet);
			setSigmaL3AddressEquipment(neax61SigmaEquipment);
		}
		int neax61eId = resultSet.getInt("neax61eId");
		if (neax61eId != 0) {
			Neax61EEquipment neax61eEquipment = new Neax61EEquipment(resultSet, true);
			setNeax61EEquipment(neax61eEquipment);
		}
		int zhoneId = resultSet.getInt("zhoneId");
		if (zhoneId != 0) {
			ZhoneEquipment zhoneEquipment = new ZhoneEquipment(resultSet, true);
			setZhoneEquipment(zhoneEquipment);
		}
	}

	public static Distributor getDistributor(Connection connection, int officeCodeId, String number) throws Exception {
		Distributor distributor = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getDistributor(?, ?)}");
			statement.setInt(1, officeCodeId);
			statement.setString(2, number);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.first()) {
				distributor = new Distributor();
				distributor.setId(resultSet.getInt("distributorId"));
				distributor.setAvailable(resultSet.getBoolean("distributorAvailable"));

				BlockPosition blockPosition = new BlockPosition();
				blockPosition.setId(resultSet.getInt("blockPositionId"));
				blockPosition.setPosition(resultSet.getString("blockPosition"));

				SwitchBlock switchBlock = SwitchBlocksManager.getSwitchBlock(resultSet.getInt("switchBlockId"));

				blockPosition.setSwitchBlock(switchBlock);
				distributor.setBlockPosition(blockPosition);

				int neax61sigmaId = resultSet.getInt("neax61sigmaId");
				if (neax61sigmaId != 0) {
					Neax61SigmaEquipment neax61SigmaEquipment = new Neax61SigmaEquipment(resultSet);
					distributor.setNeax61SigmaEquipment(neax61SigmaEquipment);
				}
				int sigmal3addrId = resultSet.getInt("sigmal3addrId");
				if (sigmal3addrId != 0) {
					SigmaL3AddressEquipment neax61SigmaEquipment = new SigmaL3AddressEquipment(resultSet);
					distributor.setSigmaL3AddressEquipment(neax61SigmaEquipment);
				}
				int neax61eId = resultSet.getInt("neax61eId");
				if (neax61eId != 0) {
					Neax61EEquipment neax61eEquipment = new Neax61EEquipment(resultSet);
					distributor.setNeax61EEquipment(neax61eEquipment);
				}
				int zhoneId = resultSet.getInt("zhoneId");
				if (zhoneId != 0) {
					ZhoneEquipment zhoneEquipment = new ZhoneEquipment(resultSet);
					distributor.setZhoneEquipment(zhoneEquipment);
				}
			}
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getDistributor().", e);
			throw new Exception("Cannot get distributor from database.");
		} finally {
			statement.close();
		}
		return distributor;
	}

	public static List<Distributor> getDistributorList(Connection connection, int switchBlockId) throws Exception {
		List<Distributor> distributorList = null;
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call getDistributorList(?)}");
			statement.setInt(1, switchBlockId);
			ResultSet resultSet = statement.executeQuery();
			distributorList = processResultSet(resultSet);
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure getDistributorList().", e);
			throw new Exception("Cannot get distributor list from database.");
		} finally {
			statement.close();
		}
		return distributorList;
	}

	private static List<Distributor> processResultSet(ResultSet resultSet) throws SQLException {
		List<Distributor> distributorList = new ArrayList<>();
		while (resultSet.next()) {
			Distributor distributor = new Distributor();
			distributor.setId(resultSet.getInt("distributorId"));
			distributor.setAvailable(resultSet.getBoolean("available"));
			distributor.setBlockPosition(new BlockPosition(resultSet, false));
			int neax61sigmaId = resultSet.getInt("neax61sigmaId");
			if (neax61sigmaId != 0) {
				Neax61SigmaEquipment neax61SigmaEquipment = new Neax61SigmaEquipment(resultSet, false);
				distributor.setNeax61SigmaEquipment(neax61SigmaEquipment);
			}
			int sigmaeluId = resultSet.getInt("sigmaeluId");
			if (sigmaeluId != 0) {
				SigmaL3AddressEquipment sigmaL3AddressEquipment = new SigmaL3AddressEquipment();
				Neax61SigmaELU neax61SigmaELU = new Neax61SigmaELU(resultSet, true);
				sigmaL3AddressEquipment.setNeax61SigmaELU(neax61SigmaELU);
				distributor.setSigmaL3AddressEquipment(sigmaL3AddressEquipment);
			}
			int neax61eId = resultSet.getInt("neax61eId");
			if (neax61eId != 0) {
				Neax61EEquipment neax61eEquipment = new Neax61EEquipment(resultSet, false);
				distributor.setNeax61EEquipment(neax61eEquipment);
			}
			int zhoneId = resultSet.getInt("zhoneId");
			if (zhoneId != 0) {
				ZhoneEquipment zhoneEquipment = new ZhoneEquipment(resultSet, false);
				distributor.setZhoneEquipment(zhoneEquipment);
			}

			distributorList.add(distributor);
		}
		return distributorList;

	}

	public void insertNeax61EDistributor(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call insertEDistributor(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			setCommonParameters(statement);
			statement.setString(5, neax61EEquipment.getSpce());
			statement.setString(6, String.valueOf(neax61EEquipment.getHighway()));
			statement.setString(7, String.valueOf(neax61EEquipment.getSubhighway()));
			statement.setString(8, neax61EEquipment.getGroup());
			statement.setString(9, neax61EEquipment.getLastGroup());
			LineModule lineModule = neax61EEquipment.getELineModule();
			statement.setString(10, lineModule.getName());
			statement.setString(11, lineModule.getFrame().getName());
			statement.execute();
			logger.debug(getBlockPosition().getSwitchBlock() + " data was inserted in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure insertEDistributor().", e);
			throw new Exception("Cannot insert distributor data in database.");
		} finally {
			statement.close();
		}
	}

	public void insertNeax61SigmaDistributor(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call insertSigmaDistributor(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			setCommonParameters(statement);
			statement.setString(5, neax61SigmaEquipment.getTimeSwitch());
			statement.setString(6, neax61SigmaEquipment.getkHighway());
			statement.setString(7, neax61SigmaEquipment.getpHighway());
			statement.setString(8, String.valueOf(neax61SigmaEquipment.getRow()));
			statement.setString(9, String.valueOf(neax61SigmaEquipment.getLastRow()));
			LineModule lineModule = neax61SigmaEquipment.getSigmaLineModule();
			statement.setString(10, lineModule.getName());
			statement.setString(11, lineModule.getFrame().getName());
			statement.execute();
			logger.debug(getBlockPosition().getSwitchBlock() + " data was inserted in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure insertSigmaDistributor().", e);
			throw new Exception("Cannot insert distributor data in database.");
		} finally {
			statement.close();
		}
	}

	public void insertNeax61SigmaELUDistributor(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call insertSigmaELUDistributor(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			setCommonParameters(statement);
			Neax61SigmaELU neax61SigmaELUEquipment = sigmaL3AddressEquipment.getNeax61SigmaELU();
			statement.setString(5, neax61SigmaELUEquipment.getTimeSwitch());
			statement.setString(6, neax61SigmaELUEquipment.getkHighway());
			statement.setString(7, neax61SigmaELUEquipment.getpHighway());
			statement.setString(8, neax61SigmaELUEquipment.getName());
			List<Integer> dtisList = neax61SigmaELUEquipment.getDTIsList();
			statement.setInt(9, dtisList.get(0));
			statement.setInt(10, dtisList.get(dtisList.size() - 1));
			statement.execute();
			logger.debug(getBlockPosition().getSwitchBlock() + " data was inserted in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure insertSigmaELUDistributor().", e);
			throw new Exception("Cannot insert distributor data in database.");
		} finally {
			statement.close();
		}
	}

	public void insertZhoneDistributor(Connection connection) throws Exception {
		CallableStatement statement = null;
		try {
			statement = connection.prepareCall("{call insertZhoneDistributor(?, ?, ?, ?, ?, ?)}");
			setCommonParameters(statement);
			statement.setString(5, zhoneEquipment.getCable());
			statement.setString(6, zhoneEquipment.getLastCable());
			statement.execute();
			logger.debug(getBlockPosition().getSwitchBlock() + " data was inserted in database.");
		} catch (SQLException e) {
			logger.error("Cannot execute stored procedure insertZhoneDistributor().", e);
			throw new Exception("Cannot insert distributor data in database.");
		} finally {
			statement.close();
		}
	}

	private void setCommonParameters(CallableStatement statement) throws SQLException {
		SwitchBlock switchBlock = blockPosition.getSwitchBlock();
		statement.setString(1, switchBlock.getName());
		statement.setInt(2, switchBlock.getSite().getId());
		statement.setString(3, switchBlock.getDescription());
		statement.setInt(4, switchBlock.getPositions());
	}

	public Equipment getEquipment() {
		Equipment equipment = null;
		if (neax61EEquipment != null)
			equipment = neax61EEquipment;
		else if (neax61SigmaEquipment != null)
			equipment = neax61SigmaEquipment;
		else if (sigmaL3AddressEquipment != null)
			equipment = sigmaL3AddressEquipment;
		else if (zhoneEquipment != null)
			equipment = zhoneEquipment;
		return equipment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder(blockPosition.toString());
		stringBuilder.append(" (");
		stringBuilder.append(getEquipment());
		stringBuilder.append(")");
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
	 * @return the blockPosition
	 */
	public BlockPosition getBlockPosition() {
		return blockPosition;
	}

	/**
	 * @param blockPosition the blockPosition to set
	 */
	public void setBlockPosition(BlockPosition blockPosition) {
		this.blockPosition = blockPosition;
	}

	/**
	 * @return the neax61EEquipment
	 */
	public Neax61EEquipment getNeax61EEquipment() {
		return neax61EEquipment;
	}

	/**
	 * @param neax61eEquipment the neax61EEquipment to set
	 */
	public void setNeax61EEquipment(Neax61EEquipment neax61eEquipment) {
		neax61EEquipment = neax61eEquipment;
	}

	/**
	 * @return the neax61SigmaEquipment
	 */
	public Neax61SigmaEquipment getNeax61SigmaEquipment() {
		return neax61SigmaEquipment;
	}

	/**
	 * @param neax61SigmaEquipment the neax61SigmaEquipment to set
	 */
	public void setNeax61SigmaEquipment(Neax61SigmaEquipment neax61SigmaEquipment) {
		this.neax61SigmaEquipment = neax61SigmaEquipment;
	}

	/**
	 * @return the sigmaL3Address
	 */
	public SigmaL3AddressEquipment getSigmaL3AddressEquipment() {
		return sigmaL3AddressEquipment;
	}

	/**
	 * @param sigmaL3Address the sigmaL3Address to set
	 */
	public void setSigmaL3AddressEquipment(SigmaL3AddressEquipment sigmaL3Address) {
		this.sigmaL3AddressEquipment = sigmaL3Address;
	}

	/**
	 * @return the zhoneEquipment
	 */
	public ZhoneEquipment getZhoneEquipment() {
		return zhoneEquipment;
	}

	/**
	 * @param zhoneEquipment the zhoneEquipment to set
	 */
	public void setZhoneEquipment(ZhoneEquipment zhoneEquipment) {
		this.zhoneEquipment = zhoneEquipment;
	}

	/**
	 * @return the available
	 */
	public boolean isAvailable() {
		return available;
	}

	/**
	 * @param available the available to set
	 */
	public void setAvailable(boolean available) {
		this.available = available;
	}
}
