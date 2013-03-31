package nl.d2n.util;

import nl.d2n.dao.*;
import nl.d2n.model.*;
import nl.d2n.model.builder.CityBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class ClassCreator {

    @Resource
    private CityDao cityDao;

    @Resource
    private UserDao userDao;

    @Resource
    private ZoneDao zoneDao;

    @Resource
    private UserActionDao userActionDao;

    @Resource
    private DistinctionDao distinctionDao;
    
    @Resource
    private InsideBuildingDao insideBuildingDao;

    @Resource
    private UniqueInsideBuildingDao uniqueInsideBuildingDao;

    @Resource
    private RoomDao roomDao;

    public UniqueInsideBuilding createUniqueInsideBuilding(
                Integer buildingId, String name, boolean temporary, Integer parent, String image,
                String flavor, String url, boolean inSprite, boolean alwaysAvailable) {
        UniqueInsideBuilding uniqueBuilding = new UniqueInsideBuilding(buildingId, name, temporary, parent, image, flavor, url, inSprite, alwaysAvailable);
        uniqueInsideBuildingDao.save(uniqueBuilding);
        return uniqueBuilding;
    }

    public UserAction createUserAction(final City city, final User user, final Zone zone, final UpdateAction action, final Date updated) {
        UserAction userAction = new UserAction();
        userAction.setCity(city);
        userAction.setUser(user);
        userAction.setZone(zone);
        userAction.setAction(action);
        userAction.setUpdated(updated);
        userActionDao.save(userAction);
        return userAction;
    }

    public City createCity(int id, String name) {
        City city = new CityBuilder()
                .setId(id)
                .setName(name)
                .toCity();
        cityDao.saveCity(city);
        return cityDao.findCity(id);
    }

    public User createUser(String name, UserKey key, Integer id) {
        return createUser(name, key, id, true, false, false, null);
    }
    public User createUser(String name, UserKey key) {
        return createUser(name, key, true, false, false);
    }
    public User createUser(String name, UserKey key, Integer id, boolean secure, boolean shunned, boolean banned, City city) {
        User user = new User();
        user.setName(name);
        user.setKey(key);
        user.setGameId(id);
        user.setSecure(secure);
        user.setShunned(shunned);
        user.setBanned(banned);
        user.setCity(city);
        userDao.save(user);
        return userDao.find(user.getKey());
    }
    public User createUser(String name, UserKey key, boolean secure, boolean shunned, boolean banned, City city) {
        return createUser(name, key, null, secure, shunned, banned, city);
    }
    public User createUser(String name, UserKey key, boolean secure, boolean shunned, boolean banned) {
        return createUser(name, key, secure, shunned, banned, null);
    }

    public Zone createZone(City city, int x, int y) {
        Zone zone = Zone.createZone(x, y);
        zone.setCity(city);
        zoneDao.saveZone(zone);
        return zoneDao.findZone(city.getId(), x, y);
    }

    public void createInsideBuilding(City city, int buildingId, InsideBuildingStatus status) {
        InsideBuilding insideBuilding = new InsideBuilding();
        insideBuilding.setCity(city);
        insideBuilding.setBuildingId(buildingId);
        insideBuilding.setStatus(status);
        insideBuildingDao.save(insideBuilding);
    }

    public Distinction createDistinction(User user, String name, int amount) {
        Distinction distinction = new Distinction();
        distinction.setUniqueDistinctionId(1);
        distinction.setUser(user);
        distinction.setName(name);
        distinction.setAmount(amount);
        distinctionDao.saveDistinction(distinction);
        return distinction;
    }

    public Distinction createDistinction(User user, String name) {
        return createDistinction(user, name, 1);
    }

    public Room createRoom(Zone zone, int x, int y, boolean west, boolean north, boolean east, boolean south, Door door, Key key) {
        Room room = new Room(zone, x, y);
        room.setCorridorWest(west);
        room.setCorridorNorth(north);
        room.setCorridorEast(east);
        room.setCorridorSouth(south);
        room.setDoor(door);
        room.setKey(key);
        roomDao.saveRoom(room);
        return room;
    }
}
