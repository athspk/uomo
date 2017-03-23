/*******************************************************************************
 * Copyright (c) 2010, 2014 Werner Keil, Creative Arts & Technologies. 
 * Crown Copyright (c) 2006, 2008, Copyright (c) 2006, 2008 Kestral Computing P/L.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Kestral Computing P/L - initial implementation
 *    Werner Keil - fixes and improvements
 *******************************************************************************/

package org.eclipse.uomo.ucum.impl;

import static org.eclipse.uomo.core.impl.OutputHelper.println;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.uomo.core.UOMoRuntimeException;
import org.eclipse.uomo.core.impl.CodeValuePair;
import org.eclipse.uomo.ucum.UcumService;
import org.eclipse.uomo.ucum.expression.Symbol;
import org.eclipse.uomo.ucum.expression.Term;
import org.eclipse.uomo.ucum.model.BaseUnit;
import org.eclipse.uomo.ucum.model.Concept;
import org.eclipse.uomo.ucum.model.ConceptKind;
import org.eclipse.uomo.ucum.model.DefinedUnit;
import org.eclipse.uomo.ucum.model.UcumModel;
import org.eclipse.uomo.ucum.parsers.DefinitionParser;
import org.eclipse.uomo.ucum.parsers.ExpressionComposer;
import org.eclipse.uomo.ucum.parsers.ExpressionParser;
import org.eclipse.uomo.ucum.parsers.FormalStructureComposer;
import org.eclipse.uomo.ucum.special.SpecialUnitHandler;
import org.eclipse.uomo.ucum.special.SpecialUnitRegistry;
import org.eclipse.uomo.util.Registry;

/**
 * implements UCUM services. Applications must provide a copy of
 * ucum-essence.xml as either a stream or a file name to create the services.
 * 
 * the provided ucum-essence.xml must be released on 25 Apr 2008 or more recent.
 * Note that if the ucum-essence.xml file does not contain a release date on an
 * attribute of the root element, it is not more recent than this date (Gunther
 * added it on this date for this usage, thanks)
 * 
 * @author Grahame Grieve
 * @author Werner Keil
 */
public class UcumEssenceService implements UcumService {

	/**
	 * UCUM ID
	 */
	public static final String UCUM_OID = "2.16.840.1.113883.6.8";

	private final UcumModel model;
	@SuppressWarnings("rawtypes")
	private final Registry<SpecialUnitHandler> handlers = new SpecialUnitRegistry();

	/**
	 * Create an instance of Ucum services. Stream must point to a valid
	 * ucum-essence file (see class documentation)
	 */
	public UcumEssenceService(InputStream stream) throws UOMoRuntimeException {
		super();
		assert stream != null : paramError("factory", "stream",
				"must not be null");
		try {
			model = new DefinitionParser().parse(stream);
		} catch (Exception e) {
			throw new UOMoRuntimeException(e);
		}
	}

	/**
	 * Create an instance of Ucum services. filename must point to a valid
	 * ucum-essence file (see class documentation)
	 */
	public UcumEssenceService(String filename) throws UOMoRuntimeException {
		super();
		assert new File(filename).exists() : paramError("factory", "file",
				"must exist");
		try {
			model = new DefinitionParser().parse(filename);
		} catch (Exception e) {
			throw new UOMoRuntimeException(e);
		}
	}

	private String paramError(String method, String param, String msg) {
		return getClass().getName() + "." + method + "." + param
				+ " is not acceptable: " + msg;
	}

