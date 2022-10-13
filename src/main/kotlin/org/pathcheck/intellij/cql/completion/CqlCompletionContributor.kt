package org.pathcheck.intellij.cql.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import org.cqframework.cql.gen.cqlLexer
import org.pathcheck.intellij.cql.CqlTokenTypes

class CqlCompletionContributor : CompletionContributor() {
    init {
        // auto-completes keywords
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(CqlTokenTypes.TOKEN_ELEMENT_TYPES[cqlLexer.IDENTIFIER]),
            KeywordCompletionProvider()
        )

        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(CqlTokenTypes.TOKEN_ELEMENT_TYPES[cqlLexer.IDENTIFIER]),
            IdentifierCompletionProvider()
        )

        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(CqlTokenTypes.TOKEN_ELEMENT_TYPES[cqlLexer.IDENTIFIER]),
            ModelCompletionProvider()
        )
    }
}