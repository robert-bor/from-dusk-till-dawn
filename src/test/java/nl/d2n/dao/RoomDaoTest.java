package nl.d2n.dao;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.*;
import nl.d2n.util.ClassCreator;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static nl.d2n.model.RuinDirection.*;

public class RoomDaoTest extends SpringContextTestCase {

    @Resource
    private CityDao cityDao;

    @Resource
    private ZoneDao zoneDao;

    @Resource
    private RoomDao roomDao;

    @Resource
    private ClassCreator classCreator;

    private City city;
    private Zone zone;

    @Before
    public void init() {
        this.city = classCreator.createCity(42, "Boulevard");
        this.zone = classCreator.createZone(city, 1,1);
    }

    @Test
    public void saveRoom() {
        Room room = classCreator.createRoom(zone, 14, 7, true, true, true, true, Door.LOCKED, Key.BUMP_KEY);

        room = roomDao.findRoom(zone, 14, 7);
        assertEquals(14, room.getX());
        assertEquals(7, room.getY());
        assertEquals(Door.LOCKED, room.getDoor());
        assertEquals(Key.BUMP_KEY, room.getKey());
        assertTrue(room.getCorridors().get(WEST));
        assertTrue(room.getCorridors().get(NORTH));
        assertTrue(room.getCorridors().get(EAST));
        assertTrue(room.getCorridors().get(SOUTH));
    }

    @Test
    public void multipleRooms() {
        classCreator.createRoom(zone, 0,  0, false, false, false, false, Door.NONE, Key.UNKNOWN);
        classCreator.createRoom(zone, 0, -1, false, false, false, false, Door.NONE, Key.UNKNOWN);
        classCreator.createRoom(zone, 0, -2, false, false, false, false, Door.NONE, Key.UNKNOWN);
        classCreator.createRoom(zone, 1,  0, false, false, false, false, Door.NONE, Key.UNKNOWN);
        classCreator.createRoom(zone, 2,  0, false, false, false, false, Door.NONE, Key.UNKNOWN);
        Ruin ruin = roomDao.getRuin(zone);
        assertEquals(3, ruin.getRuin().get(0).size());
        assertEquals(1, ruin.getRuin().get(1).size());
        assertEquals(1, ruin.getRuin().get(2).size());
    }

    @Test
    public void deleteRoom() {
        classCreator.createRoom(zone, 0, 0, false, false, false, false, Door.NONE, Key.UNKNOWN);
        roomDao.deleteRoom(zone, 0, 0);
        assertNull(roomDao.findRoom(zone, 0, 0));
    }

}
