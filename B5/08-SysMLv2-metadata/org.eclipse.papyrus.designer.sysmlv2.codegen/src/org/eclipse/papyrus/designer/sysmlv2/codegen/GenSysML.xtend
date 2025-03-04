/*******************************************************************************
 * Copyright (c) 2025 CEA LIST
 * 
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse License 2.0 which
 * accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     CEA LIST - initial API and implementation
 *******************************************************************************/

package org.eclipse.papyrus.designer.sysmlv2.codegen;

import org.eclipse.papyrus.sysml16.blocks.Block
import static extension org.eclipse.uml2.uml.util.UMLUtil.getStereotypeApplication
import org.eclipse.papyrus.sysml16.requirements.Requirement
import org.eclipse.uml2.uml.Classifier
import org.eclipse.uml2.uml.Package

class GenSysML {
	/**
	 * Simple SysMLv2 code generator
	 */
	static def CharSequence createPackage(Package pkg, boolean isTopLevel) '''
		package «pkg.name» {
			«IF isTopLevel»
				import ...
			«ENDIF»
			«FOR pe : pkg.packagedElements»
				«IF pe instanceof Classifier»
					«pe.genElement»
				«ELSEIF pe instanceof Package»
					«pe.createPackage(false)»
				«ENDIF»
			«ENDFOR»
		}
	'''
	
	static def CharSequence genElement(Classifier classifier) {
		val block = classifier.getStereotypeApplication(Block)
		if (block !== null) {
			return genBlock(block)
		}
		val req = classifier.getStereotypeApplication(Requirement)
		if (req !== null) {
			return genReq(req)
		}
		return ""
	}

	static def genBlock(Block block) '''
		part def «block.base_Class.name» {
			...
			«FOR connector : block.base_Class.ownedConnectors»
				«connector.ends.get(0).role.name»
			«ENDFOR»
		}
	'''
	
	static def genReq(Requirement req) '''
		requirement «req.base_Class.name» {
			...
		}
	'''
}
