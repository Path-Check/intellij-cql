package org.pathcheck.intellij.cql

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.antlr.intellij.adaptor.xpath.XPath
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
            "/library/statement/functionDefinition/identifierOrFunctionIdentifier/identifier",
            "/library/statement/functionDefinition/identifierOrFunctionIdentifier/functionIdentifier",
            "/library/statement/expressionDefinition/identifier",
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

    fun getLibraryNameElement(): PsiElement? {
        return listOf(
            "/library/libraryDefinition/qualifiedIdentifier/identifier/IDENTIFIER",
            "/library/libraryDefinition/qualifiedIdentifier/identifier/DELIMITEDIDENTIFIER",
            "/library/libraryDefinition/qualifiedIdentifier/identifier/QUOTEDIDENTIFIER",
        ).firstNotNullOfOrNull {
            return XPath.findAll(CqlLanguage, this, it).firstOrNull()
        }
    }
}