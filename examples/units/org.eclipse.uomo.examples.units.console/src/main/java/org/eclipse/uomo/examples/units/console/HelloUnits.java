/**
 * Copyright (c) 2005, 2013, Werner Keil and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Werner Keil - initial API and implementation
 */
package org.eclipse.uomo.examples.units.console;

import org.eclipse.uomo.units.IMeasure;
import org.eclipse.uomo.units.SI;
import org.eclipse.uomo.units.impl.quantity.AreaAmount;
import org.eclipse.uomo.units.impl.quantity.LengthAmount;
import org.eclipse.uomo.units.impl.quantity.TimeAmount;
import org.eclipse.uomo.units.impl.system.USCustomary;
import javax.measure.Unit;
import javax.measure.UnitConverter;
import org.unitsofmeasurement.quantity.Acceleration;
import org.unitsofmeasurement.quantity.Area;
import org.unitsofmeasurement.quantity.Length;
import org.unitsofmeasurement.quantity.Time;

/**
 * A 'Hello World!' style example showing some basic units and operations.
 * @author Werner Keil
 * @version 0.8
 */
public class HelloUnits {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LengthAmount length = new LengthAmount(10, SI.METRE);
//		LengthAmount length = new LengthAmount(10, SI.KILOGRAM); // this won't work ;-)
		
		System.out.println(length);
		Unit<Length> lenUnit =  length.unit();
		
		System.out.print(length.doubleValue(USCustomary.FOOT)); 
		System.out.println(" " + USCustomary.FOOT);
//		System.out.println(length.doubleValue(USCustomary.POUND)); // this won't work either.
		UnitConverter inchConverter = lenUnit.getConverterTo(USCustomary.INCH);
		System.out.print(inchConverter.convert(length.getValue().doubleValue()));		
		System.out.println(" " + USCustomary.INCH);
		
		@SuppressWarnings("unchecked")
		AreaAmount area = new AreaAmount(length.getValue().doubleValue() * length.getValue().doubleValue(), 
				(Unit<Area>) length.unit().multiply(SI.METRE));
		System.out.println(area);
		
		// Equivalent to 
		IMeasure<Length> meters = new LengthAmount(5, SI.METRE);
		IMeasure<Time> secs = new TimeAmount(2, SI.SECOND);
		@SuppressWarnings("unchecked")
		IMeasure<Acceleration> speed = (IMeasure<Acceleration>) meters.divide(secs);
		System.out.println(meters + 
				"; " + secs +
				"; " + speed);
	}
}
