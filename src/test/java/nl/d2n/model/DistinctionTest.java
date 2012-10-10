package nl.d2n.model;

import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;

public class DistinctionTest {

    @Test
    public void xmlSnippet() throws JAXBException {
        String xml = "<r name=\"Zombies eliminated\" rare=\"1\" n=\"1008\" img=\"r_killz\"><title name=\"The Hitman\"/></r>";
        Distinction distinction = (Distinction) XmlToObjectConverter.convertXmlToObject(xml, Distinction.class);
        assertEquals("Zombies eliminated", distinction.getName());
        assertEquals(true, distinction.isRare());
        assertEquals(1008, distinction.getAmount());
        assertEquals("r_killz", distinction.getImage());
        assertEquals("The Hitman", distinction.getTitle().getName());
    }
}
