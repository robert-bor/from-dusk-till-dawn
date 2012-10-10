package nl.d2n.reader;

import nl.d2n.model.ApplicationException;
import nl.d2n.model.Info;
import nl.d2n.model.InfoWrapper;
import nl.d2n.model.UserKey;
import nl.d2n.util.FileToStringConverter;

import java.io.File;

import static junit.framework.Assert.fail;

public class D2NXmlReaderFromFile extends D2NXmlReader {

    public final static String INPUT_PATH = "docs/sample-xml/automated-test-input/";

    private String fileName;

    public D2NXmlReaderFromFile(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public InfoWrapper read(final UserKey userKey) throws ApplicationException {
        return read(userKey, false);
    }
    @Override
    public InfoWrapper read(final UserKey userKey, boolean allowReadFromCache) throws ApplicationException {
        try {
            String xml = FileToStringConverter.getContent(new File(INPUT_PATH+fileName));
            return convertXmlDocument(new XmlReader().readDocumentFromString(xml));
        } catch (Exception err) {
            fail("Sample XML could not be parsed");
            return null;
        }
    }
}
