package nl.d2n.dao.result;

import nl.d2n.model.GameClock;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;

public class UserUpdateStatisticsTest {

    @SuppressWarnings({"NullableProblems"})
    @Test
    public void getRating() {
        GameClock gameClock = new GameClock("2011-08-23 16:23:16");
        UserUpdateStatistics statistics = createStatistics(1234, gameClock.getHoursAgo(7), 1, 7, 3, 15);
        assertEquals(6, statistics.getActivityRating());
        assertEquals("7 hours ago", statistics.getLastUpdateText(gameClock));
    }

    @SuppressWarnings({"NullableProblems"})
    @Test
    public void getStatsForInactiveUser() {
        UserUpdateStatistics statistics = new UserUpdateStatistics();
        assertEquals(UserUpdateStatistics.NO_UPDATES_DONE, statistics.getLastUpdateText(null));
        assertEquals(0, statistics.getActivityRating());
    }

    protected UserUpdateStatistics createStatistics(Integer id, Date lastUpdated, int recentReads, int recentWrites, int veryRecentReads, int veryRecentWrites) {
        UserUpdateStatistics statistics = new UserUpdateStatistics();
        if (lastUpdated != null) {
            statistics.setLastUpdate(new LastUpdateByUser(id, lastUpdated));
        }
        statistics.setHappenedVeryRecently(createUserUpdateCount(id, veryRecentReads, veryRecentWrites));
        statistics.setHappenedRecently(createUserUpdateCount(id, recentReads, recentWrites));
        return statistics;
    }

    protected UserUpdateCount createUserUpdateCount(Integer id, int reads, int writes) {
        return new UserUpdateCount(id, reads, writes);
    }
}
