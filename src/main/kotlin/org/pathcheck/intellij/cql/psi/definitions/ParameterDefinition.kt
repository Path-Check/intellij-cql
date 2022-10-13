package org.pathcheck.intellij.cql.psi.definitions

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.pathcheck.intellij.cql.psi.LookupProvider
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.antlr.PsiContextNodes
import org.pathcheck.intellij.cql.psi.Identifier
import org.pathcheck.intellij.cql.utils.LookupHelper
import org.pathcheck.intellij.cql.utils.cleanText

/** A subtree associated with a query.
 * Its scope is the set of arguments.
 */
class ParameterDefinition(node: ASTNode) : BasePsiNode(node), ScopeNode, LookupProvider {

    fun identifier(): Identifier? {
        return getRule(Identifier::class.java, 0)
    }

    fun accessModifier(): PsiContextNodes.AccessModifier? {
        return getRule(PsiContextNodes.AccessModifier::class.java, 0)
    }

    fun typeSpecifier(): PsiContextNodes.TypeSpecifier? {
        return getRule(PsiContextNodes.TypeSpecifier::class.java, 0)
    }

    fun expression(): PsiContextNodes.Expression? {
        return getRule(PsiContextNodes.Expression::class.java, 0)
    }

    override fun resolve(element: PsiNamedElement): PsiElement? {
        return context?.resolve(element)
    }

    override fun lookup(): List<LookupElementBuilder> {
        return listOfNotNull(
            LookupHelper.build(
                identifier()?.cleanText(),
                AllIcons.Nodes.Parameter,
                null,
                typeSpecifier()?.cleanText()
            )
        )
    }
}