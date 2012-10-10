package nl.d2n.service;

import nl.d2n.dao.UserDao;
import nl.d2n.model.*;
import nl.d2n.reader.OvalOfficeReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private OvalOfficeReader reader;

    @Autowired
    private UserDao userDao;
    
    public InboxAlert getInboxAlert(final UserKey userKey) throws ApplicationException {
        User user = userDao.find(userKey);
        if (user == null) {
            throw new ApplicationException(D2NErrorCode.USER_NOT_FOUND);
        }
        return reader.pollInbox(user.getGameId());
    }

    public void setReader(OvalOfficeReader reader) { this.reader = reader; }
}
