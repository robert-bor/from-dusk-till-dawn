package nl.d2n.model;

import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;

public class PlayedMapTest {

    @Test
    public void xmlSnippet() throws JAXBException {
        String xml = "<m name=\"Quivering sands of 1000 deaths\" season=\"2\" score=\"276\" d=\"23\" id=\"12366\" v1=\"0\"><![CDATA[Highlanders for the Win!]]></m>";
        PlayedMap playedMap = (PlayedMap)XmlToObjectConverter.convertXmlToObject(xml, PlayedMap.class);
        assertEquals((Integer)23, playedMap.getDay());
        assertEquals((Integer)12366, playedMap.getId());
        assertEquals("Highlanders for the Win!", playedMap.getMessage());
        assertEquals("Quivering sands of 1000 deaths", playedMap.getName());
        assertEquals((Integer)276, playedMap.getScore());
        assertEquals((Integer)2, playedMap.getSeason());
        assertEquals("0", playedMap.getVersion());
    }
}
