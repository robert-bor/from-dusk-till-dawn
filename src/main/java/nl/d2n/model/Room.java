package nl.d2n.model;

import com.google.gson.annotations.Expose;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.Map;
import java.util.TreeMap;

import static nl.d2n.model.RuinDirection.*;

@SuppressWarnings({"JpaDataSourceORMInspection"})
@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "zone_id", nullable = false, columnDefinition = "bigint")
    private Zone zone;

    @Column(name = "corridor_west")
    private boolean corridorWest = false;

    @Column(name = "corridor_north")
    private boolean corridorNorth = false;

    @Column(name = "corridor_east")
    private boolean corridorEast = false;

    @Column(name = "corridor_south")
    private boolean corridorSouth = false;

    @Expose
    @Column(name = "x")
    private int x;

    @Expose
    @Column(name = "y")
    private int y;

    @Expose
    @Column(name = "door")
    @Enumerated(EnumType.STRING)
    private Door door = Door.NONE;

    @Expose
    @Column(name = "door_key")
    @Enumerated(EnumType.STRING)
    private Key key = Key.UNKNOWN;

    @Expose
    @Column(name = "zombies")
    private int zombies;

    @Transient
    @Expose
    private Map<RuinDirection, Boolean> corridors = new TreeMap<RuinDirection, Boolean>();

    @Transient
    private boolean alreadyVisited;

    @PostLoad
    public void init() {
        corridors.put(WEST, corridorWest);
        corridors.put(NORTH, corridorNorth);
        corridors.put(EAST, corridorEast);
        corridors.put(SOUTH, corridorSouth);
    }

    protected Room() {}
    public Room(Zone zone, int x, int y) {
        this.zone = zone;
        setX(x);
        setY(y);
    }

    public void setCorridor(RuinDirection direction, boolean add) {
        switch (direction) {
            case WEST :
                setCorridorWest(add);
                break;
            case NORTH :
                setCorridorNorth(add);
                break;
            case EAST :
                setCorridorEast(add);
                break;
            case SOUTH :
                setCorridorSouth(add);
                break;
        }
        init();
    }

    public Integer getId() { return id; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Door getDoor() { return door; }
    public Key getKey() { return key; }
    public Map<RuinDirection, Boolean> getCorridors() { return corridors; }
    public Zone getZone() { return zone; }
    public boolean isEntrance() { return x == 0 && y == 0; }
    public boolean isAlreadyVisited() { return alreadyVisited; }
    public int getZombies() { return zombies; }

    public void setDoor(Door door) { this.door = door; }
    public void setKey(Key key) { this.key = key; }
    public void setY(int y) { this.y = y; }
    public void setX(int x) { this.x = x; }
    public void setCorridorSouth(boolean corridorSouth) { this.corridorSouth = corridorSouth; }
    public void setCorridorEast(boolean corridorEast) { this.corridorEast = corridorEast; }
    public void setCorridorNorth(boolean corridorNorth) { this.corridorNorth = corridorNorth; }
    public void setCorridorWest(boolean corridorWest) { this.corridorWest = corridorWest; }
    public void setZone(Zone zone) { this.zone = zone; }
    public void setAlreadyVisited(boolean alreadyVisited) { this.alreadyVisited = alreadyVisited; }
    public void setZombies(int zombies) { this.zombies = zombies; }

}
