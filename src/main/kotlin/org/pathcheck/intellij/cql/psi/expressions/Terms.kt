package org.pathcheck.intellij.cql.psi.expressions

import com.intellij.lang.ASTNode
import com.intellij.openapi.diagnostic.thisLogger
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode
import org.cqframework.cql.gen.cqlParser
import org.hl7.cql.model.DataType
import org.pathcheck.intellij.cql.psi.HasResultType
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.references.Identifier

open class Term(node: ASTNode) : BasePsiNode(node), HasResultType {
    // This should never happen
    override fun getResultType(): DataType? {
        thisLogger().warn("Calling Result Type in an Object that should not exist: $this")
        return null
    }
}

class ExternalConstantTerm(node: ASTNode) : Term(node) {
    fun externalConstant(): ExternalConstant? {
        return getRule(ExternalConstant::class.java, 0)
    }

    override fun getResultType() = externalConstant()?.getResultType()
}

class TupleSelectorTerm(node: ASTNode) : Term(node) {
    fun tupleSelector(): TupleSelector? {
        return getRule(TupleSelector::class.java, 0)
    }

    override fun getResultType() = tupleSelector()?.getResultType()
}

class LiteralTerm(node: ASTNode) : Term(node) {
    fun literal(): Literal? {
        return getRule(Literal::class.java, 0)
    }

    override fun getResultType() = literal()?.getResultType()
}

class ConceptSelectorTerm(node: ASTNode) : Term(node) {
    fun conceptSelector(): ConceptSelector? {
        return getRule(ConceptSelector::class.java, 0)
    }

    override fun getResultType() = conceptSelector()?.getResultType()
}

class ParenthesizedTerm(node: ASTNode) : Term(node) {
    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    override fun getResultType() = expression()?.getResultType()
}

class CodeSelectorTerm(node: ASTNode) : Term(node) {
    fun codeSelector(): CodeSelector? {
        return getRule(CodeSelector::class.java, 0)
    }

    override fun getResultType() = codeSelector()?.getResultType()
}

class InvocationTerm(node: ASTNode) : Term(node) {
    fun invocation(): Invocation? {
        return getRule(Invocation::class.java, 0)
    }

    override fun getResultType() = invocation()?.getResultType()
}

class InstanceSelectorTerm(node: ASTNode) : Term(node) {
    fun instanceSelector(): InstanceSelector? {
        return getRule(InstanceSelector::class.java, 0)
    }

    override fun getResultType() = instanceSelector()?.getResultType()
}

class IntervalSelectorTerm(node: ASTNode) : Term(node) {
    fun intervalSelector(): IntervalSelector? {
        return getRule(IntervalSelector::class.java, 0)
    }

    override fun getResultType() = intervalSelector()?.getResultType()
}

class ListSelectorTerm(node: ASTNode) : Term(node) {
    fun listSelector(): ListSelector? {
        return getRule(ListSelector::class.java, 0)
    }

    override fun getResultType() = listSelector()?.getResultType()
}

// Utils

class ExternalConstant(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun identifier(): Identifier? {
        return getRule(Identifier::class.java, 0)
    }

    fun STRING(): ANTLRPsiLeafNode? {
        return getToken(cqlParser.STRING, 0)
    }

    override fun getResultType() = null // TODO: Undefined behavior?
}