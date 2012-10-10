package nl.d2n.model;

import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class MyZoneTest {

    @Test
    public void unmarshallXmlSnippet() throws JAXBException {
        String xml = "<myZone dried=\"1\" h=\"2\" z=\"5\"/>";
        MyZone zone = (MyZone) XmlToObjectConverter.convertXmlToObject(xml, MyZone.class);
        assertEquals(true, zone.isDepleted());
        assertEquals(2, zone.getHumanControl());
        assertEquals(5, zone.getZedControl());
    }

    @Test
    public void itemsInMyZone() throws JAXBException {
        String xml =
            "<myZone dried=\"1\" h=\"2\" z=\"5\">"+
                "<item name=\"Can Opener\" count=\"1\" id=\"23\" cat=\"Weapon\" img=\"can_opener\" broken=\"1\"/>" +
                "<item name=\"Crushed Battery\" count=\"2\" id=\"214\" cat=\"Misc\" img=\"pile_broken\" broken=\"0\"/>" +
                "<item name=\"Sheet Metal (parts)\" count=\"1\" id=\"96\" cat=\"Misc\" img=\"plate_raw\" broken=\"0\"/>" +
                "<item name=\"Staff\" count=\"2\" id=\"15\" cat=\"Weapon\" img=\"staff\" broken=\"1\"/>"+
            "</myZone>";
        MyZone zone = (MyZone) XmlToObjectConverter.convertXmlToObject(xml, MyZone.class);
        assertNotNull(zone.getItems());
        assertEquals(4, zone.getItems().size());
    }
}
