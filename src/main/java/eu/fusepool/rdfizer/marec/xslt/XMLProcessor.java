package eu.fusepool.rdfizer.marec.xslt;

import java.io.InputStream;
import javax.xml.transform.TransformerException;

/**
 * Transforms an InputStream in a specific XML format to an RDF/XML Stream
 */
public interface XMLProcessor {

    public InputStream processXML(InputStream is) throws TransformerException;

}