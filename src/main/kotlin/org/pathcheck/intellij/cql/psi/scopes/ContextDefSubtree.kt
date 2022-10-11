package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
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
class ContextDefSubtree(node: ASTNode, idElementType: IElementType) : IdentifierDefSubtree(node, idElementType), ScopeNode, LookupProvider {
    override fun resolve(element: PsiNamedElement): PsiElement? {
        listOf(
            "/contextDefinition/identifier/IDENTIFIER",
            "/contextDefinition/identifier/DELIMITEDIDENTIFIER",
            "/contextDefinition/identifier/QUOTEDIDENTIFIER",
        ).mapNotNull {
            XPath.findAll(CqlLanguage, this, it)
        }.flatten().firstOrNull() {
            it.text == element.name
        }?.let {
            return it.parent
        }

        return context?.resolve(element)
    }

    fun getContextName(): PsiElement? {
        return XPath.findAll(CqlLanguage, this, "/contextDefinition/identifier").firstOrNull()
    }

    override fun lookup(): List<LookupElementBuilder> {
        return listOfNotNull(
            LookupHelper.build(
                getContextName()?.cleanText(),
                AllIcons.Nodes.Package,
                null,
                null
            )
        )
    }
}