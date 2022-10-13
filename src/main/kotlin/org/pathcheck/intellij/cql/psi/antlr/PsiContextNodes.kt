package org.pathcheck.intellij.cql.psi.antlr

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode
import org.cqframework.cql.gen.cqlParser
import org.pathcheck.intellij.cql.psi.Identifier
import org.pathcheck.intellij.cql.psi.definitions.*
import org.pathcheck.intellij.cql.psi.scopes.*

class PsiContextNodes {

    class LocalIdentifier(node: ASTNode) : BasePsiNode(node) {
        fun identifier(): Identifier? {
            return getRule(Identifier::class.java, 0)
        }
    }

    class AccessModifier(node: ASTNode) : BasePsiNode(node)

    class Codesystems(node: ASTNode) : BasePsiNode(node) {
        fun codesystemIdentifier(): List<CodesystemIdentifier>? {
            return getRules(CodesystemIdentifier::class.java)
        }

        fun codesystemIdentifier(i: Int): CodesystemIdentifier? {
            return getRule(CodesystemIdentifier::class.java, i)
        }
    }

    class CodesystemIdentifier(node: ASTNode) : BasePsiNode(node) {
        fun identifier(): Identifier? {
            return getRule(Identifier::class.java, 0)
        }

        fun libraryIdentifier(): LibraryIdentifier? {
            return getRule(LibraryIdentifier::class.java, 0)
        }
    }

    class LibraryIdentifier(node: ASTNode) : BasePsiNode(node) {
        fun identifier(): Identifier? {
            return getRule(Identifier::class.java, 0)
        }
    }

    class CodeIdentifier(node: ASTNode) : BasePsiNode(node) {
        fun identifier(): Identifier? {
            return getRule(Identifier::class.java, 0)
        }

        fun libraryIdentifier(): LibraryIdentifier? {
            return getRule(LibraryIdentifier::class.java, 0)
        }
    }

    class CodesystemId(node: ASTNode) : BasePsiNode(node) {
        fun STRING(): ANTLRPsiLeafNode? {
            return getToken(cqlParser.STRING, 0)
        }
    }

    class ValuesetId(node: ASTNode) : BasePsiNode(node) {
        fun STRING(): ANTLRPsiLeafNode? {
            return getToken(cqlParser.STRING, 0)
        }
    }

    class VersionSpecifier(node: ASTNode) : BasePsiNode(node) {
        fun STRING(): ANTLRPsiLeafNode? {
            return getToken(cqlParser.STRING, 0)
        }
    }

    class CodeId(node: ASTNode) : BasePsiNode(node) {
        fun STRING(): ANTLRPsiLeafNode? {
            return getToken(cqlParser.STRING, 0)
        }
    }

    class TypeSpecifier(node: ASTNode) : BasePsiNode(node) {
        fun namedTypeSpecifier(): NamedTypeSpecifier? {
            return getRule(NamedTypeSpecifier::class.java, 0)
        }

        fun listTypeSpecifier(): ListTypeSpecifier? {
            return getRule(ListTypeSpecifier::class.java, 0)
        }

        fun intervalTypeSpecifier(): IntervalTypeSpecifier? {
            return getRule(IntervalTypeSpecifier::class.java, 0)
        }

        fun tupleTypeSpecifier(): TupleTypeSpecifier? {
            return getRule(TupleTypeSpecifier::class.java, 0)
        }

        fun choiceTypeSpecifier(): ChoiceTypeSpecifier? {
            return getRule(ChoiceTypeSpecifier::class.java, 0)
        }
    }

    class NamedTypeSpecifier(node: ASTNode) : BasePsiNode(node) {
        fun referentialOrTypeNameIdentifier(): ReferentialOrTypeNameIdentifier? {
            return getRule(ReferentialOrTypeNameIdentifier::class.java, 0)
        }

        fun qualifier(): List<Qualifier>? {
            return getRules(Qualifier::class.java)
        }

        fun qualifier(i: Int): Qualifier? {
            return getRule(Qualifier::class.java, i)
        }
    }

    class ModelIdentifier(node: ASTNode) : BasePsiNode(node) {
        fun identifier(): Identifier? {
            return getRule(Identifier::class.java, 0)
        }
    }

    class ListTypeSpecifier(node: ASTNode) : BasePsiNode(node) {
        fun typeSpecifier(): TypeSpecifier? {
            return getRule(TypeSpecifier::class.java, 0)
        }
    }

