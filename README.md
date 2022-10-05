# Clinical Quality Language Plugin for IntelliJ Idea and Android Studio

![Build](https://github.com/Path-Check/intellij-cql/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/20063.svg)](https://plugins.jetbrains.com/plugin/20063)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/20063.svg)](https://plugins.jetbrains.com/plugin/20063)

<!-- Plugin description -->
Syntax highlighting, semantic (error) highlighting, code completion, reference finders, and local execution for the HL7 Clinical Quality Language (CQL). 

## More About the Clinical Quality Language

The Clinical Quality Language (CQL) is a domain specific language for expressing
electronic clinical quality measures (eCQM) and clinical decision support rules
(CDS) in an author-friendly computable format. Find out more about CQL:

* [CQL Specification](https://cql.hl7.org)
* [CQL Stream on FHIR Zulip Chat](https://chat.fhir.org/#narrow/stream/179220-cql)
* [clinical_quality_language on GitHub](https://github.com/cqframework/clinical_quality_language)
* [Clinical Quality Expression Language at HL7](https://www.hl7.org/special/Committees/projman/searchableProjectIndex.cfm?action=view&ProjectNumber=1108)
* [Clinical Quality Framework (CQF)](https://confluence.hl7.org/display/CQIWC/Clinical+Quality+Framework)

## Getting Help

Bugs and feature requests can be filed with [Github Issues](https://github.com/Path-Check/intellij-cql/issues).

The implementers are active on the official FHIR [Zulip chat for CQL](https://chat.fhir.org/#narrow/stream/179220-cql).

## Related Projects

* [cql-translator](https://github.com/cqframework/clinical_quality_language/tree/master/Src/java/cql-to-elm) - The ELM generation component used in this project.
* [cql-engine](https://github.com/DBCG/cql_engine) - The Java CQL runtime environment used in the extension.
* [cql-evaluator](https://github.com/DBCG/cql-evaluator) - The Java CQL evaluator used in the extension.

## License

Copyright 2022+ PathCheck Foundation

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

<https://www.apache.org/licenses/LICENSE-2.0>

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "intellij-cql"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/Path-Check/intellij-cql/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
