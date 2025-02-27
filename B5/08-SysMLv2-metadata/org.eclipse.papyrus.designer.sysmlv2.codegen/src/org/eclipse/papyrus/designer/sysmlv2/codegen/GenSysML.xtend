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

import org.eclipse.uml2.uml.Class
import org.eclipse.papyrus.sysml16.blocks.Block
import static extension org.eclipse.uml2.uml.util.UMLUtil.getStereotypeApplication
import org.eclipse.papyrus.sysml16.requirements.Requirement

class GenSysML {
	/**
	 * Simple SysMLv2 code generator
	 */
	static def genElement(Class clazz) {
		val block = clazz.getStereotypeApplication(Block)
		if (block !== null) {
			return genBlock(block)
		}
		val req = clazz.getStereotypeApplication(Requirement)
		if (req !== null) {
			return genReq(req)
		}
	}

	static def genBlock(Block block) '''
		part def «block.base_Class.name» {
			...
		}
	'''
	
	static def genReq(Requirement req) '''
		requirement «req.base_Class.name» {
			...
		}
	'''
}
