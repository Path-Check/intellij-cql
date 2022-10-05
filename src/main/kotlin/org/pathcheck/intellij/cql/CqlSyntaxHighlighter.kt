package org.pathcheck.intellij.cql

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import org.cqframework.cql.gen.cqlLexer
import org.cqframework.cql.gen.cqlParser

/** A highlighter is really just a mapping from token type to
 * some text attributes using [.getTokenHighlights].
 * The reason that it returns an array, TextAttributesKey[], is
 * that you might want to mix the attributes of a few known highlighters.
 * A [TextAttributesKey] is just a name for that a theme
 * or IDE skin can set. For example, [com.intellij.openapi.editor.DefaultLanguageHighlighterColors.KEYWORD]
 * is the key that maps to what identifiers look like in the editor.
 * To change it, see dialog: Editor > Colors & Fonts > Language Defaults.
 *
 * From [doc](http://www.jetbrains.org/intellij/sdk/docs/reference_guide/custom_language_support/syntax_highlighting_and_error_highlighting.html):
 * "The mapping of the TextAttributesKey to specific attributes used
 * in an editor is defined by the EditorColorsScheme class, and can
 * be configured by the user if the plugin provides an appropriate
 * configuration interface.
 * ...
 * The syntax highlighter returns the [TextAttributesKey]
 * instances for each token type which needs special highlighting.
 * For highlighting lexer errors, the standard TextAttributesKey
 * for bad characters HighlighterColors.BAD_CHARACTER can be used."
 */
class CqlSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        val lexer = AdaptedCqlLexer(null)
        lexer.removeErrorListeners()
        return ANTLRLexerAdaptor(CqlLanguage, lexer)
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<out TextAttributesKey?> {
        if (tokenType !is TokenIElementType) return EMPTY_KEYS
        return arrayOf(
            when (tokenType.antlrTokenType) {
                cqlLexer.IDENTIFIER -> ID
                cqlLexer.T__0 -> KEYWORD // 'library'
                cqlLexer.T__1 -> KEYWORD // 'version'
                cqlLexer.T__2 -> KEYWORD // 'using'
                cqlLexer.T__3 -> KEYWORD // 'called'
                cqlLexer.T__4 -> KEYWORD // 'include'
                cqlLexer.T__5 -> KEYWORD // 'public'
                cqlLexer.T__6 -> KEYWORD // 'private'
                cqlLexer.T__7 -> KEYWORD // 'parameter'
                cqlLexer.T__8 -> KEYWORD // 'codesystem'
                cqlLexer.T__9 -> KEYWORD // 'default'
                cqlLexer.T__10 -> DOT // ':'
                cqlLexer.T__11 -> KEYWORD // 'valueset'
                cqlLexer.T__12 -> KEYWORD // 'codesystems'
                cqlLexer.T__13 -> BRACES // '{'
                cqlLexer.T__14 -> COMMA // ','
                cqlLexer.T__15 -> BRACES // '}'
                cqlLexer.T__16 -> DOT // '.'
                cqlLexer.T__17 -> KEYWORD // 'code'
                cqlLexer.T__18 -> KEYWORD // 'from'
                cqlLexer.T__19 -> KEYWORD // 'concept'
                cqlLexer.T__20 -> KEYWORD // 'List'
                cqlLexer.T__21 -> BRACKETS // '<'
                cqlLexer.T__22 -> BRACKETS // '>'
                cqlLexer.T__23 -> KEYWORD // 'Interval'
                cqlLexer.T__24 -> KEYWORD // 'Tuple'
                cqlLexer.T__25 -> KEYWORD // 'Choice'
                cqlLexer.T__26 -> KEYWORD // 'define'
                cqlLexer.T__27 -> KEYWORD // 'context'
                cqlLexer.T__28 -> KEYWORD // 'fluent'
                cqlLexer.T__29 -> KEYWORD // 'function'
                cqlLexer.T__30 -> PARENTHESES // '('
                cqlLexer.T__31 -> PARENTHESES // ')'
                cqlLexer.T__32 -> KEYWORD // 'returns'
                cqlLexer.T__33 -> KEYWORD // 'external'
                cqlLexer.T__34 -> KEYWORD // 'with'
                cqlLexer.T__35 -> KEYWORD // 'such that'
                cqlLexer.T__36 -> KEYWORD // 'without'
                cqlLexer.T__37 -> BRACKETS // '['
                cqlLexer.T__38 -> OPERATION_SIGN // '->'
                cqlLexer.T__39 -> BRACKETS // ']'
                cqlLexer.T__40 -> KEYWORD // 'in'
                cqlLexer.T__41 -> OPERATION_SIGN // '='
                cqlLexer.T__42 -> OPERATION_SIGN // '~'
                cqlLexer.T__43 -> KEYWORD // 'let'
                cqlLexer.T__44 -> KEYWORD // 'where'
                cqlLexer.T__45 -> KEYWORD // 'return'
                cqlLexer.T__46 -> KEYWORD // 'all'
                cqlLexer.T__47 -> KEYWORD // 'distinct'
                cqlLexer.T__48 -> KEYWORD // 'aggregate'
                cqlLexer.T__49 -> KEYWORD // 'starting'
                cqlLexer.T__50 -> KEYWORD // 'sort'
                cqlLexer.T__51 -> KEYWORD // 'by'
                cqlLexer.T__52 -> KEYWORD // 'asc'
                cqlLexer.T__53 -> KEYWORD // 'ascending'
                cqlLexer.T__54 -> KEYWORD // 'desc'
                cqlLexer.T__55 -> KEYWORD // 'descending'
                cqlLexer.T__56 -> OPERATION_SIGN // 'is'
                cqlLexer.T__57 -> OPERATION_SIGN // 'not'
                cqlLexer.T__58 -> KEYWORD // 'null'
                cqlLexer.T__59 -> KEYWORD // 'true'
                cqlLexer.T__60 -> KEYWORD // 'false'
                cqlLexer.T__61 -> OPERATION_SIGN // 'as'
                cqlLexer.T__62 -> KEYWORD // 'cast'
                cqlLexer.T__63 -> FUNCTION_CALL // 'exists'
                cqlLexer.T__64 -> KEYWORD // 'properly'
                cqlLexer.T__65 -> KEYWORD // 'between'
                cqlLexer.T__66 -> OPERATION_SIGN // 'and'
                cqlLexer.T__67 -> KEYWORD // 'duration'
                cqlLexer.T__68 -> KEYWORD // 'difference'
                cqlLexer.T__69 -> OPERATION_SIGN // '<='
                cqlLexer.T__70 -> OPERATION_SIGN // '>='
                cqlLexer.T__71 -> OPERATION_SIGN // '!='
                cqlLexer.T__72 -> OPERATION_SIGN // '!~'
                cqlLexer.T__73 -> KEYWORD // 'contains'
                cqlLexer.T__74 -> OPERATION_SIGN // 'or'
                cqlLexer.T__75 -> OPERATION_SIGN // 'xor'
                cqlLexer.T__76 -> KEYWORD // 'implies'
                cqlLexer.T__77 -> OPERATION_SIGN // '|'
                cqlLexer.T__78 -> KEYWORD // 'union'
                cqlLexer.T__79 -> KEYWORD // 'intersect'
                cqlLexer.T__80 -> KEYWORD // 'except'
                cqlLexer.T__81 -> KEYWORD // 'year'
                cqlLexer.T__82 -> KEYWORD // 'month'
                cqlLexer.T__83 -> KEYWORD // 'week'
                cqlLexer.T__84 -> KEYWORD // 'day'
                cqlLexer.T__85 -> KEYWORD // 'hour'
                cqlLexer.T__86 -> KEYWORD // 'minute'
                cqlLexer.T__87 -> KEYWORD // 'second'
                cqlLexer.T__88 -> KEYWORD // 'millisecond'
                cqlLexer.T__89 -> KEYWORD // 'date'
                cqlLexer.T__90 -> KEYWORD // 'time'
                cqlLexer.T__91 -> KEYWORD // 'timezone'
                cqlLexer.T__92 -> KEYWORD // 'timezoneoffset'
                cqlLexer.T__93 -> KEYWORD // 'years'
                cqlLexer.T__94 -> KEYWORD // 'months'
                cqlLexer.T__95 -> KEYWORD // 'weeks'
                cqlLexer.T__96 -> KEYWORD // 'days'
                cqlLexer.T__97 -> KEYWORD // 'hours'
                cqlLexer.T__98 -> KEYWORD // 'minutes'
                cqlLexer.T__99 ->  KEYWORD // 'seconds'
                cqlLexer.T__100 -> KEYWORD // 'milliseconds'
                cqlLexer.T__101 -> KEYWORD // 'convert'
                cqlLexer.T__102 -> KEYWORD // 'to'
                cqlLexer.T__103 -> OPERATION_SIGN // '+'
                cqlLexer.T__104 -> OPERATION_SIGN // '-'
                cqlLexer.T__105 -> KEYWORD // 'start'
                cqlLexer.T__106 -> KEYWORD // 'end'
                cqlLexer.T__107 -> OPERATION_SIGN // 'of'
                cqlLexer.T__108 -> KEYWORD // 'width'
                cqlLexer.T__109 -> KEYWORD // 'successor'
                cqlLexer.T__110 -> KEYWORD // 'predecessor'
                cqlLexer.T__111 -> KEYWORD // 'singleton'
                cqlLexer.T__112 -> KEYWORD // 'point'
                cqlLexer.T__113 -> KEYWORD // 'minimum'
                cqlLexer.T__114 -> KEYWORD // 'maximum'
                cqlLexer.T__115 -> OPERATION_SIGN // '^'
                cqlLexer.T__116 -> OPERATION_SIGN // '*'
                cqlLexer.T__117 -> OPERATION_SIGN // '/'
                cqlLexer.T__118 -> OPERATION_SIGN // 'div'
                cqlLexer.T__119 -> OPERATION_SIGN// 'mod'
                cqlLexer.T__120 -> OPERATION_SIGN // '&'
                cqlLexer.T__121 -> KEYWORD // 'if'
                cqlLexer.T__122 -> KEYWORD // 'then'
                cqlLexer.T__123 -> KEYWORD // 'else'
                cqlLexer.T__124 -> KEYWORD // 'case'
                cqlLexer.T__125 -> KEYWORD // 'flatten'
                cqlLexer.T__126 -> KEYWORD // 'expand'
                cqlLexer.T__127 -> KEYWORD // 'collapse'
                cqlLexer.T__128 -> KEYWORD // 'per'
                cqlLexer.T__129 -> KEYWORD // 'when'
                cqlLexer.T__130 -> KEYWORD // 'or before'
                cqlLexer.T__131 -> KEYWORD // 'or after'
                cqlLexer.T__132 -> KEYWORD // 'or more'
                cqlLexer.T__133 -> KEYWORD // 'or less'
                cqlLexer.T__134 -> KEYWORD // 'less than'
                cqlLexer.T__135 -> KEYWORD // 'more than'
                cqlLexer.T__136 -> KEYWORD // 'on or'
                cqlLexer.T__137 -> KEYWORD // 'before'
                cqlLexer.T__138 -> KEYWORD // 'after'
                cqlLexer.T__139 -> KEYWORD // 'or on'
                cqlLexer.T__140 -> KEYWORD // 'starts'
                cqlLexer.T__141 -> KEYWORD // 'ends'
                cqlLexer.T__142 -> KEYWORD // 'occurs'
                cqlLexer.T__143 -> KEYWORD // 'same'
                cqlLexer.T__144 -> KEYWORD // 'includes'
                cqlLexer.T__145 -> KEYWORD // 'during'
                cqlLexer.T__146 -> KEYWORD // 'included in'
                cqlLexer.T__147 -> KEYWORD // 'within'
                cqlLexer.T__148 -> KEYWORD // 'meets'
                cqlLexer.T__149 -> KEYWORD // 'overlaps'
                cqlLexer.T__150 -> KEYWORD // '$this'
                cqlLexer.T__151 -> KEYWORD // '$index'
                cqlLexer.T__152 -> KEYWORD // '$total'
                cqlLexer.T__153 -> KEYWORD // 'display'
                cqlLexer.T__154 -> KEYWORD // 'Code'
                cqlLexer.T__155 -> KEYWORD // 'Concept'
                cqlLexer.T__156 -> KEYWORD // '%'
                cqlLexer.STRING -> STRING
                cqlLexer.COMMENT -> LINE_COMMENT
                cqlLexer.LINE_COMMENT -> BLOCK_COMMENT
                cqlLexer.DATE -> VALUE
                cqlLexer.DATETIME -> VALUE
                cqlLexer.TIME -> VALUE
                cqlLexer.NUMBER -> VALUE
                cqlLexer.QUOTEDIDENTIFIER -> FUNCTION_DECLARATION
                cqlLexer.DELIMITEDIDENTIFIER -> FUNCTION_DECLARATION
                cqlLexer.LONGNUMBER -> VALUE
                else -> return EMPTY_KEYS
            }
        )
    }

    companion object {
        private val EMPTY_KEYS = arrayOfNulls<TextAttributesKey>(0)
        val ID = TextAttributesKey.createTextAttributesKey("CQL_ID", DefaultLanguageHighlighterColors.IDENTIFIER)
        val KEYWORD = TextAttributesKey.createTextAttributesKey("CQL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val STRING = TextAttributesKey.createTextAttributesKey("CQL_STRING", DefaultLanguageHighlighterColors.STRING)
        val LINE_COMMENT = TextAttributesKey.createTextAttributesKey("CQL_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey("CQL_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT)
        val VALUE = TextAttributesKey.createTextAttributesKey("CQL_VALUE", DefaultLanguageHighlighterColors.NUMBER)
        val DOT = TextAttributesKey.createTextAttributesKey("CQL_DOT", DefaultLanguageHighlighterColors.DOT)
        val BRACES = TextAttributesKey.createTextAttributesKey("CQL_BRACES", DefaultLanguageHighlighterColors.BRACES)
        val BRACKETS = TextAttributesKey.createTextAttributesKey("CQL_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)
        val COMMA = TextAttributesKey.createTextAttributesKey("CQL_COMMA", DefaultLanguageHighlighterColors.COMMA)
        val OPERATION_SIGN = TextAttributesKey.createTextAttributesKey("CQL_OPERATION", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val PARENTHESES = TextAttributesKey.createTextAttributesKey("CQL_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)
        val FUNCTION_CALL = TextAttributesKey.createTextAttributesKey("CQL_FUNCTION_CALL", DefaultLanguageHighlighterColors.FUNCTION_CALL)
        val FUNCTION_DECLARATION = TextAttributesKey.createTextAttributesKey("CQL_FUNCTION_DECLARATION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
        init {
            PSIElementTypeFactory.defineLanguageIElementTypes(
                CqlLanguage,
                cqlParser.tokenNames,
                cqlParser.ruleNames
            )
        }
    }
}