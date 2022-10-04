package org.pathcheck.intellij.cql

import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import org.antlr.intellij.adaptor.lexer.RuleIElementType
import org.cqframework.cql.gen.cqlParser
import org.pathcheck.intellij.cql.psi.IdentifierPSINode

class CqlFindUsagesProvider : FindUsagesProvider {
    /** Is "find usages" meaningful for a kind of definition subtree?  */
    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        return  psiElement is IdentifierPSINode
    }

    override fun getHelpId(psiElement: PsiElement): String? {
        return null
    }

    override fun getWordsScanner(): WordsScanner? {
        return null
    }

    /** What kind of thing is the ID node? Can group by in "Find Usages" dialog  */
    override fun getType(element: PsiElement): String {
        // The parent of an ID node will be a RuleIElementType:
        // function, vardef, formal_arg, statement, expr, call_expr, primary
        val elType: RuleIElementType = element.parent.parent.node.elementType as RuleIElementType
        when (elType.ruleIndex) {
            cqlParser.RULE_function, cqlParser.RULE_qualifiedFunction -> return "Function"
            cqlParser.RULE_referentialIdentifier -> return "Expression"
            cqlParser.RULE_identifierOrFunctionIdentifier -> return "Function"
            //RULE_formal_arg -> return "parameter"
            //RULE_statement, RULE_expr, RULE_primary -> return "variable"
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