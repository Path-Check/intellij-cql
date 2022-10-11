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
import org.hl7.cql.model.NamespaceManager
import org.hl7.elm.r1.FunctionDef
import org.hl7.elm.r1.Library
import org.hl7.elm.r1.VersionedIdentifier
import org.pathcheck.intellij.cql.CqlLanguage
import org.pathcheck.intellij.cql.GlobalCache
import org.pathcheck.intellij.cql.PsiDirectoryLibrarySourceProvider
import org.pathcheck.intellij.cql.psi.LookupProvider
import org.pathcheck.intellij.cql.psi.ReferenceLookupProvider
import org.pathcheck.intellij.cql.utils.LookupHelper
import org.pathcheck.intellij.cql.utils.cleanText
import org.pathcheck.intellij.cql.utils.exportingLookups

/** A subtree associated with a query.
 * Its scope is the set of arguments.
 */
class IncludeDefSubtree(node: ASTNode, idElementType: IElementType) : IdentifierDefSubtree(node, idElementType), ScopeNode, LookupProvider,
    ReferenceLookupProvider {

    override fun resolve(element: PsiNamedElement): PsiElement? {
        // Clicks on the includeDefinition always point to the libraryDefinition in the linked filed.
        return PsiTreeUtil.findChildOfType(getLinkedLocalLibrary(), LibraryDefSubtree::class.java)?.getLibraryNameElement()
    }

    fun resolveInLinkedLibrary(element: PsiNamedElement): PsiElement? {
        return getLinkedLocalLibrary()?.resolve(element)
    }

    private fun getLinkedExternalLibrary(): Library? {
        val libraryVersion = getVersionIdentifier()
        val libraryPath = NamespaceManager.getPath(libraryVersion.system, libraryVersion.id)

        return GlobalCache.libraryManager.compiledLibraries[libraryPath]?.library
    }

    private fun getLinkedLocalLibrary(): FileRootSubtree? {
        return PsiDirectoryLibrarySourceProvider(containingFile.originalFile.containingDirectory)
            .getLibrarySourceFile(getVersionIdentifier()) as? FileRootSubtree
    }

    private fun getLibraryName(): PsiElement? {
        return XPath.findAll(CqlLanguage, this, "/includeDefinition/qualifiedIdentifier/identifier").firstOrNull()
    }

    private fun getLibraryLocalName(): PsiElement? {
        return XPath.findAll(CqlLanguage, this, "/includeDefinition/localIdentifier/identifier").firstOrNull()
    }

    private fun getLibraryVersion(): PsiElement? {
        return XPath.findAll(CqlLanguage, this, "/includeDefinition/versionSpecifier").firstOrNull()
    }

    private fun getVersionIdentifier(): VersionedIdentifier {
        return VersionedIdentifier()
            .withId(getLibraryName()?.cleanText())
            .withVersion(getLibraryVersion()?.cleanText())
    }

    override fun expandLookup(): List<LookupElementBuilder> {
        getLinkedLocalLibrary()?.exportingLookups()?.let { return it }
        getLinkedExternalLibrary()?.exportingLookups()?.let { return it }

        return emptyList()
    }

    override fun lookup(): List<LookupElementBuilder> {
        return listOfNotNull(
            LookupHelper.build(
                getLibraryName()?.cleanText(),
                AllIcons.Nodes.Package,
                getLibraryVersion()?.cleanText(),
                null),
            LookupHelper.build(
                getLibraryLocalName()?.cleanText(),
                AllIcons.Nodes.Package,
                getLibraryVersion()?.cleanText(),
                null)
        )
    }
}