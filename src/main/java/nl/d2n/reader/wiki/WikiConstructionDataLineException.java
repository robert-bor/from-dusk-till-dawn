package nl.d2n.reader.wiki;

public class WikiConstructionDataLineException extends Exception {

    private String page;
    private WikiConstructionDataLineErrorType error;
    private String line;
    private String field;

    public WikiConstructionDataLineException(String page, WikiConstructionDataLineErrorType error,
                                             String line, String field) {
        this.page = page;
        this.error = error;
        this.line = line;
        this.field = field;
    }

    public String getPage() {
        return this.page;
    }
    public WikiConstructionDataLineErrorType getError() {
        return this.error;
    }
    public String getLine() {
        return this.line;
    }
    public String getField() {
        return this.field;
    }
}
