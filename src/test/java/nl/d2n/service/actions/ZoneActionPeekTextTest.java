package nl.d2n.service.actions;

import nl.d2n.model.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class ZoneActionPeekTextTest extends ZoneAction {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void savePeekText() throws Exception {
        readXml();
        getAction().setPeekText("Contains a nuclear bunker").execute(new UserKey("cafebabe"), -4, 9, 5);
        Zone zone = zoneDao.findZone(14390, -4, 9);
        assertEquals("Contains a nuclear bunker", zone.getScoutPeek());
    }

    @Test
    public void forgotPeekText() throws Exception {
        readXml();
        try {
            getAction().execute(new UserKey("cafebabe"), -4, 9, 5);
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.SYSTEM_ERROR, err.getError());
        }
    }

    protected ZoneActionPeekText getAction() {
        return applicationContext.getBean(ZoneActionPeekText.class);
    }
}
