/**
 * Copyright (c) 2005, 2013, Werner Keil and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Werner Keil, Jean-Marie Dautelle - initial API and implementation
 */
package org.eclipse.uomo.units.impl.quantity;

import org.eclipse.uomo.units.impl.BaseQuantity;
import org.unitsofmeasurement.quantity.MagneticFlux;
import org.unitsofmeasurement.unit.Unit;

/**
 * Represents a magnetic flux. The metric system unit for this quantity is "Wb"
 * (Weber).
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @author <a href="mailto:uomo@catmedia.us">Werner Keil</a>
 * @version 1.2, $Date: 2010-05-21 $
 */
public class MagneticFluxAmount extends BaseQuantity<MagneticFlux> implements
		MagneticFlux {

	public MagneticFluxAmount(Number number, Unit<MagneticFlux> unit) {
		super(number, unit);
	}
}
