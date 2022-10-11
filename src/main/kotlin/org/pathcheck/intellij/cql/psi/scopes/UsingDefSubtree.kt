package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.antlr.intellij.adaptor.xpath.XPath
import org.cqframework.cql.cql2elm.ModelManager
import org.cqframework.cql.cql2elm.model.Model
import org.hl7.cql.model.DataType
import org.hl7.cql.model.ModelIdentifier
import org.hl7.elm.r1.FunctionDef
import org.hl7.elm.r1.VersionedIdentifier
import org.pathcheck.intellij.cql.CqlLanguage
import org.pathcheck.intellij.cql.GlobalCache
import org.pathcheck.intellij.cql.PsiDirectoryLibrarySourceProvider
import org.pathcheck.intellij.cql.psi.LookupProvider
import org.pathcheck.intellij.cql.psi.ReferenceLookupProvider
import org.pathcheck.intellij.cql.utils.LookupHelper
import org.pathcheck.intellij.cql.utils.cleanText
import org.pathcheck.intellij.cql.utils.getPrivateProperty
import java.util.HashMap

/** A subtree associated with a query.
 * Its scope is the set of arguments.
 */
class UsingDefSubtree(node: ASTNode, idElementType: IElementType) : IdentifierDefSubtree(node, idElementType), ScopeNode, LookupProvider,
    ReferenceLookupProvider {

    fun getModel(): Model? {
        return try {
            GlobalCache.modelManager.resolveModel(getModelIdentifier())
        } catch (e: Exception) {
            null
        }
    }

    fun getModelTypes(): Map<String, DataType> {
        val model = getModel()?: return emptyMap()

        val mapNames = model.getPrivateProperty("nameIndex") as Map<String, DataType>
        val mapClasses = model.getPrivateProperty("classIndex") as Map<String, DataType>
        return (mapNames + mapClasses)
    }

    override fun resolve(element: PsiNamedElement): PsiElement? {
        return context?.resolve(element)
    }

    private fun getModelName(): PsiElement? {
        return XPath.findAll(CqlLanguage, this, "/usingDefinition/qualifiedIdentifier/identifier").firstOrNull()
    }

    private fun getModelLocalName(): PsiElement? {
        return XPath.findAll(CqlLanguage, this, "/usingDefinition/localIdentifier/identifier").firstOrNull()
    }


    private fun getModelVersion(): PsiElement? {
        return XPath.findAll(CqlLanguage, this, "/usingDefinition/versionSpecifier").firstOrNull()
    }

    private fun getModelIdentifier(): ModelIdentifier {
        return ModelIdentifier()
            .withId(getModelName()?.cleanText())
            .withVersion(getModelVersion()?.cleanText())
    }

    override fun expandLookup(): List<LookupElementBuilder> {
        return getModelTypes().map {
            LookupElementBuilder.create(it.key)
                .withTypeText(it.value.toLabel(), true)
                .withIcon(AllIcons.Nodes.Type)
        }
    }

    override fun lookup(): List<LookupElementBuilder> {
        return listOfNotNull(
            LookupHelper.build(
                getModelName()?.cleanText(),
                AllIcons.Nodes.Package,
                getModelVersion()?.cleanText(),
                null),
            LookupHelper.build(
                getModelLocalName()?.cleanText(),
                AllIcons.Nodes.Package,
                getModelVersion()?.cleanText(),
                null)
        )
    }
}