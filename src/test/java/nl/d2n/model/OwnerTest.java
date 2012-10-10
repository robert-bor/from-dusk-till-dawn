package nl.d2n.model;

import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertNotNull;

public class OwnerTest {

    @Test
    public void unmarshallXmlSnippet() throws JAXBException {
        String xml =
            "<owner>" +
                "<citizen dead=\"0\" hero=\"0\" name=\"Fred\" x=\"8\" y=\"8\" id=\"666\" ban=\"0\" job=\"basic\" out=\"1\" baseDef=\"1\">Scavenger (1)</citizen>" +
                "<myZone dried=\"0\" h=\"8\" z=\"2\">" +
                    "<item name=\"Can Opener\" count=\"1\" id=\"23\" cat=\"Weapon\" img=\"can_opener\" broken=\"1\"/>" +
                    "<item name=\"Crushed Battery\" count=\"2\" id=\"214\" cat=\"Misc\" img=\"pile_broken\" broken=\"0\"/>" +
                    "<item name=\"Sheet Metal (parts)\" count=\"1\" id=\"96\" cat=\"Misc\" img=\"plate_raw\" broken=\"0\"/>" +
                    "<item name=\"Staff\" count=\"2\" id=\"15\" cat=\"Weapon\" img=\"staff\" broken=\"1\"/>" +
                "</myZone>" +
            "</owner>";
        Owner owner = (Owner) XmlToObjectConverter.convertXmlToObject(xml, Owner.class);
        assertNotNull(owner.getCitizen());
        assertNotNull(owner.getZone());
    }
}
