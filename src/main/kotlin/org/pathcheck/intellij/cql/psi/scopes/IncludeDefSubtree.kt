package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.SymtabUtils
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.antlr.intellij.adaptor.xpath.XPath
import org.hl7.elm.r1.VersionedIdentifier
import org.pathcheck.intellij.cql.CqlLanguage

/** A subtree associated with a query.
 * Its scope is the set of arguments.
 */
class IncludeDefSubtree(node: ASTNode, idElementType: IElementType) : IdentifierDefSubtree(node, idElementType), ScopeNode {
    override fun resolve(element: PsiNamedElement): PsiElement? {
        return listOf(
            "/includeDefinition/localIdentifier/identifier/IDENTIFIER",
            "/includeDefinition/localIdentifier/identifier/DELIMITEDIDENTIFIER",
            "/includeDefinition/localIdentifier/identifier/QUOTEDIDENTIFIER",
            "/includeDefinition/qualifiedIdentifier/identifier/IDENTIFIER",
            "/includeDefinition/qualifiedIdentifier/identifier/DELIMITEDIDENTIFIER",
            "/includeDefinition/qualifiedIdentifier/identifier/QUOTEDIDENTIFIER",
        ).firstNotNullOfOrNull {
            return SymtabUtils.resolve(this, CqlLanguage, element, it)
        }
    }

    fun getLibraryVersion(): VersionedIdentifier {
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