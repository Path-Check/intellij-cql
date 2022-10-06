package org.pathcheck.intellij.cql

import org.antlr.v4.runtime.*
import org.cqframework.cql.gen.cqlLexer

class AdaptedCqlLexer(input: CharStream?): cqlLexer(input) {

    /**
     * Custom method to remove the SKIP stage that happens when the lexer fails to parse an indentifier
     * For instance, parsing a text with an odd number of " crashes the plugin
      */
    override fun nextToken(): Token? {
        checkNotNull(_input) { "nextToken requires a non-null input stream." }

        // Mark start location in char stream so unbuffered streams are
        // guaranteed at least have text of current token
        val tokenStartMarker: Int = _input.mark()
        try {
            outer@ while (true) {
                if (_hitEOF) {
                    emitEOF()
                    return _token
                }
                _token = null
                _channel = Token.DEFAULT_CHANNEL
                _tokenStartCharIndex = _input.index()
                _tokenStartCharPositionInLine = interpreter.charPositionInLine
                _tokenStartLine = interpreter.line
                _text = null
                do {
                    _type = Token.INVALID_TYPE
                    var ttype = try {
                        interpreter.match(_input, _mode)
                    } catch (e: LexerNoViableAltException) {
                        notifyListeners(e) // report error
                        recover(e)
                        Lexer.HIDDEN
                    }
                    if (_input.LA(1) == IntStream.EOF) {
                        _hitEOF = true
                    }
                    if (_type == Token.INVALID_TYPE) _type = ttype
                    if (_type == Lexer.SKIP) {
                        continue@outer
                    }
                } while (_type == Lexer.MORE)
                if (_token == null) emit()
                return _token
            }
        } finally {
            // make sure we release marker after match or
            // unbuffered char stream will keep buffering
            _input.release(tokenStartMarker)
        }
    }
}