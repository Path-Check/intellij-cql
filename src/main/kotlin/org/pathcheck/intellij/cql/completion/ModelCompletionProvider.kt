package org.pathcheck.intellij.cql.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.util.ProcessingContext
import org.hl7.cql.model.DataType
import org.pathcheck.intellij.cql.psi.CqlFileRoot
import org.pathcheck.intellij.cql.utils.takeIndex

class ModelCompletionProvider: CompletionProvider<CompletionParameters>() {
    override fun addCompletions(params: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val prefix = result.prefixMatcher.prefix
        if (prefix.isEmpty()) {
            return
        }

        val root = params.originalFile
        if (root is CqlFileRoot) {
            val models = root.library()?.findModels() ?: emptyList()
            val dataTypes = mutableMapOf<String, DataType>()

            models.forEach {
                dataTypes.putAll(it.takeIndex())
            }

            dataTypes
                .forEach {
                    result.addElement(
                        LookupElementBuilder.create(it.value.toLabel())
                            .withTypeText(it.key, true)
                            .withIcon(AllIcons.Nodes.Type)
                    )
                }
        }
    }
}