package org.pathcheck.intellij.cql;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory;
import org.antlr.intellij.adaptor.lexer.RuleIElementType;
import org.antlr.intellij.adaptor.lexer.TokenIElementType;
import org.cqframework.cql.gen.cqlLexer;
import org.cqframework.cql.gen.cqlParser;
import org.intellij.lang.annotations.MagicConstant;

import java.util.List;

public class CqlTokenTypes {
	public static IElementType BAD_TOKEN_TYPE = new IElementType("BAD_TOKEN", CqlLanguage.INSTANCE);

	public static final List<TokenIElementType> TOKEN_ELEMENT_TYPES =
		PSIElementTypeFactory.getTokenIElementTypes(CqlLanguage.INSTANCE);
	public static final List<RuleIElementType> RULE_ELEMENT_TYPES =
		PSIElementTypeFactory.getRuleIElementTypes(CqlLanguage.INSTANCE);

    public static final TokenSet COMMENTS =
		PSIElementTypeFactory.createTokenSet(
			CqlLanguage.INSTANCE,
			cqlLexer.COMMENT,
			cqlLexer.LINE_COMMENT);

	public static final TokenSet WHITESPACES =
		PSIElementTypeFactory.createTokenSet(
			CqlLanguage.INSTANCE,
			cqlLexer.WS);

	public static final TokenSet STRINGS = PSIElementTypeFactory.createTokenSet(
			CqlLanguage.INSTANCE,
			cqlLexer.STRING
	);

    public static RuleIElementType getRuleElementType(@MagicConstant(valuesFromClass = cqlParser.class)int ruleIndex){
        return RULE_ELEMENT_TYPES.get(ruleIndex);
    }
    public static TokenIElementType getTokenElementType(@MagicConstant(valuesFromClass = cqlLexer.class)int ruleIndex){
        return TOKEN_ELEMENT_TYPES.get(ruleIndex);
    }
}