    class IntervalTypeSpecifier(node: ASTNode) : BasePsiNode(node) {
        fun typeSpecifier(): TypeSpecifier? {
            return getRule(TypeSpecifier::class.java, 0)
        }
    }

    class TupleTypeSpecifier(node: ASTNode) : BasePsiNode(node) {
        fun tupleElementDefinition(): List<TupleElementDefinition>? {
            return getRules(TupleElementDefinition::class.java)
        }

        fun tupleElementDefinition(i: Int): TupleElementDefinition? {
            return getRule(TupleElementDefinition::class.java, i)
        }
    }

    class TupleElementDefinition(node: ASTNode) : BasePsiNode(node) {
        fun referentialIdentifier(): ReferentialIdentifier? {
            return getRule(ReferentialIdentifier::class.java, 0)
        }

        fun typeSpecifier(): TypeSpecifier? {
            return getRule(TypeSpecifier::class.java, 0)
        }
    }

    class ChoiceTypeSpecifier(node: ASTNode) : BasePsiNode(node) {
        fun typeSpecifier(): List<TypeSpecifier>? {
            return getRules(TypeSpecifier::class.java)
        }

        fun typeSpecifier(i: Int): TypeSpecifier? {
            return getRule(TypeSpecifier::class.java, i)
        }
    }

    class Statement(node: ASTNode) : BasePsiNode(node) {
        fun expressionDefinition(): ExpressionDefinition? {
            return getRule(ExpressionDefinition::class.java, 0)
        }

        fun contextDefinition(): ContextDefinition? {
            return getRule(ContextDefinition::class.java, 0)
        }

        fun functionDefinition(): FunctionDefinition? {
            return getRule(FunctionDefinition::class.java, 0)
        }
    }

    class FluentModifier(node: ASTNode) : BasePsiNode(node)

    class OperandDefinition(node: ASTNode) : BasePsiNode(node) {
        fun referentialIdentifier(): ReferentialIdentifier? {
            return getRule(ReferentialIdentifier::class.java, 0)
        }

        fun typeSpecifier(): TypeSpecifier? {
            return getRule(TypeSpecifier::class.java, 0)
        }
    }

    class FunctionBody(node: ASTNode) : BasePsiNode(node) {
        fun expression(): Expression? {
            return getRule(Expression::class.java, 0)
        }
    }

    class QuerySource(node: ASTNode) : BasePsiNode(node) {
        fun retrieve(): Retrieve? {
            return getRule(Retrieve::class.java, 0)
        }

        fun qualifiedIdentifierExpression(): QualifiedIdentifierExpression? {
            return getRule(QualifiedIdentifierExpression::class.java, 0)
        }

        fun expression(): Expression? {
            return getRule(Expression::class.java, 0)
        }
    }

    class AliasedQuerySource(node: ASTNode) : BasePsiNode(node) {
        fun querySource(): QuerySource? {
            return getRule(QuerySource::class.java, 0)
        }

        fun alias(): Alias? {
            return getRule(Alias::class.java, 0)
        }
    }

    class Alias(node: ASTNode) : BasePsiNode(node) {
        fun identifier(): Identifier? {
            return getRule(Identifier::class.java, 0)
        }
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

    class Retrieve(node: ASTNode) : BasePsiNode(node) {
        fun namedTypeSpecifier(): NamedTypeSpecifier? {
            return getRule(NamedTypeSpecifier::class.java, 0)
        }

        fun contextIdentifier(): ContextIdentifier? {
            return getRule(ContextIdentifier::class.java, 0)
        }

        fun terminology(): Terminology? {
            return getRule(Terminology::class.java, 0)
        }

        fun codePath(): CodePath? {
            return getRule(CodePath::class.java, 0)
        }

        fun codeComparator(): CodeComparator? {
            return getRule(CodeComparator::class.java, 0)
        }
    }

    class ContextIdentifier(node: ASTNode) : BasePsiNode(node) {
        fun qualifiedIdentifierExpression(): QualifiedIdentifierExpression? {
            return getRule(QualifiedIdentifierExpression::class.java, 0)
        }
    }

    class CodePath(node: ASTNode) : BasePsiNode(node) {
        fun simplePath(): SimplePath? {
            return getRule(SimplePath::class.java, 0)
        }
    }

    class CodeComparator(node: ASTNode) : BasePsiNode(node)
    class Terminology(node: ASTNode) : BasePsiNode(node) {
        fun qualifiedIdentifierExpression(): QualifiedIdentifierExpression? {
            return getRule(QualifiedIdentifierExpression::class.java, 0)
        }

