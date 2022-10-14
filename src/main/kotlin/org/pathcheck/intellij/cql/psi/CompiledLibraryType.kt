package org.pathcheck.intellij.cql.psi

import org.hl7.cql.model.DataType
import org.hl7.cql.model.InstantiationContext
import org.hl7.elm.r1.Library

class CompiledLibraryType(val library: Library): DataType() {
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