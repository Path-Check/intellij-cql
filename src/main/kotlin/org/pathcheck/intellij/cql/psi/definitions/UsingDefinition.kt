package org.pathcheck.intellij.cql.psi.definitions

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.cqframework.cql.cql2elm.model.Model
import org.hl7.cql.model.ClassType
import org.hl7.cql.model.DataType
import org.hl7.cql.model.ModelIdentifier
import org.pathcheck.intellij.cql.elm.GlobalCache
import org.pathcheck.intellij.cql.psi.HasResultType
import org.pathcheck.intellij.cql.psi.LookupProvider
import org.pathcheck.intellij.cql.psi.ModelType
import org.pathcheck.intellij.cql.psi.ReferenceLookupProvider
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.utils.LookupHelper
import org.pathcheck.intellij.cql.utils.cleanText
import org.pathcheck.intellij.cql.utils.takeIndex

/** A subtree associated with a query.
 * Its scope is the set of arguments.
 */
class UsingDefinition(node: ASTNode) : BasePsiNode(node), ScopeNode, LookupProvider, ReferenceLookupProvider, HasResultType {

    fun qualifiedIdentifier(): QualifiedIdentifier? {
        return getRule(QualifiedIdentifier::class.java, 0)
    }

    fun versionSpecifier(): VersionSpecifier? {
        return getRule(VersionSpecifier::class.java, 0)
    }

    fun localIdentifier(): LocalIdentifier? {
        return getRule(LocalIdentifier::class.java, 0)
    }

    override fun getResultType() = getModel()?.let { ModelType(it) }

    fun getModel(): Model? {
        return try {
            GlobalCache.resolveModel(getModelIdentifier())
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getModelTypes(): Map<String, DataType> {
        return getModel()?.takeIndex() ?: return emptyMap()
    }

    override fun resolve(element: PsiNamedElement): PsiElement? {
        return context?.resolve(element)
    }

    private fun getModelIdentifier(): ModelIdentifier {
        return ModelIdentifier()
            .withId(qualifiedIdentifier()?.cleanText())
            .withVersion(versionSpecifier()?.cleanText())
    }

    override fun expandLookup(): List<LookupElementBuilder> {
        return getModelTypes().map {
            val type = it.value
            if (type is ClassType) {
                LookupElementBuilder.create(type.simpleName)
                    .withTypeText(type.name, true)
                    .withIcon(AllIcons.Nodes.Type)
            } else {
                LookupElementBuilder.create(type)
                    .withTypeText(type.toString(), true)
                    .withIcon(AllIcons.Nodes.Type)
            }
        }
    }

    override fun lookup(): List<LookupElementBuilder> {
        return listOfNotNull(
            LookupHelper.build(
                qualifiedIdentifier()?.cleanText(),
                AllIcons.Nodes.Package,
                versionSpecifier()?.cleanText()?.let { ":$it" } ,
                null),
            LookupHelper.build(
                localIdentifier()?.cleanText(),
                AllIcons.Nodes.Package,
                versionSpecifier()?.cleanText()?.let { ":$it" } ,
                null)
        )
    }
}