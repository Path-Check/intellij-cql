package org.pathcheck.intellij.cql

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.FoldingGroup
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import org.antlr.intellij.adaptor.xpath.XPath

class CqlFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        var i=0

        return listOf(
                "/library/statement/functionDefinition/functionBody",
                "/library/statement/expressionDefinition/expression"
            )
            .map { XPath.findAll(CqlLanguage, root, it) }
            .flatten()
            .filter { it.textRange.endOffset > it.textRange.startOffset } // define "": causes this exception 
            .map {
                FoldingDescriptor(
                    it.node,
                    TextRange(
                        it.textRange.startOffset,
                        it.textRange.endOffset
                    ),
                    FoldingGroup.newGroup(i++.toString()),
                    "..."
                )
            }.toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode): String? {
        return null
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return false
    }
}