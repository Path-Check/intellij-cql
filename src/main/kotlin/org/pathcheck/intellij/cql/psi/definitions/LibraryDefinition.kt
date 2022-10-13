package org.pathcheck.intellij.cql.psi.definitions

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.antlr.PsiContextNodes

/** A subtree associated with a query.
 * Its scope is the set of arguments.
 */
class LibraryDefinition(node: ASTNode) : BasePsiNode(node), ScopeNode {
    fun qualifiedIdentifier(): PsiContextNodes.QualifiedIdentifier? {
        return getRule(PsiContextNodes.QualifiedIdentifier::class.java, 0)
    }

    fun versionSpecifier(): PsiContextNodes.VersionSpecifier? {
        return getRule(PsiContextNodes.VersionSpecifier::class.java, 0)
    }

    override fun resolve(element: PsiNamedElement): PsiElement? {
        qualifiedIdentifier()?.identifier()?.all()?.firstOrNull {
            it.text == element.text
        }?.let {
            return it.parent
        }

        return context?.resolve(element)
    }
}