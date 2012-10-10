package nl.d2n.model;

import junit.framework.Assert;
import nl.d2n.util.GsonUtil;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class ZoneDangerTest {
    @Test
    public void zombiesToDangerClass() {
        Assert.assertEquals(ZoneDanger.NONE, ZoneDanger.getClassWithZombies(0));
        assertEquals(ZoneDanger.ONE_TO_THREE, ZoneDanger.getClassWithZombies(2));
        assertEquals(ZoneDanger.THREE_TO_FIVE, ZoneDanger.getClassWithZombies(3));
        assertEquals(ZoneDanger.FIVE_PLUS, ZoneDanger.getClassWithZombies(7));
        assertEquals(ZoneDanger.NINE_PLUS, ZoneDanger.getClassWithZombies(9));
    }

    @Test
    public void testMap() {
        Map<ZoneDanger, Map<String, Object>> dangerMap = ZoneDanger.getDangersAsMap();
        assertEquals(ZoneDanger.ONE_TO_THREE.getClassTag(), dangerMap.get(ZoneDanger.ONE_TO_THREE).get("class"));
        assertEquals(ZoneDanger.ONE_TO_THREE.getZedAmount(), dangerMap.get(ZoneDanger.ONE_TO_THREE).get("amountNormal"));
        assertEquals(ZoneDanger.ONE_TO_THREE.getTwoDigitRange(), dangerMap.get(ZoneDanger.ONE_TO_THREE).get("amountTwoDigits"));
        assertEquals(ZoneDanger.ONE_TO_THREE.isShowTwoDigitRange(), dangerMap.get(ZoneDanger.ONE_TO_THREE).get("showTwoDigits"));
    }
}
