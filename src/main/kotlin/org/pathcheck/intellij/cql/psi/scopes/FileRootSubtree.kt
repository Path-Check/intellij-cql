package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.antlr.intellij.adaptor.xpath.XPath
import org.cqframework.cql.cql2elm.model.Model
import org.pathcheck.intellij.cql.CqlFileType
import org.pathcheck.intellij.cql.CqlIcons
import org.pathcheck.intellij.cql.CqlLanguage
import org.pathcheck.intellij.cql.psi.DeclaringIdentifiers
import org.pathcheck.intellij.cql.psi.LookupProvider
import javax.swing.Icon


class FileRootSubtree(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, CqlLanguage), ScopeNode,
    DeclaringIdentifiers, LookupProvider {
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
        visibleIdentifiers().firstOrNull() {
            it.text == element.text
        }?.let {
            return it.parent
        }

        return null
    }

    override fun visibleIdentifiers(): List<PsiElement> {
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
        }.flatten().mapNotNull {
            XPath.findAll(CqlLanguage, this, it)
        }.flatten()
    }

    override fun lookup(): List<LookupElementBuilder> {
        return listOf(
            "/library/definition/usingDefinition",
            "/library/definition/includeDefinition",
            "/library/definition/codesystemDefinition",
            "/library/definition/valuesetDefinition",
            "/library/definition/codeDefinition",
            "/library/definition/conceptDefinition",
            "/library/definition/parameterDefinition",

            "/library/statement/functionDefinition",
            "/library/statement/expressionDefinition",
            "/library/statement/contextDefinition"
        ).mapNotNull {
            XPath.findAll(CqlLanguage, this, it)
        }.flatten()
        .map {
            (it as LookupProvider).lookup()
        }.flatten()
    }

    fun exportingLookups(): List<LookupElementBuilder> {
        return listOf(
            "/library/statement/functionDefinition",
            "/library/statement/expressionDefinition"
        ).mapNotNull {
            XPath.findAll(CqlLanguage, this, it)
        }.flatten()
        .map {
            (it as LookupProvider).lookup()
        }.flatten()
    }

    fun findModels(): List<Model> {
        return XPath.findAll(CqlLanguage, this, "/library/definition/usingDefinition").mapNotNull {
            (it as UsingDefSubtree).getModel()
        }
    }
}