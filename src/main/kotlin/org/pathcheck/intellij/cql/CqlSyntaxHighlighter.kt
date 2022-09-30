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
        val lexer = cqlLexer(null)
        return ANTLRLexerAdaptor(CqlLanguage, lexer)
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<out TextAttributesKey?> {
        if (tokenType !is TokenIElementType) return EMPTY_KEYS
        return arrayOf(
            when (tokenType.antlrTokenType) {
                cqlLexer.IDENTIFIER -> ID
                in cqlLexer.T__0 .. cqlLexer.T__156 -> KEYWORD
                cqlLexer.STRING -> STRING
                cqlLexer.COMMENT -> LINE_COMMENT
                cqlLexer.LINE_COMMENT -> BLOCK_COMMENT
                else -> return EMPTY_KEYS
            }
        )
    }

    companion object {
        private val EMPTY_KEYS = arrayOfNulls<TextAttributesKey>(0)
        val ID = TextAttributesKey.createTextAttributesKey("SAMPLE_ID", DefaultLanguageHighlighterColors.IDENTIFIER)
        val KEYWORD =
            TextAttributesKey.createTextAttributesKey("SAMPLE_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val STRING = TextAttributesKey.createTextAttributesKey("SAMPLE_STRING", DefaultLanguageHighlighterColors.STRING)
        val LINE_COMMENT = TextAttributesKey.createTextAttributesKey(
            "SAMPLE_LINE_COMMENT",
            DefaultLanguageHighlighterColors.LINE_COMMENT
        )
        val BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey(
            "SAMPLE_BLOCK_COMMENT",
            DefaultLanguageHighlighterColors.BLOCK_COMMENT
        )

        init {
            PSIElementTypeFactory.defineLanguageIElementTypes(
                CqlLanguage,
                cqlParser.tokenNames,
                cqlParser.ruleNames
            )
        }
    }
}