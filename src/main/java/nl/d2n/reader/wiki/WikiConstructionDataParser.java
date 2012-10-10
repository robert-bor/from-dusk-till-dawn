package nl.d2n.reader.wiki;

import nl.d2n.reader.D2NXmlFile;
import nl.d2n.reader.ReaderUtils;
import nl.d2n.reader.XmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Component
public class WikiConstructionDataParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(WikiConstructionDataParser.class);

    public static final String START_ANCHOR = "<p>This page contains the following ";
    public static final String END_ANCHOR = "[<span class=\"noprint plainlinks purgelink\">";

    @Autowired
    private XmlReader xmlReader;

    @Autowired
    private D2NXmlFile fileAccessor;
    
    public WikiConstructionData parse(WikiConstructionLoadType loadType, Integer id, String url) throws WikiConstructionParserException {
        try {
            final String htmlBody;
            if (loadType == WikiConstructionLoadType.FROM_FILE) {
                htmlBody = readFile(url, id);
            } else { // FROM WIKI
                htmlBody = readStringFromUrl(url);
            }
            try {
                writeFile(htmlBody, id);
            } catch (IOException err) {
                LOGGER.error("Unable to write file: "+id);
                // But the show must go on... so ignore
            }
            String constructionData = digForConstructionData(url, htmlBody);
            constructionData = constructionData.replaceAll("&nbsp;", "&#160;");
            return parse(url, constructionData);
        } catch (WikiConstructionParserException err) {
            logError(err);
            throw err;
        }
    }

    public String readFile(String url, Integer id) throws WikiConstructionParserException {
        try {
            return fileAccessor.readFile("buildings-from-wiki", id);
        } catch (IOException err) {
            throw new WikiConstructionParserException(url, WikiConstructionParserErrorType.COULD_NOT_READ_FILE, err.getMessage());
        }
    }
    public void writeFile(String htmlBody, Integer id) throws IOException {
        fileAccessor.writeFile(htmlBody, "buildings-from-wiki", id);
    }

    public String readStringFromUrl(String url) throws WikiConstructionParserException {
        try {
            return xmlReader.readStringFromUrl(url);
        } catch (IOException err) {
            throw new WikiConstructionParserException(url, WikiConstructionParserErrorType.URL_READ_ERROR, "Could not read from the URL");
        }
    }

    public String digForConstructionData(String url, String htmlBody) throws WikiConstructionParserException {
        int start = htmlBody.indexOf(START_ANCHOR);
        if (start == -1) {
            throw new WikiConstructionParserException(url, WikiConstructionParserErrorType.HTML_BODY_DIGGING_ERROR, "inability to find anchor text [" + START_ANCHOR + "]");
        }
        start = htmlBody.indexOf("<ul>", start);
        if (start == -1) {
            throw new WikiConstructionParserException(url, WikiConstructionParserErrorType.HTML_BODY_DIGGING_ERROR, "no <ul> found");
        }
        int end = htmlBody.indexOf(END_ANCHOR);
        if (end == -1) {
            throw new WikiConstructionParserException(url, WikiConstructionParserErrorType.HTML_BODY_DIGGING_ERROR, "inability to find anchor text [" + END_ANCHOR + "]");
        }
        end = htmlBody.lastIndexOf("<p>", end);
        if (end == -1 || start >= end) {
            throw new WikiConstructionParserException(url, WikiConstructionParserErrorType.HTML_BODY_DIGGING_ERROR, "no <p> found");
        }
        return htmlBody.substring(start, end);
    }

    protected void logError(WikiConstructionParserException err) {
        LOGGER.error("Parsing page "+err.getPage()+" failed on: "+err.getMessage());
    }

    public WikiConstructionData parse(String page, String constructionData) throws WikiConstructionParserException {
        try {
            return new WikiConstructionData(page, parseLines(constructionData));
        } catch (Exception err) {
            throw new WikiConstructionParserException(page, WikiConstructionParserErrorType.HTML_BODY_PARSING_ERROR, err.getMessage());
        }
    }

    protected List<String> parseLines(String constructionData) throws ParserConfigurationException, IOException, SAXException {
        List<String> lines = new ArrayList<String>();
        Node root = parseXmlSnippet(constructionData);
        parseUnsortedList(lines, root);
        return lines;
    }

    protected void parseUnsortedList(List<String> lines, Node unsortedList) {
        NodeList childNodes = unsortedList.getChildNodes();
        for (int nodePos = 0; nodePos < childNodes.getLength(); nodePos++) {
            Node childNode = childNodes.item(nodePos);
            if (childNode.getNodeName().equals("ul")) {
                parseList(lines, childNode);
            }
        }
    }

    protected void parseList(List<String> lines, Node unsortedList) {
        NodeList listItems = unsortedList.getChildNodes();
        for (int nodePos = 0; nodePos < listItems.getLength(); nodePos++) {
            Node listItem = listItems.item(nodePos);
            StringTokenizer tokenizer = new StringTokenizer(listItem.getTextContent(), "\n");
            while (tokenizer.hasMoreTokens()) {
                lines.add(tokenizer.nextToken().trim());
            }
        }
    }

    protected Node parseXmlSnippet(String constructionData) throws ParserConfigurationException, IOException, SAXException {
        constructionData = "<root>" + constructionData + "</root>";
        Document doc = xmlReader.readDocumentFromString(constructionData);
        return ReaderUtils.getChildNode(doc, "root");
    }

    public static String getLine(List<String> lines, String keyContains) {
        for (String line : lines) {
            if (line.contains(keyContains)) {
                return line;
            }
        }
        return null;
    }

    public void setD2NXmlFile(D2NXmlFile fileAccessor) { this.fileAccessor = fileAccessor; }
    public void setXmlReader(XmlReader xmlReader) { this.xmlReader = xmlReader; }
}
