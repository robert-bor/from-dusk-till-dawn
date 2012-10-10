package nl.d2n.model;

import com.google.gson.annotations.Expose;
import nl.d2n.model.builder.ZoneBuilder;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.*;

@SuppressWarnings({"JpaDataSourceORMInspection"})
@Entity
@Table(name = "zones")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "x")
    @XmlAttribute
    private int x;

    @Column(name = "y")
    @XmlAttribute
    private int y;

    @Expose
    @Column(name = "zombies")
    @XmlAttribute(name = "z")
    private int zombies = -1;

    @Expose
    @Column(name = "scout_sense")
    private int scoutSense = -1;

    @Expose
    @Column(name = "visited")
    private boolean visited = false;

    @Expose
    @Column(name = "zone_depleted")
    private boolean zoneDepleted;

    @Expose
    @Column(name = "building_depleted")
    private boolean buildingDepleted;

    @Expose
    @Column(name = "blue_print_retrieved")
    private boolean bluePrintRetrieved;

    @Expose
    @Column(name = "scout_peek")
    private String scoutPeek;

    @Expose
    @Column(name = "camping_topology")
    @Enumerated(EnumType.STRING)
    private CampingTopology campingTopology = CampingTopology.UNKNOWN;
    
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false, columnDefinition = "bigint")
    private City city;

    @Transient
    @XmlAttribute(name = "nvt")
    private boolean discoveredNotVisitedToday;

    @Expose
    @Transient
    @XmlAttribute
    private ZoneTag tag;

    @Expose
    @Transient
    @XmlAttribute
    private ZoneDanger danger;

    @Expose
    @Transient
    private DiscoveryStatus discoveryStatus;

    @Expose
    @Transient
    @XmlElement
    private OutsideBuilding building;

    @OneToMany(mappedBy = "zone", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<Room> rooms = new ArrayList<Room>();

    @Expose
    @OneToMany(mappedBy = "zone", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @BatchSize(size=30)
    private List<Item> items = new ArrayList<Item>();

    @Expose
    @ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @MapKey(name="action")
    @JoinTable(name="last_user_activity", joinColumns = { @JoinColumn(name = "zone_id") }, inverseJoinColumns = { @JoinColumn(name = "user_activity_id") })
    @BatchSize(size=30)
    private Map<UpdateAction, UserAction> lastUserActions = new TreeMap<UpdateAction, UserAction>();

    public Long getId() { return this.id; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getZombies() { return zombies; }
    public int getScoutSense() { return scoutSense; }
    public boolean isVisited() { return visited; }
    public boolean isZoneDepleted() { return zoneDepleted; }
    public boolean isBuildingDepleted() { return buildingDepleted; }
    public boolean isBluePrintRetrieved() { return bluePrintRetrieved; }
    public String getScoutPeek() { return scoutPeek; }
    public City getCity() { return city; }
    public List<Item> getItems() { return items; }
    public ZoneTag getTag() { return this.tag; }
    public ZoneDanger getDanger() { return this.danger; }
    public boolean getDiscoveredNotVisitedToday() { return this.discoveredNotVisitedToday; }
    public OutsideBuilding getBuilding() { return this.building; }
    public Map<UpdateAction, UserAction> getLastUserActions() { return this.lastUserActions; }
    public DiscoveryStatus getDiscoveryStatus() { return this.discoveryStatus; }
    public CampingTopology getCampingTopology() { return this.campingTopology; }

    public void addItem(Item item) { this.items.add(item); }
    public void setId(Long id) { this.id = id; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setDanger(ZoneDanger danger) { this.danger = danger; }
    public void setVisited(boolean visited) { this.visited = visited; }
    public void setDiscoveredNotVisitedToday(boolean discoveredNotVisitedToday) { this.discoveredNotVisitedToday = discoveredNotVisitedToday; }
    public void setZombies(int zombies) { this.zombies = zombies > -1 ? zombies : -1; }
    public void setScoutSense(int scoutSense) { this.scoutSense = scoutSense > -1 ? scoutSense : -1; }
    public void setZoneDepleted(boolean zoneDepleted) { this.zoneDepleted = zoneDepleted; }
    public void setBuildingDepleted(boolean buildingDepleted) { this.buildingDepleted = buildingDepleted; }
    public void setBluePrintRetrieved(boolean bluePrintRetrieved) { this.bluePrintRetrieved = bluePrintRetrieved; }
    public void setScoutPeek(String scoutPeek) { this.scoutPeek = scoutPeek; }
    public void setCity(City city) { this.city = city; }
    public void setItems(List<Item> items) { this.items = items; }
    public void setBuilding(OutsideBuilding building) { this.building = building; }
    public void clearItems() { this.items.clear(); }
    public void addLastUserAction(UserAction userAction) { this.lastUserActions.put(userAction.getAction(), userAction); }
    public void setLastUserActions(Map<UpdateAction, UserAction> lastUserActions) { this.lastUserActions = lastUserActions; }
    public void setDiscoveryStatus(DiscoveryStatus discoveryStatus) { this.discoveryStatus = discoveryStatus; }
    public void setCampingTopology(CampingTopology campingTopology) { this.campingTopology = campingTopology; }

    //
    // LOGIC FOR MERGING ZONES
    //
    //   field                 this  other deri.
    // +---------------------+------+-----+-----+
    // | X                   | yes  | yes |     | always keep this
    // | Y                   | yes  | yes |     | alwasy keep this
    // | building            | yes  |  -  |     | always keep this
    // | Zombies             | (a)  | yes |     |
    // | Discovered-not-visi.| yes  | (b) |     |
    // | Danger              | yes  |  -  | (e) |
    // | Blueprint retrieved |  -   | yes |     | always take from other
    // | Zone depleted       |  -   | yes |     | always take from other
    // | Building depleted   |  -   | yes |     | always take from other
    // | Scout peek          |  -   | yes |     | always take from other
    // | Items               |  -   | yes |     | always take from other
    // +---------------------+------+-----+-----+
    // (a) in case of Upgraded Map, "this" is preferred over "other"
    // (b) if the update day == today, then prefer "other"
    // (e) modified by looking at the actual number of zombies
    //
    // SPECIALS
    // * DISCOVERED-NOT-VISITED
    // * ZOMBIES
    //
    public Zone mergeZone(Zone other, int today) {
        // Literally copy these fields from the other zone
        this.setBluePrintRetrieved(other.isBluePrintRetrieved());
        this.setZoneDepleted(other.isZoneDepleted());
        this.setBuildingDepleted(other.isBuildingDepleted());
        this.setItems(other.getItems());
        this.setScoutPeek(other.getScoutPeek());
        this.setLastUserActions(other.getLastUserActions());
        this.setCampingTopology(other.getCampingTopology());
        this.setScoutSense(other.getScoutSense());
        this.setVisited(other.isVisited());

        // If zombies are supplied, they come from Upgraded Map -- always trust those numbers!
        if (this.getZombies() == -1) {
            this.setZombies(other.getZombies());
            // It might be that a persisted zone is not yet available in the cached XML. When that happens, check to make
            // sure the right Danger level has been assigned
            updateDangerLevel(other, today);
        }
        // If the zone has not been registered by D2N, but if it still has been updated (through "Update My Zone"),
        // set it to discovered.
        updateDiscoveryStatus(other, today);

        return this;
    }

    protected void updateDangerLevel(Zone zone, int today) {
        int zombieUpdateDay = UserAction.getHighestDay(zone, new UpdateAction[] { UpdateAction.SAVE_ZOMBIES, UpdateAction.UPDATE_ZONE } );
        if (zombieUpdateDay == today) {
            this.setDanger(ZoneDanger.getClassWithZombies(zone.getZombies()));
        }
    }
    protected void updateDiscoveryStatus(Zone zone, int today) {
        int zoneUpdatedDay = UserAction.getHighestDay(zone, new UpdateAction[] { UpdateAction.UPDATE_ZONE } );
        if (zoneUpdatedDay != -1) {
            this.setDiscoveryStatus(zoneUpdatedDay == today ?
                    DiscoveryStatus.DISCOVERED_AND_VISITED_TODAY.getHighestDiscoveryStatus(getDiscoveryStatus()) :
                    DiscoveryStatus.DISCOVERED_NOT_VISITED_TODAY.getHighestDiscoveryStatus(getDiscoveryStatus()));
        }
    }

    public static Zone createZone(int xPos, int yPos) {
        return new ZoneBuilder()
                .setX(xPos)
                .setY(yPos)
                .setDiscoveryStatus(DiscoveryStatus.UNDISCOVERED)
                .setDanger(ZoneDanger.UNKNOWN)
                .toZone();
    }

    public Map<String, Object> getSingleZoneUpdate(List<Citizen> citizens) {
        Map<String, Object> zoneMap = new TreeMap<String, Object>();
        zoneMap.put("classes", getHtmlClasses());
        if (citizens != null) {
            zoneMap.put("citizens", citizens);
        }
        zoneMap.put("x", getX());
        zoneMap.put("y", getY());
        zoneMap.put("zoneUpdate", this);
        return zoneMap;
    }

    public String[] getHtmlClasses() {
        List<String> classes = new ArrayList<String>();
        if (x == 0 && y == 0) {
            classes.add("town");
        } else {
            if (this.getDiscoveryStatus() == DiscoveryStatus.UNDISCOVERED) {
                classes.add("unknown");
            } else if (this.getDiscoveryStatus() == DiscoveryStatus.DISCOVERED_NOT_VISITED_TODAY) {
                classes.add("nvt");
            } else {
                classes.add(getDanger() == null ? ZoneDanger.getClassWithZombies(getZombies()).getClassTag() : getDanger().getClassTag());
            }
        }
        if (getBuilding() != null) {
            classes.add("building");
        }
        classes.add("zone");
        return classes.toArray(new String[classes.size()]);
    }
}
