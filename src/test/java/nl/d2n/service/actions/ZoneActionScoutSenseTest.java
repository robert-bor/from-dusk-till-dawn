package nl.d2n.service.actions;

import nl.d2n.model.UserKey;
import nl.d2n.model.Zone;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static junit.framework.Assert.assertEquals;

public class ZoneActionScoutSenseTest extends ZoneAction {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void setScoutSense() throws Exception {
        readXml();
        getAction().setScoutSense(16).execute(new UserKey("cafebabe"), -4, 9, 5);
        Zone zone = zoneDao.findZone(14390, -4, 9);
        assertEquals(16, zone.getScoutSense());
    }

    @Test
    public void resetScoutSense() throws Exception {
        readXml();
        getAction().execute(new UserKey("cafebabe"), -4, 9, 5);
        Zone zone = zoneDao.findZone(14390, -4, 9);
        assertEquals(-1, zone.getScoutSense());
    }

    protected ZoneActionScoutSense getAction() {
        return applicationContext.getBean(ZoneActionScoutSense.class);
    }
}
