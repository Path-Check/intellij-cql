package org.pathcheck.intellij.cql.psi.expressions

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode
import org.cqframework.cql.gen.cqlParser
import org.hl7.cql.model.DataType
import org.pathcheck.intellij.cql.psi.HasResultType
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode

open class Literal(node: ASTNode) : BasePsiNode(node), HasResultType {
    // This should never happen
    override fun getResultType(): DataType? {
        println("Calling Result Type in an Object that should not exist: $this")
        return null
    }
}

class NullLiteral(node: ASTNode) : Literal(node) {
    override fun getResultType() = resolveTypeName("System", "Boolean")
}

class BooleanLiteral(node: ASTNode) : Literal(node) {
    override fun getResultType() = resolveTypeName("System", "Boolean")
}

class RatioLiteral(node: ASTNode) : Literal(node) {
    fun ratio(): Ratio? {
        return getRule(Ratio::class.java, 0)
    }

    override fun getResultType() = ratio()?.getResultType()
}

class StringLiteral(node: ASTNode) : Literal(node) {
    fun STRING(): ANTLRPsiLeafNode? {
        return getToken(cqlParser.STRING, 0)
    }

    override fun getResultType() = resolveTypeName("System", "String")
}

class DateTimeLiteral(node: ASTNode) : Literal(node) {
    fun DATETIME(): ANTLRPsiLeafNode? {
        return getToken(cqlParser.DATETIME, 0)
    }

    override fun getResultType() = resolveTypeName("System", "DateTime")
}

class TimeLiteral(node: ASTNode) : Literal(node) {
    fun TIME(): ANTLRPsiLeafNode? {
        return getToken(cqlParser.TIME, 0)
    }

    override fun getResultType() = resolveTypeName("System", "Time")
}

class DateLiteral(node: ASTNode) : Literal(node) {
    fun DATE(): ANTLRPsiLeafNode? {
        return getToken(cqlParser.DATE, 0)
    }

    override fun getResultType() = resolveTypeName("System", "Date")
}

class NumberLiteral(node: ASTNode) : Literal(node) {
    fun NUMBER(): ANTLRPsiLeafNode? {
        return getToken(cqlParser.NUMBER, 0)
    }

    override fun getResultType() = resolveTypeName("System", if (text.contains(".")) "Decimal" else "Integer")
}

class LongNumberLiteral(node: ASTNode) : Literal(node) {
    fun LONGNUMBER(): ANTLRPsiLeafNode? {
        return getToken(cqlParser.LONGNUMBER, 0)
    }

    override fun getResultType() = resolveTypeName("System", "Long")
}

class QuantityLiteral(node: ASTNode) : Literal(node) {
    fun quantity(): Quantity? {
        return getRule(Quantity::class.java, 0)
    }

    override fun getResultType() = quantity()?.getResultType()
}

// Utility Nodes

class Ratio(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun quantity(): List<Quantity>? {
        return getRules(Quantity::class.java)
    }

    fun quantity(i: Int): Quantity? {
        return getRule(Quantity::class.java, i)
    }

    override fun getResultType() = resolveTypeName("System", "Ratio")
}

class Quantity(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun NUMBER(): ANTLRPsiLeafNode? {
        return getToken(cqlParser.NUMBER, 0)
    }

    fun unit(): Unit? {
        return getRule(Unit::class.java, 0)
    }

    override fun getResultType() = resolveTypeName("System", "Quantity")
}

class Unit(node: ASTNode) : BasePsiNode(node) {
    fun dateTimePrecision(): DateTimePrecision? {
        return getRule(DateTimePrecision::class.java, 0)
    }

    fun pluralDateTimePrecision(): PluralDateTimePrecision? {
        return getRule(PluralDateTimePrecision::class.java, 0)
    }

    fun STRING(): ANTLRPsiLeafNode? {
        return getToken(cqlParser.STRING, 0)
    }
}

open class SimpleLiteral(node: ASTNode) : BasePsiNode(node), HasResultType {
    // This should never happen
    override fun getResultType(): DataType? {
        println("Calling Result Type in an Object that should not exist: $this")
        return null
    }
}

class SimpleNumberLiteral(node: ASTNode) : SimpleLiteral(node) {
    fun NUMBER(): ANTLRPsiLeafNode? {
        return getToken(cqlParser.NUMBER, 0)
    }

    override fun getResultType() = resolveTypeName("System", if (text.contains(".")) "Decimal" else "Integer")
}

class SimpleStringLiteral(node: ASTNode) : SimpleLiteral(node) {
    fun STRING(): ANTLRPsiLeafNode? {
        return getToken(cqlParser.STRING, 0)
    }

    override fun getResultType() = resolveTypeName("System", "String")
}

class DateTimePrecision(node: ASTNode) : BasePsiNode(node)

class PluralDateTimePrecision(node: ASTNode) : BasePsiNode(node)