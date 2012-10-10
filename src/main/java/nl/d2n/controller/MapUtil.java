package nl.d2n.controller;

public class MapUtil {

    public static Integer getRealYPos(final int yPos, final int townYPos) {
        return townYPos - yPos;
    }
    public static Integer getRealXPos(final int xPos, final int townXPos) {
        return xPos - townXPos;
    }
}
