package org.pathcheck.intellij.cql.utils

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import org.cqframework.cql.cql2elm.model.Model
import org.hl7.cql.model.ClassType
import org.hl7.cql.model.DataType


fun Model.takeIndex(): Map<String, DataType> {
    return Model::class.java.getDeclaredField("index").let { field ->
        field.isAccessible = true
        return@let field.get(this)
    } as Map<String, DataType>
}

fun Model.expandLookup(): List<LookupElementBuilder> {
    return takeIndex().map {
        val type = it.value
        if (type is ClassType) {
            LookupElementBuilder.create(type.simpleName)
                .withTypeText(type.name, true)
                .withIcon(AllIcons.Nodes.Type)
        } else {
            LookupElementBuilder.create(type)
                .withTypeText(type.toString(), true)
                .withIcon(AllIcons.Nodes.Type)
        }
    }
}

