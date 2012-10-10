package nl.d2n.service.actions;

import nl.d2n.model.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static nl.d2n.service.MapService.*;
import static nl.d2n.service.UserSecurityCheck.ALLOW_FIRST_TIME;
import static nl.d2n.service.UserSecurityCheck.CHECK_FOR_SHUNNED_SETTING;
import static nl.d2n.service.UserSecurityCheck.DO_NOT_CHECK_FOR_SECURE_SETTING;

@Scope("prototype")
@Component
public class ZoneActionExtendedUpdate extends AbstractZoneAction {

    private List<Item> items;
    private Integer zombies;
    private Boolean zoneDepleted;
    private CampingTopology campingTopology;
    private Boolean blueprintAvailable;
            
    public Map<String, Object> execute(final UserKey userKey) throws ApplicationException {

        Info info = readInfoAndPrepareMatrix(userKey, DISALLOW_CACHE_READ, DO_NOT_PERFORM_STATUS_CHECKS).getInfo();

        try {
            info.performVariousStatusChecks();
        } catch (ApplicationException err) {
            if (!D2NErrorCode.HARD_TOWN.equals(err.getError())) {
                throw err;
            }
            if (getZombies() == null) {
                throw new ApplicationException(D2NErrorCode.MISSING_PARAMETER, "zombies");
            }
            if (getZoneDepleted() == null) {
                throw new ApplicationException(D2NErrorCode.MISSING_PARAMETER, "zone_depleted");
            }
        }

        return updateFromSecureXml(info, userKey).getSingleZoneUpdate(info.getCitizens());
    }

    public Boolean isBlueprintAvailable() {
        return this.blueprintAvailable;
    }
    
    public CampingTopology getCampingTopology() {
        return this.campingTopology;
    }

    public List<Item> getItems() {
        return this.items;
    }

    public Integer getZombies() {
        return this.zombies;
    }

    public Boolean getZoneDepleted() {
        return this.zoneDepleted;
    }

    public ZoneActionExtendedUpdate setBlueprintAvailable(final Boolean blueprintAvailable) {
        this.blueprintAvailable = blueprintAvailable;
        return this;
    }

    public ZoneActionExtendedUpdate setCampingTopology(final CampingTopology campingTopology) {
        this.campingTopology = campingTopology;
        return this;
    }

    public ZoneActionExtendedUpdate setItems(final List<Item> items) {
        this.items = items;
        return this;
    }

    public ZoneActionExtendedUpdate setZombies(final Integer zombies) {
        this.zombies = zombies;
        return this;
    }

    public ZoneActionExtendedUpdate setZoneDepleted(final Boolean zoneDepleted) {
        this.zoneDepleted = zoneDepleted;
        return this;
    }

    protected void storeBasicInformation(MyZone zone) {
        setItems(zone.getItems());
        setZombies(zone.getZedControl());
        setZoneDepleted(zone.isDepleted());
    }

    protected Zone updateBasicInformation(final Zone currentZone) {
        if (getItems() != null) {
            for (Item item : getItems()) {
                item.setZone(currentZone);
                currentZone.addItem(item);
            }
        }
        currentZone.setZombies(getZombies());
        currentZone.setZoneDepleted(getZoneDepleted());
        return currentZone;
    }

    @SuppressWarnings("ConstantConditions")
    protected Zone updateExtendedInformation(final Zone currentZone) {
        currentZone.setBluePrintRetrieved(isBlueprintAvailable() == null ? false : !isBlueprintAvailable());
        currentZone.setCampingTopology(getCampingTopology() == null ? CampingTopology.UNKNOWN : getCampingTopology());
        currentZone.setVisited(true);
        return currentZone;
    }

    @Override
    protected UpdateAction getAction() {
        return UpdateAction.UPDATE_ZONE_EXTENDED;
    }

    @Override
    protected boolean allowFirstTime() {
        return ALLOW_FIRST_TIME;
    }

    @Override
    protected boolean checkForSecuritySetting() {
        return DO_NOT_CHECK_FOR_SECURE_SETTING;
    }

    @Override
    protected boolean checkForShunnedSetting() {
        return CHECK_FOR_SHUNNED_SETTING;
    }

}
