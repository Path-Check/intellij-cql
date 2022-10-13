package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.antlr.intellij.adaptor.xpath.XPath
import org.pathcheck.intellij.cql.CqlLanguage
import org.pathcheck.intellij.cql.psi.antlr.PsiContextNodes

class InvocationExpressionTerm(node: ASTNode) : PsiContextNodes.ExpressionTerm(node), ScopeNode {
    fun expressionTerm(): PsiContextNodes.ExpressionTerm? {
        return getRule(PsiContextNodes.ExpressionTerm::class.java, 0)
    }

    override fun resolve(element: PsiNamedElement?): PsiElement? {
        // sends to parent scope.
        return context?.resolve(element)
    }

    fun qualifiedInvocation(): PsiContextNodes.QualifiedInvocation? {
        return getRule(PsiContextNodes.QualifiedInvocation::class.java, 0)
    }

    fun getQualifier(): PsiElement? {
        val qualifier = expressionTerm()
        // simpe case
        if (qualifier is PsiContextNodes.TermExpressionTerm) {
            val qualifierTerm = qualifier.term()
            if (qualifierTerm is PsiContextNodes.InvocationTerm) {
                val invocation = qualifierTerm.invocation()
                if (invocation is PsiContextNodes.MemberInvocation) {
                    return invocation.referentialIdentifier()?.identifier()?.any()
                }
            }
        }
        return null;
    }

    fun getQualifierDefScope(): ScopeNode? {
        return getQualifier()?.reference?.resolve()?.context as? ScopeNode
    }
}