        fun expression(): Expression? {
            return getRule(Expression::class.java, 0)
        }
    }

    class Qualifier(node: ASTNode) : BasePsiNode(node) {
        fun identifier(): Identifier? {
            return getRule(Identifier::class.java, 0)
        }
    }

    class SourceClause(node: ASTNode) : BasePsiNode(node) {
        fun aliasedQuerySource(): List<AliasedQuerySource>? {
            return getRules(AliasedQuerySource::class.java)
        }

        fun aliasedQuerySource(i: Int): AliasedQuerySource? {
            return getRule(AliasedQuerySource::class.java, i)
        }
    }

    class LetClause(node: ASTNode) : BasePsiNode(node) {
        fun letClauseItem(): List<LetClauseItem>? {
            return getRules(LetClauseItem::class.java)
        }

        fun letClauseItem(i: Int): LetClauseItem? {
            return getRule(LetClauseItem::class.java, i)
        }
    }

    class LetClauseItem(node: ASTNode) : BasePsiNode(node) {
        fun identifier(): Identifier? {
            return getRule(Identifier::class.java, 0)
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

    class ReturnClause(node: ASTNode) : BasePsiNode(node) {
        fun expression(): Expression? {
            return getRule(Expression::class.java, 0)
        }
    }

    class StartingClause(node: ASTNode) : BasePsiNode(node) {
        fun simpleLiteral(): SimpleLiteral? {
            return getRule(SimpleLiteral::class.java, 0)
        }

        fun quantity(): Quantity? {
            return getRule(Quantity::class.java, 0)
        }

        fun expression(): Expression? {
            return getRule(Expression::class.java, 0)
        }
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
    class SortByItem(node: ASTNode) : BasePsiNode(node) {
        fun expressionTerm(): ExpressionTerm? {
            return getRule(ExpressionTerm::class.java, 0)
        }

        fun sortDirection(): SortDirection? {
            return getRule(SortDirection::class.java, 0)
        }
    }

    class QualifiedIdentifier(node: ASTNode) : BasePsiNode(node) {
        fun identifier(): Identifier? {
            return getRule(Identifier::class.java, 0)
        }

        fun qualifier(): List<Qualifier>? {
            return getRules(Qualifier::class.java)
        }

        fun qualifier(i: Int): Qualifier? {
            return getRule(Qualifier::class.java, i)
        }
    }

    class QualifiedIdentifierExpression(node: ASTNode) : BasePsiNode(node) {
        fun referentialIdentifier(): ReferentialIdentifier? {
            return getRule(ReferentialIdentifier::class.java, 0)
        }

        fun qualifierExpression(): List<QualifierExpression>? {
            return getRules(QualifierExpression::class.java)
        }

        fun qualifierExpression(i: Int): QualifierExpression? {
            return getRule(QualifierExpression::class.java, i)
        }
    }

    class QualifierExpression(node: ASTNode) : BasePsiNode(node) {
        fun referentialIdentifier(): ReferentialIdentifier? {
            return getRule(ReferentialIdentifier::class.java, 0)
        }
    }

    open class SimplePath(node: ASTNode) : BasePsiNode(node)
    class SimplePathIndexer(node: ASTNode) : SimplePath(node) {
        fun simplePath(): SimplePath? {
            return getRule(SimplePath::class.java, 0)
        }

        fun simpleLiteral(): SimpleLiteral? {
            return getRule(SimpleLiteral::class.java, 0)
        }
    }

    class SimplePathQualifiedIdentifier(node: ASTNode) : SimplePath(node) {
        fun simplePath(): SimplePath? {
            return getRule(SimplePath::class.java, 0)
        }

        fun referentialIdentifier(): ReferentialIdentifier? {
            return getRule(ReferentialIdentifier::class.java, 0)
        }
    }

    class SimplePathReferentialIdentifier(node: ASTNode) : SimplePath(node) {
        fun referentialIdentifier(): ReferentialIdentifier? {
            return getRule(ReferentialIdentifier::class.java, 0)
        }
    }

    open class SimpleLiteral(node: ASTNode) : BasePsiNode(node)
    class SimpleNumberLiteral(node: ASTNode) : SimpleLiteral(node) {
        fun NUMBER(): ANTLRPsiLeafNode? {
            return getToken(cqlParser.NUMBER, 0)
        }
    }

    class SimpleStringLiteral(node: ASTNode) : SimpleLiteral(node) {
        fun STRING(): ANTLRPsiLeafNode? {
            return getToken(cqlParser.STRING, 0)
        }
    }

    open class Expression(node: ASTNode) : BasePsiNode(node)
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
    }

    class InFixSetExpression(node: ASTNode) : Expression(node) {
        fun expression(): List<Expression>? {
            return getRules(Expression::class.java)
        }

        fun expression(i: Int): Expression? {
            return getRule(Expression::class.java, i)
        }
    }

    class RetrieveExpression(node: ASTNode) : Expression(node) {
        fun retrieve(): Retrieve? {
            return getRule(Retrieve::class.java, 0)
        }
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
    }

    class QueryExpression(node: ASTNode) : Expression(node) {
        fun query(): Query? {
            return getRule(Query::class.java, 0)
        }
    }

    class NotExpression(node: ASTNode) : Expression(node) {
        fun expression(): Expression? {
            return getRule(Expression::class.java, 0)
        }
    }

    class BooleanExpression(node: ASTNode) : Expression(node) {
        fun expression(): Expression? {
            return getRule(Expression::class.java, 0)
        }
    }

    class OrExpression(node: ASTNode) : Expression(node) {
        fun expression(): List<Expression>? {
            return getRules(Expression::class.java)
        }

        fun expression(i: Int): Expression? {
            return getRule(Expression::class.java, i)
        }
    }

    class CastExpression(node: ASTNode) : Expression(node) {
        fun expression(): Expression? {
            return getRule(Expression::class.java, 0)
        }

        fun typeSpecifier(): TypeSpecifier? {
            return getRule(TypeSpecifier::class.java, 0)
        }
    }

    class AndExpression(node: ASTNode) : Expression(node) {
        fun expression(): List<Expression>? {
            return getRules(Expression::class.java)
        }

        fun expression(i: Int): Expression? {
            return getRule(Expression::class.java, i)
        }
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
    }

    class InequalityExpression(node: ASTNode) : Expression(node) {
        fun expression(): List<Expression>? {
            return getRules(Expression::class.java)
        }

        fun expression(i: Int): Expression? {
            return getRule(Expression::class.java, i)
        }
    }

    class EqualityExpression(node: ASTNode) : Expression(node) {
        fun expression(): List<Expression>? {
            return getRules(Expression::class.java)
        }

        fun expression(i: Int): Expression? {
            return getRule(Expression::class.java, i)
        }
    }

    class ExistenceExpression(node: ASTNode) : Expression(node) {
        fun expression(): Expression? {
            return getRule(Expression::class.java, 0)
        }
    }

    class ImpliesExpression(node: ASTNode) : Expression(node) {
        fun expression(): List<Expression>? {
            return getRules(Expression::class.java)
        }

        fun expression(i: Int): Expression? {
            return getRule(Expression::class.java, i)
        }
    }

    class TermExpression(node: ASTNode) : Expression(node) {
        fun expressionTerm(): ExpressionTerm? {
            return getRule(ExpressionTerm::class.java, 0)
        }
    }

    class TypeExpression(node: ASTNode) : Expression(node) {
        fun expression(): Expression? {
            return getRule(Expression::class.java, 0)
        }

        fun typeSpecifier(): TypeSpecifier? {
            return getRule(TypeSpecifier::class.java, 0)
        }
    }

    class DateTimePrecision(node: ASTNode) : BasePsiNode(node)
    class DateTimeComponent(node: ASTNode) : BasePsiNode(node) {
        fun dateTimePrecision(): DateTimePrecision? {
            return getRule(DateTimePrecision::class.java, 0)
        }
    }

    class PluralDateTimePrecision(node: ASTNode) : BasePsiNode(node)
    open class ExpressionTerm(node: ASTNode) : BasePsiNode(node)
    class AdditionExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun expressionTerm(): List<ExpressionTerm>? {
            return getRules(ExpressionTerm::class.java)
        }

        fun expressionTerm(i: Int): ExpressionTerm? {
            return getRule(ExpressionTerm::class.java, i)
        }
    }

