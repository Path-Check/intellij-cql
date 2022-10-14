package org.pathcheck.intellij.cql.psi.definitions

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.pathcheck.intellij.cql.psi.LookupProvider
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.references.Identifier
import org.pathcheck.intellij.cql.utils.LookupHelper
import org.pathcheck.intellij.cql.utils.cleanText

/** A subtree associated with a query.
 * Its scope is the set of arguments.
 */
class ConceptDefinition(node: ASTNode) : BasePsiNode(node),
    ScopeNode, LookupProvider {

    fun identifier(): Identifier? {
        return getRule(Identifier::class.java, 0)
    }

    fun codeIdentifier(): List<CodeIdentifier>? {
        return getRules(CodeIdentifier::class.java)
    }

    fun codeIdentifier(i: Int): CodeIdentifier? {
        return getRule(CodeIdentifier::class.java, i)
    }

    fun accessModifier(): AccessModifier? {
        return getRule(AccessModifier::class.java, 0)
    }

    fun displayClause(): DisplayClause? {
        return getRule(DisplayClause::class.java, 0)
    }

    override fun resolve(element: PsiNamedElement): PsiElement? {
        return context?.resolve(element)
    }

    override fun lookup(): List<LookupElementBuilder> {
        return listOfNotNull(
            LookupHelper.build(
                identifier()?.cleanText(),
                AllIcons.Nodes.Gvariable,
                null,
                null
            )
        )
    }
}