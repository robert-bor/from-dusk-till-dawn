package nl.d2n.service.actions;

import nl.d2n.model.*;
import nl.d2n.service.UniqueItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static nl.d2n.service.UserSecurityCheck.*;
import static nl.d2n.service.MapService.*;

@Scope("prototype")
@Component
public class ZoneActionAutomaticUpdate extends AbstractZoneAction {

    private List<Item> items;
    private Integer zombies;
    private Boolean zoneDepleted;
    
    public Map<String, Object> execute(final UserKey userKey) throws ApplicationException {

        Info info = readInfoAndPrepareMatrix(userKey, DISALLOW_CACHE_READ, PERFORM_STATUS_CHECKS).getInfo();

        performSecurityCheck(userKey);

        return updateFromSecureXml(info, userKey).getSingleZoneUpdate(info.getCitizens());
    }

    protected void storeBasicInformation(MyZone zone) {
        setItems(zone.getItems());
        setZombies(zone.getZedControl());
        setZoneDepleted(zone.isDepleted());
    }

    protected Zone updateBasicInformation(final Zone currentZone) {
        if (items != null) {
            for (Item item : getItems()) {
                item.setZone(currentZone);
                currentZone.addItem(item);
            }
        }
        currentZone.setZombies(getZombies());
        currentZone.setZoneDepleted(getZoneDepleted());
        return currentZone;
    }

    protected Zone updateExtendedInformation(final Zone currentZone) {
        currentZone.setVisited(true);
        return currentZone;
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
    
    public ZoneActionAutomaticUpdate setItems(final List<Item> items) {
        this.items = items;
        return this;
    }
    
    public ZoneActionAutomaticUpdate setZombies(final Integer zombies) {
        this.zombies = zombies;
        return this;
    }
    
    public ZoneActionAutomaticUpdate setZoneDepleted(final Boolean zoneDepleted) {
        this.zoneDepleted = zoneDepleted;
        return this;
    }

    @Override
    protected UpdateAction getAction() {
        return UpdateAction.UPDATE_ZONE;
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
