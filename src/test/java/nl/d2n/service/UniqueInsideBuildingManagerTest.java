package nl.d2n.service;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.City;
import nl.d2n.model.InsideBuilding;
import nl.d2n.model.InsideBuildingStatus;
import nl.d2n.model.UniqueInsideBuilding;
import nl.d2n.util.XmlToObjectConverter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class UniqueInsideBuildingManagerTest extends SpringContextTestCase {

    @Autowired
    UniqueInsideBuildingManager manager;

    @Before
    public void saveUniqueInsideBuildings() {
        uniqueInsideBuildingDao.save(new UniqueInsideBuilding(1042, "Upgraded Map", false, 1050, "item_electro", "Lorem ipsum", "http://somewiki.com/upgraded_map", false, false));
        uniqueInsideBuildingDao.save(new UniqueInsideBuilding(1050, "Watchtower", false, null, "item_tagger", "Lorem ipsum", null, false, false));
        uniqueInsideBuildingDao.save(new UniqueInsideBuilding(1062, "Portal Lock", true, null, "small_door_closed", "Lorem ipsum", null, false, false));
        manager.refresh();
    }

    @Test
    public void construct() {
        assertEquals(3, manager.getMap().size());
    }

    @Test
    public void mergeWithList() {
        List<InsideBuilding> buildings = new ArrayList<InsideBuilding>();
        buildings.add(new InsideBuilding(1042, "Upgraded Mapp", false, 1050, "item_electro", "Lorem ipsum", null, false, null, null, null)); // Change the name, keep the URL
        buildings.add(new InsideBuilding(1050, "Watchtower", false, 0, "item_tagger", "Flavored text", null, false, null, null, null));
        buildings.add(new InsideBuilding(1033, "Workshop", true, 0, "small_refine", "Lorem ipsum", null, false, null, null, null));
        buildings.add(new InsideBuilding(1075, "Building Registry", true, 1033, "item_rp_book2", "Lorem ipsum", null, false, null, null, null));
        manager.checkForExistence(buildings);
        assertEquals(5, manager.getMap().size());

        assertUniqueInsideBuilding(manager.get(1042), 1042, "Upgraded Mapp", false, 1050, "item_electro", "Lorem ipsum");
        assertEquals("http://somewiki.com/upgraded_map", manager.get(1042).getUrl());
        assertUniqueInsideBuilding(manager.get(1050), 1050, "Watchtower", false, null, "item_tagger", "Flavored text");
        assertUniqueInsideBuilding(manager.get(1033), 1033, "Workshop", true, null, "small_refine", "Lorem ipsum");
        assertUniqueInsideBuilding(manager.get(1075), 1075, "Building Registry", true, 1033, "item_rp_book2", "Lorem ipsum");
        assertUniqueInsideBuilding(manager.get(1062), 1062, "Portal Lock", true, null, "small_door_closed", "Lorem ipsum");
        assertEquals(5, uniqueInsideBuildingDao.findUniqueInsideBuildings().size());
    }

    protected void assertUniqueInsideBuilding(UniqueInsideBuilding building, Integer id, String name, boolean temporary,
                                              Integer parent, String image, String flavor) {
        assertEquals(id, building.getId());
        assertEquals(name, building.getName());
        assertEquals(temporary, building.isTemporary());
        assertEquals(parent, building.getParent());
        assertEquals(image, building.getImage());
        assertEquals(flavor, building.getFlavor());
    }

    @Test
    public void constructHierarchy() throws JAXBException {
        List<InsideBuilding> rootBuildings = createHierarchy();
        assertEquals(2, rootBuildings.get(0).getChildBuildings().size());
        assertEquals(0, rootBuildings.get(1).getChildBuildings().size());
        assertEquals(1, rootBuildings.get(2).getChildBuildings().size());
        assertEquals(2, rootBuildings.get(3).getChildBuildings().size());
        assertEquals(0, rootBuildings.get(4).getChildBuildings().size());
        assertEquals(1, rootBuildings.get(3).getChildBuildings().get(0).getChildBuildings().size());
    }

    @Test
    public void checkConstructedBuildings() throws JAXBException {
        List<InsideBuilding> rootBuildings = createHierarchy();
        String xml =
            "<city city=\"Cliffe of tricks\" door=\"1\" water=\"0\" chaos=\"0\" devast=\"0\" x=\"16\" y=\"11\">" +
                "<building name=\"Scanner\" temporary=\"0\"   parent=\"1050\" id=\"1035\" img=\"item_tagger\"></building>" +
                "<building name=\"Watchtower\" temporary=\"0\"                id=\"1050\" img=\"item_tagger\"></building>" +
                "<building name=\"Predictor\" temporary=\"0\" parent=\"1035\" id=\"1064\" img=\"item_tagger\"></building>" +
            "</city>";
        City city = (City) XmlToObjectConverter.convertXmlToObject(xml, City.class);
        InsideBuilding.setConstructedBuildingStatus(rootBuildings, city.getBuildings(), InsideBuildingStatus.CONSTRUCTED, false);
        assertEquals(InsideBuildingStatus.NOT_AVAILABLE, rootBuildings.get(0).getStatus());
        assertEquals(InsideBuildingStatus.CONSTRUCTED, rootBuildings.get(3).getStatus());
        assertEquals(InsideBuildingStatus.CONSTRUCTED, rootBuildings.get(3).getChildBuildings().get(0).getStatus());
        assertEquals(InsideBuildingStatus.CONSTRUCTED, rootBuildings.get(3).getChildBuildings().get(0).getChildBuildings().get(0).getStatus());
    }

    protected List<InsideBuilding> createHierarchy() throws JAXBException {
        String xml =
            "<city city=\"Cliffe of tricks\" door=\"1\" water=\"0\" chaos=\"0\" devast=\"0\" x=\"16\" y=\"11\">" +
                "<building name=\"Wall Upgrade v1\" temporary=\"0\"           id=\"1010\" img=\"small_wallimprove\"></building>" +
                "<building name=\"Pump\" temporary=\"0\"                      id=\"1011\" img=\"small_water\"></building>" +
                "<building name=\"Butcher\" temporary=\"0\"   parent=\"1033\" id=\"1021\" img=\"item_meat\"></building>" +
                "<building name=\"Great Pit\" temporary=\"0\" parent=\"1010\" id=\"1023\" img=\"small_gather\"></building>" +
                "<building name=\"Workshop\" temporary=\"0\"                  id=\"1033\" img=\"small_refine\"></building>" +
                "<building name=\"Scanner\" temporary=\"0\"   parent=\"1050\" id=\"1035\" img=\"item_tagger\"></building>" +
                "<building name=\"Watchtower\" temporary=\"0\"                id=\"1050\" img=\"item_tagger\"></building>" +
                "<building name=\"Portal Lock\" temporary=\"0\"               id=\"1062\" img=\"small_door_closed\"></building>" +
                "<building name=\"Predictor\" temporary=\"0\" parent=\"1035\" id=\"1064\" img=\"item_tagger\"></building>" +
                "<building name=\"Extrawall\" temporary=\"0\" parent=\"1010\" id=\"1167\" img=\"item_plate\"></building>" +
            "</city>";
        City city = (City) XmlToObjectConverter.convertXmlToObject(xml, City.class);
        manager.checkForExistence(city.getBuildings());
        return UniqueInsideBuilding.createBuildingHierarchy(manager.getMap().values());
    }
}
