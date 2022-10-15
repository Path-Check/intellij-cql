package org.pathcheck.intellij.cql.psi.references

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.util.IncorrectOperationException
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.pathcheck.intellij.cql.psi.IdentifierPSINode
import org.pathcheck.intellij.cql.psi.scopes.QualifiedMemberInvocation

/**
 * Represents any resolvable identifier in the editor
 */
class CqlReference(element: IdentifierPSINode) :
    PsiReferenceBase<IdentifierPSINode?>(element, TextRange(0, element.text.length)) {

    override fun getVariants(): Array<Any> {
        val scope = element.context
        if (scope is QualifiedMemberInvocation) {
            return scope.expandLookup().toTypedArray()
        }

        println("Variants not found for scope $scope")

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
        return def.text == resolved?.text && def.textRange == resolved?.textRange
    }
}