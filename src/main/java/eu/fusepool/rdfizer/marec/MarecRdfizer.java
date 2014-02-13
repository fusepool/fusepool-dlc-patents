package eu.fusepool.rdfizer.marec;

import java.io.InputStream;

import org.apache.clerezza.rdf.core.MGraph;
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
        
        MGraph xml2rdf = null;
        
        XMLProcessor processor = new PatentXSLTProcessor() ;
        InputStream rdfIs = null ; 
    
        log.debug("Starting transformation from XML to RDF");
        
        try {
            
            xml2rdf = new IndexedMGraph();
            rdfIs = processor.processXML( stream ) ;
            parser.parse(xml2rdf, rdfIs, SupportedFormat.RDF_XML) ;
            rdfIs.close() ;
            
            
        } catch (Exception e) {
            log.error("Error while processing the XML data.", e) ;
        }
        
        log.info("Finished transformation from XML to RDF");
        
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
