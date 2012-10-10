package nl.d2n.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class UserStore<C> {

    private Map<Integer, C> storedById = new TreeMap<Integer, C>();

    private Map<Integer, Integer> twinoidIdToId = new TreeMap<Integer, Integer>();
    
    public UserStore(List<User> users) {
        for (User user : users) {
            if (user.getGameId() != null) {
                twinoidIdToId.put(user.getGameId(), user.getId());
            }
        }
    }

    public C getById(Integer id) {
        return storedById.get(id);
    }

    public C getByTwinoidId(Integer twinoidId) {
        Integer id = twinoidIdToId.get(twinoidId);
        return id == null ? null : storedById.get(id);
    }

    public void putById(Integer id, C objectToStore) {
        storedById.put(id, objectToStore);
    }
}
