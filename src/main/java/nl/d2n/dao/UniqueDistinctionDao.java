package nl.d2n.dao;

import nl.d2n.model.UniqueDistinction;
import nl.d2n.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UniqueDistinctionDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public UniqueDistinction find(final String name) {
        List results = entityManager
                .createQuery("from UniqueDistinction d where d.name = :name")
                .setParameter("name", name)
                .getResultList();
        return (results.size() == 0 ? null : (UniqueDistinction)results.get(0));
    }

    @Transactional
    public void save(final UniqueDistinction uniqueDistinction) {
        entityManager.merge(uniqueDistinction);
    }

    @SuppressWarnings({"unchecked"})
    public List<UniqueDistinction> findUniqueDistinctions() {
        return (List<UniqueDistinction>)entityManager
                .createQuery("from UniqueDistinction")
                .getResultList();
    }

    @Transactional
    public void deleteUniqueDistinctions() {
        entityManager
                .createQuery("delete from UniqueDistinction")
                .executeUpdate();
    }
}
