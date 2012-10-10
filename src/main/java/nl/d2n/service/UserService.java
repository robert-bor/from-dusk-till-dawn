package nl.d2n.service;

import nl.d2n.dao.UserDao;
import nl.d2n.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProfileService profileService;

    public void removeCitizenFromTown(UserKey userKey) {
        removeUserFromCity(userDao.find(userKey));
    }
    
    public void removeDeadCitizensFromTown(final City city, List<Citizen> aliveCitizens) throws ApplicationException {
        List<User> usersInCity = userDao.findDeadUsersInTown(city.getId(), aliveCitizens);
        for (User userInCity : usersInCity) {
            removeUserFromCity(userInCity);
        }
    }
    
    protected void removeUserFromCity(User user) {
        user.setCity(null);
        userDao.save(user);
    }
    
    public void updateUser(final City city, final UserKey userKey, final Integer gameId,
                           final String name, final boolean secure,
                           final boolean shunned, List<Citizen> citizens) throws ApplicationException {

        User user = userDao.find(userKey);
        boolean update = false;
        boolean updateProfile = false;
        boolean updateOtherProfiles = false;
        if (user == null) {
            user = userDao.findByGameId(gameId);
            if (user != null) { // Does not yet exist with another key
                user.setKey(userKey);
            } else {
                user = createNewUser(userKey, name, city);
            }
            update = true;
            updateProfile = true;
        } else if (city != null && !userDao.isUserInCity(user.getId(), city.getId())) {
            user.setCity(city);
            update = true;
            updateProfile = true;
            updateOtherProfiles = true;
        }
        update |= updateName(user, name, user.getName());
        update |= updateGameId(user, gameId, user.getGameId());
        update |= updateSecureSetting(user, secure, user.isSecure());
        update |= updateShunnedSetting(user, shunned, user.isShunned());

        if (update) {
            userDao.save(user);
        }
        if (updateProfile && secure) {
            profileService.updateProfile(userDao.find(userKey));
        }
        if (updateOtherProfiles && secure) {
            for (User userToUpdate : userDao.findUsersNotYetRegisteredToSameTown(city.getId(), citizens)) {
                try {
                    profileService.updateProfile(userToUpdate);
                    userToUpdate.setCity(city);
                    userDao.save(userToUpdate);
                } catch (Exception err) {
                    LOGGER.error("Error processing profile for user :"+userToUpdate.getName());
                }
            }
        }
    }

    protected User createNewUser(UserKey userKey, String name, City city) {
        User user = new User();
        user.setKey(userKey);
        user.setName(name);
        user.setCity(city);
        return user;
    }

    protected boolean updateName(User user, String fromXml, String fromDatabase) {
        if (fromXml != null && (fromDatabase == null || !fromXml.equals(fromDatabase))) {
            user.setName(fromXml);
            return true;
        }
        return false;
    }
    
    protected boolean updateGameId(User user, Integer fromXml, Integer fromDatabase) {
        if (fromXml != null && (fromDatabase == null || !fromXml.equals(fromDatabase))) {
            user.setGameId(fromXml);
            return true;
        }
        return false;
    }
    
    protected boolean updateSecureSetting(User user, boolean fromXml, boolean fromDatabase) {
        if (fromXml != fromDatabase) {
            user.setSecure(fromXml);
            return true;
        }
        return false;
    }

    protected boolean updateShunnedSetting(User user, boolean fromXml, boolean fromDatabase) {
        if (fromXml != fromDatabase) {
            user.setShunned(fromXml);
            return true;
        }
        return false;
    }

    public void setProfileService(ProfileService profileService) { this.profileService = profileService; }
}
