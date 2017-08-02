/*
 * Copyright (c) 2005, 2017, Werner Keil and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Werner Keil - initial API and implementation
 */
package org.eclipse.uomo.examples.business.console.money;

import static org.eclipse.uomo.units.impl.system.SI.METRE;
import static org.eclipse.uomo.units.impl.system.SI.Prefix.KILO;
// Constants (Java 5 static import)
import static org.eclipse.uomo.units.impl.system.USCustomary.GALLON_LIQUID;
import static org.eclipse.uomo.units.impl.system.USCustomary.LITER;
import static org.eclipse.uomo.units.impl.system.USCustomary.MILE;
import static org.eclipse.uomo.business.money.MonetaryUnits.INR;
import static org.eclipse.uomo.business.money.MoneyUnit.EUR;
import static org.eclipse.uomo.business.money.MoneyUnit.GBP;
import static org.eclipse.uomo.business.money.MoneyUnit.USD;

import org.unitsofmeasurement.quantity.Length;
import org.unitsofmeasurement.unit.UnitFormat;

import org.eclipse.uomo.business.money.MoneyConverter;
import org.eclipse.uomo.business.money.MoneyUnit;
import org.eclipse.uomo.business.types.IMoney;
import org.eclipse.uomo.examples.business.console.internal.DemoMessages;
import javax.measure.Quantity;
import org.eclipse.uomo.units.QuantityAmount;
import org.eclipse.uomo.units.impl.BaseAmount;
import org.eclipse.uomo.units.impl.quantity.LengthAmount;

/**
 * @author Werner Keil
 * @version 0.9.2, 2013-05-20
 */
public class TripCost {
  
	
	/**
	 * @param args
	 *            The application arguments if required.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
		// /////////////////////////////////////////////////////////////////////
		// Calculates the cost of a car trip in Europe for an American tourist.
		// /////////////////////////////////////////////////////////////////////

		@SuppressWarnings("unused")
		MoneyConverter converter = new MoneyConverter(USD, EUR, 1.4);

		// Calculates trip cost.
		BaseAmount carMileage = new BaseAmount(20,
				MILE.divide(GALLON_LIQUID)); // 20 mi/gal.
		Quantity<IMoney> gazPrice = new BaseAmount(1.2, EUR.divide(LITER));
		// // 1.2 EUR/L
		LengthAmount tripDistance = new LengthAmount(400, KILO(METRE)); // 400 km
//		Quantity<Length> tripDistance2 = new LengthAmount(400, KILO(METRE));
//		LengthAmount tripDistance = new LengthAmount(4, (Unit<Length>) LAKH(METRE)); // 400 km 
		
		
		// km
		Quantity<?> tripCost =  tripDistance.divide(
				carMileage).multiply(gazPrice); // .to(USD);

		// Display trip.
		System.out.println(DemoMessages.MoneyDemo_Car_mileage + carMileage);
		System.out.println(DemoMessages.MoneyDemo_Trip_distance + tripDistance);

		// Display cost.
		System.out.print(DemoMessages.MoneyDemo_Gas_price);
		System.out.println(gazPrice); // FIXME format for CurrencyConverter
		// UFormat format = MeasureFormat.getCurrencyFormat();
		// System.out.println(format.format(gazPrice));
		// MoneyAmount mo = MoneyAmount.valueOf(100, EUR);
		// System.out.println(currFormat.format(mo));
		System.out.println(DemoMessages.MoneyDemo_Trip_cost + tripCost); // + " (" +

		System.out.println("In USD: " + gazPrice.to(USD).getValue());
		System.out.println("Trip Cost"
				+ ((QuantityAmount) tripCost).to(USD)); //$NON-NLS-1$
		//System.out.println(DemoMessages.MoneyDemo_Trip_cost + tripCost.to(USD));
//		System.out.println(Messages.MoneyDemo_Trip_cost
//				+ ((BaseAmount) tripCost).to(EUR)); //$NON-NLS-1$
		// System.out.println("Trip cost = " + tripCost + " (" +
		// tripCost.to(GBP) + ")");
		// System.out.println("Trip cost = " + tripCost + " (" +
		// tripCost.to(INR) + ")");
	}

	/*
	 1.00 CZK=0.0640254 USD
	Czech Republic Koruny 	  	United States Dollars
	1 CZK = 0.0640254 USD 	  	1 USD = 15.6188 CZK
	 */
}
