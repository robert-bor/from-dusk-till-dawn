package nl.d2n.dao;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.UniqueInsideBuilding;
import nl.d2n.model.UniqueOutsideBuilding;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class UniqueInsideBuildingDaoTest extends SpringContextTestCase {

    @Test
    public void saveItem() {
        UniqueInsideBuilding building = new UniqueInsideBuilding(1042, "Upgraded Map", true, 1050, "item_electro", "Lorem ipsum", null, false, false);
        building.setUrl("http://somewiki/upgraded_map");

        uniqueInsideBuildingDao.save(building);
        List<UniqueInsideBuilding> buildings = uniqueInsideBuildingDao.findUniqueInsideBuildings();
        assertEquals(1, buildings.size());
        building = buildings.get(0);

        assertEquals((Integer)1042, building.getId());
        assertEquals("Upgraded Map", building.getName());
        assertEquals(true, building.isTemporary());
        assertEquals((Integer)1050, building.getParent());
        assertEquals("item_electro", building.getImage());
        assertEquals("Lorem ipsum", building.getFlavor());
        assertEquals("http://somewiki/upgraded_map", building.getUrl());
    }

}
