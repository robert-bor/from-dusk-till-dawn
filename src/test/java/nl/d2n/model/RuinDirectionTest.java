package nl.d2n.model;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static nl.d2n.model.RuinDirection.*;

public class RuinDirectionTest {

    @Test
    public void inverse() {
        assertEquals(WEST,  EAST.getInverse());
        assertEquals(EAST,  WEST.getInverse());
        assertEquals(NORTH, SOUTH.getInverse());
        assertEquals(SOUTH, NORTH.getInverse());
    }

    @Test
    public void coordinates() {
        assertEquals(4, WEST.getX(5));
        assertEquals(-2, WEST.getY(-2));
        assertEquals(-1, EAST.getX(-2));
        assertEquals(3, EAST.getY(3));
        assertEquals(7, NORTH.getX(7));
        assertEquals(3, NORTH.getY(2));
        assertEquals(5, SOUTH.getX(5));
        assertEquals(-5, SOUTH.getY(-4));
    }

}
