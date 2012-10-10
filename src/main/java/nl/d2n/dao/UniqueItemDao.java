package nl.d2n.dao;

import nl.d2n.model.UniqueItem;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UniqueItemDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void save(final UniqueItem uniqueItem) {
        entityManager.merge(uniqueItem);
    }

    @SuppressWarnings({"unchecked"})
    public List<UniqueItem> findUniqueItems() {
        return (List<UniqueItem>)entityManager
                .createQuery("from UniqueItem")
                .getResultList();
    }

    @Transactional
    public void deleteUniqueItems() {
        entityManager
                .createQuery("delete from UniqueItem")
                .executeUpdate();
    }
}
