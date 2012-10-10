package nl.d2n.model;

import nl.d2n.reader.D2NXmlReader;
import nl.d2n.reader.XmlReader;
import nl.d2n.util.FileToStringConverter;
import org.junit.Test;

import java.io.*;

import static junit.framework.Assert.*;

public class InfoTest {

    @Test
    public void completeXml() throws Exception {
        String xml = FileToStringConverter.getContent(new File("docs/sample-xml/automated-test-input/sample-s3.xml"));
        Info info = new D2NXmlReader().convertXmlDocument((new XmlReader()).readDocumentFromString(xml)).getInfo();
        assertNotNull(info.getCity());
        assertNotNull(info.getGameHeader());
        assertNotNull(info.getMap());
        assertNotNull(info.getCadavers());
        assertEquals(4, info.getCadavers().size());
        assertNotNull(info.getCitizens());
        assertEquals(36, info.getCitizens().size());
        assertNotNull(info.getEstimations());
        assertEquals(2, info.getEstimations().size());
        assertNotNull(info.getItems());
        assertEquals(56, info.getItems().size());
        assertNotNull(info.getUpgrades());
        assertEquals(2, info.getUpgrades().size());
    }

    @Test
    public void nonSecureXml() {
        Info info = setUpInfo(false, true, 1, 1, 1, 1);
        assertFalse(info.isCitizenInWorldBeyond());
    }

    @Test
    public void citizenNotOutside() {
        Info info = setUpInfo(true, false, 1, 1, 1, 1);
        assertFalse(info.isCitizenInWorldBeyond());
    }

    @Test
    public void citizenInTownZone() {
        Info info = setUpInfo(true, true, 1, 1, 1, 1);
        assertFalse(info.isCitizenInWorldBeyond());
    }

    @Test
    public void citizenOutside() {
        Info info = setUpInfo(true, true, 1, 1, 0, 0);
        assertTrue(info.isCitizenInWorldBeyond());
    }

    protected Info setUpInfo(boolean secure, boolean outside, int xCitizen, int yCitizen, int xCity, int yCity) {
        Info info = new Info();
        Citizen citizen = new Citizen();
        citizen.setOutside(outside);
        citizen.setMatrixX(xCitizen);
        citizen.setMatrixY(yCitizen);
        Owner owner = new Owner();
        owner.setCitizen(citizen);
        GameHeader gameHeader = new GameHeader();
        gameHeader.setSecure(secure);
        gameHeader.setOwner(owner);
        info.setGameHeader(gameHeader);
        City city = new City();
        city.setX(xCity);
        city.setY(yCity);
        info.setCity(city);
        return info;
    }
}
