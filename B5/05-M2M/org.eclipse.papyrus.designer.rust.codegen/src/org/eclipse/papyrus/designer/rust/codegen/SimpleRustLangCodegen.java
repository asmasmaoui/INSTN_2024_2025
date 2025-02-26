/*******************************************************************************
 * Copyright (c) 2020 CEA LIST.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     CEA LIST - ansgar.radermacher@cea.fr   initial API and implementation
 *******************************************************************************/

package org.eclipse.papyrus.designer.rust.codegen;

import org.eclipse.core.resources.IProject;
import org.eclipse.papyrus.designer.languages.common.base.CommonLangCodegen;
import org.eclipse.papyrus.designer.languages.common.base.ModelElementsCreator;
import org.eclipse.papyrus.designer.languages.common.extensionpoints.ILangCodegen;
import org.eclipse.papyrus.designer.languages.common.extensionpoints.MethodInfo;
import org.eclipse.papyrus.designer.languages.common.extensionpoints.SyncInformation;
import org.eclipse.papyrus.designer.rust.codegen.transformation.RustModelElementsCreator;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.PackageableElement;

/**
 * Rust language support
 */
public class SimpleRustLangCodegen extends CommonLangCodegen implements ILangCodegen {

	@Override
	public String getDescription() {
		return "A very simple generator for Rust"; //$NON-NLS-1$
	}

	@Override
	public boolean isEligible(Element modelElement) {
		if (modelElement instanceof Classifier) {
			return true;
		}
		return false;
	}

	@Override
	public String getSuffix(FILE_KIND fileKind) {
		return "rs"; //$NON-NLS-1$
	}

	@Override
	public IProject getTargetProject(PackageableElement pe, boolean createIfMissing) {
		return LocateRustProject.getTargetProject(pe, createIfMissing);
	}

	protected ModelElementsCreator newCreator(IProject project, PackageableElement pe) {
		return new RustModelElementsCreator(project);
	}

	@Override
	public SyncInformation getSyncInformation(String methodName, String body) {
		return null;
	}

	@Override
	public MethodInfo getMethodInfo(NamedElement operationOrBehavior) {
		return null;
	}
}
