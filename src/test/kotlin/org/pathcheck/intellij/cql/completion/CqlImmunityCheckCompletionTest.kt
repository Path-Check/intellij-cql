package org.pathcheck.intellij.cql.completion

class CqlImmunityCheckCompletionTest: BaseCompletionTestCase() {
    fun testCompletionFromModel() {
        assertCompletionHas( 8, 13, "Patient", "Patient.Communication",
            "Patient.Contact", "Patient.Link", "PractitionerRole.AvailableTime",
            "MedicationKnowledge.AdministrationGuidelines.PatientCharacteristics", "ParticipationStatus")
    }

    fun testCompletionAliasIss() {
        assertCompletionHas( 17, 14, "Iss", "IssueSeverity", "IssueType",
            "ImagingStudyStatus", "ImagingStudy.Series", "ImagingStudy.Series.Instance", "ImagingStudy.Series.Performer",
            "DetectedIssue", "DetectedIssue.Evidence", "DetectedIssue.Mitigation", "DetectedIssueSeverity",
            "DetectedIssueStatus", "OperationOutcome.Issue", "MedicinalProductIngredient.SpecifiedSubstance",
            "MedicinalProductIngredient.SpecifiedSubstance.Strength",
            "MedicinalProductIngredient.SpecifiedSubstance.Strength.ReferenceStrength")
    }

    fun testCompletionAttributeFromModel() {
        assertCompletionHas( 16, 26, "protocolApplied")
    }

    fun testCompletionAttributeFromLocalLib() {
        assertCompletionHas( 17, 61, "myDoseLimit", "myDoseLimitMl")
    }

    override fun loadFiles(): List<String> {
        return listOf("ImmunityCheck-1.0.0.cql", "Commons-1.0.0.cql", "Commons-1.0.2.cql")
    }

    override fun testDirectory(): String {
        return "src/test/testData/ImmunityCheck"
    }
}