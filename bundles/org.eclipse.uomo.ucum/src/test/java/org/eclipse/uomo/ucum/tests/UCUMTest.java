/*******************************************************************************
 * Crown Copyright (c) 2006, 2011, Copyright (c) 2006, 2008 Kestral Computing P/L.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Kestral Computing P/L - initial implementation
 *    Werner Keil - changed System.out to OutputHelper
 *******************************************************************************/

package org.eclipse.uomo.ucum.tests;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

//import org.eclipse.ohf.h3et.mif.core.MIFManager;
import org.eclipse.uomo.ucum.impl.UcumMifServices;
import org.eclipse.uomo.ucum.model.Concept;
import org.eclipse.uomo.ucum.model.DefinedUnit;
import org.eclipse.uomo.core.impl.OutputHelper;
import org.eclipse.uomo.core.impl.Pair;
import org.eclipse.uomo.core.UOMoException;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

public class UCUMTest implements H3ETTestConfiguration {
	private static final Logger logger = Logger.getLogger(UCUMTest.class.getName());
//	private static final String PROJECT_PATH = System.getenv("ProjPath");
//	public static final String TEST_WORKSPACE = System.getenv("WORKSPACE") != null ? System.getenv("WORKSPACE") : PROJECT_PATH + "/Medical/workspace/ohf/";
//	public static final String TEST_PROJECT = TEST_WORKSPACE + "org.eclipse.ohf.ucum.tests";
//	public static final String UCUM_FILE = TEST_PROJECT + "/Resources/ucum-essence.xml";	
	
	// these tests use the UcumFunctionalTests, which may be found at ...
	// this constant is the local place where the correct copy of the 
	// ucum functional tests may be found
//	public static final String UCUM_TESTS_FILE = TEST_WORKSPACE + "\\org.eclipse.ohf.ucum.tests\\Resources\\UcumFunctionalTests.xml";

	private UcumMifServices service;
	
	private UcumMifServices svc() throws UOMoException {
		if (service == null) {
			service = new UcumMifServices(getClass().getClassLoader().getResourceAsStream("ucum-essence.xml"));
		}
		return service;
	}

	
	// --- non-shared tests. These tests are specific to the eclipse implementation.
	/**
	 * simplest test: check that something exists in the model
	 */
	@Test
	public void testLoadUcum() throws IOException, XmlPullParserException, UOMoException {
		OutputHelper.println("Loaded UCUM: "+
				Integer.toString(svc().getModel().getPrefixes().size())+" prefixes, " +
				Integer.toString(svc().getModel().getBaseUnits().size())+" base units, " +
				Integer.toString(svc().getModel().getDefinedUnits().size())+" units");
	}
	
	/**
	 * Just check that we actually have some properties available
	 */
	@Test
	public void testProperties() throws IOException, XmlPullParserException, UOMoException {
		assertTrue(svc().getProperties().size() > 0);
	}
	
	/**
	 * Check that the search is working properly. Should move this into the UCUM functional tests?
	 */
	@Test
	public void testSearch() throws FileNotFoundException, UOMoException {
		List<Concept> concepts = svc().search(null, "length", false);
		assertTrue("expected 44 concepts but found "+Integer.toString(concepts.size()), concepts.size() == 44);
	}

	/**
	 * Ask the service to validate itself. 
	 * There should be no errors
	 * 
	 * @throws IOException
	 * @throws XmlPullParserException
	 * @throws UOMoException
	 */
	@Test
	public void testValidate() throws IOException, XmlPullParserException, UOMoException {
		for (String err : svc().validateUCUM()) 
			OutputHelper.println("error validating UCUM: "+err);
		assertTrue(svc().validateUCUM().size() > 0);
	}

	/**
	 * Test producing a UCUM MIF. Actually, this UCUM mif got superceded by Lloyd once
	 * he saw this one, but I'm keeping it around in case we need it later
	 * 
	 * @throws FileNotFoundException
	 * @throws UOMoException
	 */
	@Test
	@Ignore
	public void testMIF() throws FileNotFoundException, UOMoException {
//		MIFManager.getInstance().saveDocument(new FileOutputStream(new File(TEMP_PATH + "/ucum.mif")), svc().asMif(), true);	
	}

	/**
	 * Test that getting defined forms works
	 * 
	 * @throws FileNotFoundException
	 * @throws UOMoException
	 */
	@Test
	@Ignore
	public void testGetDefinedForms() throws FileNotFoundException, UOMoException {
		List<DefinedUnit> concepts = svc().getDefinedForms("m");
		assertTrue("expected 46 but found "+Integer.toString(concepts.size()), concepts.size() == 46);
	}

	// UCUM function test utilities
	Document tests = null;
	
