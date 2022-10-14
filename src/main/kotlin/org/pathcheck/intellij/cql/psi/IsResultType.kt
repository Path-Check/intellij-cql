package org.pathcheck.intellij.cql.psi

import org.hl7.cql.model.DataType

/**
 * Provides a representation for code completion
 */
interface IsResultType {
    fun resolveType(): DataType?
}