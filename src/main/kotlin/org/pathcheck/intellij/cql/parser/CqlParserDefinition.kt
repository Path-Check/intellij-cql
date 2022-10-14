package org.pathcheck.intellij.cql.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor
import org.antlr.intellij.adaptor.lexer.RuleIElementType
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import org.antlr.intellij.adaptor.parser.ANTLRParseTreeToPSIConverter
import org.antlr.intellij.adaptor.parser.ANTLRParserAdaptor
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.tree.ParseTree
import org.cqframework.cql.gen.cqlParser
import org.pathcheck.intellij.cql.CqlLanguage
import org.pathcheck.intellij.cql.CqlTokenTypes
import org.pathcheck.intellij.cql.psi.CqlFileRoot
import org.pathcheck.intellij.cql.psi.Library
import org.pathcheck.intellij.cql.psi.definitions.*
import org.pathcheck.intellij.cql.psi.expressions.*
import org.pathcheck.intellij.cql.psi.expressions.Unit
import org.pathcheck.intellij.cql.psi.references.*
import org.pathcheck.intellij.cql.psi.scopes.*
import org.pathcheck.intellij.cql.psi.scopes.Function
import org.pathcheck.intellij.cql.psi.statements.*

class CqlParserDefinition : ParserDefinition {
    override fun createLexer(project: Project): Lexer {
        val lexer = AdaptedCqlLexer(null)
        lexer.removeErrorListeners()
        return ANTLRLexerAdaptor(CqlLanguage, lexer)
    }

    override fun createParser(project: Project): PsiParser {
        val parser = cqlParser(null)
        return object : ANTLRParserAdaptor(CqlLanguage, parser) {
            override fun parse(parser: Parser, root: IElementType?): ParseTree {
                // start rule depends on root passed in; sometimes we want to create an ID node etc...
                if (root is IFileElementType) {
                    return (parser as cqlParser).library()
                }

                // start rule depends on root passed in; sometimes we want to create an ID node etc...
                if (root is TokenIElementType) {
                    return (parser as cqlParser).identifier()
                }

                throw UnsupportedOperationException("Can't parse ${root?.javaClass?.name}")
            }

            override fun createListener(
                parser: Parser?,
                root: IElementType?,
                builder: PsiBuilder?
            ): ANTLRParseTreeToPSIConverter? {
                return AntlrContextToPSIConverter(language, parser, builder)
            }
        }
    }

    /** "Tokens of those types are automatically skipped by PsiBuilder."  */
    override fun getWhitespaceTokens(): TokenSet {
        return CqlTokenTypes.WHITESPACES
    }

    override fun getCommentTokens(): TokenSet {
        return CqlTokenTypes.COMMENTS
    }

    override fun getStringLiteralElements(): TokenSet {
        return CqlTokenTypes.STRINGS
    }

