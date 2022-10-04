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
        return "Cql Language file"
    }

    override fun getIcon(flags: Int): Icon {
        return CqlIcons.FILE
    }

    override fun getContext(): ScopeNode? {
        // Maximum Scope
        return null
    }

    /**
     * Looks for identifiers that match the declaration of the element.
     */
    override fun resolve(element: PsiNamedElement): PsiElement? {
        return listOf(
            "/library/statement/functionDefinition/identifierOrFunctionIdentifier",
            "/library/statement/expressionDefinition/identifier",
            "/library/definition/usingDefinition/qualifiedIdentifier",
            "/library/definition/usingDefinition/localIdentifier/identifier",
            "/library/definition/includeDefinition/qualifiedIdentifier",
            "/library/definition/includeDefinition/localIdentifier/identifier",
            "/library/definition/codesystemDefinition/identifier",
            "/library/definition/valuesetDefinition/identifier",
            "/library/definition/codeDefinition/identifier",
            "/library/definition/conceptDefinition/identifier",
            "/library/definition/parameterDefinition/identifier"
        ).map {
            SymtabUtils.resolve(this, CqlLanguage, element, it)
        }.filterNotNull().firstOrNull()
    }
}