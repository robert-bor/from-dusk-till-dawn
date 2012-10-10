package nl.d2n.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.List;

@SuppressWarnings({"JpaDataSourceORMInspection"})
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Embedded()
    private UserKey key;

    @Expose
    @Column(name = "name")
    private String name;

    @Column(name = "secure")
    private boolean secure;

    @Column(name = "shunned")
    private boolean shunned;

    @Column(name = "banned")
    private boolean banned;

    @Column(name = "image")
    private String image;

    @Column(name = "description")
    private String description;
    
    @SuppressWarnings({"FieldCanBeLocal"})
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Distinction> distinctions;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<UserAction> actions;

    @SuppressWarnings({"FieldCanBeLocal"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = true)
    private City city;

    @Column(name = "soul_points")
    private Integer soulPoints = 0;
    
    @Column(name = "game_id")
    private Integer gameId;

    public Integer getId() { return id; }
    public UserKey getKey() { return key; }
    public String getName() { return name; }
    public boolean isBanned() { return banned; }
    public boolean isSecure() { return secure; }
    public boolean isShunned() { return shunned; }
    public Integer getSoulPoints() { return soulPoints; }
    public Integer getGameId() { return gameId; }
    public String getImage() { return image; }
    public String getDescription() { return description; }

    public void setBanned(boolean banned) { this.banned = banned; }
    public void setKey(UserKey key) { this.key = key; }
    public void setName(String name) { this.name = ("".equals(name) ? null : name); }
    public void setSecure(boolean secure) { this.secure = secure; }
    public void setShunned(boolean shunned) { this.shunned = shunned; }
    public void setCity(City city) { this.city = city; }
    public void setSoulPoints(Integer soulPoints) { this.soulPoints = soulPoints; }
    public void setGameId(Integer gameId) { this.gameId = gameId; }
    protected void setId(Integer id) { this.id = id; }
}
