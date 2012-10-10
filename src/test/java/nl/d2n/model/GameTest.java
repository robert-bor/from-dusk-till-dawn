package nl.d2n.model;

import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;

public class GameTest {

    @Test
    public void unmarshallXmlSnippet() throws JAXBException {
        String xml = "<game days=\"1\" quarantine=\"0\" datetime=\"2011-07-22 21:00:13\" id=\"13449\"/>";
        Game game = (Game)XmlToObjectConverter.convertXmlToObject(xml, Game.class);

        assertEquals(1 , game.getDay());
        assertEquals(false , game.isQuarantined());
        assertEquals("2011-07-22 21:00:13", game.getDate());
        assertEquals(13449 , game.getId());
    }
}
