package nl.d2n.reader;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.Item;
import nl.d2n.model.OutsideBuilding;
import nl.d2n.util.XmlToObjectConverter;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.Assert.assertEquals;

public class XmlReaderTest extends SpringContextTestCase {

    @Autowired
    private XmlReader xmlReader;

    @Test
    public void utf8Building() throws Exception {
        String xml = "<building name=\"Motel 666? Dusk È\" type=\"51\" dig=\"0\"><![CDATA[You ask yourself what kind of person would enjoy spending a night  in a shabby hole like this motel. No doubt some sleazy sales rep whose dodgy past will catch up with him sooner or later. I really must check out room 215, you think to yourself, without really knowing why.]]></building>";
        OutsideBuilding building = (OutsideBuilding) XmlToObjectConverter.convertXmlToObject(xmlReader.readDocumentFromString(xml), OutsideBuilding.class);
        assertEquals("Motel 666? Dusk È", building.getName());
    }
    @Test
    public void utf8Item() throws Exception {
        String xml = "<item name=\"Ç Pine Fresh È Smoke Bomb\" count=\"1\" id=\"299\" cat=\"Misc\" img=\"smoke_bomb\" broken=\"0\"/>";
        Item item = (Item) XmlToObjectConverter.convertXmlToObject(xmlReader.readDocumentFromString(xml), Item.class);
        assertEquals("Ç Pine Fresh È Smoke Bomb" , item.getName());
    }
}
