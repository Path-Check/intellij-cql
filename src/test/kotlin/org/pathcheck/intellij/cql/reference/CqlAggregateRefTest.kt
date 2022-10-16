package org.pathcheck.intellij.cql.reference

import org.pathcheck.intellij.cql.psi.scopes.AggregateClause
import org.pathcheck.intellij.cql.psi.scopes.Alias
import org.pathcheck.intellij.cql.psi.statements.ExpressionDefinition


class CqlAggregateRefTest: BaseReferenceTestCase() {
    fun testReferenceToQueryAliasOfX() {
        assertReferenceIs( 15, 33, Alias::class.java)
    }

    fun testReferenceToAggregateDeclarationOfR() {
        assertReferenceIs(15, 29, AggregateClause::class.java)
    }

    fun testReferenceToExpressionDefOne() {
        assertReferenceIs( 14, 4, ExpressionDefinition::class.java)
    }

    fun testReferenceToAliasSeq() {
        assertReferenceIs( 11, 35, Alias::class.java)
    }

    fun testReferenceToExpressionDefSeq() {
        assertReferenceIs( 10, 5, ExpressionDefinition::class.java)
    }

    override fun loadFiles(): List<String> {
        return listOf("Aggregate.cql")
    }

    override fun testDirectory(): String {
        return "src/test/testData/CqlBase"
    }
}