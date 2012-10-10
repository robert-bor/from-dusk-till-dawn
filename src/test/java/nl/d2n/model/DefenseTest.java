package nl.d2n.model;

import nl.d2n.model.Defense;
import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;

public class DefenseTest {
    @Test
    public void unmarshallXmlSnippet() throws JAXBException {
        String xml = "<defense base=\"10\" items=\"1\" citizen_guardians=\"5\" citizen_homes=\"14\" upgrades=\"2\" buildings=\"3\" total=\"30\" itemsMul=\"1.1\"/>";
        Defense defense = (Defense) XmlToObjectConverter.convertXmlToObject(xml, Defense.class);
        assertEquals(10 , defense.getBase());
        assertEquals(1 , defense.getItems());
        assertEquals(5 , defense.getCitizenGuardians());
        assertEquals(14 , defense.getCitizenHomes());
        assertEquals(2 , defense.getUpgrades());
        assertEquals(3 , defense.getBuildings());
        assertEquals(30 , defense.getTotal());
        assertEquals(1.1 , defense.getItemsMultiplier());
    }
}
