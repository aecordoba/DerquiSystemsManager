/*
 * 		SubscriberRestriction.java
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
 * 		SubscriberRestriction.java
 *  Adrián E. Córdoba [software.asia@gmail.com]		Nov 28, 2017
 */
package ar.com.adriancordoba.derqui.app.console.derquisystemsmanager.derquisystemsmanagerdataaccess.model.subscriberdata.restriction;

/**
 * @author Adrián E. Córdoba [software.asia@gmail.com]
 *
 */
public class SubscriberRestriction {
	private int id;
	private OriginationRestriction originationRestriction;
	private TerminationRestriction terminationRestriction;

	/**
	 * 
	 */
	public SubscriberRestriction() {
	}

	/**
	 * @param originationRestriction
	 * @param terminationRestriction
	 */
	public SubscriberRestriction(OriginationRestriction originationRestriction, TerminationRestriction terminationRestriction) {
		this.originationRestriction = originationRestriction;
		this.terminationRestriction = terminationRestriction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return terminationRestriction.toString() + originationRestriction.toString();
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
	 * @return the originationRestriction
	 */
	public OriginationRestriction getOriginationRestriction() {
		return originationRestriction;
	}

	/**
	 * @param originationRestriction the originationRestriction to set
	 */
	public void setOriginationRestriction(OriginationRestriction originationRestriction) {
		this.originationRestriction = originationRestriction;
	}

	/**
	 * @return the terminationRestriction
	 */
	public TerminationRestriction getTerminationRestriction() {
		return terminationRestriction;
	}

	/**
	 * @param terminationRestriction the terminationRestriction to set
	 */
	public void setTerminationRestriction(TerminationRestriction terminationRestriction) {
		this.terminationRestriction = terminationRestriction;
	}
}
