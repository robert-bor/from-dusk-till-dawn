package nl.d2n.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class UniqueItemTest {

    @Test
    public void constructUniqueItem() {
        UniqueItem item = new UniqueItem();
        item.setId(13);
        item.setName("Flesh Tomato");
        item.setBreakable(true);
        item.setImage("tomato");
        item.setCategory(ItemCategory.FOOD);
        assertEquals((Integer) 13, item.getId());
        assertEquals(true, item.isBreakable());
        assertEquals("tomato", item.getImage());
    }

    @Test
    public void getItems() {
        UniqueItem item = new UniqueItem();
        item.setId(13);
        item.setName("Flesh Tomato");
        item.setBreakable(true);
        item.setImage("tomato");
        item.setCategory(ItemCategory.FOOD);
        assertEquals(2, item.getItems().size());
        item.setBreakable(false);
        assertEquals(1, item.getItems().size());
    }

    @Test
    public void nonEqualId() {
        UniqueItem uniqueItem = createUniqueItem(13, "Tomato", ItemCategory.FOOD, "tomato.png", false);
        Item item = createItem(47, "Something else", ItemCategory.DEFENSES, "sth.png", true);
        assertFalse(uniqueItem.mustBeOverwrittenBy(item));
    }

    @Test
    public void nonEqualName() {
        UniqueItem uniqueItem = createUniqueItem(13, "Tomato", ItemCategory.FOOD, "tomato.png", false);
        Item item = createItem(13, "Flesh Tomato", ItemCategory.FOOD, "tomato.png", false);
        assertTrue(uniqueItem.mustBeOverwrittenBy(item));
    }

    @Test
    public void canBeBroken() {
        UniqueItem uniqueItem = createUniqueItem(13, "Tomato", ItemCategory.FOOD, "tomato.png", false);
        Item item = createItem(13, "Flesh Tomato", ItemCategory.FOOD, "tomato.png", true);
        assertTrue(uniqueItem.mustBeOverwrittenBy(item));
    }

    @Test
    public void canBeBrokenButInstanceIsNot() {
        UniqueItem uniqueItem = createUniqueItem(13, "Tomato", ItemCategory.FOOD, "tomato.png", true);
        Item item = createItem(13, "Tomato", ItemCategory.FOOD, "tomato.png", false);
        assertFalse(uniqueItem.mustBeOverwrittenBy(item));
    }

    @Test
    public void nonEqualImage() {
        UniqueItem uniqueItem = createUniqueItem(13, "Tomato", ItemCategory.FOOD, "tomato.png", false);
        Item item = createItem(13, "Flesh Tomato", ItemCategory.FOOD, "tomato_big.png", false);
        assertTrue(uniqueItem.mustBeOverwrittenBy(item));
    }

    @Test
    public void nonEqualCategory() {
        UniqueItem uniqueItem = createUniqueItem(13, "Tomato", ItemCategory.FOOD, "tomato.png", false);
        Item item = createItem(13, "Flesh Tomato", ItemCategory.DEFENSES, "tomato.png", false);
        assertTrue(uniqueItem.mustBeOverwrittenBy(item));
    }

    @Test
    public void fullyEquals() {
        UniqueItem uniqueItem = createUniqueItem(13, "Tomato", ItemCategory.FOOD, "tomato.png", true);
        Item item = createItem(13, "Tomato", ItemCategory.FOOD, "tomato.png", true);
        assertFalse(uniqueItem.mustBeOverwrittenBy(item));
    }

    @Test
    public void differentImage() {
        UniqueItem uniqueItem = createUniqueItem(13, "Tomato", ItemCategory.FOOD, "tomato.png", true);
        uniqueItem.setInSprite(true);
        Item item = createItem(13, "Tomato", ItemCategory.FOOD, "tomatoes.png", true);
        uniqueItem = UniqueItem.createFromItem(item, uniqueItem);
        assertEquals(false, uniqueItem.isInSprite());
    }

    @Test
    public void sameImage() {
        UniqueItem uniqueItem = createUniqueItem(13, "Tomato", ItemCategory.FOOD, "tomato.png", true);
        uniqueItem.setInSprite(true);
        Item item = createItem(13, "Tomatoes", ItemCategory.FOOD, "tomato.png", true);
        uniqueItem = UniqueItem.createFromItem(item, uniqueItem);
        assertEquals(true, uniqueItem.isInSprite());
    }

    protected Item createItem(int id, String name, ItemCategory category, String image, boolean broken) {
        return new Item(id, name, category, image, broken, false, false, false);
    }

    protected UniqueItem createUniqueItem(int id, String name, ItemCategory category, String image, boolean breakable) {
        return new UniqueItem(id, name, category, image, breakable, false, false, false);
    }
}
