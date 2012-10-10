package nl.d2n.model;

import nl.d2n.util.GsonUtil;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class UserActionTest {

    @Test
    public void construct() {
        Date date = new GameClock("2011-01-01 18:32:20").getDateTime();
        UserAction action = createAction(date);
        assertEquals((Integer) 1984, action.getId());
        assertEquals(UpdateAction.ADD_BLUEPRINT, action.getAction());
        assertEquals(date, action.getUpdated());
    }

    @Test
    public void toJson() {
        Date date = new GameClock("2011-01-01 18:32:20").getDateTime();
        UserAction action = createAction(date);
        String json = GsonUtil.objectToJson(action);
        assertTrue(json.contains("2011-01-01 18:32:20"));
    }

    protected UserAction createAction(Date date) {
        UserAction action = new UserAction();
        action.setCity(new City());
        action.setUser(new User());
        action.setZone(new Zone());
        action.setId(1984);
        action.setAction(UpdateAction.ADD_BLUEPRINT);
        action.setUpdated(date);
        return action;
    }
}
