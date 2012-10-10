package nl.d2n.service.actions;

import nl.d2n.dao.UserDao;
import nl.d2n.dao.ZoneDao;
import nl.d2n.model.*;
import nl.d2n.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static nl.d2n.service.UserSecurityCheck.*;
import static nl.d2n.service.MapService.*;

@Component
public abstract class ZoneActionManualUpdate extends AbstractZoneAction {

    @Autowired
    protected ZoneDao zoneDao;
    
    @Autowired
    protected UserDao userDao;

    public Map<String, Object> execute(final UserKey userKey, final Integer x, final Integer y, final Integer day) throws ApplicationException {

        // Do this one before the check, because fetching the city might lead to a XML fetch and a legitimate user
        Integer cityId = getCityId(userKey);

        performSecurityCheck(userKey);

        Zone zone = findOrCreate(cityId, x, y);
        modifyZone(zone);
        zoneDao.saveZone(zone);

        zone.addLastUserAction(logAction(zone, userKey, day, getGameClock(userKey)));
        return zone.getSingleZoneUpdate(null);
    }

    protected Integer getCityId(UserKey userKey) throws ApplicationException {
        Integer cityId = userDao.getCityId(userKey);
        if (cityId == null) {
            readInfoAndPrepareMatrix(userKey, DISALLOW_CACHE_READ, DO_NOT_PERFORM_STATUS_CHECKS);
            cityId = userDao.getCityId(userKey);
            if (cityId == null) {
                throw new ApplicationException(D2NErrorCode.NOT_IN_TOWN);
            }
        }
        return cityId;
    }

    protected abstract void modifyZone(final Zone zone) throws ApplicationException;

    @Override
    protected void storeBasicInformation(MyZone zone) {}

    @Override
    protected Zone updateBasicInformation(final Zone currentZone) { return currentZone; }

    @Override
    protected Zone updateExtendedInformation(final Zone currentZone) { return currentZone; }

    @Override
    protected boolean allowFirstTime() {
        return DO_NOT_ALLOW_FIRST_TIME;
    }

    @Override
    protected boolean checkForSecuritySetting() {
        return CHECK_FOR_SECURE_SETTING;
    }

    @Override
    protected boolean checkForShunnedSetting() {
        return CHECK_FOR_SHUNNED_SETTING;
    }
}
