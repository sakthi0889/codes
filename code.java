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
            
                        // Change document class
            doc.changeClass(NEW_DOC_CLASS_NAME);

            // Get properties of document
            PropertyFilter pf = new PropertyFilter();
            pf.setMaxRecursion(1);
            doc.fetchProperties(pf);
            Properties props = doc.getProperties();

            // Set property values
            props.putValue(propName1, propValue1);
            props.putValue(propName2, propValue2);
            props.putValue(newPropName1, newPropValue1); // set value of new property
            props.putValue(newPropName2, newPropValue2); // set value of new property

            // Save changes to document
            doc.save(RefreshMode.REFRESH, null, null);
            
            
            
            
            // Read CSV file
String csvFile = "file.csv";
String csvDelimiter = ",";
BufferedReader br = new BufferedReader(new FileReader(csvFile));
String line = br.readLine(); // skip header row

while ((line = br.readLine()) != null) {
    String[] values = line.split(csvDelimiter);

    // Get document ID and new document class name from CSV
    String docId = values[0];
    String newDocClassName = values[1];

    // Create a map to hold the new property values
    Map<String, String> newPropValues = new HashMap<>();

    // Get the new property names and values from the CSV
    for (int i = 2; i < values.length; i += 2) {
        String propName = values[i];
        String propValue = (i + 1 < values.length) ? values[i + 1] : null;
        if (propValue != null) {
            newPropValues.put(propName, propValue);
        }
    }

    // Get document
    Document doc = Factory.Document.fetchInstance(os, docId, null);

    // Change document class
    doc.changeClass(newDocClassName);

    // Get properties of document
    PropertyFilter pf = new PropertyFilter();
    pf.setIncludeAllProperties(true);
    doc.fetchProperties(pf);
    Properties props = doc.getProperties();

    // Set new property values
    for (Map.Entry<String, String> entry : newPropValues.entrySet()) {
        String propName = entry.getKey();
        String propValue = entry.getValue();
        props.putValue(propName, propValue);
    }

    // Save changes to document
    doc.save(RefreshMode.REFRESH, null, null);
}

// Close BufferedReader
br.close();

        }
        br.close();
    }
}




// Read CSV file
String csvFile = "file.csv";
String csvDelimiter = ",";
String errorFile = "errors.csv";

// Open error file writer
PrintWriter errorWriter = new PrintWriter(new FileWriter(errorFile));

// Write header to error file
errorWriter.println("Document ID,Error");

BufferedReader br = new BufferedReader(new FileReader(csvFile));
String line = br.readLine(); // skip header row

while ((line = br.readLine()) != null) {
    String[] values = line.split(csvDelimiter);

    // Get document ID and new document class name from CSV
    String docId = values[0];
    String newDocClassName = values[1];

    // Create a map to hold the new property values
    Map<String, String> newPropValues = new HashMap<>();

    // Get the new property names and values from the CSV
    for (int i = 2; i < values.length; i += 2) {
        String propName = values[i];
        String propValue = (i + 1 < values.length) ? values[i + 1] : null;
        if (propValue != null) {
            newPropValues.put(propName, propValue);
        }
    }

    try {
        // Get document
        Document doc = Factory.Document.fetchInstance(os, docId, null);

        // Change document class
        doc.changeClass(newDocClassName);

        // Get properties of document
        PropertyFilter pf = new PropertyFilter();
        pf.setIncludeAllProperties(true);
        doc.fetchProperties(pf);
        Properties props = doc.getProperties();

        // Set new property values
        for (Map.Entry<String, String> entry : newPropValues.entrySet()) {
            String propName = entry.getKey();
            String propValue = entry.getValue();
            props.putValue(propName, propValue);
        }

        // Save changes to document
        doc.save(RefreshMode.REFRESH, null, null);
    } catch (Exception e) {
        // Write error to error file
        errorWriter.println(docId + "," + e.getMessage());
    }
}

// Close BufferedReader and error file writer
br.close();
errorWriter.close();






# Prompt the user to enter the source and destination folders
$sourceFolder = Read-Host "Enter the source folder path"
$destinationFolder = Read-Host "Enter the destination folder path"

$successLog = "C:\logs\success.log"
$errorLog = "C:\logs\error.log"

# Use Robocopy to copy shortcut link documents with /MOVE to replace the destination file if it already exists
$exitCode = robocopy $sourceFolder $destinationFolder /S /MOVE /XF *.lnk

# Log success or error message depending on the exit code of Robocopy
if ($exitCode -eq 0) {
    Write-Output "Robocopy completed successfully."
    Add-Content $successLog "$(Get-Date) - Robocopy completed successfully. Source: $sourceFolder, Destination: $destinationFolder"
} else {
    Write-Error "Robocopy failed with exit code $exitCode."
    Add-Content $errorLog "$(Get-Date) - Robocopy failed with exit code $exitCode. Source: $sourceFolder, Destination: $destinationFolder"
}
