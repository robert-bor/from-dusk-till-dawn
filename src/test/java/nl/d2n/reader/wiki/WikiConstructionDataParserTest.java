package nl.d2n.reader.wiki;

import nl.d2n.SpringContextTestCase;
import nl.d2n.reader.D2NXmlFile;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

public class WikiConstructionDataParserTest extends SpringContextTestCase {

    @Autowired
    WikiConstructionDataParser parser;

    @Test
    public void noStartAnchor() {
        testForParserException(
                "<p><br />\n" +
                "[<span class=\"noprint plainlinks purgelink\"><a href=\"http://die2nitewiki.com/w/index.php?title=Data:Wall_Upgrade_v1&amp;action=purge\" class=\"external text\" rel=\"nofollow\"><span title=\"Purge this page\">Purge</span></a></span>]",
                WikiConstructionParserErrorType.HTML_BODY_DIGGING_ERROR);
    }

    @Test
    public void noUnsortedList() {
        testForParserException(
                "<p>This page contains the following <span class=\"plainlinks\"><a href=\"http://die2nitewiki.com/w/index.php?title=Data:Wall_Upgrade_v1&amp;action=edit\" class=\"external text\" rel=\"nofollow\">metadata</a></span>:\n" +
                "<p><br />\n" +
                "[<span class=\"noprint plainlinks purgelink\"><a href=\"http://die2nitewiki.com/w/index.php?title=Data:Wall_Upgrade_v1&amp;action=purge\" class=\"external text\" rel=\"nofollow\"><span title=\"Purge this page\">Purge</span></a></span>]",
                WikiConstructionParserErrorType.HTML_BODY_DIGGING_ERROR);
    }

    @Test
    public void noEndAnchor() {
        testForParserException(
                "<p>This page contains the following <span class=\"plainlinks\"><a href=\"http://die2nitewiki.com/w/index.php?title=Data:Wall_Upgrade_v1&amp;action=edit\" class=\"external text\" rel=\"nofollow\">metadata</a></span>:\n" +
                "<ul><li> ID (id) - <b><a href=\"/wiki/Buildings_by_number\" title=\"Buildings by number\">1010</a></b>\n" +
                "<p><br />\n",
                WikiConstructionParserErrorType.HTML_BODY_DIGGING_ERROR);
    }

    @Test
    public void noParagraphBeforeEndAnchor() {
        testForParserException(
                "<p>This page contains the following <span class=\"plainlinks\"><a href=\"http://die2nitewiki.com/w/index.php?title=Data:Wall_Upgrade_v1&amp;action=edit\" class=\"external text\" rel=\"nofollow\">metadata</a></span>:\n" +
                "<ul><li> ID (id) - <b><a href=\"/wiki/Buildings_by_number\" title=\"Buildings by number\">1010</a></b>\n" +
                "[<span class=\"noprint plainlinks purgelink\"><a href=\"http://die2nitewiki.com/w/index.php?title=Data:Wall_Upgrade_v1&amp;action=purge\" class=\"external text\" rel=\"nofollow\"><span title=\"Purge this page\">Purge</span></a></span>]",
                WikiConstructionParserErrorType.HTML_BODY_DIGGING_ERROR);
    }

    protected void testForParserException(String dataBody, WikiConstructionParserErrorType error) {
        try {
            parser.digForConstructionData("ignore", dataBody);
            fail("Should have thrown an exception");
        } catch (WikiConstructionParserException err) {
            assertEquals(error, err.getError());
        }
    }

