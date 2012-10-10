package nl.d2n.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class BankTest {

    @Test
    public void severalItemsInBank() {
        List<Item> items = new ArrayList<Item>();
        items.add(createItem("Alpha", ItemCategory.ARMOURY));
        items.add(createItem("Beta", ItemCategory.DEFENSES));
        items.add(createItem("Gamma", ItemCategory.DEFENSES));
        items.add(createItem("Delta", ItemCategory.FOOD));
        items.add(createItem("Epsilon", ItemCategory.FURNITURE));
        items.add(createItem("Phi", ItemCategory.DEFENSES));
        items.add(createItem("Omega", ItemCategory.ARMOURY));
        Bank bank = new Bank(items);
        assertEquals(ItemCategory.ARMOURY.getName(), bank.getBank().get(1).category);
        assertEquals(2, bank.getBank().get(1).items.size());
        assertEquals(ItemCategory.DEFENSES.getName(), bank.getBank().get(3).category);
        assertEquals(3, bank.getBank().get(3).items.size());
        assertEquals(ItemCategory.FOOD.getName(), bank.getBank().get(4).category);
        assertEquals(1, bank.getBank().get(4).items.size());
        assertEquals(ItemCategory.FURNITURE.getName(), bank.getBank().get(5).category);
        assertEquals(1, bank.getBank().get(5).items.size());
    }

    public Item createItem(String name, ItemCategory cat) {
        Item item = new Item();
        item.setName(name);
        item.setCategory(cat);
        return item;
    }
}
