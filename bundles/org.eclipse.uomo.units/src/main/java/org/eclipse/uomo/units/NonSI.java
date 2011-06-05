/**
 * Copyright (c) 2005, 2011, Werner Keil, JScience and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Werner Keil, Jean-Marie Dautelle - initial API and implementation
 */
package org.eclipse.uomo.units;

import static org.eclipse.uomo.units.SI.AMPERE;
import static org.eclipse.uomo.units.SI.BECQUEREL;
import static org.eclipse.uomo.units.SI.BIT;
import static org.eclipse.uomo.units.SI.COULOMB;
import static org.eclipse.uomo.units.SI.CUBIC_METRE;
import static org.eclipse.uomo.units.SI.GRAM;
import static org.eclipse.uomo.units.SI.GRAY;
import static org.eclipse.uomo.units.SI.JOULE;
import static org.eclipse.uomo.units.SI.KELVIN;
import static org.eclipse.uomo.units.SI.KILOGRAM;
import static org.eclipse.uomo.units.SI.LUX;
import static org.eclipse.uomo.units.SI.METRE;
import static org.eclipse.uomo.units.SI.METRES_PER_SECOND;
import static org.eclipse.uomo.units.SI.METRES_PER_SQUARE_SECOND;
import static org.eclipse.uomo.units.SI.MOLE;
import static org.eclipse.uomo.units.SI.NEWTON;
import static org.eclipse.uomo.units.SI.PASCAL;
import static org.eclipse.uomo.units.SI.RADIAN;
import static org.eclipse.uomo.units.SI.SECOND;
import static org.eclipse.uomo.units.SI.SIEVERT;
import static org.eclipse.uomo.units.SI.SQUARE_METRE;
import static org.eclipse.uomo.units.SI.STERADIAN;
import static org.eclipse.uomo.units.SI.TESLA;
import static org.eclipse.uomo.units.SI.WATT;
import static org.eclipse.uomo.units.SI.WEBER;
import static org.eclipse.uomo.units.SI.Prefix.*;
import static org.eclipse.uomo.units.USCustomary.INCH;
import static org.eclipse.uomo.units.USCustomary.REVOLUTION;

import java.math.BigInteger;
import org.eclipse.uomo.core.IName;
import org.eclipse.uomo.units.impl.LogConverter;
import org.eclipse.uomo.units.impl.RationalConverter;
import org.unitsofmeasurement.quantity.*;
import org.unitsofmeasurement.unit.Unit;

/**
 * <p>
 * This class contains units that are not part of the International System of
 * Units, that is, they are outside the SI, but are important and widely used.
 * </p>
 * 
 * <p>
 * This is an internal collection of otherwise unassigned units used by
 * <b>UCUM</b> or similar systems.
 * </p>
 * <p>
 * This class is not intended to be implemented by clients.
 * </p>
 * 
 * @noimplement This class is not intended to be implemented by clients.
 * @noextend This class is not intended to be extended by clients.
 * 
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @author <a href="mailto:units@catmedia.us">Werner Keil</a>
 * @version 1.16 ($Revision: 231 $), $Date: 2011-04-05 02:57:59 +0200 (Mo, 06 Sep
 *          2011) $
 *          TODO expose relevant units like ROENTGEN, etc. via other SOU?
 */
final class NonSI extends AbstractSystemOfUnits implements IName {

	/**
	 * Holds the standard gravity constant: 9.80665 m/sÂ² exact.
	 */
	private static final int STANDARD_GRAVITY_DIVIDEND = 980665;

	private static final int STANDARD_GRAVITY_DIVISOR = 100000;

	/**
	 * Holds the avoirdupois pound: 0.45359237 kg exact
	 */
	private static final int AVOIRDUPOIS_POUND_DIVIDEND = 45359237;

	private static final int AVOIRDUPOIS_POUND_DIVISOR = 100000000;

