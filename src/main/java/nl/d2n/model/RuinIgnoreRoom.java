package nl.d2n.model;

public class RuinIgnoreRoom extends RuinIgnoreEntity {

    public RuinIgnoreRoom(Integer x, Integer y) {
        super(x, y);
    }

    public boolean ignore(Room currentRoom, Room otherRoom) {
        return otherRoom.getX() == getX() && otherRoom.getY() == getY();
    }
}
