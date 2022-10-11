package org.pathcheck.intellij.cql.utils

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import org.hl7.elm.r1.ExpressionDef
import org.hl7.elm.r1.FunctionDef
import org.hl7.elm.r1.Library

fun Library.exportingLookups(): List<LookupElementBuilder>? {
    return statements?.def?.mapNotNull {
        it.lookup()
    }?.flatten()
}

fun FunctionDef.getParametersStr(): String {
    return "(" + operand.joinToString(", ") { "${it.name} ${it.resultType}" } + ")"
}

fun ExpressionDef.lookup(): List<LookupElementBuilder> {
    return listOfNotNull(
        LookupHelper.build(
            name,
            AllIcons.Nodes.Function,
            if (this is FunctionDef) this.getParametersStr() else null,
            resultType.toLabel()
        )
    )
}