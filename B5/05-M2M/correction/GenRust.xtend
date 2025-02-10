/*******************************************************************************
 * Copyright (c) 2020 CEA LIST
 * 
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse License 2.0 which
 * accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   Abhishek Djeachandrane - Initial API and implementation
 *******************************************************************************/

package org.eclipse.papyrus.designer.rust.codegen;

import org.eclipse.uml2.uml.Class

class GenRust {
	/**
	 * Simple rust code generator
	 */
	static def genClass(Class clazz) '''
		struct «clazz.name» {
			«FOR attr : clazz.ownedAttributes SEPARATOR(",")»
				«attr.name» : «attr.type.name»
			«ENDFOR»
		}
		
		impl «clazz.name» {
			
			fn new(«FOR attr : clazz.ownedAttributes SEPARATOR(", ")»«attr.name» : «attr.type.name»«ENDFOR») -> Self {
				Self {
					«FOR attr : clazz.ownedAttributes SEPARATOR (", ")»«attr.name»«ENDFOR»
				}
			}
			
			«FOR op : clazz.ownedOperations»
				fn «op.name»(&self) -> «op.type.name» {
					// ...
				}
			«ENDFOR»	
		}
	'''
}
