package nl.d2n.service.actions;

import nl.d2n.model.*;
import nl.d2n.service.ActionLogger;
import nl.d2n.service.MapService;
import nl.d2n.service.UserSecurityCheck;
import org.springframework.beans.factory.annotation.Autowired;
import static nl.d2n.service.MapService.*;

public abstract class AbstractAction {

    @Autowired
    private UserSecurityCheck userSecurityCheck;

    @Autowired
    private ActionLogger actionLogger;

    @Autowired
    private GameClock gameClock;
    
    @Autowired
    private MapService mapService;

    protected void performSecurityCheck(UserKey userKey) throws ApplicationException {
        userSecurityCheck.checkUser(
                userKey,
                allowFirstTime(),
                checkForSecuritySetting(),
                checkForShunnedSetting());
    }

    protected UserAction logAction(City city, UserKey userKey, int day, GameClock gameClock) throws ApplicationException {
        return actionLogger.logAction(city.getId(), userKey, day, getAction(), gameClock);
    }

    protected UserAction logAction(Zone zone, UserKey userKey, int day, GameClock gameClock) throws ApplicationException {
        return actionLogger.logAction(zone, userKey, day, getAction(), gameClock);
    }

    public GameClock getGameClock(final UserKey userKey) throws ApplicationException {
        if (!this.gameClock.isInitialized()) {
            mapService.readInfoAndPrepareMatrix(userKey, DISALLOW_CACHE_READ, DO_NOT_PERFORM_STATUS_CHECKS);
        }
        return this.gameClock;
    }

    public InfoWrapper readInfoAndPrepareMatrix(final UserKey userKey, boolean allowReadFromCache,
                                                boolean performStatusChecks) throws ApplicationException {
        return mapService.readInfoAndPrepareMatrix(userKey, allowReadFromCache, performStatusChecks);
    }

    protected abstract UpdateAction getAction();
    protected abstract boolean allowFirstTime();
    protected abstract boolean checkForSecuritySetting();
    protected abstract boolean checkForShunnedSetting();
}
