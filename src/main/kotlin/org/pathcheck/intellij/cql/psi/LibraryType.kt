package org.pathcheck.intellij.cql.psi

import org.hl7.cql.model.DataType
import org.hl7.cql.model.InstantiationContext

class LibraryType(val library: Library): DataType() {
    override fun isGeneric(): Boolean {
        return false
    }

    override fun isInstantiable(callType: DataType?, context: InstantiationContext?): Boolean {
        return false
    }

    override fun instantiate(context: InstantiationContext?): DataType {
        return this
    }
}