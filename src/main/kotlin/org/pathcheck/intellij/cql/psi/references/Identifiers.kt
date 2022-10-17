package org.pathcheck.intellij.cql.psi.references

import com.intellij.lang.ASTNode
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.impl.source.tree.LeafPsiElement
import org.cqframework.cql.gen.cqlParser
import org.hl7.cql.model.DataType
import org.pathcheck.intellij.cql.psi.HasResultType
import org.pathcheck.intellij.cql.psi.IdentifierPSINode
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.expressions.Expression

class Identifier(node: ASTNode) : BasePsiNode(node) {
    fun IDENTIFIER(): IdentifierPSINode? {
        return getToken(cqlParser.IDENTIFIER, 0)
    }

    fun DELIMITEDIDENTIFIER(): IdentifierPSINode? {
        return getToken(cqlParser.DELIMITEDIDENTIFIER, 0)
    }

    fun QUOTEDIDENTIFIER(): IdentifierPSINode? {
        return getToken(cqlParser.QUOTEDIDENTIFIER, 0)
    }

    fun all(): List<IdentifierPSINode> {
        return listOfNotNull(IDENTIFIER(), DELIMITEDIDENTIFIER(), QUOTEDIDENTIFIER())
    }
}

class ReferentialIdentifier(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun identifier(): Identifier? {
        return getRule(Identifier::class.java, 0)
    }

    fun keywordIdentifier(): KeywordIdentifier? {
        return getRule(KeywordIdentifier::class.java, 0)
    }

    override fun anyToken(): LeafPsiElement? {
        return identifier()?.anyToken() ?: keywordIdentifier()?.anyToken()
    }

    /**
     * This should not be called if this object is used after a qualifier
     */
    override fun getResultType(): DataType? {
        val ref = anyToken()?.reference
        if (ref == null) {
            thisLogger().warn("Element ${this.text} doesn't have a reference.")
            return null
        }

        var refDefinition = ref.resolve()

        // Always returns the Identifier Node, which doesn't have a result type
        while (refDefinition != null && refDefinition !is HasResultType) {
            refDefinition = refDefinition.parent
        }

        if (refDefinition === this) {
            // the only reference is the current node, which is not ready for any results.
            return null
        }

        return if (refDefinition is HasResultType) {
            refDefinition.getResultType()
        } else {
            thisLogger().warn("Element $refDefinition doesn't have a return type. Class $this with ${this.text}")
            null
        }
    }
}

class ModelIdentifier(node: ASTNode) : BasePsiNode(node) {
    fun identifier(): Identifier? {
        return getRule(Identifier::class.java, 0)
    }
}

class ReferentialOrTypeNameIdentifier(node: ASTNode) : BasePsiNode(node) {
    fun referentialIdentifier(): ReferentialIdentifier? {
        return getRule(ReferentialIdentifier::class.java, 0)
    }

    fun typeNameIdentifier(): TypeNameIdentifier? {
        return getRule(TypeNameIdentifier::class.java, 0)
    }
}

class IdentifierOrFunctionIdentifier(node: ASTNode) : BasePsiNode(node) {
    fun identifier(): Identifier? {
        return getRule(Identifier::class.java, 0)
    }

    fun functionIdentifier(): FunctionIdentifier? {
        return getRule(FunctionIdentifier::class.java, 0)
    }
}

class ParamList(node: ASTNode) : BasePsiNode(node) {
    fun expression(): List<Expression>? {
        return getRules(Expression::class.java)
    }

    fun expression(i: Int): Expression? {
        return getRule(Expression::class.java, i)
    }
}

class Keyword(node: ASTNode) : BasePsiNode(node)
class ReservedWord(node: ASTNode) : BasePsiNode(node)
class KeywordIdentifier(node: ASTNode) : BasePsiNode(node)
class ObsoleteIdentifier(node: ASTNode) : BasePsiNode(node)
class FunctionIdentifier(node: ASTNode) : BasePsiNode(node)
class TypeNameIdentifier(node: ASTNode) : BasePsiNode(node)