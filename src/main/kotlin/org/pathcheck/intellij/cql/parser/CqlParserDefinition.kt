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
import org.pathcheck.intellij.cql.psi.Identifier
import org.pathcheck.intellij.cql.psi.Library
import org.pathcheck.intellij.cql.psi.antlr.PsiContextNodes
import org.pathcheck.intellij.cql.psi.definitions.*
import org.pathcheck.intellij.cql.psi.scopes.*

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

            // Statements
            "Statement" -> PsiContextNodes.Statement(node)
            "ExpressionDefinition" -> ExpressionDefinition(node)
            "ContextDefinition" -> ContextDefinition(node)
            "FunctionDefinition" -> FunctionDefinition(node)

            // overriden
            "AggregateClause" -> AggregateClause(node)
            "Query" -> Query(node)
            "Identifier" -> Identifier(node)

            // Type Aware
            "InvocationExpressionTerm" -> InvocationExpressionTerm(node)
            "QualifiedMemberInvocation" -> QualifiedMemberInvocation(node)
            "QualifiedFunctionInvocation" -> QualifiedFunctionInvocation(node)

            "QualifiedIdentifier" -> PsiContextNodes.QualifiedIdentifier(node)
            "VersionSpecifier" -> PsiContextNodes.VersionSpecifier(node)
            "LocalIdentifier" -> PsiContextNodes.LocalIdentifier(node)
            "AccessModifier" -> PsiContextNodes.AccessModifier(node)
            "TypeSpecifier" -> PsiContextNodes.TypeSpecifier(node)
            "Expression" -> PsiContextNodes.Expression(node)
            "CodesystemId" -> PsiContextNodes.CodesystemId(node)
            "ValuesetId" -> PsiContextNodes.ValuesetId(node)
            "Codesystems" -> PsiContextNodes.Codesystems(node)
            "CodesystemIdentifier" -> PsiContextNodes.CodesystemIdentifier(node)
            "LibraryIdentifier" -> PsiContextNodes.LibraryIdentifier(node)
            "CodeId" -> PsiContextNodes.CodeId(node)
            "DisplayClause" -> PsiContextNodes.DisplayClause(node)
            "CodeIdentifier" -> PsiContextNodes.CodeIdentifier(node)
            "NamedTypeSpecifier" -> PsiContextNodes.NamedTypeSpecifier(node)
            "ListTypeSpecifier" -> PsiContextNodes.ListTypeSpecifier(node)
            "IntervalTypeSpecifier" -> PsiContextNodes.IntervalTypeSpecifier(node)
            "TupleTypeSpecifier" -> PsiContextNodes.TupleTypeSpecifier(node)
            "ChoiceTypeSpecifier" -> PsiContextNodes.ChoiceTypeSpecifier(node)
            "Qualifier" -> PsiContextNodes.Qualifier(node)
            "ReferentialOrTypeNameIdentifier" -> PsiContextNodes.ReferentialOrTypeNameIdentifier(node)
            "ModelIdentifier" -> PsiContextNodes.ModelIdentifier(node)
            "TupleElementDefinition" -> PsiContextNodes.TupleElementDefinition(node)
            "ReferentialIdentifier" -> PsiContextNodes.ReferentialIdentifier(node)
            "FluentModifier" -> PsiContextNodes.FluentModifier(node)
            "IdentifierOrFunctionIdentifier" -> PsiContextNodes.IdentifierOrFunctionIdentifier(node)
            "OperandDefinition" -> PsiContextNodes.OperandDefinition(node)
            "FunctionBody" -> PsiContextNodes.FunctionBody(node)
            "QuerySource" -> PsiContextNodes.QuerySource(node)
            "Retrieve" -> PsiContextNodes.Retrieve(node)
            "QualifiedIdentifierExpression" -> PsiContextNodes.QualifiedIdentifierExpression(node)
            "AliasedQuerySource" -> PsiContextNodes.AliasedQuerySource(node)
            "Alias" -> PsiContextNodes.Alias(node)
            "QueryInclusionClause" -> PsiContextNodes.QueryInclusionClause(node)
            "WithClause" -> PsiContextNodes.WithClause(node)
            "WithoutClause" -> PsiContextNodes.WithoutClause(node)
            "ContextIdentifier" -> PsiContextNodes.ContextIdentifier(node)
            "CodePath" -> PsiContextNodes.CodePath(node)
            "CodeComparator" -> PsiContextNodes.CodeComparator(node)
            "Terminology" -> PsiContextNodes.Terminology(node)
            "SimplePath" -> PsiContextNodes.SimplePath(node)
            "SourceClause" -> PsiContextNodes.SourceClause(node)
            "LetClause" -> PsiContextNodes.LetClause(node)
            "WhereClause" -> PsiContextNodes.WhereClause(node)
            "ReturnClause" -> PsiContextNodes.ReturnClause(node)
            "SortClause" -> PsiContextNodes.SortClause(node)
            "LetClauseItem" -> PsiContextNodes.LetClauseItem(node)
            "StartingClause" -> PsiContextNodes.StartingClause(node)
            "SimpleLiteral" -> PsiContextNodes.SimpleLiteral(node)
            "Quantity" -> PsiContextNodes.Quantity(node)
            "SortDirection" -> PsiContextNodes.SortDirection(node)
            "SortByItem" -> PsiContextNodes.SortByItem(node)
            "ExpressionTerm" -> PsiContextNodes.ExpressionTerm(node)
            "QualifierExpression" -> PsiContextNodes.QualifierExpression(node)
            "SimplePathReferentialIdentifier" -> PsiContextNodes.SimplePathReferentialIdentifier(node)
            "SimplePathQualifiedIdentifier" -> PsiContextNodes.SimplePathQualifiedIdentifier(node)
            "SimplePathIndexer" -> PsiContextNodes.SimplePathIndexer(node)
            "SimpleStringLiteral" -> PsiContextNodes.SimpleStringLiteral(node)
            "SimpleNumberLiteral" -> PsiContextNodes.SimpleNumberLiteral(node)
            "TermExpression" -> PsiContextNodes.TermExpression(node)
            "RetrieveExpression" -> PsiContextNodes.RetrieveExpression(node)
            "QueryExpression" -> PsiContextNodes.QueryExpression(node)
            "CastExpression" -> PsiContextNodes.CastExpression(node)
            "NotExpression" -> PsiContextNodes.NotExpression(node)
            "ExistenceExpression" -> PsiContextNodes.ExistenceExpression(node)
            "DurationBetweenExpression" -> PsiContextNodes.DurationBetweenExpression(node)
            "PluralDateTimePrecision" -> PsiContextNodes.PluralDateTimePrecision(node)
            "DifferenceBetweenExpression" -> PsiContextNodes.DifferenceBetweenExpression(node)
            "InequalityExpression" -> PsiContextNodes.InequalityExpression(node)
            "TimingExpression" -> PsiContextNodes.TimingExpression(node)
            "IntervalOperatorPhrase" -> PsiContextNodes.IntervalOperatorPhrase(node)
            "EqualityExpression" -> PsiContextNodes.EqualityExpression(node)
            "MembershipExpression" -> PsiContextNodes.MembershipExpression(node)
            "DateTimePrecisionSpecifier" -> PsiContextNodes.DateTimePrecisionSpecifier(node)
            "AndExpression" -> PsiContextNodes.AndExpression(node)
            "OrExpression" -> PsiContextNodes.OrExpression(node)
            "ImpliesExpression" -> PsiContextNodes.ImpliesExpression(node)
            "InFixSetExpression" -> PsiContextNodes.InFixSetExpression(node)
            "BooleanExpression" -> PsiContextNodes.BooleanExpression(node)
            "TypeExpression" -> PsiContextNodes.TypeExpression(node)
            "BetweenExpression" -> PsiContextNodes.BetweenExpression(node)
            "DateTimePrecision" -> PsiContextNodes.DateTimePrecision(node)
            "DateTimeComponent" -> PsiContextNodes.DateTimeComponent(node)
            "TermExpressionTerm" -> PsiContextNodes.TermExpressionTerm(node)
            "Term" -> PsiContextNodes.Term(node)
            "ConversionExpressionTerm" -> PsiContextNodes.ConversionExpressionTerm(node)
            "Unit" -> PsiContextNodes.Unit(node)
            "PolarityExpressionTerm" -> PsiContextNodes.PolarityExpressionTerm(node)
            "TimeBoundaryExpressionTerm" -> PsiContextNodes.TimeBoundaryExpressionTerm(node)
            "TimeUnitExpressionTerm" -> PsiContextNodes.TimeUnitExpressionTerm(node)
            "DurationExpressionTerm" -> PsiContextNodes.DurationExpressionTerm(node)
            "DifferenceExpressionTerm" -> PsiContextNodes.DifferenceExpressionTerm(node)
            "WidthExpressionTerm" -> PsiContextNodes.WidthExpressionTerm(node)
            "SuccessorExpressionTerm" -> PsiContextNodes.SuccessorExpressionTerm(node)
            "PredecessorExpressionTerm" -> PsiContextNodes.PredecessorExpressionTerm(node)
            "ElementExtractorExpressionTerm" -> PsiContextNodes.ElementExtractorExpressionTerm(node)
            "PointExtractorExpressionTerm" -> PsiContextNodes.PointExtractorExpressionTerm(node)
            "TypeExtentExpressionTerm" -> PsiContextNodes.TypeExtentExpressionTerm(node)
            "IfThenElseExpressionTerm" -> PsiContextNodes.IfThenElseExpressionTerm(node)
            "CaseExpressionTerm" -> PsiContextNodes.CaseExpressionTerm(node)
            "CaseExpressionItem" -> PsiContextNodes.CaseExpressionItem(node)
            "AggregateExpressionTerm" -> PsiContextNodes.AggregateExpressionTerm(node)
            "SetAggregateExpressionTerm" -> PsiContextNodes.SetAggregateExpressionTerm(node)
            "PowerExpressionTerm" -> PsiContextNodes.PowerExpressionTerm(node)
            "MultiplicationExpressionTerm" -> PsiContextNodes.MultiplicationExpressionTerm(node)
            "AdditionExpressionTerm" -> PsiContextNodes.AdditionExpressionTerm(node)
            "QualifiedInvocation" -> PsiContextNodes.QualifiedInvocation(node)
            "IndexedExpressionTerm" -> PsiContextNodes.IndexedExpressionTerm(node)
            "RelativeQualifier" -> PsiContextNodes.RelativeQualifier(node)
            "OffsetRelativeQualifier" -> PsiContextNodes.OffsetRelativeQualifier(node)
            "ExclusiveRelativeQualifier" -> PsiContextNodes.ExclusiveRelativeQualifier(node)
            "QuantityOffset" -> PsiContextNodes.QuantityOffset(node)
            "TemporalRelationship" -> PsiContextNodes.TemporalRelationship(node)
            "ConcurrentWithIntervalOperatorPhrase" -> PsiContextNodes.ConcurrentWithIntervalOperatorPhrase(node)
            "IncludesIntervalOperatorPhrase" -> PsiContextNodes.IncludesIntervalOperatorPhrase(node)
            "IncludedInIntervalOperatorPhrase" -> PsiContextNodes.IncludedInIntervalOperatorPhrase(node)
            "BeforeOrAfterIntervalOperatorPhrase" -> PsiContextNodes.BeforeOrAfterIntervalOperatorPhrase(node)
            "WithinIntervalOperatorPhrase" -> PsiContextNodes.WithinIntervalOperatorPhrase(node)
            "MeetsIntervalOperatorPhrase" -> PsiContextNodes.MeetsIntervalOperatorPhrase(node)
            "OverlapsIntervalOperatorPhrase" -> PsiContextNodes.OverlapsIntervalOperatorPhrase(node)
            "StartsIntervalOperatorPhrase" -> PsiContextNodes.StartsIntervalOperatorPhrase(node)
            "EndsIntervalOperatorPhrase" -> PsiContextNodes.EndsIntervalOperatorPhrase(node)
            "InvocationTerm" -> PsiContextNodes.InvocationTerm(node)
            "Invocation" -> PsiContextNodes.Invocation(node)
            "LiteralTerm" -> PsiContextNodes.LiteralTerm(node)
            "Literal" -> PsiContextNodes.Literal(node)
            "ExternalConstantTerm" -> PsiContextNodes.ExternalConstantTerm(node)
            "ExternalConstant" -> PsiContextNodes.ExternalConstant(node)
            "IntervalSelectorTerm" -> PsiContextNodes.IntervalSelectorTerm(node)
            "IntervalSelector" -> PsiContextNodes.IntervalSelector(node)
            "TupleSelectorTerm" -> PsiContextNodes.TupleSelectorTerm(node)
            "TupleSelector" -> PsiContextNodes.TupleSelector(node)
            "InstanceSelectorTerm" -> PsiContextNodes.InstanceSelectorTerm(node)
            "InstanceSelector" -> PsiContextNodes.InstanceSelector(node)
            "ListSelectorTerm" -> PsiContextNodes.ListSelectorTerm(node)
            "ListSelector" -> PsiContextNodes.ListSelector(node)
            "CodeSelectorTerm" -> PsiContextNodes.CodeSelectorTerm(node)
            "CodeSelector" -> PsiContextNodes.CodeSelector(node)
            "ConceptSelectorTerm" -> PsiContextNodes.ConceptSelectorTerm(node)
            "ConceptSelector" -> PsiContextNodes.ConceptSelector(node)
            "ParenthesizedTerm" -> PsiContextNodes.ParenthesizedTerm(node)
            "QualifiedFunction" -> PsiContextNodes.QualifiedFunction(node)
            "ParamList" -> PsiContextNodes.ParamList(node)
            "MemberInvocation" -> PsiContextNodes.MemberInvocation(node)
            "FunctionInvocation" -> PsiContextNodes.FunctionInvocation(node)
            "Function" -> PsiContextNodes.Function(node)
            "ThisInvocation" -> PsiContextNodes.ThisInvocation(node)
            "IndexInvocation" -> PsiContextNodes.IndexInvocation(node)
            "TotalInvocation" -> PsiContextNodes.TotalInvocation(node)
            "Ratio" -> PsiContextNodes.Ratio(node)
            "BooleanLiteral" -> PsiContextNodes.BooleanLiteral(node)
            "NullLiteral" -> PsiContextNodes.NullLiteral(node)
            "StringLiteral" -> PsiContextNodes.StringLiteral(node)
            "NumberLiteral" -> PsiContextNodes.NumberLiteral(node)
            "LongNumberLiteral" -> PsiContextNodes.LongNumberLiteral(node)
            "DateTimeLiteral" -> PsiContextNodes.DateTimeLiteral(node)
            "DateLiteral" -> PsiContextNodes.DateLiteral(node)
            "TimeLiteral" -> PsiContextNodes.TimeLiteral(node)
            "QuantityLiteral" -> PsiContextNodes.QuantityLiteral(node)
            "RatioLiteral" -> PsiContextNodes.RatioLiteral(node)
            "TupleElementSelector" -> PsiContextNodes.TupleElementSelector(node)
            "InstanceElementSelector" -> PsiContextNodes.InstanceElementSelector(node)
            "Keyword" -> PsiContextNodes.Keyword(node)
            "ReservedWord" -> PsiContextNodes.ReservedWord(node)
            "KeywordIdentifier" -> PsiContextNodes.KeywordIdentifier(node)
            "ObsoleteIdentifier" -> PsiContextNodes.ObsoleteIdentifier(node)
            "FunctionIdentifier" -> PsiContextNodes.FunctionIdentifier(node)
            "TypeNameIdentifier" -> PsiContextNodes.TypeNameIdentifier(node)
            else -> ANTLRPsiNode(node)
        }
    }

    companion object {
        val FILE = IFileElementType(CqlLanguage)
    }
}