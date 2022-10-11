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
import org.pathcheck.intellij.cql.psi.DeclaringIdentifiers
import org.pathcheck.intellij.cql.psi.LookupProvider
import org.pathcheck.intellij.cql.utils.LookupHelper
import org.pathcheck.intellij.cql.utils.cleanText

/** A subtree associated with a function definition.
 * Its scope is the set of arguments.
 */
class AggregateClauseDefSubtree(node: ASTNode, idElementType: IElementType) : IdentifierDefSubtree(node, idElementType), ScopeNode, DeclaringIdentifiers, LookupProvider {
    override fun resolve(element: PsiNamedElement): PsiElement? {
        visibleIdentifiers().firstOrNull() {
            it.text == element.text
        }?.let {
            return it.parent
        }

        // sends to parent scope.
        return context?.resolve(element)
    }

    override fun visibleIdentifiers(): List<PsiElement> {
        return listOf(
            "/aggregateClause/identifier/IDENTIFIER",
            "/aggregateClause/identifier/DELIMITEDIDENTIFIER",
            "/aggregateClause/identifier/QUOTEDIDENTIFIER"
        ).mapNotNull {
            XPath.findAll(CqlLanguage, this, it)
        }.flatten()
    }

    fun getAggragateClauseName(): PsiElement? {
        return XPath.findAll(CqlLanguage, this, "/aggregateClause/identifier").firstOrNull()
    }

    override fun lookup(): List<LookupElementBuilder> {
        return listOfNotNull(
            LookupHelper.build(
                getAggragateClauseName()?.cleanText(),
                AllIcons.Nodes.Parameter,
                null,
                null
            )
        )
    }


}