package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.elementType
import com.intellij.psi.util.findParentInFile
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.lexer.RuleIElementType
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.antlr.intellij.adaptor.xpath.XPath
import org.apache.xmlbeans.impl.xb.xsdschema.FieldDocument.Field.Xpath
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
        // only resolves if it comes from a non-definition element of the current node.
        listOf(
            "/query/sourceClause/aliasedQuerySource/querySource/qualifiedIdentifierExpression/referentialIdentifier/identifier/IDENTIFIER",
            "/query/sourceClause/aliasedQuerySource/querySource/qualifiedIdentifierExpression/referentialIdentifier/identifier/DELIMITEDIDENTIFIER",
            "/query/sourceClause/aliasedQuerySource/querySource/qualifiedIdentifierExpression/referentialIdentifier/identifier/QUOTEDIDENTIFIER",

            "/query/sourceClause/aliasedQuerySource/querySource/qualifiedIdentifierExpression/qualifierExpression/referentialIdentifier/identifier/IDENTIFIER",
            "/query/sourceClause/aliasedQuerySource/querySource/qualifiedIdentifierExpression/qualifierExpression/referentialIdentifier/identifier/DELIMITEDIDENTIFIER",
            "/query/sourceClause/aliasedQuerySource/querySource/qualifiedIdentifierExpression/qualifierExpression/referentialIdentifier/identifier/QUOTEDIDENTIFIER",
        ).firstNotNullOfOrNull {
            XPath.findAll(CqlLanguage, this, it).firstOrNull()
        }?.let {
            if (it.text == element.text && it.textRange == element.textRange)
                return (parent.context as? ScopeNode)?.resolve(element)
        }

        // Finds the aliases that were defined under this subtree and checks if the element is one of these aliases.
        listOf(
            "/query/sourceClause/aliasedQuerySource/alias/identifier/IDENTIFIER",
            "/query/sourceClause/aliasedQuerySource/alias/identifier/DELIMITEDIDENTIFIER",
            "/query/sourceClause/aliasedQuerySource/alias/identifier/QUOTEDIDENTIFIER",
        ).firstNotNullOfOrNull {
            SymtabUtils.resolve(this, CqlLanguage, element, it)
        }?.let {
            return it
        }

        // sends to parent scope.
        return (parent.context as? ScopeNode)?.resolve(element)
    }
}