package org.pathcheck.intellij.cql.utils

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import org.hl7.cql.model.ClassType


fun ClassType.expandLookup(): List<LookupElementBuilder> {
    return elements.map {
        LookupElementBuilder.create(it.name)
            .withTypeText(it.type.toLabel(), true)
            .withIcon(AllIcons.Nodes.Field)
    }
}

