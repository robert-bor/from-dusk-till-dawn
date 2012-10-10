package nl.d2n.model;

import nl.d2n.util.XmlToObjectConverter;
import org.junit.Test;

import javax.xml.bind.JAXBException;

import static junit.framework.Assert.assertEquals;

public class ProfileTest {

    @Test
    public void distinguishedGentleman() throws JAXBException {
        String xml =
            "<data>"+
                "<rewards>"+
                    "<r name=\"Banishments\" rare=\"0\" n=\"1\" img=\"r_ban\"/>" +
                    "<r name=\"Drugs\" rare=\"0\" n=\"66\" img=\"r_drug\"/>" +
                    "<r name=\"Community Awards\" rare=\"1\" n=\"5\" img=\"r_bgum\">" +
                        "<title name=\"Community Spirit\"/>" +
                    "</r>" +
                    "<r name=\"Decoration\" rare=\"0\" n=\"105\" img=\"r_deco\">" +
                        "<title name=\"Coquette\"/>" +
                    "</r>" +
                    "<r name=\"Masochism\" rare=\"1\" n=\"3\" img=\"r_maso\"/>" +
                    "<r name=\"Daredevil Camper\" rare=\"0\" n=\"6\" img=\"r_camp\"/>"+
                "</rewards>"+
                "<maps>"+
                    "<m name=\"Quivering sands of 1000 deaths\" season=\"2\" score=\"276\" d=\"23\" id=\"12366\" v1=\"0\">"+
                        "<![CDATA[Highlanders for the Win!]]></m>"+
                    "<m name=\"Tomb of reptiles\" season=\"2\" score=\"210\" d=\"20\" id=\"11330\" v1=\"0\">"+
                        "<![CDATA[We ran a tight ship here. Not everyone understood, but it sure worked well and brought us far]]></m>"+
                    "<m name=\"Pitiless bay of the banished\" season=\"3\" score=\"153\" d=\"17\" id=\"14922\" v1=\"0\">"+
                        "<![CDATA[My first Zen camper experience :) Great MMJ town with great people!]]></m>"+
                "</maps>"+
            "</data>";
        Profile profile = (Profile) XmlToObjectConverter.convertXmlToObject(xml, Profile.class);
        assertEquals(6, profile.getDistinctions().size());
        assertEquals(3, profile.getPlayedMaps().size());
        assertEquals((Integer)639, profile.getSoulPoints());
    }
}
