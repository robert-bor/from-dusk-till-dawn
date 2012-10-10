package nl.d2n.reader.wiki;

import nl.d2n.reader.XmlReader;
import nl.d2n.util.FileToStringConverter;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.fail;

public class WikiReaderFromFile extends XmlReader {

    public final static String INPUT_PATH = "docs/sample-xml/automated-test-input/wiki/";

    private String fileName;

    public WikiReaderFromFile(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String readStringFromUrl(String url) throws IOException {
        try {
            return FileToStringConverter.getContent(new File(INPUT_PATH+fileName));
        } catch (Exception err) {
            fail("Sample XML could not be parsed");
            return null;
        }
    }

}
