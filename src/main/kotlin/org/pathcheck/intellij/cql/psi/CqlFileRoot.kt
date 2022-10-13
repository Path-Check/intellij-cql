package org.pathcheck.intellij.cql.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import org.antlr.intellij.adaptor.psi.ScopeNode
import org.pathcheck.intellij.cql.CqlFileType
import org.pathcheck.intellij.cql.CqlIcons
import org.pathcheck.intellij.cql.CqlLanguage
import javax.swing.Icon


class CqlFileRoot(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, CqlLanguage) {

    fun library(): Library? {
        return findChildByClass(Library::class.java)
    }

    override fun getFileType(): FileType {
        return CqlFileType
    }

    override fun toString(): String {
        return "Cql Language file"
    }

    override fun getIcon(flags: Int): Icon {
        return CqlIcons.FILE
    }
}