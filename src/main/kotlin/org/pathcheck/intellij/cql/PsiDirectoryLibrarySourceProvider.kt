package org.pathcheck.intellij.cql

import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import org.cqframework.cql.cql2elm.LibrarySourceProvider
import org.cqframework.cql.cql2elm.model.Version
import org.hl7.elm.r1.VersionedIdentifier
import java.io.*

// NOTE: This implementation is naive and assumes library file names will always take the form:
// <filename>[-<version>].cql
// And further that <filename> will never contain dashes, and that <version> will always be of the form <major>[.<minor>[.<patch>]]
// Usage outside these boundaries will result in errors or incorrect behavior.
class PsiDirectoryLibrarySourceProvider(private val path: PsiDirectory) : LibrarySourceProvider {

    // Returns the version between - and .
    // <filename>[-<version>].cql
    private fun parseVersionNumber(fileName: String): Version {
        return Version(fileName.substring(fileName.indexOf("-")+1, fileName.lastIndexOf(".")))
    }

    fun getLibrarySourceFile(libraryIdentifier: VersionedIdentifier): PsiFile? {
        val libraryName = libraryIdentifier.id
        val libraryVersion = libraryIdentifier.version

        // look for <filename>-<version>.cql
        val psiFile = path.findFile(listOfNotNull(libraryName, libraryVersion).joinToString("-")+".cql")
        if (psiFile != null) {
            return psiFile
        }

        // If the file is named correctly, but has no version, consider it the most recent version
        // look for <filename>.cql
        val noVersion = path.findFile("$libraryName.cql")
        if (noVersion != null) {
            return noVersion
        }

        val requestedVersion = if (libraryVersion != null) Version(libraryVersion) else null

        // If the file has a version, make sure it is compatible with the version we are looking for
        val newestCompatibleVersion = path.files
            .filter { it.name.startsWith(libraryName) && it.name.endsWith(".cql") }
            .filter { requestedVersion == null || parseVersionNumber(it.name).compatibleWith(requestedVersion) }
            .maxByOrNull { parseVersionNumber(it.name) }


        if (newestCompatibleVersion != null) {
            return newestCompatibleVersion
        }

        return null
    }

    override fun getLibrarySource(libraryIdentifier: VersionedIdentifier): InputStream? {
        return getLibrarySourceFile(libraryIdentifier)?.text?.byteInputStream()
    }
}