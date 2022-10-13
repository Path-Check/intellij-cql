package org.pathcheck.intellij.cql.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.util.ProcessingContext
import org.hl7.cql.model.DataType
import org.pathcheck.intellij.cql.psi.CqlFileRoot
import org.pathcheck.intellij.cql.utils.getPrivateProperty

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
            if (models.size > 1) {
                // Uses code completion without prefix
                models.forEach {
                    dataTypes.putAll(it.getPrivateProperty("index") as Map<String, DataType>)
                }
            } else {
                // Uses code completion with prefix
                models.forEach {
                    dataTypes.putAll(it.getPrivateProperty("nameIndex") as Map<String, DataType>)
                    dataTypes.putAll(it.getPrivateProperty("classIndex") as Map<String, DataType>)
                }
            }

            dataTypes
                .filter { it.key.startsWith(prefix, true) }
                .forEach {
                    result.addElement(
                        LookupElementBuilder.create(it.key)
                            .withTypeText(it.value.toLabel(), true)
                            .withIcon(AllIcons.Nodes.Type)
                    )
                }
        }
    }
}