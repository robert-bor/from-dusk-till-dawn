package nl.d2n.model;

public class RuinIgnoreDirection extends RuinIgnoreEntity {

    private Integer otherX;
    private Integer otherY;
    
    public RuinIgnoreDirection(Integer thisX, Integer thisY, Integer thatX, Integer thatY) {
        super(thisX, thisY);
        this.otherX = thatX;
        this.otherY = thatY;
    }

    public boolean ignore(Room currentRoom, Room otherRoom) {
        return 
            ((currentRoom.getX() == getX() && currentRoom.getY() == getY() && otherRoom.getX() == otherX && otherRoom.getY() == otherY) ||
             (currentRoom.getX() == otherX && currentRoom.getY() == otherY && otherRoom.getX() == getX() && otherRoom.getY() == getY()));
    }
}
