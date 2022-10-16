package org.pathcheck.intellij.cql.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.pathcheck.intellij.cql.psi.scopes.Alias
import org.pathcheck.intellij.cql.psi.statements.OperandDefinition
import org.pathcheck.intellij.cql.utils.CaretUtils


class CqlEmCareRefTest : BasePlatformTestCase() {
    fun testReferenceToFunctionParamsFromWithinCase() {
        assertReferenceIs( 33, 13, OperandDefinition::class.java)
    }

    fun testReferenceToAliasWithinAlias() {
        assertReferenceIs(91, 42, Alias::class.java)
    }

    fun testReferenceToFunctionParamsFromWithinAlias() {
        assertReferenceIs(91, 57, OperandDefinition::class.java)
    }

    fun testReferenceToFunctionParams() {
        assertReferenceIs( 90, 66, OperandDefinition::class.java)
    }

    fun testReferenceToFunctionParamsFromInsideTuple() {
        assertReferenceIs( 130, 15, OperandDefinition::class.java)
    }

    fun <T:PsiElement> assertReferenceIs(line: Int, column: Int, psiNode: Class<T>) {
        myFixture.configureByFiles("EmCareBase-0.99.99.cql")
        moveCaret(line, column)
        assertEquals(psiNode, resolveRefAtCaret()?.parent?.javaClass)
    }

    private fun assertResolvesToNothing() {
        val psiElement = resolveRefAtCaret()
        if (psiElement != null) {
            fail("Expected element at offset ${myFixture.caretOffset} to resolve to nothing, but resolved to $psiElement")
        }
    }

    override fun getTestDataPath(): String {
        return "src/test/testData/EmCare"
    }

    private fun moveCaret(line: Int, column: Int) {
        myFixture.editor.caretModel.moveToOffset(CaretUtils.calculateOffset(myFixture.file.text, line, column))
    }

    private fun resolveRefAtCaret(): PsiElement? {
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