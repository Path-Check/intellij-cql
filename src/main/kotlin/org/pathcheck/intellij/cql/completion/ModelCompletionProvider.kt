package org.pathcheck.intellij.cql.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.util.ProcessingContext
import org.hl7.cql.model.DataType
import org.pathcheck.intellij.cql.psi.scopes.FileRootSubtree
import org.pathcheck.intellij.cql.utils.getPrivateProperty

class ModelCompletionProvider: CompletionProvider<CompletionParameters>() {
    override fun addCompletions(params: CompletionParameters, context: ProcessingContext, result: CompletionResultSet) {
        val prefix = result.prefixMatcher.prefix
        if (prefix.isEmpty()) {
            return
        }

        val root = params.originalFile
        if (root is FileRootSubtree) {
            val models = root.findModels()
            if (models.size > 1) {
                // Uses code completion with prefix
                models.forEach { model ->
                    val mapNames = model.getPrivateProperty("index") as Map<String, DataType>

                    (mapNames)
                        .filter { it.key.startsWith(prefix, true) }
                        .forEach {
                            result.addElement(
                                LookupElementBuilder.create(it.key)
                                    .withTypeText(it.value.toLabel(), true)
                                    .withIcon(AllIcons.Nodes.Type)
                            )
                        }
                }
            } else {
                models.forEach { model ->
                    val mapNames = model.getPrivateProperty("nameIndex") as Map<String, DataType>
                    val mapClasses = model.getPrivateProperty("classIndex") as Map<String, DataType>

                    (mapNames + mapClasses)
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
    }
}