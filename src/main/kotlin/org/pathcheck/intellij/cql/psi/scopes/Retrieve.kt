package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.ASTNode
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.util.findParentOfType
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.hl7.cql.model.DataType
import org.hl7.cql.model.ListType
import org.pathcheck.intellij.cql.psi.HasResultType
import org.pathcheck.intellij.cql.psi.ReferenceLookupProvider
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.expressions.Expression
import org.pathcheck.intellij.cql.psi.expressions.NamedTypeSpecifier
import org.pathcheck.intellij.cql.psi.expressions.SimpleLiteral
import org.pathcheck.intellij.cql.psi.references.ReferentialIdentifier

class Retrieve(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun namedTypeSpecifier(): NamedTypeSpecifier? {
        return getRule(NamedTypeSpecifier::class.java, 0)
    }

    fun contextIdentifier(): ContextIdentifier? {
        return getRule(ContextIdentifier::class.java, 0)
    }

    fun terminology(): Terminology? {
        return getRule(Terminology::class.java, 0)
    }

    fun codePath(): CodePath? {
        return getRule(CodePath::class.java, 0)
    }

    fun codeComparator(): CodeComparator? {
        return getRule(CodeComparator::class.java, 0)
    }

    override fun getResultType(): DataType? {
        val nameSpec = namedTypeSpecifier()
        val innerType = nameSpec?.resolveType()
        if (innerType != null)
            return ListType(innerType)

        thisLogger().error("InnerType was null on Retrieve.getResultType() with nameSpec $nameSpec as ${nameSpec?.text}")

        return null
    }
}

class ContextIdentifier(node: ASTNode) : BasePsiNode(node) {
    fun qualifiedIdentifierExpression(): QualifiedIdentifierExpression? {
        return getRule(QualifiedIdentifierExpression::class.java, 0)
    }
}

class QualifiedIdentifierExpression(node: ASTNode) : BasePsiNode(node), HasResultType, ScopeNode, ReferenceLookupProvider {
    fun referentialIdentifier(): ReferentialIdentifier? {
        return getRule(ReferentialIdentifier::class.java, 0)
    }

    fun qualifierExpression(): List<QualifierExpression>? {
        return getRules(QualifierExpression::class.java)
    }

    fun qualifierExpression(i: Int): QualifierExpression? {
        return getRule(QualifierExpression::class.java, i)
    }

    // (qualifierExpression '.')* referentialIdentifier
    override fun getResultType(): DataType? {
        // unrolls the stack of qualifiers via their result type
        val qualifierType = qualifierExpression()?.lastOrNull()?.getResultType()

        return if (qualifierType != null) {
            findMemberType(qualifierType, referentialIdentifier()?.text)
        } else {
            // Unqualified Identifier
            referentialIdentifier()?.getResultType()
        }
    }

    override fun resolve(element: PsiNamedElement?): PsiElement? {
        if (element == null) return null

        return if (qualifierExpression().isNullOrEmpty()) {
            thisLogger().debug("Retrieve Resolve ${element.text}. Unqualified Identifier, sends to parent scope.")
            return context?.resolve(element)
        } else {
            val qualifierExpression = element.findParentOfType<QualifierExpression>()
                ?: return null // should never happen

            // if it is root, sends to parent scope.
            if (getParentQualifier(qualifierExpression) == null) {
                thisLogger().debug("Retrieve Resolve ${element.text}. Root Qualifier, sends to parent scope.")
                return context?.resolve(element)
            }

            // TODO: can't resolve datatypes yet.
            null;
        }
    }

    /** gets the previous qualifier or null if root */
    fun getParentQualifier(element: QualifierExpression): QualifierExpression? {
        val position = children.indexOf(element)
        if (position == 0) return null
        return children[position-1] as QualifierExpression
    }

    override fun expandLookup(): List<LookupElementBuilder> {
        return expandLookup(qualifierExpression()?.lastOrNull()?.getResultType())
    }
}

class QualifierExpression(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun referentialIdentifier(): ReferentialIdentifier? {
        return getRule(ReferentialIdentifier::class.java, 0)
    }

    fun typedParent(): QualifiedIdentifierExpression {
        return parent as QualifiedIdentifierExpression
    }

    override fun getResultType(): DataType? {
        val parentQualifier = typedParent().getParentQualifier(this)
        return if (parentQualifier != null) {
            findMemberType(parentQualifier.getResultType(), text)
        } else {
            // this is the root qualifier, resolves the reference
            return referentialIdentifier()?.getResultType()
        }
    }
}

class CodePath(node: ASTNode) : BasePsiNode(node) {
    fun simplePath(): SimplePath? {
        return getRule(SimplePath::class.java, 0)
    }
}

class CodeComparator(node: ASTNode) : BasePsiNode(node)

class Terminology(node: ASTNode) : BasePsiNode(node) {
    fun qualifiedIdentifierExpression(): QualifiedIdentifierExpression? {
        return getRule(QualifiedIdentifierExpression::class.java, 0)
    }

    fun expression(): Expression? {
        return getRule(Expression::class.java, 0)
    }
}

open class SimplePath(node: ASTNode) : BasePsiNode(node)
class SimplePathIndexer(node: ASTNode) : SimplePath(node) {
    fun simplePath(): SimplePath? {
        return getRule(SimplePath::class.java, 0)
    }

    fun simpleLiteral(): SimpleLiteral? {
        return getRule(SimpleLiteral::class.java, 0)
    }
}

class SimplePathQualifiedIdentifier(node: ASTNode) : SimplePath(node) {
    fun simplePath(): SimplePath? {
        return getRule(SimplePath::class.java, 0)
    }

    fun referentialIdentifier(): ReferentialIdentifier? {
        return getRule(ReferentialIdentifier::class.java, 0)
    }
}

class SimplePathReferentialIdentifier(node: ASTNode) : SimplePath(node) {
    fun referentialIdentifier(): ReferentialIdentifier? {
        return getRule(ReferentialIdentifier::class.java, 0)
    }
}