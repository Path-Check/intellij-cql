package org.pathcheck.intellij.cql

import com.intellij.patterns.PatternCondition
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext

/**
 * Pattern which only accepts PsiElements, which are a keyword the current language.
 */
internal class CqlKeywordPattern : PatternCondition<PsiElement>("keywordPattern()") {
    override fun accepts(psi: PsiElement, context: ProcessingContext): Boolean {
        println(psi)

        val node = psi.node ?: return false
        return CqlTokenTypes.KEYWORDS.contains(node.elementType)
    }
}