package nl.d2n.model;

import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class InsideBuildingTest {

    @Test
    public void unmarshallXmlSnippet() throws JAXBException {
        String xml = "<building name=\"Wall Upgrade v1\" temporary=\"0\" id=\"1010\" parent=\"777\" img=\"small_wallimprove\">Significantly increases the town's defences.</building>";
        InsideBuilding building = (InsideBuilding) XmlToObjectConverter.convertXmlToObject(xml, InsideBuilding.class);

        assertEquals("Wall Upgrade v1", building.getName());
        assertEquals(false, building.isTemporary());
        assertEquals(1010, building.getBuildingId());
        assertEquals("small_wallimprove", building.getImage());
        assertEquals(777, building.getParent());
        assertTrue("Flavor text must start with given text", building.getFlavor().startsWith("Significantly increases"));
    }

    @Test
    public void compareBuildings() {
        InsideBuilding building1 = new InsideBuilding();
        building1.setBuildingId(42);
        assertEquals(42, building1.hashCode());
        InsideBuilding building2 = new InsideBuilding();
        building2.setBuildingId(42);
        assertEquals(0, building1.compareTo(building2));
    }

    @Test
    public void compareSomethingElse() {
        InsideBuilding building1 = new InsideBuilding();
        building1.setBuildingId(42);
        assertEquals(-1, building1.compareTo("some text"));
    }
}
