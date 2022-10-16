package org.pathcheck.intellij.cql.utils

object CaretUtils {
    fun nthIndex(string: String, ch: Char, n: Int): Int {
        if (n <= 0) return 0
        return string.length - string.replace("^([^$ch]*$ch){$n}".toRegex(), "").length - 1
    }

    /**
     * converts from line,col to offset in string.
     *
     * line starts at 1.
     */
    fun calculateOffset(string: String, line: Int, column: Int): Int {
        return (nthIndex(string, '\n', line-1) + column)
    }
}