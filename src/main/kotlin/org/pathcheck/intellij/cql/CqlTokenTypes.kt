package org.pathcheck.intellij.cql

import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.lexer.RuleIElementType
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import org.cqframework.cql.gen.cqlLexer
import org.cqframework.cql.gen.cqlParser
import org.intellij.lang.annotations.MagicConstant

object CqlTokenTypes {
    val TOKEN_ELEMENT_TYPES = PSIElementTypeFactory.getTokenIElementTypes(CqlLanguage)!!
    val COMMENTS = PSIElementTypeFactory.createTokenSet(
        CqlLanguage,
        cqlLexer.COMMENT,
        cqlLexer.LINE_COMMENT
    )!!
    val WHITESPACES = PSIElementTypeFactory.createTokenSet(
        CqlLanguage,
        cqlLexer.WS
    )!!
    val STRINGS = PSIElementTypeFactory.createTokenSet(
        CqlLanguage,
        cqlLexer.STRING
    )!!
}