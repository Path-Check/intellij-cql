package org.pathcheck.intellij.cql.psi.definitions

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode
import org.cqframework.cql.gen.cqlParser
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.references.Identifier

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

class AccessModifier(node: ASTNode) : BasePsiNode(node)

class QualifiedIdentifier(node: ASTNode) : BasePsiNode(node) {
    fun identifier(): Identifier? {
        return getRule(Identifier::class.java, 0)
    }

    fun qualifier(): List<Qualifier>? {
        return getRules(Qualifier::class.java)
    }

    fun qualifier(i: Int): Qualifier? {
        return getRule(Qualifier::class.java, i)
    }
}

class Qualifier(node: ASTNode) : BasePsiNode(node) {
    fun identifier(): Identifier? {
        return getRule(Identifier::class.java, 0)
    }
}

class VersionSpecifier(node: ASTNode) : BasePsiNode(node) {
    fun STRING(): ANTLRPsiLeafNode? {
        return getToken(cqlParser.STRING, 0)
    }
}

class LocalIdentifier(node: ASTNode) : BasePsiNode(node) {
    fun identifier(): Identifier? {
        return getRule(Identifier::class.java, 0)
    }
}

class Codesystems(node: ASTNode) : BasePsiNode(node) {
    fun codesystemIdentifier(): List<CodesystemIdentifier>? {
        return getRules(CodesystemIdentifier::class.java)
    }

    fun codesystemIdentifier(i: Int): CodesystemIdentifier? {
        return getRule(CodesystemIdentifier::class.java, i)
    }
}

class CodesystemIdentifier(node: ASTNode) : BasePsiNode(node) {
    fun identifier(): Identifier? {
        return getRule(Identifier::class.java, 0)
    }

    fun libraryIdentifier(): LibraryIdentifier? {
        return getRule(LibraryIdentifier::class.java, 0)
    }
}

class LibraryIdentifier(node: ASTNode) : BasePsiNode(node) {
    fun identifier(): Identifier? {
        return getRule(Identifier::class.java, 0)
    }
}

class CodeIdentifier(node: ASTNode) : BasePsiNode(node) {
    fun identifier(): Identifier? {
        return getRule(Identifier::class.java, 0)
    }

    fun libraryIdentifier(): LibraryIdentifier? {
        return getRule(LibraryIdentifier::class.java, 0)
    }
}

class CodesystemId(node: ASTNode) : BasePsiNode(node) {
    fun STRING(): ANTLRPsiLeafNode? {
        return getToken(cqlParser.STRING, 0)
    }
}

class ValuesetId(node: ASTNode) : BasePsiNode(node) {
    fun STRING(): ANTLRPsiLeafNode? {
        return getToken(cqlParser.STRING, 0)
    }
}

class CodeId(node: ASTNode) : BasePsiNode(node) {
    fun STRING(): ANTLRPsiLeafNode? {
        return getToken(cqlParser.STRING, 0)
    }
}

class DisplayClause(node: ASTNode) : BasePsiNode(node) {
    fun STRING(): ANTLRPsiLeafNode? {
        return getToken(cqlParser.STRING, 0)
    }
}