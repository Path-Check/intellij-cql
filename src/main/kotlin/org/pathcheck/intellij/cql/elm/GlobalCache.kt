package org.pathcheck.intellij.cql.elm

import com.intellij.psi.PsiFile
import org.cqframework.cql.cql2elm.*
import org.cqframework.cql.cql2elm.model.CompiledLibrary
import org.cqframework.cql.cql2elm.model.Model
import org.hl7.cql.model.ModelIdentifier
import org.hl7.elm.r1.Library
import org.hl7.elm.r1.VersionedIdentifier

/**
 * Keeps a global cache of external models and libraries
 */
object GlobalCache {
    private lateinit var modelManager: ModelManager
    private lateinit var libraryManager: LibraryManager

    init {
        runSafeClassLoader {
            modelManager = ModelManager()
            libraryManager = LibraryManager(modelManager)
        }
    }

    fun <T> runSafeClassLoader(runnable: () -> T): T {
        /*
            Some libraries use ServiceLoader to detect and load implementations. For this to work in a plugin,
            the context class loader must be set to the plugin's classloader and restored afterwards with the
            original one around initialization code:
         */
        val currentThread = Thread.currentThread()
        val originalClassLoader = currentThread.contextClassLoader
        val pluginClassLoader = this.javaClass.classLoader
        return try {
            currentThread.contextClassLoader = pluginClassLoader

            runnable()
        } finally {
            currentThread.contextClassLoader = originalClassLoader
        }
    }

    fun resolveModel(modelName: String): Model? {
        return runSafeClassLoader {
            modelManager.resolveModel(modelName)
        }
    }

    fun resolveModel(modelName: ModelIdentifier): Model? {
        return runSafeClassLoader {
            modelManager.resolveModel(modelName)
        }
    }

    fun resolveLibrary(libraryVersion: VersionedIdentifier): Library? {
        return runSafeClassLoader {
            val errors = mutableListOf<CqlCompilerException>()
            libraryManager.resolveLibrary(libraryVersion, CqlTranslatorOptions(), errors).library
        }
    }

    fun compileLibrary(file: PsiFile): CqlCompiler {
        return runSafeClassLoader {
            // ModelManager and Library Manager must be created in this context to make sure ServiceLoaders are ready
            val libraryManager = AdaptedLibraryManager().apply {
                librarySourceLoader.registerProvider(PsiDirectoryLibrarySourceProvider(file.originalFile.containingDirectory))
            }

            CqlCompiler(modelManager, libraryManager).apply {
                run(file.text)
            }
        }
    }

    /**
     * Custom LibraryManager with inner LibraryManager to cache external libs only
     */
    class AdaptedLibraryManager: LibraryManager(modelManager) {
        override fun resolveLibrary(
            libraryIdentifier: VersionedIdentifier,
            options: CqlTranslatorOptions?,
            errors: MutableList<CqlCompilerException>?
        ): CompiledLibrary {

            try {
                // if this is an external lib, it might be cached.
                // The Global instance doesn't have a PsiDirectoryLibrarySourceProvider and thus
                // can't find local CQLs
                return libraryManager.resolveLibrary(libraryIdentifier, options, errors)
            } catch (e: Exception) {
                // does not trigger exceptions.
            }

            return super.resolveLibrary(libraryIdentifier, options, errors)
        }
    }
}
