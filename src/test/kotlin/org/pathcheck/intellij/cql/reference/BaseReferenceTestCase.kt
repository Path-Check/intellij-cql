package org.pathcheck.intellij.cql.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.pathcheck.intellij.cql.utils.CaretUtils

abstract class BaseReferenceTestCase : BasePlatformTestCase() {

    abstract fun loadFiles(): List<String>
    abstract fun testDirectory(): String

    override fun getTestDataPath(): String {
        return testDirectory()
    }

    fun <T: PsiElement> assertReferenceIs(line: Int, column: Int, psiNode: Class<T>) {
        myFixture.configureByFiles(*loadFiles().toTypedArray())
        moveCaret(line, column)
        BasePlatformTestCase.assertEquals(psiNode, resolveRefAtCaret()?.parent?.javaClass)
    }

    fun assertResolvesToNothing() {
        val psiElement = resolveRefAtCaret()
        if (psiElement != null) {
            BasePlatformTestCase.fail("Expected element at offset ${myFixture.caretOffset} to resolve to nothing, but resolved to $psiElement")
        }
    }

    fun moveCaret(line: Int, column: Int) {
        myFixture.editor.caretModel.moveToOffset(CaretUtils.calculateOffset(myFixture.file.text, line, column))
    }

    fun resolveRefAtCaret(): PsiElement? {
        val elementAtCaret = myFixture.file.findElementAt(myFixture.caretOffset)
        if (elementAtCaret != null) {
            val ref: PsiReference? = elementAtCaret.reference
            if (ref != null) {
                return ref.resolve()
            } else {
                BasePlatformTestCase.fail("No reference at caret")
            }
        } else {
            BasePlatformTestCase.fail("No element at caret")
        }
        return null
    }

}