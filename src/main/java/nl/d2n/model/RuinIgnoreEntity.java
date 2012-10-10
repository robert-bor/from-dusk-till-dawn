package nl.d2n.model;

public abstract class RuinIgnoreEntity {

    private Integer ignoreX;
    private Integer ignoreY;

    public RuinIgnoreEntity(Integer x, Integer y) {
        setX(x);
        setY(y);
    }
    public Integer getX() { return this.ignoreX; }
    public Integer getY() { return this.ignoreY; }
    public void setX(Integer x) { this.ignoreX = x; }
    public void setY(Integer y) { this.ignoreY = y; }
    public abstract boolean ignore(Room currentRoom, Room otherRoom);
}
