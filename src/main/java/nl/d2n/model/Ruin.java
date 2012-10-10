package nl.d2n.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Ruin {

    @Expose
    private Map<Integer, Map<Integer, Room>> ruin = new TreeMap<Integer, Map<Integer, Room>>();

    @Expose
    private Integer x;

    @Expose
    private Integer y;

    @Expose
    private Integer cityId;

    private List<Room> roomsToSave = new ArrayList<Room>();

    public Ruin() {}

    public Ruin(List<Room> rooms, Integer cityId, Integer x, Integer y) {
        for (Room room : rooms) {
            add(room);
        }
        this.cityId = cityId;
        this.x = x;
        this.y = y;
    }

    public Room getRoom(Integer x, Integer y) {
        return ruin.get(x) == null ? null : ruin.get(x).get(y);
    }

    public boolean hasEntrance() {
        return getRoom(0, 0) != null;
    }

    public Map<Integer, Map<Integer, Room>> getRuin() {
        return this.ruin;
    }

    public List<Room> getRoomsToSave() {
        return roomsToSave;
    }
    
    public void clearRoomsToSave() {
        roomsToSave.clear();
    }

    protected void add(Room room) {
        Map<Integer, Room> ruinRow = ruin.get(room.getX());
        if (ruinRow == null) {
            ruinRow = new TreeMap<Integer, Room>();
            ruin.put(room.getX(), ruinRow);
        }
        ruinRow.put(room.getY(), room);
    }
    protected void addAndSave(Room room) {
        add(room);
        roomsToSave.add(room);
    }

    public void addEntrance(Zone zone) {
        addAndSave(new Room(zone, 0, 0));
    }

    public boolean roomMayBeDeleted(Integer x, Integer y) {
        return !((x == 0 && y == 0) || hasDependentNeighbours(new RuinIgnoreRoom(x, y)));
    }

    public boolean corridorMayBeDeleted(Integer x, Integer y, RuinDirection direction) {
        Room currentRoom = getRoom(x, y);
        Room otherRoom = getNeighbouringRoom(x, y, direction);
        if (otherRoom == null) {
            return true;
        }
        RuinIgnoreDirection ignoreDirection = new RuinIgnoreDirection(x, y, otherRoom.getX(), otherRoom.getY());
        return
            canTraceRouteToEntrance(currentRoom, ignoreDirection) &&
            canTraceRouteToEntrance(otherRoom, ignoreDirection);
    }

    protected boolean hasDependentNeighbours(RuinIgnoreEntity ignoreEntity) {
        boolean hasDependentNeighbours = false;
        for (RuinDirection direction : RuinDirection.values()) {
            Room room = getNeighbouringRoom(ignoreEntity.getX(), ignoreEntity.getY(), direction);
            hasDependentNeighbours |= !canTraceRouteToEntrance(room, ignoreEntity);
        }
        return hasDependentNeighbours;
    }

    protected boolean canTraceRouteToEntrance(Room room, RuinIgnoreEntity ignoreEntity) {
        if (room == null || room.isEntrance()) {
            return true;
        }

        // Reset all rooms
        resetAllRooms();

        List<Room> roomsToVisit = new ArrayList<Room>();
        addRoomsToVisit(roomsToVisit, room, ignoreEntity);

        while (roomsToVisit.size() != 0) {
            List<Room> newRoomsToVisit = new ArrayList<Room>();
            for (Room roomToVisit : roomsToVisit) {
                if (roomToVisit.isEntrance()) {
                    return true;
                }
                addRoomsToVisit(newRoomsToVisit, roomToVisit, ignoreEntity);
                roomToVisit.setAlreadyVisited(true);
            }
            roomsToVisit = newRoomsToVisit;
        }
        return false;
    }

    public Room getNeighbouringRoom(Integer x, Integer y, RuinDirection direction) {
        return getRoom(direction.getX(x), direction.getY(y));
    }

    protected void addRoomsToVisit(List<Room> roomsToVisit, Room currentRoom, RuinIgnoreEntity ignoreEntity) {
        for (RuinDirection direction : RuinDirection.values()) {
            // Ignore this direction if there is no corridor for it
            if (!currentRoom.getCorridors().get(direction)) {
                continue;
            }
            Room roomToVisit = getNeighbouringRoom(currentRoom.getX(), currentRoom.getY(), direction);
            if (roomToVisit == null || ignoreEntity.ignore(currentRoom, roomToVisit) || roomToVisit.isAlreadyVisited()) {
                continue;
            }
            roomsToVisit.add(roomToVisit);
        }
    }

    protected void resetAllRooms() {
        for (Integer x : ruin.keySet()) {
            for (Integer y : ruin.get(x).keySet()) {
                Room roomToReset = getRoom(x, y);
                if (roomToReset == null) {
                    continue;
                }
                roomToReset.setAlreadyVisited(false);
            }
        }
    }

    public void addCorridor(Room room, RuinDirection direction) throws ApplicationException {
        if (room.getCorridors().get(direction)) {
            throw new ApplicationException(D2NErrorCode.CORRIDOR_ALREADY_EXISTS);
        }
        setCorridor(room, direction, true);
    }

    public void deleteCorridor(Room room, RuinDirection direction) throws ApplicationException {
        if (!corridorMayBeDeleted(room.getX(), room.getY(), direction)) {
            throw new ApplicationException(D2NErrorCode.CORRIDOR_MAY_NOT_BE_REMOVED);
        }
        setCorridor(room, direction, false);
    }
    
    protected void setCorridor(Room room, RuinDirection direction, boolean addCorridor) {
        room.setCorridor(direction, addCorridor);
        roomsToSave.add(room);
        Room otherRoom = getNeighbouringRoom(room.getX(), room.getY(), direction);
        if (otherRoom != null) {
            otherRoom.setCorridor(direction.getInverse(), addCorridor);
            roomsToSave.add(otherRoom);
        }
    }
    
    public void addRoom(Room room) throws ApplicationException {
        for (RuinDirection direction : RuinDirection.values()) {
            Room checkRoom = getNeighbouringRoom(room.getX(), room.getY(), direction);
            // Connect the two rooms
            if (checkRoom != null) {
                room.setCorridor(direction, true);
                checkRoom.setCorridor(direction.getInverse(), true);
                roomsToSave.add(checkRoom);
            }
        }
        // Only allow saving of the room when there is at least one adjoining room
        if (roomsToSave.size() == 0) {
            throw new ApplicationException(D2NErrorCode.ROOM_HAS_NO_ADJOINING_ROOMS);
        }
        addAndSave(room);
    }
}
