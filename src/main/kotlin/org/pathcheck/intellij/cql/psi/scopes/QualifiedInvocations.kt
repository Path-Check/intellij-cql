package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.hl7.cql.model.DataType
import org.pathcheck.intellij.cql.psi.HasQualifier
import org.pathcheck.intellij.cql.psi.HasResultType
import org.pathcheck.intellij.cql.psi.LibraryType
import org.pathcheck.intellij.cql.psi.ReferenceLookupProvider
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.references.IdentifierOrFunctionIdentifier
import org.pathcheck.intellij.cql.psi.references.ParamList
import org.pathcheck.intellij.cql.psi.references.ReferentialIdentifier

/** A subtree associated with a function definition.
 * Its scope is the set of arguments.
 */

open class QualifiedInvocation(node: ASTNode) : BasePsiNode(node), HasResultType {
    /**
     * This class is only instantiated when the parser has errors
     */
    override fun getResultType(): DataType? {
        // solve qualifiers here.
        val typedParent = parent
        if (typedParent is HasQualifier) {
            return findMemberType(typedParent.getQualifier(), text)
        }

        // It should never happen.
        return null
    }
}

class QualifiedFunctionInvocation(node: ASTNode) : QualifiedInvocation(node), ScopeNode, ReferenceLookupProvider {
    fun qualifiedFunction(): QualifiedFunction? {
        return getRule(QualifiedFunction::class.java, 0)
    }

    override fun getResultType(): DataType? {
        // solve qualifiers here.
        val typedParent = parent
        if (typedParent is HasQualifier) {
            return qualifiedFunction()?.findYouInType(typedParent.getQualifier())
        }

        // It should never happen.
        return null
    }

    override fun resolve(element: PsiNamedElement): PsiElement? {
        val member = qualifiedFunction()?.identifierOrFunctionIdentifier()?.identifier()?.any()

        // Only resolves qualifiers for the members, not subset of elements
        if (member?.text != element.text || member?.textRange != member?.textRange) {
            // passes it forward.
            return context?.resolve(element)
        }

        val typedParent = parent
        if (typedParent is HasQualifier) {
            val qualifierType = typedParent.getQualifier()
            if (qualifierType is LibraryType) {
                return qualifierType.library.resolve(element)
            }
        }

        return null
    }

    override fun expandLookup(): List<LookupElementBuilder> {
        val typedParent = parent
        if (typedParent is HasQualifier) {
            return expandLookup(typedParent.getQualifier())
        }

        return emptyList()
    }
}

class QualifiedMemberInvocation(node: ASTNode) : QualifiedInvocation(node), ScopeNode, ReferenceLookupProvider, HasResultType {
    fun referentialIdentifier(): ReferentialIdentifier? {
        return getRule(ReferentialIdentifier::class.java, 0)
    }

    override fun getResultType(): DataType? {
        // solve qualifiers here.
        val typedParent = parent
        if (typedParent is HasQualifier) {
            return findMemberType(typedParent.getQualifier(), referentialIdentifier()?.text)
        }

        // It should never happen.
        return null
    }

    override fun resolve(element: PsiNamedElement): PsiElement? {
        val member = referentialIdentifier()?.identifier()?.any()

        // Only resolves qualifiers for the members, not subset of elements
        if (member?.text != element.text || member?.textRange != member?.textRange) {
            // passes it forward.
            return context?.resolve(element)
        }

        val typedParent = parent
        if (typedParent is HasQualifier) {
            val qualifierType = typedParent.getQualifier()
            if (qualifierType is LibraryType) {
                return qualifierType.library.resolve(element)
            }
        }

        return null;
    }

    override fun expandLookup(): List<LookupElementBuilder> {
        val typedParent = parent
        if (typedParent is HasQualifier) {
            return expandLookup(typedParent.getQualifier())
        }

        return emptyList()
    }
}


class QualifiedFunction(node: ASTNode) : BasePsiNode(node) {
    fun identifierOrFunctionIdentifier(): IdentifierOrFunctionIdentifier? {
        return getRule(IdentifierOrFunctionIdentifier::class.java, 0)
    }

    fun paramList(): ParamList? {
        return getRule(ParamList::class.java, 0)
    }

    fun findYouInType(qualifier: DataType?): DataType? {
        val member = identifierOrFunctionIdentifier()?.text
        return findMemberType(qualifier, member)
    }
}

class Function(node: ASTNode) : BasePsiNode(node), HasResultType {
    fun referentialIdentifier(): ReferentialIdentifier? {
        return getRule(ReferentialIdentifier::class.java, 0)
    }

    fun paramList(): ParamList? {
        return getRule(ParamList::class.java, 0)
    }

    override fun getResultType() = referentialIdentifier()?.getResultType()
}