	private Element loadTests() throws ParserConfigurationException, FileNotFoundException, SAXException, IOException {
		if (tests == null) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(false); // no namespaces...
			DocumentBuilder builder = factory.newDocumentBuilder();
			tests = builder.parse(getClass().getClassLoader().getResourceAsStream("UcumFunctionalTests.xml"));
		}
		return tests.getDocumentElement();
	}
	
	private void validate(String id, String unit, boolean ok) throws UOMoException {
		if (ok)
			assertTrue("case "+id+": "+unit+" should be valid", svc().validate(unit) == null);
		else
			assertTrue("case "+id+": "+unit+" should not be valid", svc().validate(unit) != null);
	}

	private void generateDisplayName(String id, String unit, String expected) throws UOMoException {
		String actual = svc().analyse(unit);
		assertTrue("case "+id+": "+unit+": expected '"+expected+"' but got '"+actual+"'", expected.equals(actual));		
	}


	private void doConversion(String id, BigDecimal v1, String u1, String u2, BigDecimal v2) throws UOMoException {
		BigDecimal r = (BigDecimal)svc().convert(v1, u1, u2);
		assertTrue("case "+id+": "+"conversion from "+u1+" to "+u2+" failed. Expected "+v2.toPlainString()+" but got "+r.toPlainString(),
				r.equals(v2) || r.toPlainString().equals(v2.toPlainString()));
	}

	private String showPair(Pair pair) {
		return ((BigDecimal)pair.getValue()).toPlainString()+pair.getCode();
	}

	private void doMultiplication(String id, Pair op1, Pair op2, Pair expected) throws UOMoException {
		Pair result = svc().multiply(op1, op2);
		if (result != null) {
			Number v = svc().convert(result.getValue(), result.getCode(), expected.getCode());
			if (!expected.getValue().equals(v)) {
				throw new UOMoException("case "+id+": "+showPair(op1)+" * " +showPair(op2)+ " = "+showPair(expected)+" but got "+showPair(result)+" instead");
			}
		} else {
			// TODO factor out into separate log method, try use varargs
			StringBuilder sbOut = new StringBuilder("result null: ");
			sbOut.append(id);
			sbOut.append("; ");
			sbOut.append(op1.getValue());
			sbOut.append(op1.getCode());
			sbOut.append("; ");
			sbOut.append(op2.getValue());
			sbOut.append(op2.getCode());
			sbOut.append("; ");
			sbOut.append(expected.getValue());
			sbOut.append(expected.getCode());
			logger.warning(sbOut.toString());
		}
	}

	// --- UCUM Functional Tests
	// for doco, see the UCUM functional tests source file.
	
	@Test
	@Ignore
	public void testValidation() throws UOMoException, FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		Element docRoot = loadTests();
		Element validation = (Element) docRoot.getElementsByTagName("validation").item(0);
		NodeList cases = validation.getElementsByTagName("case");
		for (int i = 0; i < cases.getLength(); i++) {
			Element case_ = (Element) cases.item(i);
			validate(case_.getAttribute("id"), case_.getAttribute("unit"), Boolean.parseBoolean(case_.getAttribute("valid")));			
		}
	}

	@Test
	@Ignore("works in Eclipse?")
	public void testDisplayNameGeneration() throws UOMoException, FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		Element docRoot = loadTests();
		Element validation = (Element) docRoot.getElementsByTagName("displayNameGeneration").item(0);
		NodeList cases = validation.getElementsByTagName("case");
		for (int i = 0; i < cases.getLength(); i++) {
			Element case_ = (Element) cases.item(i);
			generateDisplayName(case_.getAttribute("id"), case_.getAttribute("unit"), case_.getAttribute("display"));			
		}
	}

	
	@Test
	@Ignore
	public void testConversion() throws UOMoException, FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		Element docRoot = loadTests();
		Element validation = (Element) docRoot.getElementsByTagName("conversion").item(0);
		NodeList cases = validation.getElementsByTagName("case");
		for (int i = 0; i < cases.getLength(); i++) {
			Element case_ = (Element) cases.item(i);
			doConversion(case_.getAttribute("id"), new BigDecimal(case_.getAttribute("value")), case_.getAttribute("srcUnit"), 
					case_.getAttribute("dstUnit"), new BigDecimal(case_.getAttribute("outcome")));			
		}
	}

	@Test
	public void testMultiplication() throws UOMoException, FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		Element docRoot = loadTests();
		Element validation = (Element) docRoot.getElementsByTagName("multiplication").item(0);
		NodeList cases = validation.getElementsByTagName("case");
		for (int i = 0; i < cases.getLength(); i++) {
			Element case_ = (Element) cases.item(i);
			doMultiplication(case_.getAttribute("id"), 
					new Pair(new BigDecimal(case_.getAttribute("v1")), case_.getAttribute("u1")), 
					new Pair(new BigDecimal(case_.getAttribute("v2")), case_.getAttribute("u2")), 
					new Pair(new BigDecimal(case_.getAttribute("vRes")), case_.getAttribute("uRes")));			
		}
	}

}
