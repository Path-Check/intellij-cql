package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.antlr.intellij.adaptor.xpath.XPath
import org.pathcheck.intellij.cql.CqlLanguage
import org.pathcheck.intellij.cql.psi.LookupProvider
import org.pathcheck.intellij.cql.utils.LookupHelper
import org.pathcheck.intellij.cql.utils.cleanText

/** A subtree associated with a query.
 * Its scope is the set of arguments.
 */
class CodeSystemDefSubtree(node: ASTNode, idElementType: IElementType) : IdentifierDefSubtree(node, idElementType),
    ScopeNode, LookupProvider {

    override fun resolve(element: PsiNamedElement): PsiElement? {
        return context?.resolve(element)
    }

    fun getCodeSystemName(): PsiElement? {
        return XPath.findAll(CqlLanguage, this, "/codesystemDefinition/identifier").firstOrNull()
    }

    fun getCodeSystemID(): PsiElement? {
        return XPath.findAll(CqlLanguage, this, "/codesystemDefinition/codesystemId").firstOrNull()
    }

    fun getCodeSystemVersion(): PsiElement? {
        return XPath.findAll(CqlLanguage, this, "/codesystemDefinition/versionSpecifier").firstOrNull()
    }

    override fun lookup(): List<LookupElementBuilder> {
        return listOfNotNull(
            LookupHelper.build(
                getCodeSystemName()?.cleanText(),
                AllIcons.Nodes.Gvariable,
                listOfNotNull(getCodeSystemID()?.cleanText(), getCodeSystemVersion()?.cleanText()).joinToString(" - "),
                null
            )
        )
    }
}