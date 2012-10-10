package nl.d2n.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class UserKeyTest {

    @Test
    public void invalidUserKey() {
        try {
            UserKey key = new UserKey("Broken bones");
            key.check();
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.TAMPERED_KEY, err.getError());
        }
    }

    @Test
    public void validUserKey() {
        try {
            UserKey key = new UserKey("ef97c7a299f87a8d4fd89915c2a56eb6a31774");
            key.check();
        } catch (ApplicationException err) {
            fail("Should not have thrown an exception");
        }
    }
}
