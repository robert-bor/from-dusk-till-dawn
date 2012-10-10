package nl.d2n.model;

import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class CadaverTest {

    @Test
    public void unmarshallXmlSnippet() throws JAXBException {
        String xml = "<cadaver name=\"Rich\" dtype=\"5\" id=\"1633\" day=\"1\"><msg>Forgot to come back, game just got boring after lack of content for so long so I wasn't used to paying attention.</msg></cadaver>";
        Cadaver cadaver = (Cadaver) XmlToObjectConverter.convertXmlToObject(xml, Cadaver.class);
        assertEquals("Rich", cadaver.getName());
        assertEquals(5, cadaver.getDeathType());
        assertEquals(1633, cadaver.getId());
        assertEquals(1, cadaver.getDay());
        assertTrue(cadaver.getMessage().startsWith("Forgot to come"));
    }
}
