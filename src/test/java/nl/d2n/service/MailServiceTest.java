package nl.d2n.service;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.ApplicationException;
import nl.d2n.model.D2NErrorCode;
import nl.d2n.model.InboxAlert;
import nl.d2n.model.UserKey;
import nl.d2n.reader.OvalOfficeReader;
import nl.d2n.util.ClassCreator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import static junit.framework.Assert.*;

public class MailServiceTest extends SpringContextTestCase {

    @Resource
    private ClassCreator classCreator;

    @Autowired
    private MailService mailService;
    
    @Autowired
    private OvalOfficeReader reader;

    @Before
    public void injectService() {
        mailService.setReader(reader);
    }
    
    @Test
    public void userDoesNotExist() {
        try {
            mailService.getInboxAlert(new UserKey("abcdef"));
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.USER_NOT_FOUND, err.getError());
        }
    }
    
    @Test
    public void pollInbox() throws ApplicationException {
        mailService.setReader(new OvalOfficeReader() {
            public InboxAlert pollInbox(Integer userId) throws ApplicationException {
                return new InboxAlert();
            }
        });
        classCreator.createUser("Heltharion", new UserKey("abcdef"), true, false, false, null);
        assertNotNull(mailService.getInboxAlert(new UserKey("abcdef")));
    }
}
