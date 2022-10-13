package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.pathcheck.intellij.cql.psi.antlr.PsiContextNodes
import org.pathcheck.intellij.cql.psi.ReferenceLookupProvider
import org.pathcheck.intellij.cql.psi.definitions.IncludeDefinition

/** A subtree associated with a function definition.
 * Its scope is the set of arguments.
 */
class QualifiedMemberInvocation(node: ASTNode) : PsiContextNodes.QualifiedInvocation(node), ScopeNode, ReferenceLookupProvider {
    fun referentialIdentifier(): PsiContextNodes.ReferentialIdentifier? {
        return getRule(PsiContextNodes.ReferentialIdentifier::class.java, 0)
    }

    override fun resolve(element: PsiNamedElement): PsiElement? {
        val member = referentialIdentifier()?.identifier()?.any()

        // Only resolves qualifiers for the members, not subset of elements
        if (member?.text != element.text || member?.textRange != member?.textRange) {
            // passes it forward.
            return context?.resolve(element)
        }

        if (parent is InvocationExpressionTerm) {
            val qualifierDefinitionScope = (parent as InvocationExpressionTerm).getQualifierDefScope()

            if (qualifierDefinitionScope is IncludeDefinition) {
                return qualifierDefinitionScope.resolveInLinkedLibrary(element)
            }

            return qualifierDefinitionScope?.resolve(element)
        }

        return null;
    }

    override fun expandLookup(): List<LookupElementBuilder> {
        if (parent is InvocationExpressionTerm) {
            val definition = (parent as InvocationExpressionTerm).getQualifierDefScope()
            if (definition is ReferenceLookupProvider) {
                return definition.expandLookup()
            }
        }

        return emptyList()
    }
}