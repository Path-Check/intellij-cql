package org.pathcheck.intellij.cql.folding

import com.intellij.codeInsight.folding.CodeFoldingManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class CqlFoldingBuilderTest : BasePlatformTestCase() {
    fun testShouldFoldThreeLiner() {
        // Given
        myFixture.configureByText("foo.cql","""
            define Factorial:
                Seq Seq
                    aggregate R starting 1: R * Seq
            """)

        // When
        CodeFoldingManager.getInstance(project).buildInitialFoldings(myFixture.editor)

        // Then
        assertEquals(1, myFixture.editor.foldingModel.allFoldRegions.size)
    }
    
    fun testShouldFoldTwoLiner() {
        // Given
        myFixture.configureByText("foo.cql","""
            define Seq: 
              {1, 2, 3, 4, 5}
            """)

        // When
        CodeFoldingManager.getInstance(project).buildInitialFoldings(myFixture.editor)

        // Then
        assertEquals(1, myFixture.editor.foldingModel.allFoldRegions.size)
    }


    fun testShouldFoldOneLiner() {
        // Given
        myFixture.configureByText("foo.cql","""
            define One: 1
            """)

        // When
        CodeFoldingManager.getInstance(project).buildInitialFoldings(myFixture.editor)

        // Then
        assertEquals(1, myFixture.editor.foldingModel.allFoldRegions.size)
    }

    fun testShouldNotFoldEmpty() {
        // Given
        myFixture.configureByText("foo.cql","""
            define One: 
            """)

        // When
        CodeFoldingManager.getInstance(project).buildInitialFoldings(myFixture.editor)

        // Then
        assertEquals(0, myFixture.editor.foldingModel.allFoldRegions.size)
    }

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        System.setProperty("idea.log.debug.categories", "#org.pathcheck.intellij.cql");
    }
}