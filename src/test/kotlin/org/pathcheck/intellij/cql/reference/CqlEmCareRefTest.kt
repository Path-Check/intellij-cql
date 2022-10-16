package org.pathcheck.intellij.cql.reference

import org.pathcheck.intellij.cql.psi.scopes.Alias
import org.pathcheck.intellij.cql.psi.statements.OperandDefinition


class CqlEmCareRefTest: BaseReferenceTestCase() {
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

    override fun loadFiles(): List<String> {
        return listOf("EmCareBase-0.99.99.cql")
    }

    override fun testDirectory(): String {
        return  "src/test/testData/EmCare"
    }
}