package nl.d2n.dao;

import nl.d2n.dao.result.LastUpdateByUser;
import nl.d2n.dao.result.UserUpdateCount;
import nl.d2n.model.UserAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Repository
public class UserActionDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserActionDao.class);

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void save(final UserAction userAction) {
        entityManager.merge(userAction);
    }

    @SuppressWarnings({"unchecked"})
    public List<UserAction> findActions(int cityId, int userId) {
        return (List<UserAction>)entityManager
                .createQuery("from UserAction ua where ua.city.id = :city_id and ua.user.id = :user_id")
                .setParameter("city_id", cityId)
                .setParameter("user_id", userId)
                .getResultList();
    }

    @SuppressWarnings({"unchecked"})
    public List<UserAction> findAllActions() {
        return (List<UserAction>)entityManager
                .createQuery("from UserAction ua")
                .getResultList();
    }

    // select u.id, u.name, max(ua.updated) from user_activity ua, users u where u.id = ua.user_id and ua.city_id=16388 group by u.id;
    @SuppressWarnings({"unchecked"})
    /**
    * This query has been optimized for performance. Note that because it only draws upon information in the
    * table, that it is capable of using only its index/where clause, which makes it as fast as it can get.
    */
    public List<LastUpdateByUser> findLastUpdateTimes(int cityId) {
        return (List<LastUpdateByUser>)entityManager
                .createQuery("select new nl.d2n.dao.result.LastUpdateByUser(ua.user.id, max(ua.updated)) from UserAction ua where ua.city.id = :city_id group by ua.user.id")
                .setParameter("city_id", cityId)
                .getResultList();
    }

    // select u.id, u.name, sum(case action when 'READ_MAP' then 1 else 0 end) as read_count, sum(case action when 'READ_MAP' then 0 else 1 end) as write_count from user_activity ua, users u where u.id = ua.user_id and ua.city_id=16388 and ua.user_id in (56, 283, 296, 525, 576, 923, 1246, 1254, 1270, 1279, 1512, 3539, 3572) and ua.updated >= '2011-11-01' and ua.updated < '2011-11-02' group by u.id;
    @SuppressWarnings({"unchecked"})
    public List<UserUpdateCount> findRecentChanges(int cityId, Date startDate, Date endDate) {
        List results = entityManager
                .createNativeQuery("select ua.user_id, sum(case action when 'READ_MAP' then 1 else 0 end) as read_count, sum(case action when 'READ_MAP' then 0 else 1 end) as write_count from user_activity ua where ua.city_id=:city_id and ua.updated >= :start_date and ua.updated < :end_date group by ua.user_id")
                .setParameter("city_id", cityId)
                .setParameter("start_date", startDate)
                .setParameter("end_date", endDate)
                .getResultList();
        List<UserUpdateCount> counts = new ArrayList<UserUpdateCount>();
        for (Object result : results) {
            Object[] resultArray = (Object[]) result;
            counts.add(new UserUpdateCount(getIntValue(resultArray[0]), getIntValue(resultArray[1]), getIntValue(resultArray[2])));
        }
        return counts;
    }

    protected Integer getIntValue(Object object) {
        return object instanceof BigDecimal ? ((BigDecimal)object).intValue() : ((BigInteger)object).intValue();
    }
}
