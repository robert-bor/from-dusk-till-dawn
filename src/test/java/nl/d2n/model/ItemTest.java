package nl.d2n.model;

import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

public class ItemTest {

    @Test
    public void constructorTest() {
        Item item = new Item();
        item.setName("Beer Fridge");
        item.setAmount(2);
        item.setBroken(true);
        item.setCategory(ItemCategory.FURNITURE);
        item.setD2nItemId(117);
        item.setImage("fridge");
        assertEquals("Beer Fridge", item.getName());
        assertEquals(2, item.getAmount());
        assertEquals(true, item.isBroken());
        assertEquals(ItemCategory.FURNITURE, item.getCategory());
        assertEquals(117, item.getD2nItemId());
        assertEquals("fridge", item.getImage());
    }

    @Test
    public void unmarshallXmlSnippet() throws JAXBException {
        String xml = "<item name=\"Radio Cassette Player (no battery)\" count=\"6\" id=\"104\" cat=\"Misc\" img=\"radio_off\" broken=\"0\"/>";
        Item item = (Item) XmlToObjectConverter.convertXmlToObject(xml, Item.class);
        assertEquals("Radio Cassette Player (no battery)" , item.getName());
        assertEquals(6 , item.getAmount());
        assertEquals(104 , item.getD2nItemId());
        assertEquals(ItemCategory.MISCELLANEOUS , item.getCategory());
        assertEquals("radio_off" , item.getImage());
        assertEquals(false , item.isBroken());
    }

    @Test
    public void translateItemKey() {
        Item item = Item.translateItemKey("139B-87");
        assertEquals(139, item.getD2nItemId());
        assertEquals(true, item.isBroken());
        assertEquals(87, item.getAmount());
    }

    @Test
    public void convertKeysToItems() {
        String[] keys = { "3-1", "5-3", "7-5" };
        List<Item> items = Item.convertKeysToItems(keys);
        assertEquals(3, items.size());
    }

    @Test
    public void translateItemKeyWrong() {
        try {
            Item.translateItemKey("139B87");
            fail("Should have thrown an exception");
        } catch (IllegalStateException err) {
            assertTrue(err.getMessage().contains("139B87"));
        }
    }
}
