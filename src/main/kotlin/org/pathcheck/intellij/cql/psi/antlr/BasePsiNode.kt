package org.pathcheck.intellij.cql.psi.antlr

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.pathcheck.intellij.cql.psi.IdentifierPSINode

open class BasePsiNode(node: ASTNode) : ANTLRPsiNode(node) {

    fun <T : BasePsiNode?> getRule(ctxType: Class<out T>?, i: Int): T? {
        if (i < 0 || i >= children.size) {
            return null
        }

        var j = -1 // what element have we found with ctxType?

        for (o in children) {
            if (ctxType!!.isInstance(o)) {
                j++
                if (j == i) {
                    return ctxType.cast(o)
                }
            }
        }
        return null
    }

    open fun <T : BasePsiNode?> getRules(ctxType: Class<out T>): List<T>? {
        var contexts = mutableListOf<T>()
        for (o in children) {
            if (ctxType.isInstance(o)) {
                contexts.add(ctxType.cast(o))
            }
        }
        return contexts
    }

    open fun getToken(ttype: Int, i: Int): IdentifierPSINode? {
        if (i < 0 || i >= children.size) {
            return null
        }
        var j = -1 // what token with ttype have we found?
        for (o in children) {
            if (o.node.elementType is TokenIElementType) {
                if ((o.node.elementType as TokenIElementType).antlrTokenType == ttype) {
                    j++
                    if (j == i) {
                        return o as IdentifierPSINode
                    }
                }
            }
        }
        return null
    }

}