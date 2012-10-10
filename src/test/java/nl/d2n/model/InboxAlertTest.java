package nl.d2n.model;

import com.google.gson.Gson;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class InboxAlertTest {

    @Test
    public void deserializeJson() {
        String jsonBody = "{\"messages\":\"4\",\"invitations\":\"2\",\"userid\":109665,\"tempkey\":\"c60c9d5abd1578bb26439be0ec4d41ec\",\"valid\":1326027298,\"oourl\":\"http:\\/\\/d2n.sindevel.com\\/oo\\/?user=109665&tkey=c60c9d5abd1578bb26439be0ec4d41ec\"}";
        Gson gson = new Gson();
        InboxAlert inboxAlert = gson.fromJson(jsonBody, InboxAlert.class);
        assertEquals((Integer)4, inboxAlert.getMessages());
        assertEquals((Integer)2, inboxAlert.getInvitations());
        assertEquals((Integer)109665, inboxAlert.getUserId());
        assertEquals("c60c9d5abd1578bb26439be0ec4d41ec", inboxAlert.getTempkey());
        assertEquals(new Long(1326027298), inboxAlert.getValid());
        assertEquals("http://d2n.sindevel.com/oo/?user=109665&tkey=c60c9d5abd1578bb26439be0ec4d41ec", inboxAlert.getOourl());
    }
}
