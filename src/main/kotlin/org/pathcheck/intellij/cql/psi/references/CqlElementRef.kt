package org.pathcheck.intellij.cql.psi.references

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.PsiReferenceBase
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.pathcheck.intellij.cql.psi.IdentifierPSINode

/**
 * Base class for all references to functions or expressions
 */
abstract class CqlElementRef(element: IdentifierPSINode, textRange: TextRange) :
    PsiReferenceBase<IdentifierPSINode?>(element, textRange) {

    override fun getVariants(): Array<Any> {
        return emptyArray()
    }

    override fun resolve(): PsiElement? {
        val scope = element.context as ScopeNode
        return scope.resolve(element)
    }

    override fun isReferenceTo(def: PsiElement): Boolean {
        var elem = def
        val refName: String = element.name
        if (elem is IdentifierPSINode && isDefSubtree(elem.parent)) {
            elem = elem.parent
        }
        if (isDefSubtree(elem)) {
            val id = (elem as PsiNameIdentifierOwner).nameIdentifier
            val defName = id?.text
            return defName != null && refName == defName
        }
        return false
    }

    abstract fun isDefSubtree(def: PsiElement?): Boolean
}