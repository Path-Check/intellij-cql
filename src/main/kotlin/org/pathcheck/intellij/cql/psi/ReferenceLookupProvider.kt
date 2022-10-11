package org.pathcheck.intellij.cql.psi

import com.intellij.codeInsight.lookup.LookupElementBuilder

/**
 * Provides a representation for code completion
 */
interface ReferenceLookupProvider {
    fun expandLookup(): List<LookupElementBuilder>
}