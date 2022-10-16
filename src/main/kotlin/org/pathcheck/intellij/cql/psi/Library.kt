package org.pathcheck.intellij.cql.psi

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.cqframework.cql.cql2elm.model.Model
import org.pathcheck.intellij.cql.elm.GlobalCache
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.definitions.Definition
import org.pathcheck.intellij.cql.psi.definitions.LibraryDefinition
import org.pathcheck.intellij.cql.psi.statements.Statement

class Library(node: ASTNode) : BasePsiNode(node), ScopeNode, DeclaringIdentifiers, LookupProvider {
    fun libraryDefinition(): LibraryDefinition? {
        return getRule(LibraryDefinition::class.java, 0)
    }

    fun definition(): List<Definition>? {
        return getRules(Definition::class.java)
    }

    fun definition(i: Int): Definition? {
        return getRule(Definition::class.java, i)
    }

    fun statement(): List<Statement>? {
        return getRules(Statement::class.java)
    }

    fun statement(i: Int): Statement? {
        return getRule(Statement::class.java, i)
    }

    override fun getContext(): ScopeNode? {
        // Maximum Scope
        return null
    }

    /**
     * Looks for identifiers that match the declaration of the element.
     */
    override fun resolve(element: PsiNamedElement): PsiElement? {
        visibleIdentifiers().firstOrNull() {
            it.text == element.text
        }?.let {
            return it.parent
        }

        return null
    }

    override fun visibleIdentifiers(): List<PsiElement> {
        return listOfNotNull(
           definition()?.map {
                listOfNotNull(
                    it.usingDefinition()?.localIdentifier()?.identifier()?.any(),
                    it.usingDefinition()?.qualifiedIdentifier()?.identifier()?.any(),
                    it.includeDefinition()?.localIdentifier()?.identifier()?.any(),
                    it.includeDefinition()?.qualifiedIdentifier()?.identifier()?.any(),
                    it.codeDefinition()?.identifier()?.any(),
                    it.codesystemDefinition()?.identifier()?.any(),
                    it.conceptDefinition()?.identifier()?.any(),
                    it.valuesetDefinition()?.identifier()?.any(),
                    it.parameterDefinition()?.identifier()?.any()
                )
            }?.flatten(),
            statement()?.map {
                listOfNotNull(
                    it.functionDefinition()?.identifierOrFunctionIdentifier()?.identifier()?.any(),
                    it.functionDefinition()?.identifierOrFunctionIdentifier()?.functionIdentifier(),
                    it.expressionDefinition()?.identifier()?.any(),
                    it.contextDefinition()?.identifier()?.any()
                )
            }?.flatten()
        ).flatten()
    }

    override fun lookup(): List<LookupElementBuilder> {
        return listOfNotNull(
            definition()?.map {
                listOfNotNull(
                    it.usingDefinition()?.lookup(),
                    it.includeDefinition()?.lookup(),
                    it.codeDefinition()?.lookup(),
                    it.codesystemDefinition()?.lookup(),
                    it.conceptDefinition()?.lookup(),
                    it.valuesetDefinition()?.lookup(),
                    it.parameterDefinition()?.lookup()
                ).flatten()
            }?.flatten(),
            statement()?.map {
                listOfNotNull(
                    it.functionDefinition()?.lookup(),
                    it.expressionDefinition()?.lookup()
                ).flatten()
            }?.flatten()
        ).flatten()
    }

    fun exportingLookups(): List<LookupElementBuilder> {
        return statement()?.map {
            listOfNotNull(
                it.functionDefinition()?.lookup(),
                it.expressionDefinition()?.lookup()
            ).flatten()
        }?.flatten() ?: emptyList()
    }

    fun findModels(): List<Model> {
        return try {
            val system = listOfNotNull(GlobalCache.resolveModel("System"))

            val explicitInclusions = definition()?.mapNotNull {
                it.usingDefinition()?.getModel()
            } ?: emptyList()

            explicitInclusions + system
        } catch (e: Exception) {
            println("ERROR: System model is not ready yet")
            emptyList()
        }
    }

    fun findModel(modelName: String): Model? {
        return findModels().firstOrNull {
            it.modelInfo.name == modelName
        }
    }
}