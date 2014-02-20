/*
* Licensed to the Apache Software Foundation (ASF) under one or more
* contributor license agreements.  See the NOTICE file distributed with
* this work for additional information regarding copyright ownership.
* The ASF licenses this file to You under the Apache License, Version 2.0
* (the "License"); you may not use this file except in compliance with
* the License.  You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package eu.fusepool.rdfizer.marec;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.clerezza.jaxrs.utils.TrailingSlash;
import org.apache.clerezza.jaxrs.utils.form.FormFile;
import org.apache.clerezza.jaxrs.utils.form.MultiPartBody;
import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.clerezza.rdf.core.impl.PlainLiteralImpl;
import org.apache.clerezza.rdf.core.serializedform.Parser;
import org.apache.clerezza.rdf.ontologies.RDF;
import org.apache.clerezza.rdf.ontologies.RDFS;
import org.apache.clerezza.rdf.utils.GraphNode;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.stanbol.commons.indexedgraph.IndexedMGraph;
import org.apache.stanbol.commons.web.viewable.RdfViewable;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.fusepool.datalifecycle.Rdfizer;
import org.apache.commons.io.IOUtils;



/**
 * Upload XML file to be transformed into RDF 
 */
@Component
@Service(Object.class)
@Property(name="javax.ws.rs", boolValue=true)
@Path("patentrdfizer")
public class XmlUploader {
    
    /**
     * Using slf4j for logging
     */
    private static final Logger log = LoggerFactory.getLogger(XmlUploader.class);
    
    /**
     * This service allows accessing and creating persistent triple collections
     */
    
    @Reference
    private Parser parser;

    @Reference
    Rdfizer rdfizer;
    
    @Activate
    protected void activate(ComponentContext context) {
        log.info("The XML Uploader service is being activated");
     }
    
    @Deactivate
    protected void deactivate(ComponentContext context) {
        log.info("The XML Uploader service is being activated");
    }
    
    /**
     * This method return an RdfViewable, this is an RDF serviceUri with associated
     * presentational information.
     */
    @GET
    public RdfViewable serviceEntry(@Context final UriInfo uriInfo, 
            @HeaderParam("user-agent") String userAgent) throws Exception {
        //this maks sure we are nt invoked with a trailing slash which would affect
        //relative resolution of links (e.g. css)
        TrailingSlash.enforcePresent(uriInfo);
        final String resourcePath = uriInfo.getAbsolutePath().toString();
        //The URI at which this service was accessed accessed, this will be the 
        //central serviceUri in the response
        final UriRef serviceUri = new UriRef(resourcePath);
        //the in memory graph to which the triples for the response are added
        final MGraph responseGraph = new IndexedMGraph();
        //This GraphNode represents the service within our result graph
        final GraphNode node = new GraphNode(serviceUri, responseGraph);
        //The triples will be added to the first graph of the union
        //i.e. to the in-memory responseGraph
        node.addProperty(RDF.type, Ontology.MultiEnhancer);
        node.addProperty(RDFS.comment, new PlainLiteralImpl("An XML2RDF transformation service"));
        //What we return is the GraphNode we created with a template path
        return new RdfViewable("XmlUploader", node, XmlUploader.class);
    }
    
    /**
     * This service returns an RdfVieable describing the enhanced document. 
     * @throws EnhancementException 
     */
    @SuppressWarnings("deprecation")
	@POST
    public RdfViewable transformXmlDocument(MultiPartBody body) throws IOException {
        final String [] mimeTypeValues = body.getTextParameterValues("mime_type");
        final String mimeType = mimeTypeValues.length > 0 ? mimeTypeValues[0] : null;
        final FormFile file = body.getFormFileParameterValues("file")[0];
        
        int numTriples = 0;
        
        // Transform the XML data into RDF
        MGraph graph = rdfizer.transform(new ByteArrayInputStream(file.getContent()));
        
        numTriples = graph.size();
        
        log.info("Extracted " + graph.size() + " triples from " + file.getFileName());
        
        
        //this represent the submitted Content within the resultGraph
        final GraphNode node = new GraphNode(graph.iterator().next().getSubject(), graph);
        node.addProperty(RDFS.comment, new PlainLiteralImpl(numTriples + " triples have been extracted from " + file.getFileName()));
        //node is the "root" for rendering the results 
        return new RdfViewable("triples", node, XmlUploader.class);
    }
    
}
