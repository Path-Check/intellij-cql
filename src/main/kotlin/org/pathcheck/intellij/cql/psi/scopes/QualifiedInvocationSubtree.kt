package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.antlr.intellij.adaptor.xpath.XPath
import org.pathcheck.intellij.cql.CqlLanguage
import org.pathcheck.intellij.cql.psi.ReferenceLookupProvider

/** A subtree associated with a function definition.
 * Its scope is the set of arguments.
 */
class QualifiedInvocationSubtree(node: ASTNode, idElementType: IElementType) : IdentifierDefSubtree(node, idElementType), ScopeNode, ReferenceLookupProvider {
    override fun resolve(element: PsiNamedElement): PsiElement? {
        val member = listOf(
            "/qualifiedInvocation/referentialIdentifier/identifier/IDENTIFIER",
            "/qualifiedInvocation/referentialIdentifier/identifier/DELIMITEDIDENTIFIER",
            "/qualifiedInvocation/referentialIdentifier/identifier/QUOTEDIDENTIFIER",
            "/qualifiedInvocation/qualifiedFunction/identifierOrFunctionIdentifier/identifier/IDENTIFIER",
            "/qualifiedInvocation/qualifiedFunction/identifierOrFunctionIdentifier/identifier/DELIMITEDIDENTIFIER",
            "/qualifiedInvocation/qualifiedFunction/identifierOrFunctionIdentifier/identifier/QUOTEDIDENTIFIER"
        ).mapNotNull {
            XPath.findAll(CqlLanguage, this, it)
        }.flatten().firstOrNull()

        // Only resolves qualifiers for the members, not subset of elements
        if (member?.text != element.text || member?.textRange != member?.textRange) {
            // passes it forward.
            return context?.resolve(element)
        }

        val qualifierDefinitionScope = getQualifierDefScope()

        if (qualifierDefinitionScope is IncludeDefSubtree) {
            return qualifierDefinitionScope.resolveInLinkedLibrary(element)
        }

        return qualifierDefinitionScope?.resolve(element)
    }

    fun getQualifier(): PsiElement? {
        return listOf(
            "/expressionTerm/expressionTerm/term/invocation/referentialIdentifier/identifier/IDENTIFIER",
            "/expressionTerm/expressionTerm/term/invocation/referentialIdentifier/identifier/DELIMITEDIDENTIFIER",
            "/expressionTerm/expressionTerm/term/invocation/referentialIdentifier/identifier/QUOTEDIDENTIFIER"
        ).mapNotNull {
            XPath.findAll(CqlLanguage, this.parent, it)
        }.flatten().firstOrNull()
    }

    fun getQualifierDefScope(): ScopeNode? {
        return getQualifier()?.reference?.resolve()?.context as? ScopeNode
    }

    override fun expandLookup(): List<LookupElementBuilder> {
        val definition = getQualifierDefScope()
        if (definition is ReferenceLookupProvider) {
            return definition.expandLookup()
        }
        return emptyList()
    }
}