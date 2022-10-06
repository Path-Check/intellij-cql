package org.pathcheck.intellij.cql.psi.references

import com.intellij.openapi.util.TextRange
import org.pathcheck.intellij.cql.psi.IdentifierPSINode

/** A reference object associated with (referring to) a IdentifierPSINode
 * underneath a call_expr rule subtree root.
 */
class FunctionRef(element: IdentifierPSINode) : CqlElementRef(element, TextRange(0, element.text.length)) {
}