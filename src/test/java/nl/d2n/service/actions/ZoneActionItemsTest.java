package nl.d2n.service.actions;

import nl.d2n.model.Item;
import nl.d2n.model.UserKey;
import nl.d2n.model.Zone;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static junit.framework.Assert.assertEquals;

public class ZoneActionItemsTest extends ZoneAction {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void saveCampingTopology() throws Exception {
        readXml();
        getAction().setItems(Item.convertKeysToItems(new String[] { "38-1", "184-1", "28-1" }) ).execute(new UserKey("cafebabe"), -4, 9, 5);
        Zone zone = zoneDao.findZone(14390, -4, 9);
        assertEquals(3, zone.getItems().size());
    }

    protected ZoneActionItems getAction() {
        return applicationContext.getBean(ZoneActionItems.class);
    }
}