    /** What is the IFileElementType of the root parse tree node? It
     * is called from [.createFile] at least.
     */
    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    /** Create the root of your PSI tree (a PsiFile).
     *
     * From IntelliJ IDEA Architectural Overview:
     * "A PSI (Program Structure Interface) file is the root of a structure
     * representing the contents of a file as a hierarchy of elements
     * in a particular programming language."
     *
     * PsiFile is to be distinguished from a FileASTNode, which is a parse
     * tree node that eventually becomes a PsiFile. From PsiFile, we can get
     * it back via: [PsiFile.getNode].
     */
    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return CqlFileRoot(viewProvider)
    }

    /** Convert from *NON-LEAF* parse node (AST they call it)
     * to PSI node. Leaves are created in the AST factory.
     * Rename re-factoring can cause this to be
     * called on a TokenIElementType since we want to rename ID nodes.
     * In that case, this method is called to create the root node
     * but with ID type. Kind of strange, but we can simply create a
     * ASTWrapperPsiElement to make everything work correctly.
     *
     * RuleIElementType.  Ah! It's that ID is the root
     * IElementType requested to parse, which means that the root
     * node returned from parse tree->PSI conversion.  But, it
     * must be a CompositeElement! The adaptor calls
     * rootMarker.done(root) to finish off the PSI conversion.
     * See [ANTLRParserAdaptor.parse]
     *
     * If you don't care to distinguish PSI nodes by type, it is
     * sufficient to create a [ANTLRPsiNode] around
     * the parse tree node
     */
    override fun createElement(node: ASTNode): PsiElement {
        val elType = node.elementType
        if (elType is TokenIElementType) {
            return ANTLRPsiNode(node)
        }
        if (elType !is RuleIElementType) {
            return ANTLRPsiNode(node)
        }

        return when(CqlRuleTypes.RULE_NAMES[elType.ruleIndex]) {
            // Root
            "Library" -> Library(node)

            // Library Definition
            "LibraryDefinition" -> LibraryDefinition(node)

            // Definitions
            "Definition" -> Definition(node)
            "UsingDefinition" -> UsingDefinition(node)
            "IncludeDefinition" -> IncludeDefinition(node)
            "CodesystemDefinition" -> CodesystemDefinition(node)
            "ValuesetDefinition" -> ValuesetDefinition(node)
            "CodeDefinition" -> CodeDefinition(node)
            "ConceptDefinition" -> ConceptDefinition(node)
            "ParameterDefinition" -> ParameterDefinition(node)

            "AccessModifier" -> AccessModifier(node)
            "FluentModifier" -> FluentModifier(node)

            "QualifiedIdentifier" -> QualifiedIdentifier(node)
            "Qualifier" -> Qualifier(node)
            "VersionSpecifier" -> VersionSpecifier(node)
            "LocalIdentifier" -> LocalIdentifier(node)

            "CodesystemId" -> CodesystemId(node)
            "ValuesetId" -> ValuesetId(node)
            "Codesystems" -> Codesystems(node)
            "CodesystemIdentifier" -> CodesystemIdentifier(node)
            "LibraryIdentifier" -> LibraryIdentifier(node)
            "CodeId" -> CodeId(node)
            "DisplayClause" -> DisplayClause(node)
            "CodeIdentifier" -> CodeIdentifier(node)

            // Statements
            "Statement" -> Statement(node)
            "ExpressionDefinition" -> ExpressionDefinition(node)
            "ContextDefinition" -> ContextDefinition(node)
            "FunctionDefinition" -> FunctionDefinition(node)
            "OperandDefinition" -> OperandDefinition(node)
            "FunctionBody" -> FunctionBody(node)

            // Type Aware
            "InvocationExpressionTerm" -> InvocationExpressionTerm(node)
            "QualifiedMemberInvocation" -> QualifiedMemberInvocation(node)
            "QualifiedFunctionInvocation" -> QualifiedFunctionInvocation(node)

            // Retrieve
            "Retrieve" -> Retrieve(node)
            "ContextIdentifier" -> ContextIdentifier(node)
            "CodePath" -> CodePath(node)
            "CodeComparator" -> CodeComparator(node)
            "Terminology" -> Terminology(node)
            "SimplePath" -> SimplePath(node)
            "SimplePathReferentialIdentifier" -> SimplePathReferentialIdentifier(node)
            "SimplePathQualifiedIdentifier" -> SimplePathQualifiedIdentifier(node)
            "SimplePathIndexer" -> SimplePathIndexer(node)
            "QualifiedIdentifierExpression" -> QualifiedIdentifierExpression(node)
            "QualifierExpression" -> QualifierExpression(node)

            // Query
            "Query" -> Query(node)
            "AggregateClause" -> AggregateClause(node)
            "QuerySource" -> QuerySource(node)
            "AliasedQuerySource" -> AliasedQuerySource(node)
            "Alias" -> Alias(node)
            "QueryInclusionClause" -> QueryInclusionClause(node)
            "WithClause" -> WithClause(node)
            "WithoutClause" -> WithoutClause(node)
            "SourceClause" -> SourceClause(node)
            "LetClause" -> LetClause(node)
            "WhereClause" -> WhereClause(node)
            "ReturnClause" -> ReturnClause(node)
            "SortClause" -> SortClause(node)
            "LetClauseItem" -> LetClauseItem(node)
            "StartingClause" -> StartingClause(node)
            "SortDirection" -> SortDirection(node)
            "SortByItem" -> SortByItem(node)

            // Invocations
            "Invocation" -> Invocation(node)
            "QualifiedInvocation" -> QualifiedInvocation(node)
            "QualifiedFunction" -> QualifiedFunction(node)
            "MemberInvocation" -> MemberInvocation(node)
            "FunctionInvocation" -> FunctionInvocation(node)
            "ThisInvocation" ->ThisInvocation(node)
            "IndexInvocation" -> IndexInvocation(node)
            "TotalInvocation" -> TotalInvocation(node)
            "Function" -> Function(node)

            // Literals
            "Literal" -> Literal(node)
            "BooleanLiteral" -> BooleanLiteral(node)
            "NullLiteral" -> NullLiteral(node)
            "StringLiteral" -> StringLiteral(node)
            "NumberLiteral" -> NumberLiteral(node)
            "LongNumberLiteral" -> LongNumberLiteral(node)
            "DateTimeLiteral" -> DateTimeLiteral(node)
            "DateLiteral" -> DateLiteral(node)
            "TimeLiteral" -> TimeLiteral(node)
            "QuantityLiteral" -> QuantityLiteral(node)
            "RatioLiteral" -> RatioLiteral(node)

            "SimpleLiteral" -> SimpleLiteral(node)
            "SimpleStringLiteral" -> SimpleStringLiteral(node)
            "SimpleNumberLiteral" -> SimpleNumberLiteral(node)

            "Unit" -> Unit(node)
            "Ratio" -> Ratio(node)
            "Quantity" -> Quantity(node)

            "PluralDateTimePrecision" -> PluralDateTimePrecision(node)
            "DateTimePrecision" -> DateTimePrecision(node)

            // Selectors
            "TupleElementSelector" -> TupleElementSelector(node)
            "InstanceElementSelector" -> InstanceElementSelector(node)
            "IntervalSelector" -> IntervalSelector(node)
            "TupleSelector" -> TupleSelector(node)
            "InstanceSelector" -> InstanceSelector(node)
            "ListSelector" -> ListSelector(node)
            "CodeSelector" -> CodeSelector(node)
            "ConceptSelector" -> ConceptSelector(node)

            // Type Specifiers
            "TypeSpecifier" -> TypeSpecifier(node)
            "NamedTypeSpecifier" -> NamedTypeSpecifier(node)
            "ListTypeSpecifier" -> ListTypeSpecifier(node)
            "IntervalTypeSpecifier" -> IntervalTypeSpecifier(node)
            "TupleTypeSpecifier" -> TupleTypeSpecifier(node)
            "ChoiceTypeSpecifier" -> ChoiceTypeSpecifier(node)
            "TupleElementDefinition" -> TupleElementDefinition(node)

            // expression Terms
            "ExpressionTerm" -> ExpressionTerm(node)
            "TermExpressionTerm" -> TermExpressionTerm(node)
            "ConversionExpressionTerm" -> ConversionExpressionTerm(node)
            "PolarityExpressionTerm" -> PolarityExpressionTerm(node)
            "TimeBoundaryExpressionTerm" -> TimeBoundaryExpressionTerm(node)
            "TimeUnitExpressionTerm" -> TimeUnitExpressionTerm(node)
            "DurationExpressionTerm" -> DurationExpressionTerm(node)
            "DifferenceExpressionTerm" -> DifferenceExpressionTerm(node)
            "WidthExpressionTerm" -> WidthExpressionTerm(node)
            "SuccessorExpressionTerm" -> SuccessorExpressionTerm(node)
            "PredecessorExpressionTerm" -> PredecessorExpressionTerm(node)
            "ElementExtractorExpressionTerm" -> ElementExtractorExpressionTerm(node)
            "PointExtractorExpressionTerm" -> PointExtractorExpressionTerm(node)
            "TypeExtentExpressionTerm" -> TypeExtentExpressionTerm(node)
            "IfThenElseExpressionTerm" -> IfThenElseExpressionTerm(node)
            "CaseExpressionTerm" -> CaseExpressionTerm(node)
            "CaseExpressionItem" -> CaseExpressionItem(node)
            "AggregateExpressionTerm" -> AggregateExpressionTerm(node)
            "SetAggregateExpressionTerm" -> SetAggregateExpressionTerm(node)
            "PowerExpressionTerm" -> PowerExpressionTerm(node)
            "MultiplicationExpressionTerm" -> MultiplicationExpressionTerm(node)
            "AdditionExpressionTerm" -> AdditionExpressionTerm(node)
            "IndexedExpressionTerm" -> IndexedExpressionTerm(node)

            // Terms
            "Term" -> Term(node)
            "InvocationTerm" -> InvocationTerm(node)
            "LiteralTerm" -> LiteralTerm(node)
            "ExternalConstantTerm" -> ExternalConstantTerm(node)
            "ExternalConstant" -> ExternalConstant(node)
            "IntervalSelectorTerm" -> IntervalSelectorTerm(node)
            "TupleSelectorTerm" -> TupleSelectorTerm(node)
            "InstanceSelectorTerm" -> InstanceSelectorTerm(node)
            "ListSelectorTerm" -> ListSelectorTerm(node)
            "CodeSelectorTerm" -> CodeSelectorTerm(node)
            "ConceptSelectorTerm" -> ConceptSelectorTerm(node)
            "ParenthesizedTerm" -> ParenthesizedTerm(node)

            // Expressions
            "Expression" -> Expression(node)
            "TermExpression" -> TermExpression(node)
            "RetrieveExpression" -> RetrieveExpression(node)
            "QueryExpression" -> QueryExpression(node)
            "CastExpression" -> CastExpression(node)
            "NotExpression" -> NotExpression(node)
            "ExistenceExpression" -> ExistenceExpression(node)
            "DurationBetweenExpression" -> DurationBetweenExpression(node)
            "DifferenceBetweenExpression" -> DifferenceBetweenExpression(node)
            "InequalityExpression" -> InequalityExpression(node)
            "TimingExpression" -> TimingExpression(node)
            "EqualityExpression" -> EqualityExpression(node)
            "MembershipExpression" -> MembershipExpression(node)
            "AndExpression" -> AndExpression(node)
            "OrExpression" -> OrExpression(node)
            "ImpliesExpression" -> ImpliesExpression(node)
            "InFixSetExpression" -> InFixSetExpression(node)
            "BooleanExpression" -> BooleanExpression(node)
            "TypeExpression" -> TypeExpression(node)
            "BetweenExpression" -> BetweenExpression(node)
            "DateTimeComponent" -> DateTimeComponent(node)

            // OperatorPhrases
            "ConcurrentWithIntervalOperatorPhrase" -> ConcurrentWithIntervalOperatorPhrase(node)
            "IncludesIntervalOperatorPhrase" -> IncludesIntervalOperatorPhrase(node)
            "IncludedInIntervalOperatorPhrase" -> IncludedInIntervalOperatorPhrase(node)
            "BeforeOrAfterIntervalOperatorPhrase" -> BeforeOrAfterIntervalOperatorPhrase(node)
            "WithinIntervalOperatorPhrase" -> WithinIntervalOperatorPhrase(node)
            "MeetsIntervalOperatorPhrase" -> MeetsIntervalOperatorPhrase(node)
            "OverlapsIntervalOperatorPhrase" -> OverlapsIntervalOperatorPhrase(node)
            "StartsIntervalOperatorPhrase" -> StartsIntervalOperatorPhrase(node)
            "EndsIntervalOperatorPhrase" -> EndsIntervalOperatorPhrase(node)
            "IntervalOperatorPhrase" -> IntervalOperatorPhrase(node)
            "DateTimePrecisionSpecifier" -> DateTimePrecisionSpecifier(node)
            "RelativeQualifier" -> RelativeQualifier(node)
            "OffsetRelativeQualifier" -> OffsetRelativeQualifier(node)
            "ExclusiveRelativeQualifier" -> ExclusiveRelativeQualifier(node)
            "QuantityOffset" -> QuantityOffset(node)
            "TemporalRelationship" -> TemporalRelationship(node)

            // Identifiers
            "Identifier" -> Identifier(node)
            "ReferentialIdentifier" -> ReferentialIdentifier(node)
            "IdentifierOrFunctionIdentifier" -> IdentifierOrFunctionIdentifier(node)
            "ReferentialOrTypeNameIdentifier" -> ReferentialOrTypeNameIdentifier(node)
            "ModelIdentifier" -> ModelIdentifier(node)
            "ParamList" -> ParamList(node)
            "Keyword" -> Keyword(node)
            "ReservedWord" -> ReservedWord(node)
            "KeywordIdentifier" -> KeywordIdentifier(node)
            "ObsoleteIdentifier" -> ObsoleteIdentifier(node)
            "FunctionIdentifier" -> FunctionIdentifier(node)
            "TypeNameIdentifier" -> TypeNameIdentifier(node)
            else -> {
                println("ERROR: Couldn't instantiate custom Antlr context ${CqlRuleTypes.RULE_NAMES[elType.ruleIndex]}")
                ANTLRPsiNode(node)
            }
        }
    }

    companion object {
        val FILE = IFileElementType(CqlLanguage)
    }
}