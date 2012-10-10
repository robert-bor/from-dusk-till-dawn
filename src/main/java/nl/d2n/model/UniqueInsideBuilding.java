package nl.d2n.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"JpaDataSourceORMInspection"})
@Entity
@Table(name = "unique_inside_buildings")
public class UniqueInsideBuilding implements ImageBearer {

    @Expose
    @Id
    @Column(name = "id")
    private Integer id;

    @Expose
    @Column
    private String name;

    @Expose
    @Column
    private boolean temporary;

    @Column
    private Integer parent;

    @Expose
    @Column
    private String image;

    @Expose
    @Column(length=1024)
    private String flavor;

    @Expose
    @Column
    private String url;

    @Expose
    @Column(name = "in_sprite")
    private boolean inSprite;

    @Expose
    @Column(name = "always_available")
    private boolean alwaysAvailable;

    @Expose
    @Transient
    private Integer defence;

    @Expose
    @Transient
    private Integer ap;

    @Expose
    @Transient
    private List<InsideBuildingResourceCost> requiredResources = new ArrayList<InsideBuildingResourceCost>();

    public UniqueInsideBuilding() {}
    public UniqueInsideBuilding(Integer id, String name, boolean temporary, Integer parent, String image, String flavor, String url, boolean inSprite, boolean alwaysAvailable) {
        this.id = id;
        this.name = name;
        this.temporary = temporary;
        this.parent = parent;
        this.image = image;
        this.flavor = flavor;
        this.url = url;
        this.inSprite = inSprite;
        this.alwaysAvailable = alwaysAvailable;
    }

    public void setUrl(String url) { this.url = url; }

    public String getName() { return this.name; }
    public boolean isTemporary() { return this.temporary; }
    public Integer getId() { return this.id; }
    public Integer getParent() { return this.parent; }
    public String getImage() { return this.image; }
    public String getFlavor() { return this.flavor; }
    public String getUrl() { return this.url; }
    public boolean isInSprite() { return this.inSprite; }
    public boolean isAlwaysAvailable() { return this.alwaysAvailable; }
    public Integer getAp() { return this.ap; }
    public Integer getDefence() { return this.defence; }
    public List<InsideBuildingResourceCost> getRequiredResources() { return this.requiredResources; }

    public void setInSprite(boolean inSprite) { this.inSprite = inSprite; }
    public void setAlwaysAvailable(boolean alwaysAvailable) { this.alwaysAvailable = alwaysAvailable; }
    public void setAp(Integer ap) { this.ap = ap; }
    public void setDefence(Integer defence) { this.defence = defence; }
    public void addResourceCost(InsideBuildingResourceCost requiredResource) { this.requiredResources.add(requiredResource); }
    public void clearResourceCosts() { this.requiredResources.clear(); }

    public boolean mustBeOverwrittenBy(InsideBuilding insideBuilding) {
        // If IDs are not the same, they shouldn't be here
        if (getId() != insideBuilding.getBuildingId()) {
            return false;
        }
        if (!getName().equals(insideBuilding.getName())) {
            return true;
        }
        if (isTemporary() != insideBuilding.isTemporary()) {
            return true;
        }
        if (isNull() != (insideBuilding.getParent() == 0)) {
            return true;
        }
        if (!getImage().equals(insideBuilding.getImage())) {
            return true;
        }
        if (!getFlavor().equals(insideBuilding.getFlavor())) {
            return true;
        }
        return false;
    }
    protected boolean isNull() {
        return getParent() == null || getParent() == 0;
    }

    @SuppressWarnings({"NullableProblems"})
    static public List<InsideBuilding> createBuildingHierarchy(Collection<UniqueInsideBuilding> uniqueBuildings) {
        List<InsideBuilding> rootBuildings = searchBuildingsWithParent(uniqueBuildings, null);
        processBuildings(uniqueBuildings, rootBuildings);
        return rootBuildings;
    }
    static public void processBuildings(Collection<UniqueInsideBuilding> uniqueBuildings, List<InsideBuilding> buildings) {
        for (InsideBuilding building : buildings) {
            List<InsideBuilding> childBuildings = searchBuildingsWithParent(uniqueBuildings, building.getBuildingId());
            building.setChildBuildings(childBuildings);
            processBuildings(uniqueBuildings, childBuildings);
        }
    }
    static public List<InsideBuilding> searchBuildingsWithParent(Collection<UniqueInsideBuilding> uniqueBuildings, Integer parent) {
        List<InsideBuilding> buildingsWithParent = new ArrayList<InsideBuilding>();
        for (UniqueInsideBuilding uniqueBuilding : uniqueBuildings) {
            if (parent == null) {
                if (uniqueBuilding.getParent() == null) {
                    buildingsWithParent.add(uniqueBuilding.createBuilding());
                }
            } else {
                if (parent.equals(uniqueBuilding.getParent())) {
                    buildingsWithParent.add(uniqueBuilding.createBuilding());
                }
            }
        }
        return buildingsWithParent;
    }

    public InsideBuilding createBuilding() {
        return new InsideBuilding(
            getId(),
            getName(),
            isTemporary(),
            getParent(),
            getImage(),
            getFlavor(),
            getUrl(),
            isInSprite(),
            getAp(),
            getDefence(),
            getRequiredResources()
        );
    }
    static public UniqueInsideBuilding createFromInsideBuilding(InsideBuilding insideBuilding, UniqueInsideBuilding uniqueInsideBuilding) {
        return new UniqueInsideBuilding(
                insideBuilding.getBuildingId(),
                insideBuilding.getName(),
                insideBuilding.isTemporary(),
                insideBuilding.getParent() == 0 ? null : insideBuilding.getParent(),
                insideBuilding.getImage(),
                insideBuilding.getFlavor(),
                uniqueInsideBuilding != null ? uniqueInsideBuilding.getUrl() : null,
                uniqueInsideBuilding != null && uniqueInsideBuilding.isInSprite() && insideBuilding.getImage().equals(uniqueInsideBuilding.getImage()),
                uniqueInsideBuilding != null && uniqueInsideBuilding.isAlwaysAvailable()
        );
    }
}
