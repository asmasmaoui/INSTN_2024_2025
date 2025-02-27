/*******************************************************************************
 * Copyright (c) 2025 CEA LIST
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License 2.0 which
 * accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     CEA LIST - initial API and implementation
 *******************************************************************************/

package org.eclipse.papyrus.designer.sysmlv2.codegen.transformation;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.papyrus.designer.languages.common.base.HierarchyLocationStrategy;
import org.eclipse.papyrus.designer.languages.common.base.ModelElementsCreator;
import org.eclipse.papyrus.designer.languages.common.base.file.FileSystemAccessFactory;
import org.eclipse.papyrus.designer.sysmlv2.codegen.GenSysML;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.PackageableElement;

public class SysMLModelElementsCreator extends ModelElementsCreator {

	private static final String SYSML_LANG = "SysML"; //$NON-NLS-1$

	StringBuffer resultBuffer = null;
	
	boolean done = false;

	public SysMLModelElementsCreator(IProject project) {
		this(project, SYSML_LANG);
	}

	public SysMLModelElementsCreator(IProject project, String language) {
		super(FileSystemAccessFactory.create(project), new HierarchyLocationStrategy(), language);
		this.project = project;
		sysmlExt = "sysml"; //$NON-NLS-1$
	}

	protected String sysmlExt;

	@Override
	protected boolean noCodeGen(Element element) {
		return false;
	}

	@Override
	public void createPackageableElement(PackageableElement element, IProgressMonitor monitor, boolean recursive) {
		if (done) {
			return;
		}
		if (resultBuffer == null) {
			resultBuffer = new StringBuffer();
			super.createPackageableElement(element, monitor, recursive);
			final String fileName = locStrategy.getFileName(element) + "." + sysmlExt; //$NON-NLS-1$
			fileSystemAccess.generateFile(fileName, resultBuffer.toString());
			resultBuffer = null;
			done = true;
		}
		else {
			super.createPackageableElement(element, monitor, recursive);			
		}
	}

	@Override
	protected void createPackageableElementFile(PackageableElement pe, IProgressMonitor monitor) {
		if (pe instanceof Classifier) {
			Classifier classifier = (Classifier) pe;
			if (classifier instanceof Class) {
				// please note that SysMLv1 requirements are also based on classes
				String result = GenSysML.genElement((Class) classifier).toString();
				resultBuffer.append(result);
			}
		}
	}
}
