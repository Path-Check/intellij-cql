package org.pathcheck.intellij.cql.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.pathcheck.intellij.cql.psi.definitions.QualifiedIdentifier
import org.pathcheck.intellij.cql.psi.scopes.AggregateClause
import org.pathcheck.intellij.cql.psi.scopes.Alias
import org.pathcheck.intellij.cql.psi.statements.ExpressionDefinition
import org.pathcheck.intellij.cql.utils.CaretUtils


class CqlAggregateRefTest : BasePlatformTestCase() {
    fun testReferenceToQueryAliasOfX() {
        assertReferenceIs("Aggregate.cql", 15, 33, Alias::class.java)
    }

    fun testReferenceToAggregateDeclarationOfR() {
        assertReferenceIs("Aggregate.cql", 15, 29, AggregateClause::class.java)
    }

    fun testReferenceToExpressionDefOne() {
        assertReferenceIs("Aggregate.cql", 14, 4, ExpressionDefinition::class.java)
    }

    fun testReferenceToAliasSeq() {
        assertReferenceIs("Aggregate.cql", 11, 35, Alias::class.java)
    }

    fun testReferenceToExpressionDefSeq() {
        assertReferenceIs("Aggregate.cql", 10, 5, ExpressionDefinition::class.java)
    }

    fun <T:PsiElement> assertReferenceIs(fileName: String, line: Int, column: Int, psiNode: Class<T>) {
        myFixture.configureByFiles(fileName)
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
        return "src/test/testData/CqlBase"
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