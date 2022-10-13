package org.pathcheck.intellij.cql.psi

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode
import org.cqframework.cql.gen.cqlParser
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode

class Identifier(node: ASTNode) : BasePsiNode(node) {
    fun IDENTIFIER(): IdentifierPSINode? {
        return getToken(cqlParser.IDENTIFIER, 0)
    }

    fun DELIMITEDIDENTIFIER(): IdentifierPSINode? {
        return getToken(cqlParser.DELIMITEDIDENTIFIER, 0)
    }

    fun QUOTEDIDENTIFIER(): IdentifierPSINode? {
        return getToken(cqlParser.QUOTEDIDENTIFIER, 0)
    }

    fun all(): List<IdentifierPSINode> {
        return listOfNotNull(IDENTIFIER(), DELIMITEDIDENTIFIER(), QUOTEDIDENTIFIER())
    }

    fun any(): IdentifierPSINode? {
        return all().firstOrNull()
    }
}