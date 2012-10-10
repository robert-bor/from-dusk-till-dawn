package nl.d2n.service.actions;

import nl.d2n.model.UserKey;
import nl.d2n.model.Zone;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class ZoneActionBuildingDepletionTest extends ZoneAction {

    @Test
    public void depleteBuilding() throws Exception {
        readXml();
        getDepleteAction().execute(new UserKey("cafebabe"), -4, 9, 5);
        Zone zone = zoneDao.findZone(14390, -4, 9);
        assertTrue(zone.isBuildingDepleted());
    }

    @Test
    public void replenishBuilding() throws Exception {
        readXml();
        getDepleteAction().execute(new UserKey("cafebabe"), -4, 9, 5);
        getReplenishAction().execute(new UserKey("cafebabe"), -4, 9, 5);
        Zone zone = zoneDao.findZone(14390, -4, 9);
        assertFalse(zone.isBuildingDepleted());
    }

    protected ZoneActionDepleteBuilding getDepleteAction() {
        return applicationContext.getBean(ZoneActionDepleteBuilding.class);
    }
    protected ZoneActionReplenishBuilding getReplenishAction() {
        return applicationContext.getBean(ZoneActionReplenishBuilding.class);
    }
}
