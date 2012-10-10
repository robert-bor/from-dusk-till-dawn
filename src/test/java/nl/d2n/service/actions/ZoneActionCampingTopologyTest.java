package nl.d2n.service.actions;

import nl.d2n.model.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class ZoneActionCampingTopologyTest extends ZoneAction {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void saveCampingTopology() throws Exception {
        readXml();
        getAction().setCampingTopology(CampingTopology.L5_FEW_HIDEOUTS).execute(new UserKey("cafebabe"), -4, 9, 5);
        Zone zone = zoneDao.findZone(14390, -4, 9);
        assertEquals(CampingTopology.L5_FEW_HIDEOUTS, zone.getCampingTopology());
    }

    @Test
    public void forgotCampingTopology() throws Exception {
        readXml();
        try {
            getAction().execute(new UserKey("cafebabe"), -4, 9, 5);
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.SYSTEM_ERROR, err.getError());
        }
    }

    @Test
    public void ascertainNonSingleton() throws Exception {
        readXml();
        getAction().setCampingTopology(CampingTopology.L5_FEW_HIDEOUTS).execute(new UserKey("cafebabe"), -4, 9, 5);
        try {
            getAction().execute(new UserKey("cafebabe"), -4, 9, 5);
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.SYSTEM_ERROR, err.getError());
        }
    }
    
    protected ZoneActionCampingTopology getAction() {
        return applicationContext.getBean(ZoneActionCampingTopology.class);
    }
}
