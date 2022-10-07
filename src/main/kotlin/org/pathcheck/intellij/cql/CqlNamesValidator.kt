package org.pathcheck.intellij.cql

import com.intellij.lang.refactoring.NamesValidator
import com.intellij.openapi.project.Project
import org.antlr.v4.runtime.*
import org.cqframework.cql.gen.cqlLexer
import org.cqframework.cql.gen.cqlParser

/**
 * Encapsulates the knowledge about identifier rules and keyword set of the assigned language.
 *
 *
 * Register in `com.intellij.lang.namesValidator` extension point.
 * @see com.intellij.lang.LanguageNamesValidation
 *
 * @see [Rename Refactoring](https://plugins.jetbrains.com/docs/intellij/rename-refactoring.html) */
class CqlNamesValidator : NamesValidator {
    /**
     * Checks if the specified string is a keyword in the custom language.
     *
     * @param name    the string to check
     * @param project the project in the context of which the check is done
     * @return `true` if the string is a keyword, `false` otherwise
     */
    override fun isKeyword(name: String, project: Project): Boolean {
        return CqlTokenTypes.KEYWORDNames.contains(name)
    }

    /**
     * Checks if the specified string is a valid identifier in the custom language.
     *
     * @param name    the string to check
     * @param project the project in the context of which the check is done
     * @return `true` if the string is a valid identifier, `false` otherwise
     */
    override fun isIdentifier(name: String, project: Project): Boolean {
        return canCompile(name)
    }

    private fun canCompile(text: String): Boolean {
        // Not sure if there is an easier way to check it text matches identifier subtree in the grammar.

        val errorCounter = ErrorCounter();

        val lexer = cqlLexer(CharStreams.fromString("define $text: 5"))
        lexer.removeErrorListeners()
        lexer.addErrorListener(errorCounter)
        val tokens = CommonTokenStream(lexer)
        val parser = cqlParser(tokens)
        parser.buildParseTree = true
        parser.removeErrorListeners() // Clear the default console listener
        parser.addErrorListener(errorCounter)

        parser.library()

        return errorCounter.errorCounter == 0
    }

    class ErrorCounter: BaseErrorListener() {
        var errorCounter = 0;

        override fun syntaxError(
            recognizer: Recognizer<*, *>?,
            offendingSymbol: Any?,
            line: Int,
            charPositionInLine: Int,
            msg: String?,
            e: RecognitionException?
        ) {
            errorCounter++
        }
    }

}