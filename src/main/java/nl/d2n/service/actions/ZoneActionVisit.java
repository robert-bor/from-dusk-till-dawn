package nl.d2n.service.actions;

import nl.d2n.model.UpdateAction;
import nl.d2n.model.Zone;
import org.springframework.stereotype.Component;

@Component
public class ZoneActionVisit extends ZoneActionManualUpdate {

    @Override
    protected void modifyZone(Zone zone) {
        zone.setVisited(true);
    }

    @Override
    protected UpdateAction getAction() {
        return UpdateAction.VISIT_ZONE;
    }
}
