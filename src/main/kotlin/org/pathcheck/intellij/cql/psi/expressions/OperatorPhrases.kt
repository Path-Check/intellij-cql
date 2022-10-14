package org.pathcheck.intellij.cql.psi.expressions

import com.intellij.lang.ASTNode
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode

open class IntervalOperatorPhrase(node: ASTNode) : BasePsiNode(node)
class WithinIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
    fun quantity(): Quantity? {
        return getRule(Quantity::class.java, 0)
    }
}

class IncludedInIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
    fun dateTimePrecisionSpecifier(): DateTimePrecisionSpecifier? {
        return getRule(DateTimePrecisionSpecifier::class.java, 0)
    }
}

class EndsIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
    fun dateTimePrecisionSpecifier(): DateTimePrecisionSpecifier? {
        return getRule(DateTimePrecisionSpecifier::class.java, 0)
    }
}

class ConcurrentWithIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
    fun relativeQualifier(): RelativeQualifier? {
        return getRule(RelativeQualifier::class.java, 0)
    }

    fun dateTimePrecision(): DateTimePrecision? {
        return getRule(DateTimePrecision::class.java, 0)
    }
}

class OverlapsIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
    fun dateTimePrecisionSpecifier(): DateTimePrecisionSpecifier? {
        return getRule(DateTimePrecisionSpecifier::class.java, 0)
    }
}

class IncludesIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
    fun dateTimePrecisionSpecifier(): DateTimePrecisionSpecifier? {
        return getRule(DateTimePrecisionSpecifier::class.java, 0)
    }
}

class BeforeOrAfterIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
    fun temporalRelationship(): TemporalRelationship? {
        return getRule(TemporalRelationship::class.java, 0)
    }

    fun quantityOffset(): QuantityOffset? {
        return getRule(QuantityOffset::class.java, 0)
    }

    fun dateTimePrecisionSpecifier(): DateTimePrecisionSpecifier? {
        return getRule(DateTimePrecisionSpecifier::class.java, 0)
    }
}

class MeetsIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
    fun dateTimePrecisionSpecifier(): DateTimePrecisionSpecifier? {
        return getRule(DateTimePrecisionSpecifier::class.java, 0)
    }
}

class StartsIntervalOperatorPhrase(node: ASTNode) : IntervalOperatorPhrase(node) {
    fun dateTimePrecisionSpecifier(): DateTimePrecisionSpecifier? {
        return getRule(DateTimePrecisionSpecifier::class.java, 0)
    }
}

class DateTimePrecisionSpecifier(node: ASTNode) : BasePsiNode(node) {
    fun dateTimePrecision(): DateTimePrecision? {
        return getRule(DateTimePrecision::class.java, 0)
    }
}

class RelativeQualifier(node: ASTNode) : BasePsiNode(node)
class OffsetRelativeQualifier(node: ASTNode) : BasePsiNode(node)
class ExclusiveRelativeQualifier(node: ASTNode) : BasePsiNode(node)
class QuantityOffset(node: ASTNode) : BasePsiNode(node) {
    fun quantity(): Quantity? {
        return getRule(Quantity::class.java, 0)
    }

    fun offsetRelativeQualifier(): OffsetRelativeQualifier? {
        return getRule(OffsetRelativeQualifier::class.java, 0)
    }

    fun exclusiveRelativeQualifier(): ExclusiveRelativeQualifier? {
        return getRule(ExclusiveRelativeQualifier::class.java, 0)
    }
}

class TemporalRelationship(node: ASTNode) : BasePsiNode(node)