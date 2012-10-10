package nl.d2n.service;

import nl.d2n.dao.CityDao;
import nl.d2n.dao.RoomDao;
import nl.d2n.dao.ZoneDao;
import nl.d2n.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static nl.d2n.service.UserSecurityCheck.*;

@Service
public class RuinService {

    public static final boolean CREATE_ZONE = true;
    public static final boolean DO_NOT_CREATE_ZONE = false;

    @Autowired
    private ZoneDao zoneDao;

    @Autowired
    private CityDao cityDao;

    @Autowired
    private RoomDao roomDao;

    @Autowired
    private UserSecurityCheck userSecurityCheck;

    public Ruin getRuin(final UserKey userKey, Integer cityId, Integer x, Integer y) throws ApplicationException {

        Zone zone = checkUserAndGetZone(userKey, cityId, x, y, CREATE_ZONE);

        Ruin ruin = roomDao.getRuin(zone);

        // If the ruin is empty -- create a new entrance
        if (!ruin.hasEntrance()) {
            ruin.addEntrance(zone);
            roomDao.saveRooms(ruin);
            ruin = roomDao.getRuin(zone);
        }

        return ruin;
    }

    public void addRoom(final UserKey userKey, Integer cityId, Integer xZone, Integer yZone,
                        Integer xRoom, Integer yRoom) throws ApplicationException {

        Zone zone = checkUserAndGetZone(userKey, cityId, xZone, yZone, DO_NOT_CREATE_ZONE);

        // Make sure the room does not exist
        Room room = roomDao.findRoom(zone, xRoom, yRoom);
        if (room != null) {
            throw new ApplicationException(D2NErrorCode.ROOM_ALREADY_EXISTS);
        }

        Ruin ruin = roomDao.getRuin(zone);
        ruin.addRoom(new Room(zone, xRoom, yRoom));
        roomDao.saveRooms(ruin);
    }

    public void deleteRoom( final UserKey userKey, Integer cityId, Integer xZone, Integer yZone,
                            Integer xRoom, Integer yRoom) throws ApplicationException {

        Zone zone = checkUserAndGetZone(userKey, cityId, xZone, yZone, DO_NOT_CREATE_ZONE);

        // Get the entire ruin
        Ruin ruin = roomDao.getRuin(zone);
        if (!ruin.roomMayBeDeleted(xRoom, yRoom)) {
            throw new ApplicationException(D2NErrorCode.ROOM_MAY_NOT_BE_DELETED);
        }
        roomDao.deleteRoom(zone, xRoom, yRoom);
    }

    public void addCorridor(final UserKey userKey, Integer cityId, Integer xZone, Integer yZone,
                            Integer xRoom, Integer yRoom, RuinDirection direction) throws ApplicationException {

        Zone zone = checkUserAndGetZone(userKey, cityId, xZone, yZone, DO_NOT_CREATE_ZONE);
        Ruin ruin = roomDao.getRuin(zone);
        Room room = getRoom(zone, xRoom, yRoom);
        ruin.addCorridor(room, direction);
        roomDao.saveRooms(ruin);
    }

    public void deleteCorridor(final UserKey userKey, Integer cityId, Integer xZone, Integer yZone,
                               Integer xRoom, Integer yRoom, RuinDirection direction) throws ApplicationException {
        
        Zone zone = checkUserAndGetZone(userKey, cityId, xZone, yZone, DO_NOT_CREATE_ZONE);
        Ruin ruin = roomDao.getRuin(zone);
        Room room = getRoom(ruin, xRoom, yRoom);
        ruin.deleteCorridor(room, direction);
        roomDao.saveRooms(ruin);
    }

    public void setDoor(final UserKey userKey, Integer cityId, Integer xZone, Integer yZone,
                        Integer xRoom, Integer yRoom, Door door) throws ApplicationException {

        Zone zone = checkUserAndGetZone(userKey, cityId, xZone, yZone, DO_NOT_CREATE_ZONE);
        Room room = getRoom(zone, xRoom, yRoom);
        if (room.isEntrance()) {
            throw new ApplicationException(D2NErrorCode.NO_DOOR_ALLOWED_IN_ENTRANCE);
        }
        room.setDoor(door);
        roomDao.saveRoom(room);
    }

    public void setZombies(final UserKey userKey, Integer cityId, Integer xZone, Integer yZone,
                           Integer xRoom, Integer yRoom, Integer zombies) throws ApplicationException {

        Zone zone = checkUserAndGetZone(userKey, cityId, xZone, yZone, DO_NOT_CREATE_ZONE);
        Room room = getRoom(zone, xRoom, yRoom);
        room.setZombies(zombies);
        roomDao.saveRoom(room);
    }

    public void setKey(final UserKey userKey, Integer cityId, Integer xZone, Integer yZone,
                       Integer xRoom, Integer yRoom, Key key) throws ApplicationException {

        Zone zone = checkUserAndGetZone(userKey, cityId, xZone, yZone, DO_NOT_CREATE_ZONE);
        Room room = getRoom(zone, xRoom, yRoom);
        room.setKey(key);
        roomDao.saveRoom(room);
    }

    protected Room getRoom(Zone zone, Integer x, Integer y) throws ApplicationException {
        return getRoom(roomDao.findRoom(zone, x, y));
    }
    
    protected Room getRoom(Ruin ruin, Integer x, Integer y) throws ApplicationException {
        return getRoom(ruin.getRoom(x, y));
    }
    
    protected Room getRoom(Room room) throws ApplicationException {
        if (room == null) {
            throw new ApplicationException(D2NErrorCode.ROOM_DOES_NOT_EXIST);
        }
        return room;
    }
    
    protected Zone checkUserAndGetZone(final UserKey userKey, Integer cityId, Integer xZone,
                                       Integer yZone, boolean createZone) throws ApplicationException {
        // Check whether the user & citizen both are bona fide
        userSecurityCheck.checkUser(userKey, DO_NOT_ALLOW_FIRST_TIME, CHECK_FOR_SECURE_SETTING, CHECK_FOR_SHUNNED_SETTING);

        // Get the zone of the ruin
        return createZone ?
                findOrCreateZone(cityId, xZone, yZone) :
                findZone(cityId, xZone, yZone);
    }

    protected Zone findOrCreateZone(Integer cityId, Integer x, Integer y) throws ApplicationException {
        City city = cityDao.findCity(cityId);
        if (city == null) {
            throw new ApplicationException(D2NErrorCode.CITY_DOES_NOT_EXIST);
        }
        Zone zone = zoneDao.findOrCreateZone(city, x, y);
        if (zone.getId() == null) {
            zoneDao.saveZone(zone);
            zone = zoneDao.findZone(cityId, x, y);
        }
        return zone;
    }

    protected Zone findZone(Integer cityId, Integer x, Integer y) throws ApplicationException {
        Zone zone = zoneDao.findZone(cityId, x, y);
        if (zone == null) {
            throw new ApplicationException(D2NErrorCode.ZONE_DOES_NOT_EXIST);
        }
        return zone;
    }

}