	/**
	 * Holds the Avogadro constant.
	 */
	private static final double AVOGADRO_CONSTANT = 6.02214199e23; // (1/mol).

	/**
	 * Holds the electric charge of one electron.
	 */
	private static final double ELEMENTARY_CHARGE = 1.602176462e-19; // (C).

	/**
	 * Default constructor (prevents this class from being instantiated).
	 */
	private NonSI() {
	}

	/**
	 * Returns the unique instance of this class.
	 * 
	 * @return the NonSI instance.
	 */
	static NonSI getInstance() {
		return INSTANCE;
	}

	private static final NonSI INSTANCE = new NonSI();

	// /////////////////
	// Dimensionless //
	// /////////////////
	/**
	 * A dimensionless unit equals to <code>pi</code> (standard name
	 * <code>Ï€</code>).
	 */
	static final Unit<Dimensionless> PI = addUnit(AbstractUnit.ONE
			.multiply(StrictMath.PI));

	/**
	 * A dimensionless unit equals to <code>0.01</code> (standard name
	 * <code>%</code>).
	 */
	static final Unit<Dimensionless> PERCENT = addUnit(AbstractUnit.ONE
			.divide(100));

	/**
	 * A logarithmic unit used to describe a ratio (standard name
	 * <code>dB</code>).
	 */
	static final Unit<Dimensionless> DECIBEL = addUnit(AbstractUnit.ONE
			.transform(new LogConverter(10).inverse().concatenate(
					new RationalConverter(BigInteger.ONE, BigInteger.TEN))));

	// ///////////////////////
	// Amount of substance //
	// ///////////////////////
	/**
	 * A unit of amount of substance equals to one atom (standard name
	 * <code>atom</code>).
	 */
	static final Unit<AmountOfSubstance> ATOM = addUnit(MOLE
			.divide(AVOGADRO_CONSTANT));

	// //////////
	// Length //
	// //////////

	/**
	 * A unit of length equal to <code>1E-10 m</code> (standard name
	 * <code>\u00C5ngstr\u00F6m</code>).
	 */
	static final Unit<Length> ANGSTROM = addUnit(METRE
			.divide(10000000000L));

	/**
	 * A unit of length equal to the average distance from the center of the
	 * Earth to the center of the Sun (standard name <code>ua</code>).
	 */
	static final Unit<Length> ASTRONOMICAL_UNIT = addUnit(METRE
			.multiply(149597870691.0));

	/**
	 * A unit of length equal to the distance that light travels in one year
	 * through a vacuum (standard name <code>ly</code>).
	 */
	static final Unit<Length> LIGHT_YEAR = addUnit(METRE
			.multiply(9.460528405e15));

	/**
	 * A unit of length equal to the distance at which a star would appear to
	 * shift its position by one arcsecond over the course the time (about 3
	 * months) in which the Earth moves a distance of {@link #ASTRONOMICAL_UNIT}
	 * in the direction perpendicular to the direction to the star (standard
	 * name <code>pc</code>).
	 */
	static final Unit<Length> PARSEC = addUnit(METRE
			.multiply(30856770e9));

	/**
	 * A unit of length equal to <code>0.013837 {@link #INCH}</code> exactly
	 * (standard name <code>pt</code>).
	 * 
	 * @see #PIXEL
	 */
	static final Unit<Length> POINT = addUnit(INCH.multiply(13837)
			.divide(1000000));

	/**
	 * A unit of length equal to <code>1/72 {@link #INCH}</code> (standard name
	 * <code>pixel</code>). It is the American point rounded to an even 1/72
	 * inch.
	 * 
	 * @see #POINT
	 */
	static final Unit<Length> PIXEL = addUnit(INCH.divide(72));

	/**
	 * Equivalent {@link #PIXEL}
	 */
	static final Unit<Length> COMPUTER_POINT = PIXEL;

