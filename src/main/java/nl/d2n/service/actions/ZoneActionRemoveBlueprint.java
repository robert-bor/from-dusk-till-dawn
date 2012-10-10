package nl.d2n.service.actions;

import nl.d2n.model.UpdateAction;
import nl.d2n.model.Zone;
import org.springframework.stereotype.Component;

@Component
public class ZoneActionRemoveBlueprint extends ZoneActionManualUpdate {

    @Override
    protected void modifyZone(Zone zone) {
        zone.setBluePrintRetrieved(true);
    }

    @Override
    protected UpdateAction getAction() {
        return UpdateAction.REMOVE_BLUEPRINT;
    }
}
