package nl.d2n.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings({"JpaDataSourceORMInspection"})
@Entity
@Table(name = "cities")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class City {

    public static final String UPGRADED_MAP = "Upgraded Map";

    @Id
    @Column(name = "id")
    private Integer id;

    @Expose
    @Column(name = "name")
    @XmlAttribute(name = "city")
    private String name;

    @XmlTransient
    @Column(name = "width")
    private Integer width = 0;

    @XmlTransient
    @Column(name = "height")
    private Integer height = 0;

    @XmlTransient
    @Column(name = "left_max")
    private Integer left = 0;

    @XmlTransient
    @Column(name = "right_max")
    private Integer right = 0;

    @XmlTransient
    @Column(name = "top_max")
    private Integer top = 0;

    @XmlTransient
    @Column(name = "bottom_max")
    private Integer bottom = 0;

    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Zone> zones;

    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<UserAction> userActions;

    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<InsideBuilding> insideBuildings;

    @Expose
    @Transient
    @XmlElement
    private Defense defense;

    @Transient
    @XmlElement(name = "building")
    private List<InsideBuilding> buildings;

    @Expose
    @Transient
    @XmlAttribute(name = "door")
    private boolean doorOpen;

    @Expose
    @Transient
    @XmlAttribute
    private int water;

    @Expose
    @XmlAttribute(name = "hard")
    @Column(name = "hardcore")
    private boolean hard;

    @Expose
    @Transient
    @XmlAttribute(name = "chaos")
    private boolean chaosMode;

    @Expose
    @Transient
    @XmlAttribute(name = "devast")
    private boolean devastated;

    @Transient
    @XmlAttribute
    private int x;

    @Transient
    @XmlAttribute
    private int y;

    @Transient
    @XmlElement
    private News news;

    @Expose
    @Transient
    private boolean upgradedMapAvailable = false;

    public String getName() { return this.name; }
    public Integer getId() { return this.id; }
    public Defense getDefense() { return this.defense; }
    public List<InsideBuilding> getBuildings() { return this.buildings; }
    public boolean isDoorOpen() { return this.doorOpen; }
    public int getWater() { return this.water; }
    public boolean isChaosMode() { return this.chaosMode; }
    public boolean isDevastated() { return this.devastated; }
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public News getNews() { return this.news; }
    public boolean isUpgradedMapAvailable() { return this.upgradedMapAvailable; }
    public boolean isHard() { return this.hard; }
    public Integer getWidth() { return this.width; }
    public Integer getHeight() { return this.height; }
    public Integer getLeft() { return this.left; }
    public Integer getRight() { return this.right; }
    public Integer getTop() { return this.top;}
    public Integer getBottom() { return this.bottom; }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setUpgradedMapAvailable(boolean upgradedMapAvailable) { this.upgradedMapAvailable = upgradedMapAvailable; }
    public void setWidth(Integer width) { this.width = width; }
    public void setHeight(Integer height) { this.height = height; }
    public void setLeft(Integer left) { this.left = left; }
    public void setRight(Integer right) { this.right = right; }
    public void setTop(Integer top) { this.top = top; }
    public void setBottom(Integer bottom) { this.bottom = bottom; }
    public void setHardcore(boolean hard) { this.hard = hard; }

    public void afterUnmarshal( Unmarshaller u, Object parent ) {
        checkForUpdatedMap();
        this.buildings = (this.buildings == null ? new ArrayList<InsideBuilding>() : this.buildings);
    }
    
    public void checkForUpdatedMap() {
        if (getBuildings() == null) {
            return;
        }
        for (InsideBuilding building : getBuildings()) {
            if (building.getName().equals(UPGRADED_MAP)) {
                setUpgradedMapAvailable(true);
            }
        }
    }
}
