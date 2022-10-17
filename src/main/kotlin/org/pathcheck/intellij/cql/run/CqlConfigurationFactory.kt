package org.pathcheck.intellij.cql.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.project.Project

class CqlConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {

    override fun getId(): String {
        return CqlRunConfigurationType.ID
    }

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return CqlRunConfiguration(project, this, "Cql")
    }

    override fun getOptionsClass(): Class<out BaseState?>? {
        return CqlRunConfigurationOptions::class.java
    }
}