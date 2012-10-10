package nl.d2n.dao;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.ItemCategory;
import nl.d2n.model.UniqueDistinction;
import nl.d2n.model.UniqueItem;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class UniqueDistinctionDaoTest extends SpringContextTestCase {

    @Test
    public void saveItem() {
        UniqueDistinction distinction = new UniqueDistinction();
        distinction.setName("Butcher");
        distinction.setImage("r_butch");
        distinction.setRare(true);
        distinction.setInSprite(true);
        uniqueDistinctionDao.save(distinction);
        List<UniqueDistinction> distinctions = uniqueDistinctionDao.findUniqueDistinctions();
        assertEquals(1, distinctions.size());
        distinction = distinctions.get(0);
        assertEquals("Butcher", distinction.getName());
        assertEquals("r_butch", distinction.getImage());
        assertEquals(true, distinction.isRare());
        assertEquals(true, distinction.isInSprite());
    }
}
