package nl.d2n.service;

import nl.d2n.dao.UniqueItemDao;
import nl.d2n.model.Item;
import nl.d2n.model.UniqueItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class UniqueItemManager extends UniqueManagerWithImages<Integer,UniqueItem, Item> {

    @Autowired
    private UniqueItemDao uniqueItemDao;

    protected List<UniqueItem> findUniqueObjects() {
        return this.uniqueItemDao.findUniqueItems();
    }

    protected void saveUniqueObject(UniqueItem uniqueItem) {
        uniqueItemDao.save(uniqueItem);
    }

    protected Integer getKeyFromUniqueObject(UniqueItem value) {
        return value.getId();
    }

    protected Integer getKeyFromNonUniqueObject(Item value) {
        return value.getD2nItemId();
    }

    protected UniqueItem createFromNonUniqueObject(Item item, UniqueItem uniqueItem) {
        return UniqueItem.createFromItem(item, uniqueItem);
    }

    protected boolean mustBeOverwritten(UniqueItem uniqueItem, Item item) {
        return uniqueItem.mustBeOverwrittenBy(item);
    }

    protected void appendMissingFields(Item item, UniqueItem uniqueItem) {
        item.setName(uniqueItem.getName());
        item.setImage(uniqueItem.getImage());
        item.setCategory(uniqueItem.getCategory());
    }

    public UniqueItem findItem(String name) {
        for (UniqueItem uniqueItem : getMap().values()) {
            if (uniqueItem.getName().trim().equals(name)) {
                return uniqueItem;
            }
        }
        return null;
    }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<Item>();
        for (UniqueItem uniqueItem : uniqueObjects.values()) {
            items.addAll(uniqueItem.getItems());
        }
        return items;
    }
    
    public Map<String, Integer> getItemIdsByImage() {
        Map<String, Integer> itemIdsByImage = new TreeMap<String, Integer>();
        for (UniqueItem uniqueItem : uniqueObjects.values()) {
            itemIdsByImage.put(uniqueItem.getImage(), uniqueItem.getId());
        }
        return itemIdsByImage;
    }
}
