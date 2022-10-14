package org.pathcheck.intellij.cql.psi.antlr

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.findParentOfType
import org.antlr.intellij.adaptor.lexer.TokenIElementType
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.cqframework.cql.cql2elm.DataTypes
import org.cqframework.cql.cql2elm.model.ConversionMap
import org.cqframework.cql.cql2elm.model.Model
import org.hl7.cql.model.*
import org.pathcheck.intellij.cql.psi.CqlFileRoot
import org.pathcheck.intellij.cql.psi.IdentifierPSINode
import org.pathcheck.intellij.cql.psi.Library
import java.util.*

open class BasePsiNode(node: ASTNode) : ANTLRPsiNode(node) {

    fun <T : BasePsiNode?> getRule(ctxType: Class<out T>?, i: Int): T? {
        if (i < 0 || i >= children.size) {
            return null
        }

        var j = -1 // what element have we found with ctxType?

        for (o in children) {
            if (ctxType!!.isInstance(o)) {
                j++
                if (j == i) {
                    return ctxType.cast(o)
                }
            }
        }
        return null
    }

    open fun <T : BasePsiNode?> getRules(ctxType: Class<out T>): List<T>? {
        var contexts = mutableListOf<T>()
        for (o in children) {
            if (ctxType.isInstance(o)) {
                contexts.add(ctxType.cast(o))
            }
        }
        return contexts
    }

    open fun getToken(ttype: Int, i: Int): IdentifierPSINode? {
        if (i < 0 || i >= children.size) {
            return null
        }
        var j = -1 // what token with ttype have we found?
        for (o in children) {
            if (o.node.elementType is TokenIElementType) {
                if ((o.node.elementType as TokenIElementType).antlrTokenType == ttype) {
                    j++
                    if (j == i) {
                        return o as IdentifierPSINode
                    }
                }
            }
        }
        return null
    }

    open fun anyToken(): LeafPsiElement? {
        return children.firstOrNull { it.node.elementType is TokenIElementType } as LeafPsiElement?
    }

    fun any(): PsiElement? {
        return children.firstOrNull()
    }

    // Type Logic
    fun getLibrary(): Library? {
        return findParentOfType<Library>(true)
    }
    fun resolveModel(modelName: String): Model? {
        return getLibrary()?.findModel(modelName)
    }

    fun findMemberType(type: DataType?, member: String?): DataType? {
        if (type == null || member == null) return null

        if (type is ClassType) {
            return type.elements.filter { it.name == member }.firstOrNull()?.type
        }

        if (type is ListType) {
            // List Demotion
            val innerElement = type.elementType
            if (innerElement is ClassType) {
                return innerElement.elements.filter { it.name == member }.firstOrNull()?.type
            }
        }

        return null
    }

    fun resolveTypeName(modelName: String?, typeName: String): DataType? {
        if (modelName == null) {
            // finds a model that has the typeName
            getLibrary()?.findModels()?.forEach { model ->
                model.resolveLabel(typeName)?.let { return it }
                model.resolveTypeName(typeName)?.let { return it }
            }
        } else {
            val model = resolveModel(modelName)
            if (model != null) {
                model.resolveLabel(typeName)?.let { return it }
                model.resolveTypeName(typeName)?.let { return it }
            }
        }
        return null
    }

    fun innerElementIfExists(e: DataType?): DataType? {
        if (e == null) return null

        // it's supposed to be an interval
        if (e is IntervalType) {
            return e.pointType
        }
        if (e is ListType) {
            return e.elementType
        }
        return e
    }

    open fun ensureCompatibleTypes(first: DataType?, second: DataType?): DataType? {
        if (first == null || second == null) {
            return null
        }

        val compatibleType = findCompatibleType(first, second)
        if (compatibleType != null) {
            return compatibleType
        }

        if (!second.isSubTypeOf(first)) {
            return ChoiceType(Arrays.asList(first, second))
        }

        // The above construction of a choice type guarantees this will never be hit
        DataTypes.verifyType(second, first)
        return first
    }

    open fun findIncompleteCompatibleType(first: DataType?, second: DataType?): DataType? {
        // finds compatible type even with just one element present.
        if (first != null && second != null) {
            return findCompatibleType(first, second)
        }

        if (first == null && second != null) {
            return second
        }

        if (first != null) {
            return first
        }

        return null
    }

    open fun findCompatibleType(first: DataType, second: DataType): DataType? {
        if (first == DataType.ANY) {
            return second
        }
        if (second == DataType.ANY) {
            return first
        }
        if (first.isSuperTypeOf(second) || second.isCompatibleWith(first)) {
            return first
        }
        if (second.isSuperTypeOf(first) || first.isCompatibleWith(second)) {
            return second
        }

        // If either side is a choice type, don't allow conversions because they will incorrectly eliminate choices based on convertibility
        if (!(first is ChoiceType || second is ChoiceType)) {
            val map = (containingFile as CqlFileRoot).operatorMap

            var conversion = conversionMap.findConversion(second, first, true, false, map)
            if (conversion != null) {
                return first
            }
            conversion = conversionMap.findConversion(first, second, true, false, map)
            if (conversion != null) {
                return second
            }
        }

        return null
    }


    companion object {
        private val conversionMap = ConversionMap()
    }

}