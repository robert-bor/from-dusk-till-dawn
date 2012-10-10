package nl.d2n.model;

import com.google.gson.annotations.Expose;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "up")
@XmlAccessorType(XmlAccessType.FIELD)
public class TownUpgrade {

    @Expose
    @XmlAttribute
    private String name;

    @Expose
    @XmlAttribute
    private int level;

    @Expose
    @XmlAttribute
    private int buildingId;

    @Expose
    @XmlValue
    private String description;

    public String getName() { return this.name; }
    public int getLevel() { return this.level; }
    public int getBuildingId() { return this.buildingId; }
    public String getDescription() { return this.description; }
}
