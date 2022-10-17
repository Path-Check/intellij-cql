package org.pathcheck.intellij.cql.run

import com.intellij.execution.configurations.RunConfigurationOptions
import com.intellij.openapi.components.StoredProperty

class CqlRunConfigurationOptions : RunConfigurationOptions() {
    private val expressionNameProp: StoredProperty<String?> =
        string("").provideDelegate(this, "expressionName")

    private val dataFileNameProp: StoredProperty<String?> =
        string("").provideDelegate(this, "dataFileName")

    fun getExpressionName(): String {
        return expressionNameProp.getValue(this)!!
    }

    fun setExpressionName(scriptName: String) {
        expressionNameProp.setValue(this, scriptName)
    }

    fun getDataFileName(): String {
        return dataFileNameProp.getValue(this)!!
    }

    fun setDataFileName(scriptName: String) {
        dataFileNameProp.setValue(this, scriptName)
    }
}