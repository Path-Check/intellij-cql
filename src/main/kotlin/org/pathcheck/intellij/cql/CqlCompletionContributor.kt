package org.pathcheck.intellij.cql

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import org.cqframework.cql.gen.cqlLexer

class CqlCompletionContributor : CompletionContributor() {
    init {
        // auto-completes keywords
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(CqlTokenTypes.TOKEN_ELEMENT_TYPES[cqlLexer.IDENTIFIER]),
            CqlKeywordCompletionProvider()
        )

        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(CqlTokenTypes.TOKEN_ELEMENT_TYPES[cqlLexer.IDENTIFIER]),
            CqlIdentifierCompletionProvider()
        )
    }
}