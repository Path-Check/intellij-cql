package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.antlr.intellij.adaptor.xpath.XPath
import org.pathcheck.intellij.cql.CqlLanguage

/** A subtree associated with a function definition.
 * Its scope is the set of arguments.
 */
class QualifiedInvocationSubtree(node: ASTNode, idElementType: IElementType) : IdentifierDefSubtree(node, idElementType), ScopeNode {
    override fun resolve(element: PsiNamedElement): PsiElement? {
        val member = listOf(
            "/qualifiedInvocation/referentialIdentifier/identifier/IDENTIFIER",
            "/qualifiedInvocation/referentialIdentifier/identifier/DELIMITEDIDENTIFIER",
            "/qualifiedInvocation/referentialIdentifier/identifier/QUOTEDIDENTIFIER",
            "/qualifiedInvocation/qualifiedFunction/identifierOrFunctionIdentifier/identifier/IDENTIFIER",
            "/qualifiedInvocation/qualifiedFunction/identifierOrFunctionIdentifier/identifier/DELIMITEDIDENTIFIER",
            "/qualifiedInvocation/qualifiedFunction/identifierOrFunctionIdentifier/identifier/QUOTEDIDENTIFIER"
        ).firstNotNullOfOrNull {
            XPath.findAll(CqlLanguage, this, it).firstOrNull()
            //SymtabUtils.resolve(this, CqlLanguage, element, it)
        }

        // Only resolves qualifiers for the members, not subset of elements
        if (member?.text != element.text || member?.textRange != member?.textRange) {
            // passes it forward.
            return (parent.context as? ScopeNode)?.resolve(element)
        }

        val qualifier = listOf(
            "/expressionTerm/expressionTerm/term/invocation/referentialIdentifier/identifier/IDENTIFIER",
            "/expressionTerm/expressionTerm/term/invocation/referentialIdentifier/identifier/DELIMITEDIDENTIFIER",
            "/expressionTerm/expressionTerm/term/invocation/referentialIdentifier/identifier/QUOTEDIDENTIFIER"
        ).firstNotNullOfOrNull {
            //SymtabUtils.resolve(this, CqlLanguage, member, it)
            XPath.findAll(CqlLanguage, this.parent, it).firstOrNull()
        }

        val qualifierDefinition = qualifier?.reference?.resolve()
        val qualifierDefinitionScope = qualifierDefinition?.context as? ScopeNode
        return qualifierDefinitionScope?.resolve(element)
    }
}