package nl.d2n.service;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.Distinction;
import nl.d2n.model.UniqueDistinction;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class UniqueDistinctionManagerTest extends SpringContextTestCase {

    @Autowired
    UniqueDistinctionManager manager;

    @Before
    public void saveUniqueDistinctions() {
        uniqueDistinctionDao.save(new UniqueDistinction("Butcher", false, "r_butch", false));
        uniqueDistinctionDao.save(new UniqueDistinction("Camper", false, "r_camper", false));
        uniqueDistinctionDao.save(new UniqueDistinction("Luck", true, "r_luck", false));
        manager.refresh();
    }

    @Test
    public void construct() {
        assertEquals(3, manager.getMap().size());
    }

    @Test
    public void checkOrder() {
        uniqueDistinctionDao.save(new UniqueDistinction("Butcher", false, "r_butch", false));
        uniqueDistinctionDao.save(new UniqueDistinction("Camper", false, "r_camper", false));
        uniqueDistinctionDao.save(new UniqueDistinction("Luck", true, "r_luck", false));
        List<UniqueDistinction> distinctions = manager.findUniqueObjectsOrdered();
        assertEquals("Luck", distinctions.get(0).getName());
        assertEquals("Butcher", distinctions.get(1).getName());
        assertEquals("Camper", distinctions.get(2).getName());
    }

    @Test
    public void mergeWithList() {
        List<Distinction> distinctions = new ArrayList<Distinction>();
        distinctions.add(new Distinction(null, "Butcher", false, 42, "r_botch", false, null, null));
        distinctions.add(new Distinction(null, "Camper", true, 18, "r_camper", false, null, null));
        distinctions.add(new Distinction(null, "Last Man Standing", true, 1, "r_lms", false, null, null));
        manager.checkForExistence(distinctions);
        assertEquals(4, manager.getMap().size());
        assertUniqueDistinction(manager.get("Butcher"), "Butcher", false, "r_botch");
        assertUniqueDistinction(manager.get("Camper"), "Camper", true, "r_camper");
        assertUniqueDistinction(manager.get("Luck"), "Luck", true, "r_luck");
        assertUniqueDistinction(manager.get("Last Man Standing"), "Last Man Standing", true, "r_lms");
    }

    protected void assertUniqueDistinction(UniqueDistinction distinction, String name, boolean rare, String image) {
        assertEquals(name, distinction.getName());
        assertEquals(image, distinction.getImage());
        assertEquals(rare, distinction.isRare());
    }

}
