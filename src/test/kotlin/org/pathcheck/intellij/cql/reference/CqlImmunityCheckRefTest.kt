package org.pathcheck.intellij.cql.reference

import org.pathcheck.intellij.cql.psi.definitions.LibraryDefinition
import org.pathcheck.intellij.cql.psi.definitions.QualifiedIdentifier
import org.pathcheck.intellij.cql.psi.references.IdentifierOrFunctionIdentifier
import org.pathcheck.intellij.cql.psi.scopes.Alias
import org.pathcheck.intellij.cql.psi.statements.ExpressionDefinition


class CqlImmunityCheckRefTest: BaseReferenceTestCase() {
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

    override fun loadFiles(): List<String> {
        return listOf("ImmunityCheck-1.0.0.cql", "Commons-1.0.0.cql", "Commons-1.0.2.cql")
    }

    override fun testDirectory(): String {
        return "src/test/testData/ImmunityCheck"
    }
}