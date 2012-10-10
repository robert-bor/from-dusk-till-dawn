package nl.d2n.model;

import nl.d2n.reader.D2NXmlReader;
import nl.d2n.reader.XmlReader;
import nl.d2n.util.FileToStringConverter;
import org.junit.Test;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class D2NErrorTest {

    @Test
    public void userNotFound() throws Exception {
        assertErrorCode("docs/sample-xml/error/user-not-found.xml", D2NErrorCode.USER_NOT_FOUND);
    }

    @Test
    public void notInGame() throws Exception {
        assertErrorCode("docs/sample-xml/error/not-in-game.xml", D2NErrorCode.NOT_IN_GAME);
    }

    @Test
    public void invalidKeys() throws Exception {
        assertErrorCode("docs/sample-xml/error/invalid-keys.xml", D2NErrorCode.INVALID_KEYS);
    }

    @Test
    public void hordeAttacking() throws Exception {
        assertErrorCode("docs/sample-xml/error/horde-attacking.xml", D2NErrorCode.HORDE_ATTACKING);
    }

    @Test
    public void xmlnotFound() throws Exception {
        assertErrorCode("docs/sample-xml/error/xml-not-found.xml", D2NErrorCode.XML_NOT_FOUND);
    }

    @Test
    public void versionCheck() throws Exception {
        assertErrorCode("docs/sample-xml/error/version-check.xml", D2NErrorCode.VERSION_CHECK);
    }

    protected void assertErrorCode(String filename, D2NErrorCode expectedErrorCode) throws Exception {
        String xml = FileToStringConverter.getContent(new File(filename));
        try {
            Info info = new D2NXmlReader().convertXmlDocument((new XmlReader()).readDocumentFromString(xml)).getInfo();
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(expectedErrorCode, err.getError());
        }
    }
}
