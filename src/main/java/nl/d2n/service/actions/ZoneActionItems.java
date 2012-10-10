package nl.d2n.service.actions;

import nl.d2n.model.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Scope("prototype")
@Component
public class ZoneActionItems extends ZoneActionManualUpdate {

    private List<Item> items;
    
    @Override
    protected void modifyZone(Zone zone) throws ApplicationException {
        zone = deleteAllItemsInZone(zone);
        if (getItems() != null) {
            for (Item item : items) {
                item.setZone(zone);
                zone.addItem(item);
            }
        }
    }

    protected Zone deleteAllItemsInZone(Zone zone) {
        zoneDao.deleteItemsInZone(zone.getId());
        zone.clearItems();
        return zone;
    }

    @Override
    protected UpdateAction getAction() {
        return UpdateAction.SAVE_ITEMS;
    }
    
    public ZoneActionItems setItems(final List<Item> items) {
        this.items = items;
        return this;
    }
    public List<Item> getItems() throws ApplicationException {
        return this.items;
    }
}
