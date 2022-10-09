package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.pathcheck.intellij.cql.CqlFileType
import org.pathcheck.intellij.cql.CqlIcons
import org.pathcheck.intellij.cql.CqlLanguage
import javax.swing.Icon


class FileRootSubtree(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, CqlLanguage), ScopeNode {
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
            "/library/statement/functionDefinition/identifierOrFunctionIdentifier/identifier",
            "/library/statement/functionDefinition/identifierOrFunctionIdentifier/functionIdentifier",
            "/library/statement/expressionDefinition/identifier",
            "/library/statement/contextDefinition/identifier",
            "/library/definition/usingDefinition/localIdentifier/identifier",
            "/library/definition/usingDefinition/qualifiedIdentifier/identifier",
            "/library/definition/includeDefinition/localIdentifier/identifier",
            "/library/definition/includeDefinition/qualifiedIdentifier/identifier",
            "/library/definition/codesystemDefinition/identifier",
            "/library/definition/valuesetDefinition/identifier",
            "/library/definition/codeDefinition/identifier",
            "/library/definition/conceptDefinition/identifier",
            "/library/definition/parameterDefinition/identifier"
        ).map {
            if (it.endsWith("/identifier"))
                listOf(
                    "$it/IDENTIFIER",
                    "$it/DELIMITEDIDENTIFIER",
                    "$it/QUOTEDIDENTIFIER"
                )
            else
                listOf(it)
        }.flatten()
         .firstNotNullOfOrNull {
            SymtabUtils.resolve(this, CqlLanguage, element, it)
        }
    }
}