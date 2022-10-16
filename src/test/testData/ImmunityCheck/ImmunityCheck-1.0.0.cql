library ImmunityCheck version '1.0.0'

using FHIR version '4.0.1'

include Commons version '1.0.0'
include "FHIRHelpers" version '4.0.1' called FHIRHelpers

context Patient

define "CompletedImmunization":
  exists(GetFinalDose) or exists("GetSingleDose")

// Test of Comment
define GetFinalDose:
  [Immunization] Iss
    where exists(Iss.protocolApplied)
      and Iss.protocolApplied.seriesDoses.value = Commons.myDoseLimit(0.2)

define "GetSingleDose":
  [Immunization] I
    where exists(I.protocolApplied)
      and exists(I.protocolApplied.doseNumber.value)
      and not exists(I.protocolApplied.seriesDoses.value)
