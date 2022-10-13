package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.pathcheck.intellij.cql.psi.DeclaringIdentifiers
import org.pathcheck.intellij.cql.psi.LookupProvider
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.antlr.PsiContextNodes
import org.pathcheck.intellij.cql.utils.LookupHelper
import org.pathcheck.intellij.cql.utils.cleanText

/** A subtree associated with a query.
 * Its scope is the set of arguments.
 */
class Query(node: ASTNode) : BasePsiNode(node), ScopeNode, DeclaringIdentifiers, LookupProvider {
    fun sourceClause(): PsiContextNodes.SourceClause? {
        return getRule(PsiContextNodes.SourceClause::class.java, 0)
    }

    fun letClause(): PsiContextNodes.LetClause? {
        return getRule(PsiContextNodes.LetClause::class.java, 0)
    }

    fun queryInclusionClause(): List<PsiContextNodes.QueryInclusionClause>? {
        return getRules(PsiContextNodes.QueryInclusionClause::class.java)
    }

    fun queryInclusionClause(i: Int): PsiContextNodes.QueryInclusionClause? {
        return getRule(PsiContextNodes.QueryInclusionClause::class.java, i)
    }

    fun whereClause(): PsiContextNodes.WhereClause? {
        return getRule(PsiContextNodes.WhereClause::class.java, 0)
    }

    fun aggregateClause(): AggregateClause? {
        return getRule(AggregateClause::class.java, 0)
    }

    fun returnClause(): PsiContextNodes.ReturnClause? {
        return getRule(PsiContextNodes.ReturnClause::class.java, 0)
    }

    fun sortClause(): PsiContextNodes.SortClause? {
        return getRule(PsiContextNodes.SortClause::class.java, 0)
    }

    /**
     * User clicked in the [element]. This function tries to find the definition token related to the [element]
     * inside this subtree and return it.
     */
    override fun resolve(element: PsiNamedElement): PsiElement? {
        // only resolves if it comes from a non-definition element of the current node.
        sourceClause()?.aliasedQuerySource()?.map {

            val exp = it.querySource()?.qualifiedIdentifierExpression()

            val list = exp?.qualifierExpression()?.map {
                it.referentialIdentifier()?.identifier()?.any()
            } ?: emptyList()

            list.plus(exp?.referentialIdentifier()?.identifier()?.any()).filterNotNull()

        }?.flatten()?.firstOrNull {
            it.text == element.text && it.textRange == element.textRange
        }?.let {
            return context?.resolve(element)
        }

        // Finds the aliases that were defined under this subtree and checks if the element is one of these aliases.
        visibleIdentifiers().firstOrNull() {
            it.text == element.text
        }?.let {
            return it.parent
        }

        // sends to parent scope.
        return context?.resolve(element)
    }

    override fun visibleIdentifiers(): List<PsiElement> {
        return sourceClause()?.aliasedQuerySource()?.map {
            it.alias()?.identifier()?.all() ?: emptyList()
        }?.flatten() ?: emptyList()
    }

    fun getAliasName(): PsiElement? {
        return visibleIdentifiers().first()
    }

    override fun lookup(): List<LookupElementBuilder> {
        return listOfNotNull(
            LookupHelper.build(
                getAliasName()?.cleanText(),
                AllIcons.Nodes.Variable,
                null,
                null
            )
        )
    }
}