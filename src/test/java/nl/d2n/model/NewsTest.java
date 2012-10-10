package nl.d2n.model;

import nl.d2n.model.News;
import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class NewsTest {

    @Test
    public void unmarshallXmlSnippet() throws JAXBException {
        String xml =
            "<news z=\"41\" def=\"82\">" +
                "<content>" +
                    "<p>The citizens clearly decided to leave <strong>ninjathr33</strong> to their own devices in the World Beyond, but we haven't heard from them in some time. You guys are all heart...</p><p>Furthermore, one of the 40 attacking zombies from last night was found <strong>in the well</strong> I'd strongly advise you to skip your water ration today, as it'd be a great way to get an infection...</p>" +
                "</content>" +
            "</news>";
        News news = (News) XmlToObjectConverter.convertXmlToObject(xml, News.class);
        assertEquals(41, news.getZeds());
        assertEquals(82, news.getDefense());
    }
}
