package nl.d2n.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"JpaDataSourceORMInspection"})
@Entity
@Table(name = "unique_items")
public class UniqueItem implements ImageBearer {

    @Expose
    @Id
    @Column(name = "id")
    private Integer id;

    @Expose
    @Column(name = "image")
    private String image;

    @Expose
    @Column(name = "name")
    private String name;

    @Expose
    @Column(name = "breakable")
    private boolean breakable;

    @Expose
    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private ItemCategory category;

    @Expose
    @Column(name = "in_sprite")
    private boolean inSprite;

    @Expose
    @Column(name = "poisoned")
    private boolean poisoned;

    @Expose
    @Column(name = "preset_bp")
    private boolean presetBp;

    public UniqueItem() {}
    public UniqueItem(Integer id, String name, ItemCategory category, String image, boolean breakable,
                      boolean inSprite, boolean poisoned, boolean presetBp) {
        setId(id);
        setName(name);
        setCategory(category);
        setImage(image);
        setBreakable(breakable);
        setInSprite(inSprite);
        setPoisoned(poisoned);
        setPresetBp(presetBp);
    }

    public String getImage() { return image; }
    public ItemCategory getCategory() { return category; }
    public Integer getId() { return this.id; }
    public String getName() { return this.name; }
    public boolean isBreakable() { return breakable; }
    public boolean isInSprite() { return inSprite; }
    public boolean isPoisoned() { return poisoned; }
    public boolean isPresetBp() { return presetBp; }

    public void setBreakable(boolean breakable) { this.breakable = breakable; }
    public void setCategory(ItemCategory category) { this.category = category; }
    public void setId(Integer id) { this.id = id; }
    public void setImage(String image) { this.image = image; }
    public void setName(String name) { this.name = name; }
    public void setInSprite(boolean inSprite) { this.inSprite = inSprite; }
    public void setPoisoned(boolean poisoned) { this.poisoned = poisoned; }
    public void setPresetBp(boolean presetBp) { this.presetBp = presetBp; }

    public List<Item> getItems() {
        List<Item> items = new ArrayList<Item>();
        items.add(new Item(getId(), getName(), getCategory(), getImage(), false, isInSprite(), isPoisoned(), isPresetBp()));
        if (isBreakable()) {
            items.add(new Item(getId(), getName(), getCategory(), getImage(), true, isInSprite(), isPoisoned(), isPresetBp()));
        }
        return items;
    }

    public boolean mustBeOverwrittenBy(Item item) {
        // If IDs are not the same, they shouldn't be here
        if (getId() != item.getD2nItemId()) {
            return false;
        }
        if (!getName().equals(item.getName())) {
            return true;
        }
        if (getCategory() != item.getCategory()) {
            return true;
        }
        if (!getImage().equals(item.getImage())) {
            return true;
        }
        if (!isBreakable() && item.isBroken()) {
            return true;
        }
        return false;
    }

    static public UniqueItem createFromItem(Item item, UniqueItem uniqueItem) {
        return new UniqueItem(
                item.getD2nItemId(),
                item.getName(),
                item.getCategory(),
                item.getImage(),
                item.isBroken(),
                uniqueItem != null && uniqueItem.isInSprite() && item.getImage().equals(uniqueItem.getImage()),
                uniqueItem != null && uniqueItem.isPoisoned(),
                uniqueItem != null && uniqueItem.isPresetBp()
        );
    }

}
