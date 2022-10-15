package org.pathcheck.intellij.cql.psi.statements

import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.pathcheck.intellij.cql.psi.DeclaringIdentifiers
import org.pathcheck.intellij.cql.psi.HasResultType
import org.pathcheck.intellij.cql.psi.LookupProvider
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.definitions.AccessModifier
import org.pathcheck.intellij.cql.psi.expressions.Expression
import org.pathcheck.intellij.cql.psi.expressions.TypeSpecifier
import org.pathcheck.intellij.cql.psi.references.IdentifierOrFunctionIdentifier
import org.pathcheck.intellij.cql.psi.references.ReferentialIdentifier
import org.pathcheck.intellij.cql.utils.cleanText

/** A subtree associated with a function definition.
 * Its scope is the set of arguments.
 */
class FunctionDefinition(node: ASTNode) : BasePsiNode(node), ScopeNode, DeclaringIdentifiers, LookupProvider, HasResultType {
    fun identifierOrFunctionIdentifier(): IdentifierOrFunctionIdentifier? {
        return getRule(IdentifierOrFunctionIdentifier::class.java, 0)
    }

    fun accessModifier(): AccessModifier? {
        return getRule(AccessModifier::class.java, 0)
    }

    fun fluentModifier(): FluentModifier? {
        return getRule(FluentModifier::class.java, 0)
    }

    fun operandDefinition(): List<OperandDefinition>? {
        return getRules(OperandDefinition::class.java)
    }

    fun operandDefinition(i: Int): OperandDefinition? {
        return getRule(OperandDefinition::class.java, i)
    }

    fun functionBody(): FunctionBody? {
        return getRule(FunctionBody::class.java, 0)
    }

    fun typeSpecifier(): TypeSpecifier? {
        return getRule(TypeSpecifier::class.java, 0)
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
                .withTypeText(getResultType()?.toLabel() ?: "", true)
                .withInsertHandler { context, item ->
                    ParenthesesInsertHandler.getInstance(getParametersStr().length > 2)
                        .handleInsert(context, item)
                }
        )
    }

    override fun getResultType() = typeSpecifier()?.resolveType() ?: functionBody()?.getResultType()
}

class FunctionBody(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }

    override fun getResultType() = expression()?.getResultType()
}

class FluentModifier(node: ASTNode) : BasePsiNode(node)

class OperandDefinition(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun referentialIdentifier(): ReferentialIdentifier? {
        return getRule(ReferentialIdentifier::class.java, 0)
    }

    fun typeSpecifier(): TypeSpecifier? {
        return getRule(TypeSpecifier::class.java, 0)
    }

    override fun getResultType() = typeSpecifier()?.resolveType()
}