package nl.d2n.model;

import nl.d2n.util.GsonUtil;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Map;

@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class Info {

    @XmlElement(name = "map")
    private GameMap map;

    @XmlElement
    private City city;

    @XmlElementWrapper(name = "bank")
    @XmlElement(name = "item")
    private List<Item> items;

    @XmlTransient
    private List<Item> uniqueItems;

    @XmlElementWrapper(name = "citizens")
    @XmlElement(name = "citizen")
    private List<Citizen> citizens;

    @XmlElementWrapper(name = "cadavers")
    @XmlElement(name = "cadaver")
    private List<Cadaver> cadavers;

    private GameHeader gameHeader;

    @XmlElementWrapper(name = "estimations")
    @XmlElement(name = "e")
    private List<Estimation> estimations;

    @XmlElementWrapper(name = "upgrades")
    @XmlElement(name = "up")
    private List<TownUpgrade> upgrades;

    @XmlTransient
    private MapMatrix matrix;

    @XmlTransient
    private Map<Integer, UniqueItem> itemLookup;

    public GameMap getMap() { return this.map; }
    public City getCity() { return this.city; }
    public List<Item> getItems() { return this.items; }
    public List<Citizen> getCitizens() { return this.citizens; }
    public List<Cadaver> getCadavers() { return this.cadavers; }
    public GameHeader getGameHeader() { return this.gameHeader; }
    public List<Estimation> getEstimations() { return this.estimations; }
    public List<TownUpgrade> getUpgrades() { return this.upgrades; }
    public MapMatrix getMatrix() { return this.matrix; }
    public List<Item> getUniqueItems() { return this.uniqueItems; }
    public Map<Integer, UniqueItem> getItemLookup() { return this.itemLookup; }
    public String getItemLookupAsJson() {
        return GsonUtil.objectToJson(getItemLookup());
    }
    public String getCitizensAsJson() {
        return GsonUtil.objectToJson(getCitizens());
    }
    public boolean isUpgradedMapAvailable() {
        return city.isUpgradedMapAvailable() || map.isUpgradedMapAvailable();
    }

    public void performVariousStatusChecks() throws ApplicationException {

        // 1. Check if the active citizen is outside and not in the town zone
        if (!isSecure()) {
            throw new ApplicationException(D2NErrorCode.KEY_NOT_SECURE);
        }

        // 2. Check if the citizen is dead
        if (isDead()) {
            throw new ApplicationException(D2NErrorCode.YOU_ARE_DEAD);
        }

        // 3. Check if the town is in Chaos mode
        if (isChaosMode()) {
            throw new ApplicationException(D2NErrorCode.CHAOS_MODE);
        }

        // 4. Check if the active citizen is outside and not in the town zone
        if (!isCitizenInWorldBeyond()) {
            throw new ApplicationException(D2NErrorCode.CITIZEN_NOT_OUTSIDE);
        }

        // 5. Check if the town is a hard town (Pandemonium)
        if (isHard()) {
            throw new ApplicationException(D2NErrorCode.HARD_TOWN);
        }
    }

    @SuppressWarnings({"RedundantIfStatement"})
    public boolean isCitizenInWorldBeyond() {
        if (!isSecure()) {
            // We simply do not know
            return false;
        }
        Citizen citizen = getGameHeader().getOwner().getCitizen();
        if (!citizen.isOutside()) {
            return false;
        }
        if (    !isChaosMode() &&
                getCity().getX() == citizen.getMatrixX() &&
                getCity().getY() == citizen.getMatrixY()) {
            return false;
        }
        return true;
    }

    public boolean isSecure() {
        return getGameHeader().isSecure();
    }

    public boolean isDead() {
        return isSecure() && getGameHeader().getOwner().getCitizen().isDead();
    }

    public boolean isChaosMode() {
        return getCity().isChaosMode();
    }

    public boolean isHard() {
        return getCity().isHard();
    }

    public void setUniqueItems(List<Item> uniqueItems) { this.uniqueItems = uniqueItems; }
    public void setItemLookup(Map<Integer, UniqueItem> itemLookup) { this.itemLookup = itemLookup; }
    public void setGameHeader(final GameHeader gameHeader) { this.gameHeader = gameHeader; }
    public void setMatrix(MapMatrix matrix) { this.matrix = matrix; }
    public void setCity(City city) { this.city = city; }
}
