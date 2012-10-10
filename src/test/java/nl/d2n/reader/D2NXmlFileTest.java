package nl.d2n.reader;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class D2NXmlFileTest {

    public static final String DIRECTORY = "target/";
    public static final Integer CITY_ID= 13449;

    @SuppressWarnings({"ResultOfMethodCallIgnored"})
    @After
    public void removeFile() {
        new File(D2NXmlFile.getFilePath(DIRECTORY, CITY_ID)).delete();
    }

    @Test
    public void roundRobin() throws Exception {
        D2NXmlFile xmlFile = new D2NXmlFile();
        xmlFile.setDirectory(DIRECTORY);
        String xmlText =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<hordes xmlns:dc=\"http://purl.org/dc/elements/1.1\" xmlns:content=\"http://purl.org/rss/1.0/modules/content/\">\n" +
            "    <headers link=\"http://www.die2nite.com/xml\" iconurl=\"http://data.die2nite.com/gfx/icons/\" secure=\"1\"\n" +
            "             author=\"Motion Twin &lt;http://www.motion-twin.com&gt;\" language=\"en\" version=\"2.171\"\n" +
            "             avatarurl=\"http://imgup.motion-twin.com/\" generator=\"haxe\">\n" +
            "        <owner>\n" +
            "            <citizen dead=\"0\" hero=\"0\" name=\"Heltharion\" avatar=\"hordes/2/6/42aa01ad_41449.jpg\" x=\"12\" y=\"7\" id=\"41449\"\n" +
            "                     ban=\"0\" job=\"basic\" out=\"1\" baseDef=\"1\"><![CDATA[]]></citizen>\n" +
            "            <myZone dried=\"0\" h=\"2\" z=\"2\"/>\n" +
            "        </owner>\n" +
            "        <game days=\"6\" quarantine=\"0\" datetime=\"2011-07-27 12:23:36\" id=\"13449\"/>\n" +
            "    </headers>\n"+
            "</hordes>";
        xmlFile.writeFile(xmlText, CITY_ID);
        String xmlIn = xmlFile.readFile(CITY_ID);
        assertEquals(xmlText, xmlIn);
    }

    @Test
    public void fileNotFound() {
        D2NXmlFile xmlFile = new D2NXmlFile();
        xmlFile.setDirectory(DIRECTORY);
        try {
            xmlFile.readFile(CITY_ID+1);
        } catch (FileNotFoundException err) {
            // Great, it worked
        } catch (Exception err) {
            fail("Should have thrown FileNotFoundException");
        }
    }
}
