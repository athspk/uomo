/**
 * Copyright (c) 2005, 2014, Werner Keil and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Werner Keil - initial API and implementation
 */
package org.eclipse.uomo.examples.units.console.sandbox;

import org.eclipse.uomo.examples.units.types.Seismic;
import javax.measure.Quantity;
import org.eclipse.uomo.units.impl.quantity.EnergyAmount;
import javax.measure.quantity.Energy;

/**
 * @author Werner Keil
 *
 */
public class SeismicExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Quantity<Energy> e = new EnergyAmount(8.3, Seismic.RICHTER_MAGNITUDE);
		System.out.println(e);
	}

}
