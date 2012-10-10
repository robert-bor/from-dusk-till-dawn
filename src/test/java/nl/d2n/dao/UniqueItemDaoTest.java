package nl.d2n.dao;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.UniqueItem;
import nl.d2n.model.ItemCategory;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class UniqueItemDaoTest extends SpringContextTestCase {

    @Test
    public void saveItem() {
        UniqueItem item = new UniqueItem();
        item.setId(13);
        item.setName("Flesh Tomato");
        item.setBreakable(true);
        item.setImage("tomato");
        item.setCategory(ItemCategory.FOOD);
        uniqueItemDao.save(item);
        List<UniqueItem> items = uniqueItemDao.findUniqueItems();
        assertEquals(1, items.size());
        item = items.get(0);
        assertEquals((Integer)13, item.getId());
        assertEquals(true, item.isBreakable());
        assertEquals("tomato", item.getImage());
    }

}
