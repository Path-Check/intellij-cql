package org.pathcheck.intellij.cql

import com.intellij.psi.tree.TokenSet
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import org.antlr.v4.runtime.VocabularyImpl
import org.cqframework.cql.gen.cqlLexer

object CqlTokenTypes {
    val TOKEN_ELEMENT_TYPES: List<TokenIElementType> by lazy {
        PSIElementTypeFactory.getTokenIElementTypes(CqlLanguage)!!
    }

    val COMMENTS: TokenSet by lazy {
        PSIElementTypeFactory.createTokenSet(
            CqlLanguage,
            cqlLexer.COMMENT,
            cqlLexer.LINE_COMMENT
        )!!
    }
    val WHITESPACES: TokenSet by lazy {
        PSIElementTypeFactory.createTokenSet(
            CqlLanguage,
            cqlLexer.WS
        )!!
    }
    val STRINGS: TokenSet by lazy {
        PSIElementTypeFactory.createTokenSet(
            CqlLanguage,
            cqlLexer.STRING
        )!!
    }

    val IDENTIFIERS: TokenSet by lazy {
        PSIElementTypeFactory.createTokenSet(
            CqlLanguage,
            cqlLexer.IDENTIFIER,
            cqlLexer.QUOTEDIDENTIFIER,
            cqlLexer.DELIMITEDIDENTIFIER
        )!!
    }

