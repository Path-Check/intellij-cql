package org.pathcheck.intellij.cql.psi.definitions

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.pathcheck.intellij.cql.psi.*
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.antlr.PsiContextNodes
import org.pathcheck.intellij.cql.utils.LookupHelper
import org.pathcheck.intellij.cql.utils.cleanText

/** A subtree associated with a query.
 * Its scope is the set of arguments.
 */
class ContextDefinition(node: ASTNode) : BasePsiNode(node), ScopeNode, LookupProvider, ReferenceLookupProvider {
    fun identifier(): Identifier? {
        return getRule(Identifier::class.java, 0)
    }

    fun modelIdentifier(): PsiContextNodes.ModelIdentifier? {
        return getRule(PsiContextNodes.ModelIdentifier::class.java, 0)
    }

    override fun resolve(element: PsiNamedElement): PsiElement? {
        identifier()?.all()?.firstOrNull() {
            it.text == element.name
        }?.let {
            return it.parent
        }

        return context?.resolve(element)
    }

    override fun lookup(): List<LookupElementBuilder> {
        return listOfNotNull(
            LookupHelper.build(
                identifier()?.cleanText(),
                AllIcons.Nodes.Package,
                null,
                null
            )
        )
    }

    override fun expandLookup(): List<LookupElementBuilder> {
        val labelName = identifier() ?: return emptyList()

        return (parent.parent as Library).findModels().mapNotNull { model ->
            model.resolveLabel(labelName.text)?.elements?.map {
                LookupElementBuilder.create(it.name)
                    .withTypeText(it.type.toLabel(), true)
                    .withIcon(AllIcons.Nodes.Field)
            }
        }.flatten()
    }
}