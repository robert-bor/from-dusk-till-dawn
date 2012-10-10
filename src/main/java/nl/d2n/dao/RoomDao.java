package nl.d2n.dao;

import nl.d2n.model.Room;
import nl.d2n.model.Ruin;
import nl.d2n.model.Zone;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class RoomDao {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void saveRoom(final Room room) {
        entityManager.merge(room);
    }

    @Transactional
    public void saveRooms(final Ruin ruin) {
        for (Room room : ruin.getRoomsToSave()) {
            saveRoom(room);
        }
        ruin.clearRoomsToSave();
    }

    @Transactional
    public void deleteRoom(Zone zone, int x, int y) {
        entityManager
                .createQuery("delete from Room where zone.id = :zone_id and x = :x and y = :y")
                .setParameter("zone_id", zone.getId())
                .setParameter("x", x)
                .setParameter("y", y)
                .executeUpdate();
    }

    public Room findRoom(Zone zone, int x, int y) {
        List results = entityManager
                .createQuery("from Room where zone.id = :zone_id and x = :x and y = :y")
                .setParameter("zone_id", zone.getId())
                .setParameter("x", x)
                .setParameter("y", y)
                .getResultList();
        return (results.size() == 0 ? null : (Room)results.get(0));
    }

    @SuppressWarnings({"unchecked"})
    public Ruin getRuin(Zone zone) {
        List<Room> rooms = (List<Room>)entityManager
                .createQuery("from Room where zone.id = :zone_id")
                .setParameter("zone_id", zone.getId())
                .getResultList();
        return new Ruin(rooms, zone.getCity().getId(), zone.getX(), zone.getY());
    }
}
