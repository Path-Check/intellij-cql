package org.pathcheck.intellij.cql.utils

import com.intellij.psi.PsiElement
import org.antlr.intellij.adaptor.psi.ScopeNode

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

fun PsiElement.printParentStack(): String {
    return if (parent == null) {
        ""
    } else {
        val level = parent.printParentStack()

        var addedInfo = ""
        if (this is ScopeNode) {
            addedInfo = "ScopeNode"
        }

        val myText = text.split("\n").first().take(20)

        println("$level$this <- $addedInfo \t\t $myText")
        "  $level"
    }
}