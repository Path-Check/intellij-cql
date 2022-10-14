package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.hl7.cql.model.ClassType
import org.hl7.cql.model.DataType
import org.hl7.cql.model.ListType
import org.pathcheck.intellij.cql.psi.*
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.definitions.IncludeDefinition
import org.pathcheck.intellij.cql.psi.expressions.InvocationExpressionTerm
import org.pathcheck.intellij.cql.psi.references.*
import org.pathcheck.intellij.cql.utils.expandLookup
import org.pathcheck.intellij.cql.utils.exportingLookups
import org.pathcheck.intellij.cql.utils.printParentStack

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
            val qualifier = typedParent.getQualifier()
            println("Finding ${qualifier} ${text}")
            return findMemberType(qualifier, text)
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
            val qualifier = typedParent.getQualifier()
            if (qualifier is ClassType) {
                return qualifier.expandLookup()
            }
            if (qualifier is ListType) {
                // List demotion
                if (qualifier.elementType is ClassType) {
                    return (qualifier.elementType as ClassType).expandLookup()
                }
            }
            if (qualifier is ModelType) {
                return qualifier.model.expandLookup()
            }
            if (qualifier is LibraryType) {
                return qualifier.library.exportingLookups()
            }
            if (qualifier is CompiledLibraryType) {
                return qualifier.library.exportingLookups()
            }

            println("ERROR: Couldn't expand qualifier $qualifier with class ${qualifier?.javaClass}")
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
        println("QualifiedMemberInvocation for ${referentialIdentifier()?.text}")

        val typedParent = parent
        if (typedParent is HasQualifier) {
            val qualifier = typedParent.getQualifier()
            val member = referentialIdentifier()?.text
            println("Finding ${qualifier} ${member} ${findMemberType(qualifier, member)}")
            return findMemberType(qualifier, member)
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
            val qualifier = typedParent.getQualifier()
            if (qualifier is ClassType) {
                return qualifier.expandLookup()
            }
            if (qualifier is ListType) {
                // List demotion
                if (qualifier.elementType is ClassType) {
                    return (qualifier.elementType as ClassType).expandLookup()
                }
            }
            if (qualifier is ModelType) {
                return qualifier.model.expandLookup()
            }
            if (qualifier is LibraryType) {
                return qualifier.library.exportingLookups()
            }
            if (qualifier is CompiledLibraryType) {
                return qualifier.library.exportingLookups()
            }

            println("ERROR: Couldn't expand qualifier $qualifier with class ${qualifier?.javaClass}")
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