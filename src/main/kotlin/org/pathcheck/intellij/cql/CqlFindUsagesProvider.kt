package org.pathcheck.intellij.cql

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor
import org.pathcheck.intellij.cql.parser.AdaptedCqlLexer
import org.pathcheck.intellij.cql.psi.IdentifierPSINode
import org.pathcheck.intellij.cql.psi.antlr.PsiContextNodes

class CqlFindUsagesProvider : FindUsagesProvider {
    /** Is "find usages" meaningful for a kind of definition subtree?  */
    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return  psiElement is IdentifierPSINode
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        return null
    }

    override fun getWordsScanner(): WordsScanner? {
        return DefaultWordsScanner(
            ANTLRLexerAdaptor(CqlLanguage, AdaptedCqlLexer(null)),
            CqlTokenTypes.IDENTIFIERS,
            CqlTokenTypes.COMMENTS,
            CqlTokenTypes.KEYWORDS
        )
    }

    /** What kind of thing is the ID node? Can group by in "Find Usages" dialog  */
    override fun getType(element: PsiElement): String {
        // The parent of an ID node will be a RuleIElementType:
        // function, vardef, formal_arg, statement, expr, call_expr, primary
        when (element.parent.parent) {
            is PsiContextNodes.Function -> return "Function"
            is PsiContextNodes.QualifiedFunction -> return "Function"
            is PsiContextNodes.ReferentialIdentifier -> return "Function"
            is PsiContextNodes.IdentifierOrFunctionIdentifier -> return "Function"
        }

        return ""
    }

    override fun getDescriptiveName(element: PsiElement): String {
        return element.text
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        return element.text
    }
}