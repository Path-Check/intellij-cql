package org.pathcheck.intellij.cql.utils

import com.intellij.codeInsight.lookup.LookupElementBuilder
import javax.swing.Icon

object LookupHelper {
    fun build(name: String?, icon: Icon?, tailText: String?, typeText: String?): LookupElementBuilder? {
        if (name == null) return null;

        var lookup = LookupElementBuilder.create(name)

        if (icon != null)
            lookup = lookup.withIcon(icon)

        if (tailText != null)
            lookup = lookup.withTailText(tailText, true)

        if (typeText != null)
            lookup = lookup.withTypeText(typeText)

        return lookup
    }
}