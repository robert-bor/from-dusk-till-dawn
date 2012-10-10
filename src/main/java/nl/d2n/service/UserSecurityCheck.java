package nl.d2n.service;

import nl.d2n.dao.UserDao;
import nl.d2n.model.ApplicationException;
import nl.d2n.model.D2NErrorCode;
import nl.d2n.model.User;
import nl.d2n.model.UserKey;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserSecurityCheck {

    public final static boolean ALLOW_FIRST_TIME                = true;
    public final static boolean DO_NOT_ALLOW_FIRST_TIME         = false;

    public final static boolean CHECK_FOR_SECURE_SETTING        = true;
    public final static boolean DO_NOT_CHECK_FOR_SECURE_SETTING = false;

    public final static boolean CHECK_FOR_SHUNNED_SETTING       = true;
    public final static boolean DO_NOT_CHECK_FOR_SHUNNED_SETTING= false;

    @Resource
    UserDao userDao;

    public void checkUser(final UserKey userKey, final boolean allowFirstTime, final boolean checkSecure,
                          final boolean checkShunned) throws ApplicationException {
        User user = userDao.find(userKey);
        if (user == null) {
            if (allowFirstTime) {
                return; // Unproven, so give her a chance
            } else {
                throw new ApplicationException(D2NErrorCode.USER_NOT_FOUND);
            }
        }
        if (checkSecure && !user.isSecure()) {
            throw new ApplicationException(D2NErrorCode.KEY_NOT_SECURE);
        }
        if (checkShunned && user.isShunned()) {
            throw new ApplicationException(D2NErrorCode.CITIZEN_SHUNNED);
        }
        if (user.isBanned()) {
            throw new ApplicationException(D2NErrorCode.USER_BANNED);
        }
    }

}
