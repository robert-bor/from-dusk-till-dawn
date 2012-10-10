package nl.d2n.service.actions;

import nl.d2n.model.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class ZoneActionCampingTopology extends ZoneActionManualUpdate {

    private CampingTopology campingTopology;
    
    @Override
    protected void modifyZone(Zone zone) throws ApplicationException {
        zone.setCampingTopology(getCampingTopology());
    }

    @Override
    protected UpdateAction getAction() {
        return UpdateAction.SAVE_CAMPING;
    }
    
    public ZoneActionCampingTopology setCampingTopology(final CampingTopology campingTopology) {
        this.campingTopology = campingTopology;
        return this;
    }
    public CampingTopology getCampingTopology() throws ApplicationException {
        if (this.campingTopology == null) { throw new ApplicationException(D2NErrorCode.SYSTEM_ERROR, "No camping topology"); }
        return this.campingTopology;
    }
}
