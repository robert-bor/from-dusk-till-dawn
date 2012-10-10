package nl.d2n.dao;

import nl.d2n.model.Item;
import nl.d2n.model.Zone;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ItemDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void saveItem(final Item item) {
        entityManager.merge(item);
    }

    @SuppressWarnings({"unchecked"})
    public List<Item> findAllItems() {
        return (List<Item>)entityManager
                .createQuery("from Item i")
                .getResultList();
    }

}
