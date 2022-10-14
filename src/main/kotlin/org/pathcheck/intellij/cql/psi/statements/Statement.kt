package org.pathcheck.intellij.cql.psi.statements

import com.intellij.lang.ASTNode
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode

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