package nl.d2n.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

@Entity
@Table(name = "unique_titles")
public class UniqueTitle {

    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Expose
    @Column(name = "name")
    private String name;

    @Column(name = "unique_distinction_id")
    private Integer uniqueDistinctionId;

    @Expose
    @Column(name = "treshold")
    private int treshold;

    @Expose
    @Column(name = "twinoid_points")
    private double twinoidPoints;

    public String getName() { return this.name; }
    public void setName(String name) { this.name = name; }
    public int getTreshold() { return treshold; }
    public void setTreshold(int treshold) { this.treshold = treshold; }
    public Integer getUniqueDistinctionId() { return uniqueDistinctionId; }
    public void setUniqueDistinctionId(Integer uniqueDistinctionId) { this.uniqueDistinctionId = uniqueDistinctionId; }
    public double getTwinoidPoints() { return twinoidPoints; }
    public void setTwinoidPoints(double twinoidPoints) { this.twinoidPoints = twinoidPoints; }

    public boolean mustBeOverwrittenBy(Title title) {
        if (!getName().equals(title.getName())) {
            return true;
        }
        if (!getUniqueDistinctionId().equals(title.getUniqueDistinctionId())) {
            return true;
        }
        return false;
    }

    static public UniqueTitle createFromTitle(Title title) {
        UniqueTitle uniqueTitle = new UniqueTitle();
        uniqueTitle.setName(title.getName());
        uniqueTitle.setUniqueDistinctionId(title.getUniqueDistinctionId());
        return uniqueTitle;
    }

}
