package org.pathcheck.intellij.cql.parser

import org.antlr.intellij.adaptor.lexer.RuleIElementType
import org.cqframework.cql.gen.cqlParser
import org.pathcheck.intellij.cql.CqlLanguage

object CqlRuleTypes {
    // All Rule names, including subrules (subclasses)
    val RULE_NAMES = cqlParser::class.nestedClasses.map {
        it.simpleName!!.removeSuffix("Context")
    }.toTypedArray()

    // List of Indexed Rule Types (required by IntelliJ)
    private val RULES: List<RuleIElementType> = RULE_NAMES.mapIndexed { index, s ->
        RuleIElementType(index, s, CqlLanguage)
    }

    // Map between Context names and Rule Types
    val RULE_MAP: Map<String, RuleIElementType> = RULES.associateBy { it.debugName }
}