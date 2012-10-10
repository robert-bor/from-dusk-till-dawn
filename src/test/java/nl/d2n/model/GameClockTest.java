package nl.d2n.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class GameClockTest {

    @Test
    public void gameClock() throws InterruptedException {
        GameClock clock = new GameClock("2011-01-01 10:00:00");
        assertEquals("2011-01-01", clock.getDateTimeAsString().substring(0, 10));
    }

    @SuppressWarnings({"deprecation"})
    @Test
    public void hoursAgo() throws InterruptedException {
        GameClock clock = new GameClock("2011-01-01 10:10:00");
        assertEquals(3, clock.getHoursAgo(7).getHours());
    }
}
