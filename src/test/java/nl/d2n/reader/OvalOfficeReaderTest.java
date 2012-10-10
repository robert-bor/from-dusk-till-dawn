package nl.d2n.reader;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.ApplicationException;
import nl.d2n.model.D2NErrorCode;
import nl.d2n.model.InboxAlert;
import nl.d2n.reader.sitekey.OvalOfficeSiteKey;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static junit.framework.Assert.assertEquals;

public class OvalOfficeReaderTest extends SpringContextTestCase {

    @Autowired
    private OvalOfficeReader ovalOfficeReader;
    
    @Test
    public void getKey() {
        assertEquals("http://d2n.sindevel.com/oo/api.php?mode=checkmail&apikey=1234567890&userid=1984", ovalOfficeReader.getUrl(1984));
    }
    
    @Test
    public void pollOvalOfficeSuccessful() throws ApplicationException {
        OvalOfficeReader reader = createReader("abcd-efgh-ijkl", "?{\"messages\":\"4\",\"invitations\":\"2\",\"userid\":109665,\"tempkey\":\"c60c9d5abd1578bb26439be0ec4d41ec\",\"valid\":1326027298,\"oourl\":\"http:\\/\\/d2n.sindevel.com\\/oo\\/?user=109665&tkey=c60c9d5abd1578bb26439be0ec4d41ec\"}");
        InboxAlert inboxAlert = reader.pollInbox(109665);
        assertEquals((Integer)4, inboxAlert.getMessages());
        assertEquals((Integer)2, inboxAlert.getInvitations());
        assertEquals((Integer)109665, inboxAlert.getUserId());
        assertEquals("c60c9d5abd1578bb26439be0ec4d41ec", inboxAlert.getTempkey());
        assertEquals(new Long(1326027298), inboxAlert.getValid());
        assertEquals("http://d2n.sindevel.com/oo/?user=109665&tkey=c60c9d5abd1578bb26439be0ec4d41ec", inboxAlert.getOourl());
    }

    @Test
    public void pollOvalOfficeGivesError() throws ApplicationException {
        OvalOfficeReader reader = createReader("abcd-efgh-ijkl", "?{\"error\":1,\"error_msg\":\"Invalid api key.\"}");
        try {
            InboxAlert inboxAlert = reader.pollInbox(109665);
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.OVAL_OFFICE_ERROR, err.getError());
            assertEquals("Invalid api key.", err.getDescription());
        }
    }

    @Test
    public void pollOvalOfficeGivesErrorWithUnparsableError() throws ApplicationException {
        OvalOfficeReader reader = createReader("abcd-efgh-ijkl", "");
        try {
            InboxAlert inboxAlert = reader.pollInbox(109665);
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.OVAL_OFFICE_ERROR, err.getError());
            assertEquals("Invalid api key.", err.getDescription());
        }
    }

    protected OvalOfficeSiteKey createSiteKey(String siteKeyText) {
        OvalOfficeSiteKey siteKey = new OvalOfficeSiteKey();
        siteKey.setKey(siteKeyText);
        return siteKey;
    }
    
    protected OvalOfficeReader createReader(String siteKey, String jsonBody) {
        OvalOfficeReader reader = new OvalOfficeReader();
        reader.setSiteKey(createSiteKey(siteKey));
        reader.setReader(new XmlReader() {
            public String readStringFromUrl(String url) throws IOException {
                return "?{\"messages\":\"4\",\"invitations\":\"2\",\"userid\":109665,\"tempkey\":\"c60c9d5abd1578bb26439be0ec4d41ec\",\"valid\":1326027298,\"oourl\":\"http:\\/\\/d2n.sindevel.com\\/oo\\/?user=109665&tkey=c60c9d5abd1578bb26439be0ec4d41ec\"}";
            }
        });
        return reader;
    }
}
