package org.pathcheck.intellij.cql.utils

import com.intellij.psi.PsiElement

fun PsiElement.cleanText(): String {
    return text
        .removeSurrounding("\'")
        .removeSurrounding("\"")
        .removeSurrounding("`")
}