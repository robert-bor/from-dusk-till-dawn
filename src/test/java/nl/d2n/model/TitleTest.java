package nl.d2n.model;

import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;

public class TitleTest {

    @Test
    public void xmlSnippet() throws JAXBException {
        String xml = "<title name=\"Community Spirit\"/>";
        Title title = (Title) XmlToObjectConverter.convertXmlToObject(xml, Title.class);
        assertEquals("Community Spirit", title.getName());
    }
}
