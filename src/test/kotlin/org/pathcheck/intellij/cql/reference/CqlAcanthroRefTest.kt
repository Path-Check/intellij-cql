package org.pathcheck.intellij.cql.reference

import org.pathcheck.intellij.cql.psi.definitions.LocalIdentifier
import org.pathcheck.intellij.cql.psi.definitions.ParameterDefinition
import org.pathcheck.intellij.cql.psi.definitions.QualifiedIdentifier


class CqlAcanthroRefTest: BaseReferenceTestCase() {
    fun testReferenceToParameterZ() {
        assertReferenceIs( 15, 37, ParameterDefinition::class.java)
    }

    fun testReferenceToIncludeDef() {
        assertReferenceIs( 12, 6, QualifiedIdentifier::class.java)
    }

    fun testReferenceToIncludeLocalName() {
        assertReferenceIs( 18, 9, LocalIdentifier::class.java)
    }

    override fun loadFiles(): List<String> {
        return listOf("anthrotest-0.99.99.cql", "anthrobase-0.99.99.cql", "acanthro-0.99.99.cql")
    }

    override fun testDirectory(): String {
        return "src/test/testData/Acanthro"
    }
}