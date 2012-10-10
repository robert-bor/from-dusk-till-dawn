package nl.d2n.reader;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.ApplicationException;
import nl.d2n.model.D2NErrorCode;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class D2NXmlReaderTest extends SpringContextTestCase {

    @Autowired
    private D2NXmlReader xmlReader;

    @Test
    public void userNotInGame() {
        try {
            Integer cityId = null;
            xmlReader.readXmlFromCache(cityId);
            fail("Should have thrown an error because the city is null");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.NOT_IN_GAME, err.getError());
        }
    }
}
