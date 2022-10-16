package org.pathcheck.intellij.cql.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.pathcheck.intellij.cql.psi.definitions.LibraryDefinition
import org.pathcheck.intellij.cql.psi.definitions.LocalIdentifier
import org.pathcheck.intellij.cql.psi.definitions.ParameterDefinition
import org.pathcheck.intellij.cql.psi.definitions.QualifiedIdentifier
import org.pathcheck.intellij.cql.psi.references.IdentifierOrFunctionIdentifier
import org.pathcheck.intellij.cql.psi.scopes.Alias
import org.pathcheck.intellij.cql.psi.statements.ExpressionDefinition
import org.pathcheck.intellij.cql.utils.CaretUtils


class CqlAcanthroRefTest : BasePlatformTestCase() {
    fun testReferenceToParameterZ() {
        assertReferenceIs( 15, 37, ParameterDefinition::class.java)
    }

    fun testReferenceToIncludeDef() {
        assertReferenceIs( 12, 6, QualifiedIdentifier::class.java)
    }

    fun testReferenceToIncludeLocalName() {
        assertReferenceIs( 18, 9, LocalIdentifier::class.java)
    }

    fun <T:PsiElement> assertReferenceIs(line: Int, column: Int, psiNode: Class<T>) {
        myFixture.configureByFiles("anthrotest-0.99.99.cql", "anthrobase-0.99.99.cql", "acanthro-0.99.99.cql")
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
        return "src/test/testData/Acanthro"
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