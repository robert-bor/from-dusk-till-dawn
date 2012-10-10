package nl.d2n.reader;

import nl.d2n.model.ApplicationException;
import nl.d2n.model.D2NErrorCode;
import nl.d2n.model.Profile;
import nl.d2n.model.UserKey;
import nl.d2n.reader.sitekey.D2NSiteKey;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class SoulXmlReaderTest {

    @Autowired
    D2NSiteKey siteKey;
    
    @Test
    public void readSoul() throws JAXBException, ApplicationException {
        SoulXmlReader reader = new SoulXmlReaderFromFile("soul.xml");
        Profile profile = reader.read(new UserKey("CAFEBABE"));
        assertEquals(45, profile.getDistinctions().size());
    }

    @Test
    public void readFromCacheException() {
        SoulXmlReader reader = new SoulXmlReader();
        reader.setSiteKey(new D2NSiteKey());
        reader.setXmlReader(new XmlReader() {
            public String readStringFromUrl(String url) {
                throw new RuntimeException();
            }
        });
        try {
            reader.read(new UserKey("cafebabe"), true);
            fail("Should have thrown an unsupported exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.UNSUPPORTED_OPERATION, err.getError());
        }
    }

    @Test
    public void readFromCacheNotAllowed() {
        SoulXmlReader reader = createReader(D2NErrorCode.YOU_ARE_DEAD);
        try {
            reader.read(new UserKey("cafebabe"), true);
            fail("Should have thrown the original exception, because cache reads are not allowed");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.YOU_ARE_DEAD, err.getError());
        }
    }

    protected SoulXmlReader createReader(final D2NErrorCode errorCode) {
        SoulXmlReader reader = new SoulXmlReader() {
            protected String readXmlText(final String url) throws ApplicationException {
                throw new ApplicationException(errorCode);
            }
        };
        reader.setSiteKey(new D2NSiteKey());
        return reader;
    }
}
