package nl.d2n.dao.result;

import com.ocpsoft.pretty.time.PrettyTime;
import nl.d2n.model.GameClock;

import java.util.Date;

public class UserUpdateStatistics {

    public static final String NO_UPDATES_DONE = "never";

    private LastUpdateByUser lastUpdate;
    private UserUpdateCount happenedRecently;
    private UserUpdateCount happenedVeryRecently;

    public UserUpdateCount getHappenedRecently() { return happenedRecently; }
    public UserUpdateCount getHappenedVeryRecently() { return happenedVeryRecently; }
    public LastUpdateByUser getLastUpdate() { return lastUpdate; }

    public void setHappenedRecently(UserUpdateCount happenedRecently) { this.happenedRecently = happenedRecently; }
    public void setHappenedVeryRecently(UserUpdateCount happenedVeryRecently) { this.happenedVeryRecently = happenedVeryRecently; }
    public void setLastUpdate(LastUpdateByUser lastUpdate) { this.lastUpdate = lastUpdate; }

    public String getLastUpdateText(GameClock gameClock) {
        if (getLastUpdate() == null) {
            return NO_UPDATES_DONE;
        }
        PrettyTime t = new PrettyTime(new Date(0));
        return t.format(new Date(lastUpdate.getUpdated().getTime() - gameClock.getDateTime().getTime()));
    }
    public int getActivityRating() {
        int rating = 0;
        UserUpdateCount count = getHappenedRecently();
        if (count != null) {
            if (count.getReads() > 0) {
                rating++;
            }
            if (count.getWrites() > 0) {
                rating++;
            }
        }
        count = getHappenedVeryRecently();
        if (count != null) {
            if (count.getReads() > 0) {
                rating++;
            }
            if (count.getWrites() > 0) {
                rating++;
            }
            if (count.getWrites() > 5) {
                rating++;
            }
            if (count.getWrites() > 10) {
                rating++;
            }
        }
        return rating;
    }
}
