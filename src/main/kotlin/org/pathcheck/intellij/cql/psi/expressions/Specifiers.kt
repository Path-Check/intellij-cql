package org.pathcheck.intellij.cql.psi.expressions

import com.intellij.lang.ASTNode
import org.hl7.cql.model.*
import org.pathcheck.intellij.cql.psi.IsResultType
import org.pathcheck.intellij.cql.psi.antlr.BasePsiNode
import org.pathcheck.intellij.cql.psi.definitions.Qualifier
import org.pathcheck.intellij.cql.psi.references.ReferentialOrTypeNameIdentifier
import org.pathcheck.intellij.cql.psi.references.ReferentialIdentifier

class TypeSpecifier(node: ASTNode) : BasePsiNode(node), IsResultType {
    fun namedTypeSpecifier(): NamedTypeSpecifier? {
        return getRule(NamedTypeSpecifier::class.java, 0)
    }

    fun listTypeSpecifier(): ListTypeSpecifier? {
        return getRule(ListTypeSpecifier::class.java, 0)
    }

    fun intervalTypeSpecifier(): IntervalTypeSpecifier? {
        return getRule(IntervalTypeSpecifier::class.java, 0)
    }

    fun tupleTypeSpecifier(): TupleTypeSpecifier? {
        return getRule(TupleTypeSpecifier::class.java, 0)
    }

    fun choiceTypeSpecifier(): ChoiceTypeSpecifier? {
        return getRule(ChoiceTypeSpecifier::class.java, 0)
    }

    override fun resolveType(): DataType? {
        val dataType = (any() as? IsResultType)?.resolveType()
        println("${any()?.text} $dataType ${any()?.javaClass}")
        return (any() as? IsResultType)?.resolveType()
    }
}

class NamedTypeSpecifier(node: ASTNode) : BasePsiNode(node), IsResultType {
    fun referentialOrTypeNameIdentifier(): ReferentialOrTypeNameIdentifier? {
        return getRule(ReferentialOrTypeNameIdentifier::class.java, 0)
    }

    fun qualifier(): List<Qualifier>? {
        return getRules(Qualifier::class.java)
    }

    fun qualifier(i: Int): Qualifier? {
        return getRule(Qualifier::class.java, i)
    }

    // From: (qualifier '.')* referentialOrTypeNameIdentifier
    // To: modelIdentifier  (qualifier '.')* referentialOrTypeNameIdentifier
    // if modelIdentifier is null, loops through all models
    override fun resolveType(): DataType? {
        println("Named Type ${referentialOrTypeNameIdentifier()?.text}")
        val identifier = referentialOrTypeNameIdentifier()?.text ?: return null
        return if (qualifier().isNullOrEmpty()) {
            resolveTypeName(null, identifier)
        } else {
            val idString = qualifier()?.drop(1)?.plus(identifier)?.joinToString(".")
            resolveTypeName(qualifier(0)?.text, idString!!)
        }
    }
}

class ListTypeSpecifier(node: ASTNode) : BasePsiNode(node), IsResultType {
    fun typeSpecifier(): TypeSpecifier? {
        return getRule(TypeSpecifier::class.java, 0)
    }

    override fun resolveType(): DataType? {
        return ListType(typeSpecifier()?.resolveType())
    }
}

class IntervalTypeSpecifier(node: ASTNode) : BasePsiNode(node), IsResultType {
    fun typeSpecifier(): TypeSpecifier? {
        return getRule(TypeSpecifier::class.java, 0)
    }

    override fun resolveType(): DataType? {
        return IntervalType(typeSpecifier()?.resolveType())
    }
}

class TupleTypeSpecifier(node: ASTNode) : BasePsiNode(node), IsResultType {
    fun tupleElementDefinition(): List<TupleElementDefinition>? {
        return getRules(TupleElementDefinition::class.java)
    }

    fun tupleElementDefinition(i: Int): TupleElementDefinition? {
        return getRule(TupleElementDefinition::class.java, i)
    }

    override fun resolveType(): DataType? {
        var tupleType = TupleType()
        tupleElementDefinition()?.forEach {
            tupleType.addElement(
                TupleTypeElement(
                    it.referentialIdentifier()?.text,
                    it.resolveType()
                )
            )
        }
        return tupleType
    }
}

class TupleElementDefinition(node: ASTNode) : BasePsiNode(node), IsResultType {
    fun referentialIdentifier(): ReferentialIdentifier? {
        return getRule(ReferentialIdentifier::class.java, 0)
    }

    fun typeSpecifier(): TypeSpecifier? {
        return getRule(TypeSpecifier::class.java, 0)
    }

    override fun resolveType(): DataType? {
        return typeSpecifier()?.resolveType()
    }
}

class ChoiceTypeSpecifier(node: ASTNode) : BasePsiNode(node), IsResultType {
    fun typeSpecifier(): List<TypeSpecifier>? {
        return getRules(TypeSpecifier::class.java)
    }

    fun typeSpecifier(i: Int): TypeSpecifier? {
        return getRule(TypeSpecifier::class.java, i)
    }

    override fun resolveType(): DataType? {
        return ChoiceType(typeSpecifier()?.map { it.resolveType() })
    }
}