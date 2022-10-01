package org.pathcheck.intellij.cql

import com.intellij.lang.CodeDocumentationAwareCommenter
import com.intellij.psi.PsiComment
import com.intellij.psi.tree.IElementType
import org.cqframework.cql.gen.cqlLexer

//BLOCK_COMMENT
//        :	'/*' .*? ('*/' | EOF)  -> channel(HIDDEN)
//        ;
//
//LINE_COMMENT
//        :	'//' ~[\r\n]*  -> channel(HIDDEN)
//        ;
class CqlCommenter : CodeDocumentationAwareCommenter {
    override fun getLineCommentPrefix(): String? {
        return "//"
    }

    override fun getBlockCommentPrefix(): String? {
        return "/*"
    }

    override fun getBlockCommentSuffix(): String? {
        return "*/"
    }

    override fun getCommentedBlockCommentPrefix(): String? {
        return null
    }

    override fun getCommentedBlockCommentSuffix(): String? {
        return null
    }

    override fun getLineCommentTokenType(): IElementType? {
        return CqlTokenTypes.TOKEN_ELEMENT_TYPES[cqlLexer.LINE_COMMENT]
    }

    override fun getBlockCommentTokenType(): IElementType? {
        return CqlTokenTypes.TOKEN_ELEMENT_TYPES[cqlLexer.COMMENT]
    }

    override fun getDocumentationCommentTokenType(): IElementType? {
        return CqlTokenTypes.TOKEN_ELEMENT_TYPES[cqlLexer.COMMENT]
    }

    override fun getDocumentationCommentPrefix(): String? {
        return "/*"
    }

    override fun getDocumentationCommentLinePrefix(): String? {
        return "*"
    }

    override fun getDocumentationCommentSuffix(): String? {
        return "*/"
    }

    override fun isDocumentationComment(element: PsiComment): Boolean {
        return element != null && element.tokenType === documentationCommentTokenType
    }
}