package org.pathcheck.intellij.cql.elm

import com.intellij.openapi.module.ModuleUtilCore
import com.intellij.openapi.project.rootManager
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager

class DirectoryFinder {
    /**
     * This function tries to find all possible locations of dependent files that when compiled would be in the same location
     */
    fun findAllConvergingLocations(file: PsiFile): List<PsiDirectory> {
        val providers = mutableListOf(file.originalFile.containingDirectory)

        ModuleUtilCore.findModuleForFile(file.originalFile)?.let { module ->
            val fileDir = file.originalFile.containingDirectory.virtualFile.path
            val manager = ModuleRootManager.getInstance(module)

            manager.getSourceRoots(true).forEach { srcRoot ->
                if (fileDir.startsWith(srcRoot.path)) {
                    val relPath = fileDir.removePrefix(srcRoot.path)
                    manager.getDependencies(true).forEach { depModule ->
                        depModule.rootManager.getSourceRoots(true).forEach { depSrcRoot ->
                            depSrcRoot.findFileByRelativePath(relPath)?.let { sourceProvider ->
                                PsiManager.getInstance(module.project).findDirectory(sourceProvider)?.let {
                                    providers.add(it)
                                }
                            }
                        }
                    }
                }
            }
        }

        return providers;
    }
}