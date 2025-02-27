/*******************************************************************************
 * Copyright (c) 2025 CEA LIST.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     CEA LIST - initial API and implementation
 *******************************************************************************/

package org.eclipse.papyrus.designer.sysmlv2.codegen;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.papyrus.designer.languages.common.base.AutoLanguageProjectSupport;
import org.eclipse.papyrus.designer.languages.common.base.TestInfo;
import org.eclipse.papyrus.designer.languages.common.extensionpoints.ILangProjectSupport;
import org.eclipse.papyrus.uml.tools.utils.PackageUtil;
import org.eclipse.swt.widgets.Display;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.util.UMLUtil;

public class LocateSysMLProject {

	private static final boolean Headless = TestInfo.runsHeadless();

	/**
	 * Locate and return the target project for the given packageable element. Return null if
	 * no target project can be found.
	 *
	 * Ensures that the target project is correctly setup to contain generated C/C++ code. Does
	 * not create a new project, but may modify existing ones.
	 *
	 * @param pe
	 *            a packageable element within a model
	 * @param createIfMissing
	 *            if true, ask the user to apply the C++ nature if required.
	 * @return the associated project, if the C++ nature is applied.
	 */
	public static IProject getTargetProject(PackageableElement pe, boolean createIfMissing) {
		Package rootPkg = PackageUtil.getRootPackage(pe);

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

		org.eclipse.papyrus.designer.languages.common.profile.Codegen.Project projectStereo = UMLUtil.getStereotypeApplication(rootPkg, org.eclipse.papyrus.designer.languages.common.profile.Codegen.Project.class);

		String projectName;
		if (projectStereo != null) {
			projectName = projectStereo.getProjectName();
		} else {
			projectName = "org.eclipse.papyrus.sysmlgen." + rootPkg.getName(); //$NON-NLS-1$
		}
		IProject modelProject = root.getProject(projectName);

		if (!modelProject.exists()) {
			if (Headless) {
				try {
					modelProject.create(null);
				} catch (CoreException e) {
					return null;
				}
			} else {
				boolean create = createIfMissing && openQuestion(
						"Create SysML project?", //$NON-NLS-1$
						String.format("Create SysML project with name %s? ", projectName)); //$NON-NLS-1$
				if (create) {
					// TODO - there is no Python project support yet, projectSupport will always throw an exception
					// => create PythonProjectSupport
					try {
						ILangProjectSupport projectSupport = AutoLanguageProjectSupport.getProjectSupport("SysML"); //$NON-NLS-1$
						modelProject = projectSupport.createProject(projectName);
						projectSupport.setSettings(modelProject, projectSupport.initialConfigurationData());
						if (modelProject == null) {
							return null;
						}
					} catch (Exception e) {
						// create standard project
						IProgressMonitor progressMonitor = new NullProgressMonitor();
						try {
							modelProject.create(progressMonitor);
						} catch (CoreException ce) {
							Activator.log.error(ce);
						}
					}
				} else {
					return null;
				}
			}
		}

		// Make sure the target project is open. If it was just created then it is likely open,
		// however if it is an old project then it could have been closed.
		if (!modelProject.isOpen()) {
			try {
				modelProject.open(null);
			} catch (CoreException e) {
				return null;
			}
		}

		// Make sure the target project has the python natures.
		return modelProject;
	}

	private static boolean openQuestion(final String title, final String message) {
		final boolean[] ret = new boolean[] { false };
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				ret[0] = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), title, message);
			}
		});
		return ret[0];
	}
}
