package nl.d2n.service.actions;

import nl.d2n.model.*;
import org.springframework.stereotype.Component;

@Component
public class BuildingActionUnlock extends AbstractBuildingAction {

    protected void modifyBuilding(City city, InsideBuilding insideBuilding, UniqueInsideBuilding uniqueInsideBuilding) {
        if (insideBuilding == null) {
            insideBuilding = uniqueInsideBuilding.createBuilding();
            insideBuilding.setCity(city);
        }
        insideBuilding.setStatus(InsideBuildingStatus.AVAILABLE);
        insideBuildingDao.save(insideBuilding);
    }

    @Override
    protected UpdateAction getAction() {
        return UpdateAction.UNLOCK_BUILDING;
    }
}
