package nl.d2n.model;

import nl.d2n.model.builder.CityBuilder;
import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class CityTest {

    @Test
    public void constructorTest() {
        City city = new CityBuilder()
                .setId(13449)
                .setName("Cliffe of Tricks")
                .setWidth(14)
                .setHeight(13)
                .setLeft(-7)
                .setRight(6)
                .setTop(4)
                .setBottom(8)
                .setHardcore(true)
                .toCity();
        assertEquals((Integer)13449, city.getId());
        assertEquals("Cliffe of Tricks", city.getName());
        assertEquals((Integer)14, city.getWidth());
        assertEquals((Integer)13, city.getHeight());
        assertTrue(-7 == city.getLeft());
        assertEquals((Integer)6, city.getRight());
        assertEquals((Integer)4, city.getTop());
        assertEquals((Integer)8, city.getBottom());
        assertTrue(city.isHard());
    }

    @Test
    public void noBuildings() throws JAXBException {
        String xml = "<city city=\"Cliffe of tricks\" door=\"1\" water=\"85\" chaos=\"0\" devast=\"0\" x=\"16\" y=\"11\"></city>";
        City city = (City) XmlToObjectConverter.convertXmlToObject(xml, City.class);
        assertEquals(0, city.getBuildings().size());
        assertEquals(false, city.isUpgradedMapAvailable());
    }

    @Test
    public void upgradedMapAvailable() throws JAXBException {
        String xml =
            "<city city=\"Cliffe of tricks\" door=\"1\" water=\"84\" chaos=\"0\" devast=\"0\" x=\"16\" y=\"11\">" +
                "<building name=\"Wall Upgrade v1\" temporary=\"0\" id=\"1010\" img=\"small_wallimprove\">Significantly increases the town's defences.</building>" +
                "<building name=\"Upgraded Map\" temporary=\"0\" parent=\"1050\" id=\"1042\" img=\"item_electro\">This simple electronic gadget increases the level of detail visible on the map of the World Beyond: the exact number of zombies in each zone can be clearly seen. Invaluable if you don't want to wander mindlessly into feeding time at the zombie zoo...</building>" +
                "<building name=\"Watchtower\" temporary=\"0\" id=\"1050\" img=\"item_tagger\">This tower, situated near the gates, gives an estimation of the magnitude of the forthcoming attack, allowing the citizens to be better prepared. It also unlocks all construction projects linked to emergency situations.</building>" +
                "<defense base=\"10\" items=\"9\" citizen_guardians=\"15\" citizen_homes=\"17\" upgrades=\"0\" buildings=\"15\" total=\"66\" itemsMul=\"1\"/>" +
            "</city>";
        City city = (City) XmlToObjectConverter.convertXmlToObject(xml, City.class);
        assertEquals(3, city.getBuildings().size());
        assertEquals(true, city.isUpgradedMapAvailable());
    }

    @Test
    public void unmarshallXmlSnippet() throws JAXBException {
        String xml = "<city city=\"Cliffe of tricks\" door=\"1\" water=\"85\" chaos=\"0\" devast=\"0\" x=\"16\" y=\"11\"></city>";
        City city = (City) XmlToObjectConverter.convertXmlToObject(xml, City.class);
        assertEquals("Cliffe of tricks", city.getName());
        assertEquals(true, city.isDoorOpen());
        assertEquals(85, city.getWater());
        assertEquals(false, city.isChaosMode());
        assertEquals(false, city.isDevastated());
        assertEquals(16, city.getX());
        assertEquals(11, city.getY());
    }

    @Test
    public void defenseInCity() throws JAXBException {
        String xml =
            "<city city=\"Cliffe of tricks\" door=\"1\" water=\"85\" chaos=\"0\" devast=\"0\" x=\"16\" y=\"11\">"+
                "<defense base=\"10\" items=\"5\" citizen_guardians=\"15\" citizen_homes=\"17\" upgrades=\"0\" buildings=\"5\" total=\"52\" itemsMul=\"1\"/>"+
            "</city>";
        City city = (City) XmlToObjectConverter.convertXmlToObject(xml, City.class);
        assertNotNull("Excepting defense in the city", city.getDefense());
        assertEquals(15, city.getDefense().getCitizenGuardians());
    }

    @Test
    public void buildingsInCity() throws JAXBException {
        String xml =
            "<city city=\"Cliffe of tricks\" door=\"1\" water=\"84\" chaos=\"0\" devast=\"0\" x=\"16\" y=\"11\">" +
                "<building name=\"Wall Upgrade v1\" temporary=\"0\" id=\"1010\" img=\"small_wallimprove\">Significantly increases the town's defences.</building>" +
                "<building name=\"Workshop\" temporary=\"0\" id=\"1033\" img=\"small_refine\">The advancement of a town depends on the construction of a shabby workshop full of nondescript clutter. It allows resources to be converted and is a pre-requisite for the construction of other more advanced buildings.</building>" +
                "<building name=\"Watchtower\" temporary=\"0\" id=\"1050\" img=\"item_tagger\">This tower, situated near the gates, gives an estimation of the magnitude of the forthcoming attack, allowing the citizens to be better prepared. It also unlocks all construction projects linked to emergency situations.</building>" +
                "<defense base=\"10\" items=\"9\" citizen_guardians=\"15\" citizen_homes=\"17\" upgrades=\"0\" buildings=\"15\" total=\"66\" itemsMul=\"1\"/>" +
            "</city>";
        City city = (City) XmlToObjectConverter.convertXmlToObject(xml, City.class);
        assertNotNull("Expected buildings in the city", city.getBuildings());
        assertEquals(3, city.getBuildings().size());
    }

    @Test
    public void newsInCity() throws JAXBException {
        String xml =
            "<city>" +
                "<news z=\"41\" def=\"82\">" +
                    "<content>" +
                        "<p>The citizens clearly decided to leave <strong>ninjathr33</strong> to their own devices in the World Beyond, but we haven't heard from them in some time. You guys are all heart...</p><p>Furthermore, one of the 40 attacking zombies from last night was found <strong>in the well</strong> I'd strongly advise you to skip your water ration today, as it'd be a great way to get an infection...</p>" +
                    "</content>" +
                "</news>"+
            "</city>";
        City city = (City) XmlToObjectConverter.convertXmlToObject(xml, City.class);
        assertNotNull("Expected news in the city", city.getNews());
    }

}
