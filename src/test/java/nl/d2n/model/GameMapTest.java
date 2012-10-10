package nl.d2n.model;

import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class GameMapTest {
    @Test
    public void unmarshallXmlSnippet() throws JAXBException {
        String xml = "<map hei=\"27\" wid=\"26\"/>";
        GameMap gameMap = (GameMap) XmlToObjectConverter.convertXmlToObject(xml, GameMap.class);
        assertEquals(27, gameMap.getHeight());
        assertEquals(26, gameMap.getWidth());
    }

    @Test
    public void zonesInMap() throws JAXBException {
        String xml =
            "<map hei=\"27\" wid=\"27\">" +
                "<zone x=\"16\" y=\"3\" nvt=\"1\"/>" +
                "<zone x=\"16\" y=\"4\" nvt=\"1\"/>" +
                "<zone x=\"14\" y=\"5\" nvt=\"0\"/>"+
            "</map>";
        GameMap gameMap = (GameMap) XmlToObjectConverter.convertXmlToObject(xml, GameMap.class);
        assertNotNull("Excepted zones in the game map", gameMap.getZones());
        assertEquals(3, gameMap.getZones().size());
    }

    @Test
    public void upgradedMap() throws JAXBException {
        String xml =
            "<map hei=\"27\" wid=\"27\">" +
                "<zone x=\"3\" y=\"16\" nvt=\"0\"/>"+
                "<zone x=\"4\" y=\"16\" z=\"1\" nvt=\"0\" tag=\"5\"/>"+
                "<zone x=\"5\" y=\"16\" nvt=\"0\" tag=\"5\"/>"+
            "</map>";
        GameMap gameMap = (GameMap) XmlToObjectConverter.convertXmlToObject(xml, GameMap.class);
        assertNotNull("Excepted zones in the game map", gameMap.getZones());
        assertEquals("Expected the map to be upgraded", true, gameMap.isUpgradedMapAvailable());
    }

}
