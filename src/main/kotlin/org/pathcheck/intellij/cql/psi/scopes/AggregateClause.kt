package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.pathcheck.intellij.cql.psi.DeclaringIdentifiers
import org.pathcheck.intellij.cql.psi.Identifier
import org.pathcheck.intellij.cql.psi.LookupProvider
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.antlr.PsiContextNodes
import org.pathcheck.intellij.cql.utils.LookupHelper
import org.pathcheck.intellij.cql.utils.cleanText

/** A subtree associated with a function definition.
 * Its scope is the set of arguments.
 */
class AggregateClause(node: ASTNode) : BasePsiNode(node), ScopeNode, DeclaringIdentifiers, LookupProvider {
    fun identifier(): Identifier? {
        return getRule(Identifier::class.java, 0)
    }

    fun expression(): PsiContextNodes.Expression? {
        return getRule(PsiContextNodes.Expression::class.java, 0)
    }

    fun startingClause(): PsiContextNodes.StartingClause? {
        return getRule(PsiContextNodes.StartingClause::class.java, 0)
    }

    override fun resolve(element: PsiNamedElement): PsiElement? {
        identifier()?.all()?.firstOrNull() {
            it.text == element.text
        }?.let {
            return it.parent
        }

        // sends to parent scope.
        return context?.resolve(element)
    }

    override fun visibleIdentifiers(): List<PsiElement> {
        return identifier()?.all() ?: emptyList()
    }

    override fun lookup(): List<LookupElementBuilder> {
        return listOfNotNull(
            LookupHelper.build(
                identifier()?.cleanText(),
                AllIcons.Nodes.Parameter,
                null,
                null
            )
        )
    }


}