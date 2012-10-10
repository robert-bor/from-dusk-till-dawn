package nl.d2n.model;

import com.google.gson.annotations.Expose;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"JpaDataSourceORMInspection"})
@Entity
@Table(name = "items")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {

    private static final Logger LOGGER = LoggerFactory.getLogger(Item.class);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Transient
    @XmlAttribute
    private String name;

    // http://data.die2nite.com/gfx/icons/item_[NAME].gif
    @Transient
    @XmlAttribute(name = "img")
    private String image;

    @Column(name = "amount")
    @Expose
    @XmlAttribute(name = "count")
    private int amount;

    @Column(name = "broken")
    @Expose
    @XmlAttribute
    private boolean broken;

    @Expose
    @Column(name = "d2n_item_id")
    @XmlAttribute(name = "id")
    private int d2nItemId;

    @Transient
    @Enumerated(EnumType.STRING)
    @XmlAttribute(name = "cat")
    private ItemCategory category;

    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Transient
    @Expose
    private boolean inSprite;

    @Transient
    @Expose
    private boolean poisoned;

    @Transient
    @Expose
    private boolean presetBp;

    public Item() {}
    public Item(final Integer id, final String name, final ItemCategory category, final String image,
                final boolean broken, final boolean inSprite, final boolean poisoned, final boolean presetBp) {
        setD2nItemId(id);
        setName(name);
        setCategory(category);
        setImage(image);
        setBroken(broken);
        setAmount(1);
        setInSprite(inSprite);
        setPoisoned(poisoned);
        setPresetBp(presetBp);
    }

    public int getAmount() { return amount; }
    public ItemCategory getCategory() { return category; }
    public int getD2nItemId() { return d2nItemId; }
    public String getImage() { return image; }
    public String getName() { return name; }
    public boolean isBroken() { return broken; }
    public boolean isPoisoned() { return poisoned; }
    public Zone getZone() { return zone; }
    public boolean isInSprite() { return inSprite; }
    public boolean isPresetBp() { return presetBp; }

    public void setAmount(int amount) { this.amount = amount; }
    public void setBroken(boolean broken) { this.broken = broken; }
    public void setCategory(ItemCategory category) { this.category = category; }
    public void setD2nItemId(int d2nItemId) { this.d2nItemId = d2nItemId; }
    public void setImage(String image) { this.image = image; }
    public void setName(String name) { this.name = name; }
    public void setZone(Zone zone) { this.zone = zone; }
    public void setInSprite(boolean inSprite) { this.inSprite = inSprite; }
    public void setPoisoned(boolean poisoned) { this.poisoned = poisoned; }
    public void setPresetBp(boolean presetBp) { this.presetBp = presetBp; }

    public static List<Item> convertKeysToItems(String[] itemKeys) {
        List<Item> items = new ArrayList<Item>();
        if (itemKeys == null) {
            return items;
        }
        for (String itemCompositeKey : itemKeys) {
            items.add(Item.translateItemKey(itemCompositeKey));
        }
        return items;
    }
    
    public static Item translateItemKey(String itemKey) {
        Pattern pattern = Pattern.compile("([0-9]+)([B]?)-([0-9]+)");
        Matcher matcher = pattern.matcher(itemKey);
        if (!matcher.matches()) {
            String msg = "Applying pattern "+pattern.toString()+" failed on: \""+itemKey+"\"";
            LOGGER.error(msg);
            throw new IllegalStateException(msg);
        }
        Integer id = Integer.parseInt(matcher.group(1));
        Boolean broken = "B".equals(matcher.group(2));
        Integer amount = Integer.parseInt(matcher.group(3));

        Item item = new Item();
        item.setD2nItemId(id);
        item.setAmount(amount);
        item.setBroken(broken);
        return item;
    }
}
