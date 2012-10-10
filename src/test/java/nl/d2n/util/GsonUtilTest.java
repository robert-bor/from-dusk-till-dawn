package nl.d2n.util;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.Item;
import nl.d2n.model.UniqueItem;
import nl.d2n.model.ZoneTag;
import nl.d2n.reader.XmlReader;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class GsonUtilTest extends SpringContextTestCase {

    @Autowired
    private XmlReader xmlReader;

    @Test
    public void itemToJson() throws Exception {
        String xml = "<item name=\"Ç Pine Fresh È Smoke Bomb\" count=\"1\" id=\"299\" cat=\"Misc\" img=\"smoke_bomb\" broken=\"0\"/>";
        UniqueItem item = UniqueItem.createFromItem((Item) XmlToObjectConverter.convertXmlToObject(xmlReader.readDocumentFromString(xml), Item.class), null);
        String json = GsonUtil.objectToJson(item);
        assertTrue(json.contains("Ç Pine Fresh È Smoke Bomb"));
    }
    
    @Test
    public void zoneTagToJson() throws Exception {
        String json = GsonUtil.objectToJson(ZoneTag.BETWEEN_5_AND_8_ZOMBIES);
        assertTrue(json.contains("serial"));
        assertTrue(json.contains("description"));
    }
}
