package nl.d2n.service.actions;

import nl.d2n.dao.ZoneDao;
import nl.d2n.model.*;
import nl.d2n.reader.D2NXmlReaderFromFile;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class ZoneActionExtendedUpdateTest extends ZoneAction {

    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private ZoneDao zoneDao;

    @Test
    public void missingZombies() {
        checkForMissingParameters(null, true, D2NErrorCode.MISSING_PARAMETER);
    }

    @Test
    public void missingZoneDepletion() {
        checkForMissingParameters(4, null, D2NErrorCode.MISSING_PARAMETER);
    }

    protected void checkForMissingParameters(Integer zombies, Boolean zoneDepleted, D2NErrorCode expectedError) {
        mapService.setD2nXmlReader(new D2NXmlReaderFromFile("hard-town.xml"));
        try {
            applicationContext.getBean(ZoneActionExtendedUpdate.class)
                    .setItems(Item.convertKeysToItems( new String[] { "1-3" } ))
                    .setZombies(zombies)
                    .setZoneDepleted(zoneDepleted)
                    .execute(new UserKey("cafebabe"));
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(expectedError, err.getError());
        }
    }
    
    @Test
    public void updateHardcoreTown() throws ApplicationException {
        mapService.setD2nXmlReader(new D2NXmlReaderFromFile("hard-town.xml"));
        applicationContext.getBean(ZoneActionExtendedUpdate.class)
                .setCampingTopology(CampingTopology.L5_FEW_HIDEOUTS)
                .setBlueprintAvailable(false)
                .setItems(Item.convertKeysToItems( new String[] { "1-3" } ))
                .setZombies(4)
                .setZoneDepleted(true)
                .execute(new UserKey("cafebabe")); 
        Zone zone = zoneDao.findZone(17678, -3, -5);
        assertEquals(true, zone.isBluePrintRetrieved());
        assertEquals(CampingTopology.L5_FEW_HIDEOUTS, zone.getCampingTopology());
        assertEquals(1, zone.getItems().size());
        assertEquals(4, zone.getZombies());
        assertTrue(zone.isZoneDepleted());
    }

    @Test
    public void updateChaosTown() {
        mapService.setD2nXmlReader(new D2NXmlReaderFromFile("chaos-mode.xml"));
        try {
            applicationContext.getBean(ZoneActionExtendedUpdate.class)
                    .execute(new UserKey("cafebabe"));
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.CHAOS_MODE, err.getError());
        }

    }
}
