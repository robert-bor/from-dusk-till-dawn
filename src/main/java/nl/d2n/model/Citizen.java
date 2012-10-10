package nl.d2n.model;

import com.google.gson.annotations.Expose;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Citizen {

    @Expose
    @XmlAttribute
    private String name;

    @Expose
    @XmlTransient
    private String image;

    @Expose
    @XmlTransient
    private String jobText;

    @XmlAttribute
    private boolean dead;

    // http://data.die2nite.com/gfx/icons/small_hero.gif
    @XmlAttribute
    private boolean hero;

    // http://imgup.motion-twin.com/
    @XmlAttribute
    private String avatar;

    @XmlAttribute(name = "x")
    private Integer matrixX;

    @XmlAttribute(name = "y")
    private Integer matrixY;

    @Expose
    @XmlTransient
    private Integer x;

    @Expose
    @XmlTransient
    private Integer y;

    @Expose
    @XmlAttribute
    private Integer id;

    // http://data.die2nite.com/gfx/icons/small_ban.gif
    @XmlAttribute(name = "ban")
    private boolean shunned;

    @XmlAttribute
    private Job job;

    @Expose
    @XmlAttribute(name = "out")
    private boolean outside;

    @XmlAttribute
    private int baseDef;

    @XmlValue
    private String description;

    @Expose
    @XmlTransient
    private String lastUpdated;

    @Expose
    @XmlTransient
    private int activityRating;

    @Expose
    @XmlTransient
    private Integer soulPoints = 0;
    
    @Expose
    @XmlTransient
    private String specialImage;
    
    @Expose
    @XmlTransient
    private String specialDescription;

    public String getName() { return this.name; }
    public boolean isDead() { return this.dead; }
    public boolean isHero() { return this.hero; }
    public String getAvatar() {return this.avatar; }
    public Integer getMatrixX() { return this.matrixX; }
    public Integer getMatrixY() { return this.matrixY; }
    public Integer getId() { return this.id; }
    public boolean isShunned() { return this.shunned; }
    public Job getJob() { return this.job; }
    public boolean isOutside() { return this.outside; }
    public int getBaseDef() { return this.baseDef; }
    public String getDescription() { return this.description; }
    public String getImage() { return this.image; }
    public Integer getX() { return x; }
    public Integer getY() { return y; }
    public String getLastUpdated() { return lastUpdated; }
    public int getActivityRating() { return activityRating; }
    public Integer getSoulPoints() { return soulPoints; }
    public String getSpecialImage() { return specialImage; }
    public String getSpecialDescription() { return specialDescription; }

    public void setId(Integer id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setMatrixX(int x) { this.matrixX = x; }
    public void setMatrixY(int y) { this.matrixY = y; }
    public void setOutside(boolean outside) { this.outside = outside; }
    public void setY(Integer y) { this.y = y; }
    public void setX(Integer x) { this.x = x; }
    public void setLastUpdated(String lastUpdated) { this.lastUpdated = lastUpdated; }
    public void setActivityRating(int activityRating) { this.activityRating = activityRating; }
    public void setSoulPoints(Integer soulPoints) { this.soulPoints = soulPoints; }
    public void setSpecialImage(String specialImage) { this.specialImage = specialImage; }
    public void setSpecialDescription(String specialDescription) { this.specialDescription = specialDescription; }

    public void determineImage() {
        this.image = getJob().getImage();
    }
    public void determineJobText() {
        this.jobText = getJob().getJobText();
    }

    void afterUnmarshal( Unmarshaller u, Object parent ) {
        this.job = getJob() == null ? Job.UNKNOWN : getJob();
        determineImage();
        determineJobText();
    }
}


/**
 <tr class="even">
 <td width="90">
    <a href="http://www.die2nite.com/#ghost/city?uid=52868" target="_blank" class="avatar">
        <img title="Avatar" src="http://imgup.motion-twin.com/hordes/7/1/e9e157b8_52868.jpg" alt="Avatar">
    </a>
 </td>
 <td nowrap>
    <img src="http://data.die2nite.com/gfx/icons/item_pelle.gif" title="Scavenger" alt="Scavenger">
 <a href="http://www.die2nite.com/#ghost/city?uid=52868" target="_blank">Aivaras</a> <img src="/images/small_hero.gif" title="Hero" alt="Hero"></td><td nowrap><strong>3 / 2</strong></td><td nowrap>&nbsp;(5<img src="/images/small_pa.gif" alt="AP"> / 4km)</td><td></td></tr>
 */