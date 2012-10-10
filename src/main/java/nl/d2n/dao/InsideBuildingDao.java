package nl.d2n.dao;

import nl.d2n.model.InsideBuilding;
import nl.d2n.model.Zone;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class InsideBuildingDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void save(final InsideBuilding insideBuilding) {
        entityManager.merge(insideBuilding);
    }

    @Transactional
    public void delete(final int cityId, final int buildingId) {
        entityManager
                .createQuery("delete from InsideBuilding where city.id = :city_id and buildingId = :building_id")
                .setParameter("city_id", cityId)
                .setParameter("building_id", buildingId)
                .executeUpdate();
    }

    @SuppressWarnings({"unchecked"})
    public List<InsideBuilding> findInsideBuildings(final int cityId) {
        return (List<InsideBuilding>)entityManager
                .createQuery("from InsideBuilding ib where ib.city.id = :city_id")
                .setParameter("city_id", cityId)
                .getResultList();
    }
}
