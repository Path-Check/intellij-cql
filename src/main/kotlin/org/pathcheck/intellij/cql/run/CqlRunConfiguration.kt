package org.pathcheck.intellij.cql.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project

class CqlRunConfiguration(project: Project, factory: ConfigurationFactory?, name: String?) :
    RunConfigurationBase<CqlRunConfigurationOptions>(project, factory, name) {

    override fun getOptions(): CqlRunConfigurationOptions {
        return super.getOptions() as CqlRunConfigurationOptions
    }

    fun getExpressionName(): String {
        return options.getExpressionName()
    }

    fun setExpressionName(expressionName: String) {
        options.setExpressionName(expressionName)
    }

    fun getDataFileName(): String {
        return options.getDataFileName()
    }

    fun setDataFileName(expressionName: String) {
        options.setDataFileName(expressionName)
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration?> {
        return CqlSettingsEditor(project)
    }

    override fun checkConfiguration() {}

    override fun getState(executor: Executor, executionEnvironment: ExecutionEnvironment): RunProfileState? {
        return object : CommandLineState(executionEnvironment) {
            @Throws(ExecutionException::class)
            override fun startProcess(): ProcessHandler {
                val commandLine = GeneralCommandLine(options.getExpressionName())
                val processHandler = ProcessHandlerFactory.getInstance().createColoredProcessHandler(commandLine)
                ProcessTerminatedListener.attach(processHandler)
                return processHandler
            }
        }
    }
}