package org.pathcheck.intellij.cql.psi

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReference
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.findParentInFile
import com.intellij.util.IncorrectOperationException
import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode
import org.antlr.intellij.adaptor.psi.Trees
import org.pathcheck.intellij.cql.CqlLanguage
import org.pathcheck.intellij.cql.psi.definitions.QualifiedIdentifier
import org.pathcheck.intellij.cql.psi.expressions.Invocation
import org.pathcheck.intellij.cql.psi.expressions.InvocationTerm
import org.pathcheck.intellij.cql.psi.references.CqlReference
import org.pathcheck.intellij.cql.psi.references.ReferentialIdentifier
import org.pathcheck.intellij.cql.psi.scopes.QualifiedIdentifierExpression
import org.pathcheck.intellij.cql.psi.scopes.QualifiedInvocation

/** From doc: "Every element which can be renamed or referenced
 * needs to implement com.intellij.psi.PsiNamedElement interface."
 *
 * So, all leaf nodes that represent variables, functions, classes, or
 * whatever in your plugin language must be instances of this not just
 * LeafPsiElement.  Your ASTFactory should create this kind of object for
 * ID tokens. This node is for references *and* definitions because you can
 * highlight a function and say "jump to definition". Also we want defs
 * to be included in "find usages." Besides, there is no context information
 * in the AST factory with which you could decide whether this node
 * is a definition or a reference.
 *
 * PsiNameIdentifierOwner (vs PsiNamedElement) implementations are the
 * corresponding subtree roots that define symbols.
 *
 * You can click on an ID in the editor and ask for a rename for any node
 * of this type.
 */
class IdentifierPSINode(type: IElementType?, text: CharSequence?) : ANTLRPsiLeafNode(type, text), PsiNamedElement {
    override fun getName(): String {
        return text
    }

    /** Alter this node to have text specified by the argument. Do this by
     * creating a new node through parsing of an ID and then doing a
     * replace.
     */
    @Throws(IncorrectOperationException::class)
    override fun setName(name: String): PsiElement {
        val newID = Trees.createLeafFromText(
            project,
            CqlLanguage,
            context,
            name,
            elementType
        )

        return if (newID != null) {
            this.replace(newID) // use replace on leaves but replaceChild on ID nodes that are part of defs/decls.
        } else this
    }

    /**
     * Create and return a PsiReference object associated with this ID
     * node. The reference object will be asked to resolve this ref
     * by using the text of this node to identify the appropriate definition
     * site. The definition site is typically a subtree for a function
     * or variable definition whereas this reference is just to this ID
     * leaf node.
     *
     * As the AST factory has no context and cannot create different kinds
     * of PsiNamedElement nodes according to context, every ID node
     * in the tree will be of this type. So, we distinguish references
     * from definitions or other uses by looking at context in this method
     * as we have parent (context) information.
     */
    override fun getReference(): PsiReference? {
        findParentInFile {
                     it is Invocation
                  || it is QualifiedInvocation
                  || it is QualifiedIdentifierExpression // QuerySource from variable
                  || it is QualifiedIdentifier // LibraryDef, IncludeDef, UsingDef
                  || it is InvocationTerm
                  || it is ReferentialIdentifier
        }?.let {
            return CqlReference(this)
        }

        return null
    }
}