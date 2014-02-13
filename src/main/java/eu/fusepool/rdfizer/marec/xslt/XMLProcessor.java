package eu.fusepool.rdfizer.marec.xslt;

import java.io.InputStream;

public interface XMLProcessor {

    public InputStream processXML(InputStream is) throws Exception;

}