package nl.d2n.service;

import nl.d2n.dao.UserActionDao;
import nl.d2n.dao.UserDao;
import nl.d2n.dao.result.LastUpdateByUser;
import nl.d2n.dao.result.UserUpdateCount;
import nl.d2n.dao.result.UserUpdateStatistics;
import nl.d2n.model.Citizen;
import nl.d2n.model.GameClock;
import nl.d2n.model.UserStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class ActivityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityService.class);

    public static final Integer HOURS_RECENT        = 48;
    public static final Integer HOURS_VERY_RECENT   = 12;

    public static final Boolean UPDATE_VERY_RECENT  = true;
    public static final Boolean UPDATE_RECENT       = false;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserActionDao userActionDao;

    public List<Citizen> rankCitizens(Integer cityId, List<Citizen> citizens, GameClock gameClock) {
        UserStore<UserUpdateStatistics> statistics = gatherStatistics(cityId, gameClock);
        for (Citizen citizen : citizens) {
            UserUpdateStatistics userStats = statistics.getByTwinoidId(citizen.getId());
            if (userStats == null) {
                userStats = new UserUpdateStatistics();
            }
            citizen.setLastUpdated(userStats.getLastUpdateText(gameClock));
            citizen.setActivityRating(userStats.getActivityRating());
        }
        return citizens;
    }

    protected UserStore<UserUpdateStatistics> gatherStatistics(Integer cityId, GameClock gameClock) {
        UserStore<UserUpdateStatistics> statistics = new UserStore<UserUpdateStatistics>(userDao.findUsersInCity(cityId));
        updateStatistics(statistics, cityId);
//        updateCount(statistics, cityId, gameClock.getHoursAgo(HOURS_VERY_RECENT), gameClock.getDateTime(), UPDATE_VERY_RECENT);
//        updateCount(statistics, cityId, gameClock.getHoursAgo(HOURS_RECENT), gameClock.getHoursAgo(HOURS_VERY_RECENT), UPDATE_RECENT);
        return statistics;
    }

    protected void updateCount(UserStore<UserUpdateStatistics> statistics, Integer cityId, Date startDate, Date endDate, Boolean updateVeryRecent) {
        List<UserUpdateCount> happenedSomeTimeAgo = userActionDao.findRecentChanges(cityId, startDate, endDate);
        for (UserUpdateCount userUpdateCount : happenedSomeTimeAgo) {
            Integer userId = userUpdateCount.getId();
            UserUpdateStatistics statisticsForUser = getStatisticsForUser(statistics, userId);
            if (updateVeryRecent) {
                statisticsForUser.setHappenedVeryRecently(userUpdateCount);
            } else {
                statisticsForUser.setHappenedRecently(userUpdateCount);
            }
        }
    }
    protected void updateStatistics(UserStore<UserUpdateStatistics> statistics, Integer cityId) {
        List<LastUpdateByUser> lastUpdates = userActionDao.findLastUpdateTimes(cityId);
        for (LastUpdateByUser lastUpdate : lastUpdates) {
            Integer userId = lastUpdate.getId();
            UserUpdateStatistics statisticsForUser = getStatisticsForUser(statistics, userId);
            statisticsForUser.setLastUpdate(lastUpdate);
        }
    }
    protected UserUpdateStatistics getStatisticsForUser(UserStore<UserUpdateStatistics> statistics, Integer userId) {
        UserUpdateStatistics statisticsForUser = statistics.getById(userId);
        if (statisticsForUser == null) {
            statisticsForUser = new UserUpdateStatistics();
            statistics.putById(userId, statisticsForUser);
        }
        return statisticsForUser;
    }
}
