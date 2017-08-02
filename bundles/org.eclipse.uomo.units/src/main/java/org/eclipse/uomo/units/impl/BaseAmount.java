/**
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

import static org.eclipse.uomo.core.impl.OutputHelper.println;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

import org.eclipse.uomo.units.AbstractConverter;
import org.eclipse.uomo.units.AbstractQuantity;
import org.eclipse.uomo.units.AbstractUnit;
import org.eclipse.uomo.units.impl.converter.RationalConverter;
import javax.measure.quantity.Dimensionless;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.UnitConverter;

/**
 * Represents a generic quantity amount.
 * 
 * @author <a href="mailto:uomo@catmedia.us">Werner Keil</a>
 * @version 1.5, $Date: 2017-07-30 $
 * @deprecated use BaseQuantity
 */
public class BaseAmount<Q extends Quantity<Q>> extends AbstractQuantity<Q> {
	
	private final Number value;
	
	/**
	 * @param number
	 * @param unit
	 */
	public BaseAmount(Number number, Unit<Q> unit) {
		super(unit);
		value = number;
		//measure = null; //MeasureAmount.of(number,  (MeasureUnit)unit);
		//super(number, (MeasureUnit) unit);
	}

	/**
	 * Returns the amount corresponding to the specified value and unit.
	 * 
	 * @param value
	 *            the value stated in the specified unit.
	 * @param unit
	 *            the unit in which the value is stated.
	 * @return the corresponding amount.
	 */
	public static <Q extends Quantity<Q>> BaseAmount<Q> valueOf(Number value,
			Unit<Q> unit) {
		BaseAmount<Q> amount = new BaseAmount<Q>(value, unit);
		return amount;
	}

