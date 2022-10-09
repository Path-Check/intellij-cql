package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.elementType
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.lexer.RuleIElementType
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.cqframework.cql.gen.cqlParser
import org.pathcheck.intellij.cql.CqlLanguage

/** A subtree associated with a query.
 * Its scope is the set of arguments.
 */
class QueryDefSubtree(node: ASTNode, idElementType: IElementType) : IdentifierDefSubtree(node, idElementType), ScopeNode {
    /**
     * User clicked in the [element]. This function tries to find the definition token related to the [element]
     * inside this subtree and return it.
     */
    override fun resolve(element: PsiNamedElement): PsiElement? {
        // only resolves if it comes from a non-definition element
        var checkElementIsFromAlias: PsiElement = element
        while (checkElementIsFromAlias.parent !is QueryDefSubtree) {
            if (checkElementIsFromAlias.node.elementType is RuleIElementType
                && (checkElementIsFromAlias.node.elementType as RuleIElementType).ruleIndex == cqlParser.RULE_querySource) {
                // passes it forward.
                return (parent.context as? ScopeNode)?.resolve(element)
            }

            checkElementIsFromAlias = checkElementIsFromAlias.parent
        }

        // Finds the aliases that were defined under this subtree and checks if the element is one of these aliases.
        return listOf(
            "/query/sourceClause/aliasedQuerySource/alias/identifier/IDENTIFIER",
            "/query/sourceClause/aliasedQuerySource/alias/identifier/DELIMITEDIDENTIFIER",
            "/query/sourceClause/aliasedQuerySource/alias/identifier/QUOTEDIDENTIFIER",
        ).firstNotNullOfOrNull {
            SymtabUtils.resolve(this, CqlLanguage, element, it)
        }
    }
}