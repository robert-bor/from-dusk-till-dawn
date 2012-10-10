package nl.d2n.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class UserTest {

    @Test
    public void construct() {
        User user = new User();
        user.setKey(new UserKey("abcdef"));
        user.setName("Rulesy");
        user.setBanned(true);
        user.setSecure(true);
        user.setShunned((true));
        user.setSoulPoints(1256);
        user.setGameId(1984);
        assertEquals("abcdef", user.getKey().getKey());
        assertEquals("Rulesy", user.getName());
        assertEquals(true, user.isBanned());
        assertEquals(true, user.isSecure());
        assertEquals(true, user.isShunned());
        assertEquals((Integer)1256, user.getSoulPoints());
        assertEquals((Integer)1984, user.getGameId());
    }
}