	/**
	 * Holds a dimensionless measure of one (exact).
	 */
	protected static final BaseAmount<Dimensionless> ONE = new BaseAmount<Dimensionless>(
			BigDecimal.ONE, AbstractUnit.ONE);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BaseAmount<Q> add(Quantity<Q> that) {
		final Quantity<Q> thatToUnit = that.to(getUnit());
		return new BaseAmount(this.getValue().doubleValue()
				+ thatToUnit.getValue().doubleValue(), getUnit());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public BaseAmount<Q> subtract(Quantity<Q> that) {
		final Quantity<Q> thatToUnit = that.to(getUnit());
		return new BaseAmount(this.getValue().doubleValue()
				- thatToUnit.getValue().doubleValue(), getUnit());

	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseAmount<Q> multiply(Quantity<?> that) {
		Unit<?> unit = getUnit().multiply(that.getUnit());
		return (BaseAmount<Q>) valueOf((getValue().doubleValue() * that.getValue()
				.doubleValue()), unit);
	}
	
	@Override
	public Quantity<Q> multiply(Number that) {
		return (BaseAmount<Q>) valueOf((getValue().doubleValue() * that
				.doubleValue()), getUnit());	
	}

	@SuppressWarnings("unchecked")
	@Override
	public BaseAmount<Q> divide(Quantity<?> that) {
		Unit<?> unit = getUnit().divide(that.getUnit());
		return (BaseAmount<Q>) valueOf((getValue().doubleValue() / that.getValue()
				.doubleValue()), unit);
	}

	@Override
	public BaseAmount<Q> to(Unit<Q> unit) {
		return to(unit, MathContext.DECIMAL128);
	}

	public BaseAmount<Q> to(Unit<Q> unit, MathContext ctx) {
		if (this.getUnit().equals(unit))
			return this;
		UnitConverter cvtr = this.getUnit().getConverterTo(unit);
		if (cvtr == AbstractConverter.IDENTITY)
			return (BaseAmount<Q>) valueOf(this.getValue(), unit);
		return (BaseAmount<Q>) valueOf(convert(this.getValue(), cvtr, ctx), unit);
	}

	// Try to convert the specified value.
	private static Number convert(Number value, UnitConverter cvtr,
			MathContext ctx) {
		if (cvtr instanceof RationalConverter) { // Try converting through Field
													// methods.
			RationalConverter rCvtr = (RationalConverter) cvtr;
			BigInteger dividend = rCvtr.getDividend();
			BigInteger divisor = rCvtr.getDivisor();
			if (dividend.abs().compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0)
				throw new ArithmeticException("Multiplier overflow"); //$NON-NLS-1$
			if (divisor.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0)
				throw new ArithmeticException("Divisor overflow"); //$NON-NLS-1$
			if (value instanceof BigInteger || value instanceof Long || value instanceof Integer) {
				return (value.longValue() * dividend.longValue())
					/ (divisor.longValue());
			} else {
				return (value.doubleValue() * dividend.doubleValue())
					/ (divisor.doubleValue());
				// TODO use actual BigDecimal methods for BigDecimal
			}
		} else if (cvtr instanceof AbstractConverter.Compound
				&& cvtr.isLinear()) { // Do it in two parts.
			AbstractConverter.Compound compound = (AbstractConverter.Compound) cvtr;
			Number firstConversion = convert(value, compound.getRight(), ctx);
			Number secondConversion = convert(firstConversion,
					compound.getLeft(), ctx);
			return secondConversion;
		} else { // Try using BigDecimal as intermediate.
			BigDecimal decimalValue = BigDecimal.valueOf(value.doubleValue());
			Number newValue = cvtr.convert(decimalValue);
			return newValue;
			// if (((FieldNumber)value) instanceof Decimal)
			// return (N)((FieldNumber)Decimal.valueOf(newValue));
			// if (((FieldNumber)value) instanceof Float64)
			// return (N)((FieldNumber)Float64.valueOf(newValue.doubleValue()));
			// throw new ArithmeticException(
			// "Generic amount conversion not implemented for amount of type " +
			// value.getClass());
		}
	}

	/**
	 * Returns this measure raised at the specified exponent.
	 * 
	 * @param exp
	 *            the exponent.
	 * @return <code>this<sup>exp</sup></code>
	 */
	@SuppressWarnings("unchecked")
	public Quantity<? extends Quantity<Q>> pow(int exp) {
		if (exp < 0)
			return (Quantity<? extends Quantity<Q>>) this.pow(-exp).inverse();
		if (exp == 0)

			return (Quantity<? extends Quantity<Q>>) ONE;

		Quantity<? extends Quantity<Q>> pow2 = (Quantity<? extends Quantity<Q>>) this;
		Quantity<? extends Quantity<Q>> result = null;
		while (exp >= 1) { // Iteration.

			if ((exp & 1) == 1) {
				result = (Quantity<? extends Quantity<Q>>) ((result == null) ? pow2
						: result.multiply(pow2));

			}

			pow2 = (Quantity<? extends Quantity<Q>>) pow2.multiply(pow2);

			exp >>>= 1;

		}

		return result;

	}

	public Quantity<? extends Quantity<Q>> inverse() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final Quantity<? extends Quantity<Q>> m = new BaseAmount(getValue(),
				getUnit().inverse());
		return m;
	}

	public int compareTo(BaseAmount<Q> o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj instanceof BaseAmount<?>) {
			BaseAmount<?> ba = (BaseAmount<?>) obj;
			if (this.getUnit().getClass() == ba.getUnit().getClass()) {
				return super.equals(obj);
			}
			if (ba.getUnit() instanceof AlternateUnit<?>) {
				AlternateUnit<?> baa = (AlternateUnit<?>) ba.getUnit();
				if (this.getUnit() instanceof AlternateUnit<?>) {
					return super.equals(obj);
				} else if (this.getUnit() instanceof AnnotatedUnit<?>) {
					AnnotatedUnit<?> au = (AnnotatedUnit<?>) this.getUnit();
					println("Ann: " + au); //$NON-NLS-1$
				} else if (this.getUnit() instanceof BaseUnit<?>) {
					BaseUnit<?> bu = (BaseUnit<?>) this.getUnit();
					println("Bas: " + bu); //$NON-NLS-1$
				} else if (this.getUnit() instanceof ProductUnit<?>) {
					ProductUnit<?> pu = (ProductUnit<?>) this.getUnit();
					println("Pro: " + pu); //$NON-NLS-1$
				} else if (this.getUnit() instanceof TransformedUnit<?>) {
					TransformedUnit<?> tu = (TransformedUnit<?>) this.getUnit();
					println("Tran: " + tu); //$NON-NLS-1$
					if (tu.getParentUnit().equals(baa)) {
						return true; // FIXME use number here, too
					}
				} else {
					return super.equals(obj);
				}
			}
			if (ba.getUnit() instanceof BaseUnit<?>) {
				if (this.getUnit() instanceof AlternateUnit<?>) {
					AlternateUnit<?> au = (AlternateUnit<?>) this.getUnit();
					println("Alt: " + au); //$NON-NLS-1$
				} else if (this.getUnit() instanceof AnnotatedUnit<?>) {
					AnnotatedUnit<?> au = (AnnotatedUnit<?>) this.getUnit();
					println("Ann: " + au); //$NON-NLS-1$
				} else if (this.getUnit() instanceof BaseUnit<?>) {
					return super.equals(obj);
				} else if (this.getUnit() instanceof ProductUnit<?>) {
					ProductUnit<?> pu = (ProductUnit<?>) this.getUnit();
					println("Pro: " + pu); //$NON-NLS-1$
				} else if (this.getUnit() instanceof TransformedUnit<?>) {
					TransformedUnit<?> tu = (TransformedUnit<?>) this.getUnit();
					println("Tran: " + tu); //$NON-NLS-1$
				} else {
					return super.equals(obj);
				}
			}
			if (ba.getUnit() instanceof TransformedUnit<?>) {
				TransformedUnit<?> bat = (TransformedUnit<?>) ba.getUnit();
				if (this.getUnit() instanceof AlternateUnit<?>) {
					AlternateUnit<?> au = (AlternateUnit<?>) this.getUnit();
					println("Alt: " + au); //$NON-NLS-1$
					if (bat.getParentUnit().equals(au)) {
						return true;
					}
				} else if (this.getUnit() instanceof AnnotatedUnit<?>) {
					AnnotatedUnit<?> au = (AnnotatedUnit<?>) this.getUnit();
					System.out.println("Ann: " + au); //$NON-NLS-1$
				} else if (this.getUnit() instanceof BaseUnit<?>) {
					BaseUnit<?> bu = (BaseUnit<?>) this.getUnit();
					println("Bas: " + bu); //$NON-NLS-1$
				} else if (this.getUnit() instanceof ProductUnit<?>) {
					ProductUnit<?> pu = (ProductUnit<?>) this.getUnit();
					println("Pro: " + pu); //$NON-NLS-1$
				} else if (this.getUnit() instanceof TransformedUnit<?>) {
					return super.equals(obj);
				} else {
					return super.equals(obj);
				}
			}
		}
		return super.equals(obj);
	}

	@Override
	public Number getValue() {
		return value;
	}

	public boolean isBig() {
		return value instanceof BigDecimal;
	}

	public BigDecimal decimalValue(Unit<Q> unit, MathContext ctx)
			throws ArithmeticException {
		   BigDecimal decimal = (getValue() instanceof BigDecimal) ? (BigDecimal)getValue() : BigDecimal.valueOf(getValue().doubleValue()); // TODO check value if it is a BD, otherwise use different converter
           return (getUnit().equals(unit)) ? decimal : ((AbstractConverter)getUnit().getConverterTo(unit)).convert(decimal, ctx);

	}

	@Override
	public double doubleValue(Unit<Q> unit) throws ArithmeticException {
		return (getUnit().equals(unit)) ? getValue().doubleValue() : getUnit().getConverterTo(unit).convert(getValue().doubleValue());
	}

	@Override
	public Quantity<Q> divide(Number arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
