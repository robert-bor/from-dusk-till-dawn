package nl.d2n.dao;

import nl.d2n.dao.result.UserWithDistinction;
import nl.d2n.dao.result.UserWithProfile;
import nl.d2n.model.Distinction;
import nl.d2n.model.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class DistinctionDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void saveDistinction(final Distinction distinction) {
        entityManager.merge(distinction);
    }

    @SuppressWarnings("unchecked")
    public List<UserWithDistinction> findTopUsersWithDistinction(Integer uniqueDistinctionId, int amount) {
        return (List<UserWithDistinction>)entityManager
                .createQuery(
                        "select new nl.d2n.dao.result.UserWithDistinction(u.gameId, u.name, d.amount) "+
                        "from User u, Distinction d where u=d.user and d.uniqueDistinctionId=:uniqueDistinctionId "+
                        "order by d.amount desc"
                )
                .setMaxResults(amount)
                .setParameter("uniqueDistinctionId", uniqueDistinctionId)
                .getResultList();
    }

    @SuppressWarnings({"unchecked"})
    @Cacheable(value = "distinctions", key="#cityId")
    public Map<Integer, UserWithProfile> findUsersWithDistinctions(Integer cityId, List<Integer> userIds) {
        Map<Integer, UserWithProfile> profiles = new TreeMap<Integer, UserWithProfile>();
        if (userIds == null || userIds.size() == 0) {
            return profiles;
        }

        List results = entityManager
                .createQuery("select u, d from User u, Distinction d where u=d.user and u.id in (:user_ids)")
                .setParameter("user_ids", userIds)
                .getResultList();

        for (Object result : results) {
            Object[] resultArray = (Object[]) result;
            User user = (User)resultArray[0];
            Distinction distinction = (Distinction)resultArray[1];
            UserWithProfile profile = profiles.get(user.getGameId());
            if (profile == null) {
                profile = new UserWithProfile();
                profile.setSoulPoints(user.getSoulPoints());
                profile.setImage(user.getImage());
                profile.setDescription(user.getDescription());
                profiles.put(user.getGameId(), profile);
            }
            profile.addDistinction(distinction);
        }
        return profiles;
    }

    @SuppressWarnings({"unchecked"})
    public List<Distinction> findDistinctionsOfUser(String userName) {
        return (List<Distinction>)entityManager
                .createQuery("from Distinction d where d.user.name = :username")
                .setParameter("username", userName)
                .getResultList();
    }

    @Transactional
    public void deleteDistinctions() {
        entityManager
                .createQuery("delete from Distinction")
                .executeUpdate();
    }
}
