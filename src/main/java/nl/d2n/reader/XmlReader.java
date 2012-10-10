package nl.d2n.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class XmlReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(XmlReader.class);
    
    public Document readFromUrl(String url) throws Exception {
        return readDocumentFromString(readStringFromUrl(url));
    }
    public Document readDocumentFromString(String body) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = fact.newDocumentBuilder();
        StringReader reader = new StringReader(body);
        InputSource inputSource = new InputSource(reader);
        inputSource.setEncoding("UTF-8");
        Document doc = builder.parse(inputSource);
        reader.close();
        return doc;
    }
    @SuppressWarnings({"ConstantConditions"})
    public String readStringFromUrl(String url) throws IOException {

        HttpURLConnection connection = null;
        try {
            URL serverAddress = new URL(url);
            //set up out communications stuff
            connection = null;

            //Set up the initial connection
            connection = (HttpURLConnection)serverAddress.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setReadTimeout(4000);
            connection.setConnectTimeout(4000);

            connection.connect();

            BufferedReader rd  = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }
            return sb.toString();
        }
        finally {
            connection.disconnect();
        }
    }

}
