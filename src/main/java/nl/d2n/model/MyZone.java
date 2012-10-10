package nl.d2n.model;

import nl.d2n.model.Item;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MyZone {

    @XmlAttribute(name = "dried")
    private boolean depleted;

    @XmlAttribute(name = "h")
    private int humanControl;

    @XmlAttribute(name = "z")
    private int zedControl;

    @XmlElement(name = "item")
    private List<Item> items;

    public boolean isDepleted() { return this.depleted; }
    public int getHumanControl() { return this.humanControl; }
    public int getZedControl() { return this.zedControl; }
    public List<Item> getItems() { return this.items; }
}
