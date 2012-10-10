package nl.d2n.service.actions;

import nl.d2n.dao.CityDao;
import nl.d2n.dao.InsideBuildingDao;
import nl.d2n.model.*;
import nl.d2n.service.MapService;
import nl.d2n.service.UniqueInsideBuildingManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static nl.d2n.service.UserSecurityCheck.*;

public abstract class AbstractBuildingAction extends AbstractAction {

    @Autowired
    private MapService mapService;

    @Autowired
    private UniqueInsideBuildingManager uniqueInsideBuildingManager;

    @Autowired
    private CityDao cityDao;

    @Autowired
    protected InsideBuildingDao insideBuildingDao;
    
    public void execute(UserKey userKey, Integer cityId, Integer buildingId, Integer day) throws ApplicationException {

        // 1. Perform the security check
        performSecurityCheck(userKey);

        // 2. Find the city for which the change applies
        City city = cityDao.findCity(cityId);
        if (city == null) {
            throw new ApplicationException(D2NErrorCode.CITY_DOES_NOT_EXIST);
        }

        // 3. Find the building
        UniqueInsideBuilding uniqueInsideBuilding = uniqueInsideBuildingManager.get(buildingId);
        if (uniqueInsideBuilding == null) {
            throw new ApplicationException(D2NErrorCode.BUILDING_DOES_NOT_EXIST);
        }

        // 4. Check if the city already contains the entry
        InsideBuilding insideBuilding = getBuilding(buildingId, insideBuildingDao.findInsideBuildings(cityId));

        // 5. Set the right status on the building
        modifyBuilding(city, insideBuilding, uniqueInsideBuilding);
        
        logAction(city, userKey, day, getGameClock(userKey));
    }

    protected InsideBuilding getBuilding(Integer buildingId, List<InsideBuilding> insideBuildings) {
        for (InsideBuilding building : insideBuildings) {
            if (buildingId == building.getBuildingId()) {
                return building;
            }
        }
        return null;
    }

    protected abstract void modifyBuilding(City city, InsideBuilding insideBuilding, UniqueInsideBuilding uniqueInsideBuilding);
    
    @Override
    protected boolean allowFirstTime() {
        return DO_NOT_ALLOW_FIRST_TIME;
    }

    @Override
    protected boolean checkForSecuritySetting() {
        return CHECK_FOR_SECURE_SETTING;
    }

    @Override
    protected boolean checkForShunnedSetting() {
        return CHECK_FOR_SHUNNED_SETTING;
    }

}
