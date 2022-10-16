package org.pathcheck.intellij.cql.psi.definitions

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.hl7.cql.model.NamespaceManager
import org.hl7.elm.r1.Library
import org.hl7.elm.r1.VersionedIdentifier
import org.pathcheck.intellij.cql.elm.GlobalCache
import org.pathcheck.intellij.cql.elm.PsiDirectoryLibrarySourceProvider
import org.pathcheck.intellij.cql.psi.*
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.utils.LookupHelper
import org.pathcheck.intellij.cql.utils.cleanText
import org.pathcheck.intellij.cql.utils.exportingLookups

/** A subtree associated with a query.
 * Its scope is the set of arguments.
 */
class IncludeDefinition(node: ASTNode) : BasePsiNode(node), ScopeNode, LookupProvider, ReferenceLookupProvider, HasResultType {

    fun qualifiedIdentifier(): QualifiedIdentifier? {
        return getRule(QualifiedIdentifier::class.java, 0)
    }

    fun versionSpecifier(): VersionSpecifier? {
        return getRule(VersionSpecifier::class.java, 0)
    }

    fun localIdentifier(): LocalIdentifier? {
        return getRule(LocalIdentifier::class.java, 0)
    }

    override fun getResultType() =
        getLinkedLocalLibrary()?.let { LibraryType(it) } ?: getLinkedExternalLibrary()?.let { CompiledLibraryType(it) }

    override fun resolve(element: PsiNamedElement): PsiElement? {
        // Clicks on the includeDefinition always point to the libraryDefinition in the linked filed.
        return PsiTreeUtil.findChildOfType(getLinkedLocalLibrary(), LibraryDefinition::class.java)?.qualifiedIdentifier()
    }

    fun resolveInLinkedLibrary(element: PsiNamedElement): PsiElement? {
        return getLinkedLocalLibrary()?.resolve(element)
    }

    private fun getLinkedExternalLibrary(): Library? {
        return GlobalCache.resolveLibrary(getVersionIdentifier())
    }

    private fun getLinkedLocalLibrary(): org.pathcheck.intellij.cql.psi.Library? {
        val file = PsiDirectoryLibrarySourceProvider(containingFile.originalFile.containingDirectory)
            .getLibrarySourceFile(getVersionIdentifier())

        if (file is CqlFileRoot) {
            return file.library()
        }

        return null
    }

    private fun getVersionIdentifier(): VersionedIdentifier {
        return VersionedIdentifier()
            .withId(qualifiedIdentifier()?.cleanText())
            .withVersion(versionSpecifier()?.cleanText())
    }

    override fun expandLookup(): List<LookupElementBuilder> {
        getLinkedLocalLibrary()?.exportingLookups()?.let { return it }
        getLinkedExternalLibrary()?.exportingLookups()?.let { return it }

        return emptyList()
    }

    override fun lookup(): List<LookupElementBuilder> {
        return listOfNotNull(
            LookupHelper.build(
                qualifiedIdentifier()?.identifier()?.any()?.cleanText(),
                AllIcons.Nodes.Package,
                versionSpecifier()?.cleanText()?.let { ":$it" } ,
                null),
            LookupHelper.build(
                localIdentifier()?.identifier()?.any()?.cleanText(),
                AllIcons.Nodes.Package,
                versionSpecifier()?.cleanText()?.let { ":$it" } ,
                null)
        )
    }
}