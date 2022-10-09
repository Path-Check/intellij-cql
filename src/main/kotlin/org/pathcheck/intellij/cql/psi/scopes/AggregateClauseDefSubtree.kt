package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.pathcheck.intellij.cql.CqlLanguage

/** A subtree associated with a function definition.
 * Its scope is the set of arguments.
 */
class AggregateClauseDefSubtree(node: ASTNode, idElementType: IElementType) : IdentifierDefSubtree(node, idElementType), ScopeNode {
    override fun resolve(element: PsiNamedElement): PsiElement? {
        listOf(
            "/aggregateClause/identifier/IDENTIFIER",
            "/aggregateClause/identifier/DELIMITEDIDENTIFIER",
            "/aggregateClause/identifier/QUOTEDIDENTIFIER"
        ).firstNotNullOfOrNull {
            SymtabUtils.resolve(this, CqlLanguage, element, it)
        }?.let {
            return it
        }

        // sends to parent scope.
        return (parent.context as? ScopeNode)?.resolve(element)
    }
}