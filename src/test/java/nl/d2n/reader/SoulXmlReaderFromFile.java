package nl.d2n.reader;

import nl.d2n.model.ApplicationException;
import nl.d2n.model.Info;
import nl.d2n.model.Profile;
import nl.d2n.model.UserKey;
import nl.d2n.util.FileToStringConverter;

import java.io.File;

import static junit.framework.Assert.fail;

public class SoulXmlReaderFromFile extends SoulXmlReader {

    public final static String INPUT_PATH = "docs/sample-xml/automated-test-input/";

    private String fileName;

    public SoulXmlReaderFromFile(String fileName) {
        this.fileName = fileName;
    }

    public Profile read(final UserKey userKey) throws ApplicationException {
        try {
            String xml = FileToStringConverter.getContent(new File(INPUT_PATH+fileName));
            return convertXmlDocument(new XmlReader().readDocumentFromString(xml));
        } catch (Exception err) {
            fail("Sample XML could not be parsed");
            return null;
        }
    }
}
