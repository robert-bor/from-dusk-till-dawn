package nl.d2n.reader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import nl.d2n.model.ApplicationException;
import nl.d2n.model.D2NErrorCode;
import nl.d2n.model.InboxAlert;
import nl.d2n.model.InboxAlertError;
import nl.d2n.reader.sitekey.OvalOfficeSiteKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.TreeMap;

@Service
public class OvalOfficeReader {

    public final static String BASE_URL = "http://d2n.sindevel.com/oo/api.php?mode=checkmail&apikey=[SITE_KEY]&userid=[USER_ID]";

    @Autowired
    private OvalOfficeSiteKey siteKey;
    
    @Autowired
    private XmlReader urlReader;

    public String getUrl(Integer userId) {
        return BASE_URL.replaceAll("\\[SITE_KEY\\]", siteKey.getKey()).replaceAll("\\[USER_ID\\]", userId.toString());
    }
    
    public InboxAlert pollInbox(Integer userId) throws ApplicationException {
        if (userId == null) {
            throw new ApplicationException(D2NErrorCode.USER_NOT_FOUND);
        }
        String url = getUrl(userId);
        final String jsonBody;
        try {
            jsonBody = urlReader.readStringFromUrl(url);
        } catch (IOException err) {
            throw new ApplicationException(D2NErrorCode.OVAL_OFFICE_ERROR, "Could not access Oval Office");
        }
        Gson gson = new Gson();
        try {
            return gson.fromJson(jsonBody.substring(1), InboxAlert.class);
        } catch (Exception err) {
            String errorMessage;
            try {
                InboxAlertError inboxAlertError = gson.fromJson(jsonBody.substring(1), InboxAlertError.class);
                errorMessage = inboxAlertError.getError_msg();
            } catch (Exception errorMessageParseError) {
                errorMessage = "Unable to parse error message";
            }
            throw new ApplicationException(D2NErrorCode.OVAL_OFFICE_ERROR, errorMessage);
        }
    }

    public void setSiteKey(OvalOfficeSiteKey siteKey) { this.siteKey = siteKey; }
    public void setReader(XmlReader reader) { this.urlReader = reader; }
}
