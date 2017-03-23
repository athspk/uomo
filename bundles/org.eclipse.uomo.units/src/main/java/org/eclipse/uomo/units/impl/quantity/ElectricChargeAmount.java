/**
 * Copyright (c) 2005, 2013, Werner Keil, Ikayzo and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Werner Keil, Ikayzo and others - initial API and implementation
 */
package org.eclipse.uomo.units.impl.quantity;

import org.eclipse.uomo.units.impl.BaseQuantity;
import org.unitsofmeasurement.quantity.ElectricCharge;
import org.unitsofmeasurement.unit.Unit;

/**
 * Represents an electric charge.
 * The metric system unit for this quantity is "C" (Coulomb).
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @author  <a href="mailto:uomo@catmedia.us">Werner Keil</a>
 * @version 1.2, $Date: 2013-04-28 $
 */
public final class ElectricChargeAmount extends BaseQuantity<ElectricCharge> {

	public ElectricChargeAmount(Number number, Unit<ElectricCharge> unit) {
		super(number, unit);
	}
}