	// ////////////
	// Duration //
	// ////////////
	/**
	 * A unit of duration equal to <code>60 s</code> (standard name
	 * <code>min</code>).
	 */
	static final Unit<Time> MINUTE = addUnit(SECOND.multiply(60));

	/**
	 * A unit of duration equal to <code>60 {@link #MINUTE}</code> (standard
	 * name <code>h</code>).
	 */
	static final Unit<Time> HOUR = addUnit(MINUTE.multiply(60));

	/**
	 * A unit of duration equal to <code>24 {@link #HOUR}</code> (standard name
	 * <code>d</code>).
	 */
	static final Unit<Time> DAY = addUnit(HOUR.multiply(24));

	/**
	 * A unit of duration equal to the time required for a complete rotation of
	 * the earth in reference to any star or to the vernal equinox at the
	 * meridian, equal to 23 hours, 56 minutes, 4.09 seconds (standard name
	 * <code>day_sidereal</code>).
	 */
	static final Unit<Time> DAY_SIDEREAL = addUnit(SECOND
			.multiply(86164.09));

	/**
	 * A unit of duration equal to 7 {@link #DAY} (standard name
	 * <code>week</code>).
	 */
	static final Unit<Time> WEEK = addUnit(DAY.multiply(7));

	/**
	 * A unit of duration equal to 365 {@link #DAY} (standard name
	 * <code>year</code>).
	 */
	static final Unit<Time> YEAR_CALENDAR = addUnit(DAY.multiply(365));

	/**
	 * A unit of duration equal to one complete revolution of the earth about
	 * the sun, relative to the fixed stars, or 365 days, 6 hours, 9 minutes,
	 * 9.54 seconds (standard name <code>year_sidereal</code>).
	 */
	static final Unit<Time> YEAR_SIDEREAL = addUnit(SECOND
			.multiply(31558149.54));

	/**
	 * The Julian year, as used in astronomy and other sciences, is a time unit
	 * defined as exactly 365.25 days. This is the normal meaning of the unit
	 * "year" (symbol "a" from the Latin annus, annata) used in various
	 * scientific contexts.
	 */
	static final Unit<Time> YEAR_JULIEN = addUnit(SECOND
			.multiply(31557600));

	// ////////
	// Mass //
	// ////////
	/**
	 * A unit of mass equal to 1/12 the mass of the carbon-12 atom (standard
	 * name <code>u</code>).
	 */
	static final Unit<Mass> ATOMIC_MASS = addUnit(KILOGRAM
			.multiply(1e-3 / AVOGADRO_CONSTANT));

	/**
	 * A unit of mass equal to the mass of the electron (standard name
	 * <code>me</code>).
	 */
	static final Unit<Mass> ELECTRON_MASS = addUnit(KILOGRAM
			.multiply(9.10938188e-31));

	/**
	 * A unit of mass equal to <code>453.59237 grams</code> (avoirdupois pound,
	 * standard name <code>lb</code>).
	 */
	static final Unit<Mass> POUND = addUnit(KILOGRAM.multiply(
			AVOIRDUPOIS_POUND_DIVIDEND).divide(AVOIRDUPOIS_POUND_DIVISOR));

	/**
	 * A unit of mass equal to <code>2240 {@link #POUND}</code> (long ton,
	 * standard name <code>ton_uk</code>).
	 */
	static final Unit<Mass> TON_UK = addUnit(POUND.multiply(2240));

	/**
	 * A unit of mass equal to <code>1000 kg</code> (metric ton, standard name
	 * <code>t</code>).
	 */
	static final Unit<Mass> METRIC_TON = addUnit(KILOGRAM.multiply(1000));

	// ///////////////////
	// Electric charge //
	// ///////////////////
	/**
	 * A unit of electric charge equal to the charge on one electron (standard
	 * name <code>e</code>).
	 */
	static final Unit<ElectricCharge> E = addUnit(COULOMB
			.multiply(ELEMENTARY_CHARGE));

