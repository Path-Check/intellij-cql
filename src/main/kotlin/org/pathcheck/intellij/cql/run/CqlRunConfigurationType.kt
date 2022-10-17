package org.pathcheck.intellij.cql.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import org.pathcheck.intellij.cql.CqlIcons
import javax.swing.Icon

class CqlRunConfigurationType: ConfigurationType {
    override fun getDisplayName(): String {
        return "CQL"
    }

    override fun getConfigurationTypeDescription(): String {
        return "CQL Run Configuration type"
    }

    override fun getIcon(): Icon {
        return CqlIcons.FILE
    }

    override fun getId(): String {
        return ID
    }

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf(CqlConfigurationFactory(this))
    }

    companion object {
        const val ID = "CqlRunConfiguration"
    }
}