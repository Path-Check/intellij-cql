package org.pathcheck.intellij.cql.elm

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import org.cqframework.cql.cql2elm.CqlCompiler
import org.cqframework.cql.cql2elm.CqlCompilerException
import org.cqframework.cql.elm.tracking.TrackBack
import org.hl7.elm.r1.Library
import org.pathcheck.intellij.cql.psi.CqlFileRoot
import org.pathcheck.intellij.cql.utils.CaretUtils
import java.io.IOException

class CqlExternalAnnotator : ExternalAnnotator<PsiFile?, List<CqlCompilerException>?>() {
    /** Called first; return file  */
    override fun collectInformation(file: PsiFile): PsiFile {
        return file
    }

    /** Called 2nd; run antlr on file  */
    override fun doAnnotate(file: PsiFile?): List<CqlCompilerException>? {
        if (file == null) return emptyList()

        // Must be on a RunAction to open dependency files.
        return ApplicationManager.getApplication().runReadAction<List<CqlCompilerException>?> {
            compile(file)
        }
    }

    private fun compile(file: PsiFile): List<CqlCompilerException>? {
        if (file !is CqlFileRoot) return emptyList()

        try {
            val compiler = GlobalCache.compileLibrary(file)

            file.operatorMap = compiler.compiledLibrary.operatorMap

            return (compiler.errors
                + compiler.warnings
                + compiler.messages
                + checkFileName(file.name, compiler.library, file.text)
                ).filter {
                    // Only returns annotations for the current file.
                    it.locator.library.id == compiler.library.identifier.id
                }
        } catch (e: IOException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }
    }

    private fun checkFileName(fileName: String, library: Library, text: String): List<CqlCompilerException> {
        if (library.identifier == null || library.identifier.id == null) return emptyList()

        val correctName = listOfNotNull(library.identifier.id, library.identifier.version).joinToString("-") + ".cql"
        if (fileName != correctName) {
            val start = text.indexOf(library.identifier.id)
            var end = start + library.identifier.id.length
            if (library.identifier.version != null && library.identifier.version.isNotEmpty())
                end = text.indexOf(library.identifier.version) + library.identifier.version.length

            return listOf(CqlCompilerException(
                "${library.identifier.id} filename should be $correctName",
                CqlCompilerException.ErrorSeverity.Warning,
                TrackBack(library.identifier, 0, start, 0, end)
            ))
        }
        return emptyList()
    }

    /** Called 3rd  */
    override fun apply(
        file: PsiFile,
        issues: List<CqlCompilerException>?,
        holder: AnnotationHolder
    ) {
        for (issue in issues!!) {
            annotateIssue(file, holder, issue)
        }
    }

    /**
     * converts the CQL Exception TrackBack (line + column, line + column) into a PSI TextRange format (char, char)
     */
    private fun locatorToRange(locator: TrackBack, text: String): TextRange? {
        val startChar = CaretUtils.calculateOffset(text, locator.startLine, locator.startChar)
        var endChar = CaretUtils.calculateOffset(text, locator.endLine, locator.endChar + 1)

        if (endChar > text.length) endChar = text.length

        if (startChar == 1 && endChar == text.length) {
            // full file
            return null
        }

        return TextRange(startChar, endChar)
    }

    private fun annotateIssue(file: PsiFile, holder: AnnotationHolder, issue: CqlCompilerException) {
        val severity = when (issue.severity) {
            CqlCompilerException.ErrorSeverity.Error -> HighlightSeverity.ERROR
            CqlCompilerException.ErrorSeverity.Warning -> HighlightSeverity.WARNING
            CqlCompilerException.ErrorSeverity.Info -> HighlightSeverity.INFORMATION
            null -> HighlightSeverity.GENERIC_SERVER_ERROR_OR_WARNING
        }

        val builder = holder.newAnnotation(severity, issue.message ?: "<No message found>")

        val keyRange = locatorToRange(issue.locator, file.text)
        if (keyRange != null) {
            builder.range(keyRange)
            issue.message?.let { builder.tooltip(it) }
        }

        builder.create()
    }
}