package nl.d2n.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class D2NErrorCodeTest {

    @Test
    public void toJson() {
        String json = D2NErrorCode.COULD_NOT_ACCESS_SITE.toJson();
        assertTrue(json.contains(D2NErrorCode.COULD_NOT_ACCESS_SITE.toString()));
        assertTrue(json.contains(D2NErrorCode.COULD_NOT_ACCESS_SITE.getMessage()));
    }

    @Test
    public void isAllowCacheRead() {
        assertEquals(true, D2NErrorCode.COULD_NOT_PARSE_XML.isAllowCacheRead());
        assertEquals(true, D2NErrorCode.COULD_NOT_ACCESS_SITE.isAllowCacheRead());
        assertEquals(true, D2NErrorCode.VERSION_CHECK.isAllowCacheRead());
        assertEquals(true, D2NErrorCode.HORDE_ATTACKING.isAllowCacheRead());
        assertEquals(false, D2NErrorCode.INVALID_KEYS.isAllowCacheRead());
    }
    
    @Test
    public void isSkipLogging() {
        assertEquals(true, D2NErrorCode.HARD_TOWN.isSkipLogging());
        assertEquals(true, D2NErrorCode.CITIZEN_NOT_OUTSIDE.isSkipLogging());
        assertEquals(false, D2NErrorCode.BUILDING_DOES_NOT_EXIST.isSkipLogging());
    }
}
