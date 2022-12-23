package org.pathcheck.intellij.cql.psi.expressions

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode
import org.cqframework.cql.gen.cqlParser
import org.hl7.cql.model.DataType
import org.hl7.cql.model.IntervalType
import org.hl7.cql.model.TupleType
import org.hl7.cql.model.TupleTypeElement
import org.pathcheck.intellij.cql.psi.HasResultType
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.definitions.CodesystemIdentifier
import org.pathcheck.intellij.cql.psi.definitions.DisplayClause
import org.pathcheck.intellij.cql.psi.references.ReferentialIdentifier

class IntervalSelector(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun expression(): List<Expression>? {
        return getRules(Expression::class.java)
    }

    fun expression(i: Int): Expression? {
        return getRule(Expression::class.java, i)
    }

    override fun getResultType(): DataType? {
        val innerTypes = ensureCompatibleTypes(
            expression(0)?.getResultType(),
            expression(1)?.getResultType()
        ) ?: return null

        return IntervalType(innerTypes)
    }
}

class TupleSelector(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun tupleElementSelector(): List<TupleElementSelector>? {
        return getRules(TupleElementSelector::class.java)
    }

    fun tupleElementSelector(i: Int): TupleElementSelector? {
        return getRule(TupleElementSelector::class.java, i)
    }

    override fun getResultType(): DataType? {
        var tupleType = TupleType()
        tupleElementSelector()?.forEach {
            tupleType.addElement(TupleTypeElement(
                it.referentialIdentifier()?.text,
                it.getResultType()
            ))
        }
        return tupleType
    }
}

class TupleElementSelector(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun referentialIdentifier(): ReferentialIdentifier? {
        return getRule(ReferentialIdentifier::class.java, 0)
    }

    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    override fun getResultType() = expression()?.getResultType()
}

class InstanceSelector(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun namedTypeSpecifier(): NamedTypeSpecifier? {
        return getRule(NamedTypeSpecifier::class.java, 0)
    }

    fun instanceElementSelector(): List<InstanceElementSelector>? {
        return getRules(InstanceElementSelector::class.java)
    }

    fun instanceElementSelector(i: Int): InstanceElementSelector? {
        return getRule(InstanceElementSelector::class.java, i)
    }

    override fun getResultType() = namedTypeSpecifier()?.resolveType()
}

class InstanceElementSelector(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun referentialIdentifier(): ReferentialIdentifier? {
        return getRule(ReferentialIdentifier::class.java, 0)
    }

    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    override fun getResultType() = expression()?.getResultType()
}

class ListSelector(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun expression(): List<Expression>? {
        return getRules(Expression::class.java)
    }

    fun expression(i: Int): Expression? {
        return getRule(Expression::class.java, i)
    }

    fun typeSpecifier(): TypeSpecifier? {
        return getRule(TypeSpecifier::class.java, 0)
    }

    override fun getResultType() = typeSpecifier()?.resolveType()
}

class CodeSelector(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun STRING(): ANTLRPsiLeafNode? {
        return getToken(cqlParser.STRING, 0)
    }

    fun codesystemIdentifier(): CodesystemIdentifier? {
        return getRule(CodesystemIdentifier::class.java, 0)
    }

    fun displayClause(): DisplayClause? {
        return getRule(DisplayClause::class.java, 0)
    }

    override fun getResultType() = resolveTypeName("System", "Code")
}

class ConceptSelector(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun codeSelector(): List<CodeSelector>? {
        return getRules(CodeSelector::class.java)
    }

    fun codeSelector(i: Int): CodeSelector? {
        return getRule(CodeSelector::class.java, i)
    }

    fun displayClause(): DisplayClause? {
        return getRule(DisplayClause::class.java, 0)
    }

    override fun getResultType() = resolveTypeName("System", "Concept")
}