    val KEYWORDS: TokenSet by lazy {
        PSIElementTypeFactory.createTokenSet(
            CqlLanguage,
            cqlLexer.T__0, // 'library'
            cqlLexer.T__1, // 'version'
            cqlLexer.T__2, // 'using'
            cqlLexer.T__3, // 'called'
            cqlLexer.T__4, // 'include'
            cqlLexer.T__5, // 'public'
            cqlLexer.T__6, // 'private'
            cqlLexer.T__7, // 'parameter'
            cqlLexer.T__8, // 'codesystem'
            cqlLexer.T__9, // 'default'
            cqlLexer.T__11, // 'valueset'
            cqlLexer.T__12, // 'codesystems'
            cqlLexer.T__17, // 'code'
            cqlLexer.T__18, // 'from'
            cqlLexer.T__19, // 'concept'
            cqlLexer.T__20, // 'List'
            cqlLexer.T__23, // 'Interval'
            cqlLexer.T__24, // 'Tuple'
            cqlLexer.T__25, // 'Choice'
            cqlLexer.T__26, // 'define'
            cqlLexer.T__27, // 'context'
            cqlLexer.T__28, // 'fluent'
            cqlLexer.T__29, // 'function'
            cqlLexer.T__32, // 'returns'
            cqlLexer.T__33, // 'external'
            cqlLexer.T__34, // 'with'
            cqlLexer.T__35, // 'such that'
            cqlLexer.T__36, // 'without'
            cqlLexer.T__40, // 'in'
            cqlLexer.T__43, // 'let'
            cqlLexer.T__44, // 'where'
            cqlLexer.T__45, // 'return'
            cqlLexer.T__46, // 'all'
            cqlLexer.T__47, // 'distinct'
            cqlLexer.T__48, // 'aggregate'
            cqlLexer.T__49, // 'starting'
            cqlLexer.T__50, // 'sort'
            cqlLexer.T__51, // 'by'
            cqlLexer.T__52, // 'asc'
            cqlLexer.T__53, // 'ascending'
            cqlLexer.T__54, // 'desc'
            cqlLexer.T__55, // 'descending'
            cqlLexer.T__56, // 'is'
            cqlLexer.T__57, // 'not'
            cqlLexer.T__58, // 'null'
            cqlLexer.T__59, // 'true'
            cqlLexer.T__60, // 'false'
            cqlLexer.T__61, // 'as'
            cqlLexer.T__62, // 'cast'
            cqlLexer.T__63, // 'exists'
            cqlLexer.T__64, // 'properly'
            cqlLexer.T__65, // 'between'
            cqlLexer.T__66, // 'and'
            cqlLexer.T__67, // 'duration'
            cqlLexer.T__68, // 'difference'
            cqlLexer.T__73, // 'contains'
            cqlLexer.T__74, // 'or'
            cqlLexer.T__75, // 'xor'
            cqlLexer.T__76, // 'implies'
            cqlLexer.T__78, // 'union'
            cqlLexer.T__79, // 'intersect'
            cqlLexer.T__80, // 'except'
            cqlLexer.T__81, // 'year'
            cqlLexer.T__82, // 'month'
            cqlLexer.T__83, // 'week'
            cqlLexer.T__84, // 'day'
            cqlLexer.T__85, // 'hour'
            cqlLexer.T__86, // 'minute'
            cqlLexer.T__87, // 'second'
            cqlLexer.T__88, // 'millisecond'
            cqlLexer.T__89, // 'date'
            cqlLexer.T__90, // 'time'
            cqlLexer.T__91, // 'timezone'
            cqlLexer.T__92, // 'timezoneoffset'
            cqlLexer.T__93, // 'years'
            cqlLexer.T__94, // 'months'
            cqlLexer.T__95, // 'weeks'
            cqlLexer.T__96, // 'days'
            cqlLexer.T__97, // 'hours'
            cqlLexer.T__98, // 'minutes'
            cqlLexer.T__99, // 'seconds'
            cqlLexer.T__100, // 'milliseconds'
            cqlLexer.T__101, // 'convert'
            cqlLexer.T__102, // 'to'
            cqlLexer.T__105, // 'start'
            cqlLexer.T__106, // 'end'
            cqlLexer.T__107, // 'of'
            cqlLexer.T__108, // 'width'
            cqlLexer.T__109, // 'successor'
            cqlLexer.T__110, // 'predecessor'
            cqlLexer.T__111, // 'singleton'
            cqlLexer.T__112, // 'point'
            cqlLexer.T__113, // 'minimum'
            cqlLexer.T__114, // 'maximum'
            cqlLexer.T__118, // 'div'
            cqlLexer.T__119, // 'mod'
            cqlLexer.T__120, // '&'
            cqlLexer.T__121, // 'if'
            cqlLexer.T__122, // 'then'
            cqlLexer.T__123, // 'else'
            cqlLexer.T__124, // 'case'
            cqlLexer.T__125, // 'flatten'
            cqlLexer.T__126, // 'expand'
            cqlLexer.T__127, // 'collapse'
            cqlLexer.T__128, // 'per'
            cqlLexer.T__129, // 'when'
            cqlLexer.T__130, // 'or before'
            cqlLexer.T__131, // 'or after'
            cqlLexer.T__132, // 'or more'
            cqlLexer.T__133, // 'or less'
            cqlLexer.T__134, // 'less than'
            cqlLexer.T__135, // 'more than'
            cqlLexer.T__136, // 'on or'
            cqlLexer.T__137, // 'before'
            cqlLexer.T__138, // 'after'
            cqlLexer.T__139, // 'or on'
            cqlLexer.T__140, // 'starts'
            cqlLexer.T__141, // 'ends'
            cqlLexer.T__142, // 'occurs'
            cqlLexer.T__143, // 'same'
            cqlLexer.T__144, // 'includes'
            cqlLexer.T__145, // 'during'
            cqlLexer.T__146, // 'included in'
            cqlLexer.T__147, // 'within'
            cqlLexer.T__148, // 'meets'
            cqlLexer.T__149, // 'overlaps'
            cqlLexer.T__150, // '$this'
            cqlLexer.T__151, // '$index'
            cqlLexer.T__152, // '$total'
            cqlLexer.T__153, // 'display'
            cqlLexer.T__154, // 'Code'
            cqlLexer.T__155, // 'Concept'
            cqlLexer.T__156 // '%'
        )
    }

    val KEYWORDNames: List<String> by lazy {
        (cqlLexer.VOCABULARY as VocabularyImpl).literalNames
            .filterNotNull()
            .map { it.substring(1, it.length-1) } // removes quotes
    }
}