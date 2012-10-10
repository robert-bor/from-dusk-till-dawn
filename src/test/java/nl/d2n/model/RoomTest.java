package nl.d2n.model;

import nl.d2n.util.GsonUtil;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static nl.d2n.model.RuinDirection.*;

public class RoomTest {

    @Test
    public void construct() {
        Room room = new Room();
        room.setCorridorWest(true);
        room.setCorridorNorth(true);
        room.setCorridorEast(true);
        room.setCorridorSouth(true);
        room.setX(14);
        room.setY(7);
        room.setZombies(1);
        room.setDoor(Door.LOCKED);
        room.setKey(Key.BUMP_KEY);
        room.init();

        assertEquals(14, room.getX());
        assertEquals(7, room.getY());
        assertEquals(1, room.getZombies());
        assertEquals(Door.LOCKED, room.getDoor());
        assertEquals(Key.BUMP_KEY, room.getKey());
        assertTrue(room.getCorridors().get(WEST));
        assertTrue(room.getCorridors().get(NORTH));
        assertTrue(room.getCorridors().get(EAST));
        assertTrue(room.getCorridors().get(SOUTH));
    }

    @Test
    public void testJson() {
        Room room = new Room();
        room.setCorridorWest(true);
        room.setCorridorNorth(true);
        room.setCorridorEast(true);
        room.setCorridorSouth(true);
        room.setX(14);
        room.setY(7);
        room.setDoor(Door.LOCKED);
        room.init();

        String jsonText = GsonUtil.objectToJson(room);
        assertTrue(jsonText.contains("corridors"));
        assertTrue(jsonText.contains("NORTH"));
        assertTrue(jsonText.contains(": 14"));
        assertTrue(jsonText.contains(": 7"));
        assertTrue(jsonText.contains("LOCKED"));
    }

    @Test
    public void setDirection() {
        Room room = new Room();
        room.setCorridor(WEST, true);
        room.setCorridor(NORTH, true);
        room.setCorridor(EAST, true);
        room.setCorridor(SOUTH, true);
        assertTrue(room.getCorridors().get(WEST));
        assertTrue(room.getCorridors().get(NORTH));
        assertTrue(room.getCorridors().get(EAST));
        assertTrue(room.getCorridors().get(SOUTH));
    }

    @Test
    public void isEntrance() {
        assertTrue(new Room(null, 0, 0).isEntrance());
        assertFalse(new Room(null, 2, 2).isEntrance());
    }
}
