package nl.d2n.model;

import nl.d2n.model.UpdateAction;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class UpdateActionTest {

    @Test
    public void convertFromString() {
        assertEquals(UpdateAction.ADD_BLUEPRINT, UpdateAction.valueOf(UpdateAction.ADD_BLUEPRINT.toString().toLowerCase().toUpperCase()));
    }
}
