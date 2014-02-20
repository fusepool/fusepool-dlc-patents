/*.
* Copyright 2013 Fusepool Project.
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package eu.fusepool.rdfizer.marec;

import java.io.InputStream;
import java.security.AccessController;
import java.security.AllPermission;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.impl.SimpleMGraph;
import org.apache.clerezza.rdf.core.serializedform.Parser;
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat;
import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.stanbol.commons.indexedgraph.IndexedMGraph;
import org.osgi.framework.Constants;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.fusepool.datalifecycle.Rdfizer;
import eu.fusepool.rdfizer.marec.xslt.CatalogBuilder;
import eu.fusepool.rdfizer.marec.xslt.XMLProcessor;
import eu.fusepool.rdfizer.marec.xslt.impl.PatentXSLTProcessor;


@Component(immediate = true, metatype = true, policy = ConfigurationPolicy.OPTIONAL)
@Service(Rdfizer.class)
@Properties(value = {
        @Property(name = Constants.SERVICE_RANKING, 
                intValue = MarecRdfizer.DEFAULT_SERVICE_RANKING)
})
public class MarecRdfizer implements Rdfizer {
    
    /**
     * Default value for the {@link Constants#SERVICE_RANKING} used by this engine.
     * This is a negative value to allow easy replacement by this engine depending
     * to a remote service with one that does not have this requirement
     */
    public static final int DEFAULT_SERVICE_RANKING = 101;
    
    public final String RDFIZER_TYPE_LABEL = "rdfizer";
    
    public final String RDFIZER_TYPE_VALUE = "marec";
    
    final Logger log = LoggerFactory.getLogger(this.getClass()) ;
    
    @Reference
    protected Parser parser;
    
    protected CatalogBuilder catalogBuilder ;
    
    XMLProcessor processor = null;
    

    public MGraph transform(InputStream stream) {
    	
    	AccessController.checkPermission(new AllPermission());
        
        MGraph xml2rdf = new SimpleMGraph();
        
        XMLProcessor processor = new PatentXSLTProcessor() ;
        
        InputStream rdfIs = null;
    
        log.debug("Starting transformation from XML to RDF");
        
        try {
            
        	rdfIs = processor.processXML( stream ) ;
        	
        	if(rdfIs != null) {
        		parser.parse(xml2rdf, rdfIs, SupportedFormat.RDF_XML) ;
        	}
            
        	rdfIs.close();
        	
            log.info("Finished transformation from XML to RDF");
            
            
        } catch (Exception e) {
            log.error("Error while transforming XML data into RDF.", e) ;
        }
        
        
        return xml2rdf;
    }

    public String getName() {
        return this.RDFIZER_TYPE_VALUE;
    }
    
    @Activate
    protected void activate(ComponentContext context) {
        log.info("Marec XML Rdfize service is being activated");
        
        // Build the catalog of DTDs files
        catalogBuilder = new CatalogBuilder(context.getBundleContext()) ;
        try {
            catalogBuilder.build() ;
        } catch (Exception e) {
            log.error("Error building DTDs catalog", e) ;
        }

    }

    @Deactivate
    protected void deactivate(ComponentContext context) {
        log.info("Marec XML Rdfize service is being deactivated");
        catalogBuilder.cleanupFiles();
    }


}
