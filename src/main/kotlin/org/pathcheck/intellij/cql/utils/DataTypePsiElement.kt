package org.pathcheck.intellij.cql.utils

import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.impl.PsiElementBase
import org.hl7.cql.model.DataType
import org.pathcheck.intellij.cql.psi.HasResultType

/**
 * Allows a Model to return inner DataTypes as PsiElements
 */
class DataTypePsiElement(val type: DataType, private val originalParent: PsiElement) : PsiElementBase(), HasResultType {
    override fun getLanguage(): Language {
        return originalParent.language
    }

    override fun getChildren(): Array<PsiElement> {
        return originalParent.children
    }

    override fun getParent(): PsiElement {
        return originalParent
    }

    override fun getTextRange(): TextRange {
        return originalParent.textRange
    }

    override fun getStartOffsetInParent(): Int {
        return originalParent.startOffsetInParent
    }

    override fun getTextLength(): Int {
        return originalParent.textLength
    }

    override fun findElementAt(offset: Int): PsiElement? {
        return originalParent.findElementAt(offset)
    }

    override fun getTextOffset(): Int {
        return originalParent.textOffset
    }

    override fun getText(): String {
        return originalParent.text
    }

    override fun textToCharArray(): CharArray {
        return originalParent.textToCharArray()
    }

    override fun getNode(): ASTNode {
        return originalParent.node
    }

    override fun getResultType(): DataType? {
        return type
    }

    override fun getManager(): PsiManager {
        return originalParent.manager
    }

    override fun getProject(): Project {
        return originalParent.project
    }

}