    class IndexedExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun expressionTerm(): ExpressionTerm? {
            return getRule(ExpressionTerm::class.java, 0)
        }

        fun expression(): Expression? {
            return getRule(Expression::class.java, 0)
        }
    }

    class WidthExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun expressionTerm(): ExpressionTerm? {
            return getRule(ExpressionTerm::class.java, 0)
        }
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
    }

    class TimeUnitExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun dateTimeComponent(): DateTimeComponent? {
            return getRule(DateTimeComponent::class.java, 0)
        }

        fun expressionTerm(): ExpressionTerm? {
            return getRule(ExpressionTerm::class.java, 0)
        }
    }

    class IfThenElseExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun expression(): List<Expression>? {
            return getRules(Expression::class.java)
        }

        fun expression(i: Int): Expression? {
            return getRule(Expression::class.java, i)
        }
    }

    class TimeBoundaryExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun expressionTerm(): ExpressionTerm? {
            return getRule(ExpressionTerm::class.java, 0)
        }
    }

    class ElementExtractorExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun expressionTerm(): ExpressionTerm? {
            return getRule(ExpressionTerm::class.java, 0)
        }
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
    }

    class TypeExtentExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun namedTypeSpecifier(): NamedTypeSpecifier? {
            return getRule(NamedTypeSpecifier::class.java, 0)
        }
    }

    class PredecessorExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun expressionTerm(): ExpressionTerm? {
            return getRule(ExpressionTerm::class.java, 0)
        }
    }

    class PointExtractorExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun expressionTerm(): ExpressionTerm? {
            return getRule(ExpressionTerm::class.java, 0)
        }
    }

    class MultiplicationExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun expressionTerm(): List<ExpressionTerm>? {
            return getRules(ExpressionTerm::class.java)
        }

        fun expressionTerm(i: Int): ExpressionTerm? {
            return getRule(ExpressionTerm::class.java, i)
        }
    }

    class AggregateExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun expression(): Expression? {
            return getRule(Expression::class.java, 0)
        }
    }

    class DurationExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun pluralDateTimePrecision(): PluralDateTimePrecision? {
            return getRule(PluralDateTimePrecision::class.java, 0)
        }

        fun expressionTerm(): ExpressionTerm? {
            return getRule(ExpressionTerm::class.java, 0)
        }
    }

    class DifferenceExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun pluralDateTimePrecision(): PluralDateTimePrecision? {
            return getRule(PluralDateTimePrecision::class.java, 0)
        }

        fun expressionTerm(): ExpressionTerm? {
            return getRule(ExpressionTerm::class.java, 0)
        }
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
    }

    class PowerExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun expressionTerm(): List<ExpressionTerm>? {
            return getRules(ExpressionTerm::class.java)
        }

        fun expressionTerm(i: Int): ExpressionTerm? {
            return getRule(ExpressionTerm::class.java, i)
        }
    }

    class SuccessorExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun expressionTerm(): ExpressionTerm? {
            return getRule(ExpressionTerm::class.java, 0)
        }
    }

    class PolarityExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun expressionTerm(): ExpressionTerm? {
            return getRule(ExpressionTerm::class.java, 0)
        }
    }

    class TermExpressionTerm(node: ASTNode) : ExpressionTerm(node) {
        fun term(): Term? {
            return getRule(Term::class.java, 0)
        }
    }

    class CaseExpressionItem(node: ASTNode) : BasePsiNode(node) {
        fun expression(): List<Expression>? {
            return getRules(Expression::class.java)
        }

        fun expression(i: Int): Expression? {
            return getRule(Expression::class.java, i)
        }
    }

    class DateTimePrecisionSpecifier(node: ASTNode) : BasePsiNode(node) {
        fun dateTimePrecision(): DateTimePrecision? {
            return getRule(DateTimePrecision::class.java, 0)
        }
    }

    class RelativeQualifier(node: ASTNode) : BasePsiNode(node)
    class OffsetRelativeQualifier(node: ASTNode) : BasePsiNode(node)
    class ExclusiveRelativeQualifier(node: ASTNode) : BasePsiNode(node)
    class QuantityOffset(node: ASTNode) : BasePsiNode(node) {
        fun quantity(): Quantity? {
            return getRule(Quantity::class.java, 0)
        }

        fun offsetRelativeQualifier(): OffsetRelativeQualifier? {
            return getRule(OffsetRelativeQualifier::class.java, 0)
        }

        fun exclusiveRelativeQualifier(): ExclusiveRelativeQualifier? {
            return getRule(ExclusiveRelativeQualifier::class.java, 0)
        }
    }

    class TemporalRelationship(node: ASTNode) : BasePsiNode(node)
    open class IntervalOperatorPhrase(node: ASTNode) : BasePsiNode(node)
    class WithinIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
        fun quantity(): Quantity? {
            return getRule(Quantity::class.java, 0)
        }
    }

    class IncludedInIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
        fun dateTimePrecisionSpecifier(): DateTimePrecisionSpecifier? {
            return getRule(DateTimePrecisionSpecifier::class.java, 0)
        }
    }

    class EndsIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
        fun dateTimePrecisionSpecifier(): DateTimePrecisionSpecifier? {
            return getRule(DateTimePrecisionSpecifier::class.java, 0)
        }
    }

    class ConcurrentWithIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
        fun relativeQualifier(): RelativeQualifier? {
            return getRule(RelativeQualifier::class.java, 0)
        }

        fun dateTimePrecision(): DateTimePrecision? {
            return getRule(DateTimePrecision::class.java, 0)
        }
    }

    class OverlapsIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
        fun dateTimePrecisionSpecifier(): DateTimePrecisionSpecifier? {
            return getRule(DateTimePrecisionSpecifier::class.java, 0)
        }
    }

    class IncludesIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
        fun dateTimePrecisionSpecifier(): DateTimePrecisionSpecifier? {
            return getRule(DateTimePrecisionSpecifier::class.java, 0)
        }
    }

    class BeforeOrAfterIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
        fun temporalRelationship(): TemporalRelationship? {
            return getRule(TemporalRelationship::class.java, 0)
        }

        fun quantityOffset(): QuantityOffset? {
            return getRule(QuantityOffset::class.java, 0)
        }

        fun dateTimePrecisionSpecifier(): DateTimePrecisionSpecifier? {
            return getRule(DateTimePrecisionSpecifier::class.java, 0)
        }
    }

    class MeetsIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
        fun dateTimePrecisionSpecifier(): DateTimePrecisionSpecifier? {
            return getRule(DateTimePrecisionSpecifier::class.java, 0)
        }
    }

    class StartsIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
        fun dateTimePrecisionSpecifier(): DateTimePrecisionSpecifier? {
            return getRule(DateTimePrecisionSpecifier::class.java, 0)
        }
    }

    open class Term(node: ASTNode) : BasePsiNode(node)
    class ExternalConstantTerm(node: ASTNode) : Term(node) {
        fun externalConstant(): ExternalConstant? {
            return getRule(ExternalConstant::class.java, 0)
        }
    }

    class TupleSelectorTerm(node: ASTNode) : Term(node) {
        fun tupleSelector(): TupleSelector? {
            return getRule(TupleSelector::class.java, 0)
        }
    }

    class LiteralTerm(node: ASTNode) : Term(node) {
        fun literal(): Literal? {
            return getRule(Literal::class.java, 0)
        }
    }

    class ConceptSelectorTerm(node: ASTNode) : Term(node) {
        fun conceptSelector(): ConceptSelector? {
            return getRule(ConceptSelector::class.java, 0)
        }
    }

    class ParenthesizedTerm(node: ASTNode) : Term(node) {
        fun expression(): Expression? {
            return getRule(Expression::class.java, 0)
        }
    }

    class CodeSelectorTerm(node: ASTNode) : Term(node) {
        fun codeSelector(): CodeSelector? {
            return getRule(CodeSelector::class.java, 0)
        }
    }

    class InvocationTerm(node: ASTNode) : Term(node) {
        fun invocation(): Invocation? {
            return getRule(Invocation::class.java, 0)
        }
    }

    class InstanceSelectorTerm(node: ASTNode) : Term(node) {
        fun instanceSelector(): InstanceSelector? {
            return getRule(InstanceSelector::class.java, 0)
        }
    }

    class IntervalSelectorTerm(node: ASTNode) : Term(node) {
        fun intervalSelector(): IntervalSelector? {
            return getRule(IntervalSelector::class.java, 0)
        }
    }

    class ListSelectorTerm(node: ASTNode) : Term(node) {
        fun listSelector(): ListSelector? {
            return getRule(ListSelector::class.java, 0)
        }
    }

    open class QualifiedInvocation(node: ASTNode) : BasePsiNode(node)

    class QualifiedFunction(node: ASTNode) : BasePsiNode(node) {
        fun identifierOrFunctionIdentifier(): IdentifierOrFunctionIdentifier? {
            return getRule(IdentifierOrFunctionIdentifier::class.java, 0)
        }

        fun paramList(): ParamList? {
            return getRule(ParamList::class.java, 0)
        }
    }

    open class Invocation(node: ASTNode) : BasePsiNode(node)
    class TotalInvocation(node: ASTNode) : Invocation(node)
    class ThisInvocation(node: ASTNode) : Invocation(node)
    class IndexInvocation(node: ASTNode) : Invocation(node)
    class FunctionInvocation(node: ASTNode) : Invocation(node) {
        fun function(): Function? {
            return getRule(Function::class.java, 0)
        }
    }

    class MemberInvocation(node: ASTNode) : Invocation(node) {
        fun referentialIdentifier(): ReferentialIdentifier? {
            return getRule(ReferentialIdentifier::class.java, 0)
        }
    }

    class Function(node: ASTNode) : BasePsiNode(node) {
        fun referentialIdentifier(): ReferentialIdentifier? {
            return getRule(ReferentialIdentifier::class.java, 0)
        }

        fun paramList(): ParamList? {
            return getRule(ParamList::class.java, 0)
        }
    }

    class Ratio(node: ASTNode) : BasePsiNode(node) {
        fun quantity(): List<Quantity>? {
            return getRules(Quantity::class.java)
        }

        fun quantity(i: Int): Quantity? {
            return getRule(Quantity::class.java, i)
        }
    }

    open class Literal(node: ASTNode) : BasePsiNode(node)
    class TimeLiteral(node: ASTNode) : Literal(node) {
        fun TIME(): ANTLRPsiLeafNode? {
            return getToken(cqlParser.TIME, 0)
        }
    }

    class NullLiteral(node: ASTNode) : Literal(node)
    class RatioLiteral(node: ASTNode) : Literal(node) {
        fun ratio(): Ratio? {
            return getRule(Ratio::class.java, 0)
        }
    }

    class DateTimeLiteral(node: ASTNode) : Literal(node) {
        fun DATETIME(): ANTLRPsiLeafNode? {
            return getToken(cqlParser.DATETIME, 0)
        }
    }

    class StringLiteral(node: ASTNode) : Literal(node) {
        fun STRING(): ANTLRPsiLeafNode? {
            return getToken(cqlParser.STRING, 0)
        }
    }

    class DateLiteral(node: ASTNode) : Literal(node) {
        fun DATE(): ANTLRPsiLeafNode? {
            return getToken(cqlParser.DATE, 0)
        }
    }

    class BooleanLiteral(node: ASTNode) : Literal(node)
    class NumberLiteral(node: ASTNode) : Literal(node) {
        fun NUMBER(): ANTLRPsiLeafNode? {
            return getToken(cqlParser.NUMBER, 0)
        }
    }

    class LongNumberLiteral(node: ASTNode) : Literal(node) {
        fun LONGNUMBER(): ANTLRPsiLeafNode? {
            return getToken(cqlParser.LONGNUMBER, 0)
        }
    }

    class QuantityLiteral(node: ASTNode) : Literal(node) {
        fun quantity(): Quantity? {
            return getRule(Quantity::class.java, 0)
        }
    }

    class IntervalSelector(node: ASTNode) : BasePsiNode(node) {
        fun expression(): List<Expression>? {
            return getRules(Expression::class.java)
        }

        fun expression(i: Int): Expression? {
            return getRule(Expression::class.java, i)
        }
    }

    class TupleSelector(node: ASTNode) : BasePsiNode(node) {
        fun tupleElementSelector(): List<TupleElementSelector>? {
            return getRules(TupleElementSelector::class.java)
        }

        fun tupleElementSelector(i: Int): TupleElementSelector? {
            return getRule(TupleElementSelector::class.java, i)
        }
    }

    class TupleElementSelector(node: ASTNode) : BasePsiNode(node) {
        fun referentialIdentifier(): ReferentialIdentifier? {
            return getRule(ReferentialIdentifier::class.java, 0)
        }

        fun expression(): Expression? {
            return getRule(Expression::class.java, 0)
        }
    }

    class InstanceSelector(node: ASTNode) : BasePsiNode(node) {
        fun namedTypeSpecifier(): NamedTypeSpecifier? {
            return getRule(NamedTypeSpecifier::class.java, 0)
        }

        fun instanceElementSelector(): List<InstanceElementSelector>? {
            return getRules(InstanceElementSelector::class.java)
        }

        fun instanceElementSelector(i: Int): InstanceElementSelector? {
            return getRule(InstanceElementSelector::class.java, i)
        }
    }

    class InstanceElementSelector(node: ASTNode) : BasePsiNode(node) {
        fun referentialIdentifier(): ReferentialIdentifier? {
            return getRule(ReferentialIdentifier::class.java, 0)
        }

        fun expression(): Expression? {
            return getRule(Expression::class.java, 0)
        }
    }

    class ListSelector(node: ASTNode) : BasePsiNode(node) {
        fun expression(): List<Expression>? {
            return getRules(Expression::class.java)
        }

        fun expression(i: Int): Expression? {
            return getRule(Expression::class.java, i)
        }

        fun typeSpecifier(): TypeSpecifier? {
            return getRule(TypeSpecifier::class.java, 0)
        }
    }

    class DisplayClause(node: ASTNode) : BasePsiNode(node) {
        fun STRING(): ANTLRPsiLeafNode? {
            return getToken(cqlParser.STRING, 0)
        }
    }

    class CodeSelector(node: ASTNode) : BasePsiNode(node) {
        fun STRING(): ANTLRPsiLeafNode? {
            return getToken(cqlParser.STRING, 0)
        }

        fun codesystemIdentifier(): CodesystemIdentifier? {
            return getRule(CodesystemIdentifier::class.java, 0)
        }

        fun displayClause(): DisplayClause? {
            return getRule(DisplayClause::class.java, 0)
        }
    }

    class ConceptSelector(node: ASTNode) : BasePsiNode(node) {
        fun codeSelector(): List<CodeSelector>? {
            return getRules(CodeSelector::class.java)
        }

        fun codeSelector(i: Int): CodeSelector? {
            return getRule(CodeSelector::class.java, i)
        }

        fun displayClause(): DisplayClause? {
            return getRule(DisplayClause::class.java, 0)
        }
    }

    class Keyword(node: ASTNode) : BasePsiNode(node)
    class ReservedWord(node: ASTNode) : BasePsiNode(node)
    class KeywordIdentifier(node: ASTNode) : BasePsiNode(node)
    class ObsoleteIdentifier(node: ASTNode) : BasePsiNode(node)
    class FunctionIdentifier(node: ASTNode) : BasePsiNode(node)
    class TypeNameIdentifier(node: ASTNode) : BasePsiNode(node)
    class ReferentialIdentifier(node: ASTNode) : BasePsiNode(node) {
        fun identifier(): Identifier? {
            return getRule(Identifier::class.java, 0)
        }

        fun keywordIdentifier(): KeywordIdentifier? {
            return getRule(KeywordIdentifier::class.java, 0)
        }
    }

    class ReferentialOrTypeNameIdentifier(node: ASTNode) : BasePsiNode(node) {
        fun referentialIdentifier(): ReferentialIdentifier? {
            return getRule(ReferentialIdentifier::class.java, 0)
        }

        fun typeNameIdentifier(): TypeNameIdentifier? {
            return getRule(TypeNameIdentifier::class.java, 0)
        }
    }

    class IdentifierOrFunctionIdentifier(node: ASTNode) : BasePsiNode(node) {
        fun identifier(): Identifier? {
            return getRule(Identifier::class.java, 0)
        }

        fun functionIdentifier(): FunctionIdentifier? {
            return getRule(FunctionIdentifier::class.java, 0)
        }
    }

    class ExternalConstant(node: ASTNode) : BasePsiNode(node) {
        fun identifier(): Identifier? {
            return getRule(Identifier::class.java, 0)
        }

        fun STRING(): ANTLRPsiLeafNode? {
            return getToken(cqlParser.STRING, 0)
        }
    }

    class ParamList(node: ASTNode) : BasePsiNode(node) {
        fun expression(): List<Expression>? {
            return getRules(Expression::class.java)
        }

        fun expression(i: Int): Expression? {
            return getRule(Expression::class.java, i)
        }
    }

    class Quantity(node: ASTNode) : BasePsiNode(node) {
        fun NUMBER(): ANTLRPsiLeafNode? {
            return getToken(cqlParser.NUMBER, 0)
        }

        fun unit(): Unit? {
            return getRule(Unit::class.java, 0)
        }
    }

    class Unit(node: ASTNode) : BasePsiNode(node) {
        fun dateTimePrecision(): DateTimePrecision? {
            return getRule(DateTimePrecision::class.java, 0)
        }

        fun pluralDateTimePrecision(): PluralDateTimePrecision? {
            return getRule(PluralDateTimePrecision::class.java, 0)
        }

        fun STRING(): ANTLRPsiLeafNode? {
            return getToken(cqlParser.STRING, 0)
        }
    }
}