	/**
	 * A unit of electric charge equal to equal to the product of Avogadro's
	 * number (see {@link SI#MOLE}) and the charge (1 e) on a single electron
	 * (standard name <code>Fd</code>).
	 */
	static final Unit<ElectricCharge> FARADAY = addUnit(COULOMB
			.multiply(ELEMENTARY_CHARGE * AVOGADRO_CONSTANT)); // e/mol

	/**
	 * A unit of electric charge which exerts a force of one dyne on an equal
	 * charge at a distance of one centimeter (standard name <code>Fr</code>).
	 */
	static final Unit<ElectricCharge> FRANKLIN = addUnit(COULOMB
			.multiply(3.3356e-10));

	// ///////////////
	// Temperature //
	// ///////////////
	/**
	 * A unit of temperature equal to <code>5/9 Â°K</code> (standard name
	 * <code>Â°R</code>).
	 */
	static final Unit<Temperature> RANKINE = addUnit(KELVIN.multiply(5)
			.divide(9));

	/**
	 * A unit of angle equal to <code>0.01 {@link SI#RADIAN}</code> (standard
	 * name <code>centiradian</code>).
	 */
	static final Unit<Angle> CENTIRADIAN = addUnit(RADIAN.divide(100));

	/**
	 * A unit of angle measure equal to <code>1/400 {@link #REVOLUTION}</code>
	 * (standard name <code>grade</code> ).
	 */
	static final Unit<Angle> GRADE = addUnit(REVOLUTION.divide(400));

	// ////////////
	// Velocity //
	// ////////////
	/**
	 * A unit of velocity relative to the speed of light (standard name
	 * <code>c</code>).
	 */
	static final Unit<Velocity> C = addUnit(METRES_PER_SECOND
			.multiply(299792458));

	// ////////////////
	// Acceleration //
	// ////////////////
	/**
	 * A unit of acceleration equal to the gravity at the earth's surface
	 * (standard name <code>grav</code>).
	 */
	static final Unit<Acceleration> G = addUnit(METRES_PER_SQUARE_SECOND
			.multiply(STANDARD_GRAVITY_DIVIDEND).divide(
					STANDARD_GRAVITY_DIVISOR));

	// ////////
	// Area //
	// ////////
	/**
	 * A unit of area equal to <code>100 m²</code> (standard name <code>a</code>
	 * ).
	 */
	static final Unit<Area> ARE = addUnit(SQUARE_METRE.multiply(100));

	/**
	 * A unit of area equal to <code>100 {@link #ARE}</code> (standard name
	 * <code>ha</code>).
	 */
	static final Unit<Area> HECTARE = addUnit(ARE.multiply(100)); // Exact.

	// ///////////////
	// Data Amount //
	// ///////////////
	/**
	 * A unit of data amount equal to <code>8 {@link SI#BIT}</code> (BinarY
	 * TErm, standard name <code>byte</code>).
	 */
	static final Unit<Information> BYTE = addUnit(BIT.multiply(8));

	/**
	 * Equivalent {@link #BYTE}
	 */
	static final Unit<Information> OCTET = BYTE;

	// ////////////////////
	// Electric current //
	// ////////////////////
	/**
	 * A unit of electric charge equal to the centimeter-gram-second
	 * electromagnetic unit of magnetomotive force, equal to <code>10/4
	 * &pi;ampere-turn</code> (standard name <code>Gi</code>).
	 */
	static final Unit<ElectricCurrent> GILBERT = addUnit(AMPERE
			.multiply(10).divide(4).multiply(PI).asType(ElectricCurrent.class));

	// //////////
	// Energy //
	// //////////
	/**
	 * A unit of energy equal to <code>1E-7 J</code> (standard name
	 * <code>erg</code>).
	 */
	static final Unit<Energy> ERG = addUnit(JOULE.divide(10000000));

