package org.pathcheck.intellij.cql

import org.cqframework.cql.cql2elm.CqlCompilerException
import org.cqframework.cql.cql2elm.CqlTranslatorOptions
import org.cqframework.cql.cql2elm.LibraryManager
import org.cqframework.cql.cql2elm.model.CompiledLibrary
import org.hl7.elm.r1.VersionedIdentifier

/**
 * Custom LibraryManager with inner LibraryManager to cache external libs only
 */
class AdaptedLibraryManager: LibraryManager(GlobalCache.modelManager) {
    override fun resolveLibrary(
        libraryIdentifier: VersionedIdentifier,
        options: CqlTranslatorOptions?,
        errors: MutableList<CqlCompilerException>?
    ): CompiledLibrary {

        try {
            // if this is an external lib, it might be cached.
            return GlobalCache.libraryManager.resolveLibrary(libraryIdentifier, options, errors)
        } catch (e: Exception) {
            // does not trigger exceptions.
        }

        return super.resolveLibrary(libraryIdentifier, options, errors)
    }
}