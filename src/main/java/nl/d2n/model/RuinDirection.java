package nl.d2n.model;

public enum RuinDirection {
    WEST(-1,0),
    NORTH(0,1),
    EAST(1,0),
    SOUTH(0,-1);

    final private int xMod;
    final private int yMod;

    private RuinDirection(int xMod, int yMod) {
        this.xMod = xMod;
        this.yMod = yMod;
    }
    public int getX(int x) {
        return x + xMod;
    }
    public int getY(int y) {
        return y + yMod;
    }
    public RuinDirection getInverse() {
        return RuinDirection.values()[(ordinal() + 2) % 4];
    }
}
