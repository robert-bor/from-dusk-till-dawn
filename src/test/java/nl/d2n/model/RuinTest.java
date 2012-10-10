package nl.d2n.model;

import org.junit.Test;

import java.math.RoundingMode;

import static junit.framework.Assert.*;
import static nl.d2n.model.RuinDirection.*;

public class RuinTest {

    @Test
    public void hasNoEntrance() {
        Ruin ruin = new Ruin();
        assertFalse(ruin.hasEntrance());
    }

    @Test
    public void addRoom() throws ApplicationException {
        Zone zone = new Zone();
        Ruin ruin = new Ruin();
        ruin.addEntrance(zone);
        ruin.addRoom(new Room(zone, 1, 0));
        assertTrue(ruin.getRoom(0, 0).getCorridors().get(EAST));
        assertTrue(ruin.getRoom(1, 0).getCorridors().get(WEST));
    }

    @Test
    public void addNonAdjoiningRoom() {
        Zone zone = new Zone();
        Ruin ruin = new Ruin();
        ruin.addEntrance(zone);
        try {
            ruin.addRoom(new Room(zone, 2, 0)); // no room 1,0 in-between
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.ROOM_HAS_NO_ADJOINING_ROOMS, err.getError());
        }
    }

    @Test
    public void checkCorridorDeletion() throws ApplicationException {
        //      0   1
        //    +---+---+
        //  0 | E |   |
        //    +---+-X-+
        // -1 |   |   |
        //    +---+---+
        // X = marks the corridor to be removed
        Zone zone = new Zone();
        Ruin ruin = new Ruin();
        ruin.addEntrance(zone);
        ruin.addRoom(new Room(zone, 1, 0));
        ruin.addRoom(new Room(zone, 0, -1));
        ruin.addRoom(new Room(zone, 1, -1));
        assertTrue("Both rooms can trace to the exit, so corridor is removable", ruin.corridorMayBeDeleted(1, 0, RuinDirection.SOUTH));
    }

    @Test
    public void checkCorridorDeletionWithDependentCorridor() throws ApplicationException {
        //      0   1   2
        //    +---+---+---+
        //  0 | E |   X   |
        //    +---+---+---+
        // X = marks the corridor to be removed
        Zone zone = new Zone();
        Ruin ruin = new Ruin();
        ruin.addEntrance(zone);
        ruin.addRoom(new Room(zone, 1, 0));
        ruin.addRoom(new Room(zone, 2, 0));
        assertFalse("The corridor is required for 2/0, so cannot be removed", ruin.corridorMayBeDeleted(1, 0, RuinDirection.EAST));
    }


    @Test
    public void deleteCorridorFromRoomDependentOnCorridor() throws ApplicationException {
        //      0   1
        //    +---+---+
        //  0 | E X   |
        //    +-/-+---+
        // -1 |   |   |
        //    +---+---+
        // / = connection severed
        // X = marks the corridor to be removed
        Zone zone = new Zone();
        Ruin ruin = new Ruin();
        ruin.addEntrance(zone);
        ruin.addRoom(new Room(zone, 1, 0));
        ruin.addRoom(new Room(zone, 0, -1));
        ruin.addRoom(new Room(zone, 1, -1));
        ruin.deleteCorridor(ruin.getRoom(0, 0), RuinDirection.SOUTH);
        try {
            ruin.deleteCorridor(ruin.getRoom(0, 0), RuinDirection.EAST);
            fail("Corridor may not be removed because 1/0, 1/-1 and 0/-1 depend on it");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.CORRIDOR_MAY_NOT_BE_REMOVED, err.getError());
        }
    }

    @Test
    public void addCorridorWhereItAlreadyExists() throws ApplicationException {
        //      0   1
        //    +---+---+
        //  0 | E |   |
        //    +---+---+
        Zone zone = new Zone();
        Ruin ruin = new Ruin();
        ruin.addEntrance(zone);
        ruin.addRoom(new Room(zone, 1, 0));
        try {
            ruin.addCorridor(ruin.getRoom(0, 0), RuinDirection.EAST);
            fail("Corridor already exists");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.CORRIDOR_ALREADY_EXISTS, err.getError());
        }
    }

    @Test
    public void deleteCorridorFromAdjourningRoom() throws ApplicationException {
        //      0   1
        //    +---+---+
        //  0 | E |   |
        //    +-X-+---+
        // -1 |   |   |
        //    +---+---+
        // X = marks the corridor to be removed
        Zone zone = new Zone();
        Ruin ruin = new Ruin();
        ruin.addEntrance(zone);
        ruin.addRoom(new Room(zone, 1, 0));
        ruin.addRoom(new Room(zone, 0, -1));
        ruin.addRoom(new Room(zone, 1, -1));
        assertTrue(ruin.getRoom(0, 0).getCorridors().get(RuinDirection.SOUTH));
        assertTrue(ruin.getRoom(0, -1).getCorridors().get(RuinDirection.NORTH));
        ruin.deleteCorridor(ruin.getRoom(0, 0), RuinDirection.SOUTH);
        assertFalse(ruin.getRoom(0, 0).getCorridors().get(RuinDirection.SOUTH));
        assertFalse(ruin.getRoom(0, -1).getCorridors().get(RuinDirection.NORTH));
    }

    @Test
    public void addCorridorBetweenAdjourningRooms() throws ApplicationException {
        //      0   1
        //    +---+---+
        //  0 | E |   |
        //    +-X-+---+
        // -1 |   |   |
        //    +---+---+
        // X = marks the corridor to be removed, then added again
        Zone zone = new Zone();
        Ruin ruin = new Ruin();
        ruin.addEntrance(zone);
        ruin.addRoom(new Room(zone, 1, 0));
        ruin.addRoom(new Room(zone, 0, -1));
        ruin.addRoom(new Room(zone, 1, -1));
        ruin.deleteCorridor(ruin.getRoom(0, 0), RuinDirection.SOUTH);
        ruin.addCorridor(ruin.getRoom(0, 0), RuinDirection.SOUTH);
        assertTrue(ruin.getRoom(0, 0).getCorridors().get(RuinDirection.SOUTH));
        assertTrue(ruin.getRoom(0, -1).getCorridors().get(RuinDirection.NORTH));
    }


    @Test
    public void checkRoomDeletion() throws ApplicationException {
        //      0   1   2   3   4
        //    +---+---+
        //  0 | E |   |
        //    +---+---+---+---+---+
        // -1     |   |   |   |   |
        //        +---+---+---+---+
        // -2     |   |       |   |
        //        +---+       +---+
        // -3     |   |       |   |
        //        +---+---+---+---+
        // -4     |   |   |   |   |
        //        +---+---+---+---+

        Zone zone = new Zone();
        Ruin ruin = new Ruin();
        ruin.addEntrance(zone);
        ruin.addRoom(new Room(zone, 1, 0));
        ruin.addRoom(new Room(zone, 1, -1));
        ruin.addRoom(new Room(zone, 2, -1));
        ruin.addRoom(new Room(zone, 3, -1));
        ruin.addRoom(new Room(zone, 4, -1));
        ruin.addRoom(new Room(zone, 1, -2));
        ruin.addRoom(new Room(zone, 4, -2));
        ruin.addRoom(new Room(zone, 1, -3));
        ruin.addRoom(new Room(zone, 4, -3));
        ruin.addRoom(new Room(zone, 1, -4));
        ruin.addRoom(new Room(zone, 2, -4));
        ruin.addRoom(new Room(zone, 3, -4));
        ruin.addRoom(new Room(zone, 4, -4));
        assertFalse(ruin.roomMayBeDeleted(0, 0));
        assertFalse(ruin.roomMayBeDeleted(1, 0));
        assertFalse(ruin.roomMayBeDeleted(1, -1));
        assertTrue(ruin.roomMayBeDeleted(2, -1));
        assertTrue(ruin.roomMayBeDeleted(3, -1));
        assertTrue(ruin.roomMayBeDeleted(4, -1));
        assertTrue(ruin.roomMayBeDeleted(1, -2));
        assertTrue(ruin.roomMayBeDeleted(4, -2));
        assertTrue(ruin.roomMayBeDeleted(1, -3));
        assertTrue(ruin.roomMayBeDeleted(4, -3));
        assertTrue(ruin.roomMayBeDeleted(1, -4));
        assertTrue(ruin.roomMayBeDeleted(2, -4));
        assertTrue(ruin.roomMayBeDeleted(3, -4));
        assertTrue(ruin.roomMayBeDeleted(4, -4));
    }

    @Test
    public void removeCorridor() throws ApplicationException {
        Ruin ruin = new Ruin();
        Zone zone = new Zone();
        ruin.addEntrance(zone);
        ruin.addRoom(new Room(zone, 1, 0));
        Room room = ruin.getRoom(0, 0);
        room.init();
        room.setCorridor(RuinDirection.NORTH, true);
        assertTrue(ruin.corridorMayBeDeleted(0, 0, NORTH));
        assertFalse(ruin.corridorMayBeDeleted(0, 0, EAST));
    }
}
