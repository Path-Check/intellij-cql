package org.pathcheck.intellij.cql.psi.expressions

import com.intellij.lang.ASTNode
import org.hl7.cql.model.DataType
import org.pathcheck.intellij.cql.psi.HasResultType
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.scopes.Query
import org.pathcheck.intellij.cql.psi.scopes.Retrieve

open class Expression(node: ASTNode) : BasePsiNode(node), HasResultType {
    // This should never happen
    override fun getResultType(): DataType? {
        println("Calling Result Type in an Object that should not exist: $this")
        return null
    }
}

class DurationBetweenExpression(node: ASTNode) : Expression(node) {
    fun pluralDateTimePrecision(): PluralDateTimePrecision? {
        return getRule(PluralDateTimePrecision::class.java, 0)
    }

    fun expressionTerm(): List<ExpressionTerm>? {
        return getRules(ExpressionTerm::class.java)
    }

    fun expressionTerm(i: Int): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, i)
    }

    override fun getResultType() = resolveTypeName("System", "Integer")
}

class InFixSetExpression(node: ASTNode) : Expression(node) {
    fun expression(): List<Expression>? {
        return getRules(Expression::class.java)
    }

    fun expression(i: Int): Expression? {
        return getRule(Expression::class.java, i)
    }

    override fun getResultType() = findIncompleteCompatibleType(
        expression(0)?.getResultType(),
        expression(1)?.getResultType()
    )
}

class RetrieveExpression(node: ASTNode) : Expression(node) {
    fun retrieve(): Retrieve? {
        return getRule(Retrieve::class.java, 0)
    }

    override fun getResultType() = retrieve()?.getResultType()
}

class TimingExpression(node: ASTNode) : Expression(node) {
    fun expression(): List<Expression>? {
        return getRules(Expression::class.java)
    }

    fun expression(i: Int): Expression? {
        return getRule(Expression::class.java, i)
    }

    fun intervalOperatorPhrase(): IntervalOperatorPhrase? {
        return getRule(IntervalOperatorPhrase::class.java, 0)
    }

    override fun getResultType() = null // TODO: Estimate ResultType for TimingExpression
}

class QueryExpression(node: ASTNode) : Expression(node) {
    fun query(): Query? {
        return getRule(Query::class.java, 0)
    }

    override fun getResultType() = query()?.getResultType()
}

class NotExpression(node: ASTNode) : Expression(node) {
    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    override fun getResultType() = resolveTypeName("System", "Boolean")
}

class BooleanExpression(node: ASTNode) : Expression(node) {
    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    override fun getResultType() = resolveTypeName("System", "Boolean")
}

class OrExpression(node: ASTNode) : Expression(node) {
    fun expression(): List<Expression>? {
        return getRules(Expression::class.java)
    }

    fun expression(i: Int): Expression? {
        return getRule(Expression::class.java, i)
    }

    override fun getResultType() = resolveTypeName("System", "Boolean")
}

class CastExpression(node: ASTNode) : Expression(node) {
    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    fun typeSpecifier(): TypeSpecifier? {
        return getRule(TypeSpecifier::class.java, 0)
    }

    override fun getResultType() = typeSpecifier()?.resolveType()
}

class AndExpression(node: ASTNode) : Expression(node) {
    fun expression(): List<Expression>? {
        return getRules(Expression::class.java)
    }

    fun expression(i: Int): Expression? {
        return getRule(Expression::class.java, i)
    }

    override fun getResultType() = resolveTypeName("System", "Boolean")
}

class BetweenExpression(node: ASTNode) : Expression(node) {
    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    fun expressionTerm(): List<ExpressionTerm>? {
        return getRules(ExpressionTerm::class.java)
    }

    fun expressionTerm(i: Int): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, i)
    }

    override fun getResultType() = resolveTypeName("System", "Boolean")
}

class MembershipExpression(node: ASTNode) : Expression(node) {
    fun expression(): List<Expression>? {
        return getRules(Expression::class.java)
    }

    fun expression(i: Int): Expression? {
        return getRule(Expression::class.java, i)
    }

    fun dateTimePrecisionSpecifier(): DateTimePrecisionSpecifier? {
        return getRule(DateTimePrecisionSpecifier::class.java, 0)
    }

    override fun getResultType() = resolveTypeName("System", "Boolean")
}

class DifferenceBetweenExpression(node: ASTNode) : Expression(node) {
    fun pluralDateTimePrecision(): PluralDateTimePrecision? {
        return getRule(PluralDateTimePrecision::class.java, 0)
    }

    fun expressionTerm(): List<ExpressionTerm>? {
        return getRules(ExpressionTerm::class.java)
    }

    fun expressionTerm(i: Int): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, i)
    }

    override fun getResultType() = resolveTypeName("System", "Integer")
}

class InequalityExpression(node: ASTNode) : Expression(node) {
    fun expression(): List<Expression>? {
        return getRules(Expression::class.java)
    }

    fun expression(i: Int): Expression? {
        return getRule(Expression::class.java, i)
    }

    override fun getResultType() = resolveTypeName("System", "Boolean")
}

class EqualityExpression(node: ASTNode) : Expression(node) {
    fun expression(): List<Expression>? {
        return getRules(Expression::class.java)
    }

    fun expression(i: Int): Expression? {
        return getRule(Expression::class.java, i)
    }

    override fun getResultType() = resolveTypeName("System", "Boolean")
}

class ExistenceExpression(node: ASTNode) : Expression(node) {
    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    override fun getResultType() = resolveTypeName("System", "Boolean")
}

class ImpliesExpression(node: ASTNode) : Expression(node) {
    fun expression(): List<Expression>? {
        return getRules(Expression::class.java)
    }

    fun expression(i: Int): Expression? {
        return getRule(Expression::class.java, i)
    }

    override fun getResultType() = resolveTypeName("System", "Boolean")
}

class TermExpression(node: ASTNode) : Expression(node) {
    fun expressionTerm(): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, 0)
    }

    override fun getResultType() = expressionTerm()?.getResultType()
}

class TypeExpression(node: ASTNode) : Expression(node) {
    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    fun typeSpecifier(): TypeSpecifier? {
        return getRule(TypeSpecifier::class.java, 0)
    }

    override fun getResultType(): DataType? {
        return if (children[1]?.text == "is")
            resolveTypeName("System", "Boolean")
        else
            typeSpecifier()?.resolveType()
    }
}