package org.pathcheck.intellij.cql.psi.expressions

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.hl7.cql.model.DataType
import org.pathcheck.intellij.cql.psi.HasQualifier
import org.pathcheck.intellij.cql.psi.HasResultType
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.scopes.QualifiedInvocation
import org.pathcheck.intellij.cql.psi.scopes.QualifierExpression

open class ExpressionTerm(node: ASTNode) : BasePsiNode(node), HasResultType {
    // This should never happen
    override fun getResultType(): DataType? {
        println("Calling Result Type in an Object that should not exist: $this")
        return null
    }
}

class InvocationExpressionTerm(node: ASTNode) : ExpressionTerm(node), HasQualifier {
    fun expressionTerm(): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, 0)
    }

    fun qualifiedInvocation(): QualifiedInvocation? {
        return getRule(QualifiedInvocation::class.java, 0)
    }

    override fun getQualifier(): DataType? {
        return expressionTerm()?.getResultType()
    }

    override fun getResultType() = qualifiedInvocation()?.getResultType()
}

class AdditionExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun expressionTerm(): List<ExpressionTerm>? {
        return getRules(ExpressionTerm::class.java)
    }

    fun expressionTerm(i: Int): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, i)
    }

    override fun getResultType() = findIncompleteCompatibleType(
        expressionTerm(0)?.getResultType(),
        expressionTerm(1)?.getResultType()
    )
}

class IndexedExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun expressionTerm(): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, 0)
    }

    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    override fun getResultType() = innerElementIfExists(expressionTerm()?.getResultType())
}

class WidthExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun expressionTerm(): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, 0)
    }

    override fun getResultType() = innerElementIfExists(expressionTerm()?.getResultType())
}

class SetAggregateExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun expression(): List<Expression>? {
        return getRules(Expression::class.java)
    }

    fun expression(i: Int): Expression? {
        return getRule(Expression::class.java, i)
    }

    fun dateTimePrecision(): DateTimePrecision? {
        return getRule(DateTimePrecision::class.java, 0)
    }

    override fun getResultType() = null // TODO: Estimate SetAggregateExpressionTerm's return type
}

class TimeUnitExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun dateTimeComponent(): DateTimeComponent? {
        return getRule(DateTimeComponent::class.java, 0)
    }

    fun expressionTerm(): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, 0)
    }

    override fun getResultType() = when (dateTimeComponent()?.text) {
        "date" -> resolveTypeName("System", "Date")
        "time" -> resolveTypeName("System", "Time")
        "timezone" -> resolveTypeName("System", "Decimal")
        "timezoneoffset" -> resolveTypeName("System", "Decimal")
        else -> resolveTypeName("System", "Integer")
    }
}

class IfThenElseExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun expression(): List<Expression>? {
        return getRules(Expression::class.java)
    }

    fun expression(i: Int): Expression? {
        return getRule(Expression::class.java, i)
    }

    override fun getResultType() = findIncompleteCompatibleType(
        expression(1)?.getResultType(),
        expression(2)?.getResultType()
    )
}

class TimeBoundaryExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun expressionTerm(): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, 0)
    }

    override fun getResultType() = innerElementIfExists(expressionTerm()?.getResultType())
}

class ElementExtractorExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun expressionTerm(): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, 0)
    }

    override fun getResultType() = innerElementIfExists(expressionTerm()?.getResultType())
}

class ConversionExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    fun typeSpecifier(): TypeSpecifier? {
        return getRule(TypeSpecifier::class.java, 0)
    }

    fun unit(): Unit? {
        return getRule(Unit::class.java, 0)
    }

    override fun getResultType(): DataType? {
       return typeSpecifier()?.resolveType() ?: resolveTypeName("System", "Quantity")
    }
}

class TypeExtentExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun namedTypeSpecifier(): NamedTypeSpecifier? {
        return getRule(NamedTypeSpecifier::class.java, 0)
    }

    override fun getResultType() = namedTypeSpecifier()?.resolveType()
}

class PredecessorExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun expressionTerm(): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, 0)
    }

    override fun getResultType() = expressionTerm()?.getResultType()
}

class PointExtractorExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun expressionTerm(): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, 0)
    }

    override fun getResultType() = innerElementIfExists(expressionTerm()?.getResultType())
}

class MultiplicationExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun expressionTerm(): List<ExpressionTerm>? {
        return getRules(ExpressionTerm::class.java)
    }

    fun expressionTerm(i: Int): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, i)
    }

    override fun getResultType() = findIncompleteCompatibleType(
        expressionTerm(0)?.getResultType(),
        expressionTerm(1)?.getResultType()
    )
}

class AggregateExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    override fun getResultType() = expression()?.getResultType()
}

class DurationExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun pluralDateTimePrecision(): PluralDateTimePrecision? {
        return getRule(PluralDateTimePrecision::class.java, 0)
    }

    fun expressionTerm(): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, 0)
    }

    override fun getResultType() = resolveTypeName("System", "Integer")
}

class DifferenceExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun pluralDateTimePrecision(): PluralDateTimePrecision? {
        return getRule(PluralDateTimePrecision::class.java, 0)
    }

    fun expressionTerm(): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, 0)
    }

    override fun getResultType() = resolveTypeName("System", "Integer")
}

class CaseExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun expression(): List<Expression>? {
        return getRules(Expression::class.java)
    }

    fun expression(i: Int): Expression? {
        return getRule(Expression::class.java, i)
    }

    fun caseExpressionItem(): List<CaseExpressionItem>? {
        return getRules(CaseExpressionItem::class.java)
    }

    fun caseExpressionItem(i: Int): CaseExpressionItem? {
        return getRule(CaseExpressionItem::class.java, i)
    }

    // uses just the first element to determine the type
    override fun getResultType() = caseExpressionItem(0)?.getResultType()
}

class CaseExpressionItem(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun expression(): List<Expression>? {
        return getRules(Expression::class.java)
    }

    fun expression(i: Int): Expression? {
        return getRule(Expression::class.java, i)
    }

    fun whenExp(): Expression? {
        return expression(0)
    }

    fun thenExp(): Expression? {
        return expression(1)
    }

    override fun getResultType() = thenExp()?.getResultType()
}

class PowerExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun expressionTerm(): List<ExpressionTerm>? {
        return getRules(ExpressionTerm::class.java)
    }

    fun expressionTerm(i: Int): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, i)
    }

    override fun getResultType() = findIncompleteCompatibleType(
        expressionTerm(0)?.getResultType(),
        expressionTerm(1)?.getResultType()
    )
}

class SuccessorExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun expressionTerm(): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, 0)
    }

    override fun getResultType() = expressionTerm()?.getResultType()
}

class PolarityExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun expressionTerm(): ExpressionTerm? {
        return getRule(ExpressionTerm::class.java, 0)
    }

    override fun getResultType() = expressionTerm()?.getResultType()
}

class TermExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
    fun term(): Term? {
        return getRule(Term::class.java, 0)
    }

    override fun getResultType() = term()?.getResultType()
}

class DateTimeComponent(node: ASTNode) : BasePsiNode(node) {
    fun dateTimePrecision(): DateTimePrecision? {
        return getRule(DateTimePrecision::class.java, 0)
    }
}