package nl.d2n.dao;

import nl.d2n.model.UniqueInsideBuilding;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UniqueInsideBuildingDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void save(final UniqueInsideBuilding uniqueInsideBuilding) {
        entityManager.merge(uniqueInsideBuilding);
    }

    @SuppressWarnings({"unchecked"})
    public List<UniqueInsideBuilding> findUniqueInsideBuildings() {
        return (List<UniqueInsideBuilding>)entityManager
                .createQuery("from UniqueInsideBuilding order by id")
                .getResultList();
    }

    @Transactional
    public void deleteUniqueInsideBuildings() {
        entityManager
                .createQuery("delete from UniqueInsideBuilding")
                .executeUpdate();
    }
}
