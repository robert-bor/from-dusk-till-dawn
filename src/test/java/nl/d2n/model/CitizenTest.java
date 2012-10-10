package nl.d2n.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;
import javax.xml.bind.JAXBException;

public class CitizenTest {

    @Test
    public void unmarshallXmlSnippet() throws JAXBException {
        String xml = "<citizen dead=\"0\" hero=\"1\" name=\"Aivaras\" avatar=\"hordes/7/1/e9e157b8_52868.jpg\" x=\"19\" y=\"9\" id=\"52868\" ban=\"0\" job=\"collec\" out=\"1\" baseDef=\"11\">Tok tok, it's me</citizen>";
        Citizen citizen = (Citizen)XmlToObjectConverter.convertXmlToObject(xml, Citizen.class);
        assertEquals("Aivaras", citizen.getName());
        assertEquals(11, citizen.getBaseDef());
        assertEquals(false, citizen.isDead());
        assertEquals(true, citizen.isHero());
        assertEquals(true, citizen.isOutside());
        assertEquals(false, citizen.isShunned());
        assertEquals((Integer)19, citizen.getMatrixX());
        assertEquals((Integer)9, citizen.getMatrixY());
        assertEquals("hordes/7/1/e9e157b8_52868.jpg", citizen.getAvatar());
        assertEquals((Integer)52868, citizen.getId());
        assertEquals(Job.SCAVENGER, citizen.getJob());
        assertEquals("Tok tok, it's me", citizen.getDescription());
    }

    @Test
    public void citizenInChaosTown() throws JAXBException {
        String xml = "<citizen dead=\"0\" hero=\"0\" name=\"Bearclaw\" id=\"62104\" ban=\"0\" job=\"collec\" out=\"0\" baseDef=\"6\"></citizen>";
        Citizen citizen = (Citizen)XmlToObjectConverter.convertXmlToObject(xml, Citizen.class);
        assertNull(citizen.getMatrixX());
        assertNull(citizen.getMatrixY());
        assertEquals(Job.SCAVENGER.getImage(), citizen.getImage());
    }
    @Test
    public void getImage() throws JAXBException {
        String xml = "<citizen dead=\"0\" hero=\"1\" name=\"Aivaras\" avatar=\"hordes/7/1/e9e157b8_52868.jpg\" x=\"19\" y=\"9\" id=\"52868\" ban=\"0\" job=\"collec\" out=\"1\" baseDef=\"11\">Tok tok, it's me</citizen>";
        Citizen citizen = (Citizen)XmlToObjectConverter.convertXmlToObject(xml, Citizen.class);
        assertEquals(Job.SCAVENGER.getImage(), citizen.getImage());
    }

    @Test
    public void citizenWithoutJob() throws JAXBException {
        String xml = "<citizen dead=\"0\" hero=\"0\" name=\"Barbarossa\" avatar=\"hordes/7/b/00ff6625_130201.jpg\" x=\"18\" y=\"12\" id=\"130201\" ban=\"0\" job=\"\" out=\"0\" baseDef=\"0\"></citizen>";
        Citizen citizen = (Citizen)XmlToObjectConverter.convertXmlToObject(xml, Citizen.class);
        assertEquals(Job.UNKNOWN, citizen.getJob());
    }
}