	/**
	 * A unit of energy equal to one electron-volt (standard name
	 * <code>eV</code>, also recognized <code>keV, MeV, GeV</code>).
	 */
	static final Unit<Energy> ELECTRON_VOLT = addUnit(JOULE
			.multiply(ELEMENTARY_CHARGE));

	// ///////////////
	// Illuminance //
	// ///////////////
	/**
	 * A unit of illuminance equal to <code>1E4 Lx</code> (standard name
	 * <code>La</code>).
	 */
	static final Unit<Illuminance> LAMBERT = addUnit(LUX.multiply(10000));

	// /////////////////
	// Magnetic Flux //
	// /////////////////
	/**
	 * A unit of magnetic flux equal <code>1E-8 Wb</code> (standard name
	 * <code>Mx</code>).
	 */
	static final Unit<MagneticFlux> MAXWELL = addUnit(WEBER
			.divide(100000000));

	// /////////////////////////
	// Magnetic Flux Density //
	// /////////////////////////
	/**
	 * A unit of magnetic flux density equal <code>1000 A/m</code> (standard
	 * name <code>G</code>).
	 */
	static final Unit<MagneticFluxDensity> GAUSS = addUnit(TESLA
			.divide(10000));

	// /////////
	// Force //
	// /////////
	/**
	 * A unit of force equal to <code>1E-5 N</code> (standard name
	 * <code>dyn</code>).
	 */
	static final Unit<Force> DYNE = addUnit(NEWTON.divide(100000));

	/**
	 * A unit of force equal to <code>9.80665 N</code> (standard name
	 * <code>kgf</code>).
	 */
	static final Unit<Force> KILOGRAM_FORCE = addUnit(NEWTON.multiply(
			STANDARD_GRAVITY_DIVIDEND).divide(STANDARD_GRAVITY_DIVISOR));

	/**
	 * A unit of force equal to <code>{@link #POUND}Â·{@link #G}</code>
	 * (standard name <code>lbf</code>).
	 */
	static final Unit<Force> POUND_FORCE = addUnit(NEWTON.multiply(
			1L * AVOIRDUPOIS_POUND_DIVIDEND * STANDARD_GRAVITY_DIVIDEND)
			.divide(1L * AVOIRDUPOIS_POUND_DIVISOR * STANDARD_GRAVITY_DIVISOR));

	// /////////
	// Power //
	// /////////
	/**
	 * A unit of power equal to the power required to raise a mass of 75
	 * kilograms at a velocity of 1 meter per second (metric, standard name
	 * <code>hp</code>).
	 */
	static final Unit<Power> HORSEPOWER = addUnit(WATT.multiply(735.499));

	// ////////////
	// Pressure //
	// ////////////
	/**
	 * A unit of pressure equal to the average pressure of the Earth's
	 * atmosphere at sea level (standard name <code>atm</code>).
	 */
	static final Unit<Pressure> ATMOSPHERE = addUnit(PASCAL
			.multiply(101325));

	/**
	 * A unit of pressure equal to <code>100 kPa</code> (standard name
	 * <code>bar</code>).
	 */
	static final Unit<Pressure> BAR = addUnit(PASCAL.multiply(100000));

	/**
	 * A unit of pressure equal to the pressure exerted at the Earth's surface
	 * by a column of mercury 1 millimeter high (standard name <code>mmHg</code>
	 * ).
	 */
	static final Unit<Pressure> MILLIMETRE_OF_MERCURY = addUnit(PASCAL
			.multiply(133.322));

	/**
	 * A unit of pressure equal to the pressure exerted at the Earth's surface
	 * by a column of mercury 1 inch high (standard name <code>inHg</code>).
	 */
	static final Unit<Pressure> INCH_OF_MERCURY = addUnit(PASCAL
			.multiply(3386.388));

