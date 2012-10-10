package nl.d2n.model;

import nl.d2n.model.builder.ZoneBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class MapMatrixTest {

    @Test
    public void constructMatrix() {
        MapMatrix matrix = new MapMatrix(2, 2, 1, 1, new ArrayList<Zone>(), false);
        Zone[][] zones = matrix.getMatrix();
        assertEquals(2, zones.length);
        assertEquals(2, zones[0].length);
    }

    @Test
    public void getBoundaries() {
        MapMatrix mapMatrix = new MapMatrix(13, 13, 8, 5, new ArrayList<Zone>(), false);
        assertTrue(-8 == mapMatrix.getLeft());
        assertEquals((Integer)4, mapMatrix.getRight());
        assertEquals((Integer)5, mapMatrix.getTop());
        assertTrue(-7 == mapMatrix.getBottom());
    }
    
    @Test
    public void mergeZones() {

        //    -1   0   1
        //   +---+---+---+
        // 1 | P | G |P+G|
        //   +---+---+---+
        // 0 | G | T |   |
        //   +---+---+---+
        // P = Persisted Zone
        // G = Generic Zone
        // T = Town

        List<Zone> genericZones = new ArrayList<Zone>();
        genericZones.add(createZone(1, 0, ZoneDanger.ONE_TO_THREE, -1));
        genericZones.add(createZone(2, 0, ZoneDanger.FIVE_PLUS, -1));
        genericZones.add(createZone(0, 1, ZoneDanger.NONE, -1));
        MapMatrix matrix = new MapMatrix(3, 2, 1, 1, genericZones, false);

        List<Zone> persistedZones = new ArrayList<Zone>();
        Zone zone = createZone(-1, 1, null, 13);
        UserAction userAction = new UserAction();
        userAction.setDay(1);
        userAction.setAction(UpdateAction.UPDATE_ZONE);
        zone.addLastUserAction(userAction);
        persistedZones.add(zone);
        persistedZones.add(createZone( 1, 1, null,  7));
        matrix.mergeZones(persistedZones, 1);

        Zone[][] zones = matrix.getMatrix();
        assertZone(zones[0][0], -1, 1, ZoneDanger.NINE_PLUS,   13);
        assertEquals(DiscoveryStatus.DISCOVERED_AND_VISITED_TODAY, zones[0][0].getDiscoveryStatus());
        assertZone(zones[1][0],  0, 1, ZoneDanger.ONE_TO_THREE,-1);
        assertZone(zones[2][0],  1, 1, ZoneDanger.FIVE_PLUS,   7);
        assertZone(zones[0][1], -1, 0, ZoneDanger.NONE,       -1);
        assertZone(zones[1][1],  0, 0, ZoneDanger.UNKNOWN,    -1);
        assertZone(zones[2][1],  1, 0, ZoneDanger.UNKNOWN,    -1);

        assertTrue(matrix.toJson().contains("13"));
    }

    protected Citizen createCitizen(String name, int x, int y) {
        Citizen citizen = new Citizen();
        citizen.setName(name);
        citizen.setMatrixX(x);
        citizen.setMatrixY(y);
        citizen.setOutside(true);
        return citizen;
    }

    protected void assertZone(Zone zone, int x, int y, ZoneDanger danger, int zombies) {
        assertEquals(x, zone.getX());
        assertEquals(y, zone.getY());
        assertEquals(danger, zone.getDanger());
        assertEquals(zombies, zone.getZombies());
    }

    protected Zone createZone(int x, int y, ZoneDanger danger, int zombies) {
        return new ZoneBuilder()
                .setX(x)
                .setY(y)
                .setZombies(zombies)
                .setDanger(danger)
                .toZone();
    }
}
