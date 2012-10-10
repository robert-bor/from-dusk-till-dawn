package nl.d2n.service.actions;

import nl.d2n.dao.CityDao;
import nl.d2n.dao.ZoneDao;
import nl.d2n.model.*;
import nl.d2n.service.UniqueItemManager;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractZoneAction extends AbstractAction {

    @Autowired
    protected ZoneDao zoneDao;
    
    @Autowired
    protected CityDao cityDao;

    @Autowired
    private UniqueItemManager uniqueItemManager;

    protected Zone deleteAllItemsInZone(Zone zone) {
        zoneDao.deleteItemsInZone(zone.getId());
        zone.clearItems();
        return zone;
    }

    protected Zone findOrCreate(final int cityId, final int x, final int y) throws ApplicationException {
        City city = cityDao.findCity(cityId);
        if (city == null) {
            throw new ApplicationException(D2NErrorCode.CITY_DOES_NOT_EXIST);
        }
        if (city.getWidth() != 0) {
            if (    x < city.getLeft() ||
                    x > city.getRight() ||
                    y > city.getTop() ||
                    y < city.getBottom()) {
                throw new ApplicationException(D2NErrorCode.ILLEGAL_COORDINATES);
            }
        }
        return zoneDao.findOrCreateZone(city, x, y);
    }

    protected Zone updateFromSecureXml(Info info, final UserKey userKey) throws ApplicationException {

        // 1. Store the information in a zone
        storeZoneInformation(info.getGameHeader().getOwner().getZone());

        // 2. Store the Zone
        Citizen citizen = info.getGameHeader().getOwner().getCitizen();
        Zone zone = updateZone(
                info.getGameHeader().getGame().getId(),
                info.getMatrix().getRealXPos(citizen.getMatrixX()),
                info.getMatrix().getRealYPos(citizen.getMatrixY()));

        // 3. Log the action
        zone.addLastUserAction(logAction(
                zone, userKey, info.getGameHeader().getGame().getDay(), getGameClock(userKey)));

        // 4. Merge the zones with the updated zone
        info.getMatrix().mergeZone(zone, info.getGameHeader().getGame().getDay());

        return info.getMatrix().getMatrix()[citizen.getMatrixX()][citizen.getMatrixY()];
    }

    protected void storeZoneInformation(MyZone zone) {

        if (zone == null) {
            return;
        }

        // 1. Check for unique items in the ZONE -- do this after the secure check
        uniqueItemManager.checkForExistence(zone.getItems());

        // 2. Remember the variables from the info file
        storeBasicInformation(zone);
    }

    protected Zone updateZone(Integer cityId, Integer x, Integer y) throws ApplicationException {
        final Zone zone = findOrCreate(cityId, x, y);
        deleteAllItemsInZone(zone);
        updateBasicInformation(zone);
        updateExtendedInformation(zone);
        zoneDao.saveZone(zone);
        return zone;
    }

    abstract protected void storeBasicInformation(MyZone zone);

    abstract protected Zone updateBasicInformation(final Zone currentZone);

    abstract protected Zone updateExtendedInformation(final Zone currentZone);
}