package nl.d2n.dao;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.ItemCategory;
import nl.d2n.model.UniqueItem;
import nl.d2n.model.UniqueOutsideBuilding;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class UniqueOutsideBuildingDaoTest extends SpringContextTestCase {

    @Test
    public void saveItem() {
        UniqueOutsideBuilding building = new UniqueOutsideBuilding(44, "Garden Shed", "Lorem ipsum", null);
        building.setUrl("http://somewiki/garden_shed");
        uniqueOutsideBuildingDao.save(building);
        List<UniqueOutsideBuilding> buildings = uniqueOutsideBuildingDao.findUniqueOutsideBuildings();
        assertEquals(1, buildings.size());
        building = buildings.get(0);
        assertEquals((Integer)44, building.getId());
        assertEquals("Garden Shed", building.getName());
        assertEquals("Lorem ipsum", building.getFlavor());
        assertEquals("http://somewiki/garden_shed", building.getUrl());
    }

}
