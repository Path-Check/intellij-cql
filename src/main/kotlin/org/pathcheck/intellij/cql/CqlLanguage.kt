package org.pathcheck.intellij.cql

import com.intellij.lang.Language
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.cqframework.cql.gen.cqlParser
import org.pathcheck.intellij.cql.parser.CqlRuleTypes

object CqlLanguage: Language("Cql") {
    init {
        /**
         * Includes subclasses of rules
         */
        PSIElementTypeFactory.defineLanguageIElementTypes(
            CqlLanguage,
            cqlParser.tokenNames,
            CqlRuleTypes.RULE_NAMES
        )
    }
}

