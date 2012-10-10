package nl.d2n.service;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.*;
import nl.d2n.util.ClassCreator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import static junit.framework.Assert.*;

public class RuinServiceTest extends SpringContextTestCase {

    @Autowired
    private RuinService ruinService;

    @Resource
    private ClassCreator classCreator;

    @Before
    public void init() {
        classCreator.createUser("Bob", new UserKey("cafebabe"));
        City city = classCreator.createCity(43, "Boulevard of Broken Dreams");
        classCreator.createZone(city, 1, 0);
    }

    @Test
    public void cityDoesNotExist() {
        try {
            ruinService.getRuin(new UserKey("cafebabe"), 42, 1, 1);
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.CITY_DOES_NOT_EXIST, err.getError());
        }
    }

    @Test
    public void newRuinZoneDoesNotExist() throws ApplicationException {
        Ruin ruin = ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 1);
        assertNotNull(ruin.getRoom(0, 0));
    }

    @Test
    public void addRoomInNonExistingZone() throws ApplicationException {
        try {
            ruinService.addRoom(new UserKey("cafebabe"), 43, 13, 13, 1, 0);
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.ZONE_DOES_NOT_EXIST, err.getError());
        }
    }

    @Test
    public void addRoomAgain() throws ApplicationException {
        ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 0); // this generates the entrance
        ruinService.addRoom(new UserKey("cafebabe"), 43, 1, 0, 1, 0);
        try {
            ruinService.addRoom(new UserKey("cafebabe"), 43, 1, 0, 1, 0);
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.ROOM_ALREADY_EXISTS, err.getError());
        }
    }

    @Test
    public void deleteEntrance() throws ApplicationException {
        ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 0); // this generates the entrance
        try {
            ruinService.deleteRoom(new UserKey("cafebabe"), 43, 1, 0, 0, 0);
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.ROOM_MAY_NOT_BE_DELETED, err.getError());
        }
    }

    @Test
    public void deleteRoom() throws ApplicationException {
        ruinService.getRuin   (new UserKey("cafebabe"), 43, 1, 0); // this generates the entrance
        ruinService.addRoom   (new UserKey("cafebabe"), 43, 1, 0, 1, 0);
        ruinService.deleteRoom(new UserKey("cafebabe"), 43, 1, 0, 1, 0);
        assertNull(ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 0).getRoom(1, 0));
    }

    @Test
    public void deleteRoomWithDependentRooms() throws ApplicationException {
        // +---+---+---+
        // | E | X |   |
        // +---+---+---+
        ruinService.getRuin   (new UserKey("cafebabe"), 43, 1, 0); // this generates the entrance
        ruinService.addRoom(new UserKey("cafebabe"), 43, 1, 0, 1, 0);
        ruinService.addRoom   (new UserKey("cafebabe"), 43, 1, 0, 2, 0);
        try {
            ruinService.deleteRoom(new UserKey("cafebabe"), 43, 1, 0, 1, 0);
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.ROOM_MAY_NOT_BE_DELETED, err.getError());
        }
    }

    @Test
    public void addCorridor() throws ApplicationException {
        ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 0); // this generates the entrance
        ruinService.addCorridor(new UserKey("cafebabe"), 43, 1, 0, 0, 0, RuinDirection.EAST);
        Room room = ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 0).getRoom(0, 0);
        assertTrue(room.getCorridors().get(RuinDirection.EAST));
    }

    @Test
    public void deleteCorridorThatHasAdjoiningRoom() throws ApplicationException {
        ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 0); // this generates the entrance
        ruinService.addRoom(new UserKey("cafebabe"), 43, 1, 0, 1, 0);
        try {
            ruinService.deleteCorridor(new UserKey("cafebabe"), 43, 1, 0, 0, 0, RuinDirection.EAST);
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.CORRIDOR_MAY_NOT_BE_REMOVED, err.getError());
        }
    }

    @Test
    public void deleteCorridor() throws ApplicationException {
        ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 0); // this generates the entrance
        ruinService.addCorridor(new UserKey("cafebabe"), 43, 1, 0, 0, 0, RuinDirection.SOUTH);
        ruinService.deleteCorridor(new UserKey("cafebabe"), 43, 1, 0, 0, 0, RuinDirection.SOUTH);
        Room room = ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 0).getRoom(0, 0);
        assertFalse(room.getCorridors().get(RuinDirection.SOUTH));
    }

    @Test
    public void setDoor() throws ApplicationException {
        ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 0); // this generates the entrance
        ruinService.addRoom(new UserKey("cafebabe"), 43, 1, 0, 1, 0);
        ruinService.setDoor(new UserKey("cafebabe"), 43, 1, 0, 1, 0, Door.LOCKED);
        Room room = ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 0).getRoom(1, 0);
        assertEquals(Door.LOCKED, room.getDoor());
    }

    @Test
    public void setDoorInEntrance() throws ApplicationException {
        ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 0); // this generates the entrance
        try {
            ruinService.setDoor(new UserKey("cafebabe"), 43, 1, 0, 0, 0, Door.LOCKED);
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.NO_DOOR_ALLOWED_IN_ENTRANCE, err.getError());
        }
    }

    @Test
    public void setKey() throws ApplicationException {
        ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 0); // this generates the entrance
        ruinService.setKey(new UserKey("cafebabe"), 43, 1, 0, 0, 0, Key.MAGNETIC_KEY);
        Room room = ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 0).getRoom(0, 0);
        assertEquals(Key.MAGNETIC_KEY, room.getKey());
    }

    @Test
    public void setZombies() throws ApplicationException {
        ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 0); // this generates the entrance
        ruinService.setZombies(new UserKey("cafebabe"), 43, 1, 0, 0, 0, 3);
        Room room = ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 0).getRoom(0, 0);
        assertEquals(3, room.getZombies());
    }

    @Test
    public void addCorridorInNonExistingRoom() throws ApplicationException {
        ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 0); // this generates the entrance
        try {
            ruinService.addCorridor(new UserKey("cafebabe"), 43, 1, 0, 42, 42, RuinDirection.NORTH);
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.ROOM_DOES_NOT_EXIST, err.getError());
        }
    }

    @Test
    public void changeInNonExistingRoom() throws ApplicationException {
        ruinService.getRuin(new UserKey("cafebabe"), 43, 1, 0); // this generates the entrance
        try {
            ruinService.setZombies(new UserKey("cafebabe"), 43, 1, 0, 42, 42, 3);
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.ROOM_DOES_NOT_EXIST, err.getError());
        }
    }
}
