package nl.d2n.service;

import nl.d2n.dao.*;
import nl.d2n.model.*;
import nl.d2n.model.Info;
import nl.d2n.model.Owner;
import nl.d2n.reader.D2NXmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static nl.d2n.service.UserSecurityCheck.*;

@Service
public class MapService {

    public static final boolean ALLOW_CACHE_READ    = true;
    public static final boolean DISALLOW_CACHE_READ = false;

    public static final boolean PERFORM_STATUS_CHECKS = true;
    public static final boolean DO_NOT_PERFORM_STATUS_CHECKS = false;

    public static final boolean ALWAYS_AVAILABLE_BUILDING   = true;
    public static final boolean STATUS_FROM_INPUT           = false;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private D2NXmlReader d2nXmlReader;

    @Autowired
    private UniqueItemManager uniqueItemsManager;

    @Autowired
    private UniqueOutsideBuildingManager uniqueOutsideBuildingManager;

    @Autowired
    private UniqueInsideBuildingManager uniqueInsideBuildingManager;

    @Autowired
    private CityDao cityDao;

    @Autowired
    private ZoneDao zoneDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private InsideBuildingDao insideBuildingDao;

    @Autowired
    private UserSecurityCheck userSecurityCheck;

    @Autowired
    private GameClock gameClock;

    @Autowired
    private UserService userService;

    public List<InsideBuilding> getBuildingsWithStatus(List<InsideBuilding> constructedBuildings, Integer cityId) {
        // Draw the initial list from the unique buildings
        List<InsideBuilding> buildings = UniqueInsideBuilding.createBuildingHierarchy(uniqueInsideBuildingManager.getMap().values());
        // Add all the buildings which are automatically included as always available
        buildings = InsideBuilding.setConstructedBuildingStatus(
                buildings,
                uniqueInsideBuildingManager.getAllAlwaysAvailableBuildings(),
                InsideBuildingStatus.AVAILABLE,
                ALWAYS_AVAILABLE_BUILDING);
        // Add the available status -- this information is stored in the database
        buildings = InsideBuilding.setConstructedBuildingStatus(
                buildings,
                insideBuildingDao.findInsideBuildings(cityId),
                InsideBuildingStatus.AVAILABLE,
                STATUS_FROM_INPUT);
        // Add the constructed status -- this information is taken directly from the XML file
        return InsideBuilding.setConstructedBuildingStatus(
                buildings,
                constructedBuildings,
                InsideBuildingStatus.CONSTRUCTED,
                STATUS_FROM_INPUT);
    }

    protected Zone deleteAllItemsInZone(Zone zone) {
        zoneDao.deleteItemsInZone(zone.getId());
        zone.clearItems();
        return zone;
    }

    public Map<Integer, Map<Integer, Zone>> getZones(int cityId) throws ApplicationException {
        InfoWrapper wrapper = getInfoFromCache(cityId);

        MapMatrix matrix = wrapper.getInfo().getMatrix();
        matrix.mergeZones(
                zoneDao.findZones(cityId),
                wrapper.getInfo().getGameHeader().getGame().getDay());

        return matrix.toJsonMap();
    }

    public Zone getZone(int cityId, int x, int y) throws ApplicationException {
        InfoWrapper wrapper = getInfoFromCache(cityId);

        // Load the specific zone from the database and merge it into the info files
        Zone zone = zoneDao.findZone(cityId, x, y);

        MapMatrix matrix = wrapper.getInfo().getMatrix();
        if (zone != null) {
            matrix.mergeZone(
                    zone,
                    wrapper.getInfo().getGameHeader().getGame().getDay());
        }

        return matrix.getMatrix()[matrix.getMatrixXPos(x)][matrix.getMatrixYPos(y)];
    }

    protected InfoWrapper getInfoFromCache(int cityId) throws ApplicationException {
        InfoWrapper wrapper = d2nXmlReader.readXmlFromCache(cityId);

        if (wrapper.requiresEnhancing()) {
            enhanceInformation(wrapper);
        }

        return wrapper;
    }

    protected InfoWrapper readInfo(final UserKey userKey, boolean allowReadFromCache) throws ApplicationException {
        try {
            return d2nXmlReader.read(userKey, allowReadFromCache);
        } catch (ApplicationException err) {
            if (D2NErrorCode.NOT_IN_GAME.equals(err.getError())) {
                userService.removeCitizenFromTown(userKey);
            }
            throw err;
        }
    }
    
    public InfoWrapper readInfoAndPrepareMatrix(final UserKey userKey, boolean allowReadFromCache,
                                                   boolean performStatusChecks) throws ApplicationException {

        // 0. Check key
        userKey.check();

        // 1. Read from D2N
        final InfoWrapper infoWrapper = readInfo(userKey, allowReadFromCache);

        // 2. Store new information from the XML
        if (!infoWrapper.isStale()) {
            storeNewInformation(infoWrapper, userKey);
        }

        // 3. Check if the next steps may be executed -- user may be dead, inside town, town is in chaos etc
        if (performStatusChecks) {
            infoWrapper.getInfo().performVariousStatusChecks();
        }

        // 4. Enhance the information object
        if (infoWrapper.requiresEnhancing()) {
            enhanceInformation(infoWrapper);
        }

        // 5. Add the user
        infoWrapper.setUser(userDao.find(userKey));

        return infoWrapper;
    }

