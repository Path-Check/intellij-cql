package org.pathcheck.intellij.cql.psi.references

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.util.IncorrectOperationException
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.pathcheck.intellij.cql.psi.IdentifierPSINode
import org.pathcheck.intellij.cql.psi.scopes.QualifiedIdentifierExpression
import org.pathcheck.intellij.cql.psi.scopes.QualifiedMemberInvocation

/**
 * Represents any resolvable identifier in the editor
 */
class CqlReference(element: IdentifierPSINode) :
    PsiReferenceBase<IdentifierPSINode?>(element, TextRange(0, element.text.length)) {

    override fun getVariants(): Array<Any> {
        val scope = element.context
        thisLogger().debug("GetVariants called for element ${element.text} and scope $scope")

        if (scope is QualifiedMemberInvocation) {
            return scope.expandLookup().toTypedArray()
        }

        if (scope is QualifiedIdentifierExpression) {
            return scope.expandLookup().toTypedArray()
        }

        thisLogger().debug("Variant Implementation not found for scope $scope")

        return emptyArray()
    }

    override fun resolve(): PsiElement? {
        val scope = element.context as ScopeNode
        thisLogger().debug("Resolving Reference ${element.text}. Scope found: $scope")
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