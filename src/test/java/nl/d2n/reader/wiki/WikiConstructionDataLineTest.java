package nl.d2n.reader.wiki;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class WikiConstructionDataLineTest {

    @Test
    public void couldNotFindId() {
        try {
            convertString("Name (name) - Wall Upgrade v1").getText("ID");
        } catch (WikiConstructionDataLineException err) {
            assertEquals(WikiConstructionDataLineErrorType.COULD_NOT_FIND_KEY, err.getError());
        }
    }

    @Test
    public void couldNotConvertToInteger() {
        try {
            convertString("Name (name) - Wall Upgrade v1").getInteger("Name");
        } catch (WikiConstructionDataLineException err) {
            assertEquals(WikiConstructionDataLineErrorType.NOT_AN_INTEGER, err.getError());
        }
    }

    @Test
    public void couldNotFindValue() {
        try {
            convertString("Name (name)").getInteger("Name");
        } catch (WikiConstructionDataLineException err) {
            assertEquals(WikiConstructionDataLineErrorType.COULD_NOT_FIND_VALUE, err.getError());
        }
    }

    @Test
    public void couldNotFindResourceValue() {
        try {
            // Missing the word 'Quantity'
            convertString("First resource (res1) - Twisted Plank, some-other-word-than-quantity (resQ1) - 6").getResourceValue("(res1)");
        } catch (WikiConstructionDataLineException err) {
            assertEquals(WikiConstructionDataLineErrorType.COULD_NOT_FIND_RESOURCE_NAME, err.getError());
        }
    }

    @Test
    public void couldNotFindResourceAmount() {
        try {
            // A ':' instead of a '-'
            convertString("First resource (res1) - Twisted Plank, Quantity (resQ1) : 6").getResourceValue("(res1)");
        } catch (WikiConstructionDataLineException err) {
            assertEquals(WikiConstructionDataLineErrorType.COULD_NOT_FIND_RESOURCE_AMOUNT, err.getError());
        }
    }

    protected WikiConstructionDataLine convertString(String line) {
        List<String> lines = new ArrayList<String>();
        lines.add(line);
        return new WikiConstructionDataLine("", lines);
    }
}
