package org.pathcheck.intellij.cql

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object CqlFileType: LanguageFileType(CqlLanguage) {
    override fun getName(): String {
        return "CQL File"
    }

    override fun getDescription(): String {
        return "Clinical Quality Language File"
    }

    override fun getDefaultExtension(): String {
        return "cql"
    }

    override fun getIcon(): Icon? {
        return CqlIcons.FILE
    }
}