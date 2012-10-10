package nl.d2n.model;

import nl.d2n.model.builder.ZoneBuilder;
import nl.d2n.util.GsonUtil;
import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class ZoneTest {

    @Test
    public void constructorTest() {
        Zone zone = new ZoneBuilder()
                .setX(-10)
                .setY(5)
                .setZoneDepleted(true)
                .setBuildingDepleted(true)
                .setBluePrintRetrieved(true)
                .setScoutPeek("Rolly's Rotor House")
                .setCampingTopology(CampingTopology.L2_SHORTAGE)
                .setZombies(19)
                .toZone();
        assertEquals(-10, zone.getX());
        assertEquals(5, zone.getY());
        assertEquals(true, zone.isBluePrintRetrieved());
        assertEquals(true, zone.isZoneDepleted());
        assertEquals(true, zone.isBuildingDepleted());
        assertEquals(CampingTopology.L2_SHORTAGE, zone.getCampingTopology());
        assertEquals("Rolly's Rotor House", zone.getScoutPeek());
        assertEquals(19, zone.getZombies());
    }

    @Test
    public void unmarshallXmlSnippet() throws JAXBException {
        String xml = "<zone x=\"21\" y=\"12\" nvt=\"1\" tag=\"5\" danger=\"1\"/>";
        Zone zone = (Zone) XmlToObjectConverter.convertXmlToObject(xml, Zone.class);
        assertEquals(21, zone.getX());
        assertEquals(12, zone.getY());
        assertEquals(true, zone.getDiscoveredNotVisitedToday());
        assertEquals(ZoneTag.DEPLETED, zone.getTag());
        assertEquals(-1, zone.getZombies());
        assertEquals(ZoneDanger.ONE_TO_THREE, zone.getDanger());
    }

    @Test
    public void hereBeZombies() throws JAXBException {
        String xml = "<zone x=\"21\" y=\"12\" z=\"7\" nvt=\"1\" tag=\"5\" danger=\"1\"/>";
        Zone zone = (Zone) XmlToObjectConverter.convertXmlToObject(xml, Zone.class);
        assertEquals(7, zone.getZombies());
    }

    @Test
    public void zoneInRangeButNoUpdatedMapAvailable() throws JAXBException {
        String xml = "<zone x=\"14\" y=\"12\" nvt=\"0\" danger=\"2\"/>";
        Zone zone = (Zone) XmlToObjectConverter.convertXmlToObject(xml, Zone.class);
        assertEquals(-1, zone.getZombies());
    }

    @Test
    public void buildingInZone() throws JAXBException {
        String xml =
            "<zone x=\"14\" y=\"15\" nvt=\"0\" danger=\"3\">"+
                "<building name=\"Disused Car Park\" type=\"14\" dig=\"0\">An almost completely buried underground parking lot. An ideal venue to 'go quietly into the night' as long as nobody hears you...</building>"+
            "</zone>";
        Zone zone = (Zone) XmlToObjectConverter.convertXmlToObject(xml, Zone.class);
        assertNotNull("Expected a building to be inside the zone", zone.getBuilding());
        assertEquals(14, zone.getBuilding().getType());
        assertEquals(0, zone.getBuilding().getDig());
    }

    @Test
    public void mergeZones() {
        Item item = new Item();
        item.setName("Battery");
        List<Item> items = new ArrayList<Item>();
        items.add(item);
        Zone genericZone  = createZone(0,0,ZoneDanger.ONE_TO_THREE,-1,-1,false,false,false,false,  null, null,       null,                 null,  0);
        Zone specificZone = createZone(0,0,ZoneDanger.NONE,       2,5,true, true, true, true,  items, "Huge Well","2011-08-04 12:30:15","Fred",8 );
        genericZone.mergeZone(specificZone, 8);
        assertEquals(true, genericZone.isVisited());
        assertEquals(2, genericZone.getZombies());
        assertEquals(5, genericZone.getScoutSense());
        assertEquals(true, genericZone.isBluePrintRetrieved());
        assertEquals(true, genericZone.isBuildingDepleted());
        assertEquals(true, genericZone.isZoneDepleted());
        assertEquals(1, genericZone.getItems().size());
        assertEquals("Huge Well", genericZone.getScoutPeek());
    }

    @Test
    public void htmlTown() {
        Zone zone = createZone(0,0,ZoneDanger.UNKNOWN,3,true, true, true,  new ArrayList<Item>(), "Huge Well","2011-08-04 12:30:15","Fred",8 );
        assertEquals(2, zone.getHtmlClasses().length);
        assertEquals("town", zone.getHtmlClasses()[0]);
        assertEquals("zone", zone.getHtmlClasses()[1]);
    }

    @Test
    public void htmlNvt() {
        Zone zone = createZone(1,1,ZoneDanger.UNKNOWN,-1,true, true, true,  new ArrayList<Item>(), null,null,null,0 );
        zone.setDiscoveredNotVisitedToday(true);
        zone.setDiscoveryStatus(DiscoveryStatus.DISCOVERED_NOT_VISITED_TODAY);
        assertEquals(2, zone.getHtmlClasses().length);
        assertEquals("nvt", zone.getHtmlClasses()[0]);
        assertEquals("zone", zone.getHtmlClasses()[1]);
    }

    @Test
    public void htmlWithinRadarRange() {
        Zone zone = createZone(1,1,ZoneDanger.FIVE_PLUS,-1,true, true, true,  new ArrayList<Item>(), null,null,null,0 );
        assertEquals(2, zone.getHtmlClasses().length);
        assertEquals("danger3", zone.getHtmlClasses()[0]);
        assertEquals("zone", zone.getHtmlClasses()[1]);
    }

    @Test
    public void htmlWithBuilding() {
        Zone zone = createZone(1,1,ZoneDanger.FIVE_PLUS,-1,true, true, true,  new ArrayList<Item>(), null,null,null,0 );
        OutsideBuilding building = new OutsideBuilding();
        zone.setBuilding(building);
        assertEquals(3, zone.getHtmlClasses().length);
        assertEquals("danger3", zone.getHtmlClasses()[0]);
        assertEquals("building", zone.getHtmlClasses()[1]);
        assertEquals("zone", zone.getHtmlClasses()[2]);
    }

    @Test
    public void getSingleZoneUpdate() {
        Zone zone = createZone(1,1,ZoneDanger.FIVE_PLUS,-1,true, true, true,  new ArrayList<Item>(), null,null,null,0 );
        Map<String, Object> zoneMap = zone.getSingleZoneUpdate(null);
        assertEquals(2, ((String[])zoneMap.get("classes")).length);
        assertEquals((Integer)1, (Integer)zoneMap.get("x"));
        assertEquals((Integer)1, (Integer)zoneMap.get("y"));
        assertEquals(ZoneDanger.FIVE_PLUS, ((Zone)zoneMap.get("zoneUpdate")).getDanger());
    }

    @Test
    public void sameActionTwice() {
        Zone zone = new Zone();
        zone.addLastUserAction(new UserAction(null, null, null, null, UpdateAction.UPDATE_ZONE));
        zone.addLastUserAction(new UserAction(null, null, null, null, UpdateAction.UPDATE_ZONE));
        assertEquals(1, zone.getLastUserActions().size());
    }

    @Test
    public void multipleActions() {
        Zone zone = new Zone();
        zone.addLastUserAction(new UserAction(null, null, null, null, UpdateAction.UPDATE_ZONE));
        zone.addLastUserAction(new UserAction(null, null, null, null, UpdateAction.SAVE_ZOMBIES));
        zone.addLastUserAction(new UserAction(null, null, null, null, UpdateAction.SAVE_PEEK));
        assertEquals(3, zone.getLastUserActions().size());
    }

    @Test
    public void toJson() {
        Zone zone = new Zone();
        zone.addLastUserAction(new UserAction(null, null, null, null, UpdateAction.UPDATE_ZONE));
        String json = GsonUtil.objectToJson(zone);
        assertTrue(json.contains(UpdateAction.UPDATE_ZONE.toString()));
    }

    protected Zone createZoneFromSecureFeed(int x, int y, int zombies, boolean zoneDepleted,
                                            String updated, String updatedBy, int day) {
        City city = new City();
        city.setUpgradedMapAvailable(true);
        return new ZoneBuilder()
                .setCity(city)
                .setX(x)
                .setY(y)
                .setZombies(zombies)
                .setZoneDepleted(zoneDepleted)
                .toZone();
    }

    protected Zone createZone(int x, int y, ZoneDanger danger, int zombies, int scoutSense, boolean visited,
                              boolean bluePrintRetrieved, boolean zoneDepleted, boolean buildingDepleted,
                              List<Item> items, String scoutPeek, String updated, String updatedBy, int day) {
        return new ZoneBuilder()
                .setX(x)
                .setY(y)
                .setZoneDepleted(zoneDepleted)
                .setBuildingDepleted(buildingDepleted)
                .setBluePrintRetrieved(bluePrintRetrieved)
                .setScoutPeek(scoutPeek)
                .setZombies(zombies)
                .setScoutSense(scoutSense)
                .setDanger(danger)
                .setItems(items)
                .setVisited(visited)
                .toZone();
    }
    protected Zone createZone(int x, int y, ZoneDanger danger, int zombies, boolean bluePrintRetrieved,
                              boolean zoneDepleted, boolean buildingDepleted, List<Item> items, String scoutPeek,
                              String updated, String updatedBy, int day) {
        return createZone(x, y, danger, zombies, -1, false, bluePrintRetrieved, zoneDepleted, buildingDepleted, items,
                          scoutPeek, updated, updatedBy, day);
    }

}
