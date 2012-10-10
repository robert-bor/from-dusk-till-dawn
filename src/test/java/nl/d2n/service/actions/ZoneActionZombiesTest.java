package nl.d2n.service.actions;

import nl.d2n.model.UserKey;
import nl.d2n.model.Zone;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static junit.framework.Assert.assertEquals;

public class ZoneActionZombiesTest extends ZoneAction {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void setZombies() throws Exception {
        readXml();
        getAction().setZombies(3).execute(new UserKey("cafebabe"), -4, 9, 5);
        Zone zone = zoneDao.findZone(14390, -4, 9);
        assertEquals(3, zone.getZombies());
    }

    @Test
    public void resetZombies() throws Exception {
        readXml();
        getAction().execute(new UserKey("cafebabe"), -4, 9, 5);
        Zone zone = zoneDao.findZone(14390, -4, 9);
        assertEquals(-1, zone.getZombies());
    }

    protected ZoneActionZombies getAction() {
        return applicationContext.getBean(ZoneActionZombies.class);
    }
}
