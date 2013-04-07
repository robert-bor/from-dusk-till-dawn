package nl.d2n.dao;

import nl.d2n.model.UniqueTitle;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UniqueTitleDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void save(final UniqueTitle uniqueTitle) {
        entityManager.merge(uniqueTitle);
    }

    @SuppressWarnings({"unchecked"})
    public List<UniqueTitle> findUniqueTitles() {
        return (List<UniqueTitle>)entityManager
                .createQuery("from UniqueTitle")
                .getResultList();
    }

    @Transactional
    public void deleteUniqueTitles() {
        entityManager
                .createQuery("delete from UniqueTitle")
                .executeUpdate();
    }
}
