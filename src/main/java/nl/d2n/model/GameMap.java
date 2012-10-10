package nl.d2n.model;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "map")
@XmlAccessorType(XmlAccessType.FIELD)
public class GameMap {

    @XmlAttribute(name = "hei")
    private int height;

    @XmlAttribute(name = "wid")
    private int width;

    @XmlElement(name = "zone")
    private List<Zone> zones;

    private boolean upgradedMapAvailable = false;

    public int getHeight() { return this.height; }
    public int getWidth() { return this.width; }
    public List<Zone> getZones() { return this.zones; }
    public boolean isUpgradedMapAvailable() { return this.upgradedMapAvailable; }

    public void setUpgradedMapAvailable(boolean upgradedMapAvailable) { this.upgradedMapAvailable = upgradedMapAvailable; }

    public void afterUnmarshal( Unmarshaller u, Object parent ) {
        checkForUpdatedMap();
    }

    public void checkForUpdatedMap() {
        if (getZones() == null) {
            return;
        }
        for (Zone zone : getZones()) {
            if (zone.getZombies() != -1) {
                setUpgradedMapAvailable(true);
                break;
            }
        }
    }

}
