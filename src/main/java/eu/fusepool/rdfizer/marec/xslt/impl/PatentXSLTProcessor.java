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


/**
 * @author giorgio
 * @author luigi
 * 
 */
public class PatentXSLTProcessor implements XMLProcessor {
    
    private TransformerFactory tFactory;
    
    public PatentXSLTProcessor() {
        
        
        if(tFactory==null) {
            
            tFactory = TransformerFactory.newInstance();
            
        }
                
    }
    
    public InputStream processXML(InputStream is) throws Exception {
        URIResolver defResolver = tFactory.getURIResolver() ;
        ResourceURIResolver customResolver = new ResourceURIResolver(defResolver) ;
        tFactory.setURIResolver(customResolver) ;
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream xslIs = this.getClass().getResourceAsStream("/xsl/marec.xsl") ;
        StreamSource xlsSS = new StreamSource(xslIs) ;
        Transformer transformer = tFactory.newTransformer(xlsSS);
        
        InputSource inputSource = new InputSource(is) ;
        
        ResolvingXMLFilter filter = new ResolvingXMLFilter(new MarecXMLReader());
        
        SAXSource saxSource = new SAXSource(filter, inputSource) ;
        StreamResult sRes = new StreamResult(outputStream) ;
        transformer.transform(saxSource, sRes) ; 
        
        return new ByteArrayInputStream(outputStream.toByteArray());
        
    }
    
    
}
