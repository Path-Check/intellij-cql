package org.pathcheck.intellij.cql

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import javax.swing.Icon

class CqlPSIFileRoot(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, CqlLanguage) {
    override fun getFileType(): FileType {
        return CqlFileType
    }

    override fun toString(): String {
        return "Sample Language file"
    }

    override fun getIcon(flags: Int): Icon {
        return CqlIcons.FILE
    }
}