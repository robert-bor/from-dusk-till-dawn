package nl.d2n.model;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.Date;

@SuppressWarnings({"JpaDataSourceORMInspection"})
@Entity
@Table(name = "user_activity")
public class UserAction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Integer id;

    @Column(name = "action")
    @Enumerated(EnumType.STRING)
    private UpdateAction action;

    @Expose
    @Column(name = "updated")
    private Date updated;

    @Expose
    @Column(name = "day")
    private int day;

    @SuppressWarnings({"FieldCanBeLocal"})
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = true, columnDefinition = "bigint")
    private Zone zone;

    @Expose
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "bigint")
    private User user;

    @SuppressWarnings({"FieldCanBeLocal"})
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false, columnDefinition = "bigint")
    private City city;

    public UserAction() {}
    public UserAction(City city, Zone zone, User user, Date updated, UpdateAction action) {
        setCity(city);
        setZone(zone);
        setUser(user);
        setUpdated(updated);
        setAction(action);
    }

    public Integer getId() {return id; }
    public UpdateAction getAction() {return action; }
    public Date getUpdated() {return updated; }
    public int getDay() { return day; }
    public User getUser() { return user; }

    public void setId(Integer id) {this.id = id; }
    public void setAction(UpdateAction action) {this.action = action; }
    public void setDay(int day) { this.day = day; }
    public void setUpdated(Date updated) {this.updated = updated; }
    public void setUser(User user) {this.user = user; }
    public void setCity(City city) {this.city = city; }
    public void setZone(Zone zone) {this.zone = zone; }

    public static int getHighestDay(Zone zone, UpdateAction[] actions) {
        int highestDay = -1;
        for (UpdateAction actionType : actions) {
            highestDay = Math.max(highestDay, getDay(zone, actionType));
        }
        return highestDay;
    }
    public static int getDay(Zone zone, UpdateAction actionType) {
        UserAction action = zone.getLastUserActions().get(actionType);
        return action == null ? -1 : action.getDay();
    }
}