    protected void storeNewInformation(InfoWrapper infoWrapper, UserKey userKey) throws ApplicationException {
        Info info = infoWrapper.getInfo();

        // 1. Update the game clock
        gameClock.setDateTime(info.getGameHeader().getGame().getDate());

        // 2. Check for unique items
        uniqueItemsManager.checkForExistence(info.getItems());

        // 3. Check for unique outside buildings
        uniqueOutsideBuildingManager.checkForExistence(OutsideBuilding.extractOutsideBuildingsFromMap(info.getMap().getZones()));

        // 4. Check for unique inside buildings
        uniqueInsideBuildingManager.checkForExistence(info.getCity().getBuildings());

        // 5. Check if the city exists, and save if need be
        City city = saveCity(info);

        // 6. Remove all the dead citizens from that town
        userService.removeDeadCitizensFromTown(city, info.getCitizens());

        // 7. Store the user information
        userService.updateUser(
                city,
                userKey,
                info.getGameHeader().isSecure() ? info.getGameHeader().getOwner().getCitizen().getId() : null,
                info.getGameHeader().isSecure() ? info.getGameHeader().getOwner().getCitizen().getName() : "",
                info.getGameHeader().isSecure(),
                info.getGameHeader().isSecure() && info.getGameHeader().getOwner().getCitizen().isShunned(),
                info.getCitizens());
    }

    protected InfoWrapper enhanceInformation(InfoWrapper infoWrapper) {

        Info info = infoWrapper.getInfo();

        // 1. Supplement the outside buildings with the die2nite Wiki URL
        addUrlsToOutsideBuildings(info.getMap().getZones());

        // 2. Convert to a matrix object
        MapMatrix mapMatrix = new MapMatrix(
                info.getMap().getWidth(),
                info.getMap().getHeight(),
                info.getCity().getX(),
                info.getCity().getY(),
                info.getMap().getZones(),
                info.isUpgradedMapAvailable());
        info.setMatrix(mapMatrix);

        // 3. Update the citizens with their real coordinates
        info.getMatrix().updateCitizenCoordinates(
                info.getCitizens(),
                info.getCity().isChaosMode(),
                info.getGameHeader().isSecure() ? info.getGameHeader().getOwner().getCitizen() : null);

        // 4. Rank the users for activity
        if (gameClock.isInitialized()) {
            activityService.rankCitizens(info.getGameHeader().getGame().getId(), info.getCitizens(), gameClock);
        }

        // 5. Enhance with item information
        info.setUniqueItems(uniqueItemsManager.getItems());
        info.setItemLookup(uniqueItemsManager.getMap());

        return infoWrapper;
    }


    private void addUrlsToOutsideBuildings(List<Zone> zones) {
        for (Zone zone : zones) {
            OutsideBuilding building = zone.getBuilding();
            if (building == null) {
                continue;
            }
            UniqueOutsideBuilding uniqueOutsideBuilding = uniqueOutsideBuildingManager.get(building.getType());
            if (uniqueOutsideBuilding == null) {
                continue;
            }
            building.setUrl(uniqueOutsideBuilding.getUrl());
        }
    }

    protected City saveCity(Info info) {
        info.getCity().setId(info.getGameHeader().getGame().getId());
        City city = cityDao.findCity(info.getCity().getId());
        if (city == null) {
            city = info.getCity();
        }
        if (city.getWidth() == 0) { // Works both on new and existing cities without dimensions
            updateCity(city, info);
            city = saveCity(city);
        }
        return city;
    }
    protected void updateCity(City city, Info info) {
        city.setWidth(info.getMap().getWidth());
        city.setHeight(info.getMap().getHeight());
        city.setHardcore(info.getCity().isHard());
        MapMatrix mapMatrix = info.getMatrix();
        if (mapMatrix == null) {
            // Create a temporary matrix object to calculate the boundaries
            mapMatrix = new MapMatrix(city.getWidth(), city.getHeight(), info.getCity().getX(), info.getCity().getY(), new ArrayList<Zone>(), false);
        }
        city.setLeft(mapMatrix.getLeft());
        city.setRight(mapMatrix.getRight());
        city.setTop(mapMatrix.getTop());
        city.setBottom(mapMatrix.getBottom());
    }
    protected City saveCity(City city) {
        cityDao.saveCity(city);
        city = cityDao.findCity(city.getId());
        return city;
    }

    public void setD2nXmlReader(D2NXmlReader reader) { this.d2nXmlReader = reader; }
    public void setUserService(UserService userService) { this.userService = userService; }
}
