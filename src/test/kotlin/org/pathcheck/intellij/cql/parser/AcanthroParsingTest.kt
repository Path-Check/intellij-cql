package org.pathcheck.intellij.cql.parser

class AcanthroParsingTest : BaseParsingTestCase("Acanthro") {
    fun testAcanthro() {
        doTest("acanthro-0.99.99", true, true)
    }

    fun testAnthroBase() {
        doTest("anthrobase-0.99.99", true, true)
    }

    fun testAnthroTest() {
        doTest("anthrotest-0.99.99", true, true)
    }
}