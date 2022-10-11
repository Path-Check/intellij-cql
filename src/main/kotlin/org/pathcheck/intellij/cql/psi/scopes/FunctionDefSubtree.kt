package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.codeInsight.completion.util.ParenthesesInsertHandler
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.antlr.intellij.adaptor.xpath.XPath
import org.pathcheck.intellij.cql.CqlLanguage
import org.pathcheck.intellij.cql.psi.DeclaringIdentifiers
import org.pathcheck.intellij.cql.psi.LookupProvider
import org.pathcheck.intellij.cql.utils.LookupHelper
import org.pathcheck.intellij.cql.utils.cleanText

/** A subtree associated with a function definition.
 * Its scope is the set of arguments.
 */
class FunctionDefSubtree(node: ASTNode, idElementType: IElementType) : IdentifierDefSubtree(node, idElementType),
    ScopeNode, DeclaringIdentifiers, LookupProvider {

    override fun resolve(element: PsiNamedElement): PsiElement? {
        visibleIdentifiers().firstOrNull() {
            it.text == element.text
        }?.let {
            return it.parent
        }

        return context?.resolve(element)
    }

    override fun visibleIdentifiers(): List<PsiElement> {
       return listOf(
            "/functionDefinition/operandDefinition/referentialIdentifier/identifier/IDENTIFIER",
            "/functionDefinition/operandDefinition/referentialIdentifier/identifier/DELIMITEDIDENTIFIER",
            "/functionDefinition/operandDefinition/referentialIdentifier/identifier/QUOTEDIDENTIFIER",
            "/functionDefinition/operandDefinition/referentialIdentifier/identifier/keywordIdentifier"
        ).mapNotNull {
            XPath.findAll(CqlLanguage, this, it)
        }.flatten()
    }

    fun getFunctionName(): PsiElement? {
        return XPath.findAll(CqlLanguage, this, "/functionDefinition/identifierOrFunctionIdentifier").firstOrNull()
    }

    fun getFunctionParameters(): List<PsiElement>? {
        return XPath.findAll(CqlLanguage, this, "/functionDefinition/operandDefinition").toList()
    }

    fun getParametersStr(): String {
        return "(" + getFunctionParameters()?.joinToString(", ") { it.cleanText() } + ")"
    }

    override fun lookup(): List<LookupElementBuilder> {
        val functionName = getFunctionName() ?: return emptyList();

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