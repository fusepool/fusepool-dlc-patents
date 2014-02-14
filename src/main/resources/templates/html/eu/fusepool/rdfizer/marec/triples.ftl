<#--
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
-->
<@namespace dct="http://purl.org/dc/terms/" />
<@namespace fise="http://fise.iks-project.eu/ontology/" />
<@namespace dcterms="http://purl.org/dc/terms/" />
<@namespace rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#" />
<@namespace rdfs="http://www.w3.org/2000/01/rdf-schema#" />

<html>
  <head>
    <title>Fusepool - Marec XML Documents Rdfizer</title>
    <link type="text/css" rel="stylesheet" href="styles/multi-enhancer.css" />
  </head>

  <body>
    <h1>Rdfize a Marec XML document</h1>
    The uploaded file's URI is: <@ldpath path="."/><br/>
    Transformation result: <@ldpath path="rdfs:comment"/><br/>
    <@ldpath path="^fise:extracted-from">
        <p>Annotation: <@ldpath path="."/><br/>
        <!-- unfortunately it doesn't seem to be possible to show all the properties and there values -->
        Created by: <@ldpath path="dct:creator"/><br/>
        <@ldpath path="rdf:type">
          Of type: <@ldpath path="."/><br/>
        </@ldpath>
        </p>
    </@ldpath>
    <#include "/html/includes/footer.ftl">
  </body>
</html>

