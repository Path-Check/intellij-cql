package org.pathcheck.intellij.cql.parser

class CqlBaseParsingTest : BaseParsingTestCase("CqlBase") {
    fun testAggregate() {
        doTest("Aggregate", true, true)
    }
}