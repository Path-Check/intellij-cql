package org.pathcheck.intellij.cql.elm

import org.cqframework.cql.cql2elm.LibraryManager
import org.cqframework.cql.cql2elm.ModelManager

/**
 * Keeps a global cache of external models and libraries
 */
object GlobalCache {
    val modelManager: ModelManager
    val libraryManager: LibraryManager

    init {
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

            modelManager = ModelManager()
            libraryManager = LibraryManager(modelManager)
        } finally {
            currentThread.contextClassLoader = originalClassLoader
        }
    }
}