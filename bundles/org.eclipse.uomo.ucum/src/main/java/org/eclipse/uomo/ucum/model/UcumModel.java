/*******************************************************************************
 * Crown Copyright (c) 2006, 2013, Copyright (c) 2006, 2017 Kestral Computing P/L and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Kestral Computing P/L - initial implementation
 *    Werner Keil - Enhanced and unified with other UOMo parts 
 *******************************************************************************/

package org.eclipse.uomo.ucum.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.uomo.core.IName;
import org.eclipse.uomo.core.IVersion;

public class UcumModel implements IName, IVersion {

	/**
	 * UCUM version
	 */
	private final String version;

	/**
	 * revision="$Revision: 1.1 $"
	 */
	// private final String revision;

	/**
	 * date this revision was made public
	 */
	private final Date revisionDate;

	private final List<Prefix> prefixes = new ArrayList<Prefix>();
	private final List<BaseUnit> baseUnits = new ArrayList<BaseUnit>();
	private final List<DefinedUnit> definedUnits = new ArrayList<DefinedUnit>();

	/**
	 * @param revision
	 * @param revisionDate
	 */
	public UcumModel(String version, String revision, Date revisionDate) {
		super();
		this.version = version;
		// this.revision = revision;
		this.revisionDate = revisionDate;
	}

	public String getName() {
		return "UCUM";
	}

	/**
	 * @return the prefixes
	 */
	public List<Prefix> getPrefixes() {
		return prefixes;
	}

	/**
	 * @return the baseUnits
	 */
	public List<BaseUnit> getBaseUnits() {
		return baseUnits;
	}

	/**
	 * @return the units
	 */
	public List<DefinedUnit> getDefinedUnits() {
		return definedUnits;
	}

	/**
	 * @return the revision
	 */
	// public String getRevision() {
	// return revision;
	// }
	// /**
	// * @param revision the revision to set
	// */
	// public void setRevision(String revision) {
	// this.revision = revision;
	// }
	/**
	 * @return the revisionDate
	 */
	public Date getRevisionDate() {
		return revisionDate;
	}

	// /**
	// * @param revisionDate the revisionDate to set
	// */
	// public void setRevisionDate(Date revisionDate) {
	// this.revisionDate = revisionDate;
	// }
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	// /**
	// * @param version the version to set
	// */
	// public void setVersion(String version) {
	// this.version = version;
	// }

	public UcumUnit getUnit(String code) {
		for (UcumUnit unit : getBaseUnits()) {
			if (unit.getCode().equals(code))
				return unit;
		}
		for (UcumUnit unit : getDefinedUnits()) {
			if (unit.getCode().equals(code))
				return unit;
		}
		return null;
	}

	public BaseUnit getBaseUnit(String code) {
		for (BaseUnit unit : getBaseUnits()) {
			if (unit.getCode().equals(code))
				return unit;
		}
		return null;
	}
}
