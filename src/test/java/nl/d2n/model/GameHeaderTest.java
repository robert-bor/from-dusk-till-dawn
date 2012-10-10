package nl.d2n.model;

import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class GameHeaderTest {

    @Test
    public void unmarshallXmlSnippet() throws JAXBException {
        String xml =
            "<headers link=\"http://www.die2nite.com/xml\" iconurl=\"http://data.die2nite.com/gfx/icons/\" secure=\"0\" author=\"Motion Twin &lt;http://www.motion-twin.com&gt;\" "+
                     "language=\"en\" version=\"2.171\" avatarurl=\"http://imgup.motion-twin.com/\" generator=\"haxe\">" +
                "<game days=\"3\" quarantine=\"0\" datetime=\"2011-07-24 10:23:48\" id=\"13449\"/>" +
            "</headers>";
        GameHeader gameHeader = (GameHeader)XmlToObjectConverter.convertXmlToObject(xml, GameHeader.class);

        assertEquals("http://www.die2nite.com/xml" , gameHeader.getLink());
        assertEquals("http://data.die2nite.com/gfx/icons/" , gameHeader.getIconUrl());
        assertEquals(false , gameHeader.isSecure());
        assertEquals("Motion Twin <http://www.motion-twin.com>" , gameHeader.getAuthor());
        assertEquals("en" , gameHeader.getLanguage());
        assertEquals("2.171" , gameHeader.getVersion());
        assertEquals("http://imgup.motion-twin.com/" , gameHeader.getAvatarUrl());
        assertEquals("haxe" , gameHeader.getGenerator());
        assertNotNull(gameHeader.getGame());
        assertEquals(3, gameHeader.getGame().getDay());
    }
}
