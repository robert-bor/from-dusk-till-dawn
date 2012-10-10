package nl.d2n.service.actions;

import nl.d2n.model.UserKey;
import nl.d2n.model.Zone;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class ZoneActionBlueprintsTest extends ZoneAction {

    @Test
    public void removeBluePrint() throws Exception {
        readXml();
        getRemoveAction().execute(new UserKey("cafebabe"), -4, 9, 5);
        Zone zone = zoneDao.findZone(14390, -4, 9);
        assertTrue(zone.isBluePrintRetrieved());
    }

    @Test
    public void addBluePrint() throws Exception {
        readXml();
        getRemoveAction().execute(new UserKey("cafebabe"), -4, 9, 5);
        getAddAction().execute(new UserKey("cafebabe"), -4, 9, 5);
        Zone zone = zoneDao.findZone(14390, -4, 9);
        assertFalse(zone.isBluePrintRetrieved());
    }

    protected ZoneActionRemoveBlueprint getRemoveAction() {
        return applicationContext.getBean(ZoneActionRemoveBlueprint.class);
    }
    protected ZoneActionAddBlueprint getAddAction() {
        return applicationContext.getBean(ZoneActionAddBlueprint.class);
    }
}
