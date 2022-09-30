package org.pathcheck.intellij.cql

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.psi.ScopeNode
import javax.swing.Icon

class CqlPSIFileRoot(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, CqlLanguage), ScopeNode {
    override fun getFileType(): FileType {
        return CqlFileType
    }

    override fun toString(): String {
        return "Sample Language file"
    }

    override fun getIcon(flags: Int): Icon? {
        return CqlIcons.FILE
    }

    /** Return null since a file scope has no enclosing scope. It is
     * not itself in a scope.
     */
    override fun getContext(): ScopeNode? {
        return null
    }

    override fun resolve(element: PsiNamedElement): PsiElement? {
        return SymtabUtils.resolve(
            this, CqlLanguage,
            element, "/library/libraryDefinition/qualifiedIdentifier"
        )
    }
}