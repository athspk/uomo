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
package org.eclipse.uomo.units.impl;

import static org.eclipse.uomo.units.impl.system.Units.*;

import java.util.HashMap;
import java.util.Map;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.*;
import javax.measure.spi.QuantityFactory;

import org.eclipse.uomo.units.AbstractUnit;

/**
 * The default factory implementation. This factory provides a default implementation for every {@link AbstractQuantity} sub-type.
 *
 * For example:<br>
 * <code>
 *      Quantity&lt;Mass&gt; m = DefaultQuantityFactory.getInstance(Mass.class).create(23.0, KILOGRAM); // 23.0 kg<br>
 *      Quantity&lt;Time&gt; m = DefaultQuantityFactory.getInstance(Time.class).create(124, MILLI(SECOND)); // 124 ms
 * </code>
 * 
 * @param <Q>
 *          The type of the quantity.
 *
 * @author <a href="mailto:martin.desruisseaux@geomatys.com">Martin Desruisseaux</a>
 * @author <a href="mailto:units@catmedia.us">Werner Keil</a>
 * @author <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @version 1.0, $Date: 2016-10-04 $
 * @since 1.0
 */
public final class QuantityFactoryImpl<Q extends Quantity<Q>> extends AbstractQuantityFactory<Q> {
  /**
   * The type of the quantities created by this factory.
   */
  @SuppressWarnings("unused")
  private final Class<Q> type;

  /**
   * The metric unit for quantities created by this factory.
   */
  private final Unit<Q> metricUnit;

  /**
   * Creates a new factory for quantities of the given type.
   *
   * @param type
   *          The type of the quantities created by this factory.
   */
  @SuppressWarnings("unchecked")
  QuantityFactoryImpl(final Class<Q> type) {
    this.type = type;
    metricUnit = CLASS_TO_METRIC_UNIT.get(type);
  }

  @SuppressWarnings("rawtypes")
  static final Map<Class, Unit> CLASS_TO_METRIC_UNIT = new HashMap<Class, Unit>();
  static {
    CLASS_TO_METRIC_UNIT.put(Dimensionless.class, AbstractUnit.ONE);
    CLASS_TO_METRIC_UNIT.put(ElectricCurrent.class, AMPERE);
    CLASS_TO_METRIC_UNIT.put(LuminousIntensity.class, CANDELA);
    CLASS_TO_METRIC_UNIT.put(Temperature.class, KELVIN);
    CLASS_TO_METRIC_UNIT.put(Mass.class, KILOGRAM);
    CLASS_TO_METRIC_UNIT.put(Length.class, METRE);
    CLASS_TO_METRIC_UNIT.put(AmountOfSubstance.class, MOLE);
    CLASS_TO_METRIC_UNIT.put(Time.class, SECOND);
    CLASS_TO_METRIC_UNIT.put(Angle.class, RADIAN);
    CLASS_TO_METRIC_UNIT.put(SolidAngle.class, STERADIAN);
    CLASS_TO_METRIC_UNIT.put(Frequency.class, HERTZ);
    CLASS_TO_METRIC_UNIT.put(Force.class, NEWTON);
    CLASS_TO_METRIC_UNIT.put(Pressure.class, PASCAL);
    CLASS_TO_METRIC_UNIT.put(Energy.class, JOULE);
    CLASS_TO_METRIC_UNIT.put(Power.class, WATT);
    CLASS_TO_METRIC_UNIT.put(ElectricCharge.class, COULOMB);
    CLASS_TO_METRIC_UNIT.put(ElectricPotential.class, VOLT);
    CLASS_TO_METRIC_UNIT.put(ElectricCapacitance.class, FARAD);
    CLASS_TO_METRIC_UNIT.put(ElectricResistance.class, OHM);
    CLASS_TO_METRIC_UNIT.put(ElectricConductance.class, SIEMENS);
    CLASS_TO_METRIC_UNIT.put(MagneticFlux.class, WEBER);
    CLASS_TO_METRIC_UNIT.put(MagneticFluxDensity.class, TESLA);
    CLASS_TO_METRIC_UNIT.put(ElectricInductance.class, HENRY);
    CLASS_TO_METRIC_UNIT.put(LuminousFlux.class, LUMEN);
    CLASS_TO_METRIC_UNIT.put(Illuminance.class, LUX);
    CLASS_TO_METRIC_UNIT.put(Radioactivity.class, BECQUEREL);
    CLASS_TO_METRIC_UNIT.put(RadiationDoseAbsorbed.class, GRAY);
    CLASS_TO_METRIC_UNIT.put(RadiationDoseEffective.class, SIEVERT);
    CLASS_TO_METRIC_UNIT.put(CatalyticActivity.class, KATAL);
    CLASS_TO_METRIC_UNIT.put(Speed.class, METRE_PER_SECOND);
    CLASS_TO_METRIC_UNIT.put(Acceleration.class, METRE_PER_SQUARE_SECOND);
    CLASS_TO_METRIC_UNIT.put(Area.class, SQUARE_METRE);
    CLASS_TO_METRIC_UNIT.put(Volume.class, CUBIC_METRE);
  }

  public Quantity<Q> create(Number value, Unit<Q> unit) {
    return Quantities.getQuantity(value, unit);
  }

  public Unit<Q> getSystemUnit() {
    return metricUnit;
  }

  /**
   * Returns the default instance for the specified quantity type.
   *
   * @param <Q>
   *          The type of the quantity
   * @param type
   *          the quantity type
   * @return the quantity factory for the specified type
   */
  @SuppressWarnings("unchecked")
  public static <Q extends Quantity<Q>> QuantityFactory<Q> getInstance(final Class<Q> type) {
    logger.log(LOG_LEVEL, "Type: " + type + ": " + type.isInterface());
    QuantityFactory<Q> factory;
    if (!type.isInterface()) {
      factory = new QuantityFactoryImpl<Q>(type);
      // TODO use instances?
    } else {
      factory = INSTANCES.get(type);
      if (factory != null)
        return factory;
      if (!Quantity.class.isAssignableFrom(type))
        // This exception is not documented because it should never
        // happen if the
        // user don't try to trick the Java generic types system with
        // unsafe cast.
        throw new ClassCastException();
      factory = new QuantityFactoryImpl<Q>(type);
      setInstance(type, factory);
    }
    return factory;
  }
}