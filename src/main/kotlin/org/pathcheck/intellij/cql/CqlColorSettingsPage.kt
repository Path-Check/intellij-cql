package org.pathcheck.intellij.cql

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import org.pathcheck.intellij.cql.CqlSyntaxHighlighter.Companion.BLOCK_COMMENT
import org.pathcheck.intellij.cql.CqlSyntaxHighlighter.Companion.BRACES
import org.pathcheck.intellij.cql.CqlSyntaxHighlighter.Companion.BRACKETS
import org.pathcheck.intellij.cql.CqlSyntaxHighlighter.Companion.COMMA
import org.pathcheck.intellij.cql.CqlSyntaxHighlighter.Companion.DOT
import org.pathcheck.intellij.cql.CqlSyntaxHighlighter.Companion.FUNCTION_CALL
import org.pathcheck.intellij.cql.CqlSyntaxHighlighter.Companion.FUNCTION_DECLARATION
import org.pathcheck.intellij.cql.CqlSyntaxHighlighter.Companion.ID
import org.pathcheck.intellij.cql.CqlSyntaxHighlighter.Companion.KEYWORD
import org.pathcheck.intellij.cql.CqlSyntaxHighlighter.Companion.LINE_COMMENT
import org.pathcheck.intellij.cql.CqlSyntaxHighlighter.Companion.OPERATION_SIGN
import org.pathcheck.intellij.cql.CqlSyntaxHighlighter.Companion.PARENTHESES
import org.pathcheck.intellij.cql.CqlSyntaxHighlighter.Companion.STRING
import org.pathcheck.intellij.cql.CqlSyntaxHighlighter.Companion.VALUE
import javax.swing.Icon

class CqlColorSettingsPage : ColorSettingsPage {
    override fun getIcon(): Icon? {
        return CqlIcons.FILE
    }

    override fun getHighlighter(): SyntaxHighlighter {
        return CqlSyntaxHighlighter()
    }

    override fun getDemoText(): String {
        return """library ImmunityCheck version '1.0.0'

using FHIR version '4.0.1'

include "FHIRHelpers" version '4.0.1' called FHIRHelpers

context Patient

define "CompletedImmunization":
  exists(GetFinalDose) or exists(GetSingleDose)

define "Test": 5+3

define Date: @2012-05-03

// Test of Comment
define "GetFinalDose":
  [Immunization] I
    where exists(I.protocolApplied)
      and I.protocolApplied.doseNumber.value = '2'

define "GetSingleDose":
  [Immunization] I
    where exists(I.protocolApplied)
      and exists(I.protocolApplied.doseNumber.value)
      and not exists(I.protocolApplied.seriesDoses.value)
      and "GetFinalDose"
"""
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? {
        return null
    }

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return DESCRIPTORS
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String {
        return "Cql"
    }

    companion object {
        private val DESCRIPTORS = arrayOf(
            AttributesDescriptor("Identifier", ID),
            AttributesDescriptor("Keyword", KEYWORD),
            AttributesDescriptor("String", STRING),
            AttributesDescriptor("Comment", LINE_COMMENT),
            AttributesDescriptor("Block Comment", BLOCK_COMMENT),
            AttributesDescriptor("Value", VALUE),
            AttributesDescriptor("Dot", DOT),
            AttributesDescriptor("Braces", BRACES),
            AttributesDescriptor("Brackets", BRACKETS),
            AttributesDescriptor("Comma", COMMA),
            AttributesDescriptor("Operation Sign", OPERATION_SIGN),
            AttributesDescriptor("Parentheses", PARENTHESES),
            AttributesDescriptor("Function Call", FUNCTION_CALL),
            AttributesDescriptor("Function Declaration", FUNCTION_DECLARATION)
        )
    }
}