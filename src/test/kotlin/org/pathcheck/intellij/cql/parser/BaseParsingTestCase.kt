package org.pathcheck.intellij.cql.parser

import com.intellij.testFramework.ParsingTestCase
import java.io.IOException

abstract class BaseParsingTestCase(subfolder: String) : ParsingTestCase(subfolder, "cql", CqlParserDefinition()) {
    fun doTest(name: String, checkResult: Boolean, ensureNoErrorElements: Boolean) {
        try {
            parseFile(name, loadFile("$name.$myFileExt"))
            if (checkResult) {
                checkResult(name, myFile)
                if (ensureNoErrorElements) {
                    ensureNoErrorElements()
                }
            } else {
                toParseTreeText(myFile, skipSpaces(), includeRanges())
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        System.setProperty("idea.log.debug.categories", "#org.pathcheck.intellij.cql");
    }

    /**
     * @return path to test data file directory relative to root of this module.
     */
    override fun getTestDataPath(): String {
        return "src/test/testData"
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }
}