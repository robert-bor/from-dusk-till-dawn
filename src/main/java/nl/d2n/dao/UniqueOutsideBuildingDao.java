package nl.d2n.dao;

import nl.d2n.model.UniqueOutsideBuilding;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UniqueOutsideBuildingDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void save(final UniqueOutsideBuilding uniqueOutsideBuilding) {
        entityManager.merge(uniqueOutsideBuilding);
    }

    @SuppressWarnings({"unchecked"})
    public List<UniqueOutsideBuilding> findUniqueOutsideBuildings() {
        return (List<UniqueOutsideBuilding>)entityManager
                .createQuery("from UniqueOutsideBuilding order by id")
                .getResultList();
    }

    @Transactional
    public void deleteUniqueOutsideBuildings() {
        entityManager
                .createQuery("delete from UniqueOutsideBuilding")
                .executeUpdate();
    }
}
