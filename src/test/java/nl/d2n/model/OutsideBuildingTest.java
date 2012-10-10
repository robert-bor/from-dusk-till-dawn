package nl.d2n.model;

import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class OutsideBuildingTest {

    @Test
    public void unmarshallXmlSnippet() throws JAXBException {
        String xml = "<building name=\"Garden Shed\" type=\"44\" dig=\"0\">In the middle of a completely derelict square, you find a sizeable garden shed. The door gives way easilty, revealing a vast space full of shelf units and all kinds of tools.</building>";
        OutsideBuilding building = (OutsideBuilding) XmlToObjectConverter.convertXmlToObject(xml, OutsideBuilding.class);
        building.setUrl("http://somewiki/garden_shed");

        assertEquals("Garden Shed", building.getName());
        assertEquals(0, building.getDig());
        assertEquals(44, building.getType());
        assertTrue("Flavor text must start with given text", building.getFlavor().startsWith("In the middle"));
        assertEquals("http://somewiki/garden_shed", building.getUrl());
    }
}
