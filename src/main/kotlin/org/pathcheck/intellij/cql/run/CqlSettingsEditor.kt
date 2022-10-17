package org.pathcheck.intellij.cql.run

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.TextFieldWithAutoCompletion
import com.intellij.ui.TextFieldWithAutoCompletion.StringsCompletionProvider
import java.awt.GridLayout
import java.util.*
import javax.swing.JComponent
import javax.swing.JPanel

class CqlSettingsEditor(val myProject: Project) : SettingsEditor<CqlRunConfiguration>() {
    private val myPanel: JPanel = JPanel(GridLayout(2, 1, 5, 5));
    private lateinit var myDataFileName: LabeledComponent<TextFieldWithBrowseButton>
    private lateinit var myExpressionName: LabeledComponent<TextFieldWithAutoCompletion<String>>

    private fun createUIComponents() {
        myExpressionName = LabeledComponent()
        myExpressionName.label.text = "Expression Name"
        myExpressionName.component = TextFieldWithAutoCompletion<String>(
            myProject,
            StringsCompletionProvider(listOf("Test1", "Test2"), null),
            true,
            null
        )

        myDataFileName = LabeledComponent()
        myDataFileName.label.text = "Data File (Json of FHIR Objects in a Bundle)"
        myDataFileName.component = TextFieldWithBrowseButton()
        myDataFileName.component.addBrowseFolderListener("Select data file", "This directory must contain a .json file. ",
            null, FileChooserDescriptor(true, false, false, false, false, false)
        )
    }

    override fun resetEditorFrom(cqlRunConfiguration: CqlRunConfiguration) {
        myExpressionName.component.text = cqlRunConfiguration.getExpressionName()
        myDataFileName.component.text = cqlRunConfiguration.getDataFileName()
    }

    override fun applyEditorTo(CqlRunConfiguration: CqlRunConfiguration) {
        CqlRunConfiguration.setExpressionName(myExpressionName.component.text)
        CqlRunConfiguration.setDataFileName(myDataFileName.component.text)
    }

    override fun createEditor(): JComponent {
        createUIComponents()
        myPanel.add(myExpressionName)
        myPanel.add(myDataFileName)
        return myPanel
    }

}