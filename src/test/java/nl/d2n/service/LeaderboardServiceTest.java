package nl.d2n.service;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.UniqueDistinction;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static junit.framework.Assert.*;

public class LeaderboardServiceTest extends SpringContextTestCase {

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private UniqueDistinctionManager manager;

    @Test
    public void getUniqueDistinctions() {
        uniqueDistinctionDao.save(new UniqueDistinction("Butcher", false, "r_butch", false));
        manager.refresh();
        assertEquals(1, leaderboardService.findUniqueDistinctions().size());
    }

}
