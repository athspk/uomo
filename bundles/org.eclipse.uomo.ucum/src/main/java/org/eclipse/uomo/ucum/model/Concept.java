/*******************************************************************************
 * Crown Copyright (c) 2006, 2012, Copyright (c) 2006, 2017 Kestral Computing P/L and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Kestral Computing P/L - initial implementation
 *    Werner Keil - updates and consolidation with core
 *******************************************************************************/

package org.eclipse.uomo.ucum.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uomo.core.ICode;
import org.eclipse.uomo.core.IDescription;
import org.eclipse.uomo.core.INames;
import org.eclipse.uomo.core.ISymbol;

public class Concept implements ICode<String>, IDescription, ISymbol, INames {

	private final ConceptKind kind;
	/**
	 * case sensitive code for this concept
	 */
	private String code;
	
	/**
	 * case insensitive (UPPERCASE) code for this concept
	 */
	private String codeUC;
	
	/**
	 * print symbol for this concept 
	 */
	private String printSymbol;
	
	/**
	 * names for the concept
	 */
	private List<String> names = new ArrayList<String>();
	
	
	/**
	 * @param code
	 * @param codeUC
	 */
	public Concept(ConceptKind kind, String code, String codeUC) {
		super();
		this.kind = kind;
		this.code = code;
		this.codeUC = codeUC;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the codeUC
	 */
	public String getCodeUC() {
		return codeUC;
	}

	/**
	 * @param codeUC the codeUC to set
	 */
	public void setCodeUC(String codeUC) {
		this.codeUC = codeUC;
	}

	/**
	 * @return the printSymbol
	 */
	public String getPrintSymbol() {
		return printSymbol;
	}

	/**
	 * @param printSymbol the printSymbol to set
	 */
	public void setPrintSymbol(String printSymbol) {
		this.printSymbol = printSymbol;
	}
	
	@Override
	public String getSymbol() {
		return getPrintSymbol();
	}

	/**
	 * @return the names
	 */
	public List<String> getNames() {
		return names;
	}
	
	public String getName() {
		return (names!=null && names.size()>0) ? names.get(0) : "";
	}

	/**
	 * @return the kind
	 */
	public ConceptKind getKind() {
		return kind;
	}

	public String getDescription() {
		return  kind.toString().toLowerCase()+" "+code+" ('"+names.get(0)+"')"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
	
	@Override
	public String toString() {
		return this.getCode() + " = " + getDescription(); //$NON-NLS-1$
	}
}
