package org.pathcheck.intellij.cql.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.parents
import com.intellij.util.ProcessingContext
import org.pathcheck.intellij.cql.psi.DeclaringIdentifiers
import org.pathcheck.intellij.cql.psi.LookupProvider

class IdentifierCompletionProvider: CompletionProvider<CompletionParameters>() {
    override fun addCompletions(params: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val prefix = result.prefixMatcher.prefix
        if (prefix.isEmpty()) {
            return
        }

        // from the position the cursor is in, find all visible identifiers
        params.position.parents(false)
            .filter { it is LookupProvider }
            .map { (it as LookupProvider).lookup() }
            .flatten()
            .filter { it.lookupString.startsWith(prefix, true) }
            .forEach { result.addElement(it) }
    }
}