package org.pathcheck.intellij.cql.completion

class CqlAggregateCompletionTest: BaseCompletionTestCase() {
    fun testCompletionIncludesKeywords() {
        assertCompletionHas( 6, 2, "date", "day", "days", "default", "define",
            "desc", "descending", "difference", "display", "distinct", "div", "duration", "during")
    }

    fun testCompletionAliasSeq() {
        assertCompletionHas( 11, 35, "Seq", "ValueSet")
    }

    fun testCompletionFunctionOne() {
        assertCompletionHas( 14, 5, "One", "Concept", "Long")
    }

    override fun loadFiles(): List<String> {
        return listOf("Aggregate.cql")
    }

    override fun testDirectory(): String {
        return "src/test/testData/CqlBase"
    }
}