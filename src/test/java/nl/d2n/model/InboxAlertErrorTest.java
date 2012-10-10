package nl.d2n.model;

import com.google.gson.Gson;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class InboxAlertErrorTest {

    @Test
    public void deserializeJson() {
        String jsonBody = "{\"error\":1,\"error_msg\":\"Invalid api key.\"}";
        Gson gson = new Gson();
        InboxAlertError error = gson.fromJson(jsonBody, InboxAlertError.class);
        assertEquals((Integer)1, error.getError());
        assertEquals("Invalid api key.", error.getError_msg());
    }
}
