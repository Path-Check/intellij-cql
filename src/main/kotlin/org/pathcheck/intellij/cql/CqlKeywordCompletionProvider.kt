package org.pathcheck.intellij.cql

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

class CqlKeywordCompletionProvider: CompletionProvider<CompletionParameters>() {
    override fun addCompletions(params: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val prefix = result.prefixMatcher.prefix
        if (prefix.isEmpty()) {
            return
        }

        CqlTokenTypes.KEYWORDNames
            .filter { it.startsWith("$prefix", true) }
            .forEach { result.addElement(LookupElementBuilder.create(it)) }
    }
}