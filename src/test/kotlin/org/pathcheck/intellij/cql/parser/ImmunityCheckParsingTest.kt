package org.pathcheck.intellij.cql.parser

class ImmunityCheckParsingTest : BaseParsingTestCase("ImmunityCheck") {
    fun testImmunityCheck() {
        doTest("ImmunityCheck-1.0.0", true, true)
    }

    fun testCommons100() {
        doTest("Commons-1.0.0", true, true)
    }

    fun testCommons102() {
        doTest("Commons-1.0.2", true, true)
    }
}