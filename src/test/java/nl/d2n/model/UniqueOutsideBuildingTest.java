package nl.d2n.model;

import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class UniqueOutsideBuildingTest {

    @Test
    public void constructor() {
        UniqueOutsideBuilding building = new UniqueOutsideBuilding(44, "Garden Shed", "Lorem ipsum", null);
        building.setUrl("http://somewiki/garden_shed");
        assertEquals((Integer)44, building.getId());
        assertEquals("Garden Shed", building.getName());
        assertEquals("Lorem ipsum", building.getFlavor());
        assertEquals("http://somewiki/garden_shed", building.getUrl());
    }

    @Test
    public void overwrite() throws JAXBException {
        String xml = "<building name=\"Garden Shed\" type=\"44\" dig=\"0\">In the middle of a completely derelict square, you find a sizeable garden shed. The door gives way easilty, revealing a vast space full of shelf units and all kinds of tools.</building>";
        OutsideBuilding building = (OutsideBuilding) XmlToObjectConverter.convertXmlToObject(xml, OutsideBuilding.class);
        UniqueOutsideBuilding uniqueBuilding = new UniqueOutsideBuilding(44, "Garden Shed", "Lorem ipsum set iquor", null);
        assertTrue("Expects that the unique building must be overwritten because of a changed flavor text", uniqueBuilding.mustBeOverwrittenBy(building));
        uniqueBuilding = UniqueOutsideBuilding.createFromOutsideBuilding(building, uniqueBuilding);
        assertEquals((Integer)44, uniqueBuilding.getId());
        assertEquals("Garden Shed", uniqueBuilding.getName());
        assertTrue(uniqueBuilding.getFlavor().startsWith("In the middle of"));
    }

    @Test
    public void keepUrl() {
        UniqueOutsideBuilding uniqueBuilding = new UniqueOutsideBuilding(44, "Garden Shed", "Lorem ipsum", "http://somewiki");
        OutsideBuilding building = new OutsideBuilding(44, "Garden Shed", "Lorem ipsum", null);
        uniqueBuilding = UniqueOutsideBuilding.createFromOutsideBuilding(building, uniqueBuilding);
        assertNotNull("Expected the URL to exist", uniqueBuilding.getUrl());
    }
}
