package org.pathcheck.intellij.cql.psi

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.jetbrains.rd.util.firstOrNull
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.cqframework.cql.cql2elm.model.Model
import org.hl7.cql.model.ClassType
import org.pathcheck.intellij.cql.elm.GlobalCache
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.definitions.Definition
import org.pathcheck.intellij.cql.psi.definitions.LibraryDefinition
import org.pathcheck.intellij.cql.psi.statements.Statement
import org.pathcheck.intellij.cql.utils.DataTypePsiElement
import org.pathcheck.intellij.cql.utils.takeIndex

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
            thisLogger().debug("Library Resolve ${element.text} to ${it.parent.parent.parent}/${it.parent.parent}")
            return it.parent
        }

        // Seachers inside models
        val matchUsing = definition()?.mapNotNull {
            it.usingDefinition()
        }?.forEach {
            val model = it.getModel()
            val type = model?.takeIndex()?.values?.filter { type ->
                if (type is ClassType) {
                    type.simpleName == element.text
                } else {
                    type.toLabel() == element.text
                }
            }?.firstOrNull()

            if (type != null && it.qualifiedIdentifier() != null) {
                thisLogger().debug("Library Resolve ${element.text} to ${model.modelInfo.name}/${type}")
                return DataTypePsiElement(type, it.qualifiedIdentifier()!!)
            }
        }

        thisLogger().debug("Library Resolve ${element.text} -> Not part of the Identifying set")

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
                    it.expressionDefinition()?.identifier()?.any()
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
            thisLogger().warn("ERROR: System model is not ready yet")
            emptyList()
        }
    }

    fun findModel(modelName: String): Model? {
        return findModels().firstOrNull {
            it.modelInfo.name == modelName
        }
    }
}