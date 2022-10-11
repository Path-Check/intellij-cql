package org.pathcheck.intellij.cql.utils

import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import org.hl7.elm.r1.ExpressionDef
import org.hl7.elm.r1.FunctionDef
import org.hl7.elm.r1.Library

fun Library.exportingLookups(): List<LookupElementBuilder>? {
    return statements?.def?.mapNotNull {
        if (it is FunctionDef) it.lookup() else it.lookup()
    }?.flatten()
}

fun FunctionDef.getParametersStr(): String {
    return "(" + operand.joinToString(", ") { "${it.name} ${it.resultType}" } + ")"
}

fun FunctionDef.lookup(): List<LookupElementBuilder> {
    return listOf(
        LookupElementBuilder.create(name)
            .withTailText(getParametersStr(), true)
            .withIcon(AllIcons.Nodes.Function)
            .withTypeText(resultType.toLabel())
            .withInsertHandler { context, item ->
                ParenthesesInsertHandler.getInstance(getParametersStr().length > 2)
                    .handleInsert(context, item)
            }
    )
}

fun ExpressionDef.lookup(): List<LookupElementBuilder> {
    return listOf(
        LookupElementBuilder.create(name)
            .withIcon(AllIcons.Nodes.Function)
            .withTypeText(resultType.toLabel())
    )
}