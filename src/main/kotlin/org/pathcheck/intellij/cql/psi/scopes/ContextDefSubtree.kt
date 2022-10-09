package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.pathcheck.intellij.cql.CqlLanguage

/** A subtree associated with a query.
 * Its scope is the set of arguments.
 */
class ContextDefSubtree(node: ASTNode, idElementType: IElementType) : IdentifierDefSubtree(node, idElementType), ScopeNode {
    override fun resolve(element: PsiNamedElement): PsiElement? {
        return listOf(
            "/contextDefinition/identifier/IDENTIFIER",
            "/contextDefinition/identifier/DELIMITEDIDENTIFIER",
            "/contextDefinition/identifier/QUOTEDIDENTIFIER",
        ).firstNotNullOfOrNull {
            return SymtabUtils.resolve(this, CqlLanguage, element, it)
        }
    }
}