package nl.d2n.service.actions;

import nl.d2n.model.UserKey;
import nl.d2n.model.Zone;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class ZoneActionZoneDepletionTest extends ZoneAction {

    @Test
    public void depleteZone() throws Exception {
        readXml();
        getDepleteAction().execute(new UserKey("cafebabe"), -4, 9, 5);
        Zone zone = zoneDao.findZone(14390, -4, 9);
        assertTrue(zone.isZoneDepleted());
    }

    @Test
    public void replenishZone() throws Exception {
        readXml();
        getDepleteAction().execute(new UserKey("cafebabe"), -4, 9, 5);
        getReplenishAction().execute(new UserKey("cafebabe"), -4, 9, 5);
        Zone zone = zoneDao.findZone(14390, -4, 9);
        assertFalse(zone.isZoneDepleted());
    }

    protected ZoneActionDepleteZone getDepleteAction() {
        return applicationContext.getBean(ZoneActionDepleteZone.class);
    }
    protected ZoneActionReplenishZone getReplenishAction() {
        return applicationContext.getBean(ZoneActionReplenishZone.class);
    }
}
