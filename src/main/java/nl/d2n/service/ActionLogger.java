package nl.d2n.service;

import nl.d2n.dao.CityDao;
import nl.d2n.dao.UserActionDao;
import nl.d2n.dao.UserDao;
import nl.d2n.dao.ZoneDao;
import nl.d2n.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ActionLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionLogger.class);

    @Resource
    private ZoneDao zoneDao;

    @Resource
    private CityDao cityDao;

    @Resource
    private UserDao userDao;

    @Resource
    private UserActionDao userActionDao;

    @SuppressWarnings({"NullableProblems"})
    public UserAction logAction(int cityId, UserKey userKey, int day, UpdateAction action, GameClock gameClock) throws ApplicationException {
        return logAction(null, cityId, userKey, day, action, gameClock);
    }

    public UserAction logAction(Zone zone, UserKey userKey, int day, UpdateAction action, GameClock gameClock) throws ApplicationException {
        assert zone != null;
        if (zone.getId() == null) {
            zone = zoneDao.findZone(zone.getCity().getId(), zone.getX(), zone.getY());
        }
        return logAction(zone, zone.getCity().getId(), userKey, day, action, gameClock);
    }

    protected UserAction logAction(Zone zone, int cityId, UserKey userKey, int day, UpdateAction action, GameClock gameClock) throws ApplicationException {
        return logAction(
                zone,
                cityDao.findCity(cityId),
                userDao.find(userKey),
                gameClock,
                day,
                action);
    }

    private UserAction logAction(Zone zone, City city, User user, GameClock gameClock, int day, UpdateAction action) {
        UserAction userAction = new UserAction();
        userAction.setCity(city);
        userAction.setZone(zone);
        userAction.setUser(user);
        userAction.setUpdated(gameClock.getDateTime());
        userAction.setDay(day);
        userAction.setAction(action);
        if (zone != null) {
            zone = zoneDao.findZone(zone.getId());
            zone.addLastUserAction(userAction);
            zoneDao.saveZone(zone);
        } else {
            userActionDao.save(userAction);
        }
        return userAction;
    }

}
