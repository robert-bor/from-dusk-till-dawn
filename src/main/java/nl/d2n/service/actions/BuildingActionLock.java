package nl.d2n.service.actions;

import nl.d2n.model.City;
import nl.d2n.model.InsideBuilding;
import nl.d2n.model.UniqueInsideBuilding;
import nl.d2n.model.UpdateAction;
import org.springframework.stereotype.Component;

@Component
public class BuildingActionLock extends AbstractBuildingAction {

    protected void modifyBuilding(City city, InsideBuilding insideBuilding, UniqueInsideBuilding uniqueInsideBuilding) {
        if (insideBuilding != null) {
            insideBuildingDao.delete(city.getId(), insideBuilding.getBuildingId());
        }
    }

    @Override
    protected UpdateAction getAction() {
        return UpdateAction.LOCK_BUILDING;
    }
}
