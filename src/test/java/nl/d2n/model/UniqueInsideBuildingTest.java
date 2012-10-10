package nl.d2n.model;

import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class UniqueInsideBuildingTest {

    @Test
    public void constructor() {
        UniqueInsideBuilding building = new UniqueInsideBuilding(1042, "Upgraded Map", true, 1050, "item_electro", "Lorem ipsum", null, false, false);
        building.setUrl("http://somewiki/upgraded_map");
        assertEquals((Integer)1042, building.getId());
        assertEquals("Upgraded Map", building.getName());
        assertEquals((Integer)1050, building.getParent());
        assertEquals("item_electro", building.getImage());
        assertEquals("Lorem ipsum", building.getFlavor());
        assertEquals("http://somewiki/upgraded_map", building.getUrl());
    }

    @Test
    public void overwrite() throws JAXBException {
        String xml = "<building name=\"Upgraded Map\" temporary=\"1\" parent=\"1050\" id=\"1042\" img=\"item_electro\">This simple electronic gadget increases the level of detail visible on the map of the World Beyond: the exact number of zombies in each zone can be clearly seen. Invaluable if you don't want to wander mindlessly into feeding time at the zombie zoo...</building>";
        InsideBuilding building = (InsideBuilding) XmlToObjectConverter.convertXmlToObject(xml, InsideBuilding.class);
        UniqueInsideBuilding uniqueBuilding = new UniqueInsideBuilding(1042, "Upgraded Map", true, 1050, "item_electro", "Lorem ipsum", null, false, false);
        assertTrue("Expects that the unique building must be overwritten because of a changed flavor text", uniqueBuilding.mustBeOverwrittenBy(building));
        uniqueBuilding = UniqueInsideBuilding.createFromInsideBuilding(building, null);
        assertEquals((Integer)1042, uniqueBuilding.getId());
        assertEquals("Upgraded Map", uniqueBuilding.getName());
        assertEquals(true, uniqueBuilding.isTemporary());
        assertEquals((Integer)1050, uniqueBuilding.getParent());
        assertEquals("item_electro", uniqueBuilding.getImage());
        assertTrue(uniqueBuilding.getFlavor().startsWith("This simple elec"));
    }

    @Test
    public void roundRobin() {
        UniqueInsideBuilding uniqueBuilding = new UniqueInsideBuilding(1042, "Upgraded Map", true, 1050, "item_electro", "Lorem ipsum", null, false, false);
        InsideBuilding building = uniqueBuilding.createBuilding();
        uniqueBuilding = UniqueInsideBuilding.createFromInsideBuilding(building, null);
        assertEquals((Integer)1042, uniqueBuilding.getId());
        assertEquals("Upgraded Map", uniqueBuilding.getName());
        assertEquals(true, uniqueBuilding.isTemporary());
        assertEquals((Integer)1050, uniqueBuilding.getParent());
        assertEquals("item_electro", uniqueBuilding.getImage());
        assertEquals("Lorem ipsum", uniqueBuilding.getFlavor());
    }

    @Test
    public void differentImage() {
        UniqueInsideBuilding uniqueBuilding = new UniqueInsideBuilding(1042, "Upgraded Map", true, 1050, "item_electro", "Lorem ipsum", null, true, false);
        InsideBuilding building = new InsideBuilding(1042, "Upgraded Map", true, 1050, "item_bavario", "Lorem ipsum", null, true, null, null, null);
        uniqueBuilding = UniqueInsideBuilding.createFromInsideBuilding(building, uniqueBuilding);
        assertEquals(false, uniqueBuilding.isInSprite());
    }

    @Test
    public void sameImage() {
        UniqueInsideBuilding uniqueBuilding = new UniqueInsideBuilding(1042, "Upgraded Map", true, 1050, "item_electro", "Lorem ipsum", null, true, false);
        InsideBuilding building = new InsideBuilding(1042, "Upgraded Map Tool", true, 1050, "item_electro", "Lorem ipsum", null, true, null, null, null);
        uniqueBuilding = UniqueInsideBuilding.createFromInsideBuilding(building, uniqueBuilding);
        assertEquals(true, uniqueBuilding.isInSprite());
    }

    @Test
    public void keepUrl() {
        UniqueInsideBuilding uniqueBuilding = new UniqueInsideBuilding(1042, "Upgraded Map", true, 1050, "item_electro", "Lorem ipsum", "http://somewiki.org", true, false);
        InsideBuilding building = new InsideBuilding(1042, "Upgraded Map Tool", true, 1050, "item_electro", "Lorem ipsum", null, true, null, null, null);
        uniqueBuilding = UniqueInsideBuilding.createFromInsideBuilding(building, uniqueBuilding);
        assertNotNull("Expected the URL to still exist", uniqueBuilding.getUrl());
    }

}
