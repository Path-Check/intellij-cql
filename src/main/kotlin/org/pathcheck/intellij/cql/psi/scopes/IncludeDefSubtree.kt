package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.antlr.intellij.adaptor.xpath.XPath
import org.hl7.elm.r1.VersionedIdentifier
import org.pathcheck.intellij.cql.CqlLanguage
import org.pathcheck.intellij.cql.PsiDirectoryLibrarySourceProvider

/** A subtree associated with a query.
 * Its scope is the set of arguments.
 */
class IncludeDefSubtree(node: ASTNode, idElementType: IElementType) : IdentifierDefSubtree(node, idElementType), ScopeNode {
    override fun resolve(element: PsiNamedElement): PsiElement? {
        // Clicks on the includeDefinition always point to the libraryDefinition in the linked filed.
        return PsiTreeUtil.findChildOfType(getLinkedLibrary(), LibraryDefSubtree::class.java)?.getLibraryNameElement()
    }

    fun resolveInLinkedLibrary(element: PsiNamedElement): PsiElement? {
        return getLinkedLibrary()?.resolve(element)
    }

    private fun getLinkedLibrary(): CqlPSIFileRoot? {
        return PsiDirectoryLibrarySourceProvider(containingFile.containingDirectory)
            .getLibrarySourceFile(getLibraryVersion()) as? CqlPSIFileRoot
    }

    private fun getLibraryVersion(): VersionedIdentifier {
        val libraryId =
            XPath.findAll(CqlLanguage, this, "/includeDefinition/qualifiedIdentifier/identifier")
                .first().text
                .removeSurrounding("\'")
                .removeSurrounding("\"")
                .removeSurrounding("`")

        val libraryVersion =
            XPath.findAll(CqlLanguage, this, "/includeDefinition/versionSpecifier")
                .firstOrNull()?.text
                ?.removeSurrounding("\'")
                ?.removeSurrounding("\"")
                ?.removeSurrounding("`")

        return VersionedIdentifier().withId(libraryId).withVersion(libraryVersion)
    }
}