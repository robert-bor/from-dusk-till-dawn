package nl.d2n.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class UserStoreTest {

    @Test
    public void testConstructor() {
        List<User> users = new ArrayList<User>();
        users.add(createUser(1, "Alpha", 11));
        users.add(createUser(2, "Gamma", 22));
        users.add(createUser(3, "Beta", 33));
        UserStore<String> store = new UserStore<String>(users);
        store.putById(1, "Hello nr 1");
        store.putById(2, "Hello nr 2");
        store.putById(3, "Hello nr 3");
        assertEquals("Hello nr 1", store.getById(1));
        assertEquals("Hello nr 2", store.getById(2));
        assertEquals("Hello nr 3", store.getById(3));
        assertEquals("Hello nr 1", store.getByTwinoidId(11));
        assertEquals("Hello nr 2", store.getByTwinoidId(22));
        assertEquals("Hello nr 3", store.getByTwinoidId(33));
        assertEquals(null, store.getByTwinoidId(44));
    }

    public User createUser(Integer id, String name, Integer gameId) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setGameId(gameId);
        return user;
    }
}
