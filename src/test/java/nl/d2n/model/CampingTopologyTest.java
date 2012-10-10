package nl.d2n.model;

import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class CampingTopologyTest {
    
    @Test
    public void testMap() {
        Map<CampingTopology, String> map = CampingTopology.getCampingTopologyMap();
        assertTrue(map.get(CampingTopology.L1_SUICIDE).contains("Sleeping somewhere"));
    }
    
    @Test
    public void testGetTopologyByStartText() {
        assertEquals(7, CampingTopology.getTopologyByStartText().size());
    }
}
