package nl.d2n.service.actions;

import nl.d2n.model.ApplicationException;
import nl.d2n.model.UpdateAction;
import nl.d2n.model.Zone;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("prototype")
@Component
public class ZoneActionZombies extends ZoneActionManualUpdate {

    private Integer zombies;
    
    @Override
    protected void modifyZone(Zone zone) throws ApplicationException {
        if (getZombies() == null) {
            zone.setZombies(-1);
        } else {
            zone.setZombies(getZombies());
        }
    }

    @Override
    protected UpdateAction getAction() {
        return UpdateAction.SAVE_ZOMBIES;
    }
    
    public ZoneActionZombies setZombies(final Integer zombies) {
        this.zombies = zombies;
        return this;
    }
    public Integer getZombies() throws ApplicationException {
        return this.zombies;
    }
}