	public UcumVersionDetails ucumIdentification() {
		return new UcumVersionDetails(model.getRevisionDate(),
				model.getVersion());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.uomo.ucum.UcumServiceEx#getModel()
	 */
	public UcumModel getModel() {
		return model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.uomo.ucum.UcumServiceEx#search(org.eclipse.uomo.ucum.model.
	 * ConceptKind, java.lang.String, boolean)
	 */
	public List<Concept> search(ConceptKind kind, String text, boolean isRegex) {
		assert checkStringParam(text) : paramError("search", "text",
				"must not be null or empty");
		return new SearchImpl().doSearch(model, kind, text, isRegex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.uomo.ucum.UcumService#validateUCUM()
	 */
	public List<String> validateUCUM() {
		return new UcumValidator(model, handlers).validate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.uomo.ucum.UcumService#getProperties()
	 */
	public Set<String> getProperties() {
		Set<String> result = new HashSet<String>();
		for (DefinedUnit unit : model.getDefinedUnits()) {
			result.add(unit.getProperty());
		}
		for (BaseUnit unit : model.getBaseUnits()) {
			result.add(unit.getProperty());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ohf.ucum.UcumServiceEx#validate(java.lang.String)
	 */
	public String validate(String unit) {
		assert unit != null : paramError("validate", "unit", "must not be null");
		try {
			new ExpressionParser(model).parse(unit);
			return null;
		} catch (UOMoRuntimeException e) {
			return e.getMessage();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ohf.ucum.UcumServiceEx#validateInProperty(java.lang.String,
	 * java.lang.String)
	 */
	public String validateInProperty(String unit, String property) {
		assert checkStringParam(unit) : paramError("validate", "unit",
				"must not be null or empty");
		assert checkStringParam(property) : paramError("validateInProperty",
				"property", "must not be null or empty");
		try {
			Term term = new ExpressionParser(model).parse(unit);
			Canonical can = new UcumConverter(model, handlers).convert(term);
			term = can.getUnit();
			String cu = new ExpressionComposer().compose(term);
			if (term.hasComp() && !term.hasOp()
					&& term.getComp() instanceof Symbol) {
				Symbol sym = (Symbol) term.getComp();
				if (sym.getExponent() == 1 && sym.getUnit() instanceof BaseUnit) {
					BaseUnit b = (BaseUnit) sym.getUnit();
					if (property.equals(b.getProperty()))
						return null;
					else
						return "unit " + unit + " is of the property type "
								+ b.getProperty() + " (" + cu + "), not "
								+ property + " as required.";
				}
			}
			// defined special case
			if ("concentration".equals(property)
					&& ("g/L".equals(cu) || "mol/L".equals(cu)))
				return null;

			return "unit " + unit + " has the base units " + cu
					+ ", and are not from the property " + property
					+ " as required.";
		} catch (UOMoRuntimeException e) {
			return e.getMessage();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ohf.ucum.UcumServiceEx#validateCanonicalUnits(java.lang.String
	 * , java.lang.String)
	 */
	public String validateCanonicalUnits(String unit, String canonical) {
		assert checkStringParam(unit) : paramError("validate", "unit",
				"must not be null or empty");
		assert checkStringParam(canonical) : paramError(
				"validateCanonicalUnits", "canonical",
				"must not be null or empty");
		try {
			Term term = new ExpressionParser(model).parse(unit);
			Canonical can = new UcumConverter(model, handlers).convert(term);
			term = can.getUnit();
			String cu = new ExpressionComposer().compose(term);
			if (!canonical.equals(cu))
				return "unit " + unit + " has the base units " + cu + ", not "
						+ canonical + " as required.";
			return null;
		} catch (UOMoRuntimeException e) {
			return e.getMessage();
		}
	}

	/**
	 * given a unit, return a formal description of what the units stand for
	 * using full names
	 * 
	 * @param units
	 *            the unit code
	 * @return formal description
	 * @throws UOMoRuntimeException
	 */
	public String analyse(String unit) throws UOMoRuntimeException {
		assert checkStringParam(unit) : paramError("analyse", "unit",
				"must not be null or empty");
		Term term = new ExpressionParser(model).parse(unit);
		return new FormalStructureComposer().compose(term);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ohf.ucum.UcumServiceEx#getCanonicalUnits(java.lang.String)
	 */
	public String getCanonicalUnits(String unit) throws UOMoRuntimeException {
		assert checkStringParam(unit) : paramError("getCanonicalUnits", "unit",
				"must not be null or empty");
		try {
			Term term = new ExpressionParser(model).parse(unit);
			term = new UcumConverter(model, handlers).convert(term).getUnit();
			return new ExpressionComposer().compose(term);
		} catch (RuntimeException e) {
			throw new UOMoRuntimeException("Error processing " + unit + ": "
					+ e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.uomo.ucum.UcumServiceEx#getDefinedForms(java.lang.String)
	 */
	public List<DefinedUnit> getDefinedForms(String code)
			throws UOMoRuntimeException {
		assert checkStringParam(code) : paramError("getDefinedForms", "code",
				"must not be null or empty");
		List<DefinedUnit> result = new ArrayList<DefinedUnit>();
		BaseUnit base = model.getBaseUnit(code);
		if (base != null) {
			for (DefinedUnit unit : model.getDefinedUnits()) {
				if (!unit.isSpecial()
						&& code.equals(getCanonicalUnits(unit.getCode())))
					result.add(unit);
			}
		}
		return result;
	}

	private boolean checkStringParam(String s) {
		return s != null && !s.equals("");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ohf.ucum.UcumServiceEx#getCanonicalForm(org.eclipse.ohf.ucum
	 * .UcumEssenceService.Pair)
	 */
	public CodeValuePair<Number, String> getCanonicalForm(CodeValuePair<Number, String> value)
			throws UOMoRuntimeException {
		assert value != null : paramError("getCanonicalForm", "value",
				"must not be null");
		assert checkStringParam(value.getCode()) : paramError(
				"getCanonicalForm", "value.code", "must not be null or empty");

		Term term = new ExpressionParser(model).parse(value.getCode());
		Canonical c = new UcumConverter(model, handlers).convert(term);
		if (value.getValue() == null)
			return new CodeValuePair<Number, String>(null,
					new ExpressionComposer().compose(c.getUnit()));
		else
			return new CodeValuePair<Number, String>(
					((BigDecimal) value.getValue()).multiply(c.getValue()),
					new ExpressionComposer().compose(c.getUnit()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.uomo.ucum.UcumServiceEx#convert(java.lang.Number,
	 * java.lang.String, java.lang.String)
	 */
	public Number convert(final Number value, String sourceUnit, String destUnit)
			throws UOMoRuntimeException {
		assert value != null : paramError("convert", "value",
				"must not be null");
		assert checkStringParam(sourceUnit) : paramError("convert",
				"sourceUnit", "must not be null or empty");
		assert checkStringParam(destUnit) : paramError("convert", "destUnit",
				"must not be null or empty");

		if (sourceUnit.equals(destUnit))
			return value;

		Canonical src = new UcumConverter(model, handlers)
				.convert(new ExpressionParser(model).parse(sourceUnit));
		Canonical dst = new UcumConverter(model, handlers)
				.convert(new ExpressionParser(model).parse(destUnit));
		String s = new ExpressionComposer().compose(src.getUnit());
		String d = new ExpressionComposer().compose(dst.getUnit());
		if (!s.equals(d))
			throw new UOMoRuntimeException("Unable to convert between units "
					+ sourceUnit + " and " + destUnit
					+ " as they do not have matching canonical forms (" + s
					+ " and " + d + " respectively)");
		final BigDecimal decValue = (BigDecimal) value;
		final BigDecimal canValue = decValue.multiply(src.getValue());
		final BigDecimal result = canValue.divide(dst.getValue(), new MathContext(25));
		println(decValue.toPlainString() + sourceUnit + " =("
				+ src.getValue().toPlainString() + ")= "
				+ canValue.toPlainString() + s + " =("
				+ dst.getValue().toPlainString() + ")= "
				+ result);
		return result;
	}

	public CodeValuePair<Number, String> multiply(CodeValuePair<Number, String> o1,
			CodeValuePair<Number, String> o2) {
		// Term term = new ExpressionParser(model).parse(o1.getCode());
		// Canonical c = new Converter(model, handlers).convert(term);
		return new CodeValuePair<Number, String>(
				((BigDecimal) o1.getValue()).multiply((BigDecimal) o2
						.getValue()),
				// new ExpressionComposer().compose(c.getUnit()));
				o1.getCode() + "." + o2.getCode());
	}

}
