import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.filenet.api.collection.DocumentSet;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.Properties;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;

public class ChangeDocumentClass {
    
    private static final String CSV_FILE = "documents.csv"; // path to the CSV file
    private static final String OBJECT_STORE_NAME = "ObjectStore"; // name of the object store
    private static final String NEW_DOC_CLASS_NAME = "NewDocumentClass"; // name of the new document class
    
    public static void main(String[] args) throws IOException {
        // Get connection to object store
        ObjectStore os = Factory.Connection.getConnection().getObjectStore(OBJECT_STORE_NAME);
        
        // Read CSV file and update documents
        BufferedReader br = new BufferedReader(new FileReader(CSV_FILE));
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            String docId = values[0];
            String propName1 = values[1]; // name of the first property to update
            String propValue1 = values[2]; // value of the first property to update
            String propName2 = values[3]; // name of the second property to update
            String propValue2 = values[4]; // value of the second property to update
            
            // Get document by ID
            Document doc = Factory.Document.fetchInstance(os, new Id(docId), null);
            
            // Get properties of document
            PropertyFilter pf = new PropertyFilter();
            pf.addIncludeProperty(new Integer(0), null, true, PropertyFilter.CURRENT_VERSION);
            Properties props = doc.getProperties(pf);
            
            // Update document properties
            props.putValue(propName1, propValue1);
            props.putValue(propName2, propValue2);
            
            // Change document class and save changes
            doc.changeClass(NEW_DOC_CLASS_NAME);
            doc.save(RefreshMode.REFRESH, null, null);
        }
        br.close();
    }
}
