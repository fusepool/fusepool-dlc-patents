package eu.fusepool.rdfizer.marec.xslt.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.xml.resolver.tools.ResolvingXMLFilter;
import org.xml.sax.InputSource;

import eu.fusepool.rdfizer.marec.xslt.MarecXMLReader;
import eu.fusepool.rdfizer.marec.xslt.ResourceURIResolver;
import eu.fusepool.rdfizer.marec.xslt.XMLProcessor;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.crypto.dsig.TransformException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;

/**
 * @author giorgio
 * @author luigi
 *
 */
public class PatentXSLTProcessor implements XMLProcessor {

    private TransformerFactory tFactory;

    public PatentXSLTProcessor() {

        //just referencing the class to make sure it's in the OSGi import and available
        if (net.sf.saxon.TransformerFactoryImpl.class == null) {
            throw new RuntimeException("You'll never get this exception");
        }
        tFactory = TransformerFactory.newInstance("net.sf.saxon.TransformerFactoryImpl", this.getClass().getClassLoader());

    }

    public InputStream processXML(InputStream is) throws TransformerException {
        URIResolver defResolver = tFactory.getURIResolver();
        ResourceURIResolver customResolver = new ResourceURIResolver(defResolver);
        tFactory.setURIResolver(customResolver);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream xslIs = this.getClass().getResourceAsStream("/xsl/marec.xsl");
        StreamSource xlsSS = new StreamSource(xslIs);
        Transformer transformer = tFactory.newTransformer(xlsSS);

        InputSource inputSource = new InputSource(is);

        ResolvingXMLFilter filter = new ResolvingXMLFilter(new MarecXMLReader());

       /* DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false); // turns off validation
        factory.setSchema(null);      // turns off use of schema
        // but that's *still* not enough!
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            throw new RuntimeException(ex);
        }
        builder.setEntityResolver(new NullEntityResolver());
        
        builder.parse(inputSource);*/
        Source saxSource = new SAXSource(filter, inputSource);

        StreamResult sRes = new StreamResult(outputStream);

        transformer.transform(saxSource, sRes);

        return new ByteArrayInputStream(outputStream.toByteArray());

    }

    /**
     * my resolver that doesn't
     */
    private static class NullEntityResolver implements EntityResolver {

        public InputSource resolveEntity(String publicId, String systemId)
                throws SAXException, IOException {
            // Message only for debugging / if you care
            System.out.println("I'm asked to resolve: " + publicId + " / " + systemId);
            return new InputSource(new ByteArrayInputStream(new byte[0]));
        }
    }
}
