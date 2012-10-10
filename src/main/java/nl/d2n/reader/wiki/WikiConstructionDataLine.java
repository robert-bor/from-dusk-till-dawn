package nl.d2n.reader.wiki;

import java.util.List;

public class WikiConstructionDataLine {

    private String page;
    private List<String> lines;

    public WikiConstructionDataLine(String page, List<String> lines) {
        this.page = page;
        this.lines = lines;
    }

    public Integer getInteger(String field) throws WikiConstructionDataLineException {
        String line = getLine(field);
        return convertStringToInteger(getValue(field, line), field, line);
    }
    public String getText(String field) throws WikiConstructionDataLineException {
        return getValue(field, getLine(field));
    }

    protected Integer convertStringToInteger(String text, String field, String line) throws WikiConstructionDataLineException {
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException err) {
            throw new WikiConstructionDataLineException(page, WikiConstructionDataLineErrorType.NOT_AN_INTEGER, line, field);
        }
    }

    public Integer getAmountValue(String field) throws WikiConstructionDataLineException {
        String line = getLine(field);
        String value = getValue(field, line);
        int start = value.lastIndexOf("-");
        if (start == -1) {
            throw new WikiConstructionDataLineException(page, WikiConstructionDataLineErrorType.COULD_NOT_FIND_RESOURCE_AMOUNT, line, field);
        }
        return convertStringToInteger(value.substring(start+1).trim(), field, line);
    }
    public String getResourceValue(String field) throws WikiConstructionDataLineException {
        String line = getLine(field);
        String value = getValue(field, line);
        int end = value.indexOf(", Quantity (");
        if (end == -1) {
            throw new WikiConstructionDataLineException(page, WikiConstructionDataLineErrorType.COULD_NOT_FIND_RESOURCE_NAME, line, field);
        }
        return value.substring(0, end).trim();
    }

    public String getValue(String field, String line) throws WikiConstructionDataLineException {
        int start = line.indexOf("-");
        if (start == -1) {
            throw new WikiConstructionDataLineException(page, WikiConstructionDataLineErrorType.COULD_NOT_FIND_VALUE, line, field);
        }
        return line.substring(start+1).trim();
    }

    public String getLine(String field) throws WikiConstructionDataLineException {
        String line = WikiConstructionDataParser.getLine(lines, field);
        if (line == null) {
            throw new WikiConstructionDataLineException(page, WikiConstructionDataLineErrorType.COULD_NOT_FIND_KEY, null, field);
        }
        return line;
    }

}
