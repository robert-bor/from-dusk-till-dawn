package nl.d2n.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings({"JpaDataSourceORMInspection"})
@Entity
@Table(name = "inside_buildings")
@XmlRootElement(name = "building")
@XmlAccessorType(XmlAccessType.FIELD)
public class InsideBuilding implements Comparable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    @XmlTransient
    private Integer id;

    @Expose
    @XmlAttribute
    @Transient
    private String name;

    @Expose
    @XmlAttribute
    @Transient
    private boolean temporary;

    @Expose
    @SerializedName("id")
    @XmlAttribute(name = "id")
    @Column(name = "unique_inside_building_id")
    private int buildingId;

    @Expose
    @XmlAttribute
    @Transient
    private int parent;

    @Expose
    @XmlAttribute(name = "img")
    @Transient
    private String image;

    @Expose
    @XmlValue
    @Transient
    private String flavor;

    @Expose
    @XmlTransient
    @Transient
    private String url;

    @Expose
    @XmlTransient
    @Transient
    private List<InsideBuilding> childBuildings = new ArrayList<InsideBuilding>();

    @Expose
    @XmlTransient
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private InsideBuildingStatus status = InsideBuildingStatus.NOT_AVAILABLE;

    @Expose
    @XmlTransient
    @Transient
    private boolean inSprite;

    @Expose
    @XmlTransient
    @Transient
    private boolean alwaysAvailable;

    @SuppressWarnings({"FieldCanBeLocal"})
    @XmlTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false, columnDefinition = "bigint")
    private City city;

    @Expose
    @XmlTransient
    @Transient
    private Integer defence;

    @Expose
    @XmlTransient
    @Transient
    private Integer ap;

    @Expose
    @XmlTransient
    @Transient
    private List<InsideBuildingResourceCost> requiredResources = new ArrayList<InsideBuildingResourceCost>();

    public InsideBuilding() {}
    public InsideBuilding(Integer buildingId, String name, boolean temporary, Integer parent, String image,
                          String flavor, String url, boolean inSprite, Integer ap, Integer defence,
                          List<InsideBuildingResourceCost> requiredResources) {
        this.buildingId = buildingId;
        this.name = name;
        this.temporary = temporary;
        this.parent = parent == null ? 0 : parent;
        this.image = image;
        this.flavor = flavor;
        this.url = url;
        this.inSprite = inSprite;
        this.ap = ap;
        this.defence = defence;
        this.requiredResources = requiredResources;
    }

    public String getName() { return this.name; }
    public boolean isTemporary() { return this.temporary; }
    public int getBuildingId() { return this.buildingId; }
    public int getParent() { return this.parent; }
    public String getImage() { return this.image; }
    public String getFlavor() { return this.flavor; }
    public String getUrl() { return this.url; }
    public List<InsideBuilding> getChildBuildings() { return this.childBuildings; }
    public InsideBuildingStatus getStatus() { return this.status; }
    public boolean isInSprite() { return this.inSprite; }
    public boolean isAlwaysAvailable() { return this.alwaysAvailable; }
    public Integer getAp() { return this.ap; }
    public Integer getDefence() { return this.defence; }
    public List<InsideBuildingResourceCost> getRequiredResources() { return this.requiredResources; }

    public void setChildBuildings(List<InsideBuilding> childBuildings) { this.childBuildings = childBuildings; }
    public void setStatus(InsideBuildingStatus status) { this.status = status; }
    public void setCity(City city) { this.city = city; }
    public void setBuildingId(int buildingId) { this.buildingId = buildingId; }
    public void setAlwaysAvailable(boolean alwaysAvailable) { this.alwaysAvailable = alwaysAvailable; }

    public static List<InsideBuilding> setConstructedBuildingStatus(List<InsideBuilding> nodeBuildings,
                                                                    List<InsideBuilding> constructedBuildings,
                                                                    InsideBuildingStatus status,
                                                                    boolean alwaysAvailable) {
        Map<Integer, InsideBuilding> constructedBuildingsMap = new TreeMap<Integer, InsideBuilding>();
        for (InsideBuilding constructedBuilding : constructedBuildings) {
            constructedBuildingsMap.put(constructedBuilding.getBuildingId(), constructedBuilding);
        }
        setConstructedBuildingStatus(nodeBuildings, constructedBuildingsMap, status, alwaysAvailable);
        return nodeBuildings;
    }
    public static void setConstructedBuildingStatus(List<InsideBuilding> nodeBuildings,
                                                    Map<Integer, InsideBuilding> constructedBuildingsMap,
                                                    InsideBuildingStatus status,
                                                    boolean alwaysAvailable) {
        for (InsideBuilding building : nodeBuildings) {
            InsideBuilding buildingWithStatus = constructedBuildingsMap.get(building.getBuildingId());
            if (buildingWithStatus != null) {
                building.setStatus(status);
                building.setAlwaysAvailable(alwaysAvailable);
            }
            setConstructedBuildingStatus(building.getChildBuildings(), constructedBuildingsMap, status, alwaysAvailable);
        }
    }

    public int hashCode() {
        return id == null ? buildingId : id;
    }
    public int compareTo(Object o) {
        if (!(o instanceof InsideBuilding)) {
            return -1;
        }
        InsideBuilding otherBuilding = (InsideBuilding)o;
        return ((Integer) getBuildingId()).compareTo(otherBuilding.getBuildingId());
    }
}
