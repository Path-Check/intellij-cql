package org.pathcheck.intellij.cql.psi.definitions

import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.pathcheck.intellij.cql.psi.DeclaringIdentifiers
import org.pathcheck.intellij.cql.psi.LookupProvider
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.antlr.PsiContextNodes
import org.pathcheck.intellij.cql.utils.cleanText

/** A subtree associated with a function definition.
 * Its scope is the set of arguments.
 */
class FunctionDefinition(node: ASTNode) : BasePsiNode(node), ScopeNode, DeclaringIdentifiers, LookupProvider {
    fun identifierOrFunctionIdentifier(): PsiContextNodes.IdentifierOrFunctionIdentifier? {
        return getRule(PsiContextNodes.IdentifierOrFunctionIdentifier::class.java, 0)
    }

    fun functionBody(): PsiContextNodes.FunctionBody? {
        return getRule(PsiContextNodes.FunctionBody::class.java, 0)
    }

    fun accessModifier(): PsiContextNodes.AccessModifier? {
        return getRule(PsiContextNodes.AccessModifier::class.java, 0)
    }

    fun fluentModifier(): PsiContextNodes.FluentModifier? {
        return getRule(PsiContextNodes.FluentModifier::class.java, 0)
    }

    fun operandDefinition(): List<PsiContextNodes.OperandDefinition>? {
        return getRules(PsiContextNodes.OperandDefinition::class.java)
    }

    fun operandDefinition(i: Int): PsiContextNodes.OperandDefinition? {
        return getRule(PsiContextNodes.OperandDefinition::class.java, i)
    }

    fun typeSpecifier(): PsiContextNodes.TypeSpecifier? {
        return getRule(PsiContextNodes.TypeSpecifier::class.java, 0)
    }

    override fun resolve(element: PsiNamedElement): PsiElement? {
        visibleIdentifiers().firstOrNull() {
            it.text == element.text
        }?.let {
            return it.parent
        }

        return context?.resolve(element)
    }

    override fun visibleIdentifiers(): List<PsiElement> {
        return operandDefinition()?.map {
            listOfNotNull(it.referentialIdentifier()?.identifier(), it.referentialIdentifier()?.keywordIdentifier())
        }?.flatten() ?: emptyList()
    }

    fun getParametersStr(): String {
        return "(" + operandDefinition()?.joinToString(", ") { it.cleanText() } + ")"
    }

    override fun lookup(): List<LookupElementBuilder> {
        val functionName = identifierOrFunctionIdentifier() ?: return emptyList();

        return listOfNotNull(
            LookupElementBuilder.create(functionName.cleanText())
                .withTailText(getParametersStr(), true)
                .withIcon(AllIcons.Nodes.Function)
                .withInsertHandler { context, item ->
                    ParenthesesInsertHandler.getInstance(getParametersStr().length > 2)
                        .handleInsert(context, item)
                }
        )
    }
}