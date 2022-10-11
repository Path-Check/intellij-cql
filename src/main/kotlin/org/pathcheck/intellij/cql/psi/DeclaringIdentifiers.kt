package org.pathcheck.intellij.cql.psi

import com.intellij.psi.PsiElement

/**
 * Represents a set of visible identifiers in this scope.
 */
interface DeclaringIdentifiers {
    fun visibleIdentifiers(): List<PsiElement>
}