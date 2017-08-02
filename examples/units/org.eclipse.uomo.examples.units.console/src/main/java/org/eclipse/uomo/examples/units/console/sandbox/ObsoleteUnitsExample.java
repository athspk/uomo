/**
 * Copyright (c) 2012, 2013, Werner Keil and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Werner Keil - initial API and implementation
 */
package org.eclipse.uomo.examples.units.console.sandbox;

import static org.eclipse.uomo.examples.units.types.GermanObsolete.*;
import static org.eclipse.uomo.units.impl.system.SI.*;
import static org.eclipse.uomo.units.impl.system.USCustomary.FOOT;

import org.eclipse.uomo.examples.units.types.PolishObsolete;
import javax.measure.Quantity;
import org.eclipse.uomo.units.impl.BaseAmount;
import javax.measure.quantity.Length;

/**
 * @author Werner Keil
 *
 */
public class ObsoleteUnitsExample {

	/**
	 * @param args
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		Quantity<Length> l = BaseAmount.valueOf(10, METRE);
		System.out.println(l);
		System.out.println(l.to(FOOT));
		System.out.println(l.to(FOOT_ZURICH));
		System.out.println(l.to(STONE_FOOT));
		System.out.println(l.to(FOOT_LAUSANNE));
		System.out.println(l.to(ELL_NORTH));
		System.out.println(l.to(ELL_SOUTH));
		System.out.println(l.to(PolishObsolete.ELL));
	}

}