    @Test
    public void parseDataBody() throws Exception {
        String dataBody =
                "<ul><li> ID (id) - <b><a href=\"/wiki/Buildings_by_number\" title=\"Buildings by number\">1010</a></b>\n" +
                "</li><li> Name (name) - <b><a href=\"/wiki/Wall_Upgrade_v1\" title=\"Wall Upgrade v1\">Wall Upgrade v1</a></b>\n" +
                "</li><li> Image (image) - <a href=\"/wiki/File:Small_wallimprove.gif\" class=\"image\"><img alt=\"Small wallimprove.gif\" src=\"/w/images/6/6a/Small_wallimprove.gif\" width=\"16\" height=\"17\" /></a> (<b><a href=\"/wiki/File:Small_wallimprove.gif\" title=\"File:Small wallimprove.gif\">small_wallimprove.gif?</a></b>)\n" +
                "</li><li> Category (category) - <b><a href=\"/wiki/Category:Buildings\" title=\"Category:Buildings\">Buildings</a></b>\n" +
                "</li><li> Subcategory (subcategory) - <b><a href=\"/wiki/Category:Wall_Upgrade_v1\" title=\"Category:Wall Upgrade v1\">Wall Upgrade v1</a></b>\n" +
                "</li><li> Blurb (blurb) - <i>Significantly increases the town's defences.</i>\n" +
                "</li></ul>\n" +
                "<p><br />\n" +
                "Building specific data\n" +
                "</p>\n" +
                "<ul><li> <a href=\"/wiki/Temporary_buildings\" title=\"Temporary buildings\">Temporary</a> (temporary) - <b>false</b>\n" +
                "</li><li> <a href=\"/wiki/Building_upgrades\" title=\"Building upgrades\">Upgradable</a> (upgradable) - <b>false</b>\n" +
                "</li><li> <a href=\"/wiki/Blueprints\" title=\"Blueprints\">Blueprint</a> (blueprint) - <b>false</b>\n" +
                "</li><li> <a href=\"/wiki/Category:Building_Tree\" title=\"Category:Building Tree\">Parent building</a> (parent) - <b><a href=\"/wiki/Category:Unknown_parent\" title=\"Category:Unknown parent\">Unknown parent</a></b>\n" +
                "</li><li> <a href=\"/wiki/Defence\" title=\"Defence\">Defence</a> (defence) - <b>10</b>\n" +
                "</li><li> <a href=\"/wiki/Action_Points\" title=\"Action Points\">AP cost</a> (ap) - <b>30</b>\n" +
                "</li><li> <a href=\"/wiki/Resources\" title=\"Resources\">Number of resources</a> (resources) - <b>2</b>\n" +
                "<ul><li>First resource (res1) - <b><a href=\"/wiki/Twisted_Plank\" title=\"Twisted Plank\">Twisted Plank</a></b>, Quantity (resQ1) - <b>6</b>\n" +
                "</li><li>Second resource (res2) - <b><a href=\"/wiki/Wrought_Iron\" title=\"Wrought Iron\">Wrought Iron</a></b>, Quantity (resQ2) - <b>4</b>\n" +
                "</li></ul>\n" +
                "</li></ul>\n";
        WikiConstructionData construction = parser.parse("URL", dataBody);
        assertConstructionData(construction,
                1010, 10, "Wall Upgrade v1", 30,
                new String[] { "Twisted Plank", "Wrought Iron" },
                new Integer[] { 6, 4 } );
    }

    @Test
    public void readFromFile() throws IOException, WikiConstructionParserException {
        WikiConstructionDataParser parser = new WikiConstructionDataParser();
        parser.setD2NXmlFile(new D2NXmlFile() {
            public void writeFile(final String xmlText, final String subdir, final Integer id) throws IOException, FileNotFoundException {}
        });
        parser.setXmlReader(new WikiReaderFromFile("wall-upgrade-v1.html"));
        WikiConstructionData construction = parser.parse(WikiConstructionLoadType.FROM_WIKI, 1010, "http://die2nitewiki.com/wiki/Data:Wall_Upgrade_v1");
        assertConstructionData(construction,
                1010, 10, "Wall Upgrade v1", 30,
                new String[] { "Twisted Plank", "Wrought Iron" },
                new Integer[] { 6, 4 } );
    }

    protected void assertConstructionData(WikiConstructionData construction, Integer id, Integer defence, String name, Integer ap, String[] names, Integer[] amounts) {
        assertEquals(id, construction.getId());
        assertEquals(name, construction.getName());
        assertEquals(ap, construction.getAp());
        int count = 0;
        for (WikiResourceCost resourceCosts : construction.getResourceCosts()) {
            assertEquals(amounts[count], resourceCosts.getAmount());
            assertEquals(names[count], resourceCosts.getResource());
            count++;
        }
    }
}
