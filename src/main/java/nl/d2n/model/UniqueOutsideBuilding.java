package nl.d2n.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

@SuppressWarnings({"JpaDataSourceORMInspection"})
@Entity
@Table(name = "unique_outside_buildings")
public class UniqueOutsideBuilding {

    @Expose
    @Id
    @Column(name = "id")
    private Integer id;

    @Expose
    @Column
    private String name;

    @Expose
    @Column(length=1024)
    private String flavor;

    @Expose
    @Column
    private String url;

    public UniqueOutsideBuilding() {}
    public UniqueOutsideBuilding(Integer id, String name, String flavor, String url) {
        setId(id);
        setName(name);
        setFlavor(flavor);
        setUrl(url);
    }

    public Integer getId() { return this.id; }
    public String getName() { return this.name; }
    public String getFlavor() { return this.flavor; }
    public String getUrl() { return this.url; }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setFlavor(String flavor) { this.flavor = flavor; }
    public void setUrl(String url) { this.url = url; }

    public boolean mustBeOverwrittenBy(OutsideBuilding outsideBuilding) {
        // If IDs are not the same, they shouldn't be here
        if (getId() != outsideBuilding.getType()) {
            return false;
        }
        if (!getName().equals(outsideBuilding.getName())) {
            return true;
        }
        if (!getFlavor().equals(outsideBuilding.getFlavor())) {
            return true;
        }
        return false;
    }

    static public UniqueOutsideBuilding createFromOutsideBuilding(OutsideBuilding outsideBuilding, UniqueOutsideBuilding uniqueOutsideBuilding) {
        return new UniqueOutsideBuilding(
                outsideBuilding.getType(),
                outsideBuilding.getName(),
                outsideBuilding.getFlavor(),
                uniqueOutsideBuilding != null ? uniqueOutsideBuilding.getUrl() : null
        );
    }

}