	// ///////////////////////////
	// Radiation dose absorbed //
	// ///////////////////////////
	/**
	 * A unit of radiation dose absorbed equal to a dose of 0.01 joule of energy
	 * per kilogram of mass (J/kg) (standard name <code>rd</code>).
	 */
	static final Unit<RadiationDoseAbsorbed> RAD = addUnit(GRAY
			.divide(100));

	/**
	 * A unit of radiation dose effective equal to <code>0.01 Sv</code>
	 * (standard name <code>rem</code>).
	 */
	static final Unit<RadiationDoseEffective> REM = addUnit(SIEVERT
			.divide(100));

	// ////////////////////////
	// Radioactive activity //
	// ////////////////////////
	/**
	 * A unit of radioctive activity equal to the activity of a gram of radium
	 * (standard name <code>Ci</code>).
	 */
	static final Unit<RadioactiveActivity> CURIE = addUnit(BECQUEREL
			.multiply(37000000000L));

	/**
	 * A unit of radioctive activity equal to 1 million radioactive
	 * disintegrations per second (standard name <code>Rd</code>).
	 */
	static final Unit<RadioactiveActivity> RUTHERFORD = addUnit(BECQUEREL
			.multiply(1000000));

	// ///////////////
	// Solid angle //
	// ///////////////
	/**
	 * A unit of solid angle equal to <code>4 <i>&pi;</i> steradians</code>
	 * (standard name <code>sphere</code>).
	 */
	static final Unit<SolidAngle> SPHERE = addUnit(STERADIAN.multiply(4)
			.multiply(PI).asType(SolidAngle.class));

	// //////////
	// Volume //
	// //////////
	/**
	 * A unit of volume equal to one cubic decimeter (default label
	 * <code>L</code>, also recognized <code>ÂµL, mL, cL, dL</code>).
	 */
	static final Unit<Volume> LITRE = addUnit(CUBIC_METRE.divide(1000));

	/**
	 * A unit of volume equal to <code>4.546 09 {@link #LITRE}</code> (standard
	 * name <code>gal_uk</code>).
	 */
	static final Unit<Volume> GALLON_UK = addUnit(LITRE.multiply(454609)
			.divide(100000));

	/**
	 * A unit of volume equal to <code>1 / 160 {@link #GALLON_UK}</code>
	 * (standard name <code>oz_fl_uk</code>).
	 */
	static final Unit<Volume> OUNCE_LIQUID_UK = addUnit(GALLON_UK
			.divide(160));

	// /////////////
	// Viscosity //
	// /////////////
	/**
	 * A unit of dynamic viscosity equal to <code>1 g/(cmÂ·s)</code> (cgs unit).
	 */
	static final Unit<DynamicViscosity> POISE = addUnit(
			GRAM.divide(CENTI(METRE).multiply(SECOND))).asType(
			DynamicViscosity.class);

	/**
	 * A unit of kinematic viscosity equal to <code>1 cm²/s</code> (cgs unit).
	 */
	static final Unit<KinematicViscosity> STOKE = addUnit(
			CENTI(METRE).pow(2).divide(SECOND))
			.asType(KinematicViscosity.class);

	// /////////////
	// Frequency //
	// /////////////
	/**
	 * A unit used to measure the frequency (rate) at which an imaging device
	 * produces unique consecutive images (standard name <code>fps</code>).
	 */
	static final Unit<Frequency> FRAMES_PER_SECOND = addUnit(
			AbstractUnit.ONE.divide(SECOND)).asType(Frequency.class);

	// //////////
	// Others //
	// //////////
	/**
	 * A unit used to measure the ionizing ability of radiation (standard name
	 * <code>Roentgen</code>).
	 */
	static final Unit<IonizingRadiation> ROENTGEN = SI.ROENTGEN;

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public <Q extends Quantity<Q>> Unit<Q> getUnit(Class<Q> quantityType) {
		// TODO Auto-generated method stub
		return null;
	}
}
