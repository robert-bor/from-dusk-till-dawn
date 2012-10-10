package nl.d2n.service;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.ApplicationException;
import nl.d2n.model.D2NErrorCode;
import nl.d2n.model.UserKey;
import nl.d2n.util.ClassCreator;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class UserSecurityCheckTest extends SpringContextTestCase {

    @Resource
    ClassCreator classCreator;

    @Autowired
    UserSecurityCheck userSecurityCheck;

    @Test
    public void userNotSecure() {
        classCreator.createUser("Irrelevant", new UserKey("abcdef"), false, false, false);
        try {
            userSecurityCheck.checkUser(new UserKey("abcdef"), true, true, true);
            fail("Should have thrown exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.KEY_NOT_SECURE, err.getError());
        }
    }

    @Test
    public void userShunned() {
        classCreator.createUser("Irrelevant", new UserKey("abcdef"), false, true, false);
        try {
            userSecurityCheck.checkUser(new UserKey("abcdef"), true, false, true);
            fail("Should have thrown exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.CITIZEN_SHUNNED, err.getError());
        }
    }

    @Test
    public void userBanned() {
        classCreator.createUser("Irrelevant", new UserKey("abcdef"), false, false, true);
        try {
            userSecurityCheck.checkUser(new UserKey("abcdef"), true, false, true);
            fail("Should have thrown exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.USER_BANNED, err.getError());
        }
    }

    @Test
    public void userDoesNotExistYetAndAllowFirstTimeAccess() {
        try {
            userSecurityCheck.checkUser(new UserKey("abcdef"), true, false, true);
        } catch (ApplicationException err) {
            fail("Should not have thrown an exception");
        }
    }

    @Test
    public void userDoesNotExistYetAndDoNotAllowFirstTimeAccess() {
        try {
            userSecurityCheck.checkUser(new UserKey("abcdef"), false, false, true);
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.USER_NOT_FOUND, err.getError());
        }
    }
}
