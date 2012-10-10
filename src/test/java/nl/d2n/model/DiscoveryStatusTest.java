package nl.d2n.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DiscoveryStatusTest {

    @SuppressWarnings({"NullableProblems"})
    @Test
    public void getHighestDiscoveryStatus() {
        assertEquals(
                DiscoveryStatus.DISCOVERED_AND_VISITED_TODAY,
                DiscoveryStatus.DISCOVERED_AND_VISITED_TODAY.getHighestDiscoveryStatus(null));
    }
}
