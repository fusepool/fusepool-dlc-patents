package eu.fusepool.rdfizer.marec;

import java.io.File;
import java.io.InputStream;

import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.impl.SimpleMGraph;
import org.apache.clerezza.rdf.core.serializedform.Parser;
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat;
import org.apache.clerezza.rdf.jena.parser.JenaParserProvider;
import org.apache.commons.io.IOUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.ConfigurationPolicy;
import org.junit.Assert;
import org.junit.Test;

import eu.fusepool.rdfizer.marec.xslt.CatalogBuilder;
import eu.fusepool.rdfizer.marec.xslt.FileCatalogBuilder;
import eu.fusepool.rdfizer.marec.xslt.XMLProcessor;
import eu.fusepool.rdfizer.marec.xslt.impl.PatentXSLTProcessor;


public class PatentXSLTProcessorTest {
    /*
    @Test
    public void parseRdf() {
        
        MGraph graph = new SimpleMGraph();
        
       try {
            
           InputStream in = this.getClass().getResourceAsStream("/test/EP-1000000-A1.rdf");
           
           Parser parser = Parser.getInstance();
           
           parser.bindParsingProvider(new JenaParserProvider());
           
           parser.parse(graph, in, SupportedFormat.RDF_XML);
           
           System.out.println("Number of triples: " + graph.size());
           
           Assert.assertTrue(graph.size() > 0);
            
            
        } catch (Exception e) {
            System.out.println("Error while parsing RDF data.");
        }   
    }
    */
    
    /**
     * Start the catalog builder to deploy DTD files then start the transformation.
     */
    @Test
    public void transformXml() {
        
        // Build the catalog of DTDs files
        File baseDir = new File("/home/luigi/test");
        
        FileCatalogBuilder catalogBuilder = new FileCatalogBuilder(baseDir) ;
        try {
            catalogBuilder.build() ;
        } catch (Exception e) {
            System.out.println("Error building DTDs files catalog.\n");
            e.printStackTrace();
        }
        
        // Start the transformer
        XMLProcessor processor = new PatentXSLTProcessor();
        
        // Transform a XML file
        try {
            
            InputStream in = this.getClass().getResourceAsStream("/test/EP-1000000-A1.xml");
            
            //System.out.println(IOUtils.toString(in));
            
            InputStream rdfIs = processor.processXML( in ) ;
            
            if(rdfIs != null) {
                
                Parser parser = Parser.getInstance();
                
                parser.bindParsingProvider(new JenaParserProvider());
                
                MGraph graph = new SimpleMGraph();
                
                parser.parse(graph, in, SupportedFormat.RDF_XML);
                
                System.out.println("Number of triples: " + graph.size());
                
            }
            
            rdfIs.close();
            
            System.out.println("Finished transformation from XML to RDF");
            
            
        } catch (Exception e) {
            System.out.println("Error while transforming XML data into RDF.") ;
        }
        
    }

}
