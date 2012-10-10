package nl.d2n.service.actions;

import nl.d2n.SpringContextTestCase;
import nl.d2n.dao.ZoneDao;
import nl.d2n.model.*;
import nl.d2n.model.builder.CityBuilder;
import nl.d2n.util.ClassCreator;
import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class ZoneActionAutomaticUpdateTest extends SpringContextTestCase {

    @Autowired
    private ApplicationContext applicationContext;
    
    @Autowired
    private ClassCreator classCreator;
    
    @Autowired
    private ZoneDao zoneDao;
    
    @Test
    public void updateMyZone() throws JAXBException, ApplicationException {
        String xml =
                "<owner>" +
                        "<citizen dead=\"0\" hero=\"0\" name=\"Fred\" x=\"8\" y=\"7\" id=\"666\" ban=\"0\" job=\"basic\" out=\"1\" baseDef=\"1\">Scavenger (1)</citizen>" +
                        "<myZone dried=\"1\" h=\"8\" z=\"2\">" +
                        "<item name=\"Can Opener\" count=\"1\" id=\"23\" cat=\"Weapon\" img=\"can_opener\" broken=\"1\"/>" +
                        "<item name=\"Crushed Battery\" count=\"2\" id=\"214\" cat=\"Misc\" img=\"pile_broken\" broken=\"0\"/>" +
                        "<item name=\"Sheet Metal (parts)\" count=\"1\" id=\"96\" cat=\"Misc\" img=\"plate_raw\" broken=\"0\"/>" +
                        "<item name=\"Staff\" count=\"2\" id=\"15\" cat=\"Weapon\" img=\"staff\" broken=\"1\"/>" +
                        "</myZone>" +
                        "</owner>";
        Owner owner = (Owner) XmlToObjectConverter.convertXmlToObject(xml, Owner.class);
        classCreator.createCity(1666, "City of Evil");
        Zone currentZone = applicationContext.getBean(ZoneActionAutomaticUpdate.class).findOrCreate(1666, owner.getCitizen().getMatrixX(), owner.getCitizen().getMatrixY());
        ZoneActionAutomaticUpdate action = applicationContext.getBean(ZoneActionAutomaticUpdate.class);
        action.storeBasicInformation(owner.getZone());
        currentZone = action.updateZone(1666, 8, 7);
        currentZone = zoneDao.findZone(currentZone.getCity().getId(), currentZone.getX(), currentZone.getY());
        assertEquals(8, currentZone.getX());
        assertEquals(7, currentZone.getY());
        assertEquals(2, currentZone.getZombies());
        assertEquals(true, currentZone.isZoneDepleted());
        assertEquals(4, currentZone.getItems().size());
        Item item = currentZone.getItems().get(0);
        assertEquals(1, item.getAmount());
        assertEquals(true, item.isBroken());
        assertEquals(null, item.getCategory());
        assertEquals(23, item.getD2nItemId());
        assertEquals(null, item.getImage());
        assertEquals(null, item.getName());
    }

    @Test
    public void coordinatesOutsideLegitimateRange() {
        City city = new CityBuilder()
                .setId(13449)
                .setName("Cliffe of Tricks")
                .setWidth(14)
                .setHeight(13)
                .setLeft(-7)
                .setRight(6)
                .setTop(4)
                .setBottom(-8)
                .setHardcore(true)
                .toCity();
        cityDao.saveCity(city);
        assertException(D2NErrorCode.ILLEGAL_COORDINATES, 13449, -9, 2);
        assertException(D2NErrorCode.ILLEGAL_COORDINATES, 13449, 8, 3);
        assertException(D2NErrorCode.ILLEGAL_COORDINATES, 13449, 4, 7);
        assertException(D2NErrorCode.ILLEGAL_COORDINATES, 13449, 4, -10);
    }

    protected void assertException(D2NErrorCode expectedError, Integer cityId, Integer x, Integer y) {
        try {
            applicationContext.getBean(ZoneActionAutomaticUpdate.class).findOrCreate(cityId, x, y);
            fail("Should have thrown an exception because the coordinates are illegal");
        } catch (ApplicationException err) {
            assertEquals(expectedError, err.getError());
        }
    }

    @Test
    public void findNonExistingCity() {
        try {
            applicationContext.getBean(ZoneActionAutomaticUpdate.class).findOrCreate(424242, 13, 26);
            fail("Should have thrown an exception");
        } catch (ApplicationException err) {
            assertEquals(D2NErrorCode.CITY_DOES_NOT_EXIST, err.getError());
        }
    }

}
