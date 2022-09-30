grammar targetmap;

/*
Target mapping language to support mapping expressions for primitives, choices, extensions, and slicing:

// Primitives
%value.value
QICoreHelpers.ToConcept(%value)

// TODO: Consider alignment with FHIRPath here, $value could be $this?
// The challenge is that %value is more of a replacement token, and are some FHIR maps that will already have $this (slicing maps in particular often include $this)

// Choices
System.DateTime:%value.value;;QICore.Timing:null
QICore.Reference:null;System.Concept:QICoreHelpers.ToConcept(%value)

// Extensions
QICoreHelpers.ToConcept(%value.extension[url=http://hl7.org/fhir/us/qicore/StructureDefinition/qicore-doNotPerformReason].value)
%value.modifierExtension[url=http://hl7.org/fhir/StructureDefinition/request-doNotPerform].value.value

// TODO: Consider alignment with FHIRPath here? (i.e. use .extension(), rather than this syntax)


// Slicing
QICoreHelpers.ToConcept(%value.category[coding.system=http://terminology.hl7.org/CodeSystem/observation-category,coding.code=vital-signs].value)
QICoreHelpers.ToCode(%value.code.coding[system=http://loinc.org,code=39156-5].value)
%value.component[code.coding.system=http://loinc.org,code.coding.code=8480-6]
%value.component[code.coding.system=http://loinc.org,code.coding.code=8462-4]

// TODO: Consider alignment with FHIRPath here? (i.e. use .where(), rather than this syntax)

// TODO: Consider a use case for coalescing? E.g. %value.foo ?? %value.bar ?? %value.baz (using typescript-inspired syntax; is there a FHIRPath variant?). Maybe that's going too far? I mean, I guess we don't want to redefine all of FHIRPath, right?
*/

targetMapping
  : targetMap EOF
  ;

targetMap
  : mappingTarget
  | targetInvocation
  | targetMap '.' targetInvocation
  | targetMap '[' targetIndex ']'
  | choiceMap
  ;

choiceMap
  : choiceMapItem (';' choiceMapItem)*
  ;

choiceMapItem
  : (qualifiedIdentifier ':' targetMap)?
  ;

targetIndex
  : targetIndexItem (',' targetIndexItem)*
  ;

targetIndexItem
  : identifier '=' STRING
  ;

// TODO: Consider use cases for numeric indexes (e.g. component[0] (first item) component[-1] (last item)

mappingTarget
  : '%' identifier
  | 'null'
  ;

targetInvocation
  : identifier
  | function
  ;

qualifiedIdentifier
  : identifier ('.' identifier)*
  ;

identifier
  : IDENTIFIER
  | DELIMITEDIDENTIFIER
  ;

function
  : identifier '(' targetMap ')'
  ;

IDENTIFIER
        : ([A-Za-z] | '_')([A-Za-z0-9] | '_')*            // Added _ to support CQL (FHIR could constrain it out)
        ;

DELIMITEDIDENTIFIER
        : '`' (ESC | .)*? '`'
        ;

STRING
  : '\'' (ESC | .)*? '\''
  ;

NUMBER
  : [0-9]+('.' [0-9]+)?
  ;

// Pipe whitespace to the HIDDEN channel to support retrieving source text through the parser.
WS
  : [ \r\n\t]+ -> channel(HIDDEN)
  ;

fragment ESC
  : '\\' ([`'\\/fnrt] | UNICODE)    // allow \`, \', \\, \/, \f, etc. and \uXXX
  ;

fragment UNICODE
  : 'u' HEX HEX HEX HEX
  ;

fragment HEX
  : [0-9a-fA-F]
  ;
