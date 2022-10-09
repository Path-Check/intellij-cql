package org.pathcheck.intellij.cql

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.tree.IElementType
import com.intellij.util.ProcessingContext
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor

class CqlIdentifierCompletionProvider: CompletionProvider<CompletionParameters>() {
    override fun addCompletions(params: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val prefix = result.prefixMatcher.prefix
        if (prefix.isEmpty()) {
            return
        }

        // There must be a better way to get all identifiers of the language
        val lexer = AdaptedCqlLexer(null);
        lexer.removeErrorListeners();
        val myLexer = ANTLRLexerAdaptor(CqlLanguage, lexer)
        myLexer.start(params.originalFile.text)

        val identifiers = mutableListOf<String>()

        var type: IElementType?
        while (myLexer.tokenType.also { type = it } != null) {
            if (CqlTokenTypes.IDENTIFIERS.contains(type)) {
                identifiers.add(myLexer.tokenText)
            }

            myLexer.advance()
        }

        identifiers
            .filter { it.startsWith(prefix, true) }
            .forEach { result.addElement(LookupElementBuilder.create(it)) }
    }
}