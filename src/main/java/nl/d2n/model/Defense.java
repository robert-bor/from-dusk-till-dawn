package nl.d2n.model;

import com.google.gson.annotations.Expose;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Defense {
    @Expose
    @XmlAttribute
    private int base;

    @Expose
    @XmlAttribute
    private int items;

    @Expose
    @XmlAttribute(name = "citizen_guardians")
    private int citizenGuardians;

    @Expose
    @XmlAttribute(name = "citizen_homes")
    private int citizenHomes;

    @Expose
    @XmlAttribute
    private int upgrades;

    @Expose
    @XmlAttribute
    private int buildings;

    @Expose
    @XmlAttribute
    private int total;

    @Expose
    @XmlAttribute(name = "itemsMul")
    private double itemsMultiplier;

    public int getBase() { return this.base; }
    public int getItems() { return this.items; }
    public int getCitizenGuardians() { return this.citizenGuardians; }
    public int getCitizenHomes() { return this.citizenHomes; }
    public int getUpgrades() { return this.upgrades; }
    public int getBuildings() { return this.buildings; }
    public int getTotal() { return this.total; }
    public double getItemsMultiplier() { return this.itemsMultiplier; }
}
