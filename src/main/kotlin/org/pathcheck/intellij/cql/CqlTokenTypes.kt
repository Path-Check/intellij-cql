package org.pathcheck.intellij.cql

import com.intellij.psi.tree.TokenSet
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import org.cqframework.cql.gen.cqlLexer

object CqlTokenTypes {
    val TOKEN_ELEMENT_TYPES: List<TokenIElementType> by lazy {
        PSIElementTypeFactory.getTokenIElementTypes(CqlLanguage)!!
    }

    val COMMENTS: TokenSet by lazy {
        PSIElementTypeFactory.createTokenSet(
            CqlLanguage,
            cqlLexer.COMMENT,
            cqlLexer.LINE_COMMENT
        )!!
    }
    val WHITESPACES: TokenSet by lazy {
        PSIElementTypeFactory.createTokenSet(
            CqlLanguage,
            cqlLexer.WS
        )!!
    }
    val STRINGS:TokenSet by lazy {
        PSIElementTypeFactory.createTokenSet(
            CqlLanguage,
            cqlLexer.STRING
        )!!
    }
}