package org.pathcheck.intellij.cql.psi.references

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.pathcheck.intellij.cql.psi.IdentifierPSINode

/** A reference object associated with (referring to) a IdentifierPSINode
 * underneath a call_expr rule subtree root.
 */
class ExpressionRef(element: IdentifierPSINode) : CqlElementRef(element, TextRange(0, element.text.length)) {
    override fun isDefSubtree(def: PsiElement?): Boolean {
        return false
    }
}