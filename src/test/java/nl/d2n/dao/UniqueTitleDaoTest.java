package nl.d2n.dao;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.UniqueDistinction;
import nl.d2n.model.UniqueTitle;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class UniqueTitleDaoTest extends SpringContextTestCase {

    @Test
    public void saveItem() {
        UniqueTitle title = new UniqueTitle();
        title.setName("Last Boy Scout");
        title.setTwinoidPoints(6.5);
        title.setTreshold(15);
        title.setUniqueDistinctionId(270);
        uniqueTitleDao.save(title);
        List<UniqueTitle> titles = uniqueTitleDao.findUniqueTitles();
        assertEquals(1, titles.size());
        title = titles.get(0);
        assertEquals("Last Boy Scout", title.getName());
        assertEquals(6.5, title.getTwinoidPoints());
        assertEquals(15, title.getTreshold());
        assertEquals((Integer)270, title.getUniqueDistinctionId());
    }
}
