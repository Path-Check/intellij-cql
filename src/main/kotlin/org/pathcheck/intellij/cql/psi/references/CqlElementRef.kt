package org.pathcheck.intellij.cql.psi.references

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.util.IncorrectOperationException
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

    @Throws(IncorrectOperationException::class)
    override fun handleElementRename(newElementName: String): PsiElement? {
        return myElement?.let { it.setName(newElementName) }
    }

    override fun isReferenceTo(def: PsiElement): Boolean {
        val resolved = resolve()
        println("${element.text} (${element.textRange}) ${resolved?.text} (${resolved?.textRange})")
        println("Result " + def.text == resolved?.text && def.textRange == resolved?.textRange)

        return def.text == resolved?.text && def.textRange == resolved?.textRange
    }
}