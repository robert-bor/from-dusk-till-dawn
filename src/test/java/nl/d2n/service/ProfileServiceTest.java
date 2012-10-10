package nl.d2n.service;

import nl.d2n.SpringContextTestCase;
import nl.d2n.model.*;
import nl.d2n.reader.SoulXmlReaderFromFile;
import nl.d2n.util.ClassCreator;
import nl.d2n.util.UserCitizenConverter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCacheManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ProfileServiceTest extends SpringContextTestCase {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ClassCreator classCreator;

    @Autowired
    private EhCacheCacheManager cacheManager;

    @Before
    public void stubSoulReader() {
        profileService.setSoulXmlReader(new SoulXmlReaderFromFile("soul.xml"));
        cacheManager.getCache("distinctions").clear();
    }

    @Test
    public void testReadSoulNotSecure() {
        User user = classCreator.createUser("BerZerg", new UserKey("cafebabe"), false, false, false); // Not secure!
        try {
            profileService.updateProfile(user);
            fail("Should have thrown an exception because of insecure key");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.KEY_NOT_SECURE, err.getError());
        }
    }

    @Test
    public void testReadSoul() throws ApplicationException {
        User user = classCreator.createUser("BerZerg", new UserKey("CAFEBABE"), 11, true, false, false, null);
        profileService.updateProfile(user);
        List<Distinction> distinctions = profileService.getDistinctions(1984, UserCitizenConverter.convertUserToCitizen(user)).get(11);
        assertEquals(45, distinctions.size());
    }

    @Test
    public void testUserNotInGame() {
        User user = classCreator.createUser("BerZerg", new UserKey("CAFEBABE"), true, false, false);
        profileService.setSoulXmlReader(new SoulXmlReaderFromFile("") {
            public Profile read(final UserKey userKey) throws ApplicationException {
                return null;
            }
        });
        try {
            profileService.updateProfile(user);
            fail("Should have thrown an exception because user is not in game");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.NOT_IN_GAME, err.getError());
        }
    }

    @Test
    public void testUpdateSoulUserDoesNotExist() {
        try {
            profileService.updateProfile(new UserKey("cafebabe"));
            fail("Should have thrown an exception because user with key does not exist");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.USER_NOT_FOUND, err.getError());
        }
    }

    @Test
    public void testSetSoulAndUpdate() throws ApplicationException {
        User user = classCreator.createUser("BerZerg", new UserKey("CAFEBABE"), 11, true, false, false, null);
        profileService.updateProfile(user);
        List<Distinction> distinctions = profileService.getDistinctions(1984, UserCitizenConverter.convertUserToCitizen(user)).get(11);
        assertEquals(45, distinctions.size());

        profileService.setSoulXmlReader(new SoulXmlReaderFromFile("soul-updated.xml"));
        profileService.updateProfile(user);

        cacheManager.getCache("distinctions").clear();

        distinctions = profileService.getDistinctions(1984, UserCitizenConverter.convertUserToCitizen(user)).get(11);
        assertEquals(46, distinctions.size());

        assertDistinctionsContain(distinctions, "Roleplayer", 19);
        assertDistinctionsContain(distinctions, "Crappy Programmer", 1);

        user = userDao.find(new UserKey("CAFEBABE"));
        assertEquals((Integer)1084, user.getSoulPoints());
    }
    protected void assertDistinctionsContain(List<Distinction> distinctions, String distinctionName, int amount) {
        for (Distinction distinction : distinctions) {
            if (distinction.getName().equals(distinctionName)) {
                assertEquals(amount, distinction.getAmount());
                return;
            }
        }
        fail("Should have found a distinction with name "+distinctionName);
    }
}
