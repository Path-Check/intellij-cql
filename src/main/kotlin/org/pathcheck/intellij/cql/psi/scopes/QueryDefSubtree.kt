package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.antlr.intellij.adaptor.xpath.XPath
import org.pathcheck.intellij.cql.CqlLanguage
import org.pathcheck.intellij.cql.psi.DeclaringIdentifiers
import org.pathcheck.intellij.cql.psi.LookupProvider
import org.pathcheck.intellij.cql.utils.LookupHelper
import org.pathcheck.intellij.cql.utils.cleanText

/** A subtree associated with a query.
 * Its scope is the set of arguments.
 */
class QueryDefSubtree(node: ASTNode, idElementType: IElementType) : IdentifierDefSubtree(node, idElementType), ScopeNode, DeclaringIdentifiers, LookupProvider {
    /**
     * User clicked in the [element]. This function tries to find the definition token related to the [element]
     * inside this subtree and return it.
     */
    override fun resolve(element: PsiNamedElement): PsiElement? {
        // only resolves if it comes from a non-definition element of the current node.
        listOf(
            "/query/sourceClause/aliasedQuerySource/querySource/qualifiedIdentifierExpression/referentialIdentifier/identifier/IDENTIFIER",
            "/query/sourceClause/aliasedQuerySource/querySource/qualifiedIdentifierExpression/referentialIdentifier/identifier/DELIMITEDIDENTIFIER",
            "/query/sourceClause/aliasedQuerySource/querySource/qualifiedIdentifierExpression/referentialIdentifier/identifier/QUOTEDIDENTIFIER",

            "/query/sourceClause/aliasedQuerySource/querySource/qualifiedIdentifierExpression/qualifierExpression/referentialIdentifier/identifier/IDENTIFIER",
            "/query/sourceClause/aliasedQuerySource/querySource/qualifiedIdentifierExpression/qualifierExpression/referentialIdentifier/identifier/DELIMITEDIDENTIFIER",
            "/query/sourceClause/aliasedQuerySource/querySource/qualifiedIdentifierExpression/qualifierExpression/referentialIdentifier/identifier/QUOTEDIDENTIFIER",
        ).mapNotNull {
            XPath.findAll(CqlLanguage, this, it)
        }.flatten().firstOrNull()?.let {
            if (it.text == element.text && it.textRange == element.textRange)
                return context?.resolve(element)
        }

        // Finds the aliases that were defined under this subtree and checks if the element is one of these aliases.
        visibleIdentifiers().firstOrNull() {
            it.text == element.text
        }?.let {
            return it.parent
        }

        // sends to parent scope.
        return context?.resolve(element)
    }

    override fun visibleIdentifiers(): List<PsiElement> {
        return listOf(
            "/query/sourceClause/aliasedQuerySource/alias/identifier/IDENTIFIER",
            "/query/sourceClause/aliasedQuerySource/alias/identifier/DELIMITEDIDENTIFIER",
            "/query/sourceClause/aliasedQuerySource/alias/identifier/QUOTEDIDENTIFIER",
        ).mapNotNull {
            XPath.findAll(CqlLanguage, this, it)
        }.flatten()
    }

    fun getAliasName(): PsiElement? {
        return XPath.findAll(CqlLanguage, this, "/query/sourceClause/aliasedQuerySource/alias/identifier").firstOrNull()
    }

    override fun lookup(): List<LookupElementBuilder> {
        return listOfNotNull(
            LookupHelper.build(
                getAliasName()?.cleanText(),
                AllIcons.Nodes.Variable,
                null,
                null
            )
        )
    }
}