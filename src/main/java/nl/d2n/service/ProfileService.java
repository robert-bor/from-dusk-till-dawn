package nl.d2n.service;

import nl.d2n.dao.DistinctionDao;
import nl.d2n.dao.UserDao;
import nl.d2n.dao.result.UserWithProfile;
import nl.d2n.model.*;
import nl.d2n.reader.SoulXmlReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ProfileService {

    @Autowired
    private SoulXmlReader soulXmlReader;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DistinctionDao distinctionDao;

    @Autowired
    private UniqueDistinctionManager uniqueDistinctionManager;

    @Autowired
    private UniqueTitleManager uniqueTitleManager;

    public Map<Integer, List<Distinction>> getDistinctions(Integer cityId, List<Citizen> citizens) {
        Map<Integer, List<Distinction>> distinctionsGroupedByCitizen = new TreeMap<Integer, List<Distinction>>();
        Map<Integer, UniqueDistinction> uniqueDistinctions = uniqueDistinctionManager.getMapWithIntegerKeys();
        Map<Integer, UserWithProfile> profiles = distinctionDao.findUsersWithDistinctions(cityId, userDao.findUserIds(citizens));
        for (Citizen citizen : citizens) {
            UserWithProfile profile = profiles.get(citizen.getId());
            if (profile == null) {
                continue;
            }
            List<Distinction> distinctions = profile.getDistinctions();
            uniqueDistinctionManager.appendMissingFields(distinctions);
            Collections.sort(distinctions, new DistinctionComparator());
            distinctionsGroupedByCitizen.put(citizen.getId(), distinctions);
            citizen.setSpecialImage(profile.getImage());
            citizen.setSpecialDescription(profile.getDescription());
            citizen.setSoulPoints(profile.getSoulPoints());
        }
        return distinctionsGroupedByCitizen;
    }

    public class DistinctionComparator implements Comparator<Distinction> {
        public int compare(Distinction thisDistinction, Distinction otherDistinction) {
            if (thisDistinction.isRare() != otherDistinction.isRare()) {
                return -((Boolean)thisDistinction.isRare()).compareTo(otherDistinction.isRare());
            }
            return -((Integer)thisDistinction.getAmount()).compareTo(otherDistinction.getAmount());
        }
    }

    public void updateProfile(UserKey userKey) throws ApplicationException {
        User user = userDao.find(userKey);
        if (user == null) {
            throw new ApplicationException(D2NErrorCode.USER_NOT_FOUND);
        }
        updateProfile(user);
    }
    public void updateProfile(User user) throws ApplicationException {
        if (!user.isSecure()) {
            throw new ApplicationException(D2NErrorCode.KEY_NOT_SECURE);
        }
        Profile profile = soulXmlReader.read(user.getKey());
        if (profile == null) {
            throw new ApplicationException(D2NErrorCode.NOT_IN_GAME);
        }
        uniqueDistinctionManager.checkForExistence(profile.getDistinctions());
        uniqueTitleManager.checkForExistence(uniqueDistinctionManager.deriveTitlesFromProfile(profile.getDistinctions()));

        // Load the distinctions of the current user
        List<Distinction> distinctions = distinctionDao.findDistinctionsOfUser(user.getName());
        uniqueDistinctionManager.appendMissingFields(distinctions);
        Map<String, Distinction> distinctionsOfUser = convertListToMap(distinctions);

        for (Distinction distinction : profile.getDistinctions()) {
            Distinction existingDistinction = distinctionsOfUser.get(distinction.getName());
            if (existingDistinction == null) {
                existingDistinction = distinction;
                existingDistinction.setUniqueDistinctionId(uniqueDistinctionManager.get(distinction.getName()).getId());
                existingDistinction.setUser(user);
                distinctionDao.saveDistinction(distinction);
            } else if (distinction.getAmount() != existingDistinction.getAmount()) {
                existingDistinction.setAmount(distinction.getAmount());
                distinctionDao.saveDistinction(existingDistinction);
            }
        }
        user.setSoulPoints(profile.getSoulPoints());
        userDao.save(user);
    }

    protected Map<String, Distinction> convertListToMap(List<Distinction> distinctions) {
        Map<String, Distinction> distinctionMap = new TreeMap<String, Distinction>();
        for (Distinction distinction : distinctions) {
            distinctionMap.put(distinction.getName(), distinction);
        }
        return distinctionMap;
    }

    public void setSoulXmlReader(SoulXmlReader soulXmlReader) { this.soulXmlReader = soulXmlReader; }
}
