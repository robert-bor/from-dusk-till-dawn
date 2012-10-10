package nl.d2n.service.actions;

import nl.d2n.dao.ZoneDao;
import nl.d2n.model.*;
import nl.d2n.service.MapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static nl.d2n.service.UserSecurityCheck.*;
import static nl.d2n.service.MapService.*;

@Service
public class MapActionRead extends AbstractAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapActionRead.class);

    @Autowired
    private ZoneDao zoneDao;

    public InfoWrapper execute(final UserKey userKey) throws ApplicationException {

        // 1. Check whether the user & citizen both are bona fide
        performSecurityCheck(userKey);

        // 2. Read the info & prepare the matrix
        InfoWrapper infoWrapper = readInfoAndPrepareMatrix(userKey, ALLOW_CACHE_READ, DO_NOT_PERFORM_STATUS_CHECKS);
        Info info = infoWrapper.getInfo();

        try {

            // 3. Read zones from database & merge
            info.getMatrix().mergeZones(
                    zoneDao.findZones(info.getGameHeader().getGame().getId()),
                    info.getGameHeader().getGame().getDay());

        } catch (NullPointerException err) {
            LOGGER.error("", infoWrapper.getXml());
        }

        // 4. Update the activity log
        if (!infoWrapper.isStale()) {
            logAction(info.getCity(), userKey, info.getGameHeader().getGame().getDay(), getGameClock(userKey));
        }

        return infoWrapper;
    }
    
    @Override
    protected UpdateAction getAction() {
        return UpdateAction.READ_MAP;
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
        return DO_NOT_CHECK_FOR_SHUNNED_SETTING;
    }
}
