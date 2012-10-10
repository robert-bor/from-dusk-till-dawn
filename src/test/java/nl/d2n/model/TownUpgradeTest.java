package nl.d2n.model;

import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class TownUpgradeTest {

    @Test
    public void unmarshallXmlSnippet() throws JAXBException {
        String xml = "<up name=\"Watchtower\" level=\"2\" buildingId=\"1050\">Every morning, all the zombies within 6 zones of the town are detected.</up>";
        TownUpgrade upgrade = (TownUpgrade) XmlToObjectConverter.convertXmlToObject(xml, TownUpgrade.class);
        assertEquals("Watchtower", upgrade.getName());
        assertEquals(2, upgrade.getLevel());
        assertEquals(1050, upgrade.getBuildingId());
        assertTrue(upgrade.getDescription().startsWith("Every morning"));
    }
}
