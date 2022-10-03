package org.pathcheck.intellij.cql

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.ExternalAnnotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile
import org.cqframework.cql.cql2elm.*
import org.cqframework.cql.cql2elm.quick.FhirLibrarySourceProvider
import org.cqframework.cql.elm.tracking.TrackBack
import java.io.IOException

class CqlExternalAnnotator : ExternalAnnotator<PsiFile?, List<CqlCompilerException>?>() {
    /** Called first; return file  */
    override fun collectInformation(file: PsiFile): PsiFile {
        return file
    }

    /** Called 2nd; run antlr on file  */
    override fun doAnnotate(file: PsiFile?): List<CqlCompilerException>? {
        val fileContents = file!!.text

        return ApplicationManager.getApplication().runReadAction<List<CqlCompilerException>?> {
            /*
                Some libraries use ServiceLoader to detect and load implementations. For this to work in a plugin,
                the context class loader must be set to the plugin's classloader and restored afterwards with the
                original one around initialization code:
             */
            val currentThread = Thread.currentThread()
            val originalClassLoader = currentThread.contextClassLoader
            val pluginClassLoader = this.javaClass.classLoader
            try {
                currentThread.contextClassLoader = pluginClassLoader
                try {
                    // ModelManager and Library Manager must be created in this contxt to make sure ServiceLoaders are ready
                    val modelManager = ModelManager()
                    val libraryManager = LibraryManager(modelManager).apply {
                        librarySourceLoader.registerProvider(PsiDirectoryLibrarySourceProvider(file.containingDirectory))
                    }

                    val compiler = CqlCompiler(modelManager, libraryManager)
                    compiler.run(fileContents)
                    (compiler.errors + compiler.warnings + compiler.messages).filter {
                        // Only returns annotations for the current file.
                        it.locator.library.id == compiler.library.identifier.id
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    throw RuntimeException(e)
                }

            } finally {
                currentThread.contextClassLoader = originalClassLoader
            }
        }
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

    private fun nthIndex(string: String, ch: Char, n: Int): Int {
        if (n <= 0) return 0
        return string.length - string.replace("^([^$ch]*$ch){$n}".toRegex(), "").length - 1
    }

    /**
     * converts the CQL Exception TrackBack (line + column, line + column) into a PSI TextRange format (char, char)
     */
    private fun locatorToRange(locator: TrackBack, text: String): TextRange? {
        val startChar = (nthIndex(text, '\n', locator.startLine-1) + locator.startChar)
        var endChar = (nthIndex(text, '\n', locator.endLine-1) + locator.endChar)

        if (endChar > text.length) endChar = text.length

        if (startChar == 1 && endChar == text.length) {
            // full file
            return null
        }

        return TextRange(startChar, endChar)
    }

    private fun annotateIssue(file: PsiFile, holder: AnnotationHolder, issue: CqlCompilerException) {
        val builder = when (issue.severity) {
            CqlCompilerException.ErrorSeverity.Error -> holder.newAnnotation(HighlightSeverity.ERROR, issue.message!!)
            CqlCompilerException.ErrorSeverity.Warning -> holder.newAnnotation(HighlightSeverity.WARNING, issue.message!!)
            CqlCompilerException.ErrorSeverity.Info -> holder.newAnnotation(HighlightSeverity.INFORMATION, issue.message!!)
            null -> holder.newAnnotation(HighlightSeverity.GENERIC_SERVER_ERROR_OR_WARNING, issue.message!!)
        }

        val keyRange = locatorToRange(issue.locator, file.text)
        if (keyRange != null) {
            builder.range(keyRange)
        }

        builder.create()
    }
}