/**
 * Copyright (c) 2005, 2012, Werner Keil, JScience and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Werner Keil - initial API and implementation
 */
package org.eclipse.uomo.units;

import org.unitsofmeasurement.quantity.Quantity;
import org.unitsofmeasurement.unit.Unit;

/**
 * <p> This interface represents the IMeasure, countable, or comparable 
 *     property or aspect of a thing.</p>
 *     
 * <p> Implementing instances are typically the result of a measurement:[code]
 *         IMeasure<Mass> weight = BaseAmount.valueOf(180.0, POUND);
 *     [/code]
 *     They can also be created from custom classes:[code]
 *     class Delay implements IMeasure<Duration> {
 *          private long nanoSeconds; // Implicit internal unit.
 *          public double doubleValue(Unit<Velocity> unit) { ... }
 *          public long longValue(Unit<Velocity> unit) { ... }
 *     }
 *     Thread.wait(new Delay(24, HOUR)); // Assuming Thread.wait(IMeasure<Duration>) method.
 *     [/code]</p>
 *     
 * <p> Although IMeasure instances are for the most part scalar quantities; 
 *     more complex implementations (e.g. vectors, data set) are allowed as 
 *     long as an aggregate magnitude can be determined. For example:[code]
 *     class Velocity3D implements IMeasure<Velocity> {
 *          private double x, y, z; // Meter per seconds.
 *          public double doubleValue(Unit<Velocity> unit) { ... } // Returns vector norm.
 *          ... 
 *     }
 *     class Sensors<Q extends Quantity> extends QuantityAmount<double[], Q> {
 *          public doubleValue(Unit<Q> unit) { ... } // Returns median value. 
 *          ...
 *     } [/code]</p>
 * 
 * @author  <a href="mailto:jean-marie@dautelle.com">Jean-Marie Dautelle</a>
 * @author <a href="mailto:uomo@catmedia.us">Werner Keil</a>
 * @version 0.4.2, $Date: 2014-08-03 $
 * @param <Q>
 */
public interface IMeasure<Q extends Quantity<Q>> extends Quantity<Q> {    
        
    /**
     * Returns the sum of this amount with the one specified.
     *
     * @param  that the amount to be added.
     * @return <code>this + that</code>.
     */
    IMeasure<Q> add(IMeasure<Q> that);
    
    /**
     * Returns the difference between this amount and the one specified.
     *
     * @param  that the number to be subtracted.
     * @return <code>this - that</code>.
     */
    IMeasure<Q> subtract(IMeasure<Q> that);
    
    /**
     * Returns the product of this amount with the one specified.
     *
     * @param  that the amount multiplier.
     * @return <code>this · that</code>.
     */
    IMeasure<?> multiply(IMeasure<?> that);
    
    /**
     * Returns the product of this amount with the number specified.
     *
     * @param  that the number multiplier.
     * @return <code>this · that</code>.
     */
    IMeasure<?> multiply(Number that);
    
    /**
     * Returns this amount divided by the one specified.
     *
     * @param  that the amount divisor.
     * @return <code>this / that</code>.
     */
    IMeasure<?> divide(IMeasure<?> that);
    
    IMeasure<? extends IMeasure<Q>> inverse();
    
    /**
     * Returns this measurement converted into another unit.
     * 
     * @param unit
     * @return the converted result.
     */
    IMeasure<Q> to(Unit<Q> unit);
    
    /**
     * Returns the value of this quantity as <code>double</code> stated
     * in the specified unit. This method is recommended over <code>
     * q.getUnit().getConverterTo(unit).convert(q.getValue()).doubleValue()</code>
     *
     * @param unit the unit in which the returned value is stated.
     * @return the value of this quantity when stated in the specified unit.
     */
    public double doubleValue(Unit<Q> unit);
    
    /**
     * Returns the value of this quantity as <code>long</code> stated
     * in the specified unit. This method is recommended over <code>
     * q.getUnit().getConverterTo(unit).convert(q.getValue()).longValue()</code>
     *
     * @param unit the unit in which the returned value is stated.
     * @return the value of this quantity when stated in the specified unit.
     */
    public long longValue(Unit<Q> unit);
}
