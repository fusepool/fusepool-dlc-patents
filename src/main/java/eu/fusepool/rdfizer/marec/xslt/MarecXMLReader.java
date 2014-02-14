/**
 * 
 */
package eu.fusepool.rdfizer.marec.xslt;

import java.io.IOException;


import org.apache.xerces.util.XMLCatalogResolver;
import org.apache.xml.resolver.tools.ResolvingXMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;

/**
 * @author giorgio
 *
 */
public class MarecXMLReader extends ResolvingXMLReader {

    
    XMLCatalogResolver resolver ;
    
    /**
     * @throws Exception 
     * @throws SAXNotRecognizedException 
     * 
     */
    public MarecXMLReader() throws SAXNotRecognizedException, Exception {
        super();
        String [] catalogs = {"file:///" + CatalogBuilder.getCatalogPath()};
        // Create catalog resolver and set a catalog list.
        resolver = new XMLCatalogResolver();
        resolver.setPreferPublic(true);
        resolver.setCatalogList(catalogs);
        
    }

    /* (non-Javadoc)
     * @see org.apache.xml.resolver.tools.ResolvingXMLFilter#resolveEntity(java.lang.String, java.lang.String)
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) {
        try {
            //System.out.println("<public publicId=\""+publicId+"\"" + " uri=\""+systemId+"\" />");
            InputSource is = resolver.resolveEntity(publicId, systemId);
            if(is==null) {
                //System.out.println("############ NOT FOUND #########<public publicId=\""+publicId+"\"" + " uri=\""+systemId+"\" />");
            }
            return is ;
        } catch (SAXException e) {
            e.printStackTrace();
            return null ;
        } catch (IOException e) {
            e.printStackTrace();
            return null ;
        }
    }


    
    
    
}
