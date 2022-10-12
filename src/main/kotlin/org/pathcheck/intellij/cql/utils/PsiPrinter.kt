package org.pathcheck.intellij.cql.utils

import com.intellij.psi.PsiElement

fun PsiElement.printTree() {
    printTree("")
}

fun PsiElement.printTree(level: String = "") {
    if (children.isEmpty()) {
        println("$level$text")
    } else {
        println("$level${toString()}")
    }

    children.forEach {
        it.printTree("  $level")
    }
}