package org.pathcheck.intellij.cql.psi

import org.hl7.cql.model.DataType

/**
 * Provides a representation for code completion
 */
interface HasResultType {
    fun getResultType(): DataType?
}