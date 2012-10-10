package nl.d2n.service.actions;

import nl.d2n.model.ApplicationException;
import nl.d2n.model.UpdateAction;
import nl.d2n.model.Zone;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class ZoneActionScoutSense extends ZoneActionManualUpdate {

    private Integer scoutSense;
    
    @Override
    protected void modifyZone(Zone zone) throws ApplicationException {
        if (getScoutSense() == null) {
            zone.setScoutSense(-1);
        } else {
            zone.setScoutSense(getScoutSense());
        }
    }

    @Override
    protected UpdateAction getAction() {
        return UpdateAction.SAVE_SCOUT_SENSE;
    }
    
    public ZoneActionScoutSense setScoutSense(final Integer scoutSense) {
        this.scoutSense = scoutSense;
        return this;
    }
    public Integer getScoutSense() throws ApplicationException {
        return this.scoutSense;
    }
}
