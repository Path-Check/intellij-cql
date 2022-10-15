package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.hl7.cql.model.DataType
import org.pathcheck.intellij.cql.psi.DeclaringIdentifiers
import org.pathcheck.intellij.cql.psi.HasResultType
import org.pathcheck.intellij.cql.psi.LookupProvider
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.expressions.Expression
import org.pathcheck.intellij.cql.psi.expressions.ExpressionTerm
import org.pathcheck.intellij.cql.psi.expressions.Quantity
import org.pathcheck.intellij.cql.psi.expressions.SimpleLiteral
import org.pathcheck.intellij.cql.psi.references.Identifier
import org.pathcheck.intellij.cql.utils.LookupHelper
import org.pathcheck.intellij.cql.utils.cleanText

/** A subtree associated with a query.
 * Its scope is the set of arguments.
 */
class Query(node: ASTNode) : BasePsiNode(node), ScopeNode, DeclaringIdentifiers, LookupProvider, HasResultType {
    fun sourceClause(): SourceClause? {
        return getRule(SourceClause::class.java, 0)
    }

    fun letClause(): LetClause? {
        return getRule(LetClause::class.java, 0)
    }

    fun queryInclusionClause(): List<QueryInclusionClause>? {
        return getRules(QueryInclusionClause::class.java)
    }

    fun queryInclusionClause(i: Int): QueryInclusionClause? {
        return getRule(QueryInclusionClause::class.java, i)
    }

    fun whereClause(): WhereClause? {
        return getRule(WhereClause::class.java, 0)
    }

    fun aggregateClause(): AggregateClause? {
        return getRule(AggregateClause::class.java, 0)
    }

    fun returnClause(): ReturnClause? {
        return getRule(ReturnClause::class.java, 0)
    }

    fun sortClause(): SortClause? {
        return getRule(SortClause::class.java, 0)
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
                it.referentialIdentifier()?.identifier()?.anyToken()
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

    override fun getResultType(): DataType? {
        return aggregateClause()?.getResultType()
            ?: returnClause()?.getResultType()
            ?: sourceClause()?.getResultType()
    }
}


class QuerySource(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun retrieve(): Retrieve? {
        return getRule(Retrieve::class.java, 0)
    }

    fun qualifiedIdentifierExpression(): QualifiedIdentifierExpression? {
        return getRule(QualifiedIdentifierExpression::class.java, 0)
    }

    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    override fun getResultType(): DataType? {
        return retrieve()?.getResultType()
            ?: expression()?.getResultType()
            ?: qualifiedIdentifierExpression()?.getResultType()
    }
}

class AliasedQuerySource(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun querySource(): QuerySource? {
        return getRule(QuerySource::class.java, 0)
    }

    fun alias(): Alias? {
        return getRule(Alias::class.java, 0)
    }

    override fun getResultType() = querySource()?.getResultType()
}

class Alias(node: ASTNode) : BasePsiNode(node) {
    fun identifier(): Identifier? {
        return getRule(Identifier::class.java, 0)
    }
}

class LetClause(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun letClauseItem(): List<LetClauseItem>? {
        return getRules(LetClauseItem::class.java)
    }

    fun letClauseItem(i: Int): LetClauseItem? {
        return getRule(LetClauseItem::class.java, i)
    }

    override fun getResultType() = letClauseItem(0)?.getResultType()
}

class LetClauseItem(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun identifier(): Identifier? {
        return getRule(Identifier::class.java, 0)
    }

    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    override fun getResultType() = expression()?.getResultType()
}

class SourceClause(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun aliasedQuerySource(): List<AliasedQuerySource>? {
        return getRules(AliasedQuerySource::class.java)
    }

    fun aliasedQuerySource(i: Int): AliasedQuerySource? {
        return getRule(AliasedQuerySource::class.java, i)
    }

    override fun getResultType() = aliasedQuerySource(0)?.getResultType()
}

class QueryInclusionClause(node: ASTNode) : BasePsiNode(node) {
    fun withClause(): WithClause? {
        return getRule(WithClause::class.java, 0)
    }

    fun withoutClause(): WithoutClause? {
        return getRule(WithoutClause::class.java, 0)
    }
}

class WithClause(node: ASTNode) : BasePsiNode(node) {
    fun aliasedQuerySource(): AliasedQuerySource? {
        return getRule(AliasedQuerySource::class.java, 0)
    }

    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }
}

class WithoutClause(node: ASTNode) : BasePsiNode(node) {
    fun aliasedQuerySource(): AliasedQuerySource? {
        return getRule(AliasedQuerySource::class.java, 0)
    }

    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }
}


class WhereClause(node: ASTNode) : BasePsiNode(node) {
    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }
}

class AggregateClause(node: ASTNode) : BasePsiNode(node), ScopeNode, DeclaringIdentifiers, LookupProvider, HasResultType {
    fun identifier(): Identifier? {
        return getRule(Identifier::class.java, 0)
    }

    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    fun startingClause(): StartingClause? {
        return getRule(StartingClause::class.java, 0)
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

    override fun getResultType() = startingClause()?.getResultType() ?: resolveTypeName("System", "Any")
}


class StartingClause(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun simpleLiteral(): SimpleLiteral? {
        return getRule(SimpleLiteral::class.java, 0)
    }

    fun quantity(): Quantity? {
        return getRule(Quantity::class.java, 0)
    }

    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    override fun getResultType(): DataType? {
        return simpleLiteral()?.getResultType() ?:
        quantity()?.getResultType() ?:
        expression()?.getResultType()
    }
}

class ReturnClause(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    override fun getResultType() = expression()?.getResultType()
}

class SortClause(node: ASTNode) : BasePsiNode(node) {
    fun sortDirection(): SortDirection? {
        return getRule(SortDirection::class.java, 0)
    }

    fun sortByItem(): List<SortByItem>? {
        return getRules(SortByItem::class.java)
    }

    fun sortByItem(i: Int): SortByItem? {
        return getRule(SortByItem::class.java, i)
    }
}

class SortDirection(node: ASTNode) : BasePsiNode(node)
class SortByItem(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun expressionTerm(): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, 0)
    }

    fun sortDirection(): SortDirection? {
        return getRule(SortDirection::class.java, 0)
    }

    override fun getResultType() = expressionTerm()?.getResultType()
}