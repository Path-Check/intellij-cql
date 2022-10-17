package org.pathcheck.intellij.cql.psi.expressions

import com.intellij.lang.ASTNode
import com.intellij.openapi.diagnostic.thisLogger
import org.hl7.cql.model.DataType
import org.pathcheck.intellij.cql.psi.HasResultType
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.references.ReferentialIdentifier
import org.pathcheck.intellij.cql.psi.scopes.Function

open class Invocation(node: ASTNode) : BasePsiNode(node), HasResultType {
    // This should never happen
    override fun getResultType(): DataType? {
        thisLogger().warn("Calling Result Type in an Object that should not exist: $this")
        return null
    }
}

class TotalInvocation(node: ASTNode) : Invocation(node) {
    override fun getResultType() = resolveTypeName("System", "Decimal")
}
class ThisInvocation(node: ASTNode) : Invocation(node) {
    override fun getResultType() = resolveTypeName("System", "Integer")
}
class IndexInvocation(node: ASTNode) : Invocation(node) {
    override fun getResultType() = resolveTypeName("System", "Integer")
}

class FunctionInvocation(node: ASTNode) : Invocation(node) {
    fun function(): Function? {
        return getRule(Function::class.java, 0)
    }

    override fun getResultType() = function()?.getResultType()
}

class MemberInvocation(node: ASTNode) : Invocation(node) {
    fun referentialIdentifier(): ReferentialIdentifier? {
        return getRule(ReferentialIdentifier::class.java, 0)
    }

    override fun getResultType() = referentialIdentifier()?.getResultType()
}