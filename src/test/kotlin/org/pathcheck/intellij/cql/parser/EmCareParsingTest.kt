package org.pathcheck.intellij.cql.parser

class EmCareParsingTest : BaseParsingTestCase("EmCare") {
    fun testEmCareB6measurements() {
        doTest("emcareb6measurements-0.99.99", true, true)
    }

    fun testEmCareBase() {
        doTest("EmCareBase-0.99.99", true, true)
    }

    fun testWeightForAge() {
        doTest("WeightForAge-0.99.99", true, true)
    }
}