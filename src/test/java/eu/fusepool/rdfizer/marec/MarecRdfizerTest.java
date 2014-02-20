/*
 * Copyright 2014 Reto.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.fusepool.rdfizer.marec;

import eu.fusepool.datalifecycle.Rdfizer;
import java.io.InputStream;
import org.apache.clerezza.rdf.core.MGraph;
import org.apache.clerezza.rdf.core.serializedform.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.apache.clerezza.rdf.jena.parser.JenaParserProvider;

/**
 *
 * @author Reto
 */
public class MarecRdfizerTest {
    
    Rdfizer rdfizer = new MarecRdfizer();
    
    @Test
    public void epoXml() {
        //AccessController.checkPermission(new AllPermission());
        /*
        InputStream in = this.getClass().getResourceAsStream("/test/EP-1000000-A1.xml");
        
        Parser parser = Parser.getInstance();
        parser.bindParsingProvider(new JenaParserProvider());
        
        MGraph mGraph = rdfizer.transform(in);
        Assert.assertTrue(mGraph.size() > 0);
        */
    }
    
    @Test
    public void pubMedXml() {
    	/*
        InputStream in = this.getClass().getResourceAsStream("EP-1000000-A1.xml");
        MGraph mGraph = rdfizer.transform(in);
        Assert.assertTrue(mGraph.size() > 0);
        */
    }
    
}
