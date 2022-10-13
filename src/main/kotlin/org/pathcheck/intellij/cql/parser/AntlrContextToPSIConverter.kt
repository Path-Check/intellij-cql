package org.pathcheck.intellij.cql.parser

import com.intellij.lang.Language
import com.intellij.lang.PsiBuilder
import com.intellij.openapi.progress.ProgressIndicatorProvider
import org.antlr.intellij.adaptor.parser.ANTLRParseTreeToPSIConverter
import org.antlr.v4.runtime.Parser
import org.antlr.v4.runtime.ParserRuleContext

/**
 * Converter based on Context class names and not only rule name
 */
class AntlrContextToPSIConverter(language: Language?, parser: Parser?, builder: PsiBuilder?) :
    ANTLRParseTreeToPSIConverter(language, parser, builder) {
    override fun exitEveryRule(ctx: ParserRuleContext) {
        ProgressIndicatorProvider.checkCanceled()
        val marker = markers.pop()
        try {
            marker.done(CqlRuleTypes.RULE_MAP[ctx.javaClass.simpleName.removeSuffix("Context")]!!)
        } catch (e: NullPointerException) {
            throw java.lang.RuntimeException("Cannot find ${ctx.javaClass.simpleName} " +
                    "in RULE MAP with ${CqlRuleTypes.RULE_MAP.size} entries", e)
        }
    }
}