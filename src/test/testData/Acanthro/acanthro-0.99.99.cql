
library acanthro version '0.99.99'
using FHIR version '4.0.1'
include anthrobase version '0.99.99' called base
include FHIRHelpers version '4.0.1' called FHIRHelpers
// Antrho library for acanthro Z-Score
codesystem "administrative-gender": 'http://hl7.org/fhir/administrative-gender'
//code "Female" : 'female' from "administrative-gender"  display 'Female'


define ArmCircumferenceFemale:
{
	{y:91.0,l:-0.1733,s:0.08262,m:13.0245},
	{y:92.0,l:-0.1733,s:0.08264,m:13.0371},
	{y:93.0,l:-0.1733,s:0.08265,m:13.0496},
	{y:94.0,l:-0.1733,s:0.08266,m:13.062},
	{y:1855.0,l:-0.1733,s:0.09378,m:16.8988},
	{y:1856.0,l:-0.1733,s:0.09379,m:16.9004}
}

define ArmCircumferenceMale:
{
	{y:91.0,l:0.3933,s:0.07474,m:13.4779},
	{y:92.0,l:0.3916,s:0.07476,m:13.49},
	{y:93.0,l:0.39,s:0.07478,m:13.502},
	{y:1853.0,l:-0.4043,s:0.0865,m:16.5474},
	{y:1854.0,l:-0.4046,s:0.08651,m:16.5484},
	{y:1855.0,l:-0.4048,s:0.08651,m:16.5495},
	{y:1856.0,l:-0.405,s:0.08652,m:16.5506}
}

define function ZscoreArmCircumferencetables(sex String, Age Decimal):
		if sex = 'female' then  First(ArmCircumferenceFemale c where c.y = Age )
		else First(ArmCircumferenceMale c where  c.y = Age)

define function generateZScoreArmCircumference(sex String, Age Decimal, weight  Decimal)  :
	base.computeZScore(
		weight,
		(ZscoreArmCircumferencetables(sex,Age).m ),
		(ZscoreArmCircumferencetables(sex,Age).l ),
		(ZscoreArmCircumferencetables(sex,Age).s )
	)

define function generateArmCircumferenceFromAge(sex String, Age Decimal, zscore  Decimal) :
	base.computeReverseZScore(
		zscore,
		(ZscoreArmCircumferencetables(sex,Age).m ),
		(ZscoreArmCircumferencetables(sex,Age).l ),
		(ZscoreArmCircumferencetables(sex,Age).s )
	)

        /*
define function ZscoreArmCircumferencetables_s(sex String, Age Decimal)  :
    if sex = 'female' then
        First(ArmCircumferenceFemale c where c.y = Age return (c.s as Decimal))
    else
        First(ArmCircumferenceMale c where c.y = Age return (c.s as Decimal))


define function ZscoreArmCircumferencetables_l(sex String, Age Decimal)  :
    if sex = 'female' then
        First(ArmCircumferenceFemale c where c.y = Age return (c.l as Decimal))
    else
        First(ArmCircumferenceMale c where c.y = Age return (c.l as Decimal))


define function ZscoreArmCircumferencetables_m(sex String, Age Decimal)  :
    if sex = 'female' then
        First(ArmCircumferenceFemale c where c.y = Age return (c.m as Decimal))
    else
        First(ArmCircumferenceMale c where c.y = Age return (c.m as Decimal))




define function generateZScoreArmCircumference(sex String, Age Decimal, weight  Decimal)  :
	base.computeZScore(
		weight,
		(ZscoreArmCircumferencetables_m(sex,Age) ),
		(ZscoreArmCircumferencetables_l(sex,Age) ),
		(ZscoreArmCircumferencetables_s(sex,Age) )
	)

define function generateArmCircumferenceFromAge(sex String, Age decimal, zscore  decimal) :
	base.computeReverseZScore(
		zscore,
		(ZscoreArmCircumferencetables_m(sex,Age) ),
		(ZscoreArmCircumferencetables_l(sex,Age) ),
		(ZscoreArmCircumferencetables_s(sex,Age) )
	)

    */

