package org.pathcheck.intellij.cql.completion

import com.intellij.codeInsight.completion.CompletionType
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.pathcheck.intellij.cql.utils.CaretUtils

abstract class BaseCompletionTestCase : BasePlatformTestCase() {

    abstract fun loadFiles(): List<String>
    abstract fun testDirectory(): String

    override fun getTestDataPath(): String {
        return testDirectory()
    }

    fun assertCompletionHas(line: Int, column: Int, vararg expectedLookupElements: String) {
        myFixture.configureByFiles(*loadFiles().toTypedArray());
        moveCaret(line, column)

        myFixture.complete(CompletionType.BASIC)
        val lookupElementStrings = myFixture.lookupElementStrings!!.toTypedArray()

        assertNotNull(lookupElementStrings)
        assertSameElements(lookupElementStrings, *expectedLookupElements)
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
                fail("No reference at caret")
            }
        } else {
            fail("No element at caret")
        }
        return null
    }

}