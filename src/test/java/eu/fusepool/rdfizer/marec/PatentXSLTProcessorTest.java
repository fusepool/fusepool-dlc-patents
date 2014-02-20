package eu.fusepool.rdfizer.marec;

import java.io.InputStream;

import org.apache.clerezza.rdf.core.serializedform.Parser;
import org.apache.clerezza.rdf.core.serializedform.SupportedFormat;
import org.junit.Test;

import eu.fusepool.rdfizer.marec.xslt.XMLProcessor;
import eu.fusepool.rdfizer.marec.xslt.impl.PatentXSLTProcessor;
import org.apache.clerezza.rdf.core.Graph;
import org.junit.Assert;

public class PatentXSLTProcessorTest {
    /**
     * Start the catalog builder to deploy DTD files then start the
     * transformation.
     */
    @Test
    public void transformXml() throws Exception {


        // Start the transformer
        XMLProcessor processor = new PatentXSLTProcessor();

        // Transform a XML file

        InputStream xmlIn = this.getClass().getResourceAsStream("EP-1000000-A1.xml");

        InputStream rdfFromXmlIn = processor.processXML(xmlIn);

        Parser parser = Parser.getInstance();


        Graph graphFromXml = parser.parse(rdfFromXmlIn, SupportedFormat.RDF_XML);
        rdfFromXmlIn.close();
        
        InputStream rdfIn = this.getClass().getResourceAsStream("EP-1000000-A1.rdf");
        Graph graphFromRdf = parser.parse(rdfIn, SupportedFormat.RDF_XML);
        
        //because of undeterministic URI assignment (UUIDs) this test cannot work
        //Assert.assertEquals("The graph from XML is not the expected one", graphFromRdf, graphFromXml);

        //checking that at least the size is right
        Assert.assertEquals("The graph from XML is not of the expected size", 
                graphFromRdf.size(), graphFromXml.size());

        



    }
}
