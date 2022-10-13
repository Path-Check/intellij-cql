package org.pathcheck.intellij.cql.parser

import com.intellij.lang.DefaultASTFactoryImpl
import com.intellij.psi.impl.source.tree.LeafElement
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import org.cqframework.cql.gen.cqlLexer
import org.pathcheck.intellij.cql.CqlTokenTypes
import org.pathcheck.intellij.cql.psi.IdentifierPSINode

/** How to create parse tree nodes (Jetbrains calls them AST nodes). Later
 * non-leaf nodes are converted to PSI nodes by the [ParserDefinition].
 * Leaf nodes are already considered PSI nodes.  This is mostly just
 * [DefaultASTFactoryImpl] but with comments on the methods that you might want
 * to override.
 */
class CqlASTFactory : DefaultASTFactoryImpl() {

    /** Create a parse tree (AST) leaf node from a token. Doubles as a PSI leaf node.
     * Does not see whitespace tokens.  Default impl makes [LeafPsiElement]
     * or [PsiCoreCommentImpl] depending on [ParserDefinition.getCommentTokens].
     */
    override fun createLeaf(
        type: IElementType,
        text: CharSequence
    ): LeafElement {
        return if (type is TokenIElementType
               && (type.antlrTokenType == CqlTokenTypes.TOKEN_ELEMENT_TYPES[cqlLexer.IDENTIFIER].antlrTokenType
                || type.antlrTokenType == CqlTokenTypes.TOKEN_ELEMENT_TYPES[cqlLexer.QUOTEDIDENTIFIER].antlrTokenType
                || type.antlrTokenType == CqlTokenTypes.TOKEN_ELEMENT_TYPES[cqlLexer.DELIMITEDIDENTIFIER].antlrTokenType
                )) {
            IdentifierPSINode(type, text)
        } else
            super.createLeaf(type, text)
    }
}