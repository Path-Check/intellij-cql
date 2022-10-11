package org.pathcheck.intellij.cql.psi.scopes

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.psi.IdentifierDefSubtree
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.antlr.intellij.adaptor.xpath.XPath
import org.pathcheck.intellij.cql.CqlLanguage

/** A subtree associated with a query.
 * Its scope is the set of arguments.
 */
class LibraryDefSubtree(node: ASTNode, idElementType: IElementType) : IdentifierDefSubtree(node, idElementType), ScopeNode {
    override fun resolve(element: PsiNamedElement): PsiElement? {
        listOf(
            "/libraryDefinition/qualifiedIdentifier/identifier/IDENTIFIER",
            "/libraryDefinition/qualifiedIdentifier/identifier/DELIMITEDIDENTIFIER",
            "/libraryDefinition/qualifiedIdentifier/identifier/QUOTEDIDENTIFIER",
        ).mapNotNull {
            XPath.findAll(CqlLanguage, this, it)
        }.flatten().firstOrNull() {
            it.text == element.text
        }?.let {
            return it.parent
        }

        return context?.resolve(element)
    }

    fun getLibraryNameElement(): PsiElement? {
        return listOf(
            "/libraryDefinition/qualifiedIdentifier/identifier/IDENTIFIER",
            "/libraryDefinition/qualifiedIdentifier/identifier/DELIMITEDIDENTIFIER",
            "/libraryDefinition/qualifiedIdentifier/identifier/QUOTEDIDENTIFIER",
        ).mapNotNull {
            XPath.findAll(CqlLanguage, this, it)
        }.flatten().firstOrNull()
    }
}