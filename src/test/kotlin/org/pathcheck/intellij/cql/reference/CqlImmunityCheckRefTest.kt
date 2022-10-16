package org.pathcheck.intellij.cql.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.pathcheck.intellij.cql.psi.definitions.LibraryDefinition
import org.pathcheck.intellij.cql.psi.definitions.QualifiedIdentifier
import org.pathcheck.intellij.cql.psi.references.IdentifierOrFunctionIdentifier
import org.pathcheck.intellij.cql.psi.scopes.Alias
import org.pathcheck.intellij.cql.psi.statements.ExpressionDefinition
import org.pathcheck.intellij.cql.utils.CaretUtils


class CqlImmunityCheckRefTest : BasePlatformTestCase() {
    fun testReferenceToAliasIss() {
        assertReferenceIs( 17, 12, Alias::class.java)
    }

    fun testReferenceToExpressionDefGetSingleDoseInQuotes() {
        assertReferenceIs(11, 39, ExpressionDefinition::class.java)
    }

    fun testReferenceToExpressionDefGetFinalDose() {
        assertReferenceIs(11, 15, ExpressionDefinition::class.java)
    }

    fun testReferenceToIncludeDefCommons() {
        assertReferenceIs( 17, 54, QualifiedIdentifier::class.java)
    }

    fun testReferenceToCommonsLibrary() {
        assertReferenceIs(5, 13, LibraryDefinition::class.java)
    }

    fun testReferenceToIncludeDefCommonsFunction() {
        assertReferenceIs(17, 63, IdentifierOrFunctionIdentifier::class.java)
    }

    fun <T:PsiElement> assertReferenceIs(line: Int, column: Int, psiNode: Class<T>) {
        myFixture.configureByFiles("ImmunityCheck-1.0.0.cql", "Commons-1.0.0.cql", "Commons-1.0.2.cql")
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
        return "src/test/testData/ImmunityCheck"
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