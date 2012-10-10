package nl.d2n.dao;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.*;
import nl.d2n.model.builder.CityBuilder;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static nl.d2n.dao.ZoneDaoTest.createZone;

public class ItemDaoTest extends SpringContextTestCase {

    @Resource
    private CityDao cityDao;

    @Resource
    private ZoneDao zoneDao;

    @Resource
    private ItemDao itemDao;

    @Test
    public void saveItem() {
        City city = createCity(13449, "Cliffe of Tricks");
        Zone zone = saveZone(createZone(city, -10, 5, true, true, true, "Rolly's Rotor House", CampingTopology.L3_MINIMAL, 19));
        Item item = createItem(zone, "Beer Fridge", "fridge", 2, true, 117, ItemCategory.FURNITURE);
        itemDao.saveItem(item);
        zone = zoneDao.findZone(13449, -10, 5);
        List<Item> items = zone.getItems();
        assertNotNull(items);
        assertEquals(1, items.size());
        Item fetchedItem = items.get(0);
        assertEquals(2, fetchedItem.getAmount());
        assertEquals(true, fetchedItem.isBroken());
        assertEquals(117, fetchedItem.getD2nItemId());
        assertEquals(null, fetchedItem.getCategory());
        assertEquals(null, fetchedItem.getName());
        assertEquals(null, fetchedItem.getImage());
    }

    @Test
    public void saveItems() {
        City city = createCity(13449, "Cliffe of Tricks");
        Zone zone = saveZone(createZone(city, -10, 5, true, true, true, "Rolly's Rotor House", CampingTopology.UNKNOWN, 19));
        itemDao.saveItem(createItem(zone, "Beer Fridge", "fridge", 2, true, 117, ItemCategory.FURNITURE));
        itemDao.saveItem(createItem(zone, "Penknife", "knife", 1, false, 89, ItemCategory.ARMOURY));
        itemDao.saveItem(createItem(zone, "Jaffa Cake", "jaffa", 1, false, 142, ItemCategory.FOOD));
        itemDao.saveItem(createItem(zone, "Twinoid 800mg", "twinoid", 1, false, 131, ItemCategory.FURNITURE));
        zone = zoneDao.findZone(13449, -10, 5);
        List<Item> items = zone.getItems();
        assertNotNull(items);
        assertEquals(4, items.size());
    }

    @Test
    public void saveDeleteSaveItems() {
        City city = createCity(13449, "Cliffe of Tricks");
        Zone zone = createZone(city, -10, 5, true, true, true, "Rolly's Rotor House", CampingTopology.UNKNOWN, 19);
        zone.addItem(createItem(zone, "Beer Fridge", "fridge", 2, true, 117, ItemCategory.FURNITURE));
        zone.addItem(createItem(zone, "Penknife", "knife", 1, false, 89, ItemCategory.ARMOURY));
        zone.addItem(createItem(zone, "Jaffa Cake", "jaffa", 1, false, 142, ItemCategory.FOOD));
        zone.addItem(createItem(zone, "Twinoid 800mg", "twinoid", 1, false, 131, ItemCategory.FURNITURE));
        zone = saveZone(zone);
        assertEquals(4, zone.getItems().size());
        zoneDao.deleteItemsInZone(zone.getId());
        zone.clearItems();
        zone = saveZone(zone);
        assertEquals(0, zone.getItems().size());
        zone.addItem(createItem(zone, "Beer Fridge", "fridge", 2, true, 117, ItemCategory.FURNITURE));
        zone.addItem(createItem(zone, "Penknife", "knife", 1, false, 89, ItemCategory.ARMOURY));
        zone = saveZone(zone);
        assertEquals(2, zone.getItems().size());
    }

    protected Item createItem(Zone zone, String name, String image, int amount, boolean broken, int d2nItemId, ItemCategory category) {
        Item item = new Item();
        item.setZone(zone);
        item.setName(name);
        item.setImage(image);
        item.setAmount(amount);
        item.setBroken(broken);
        item.setD2nItemId(d2nItemId);
        item.setCategory(category);
        return item;
    }
    protected Zone saveZone(Zone zone) {
        zoneDao.saveZone(zone);
        return zoneDao.findZone(zone.getCity().getId(), zone.getX(), zone.getY());
    }

    public City createCity(int id, String name) {
        City city = new CityBuilder()
                .setId(id)
                .setName(name)
                .toCity();
        cityDao.saveCity(city);
        return cityDao.findCity(id);
    }

}
