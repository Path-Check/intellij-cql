package org.pathcheck.intellij.cql.psi.definitions

import com.intellij.lang.ASTNode
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode

class Definition(node: ASTNode) : BasePsiNode(node) {
    fun usingDefinition(): UsingDefinition? {
        return getRule(UsingDefinition::class.java, 0)
    }

    fun includeDefinition(): IncludeDefinition? {
        return getRule(IncludeDefinition::class.java, 0)
    }

    fun codesystemDefinition(): CodesystemDefinition? {
        return getRule(CodesystemDefinition::class.java, 0)
    }

    fun valuesetDefinition(): ValuesetDefinition? {
        return getRule(ValuesetDefinition::class.java, 0)
    }

    fun codeDefinition(): CodeDefinition? {
        return getRule(CodeDefinition::class.java, 0)
    }

    fun conceptDefinition(): ConceptDefinition? {
        return getRule(ConceptDefinition::class.java, 0)
    }

    fun parameterDefinition(): ParameterDefinition? {
        return getRule(ParameterDefinition::class.java, 0)
    }
}