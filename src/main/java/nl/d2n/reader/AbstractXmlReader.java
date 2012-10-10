package nl.d2n.reader;

import nl.d2n.model.ApplicationException;
import nl.d2n.model.D2NError;
import nl.d2n.model.D2NErrorCode;
import nl.d2n.model.UserKey;
import nl.d2n.reader.sitekey.D2NSiteKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

public abstract class AbstractXmlReader<I> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractXmlReader.class);

    @Autowired
    private XmlReader xmlReader;

    @Autowired
    private D2NSiteKey siteKey;

    public I read(final UserKey userKey) throws ApplicationException {
        return read(userKey, false);
    }
    public I read(final UserKey userKey, final boolean allowReadFromCache) throws ApplicationException {
        final String url = createGetUrl(getUrl(), userKey);
        LOGGER.debug("Accessing URL: "+url);

        final String xmlText;
        try {
            xmlText = readXmlText(url);
        } catch (ApplicationException err) {
            return readFromCache(err, userKey, allowReadFromCache);
        }
        
        try {
            return convertTextToObject(xmlText, true);
        } catch (ApplicationException err) {
//            LOGGER.error("", xmlText);
            return readFromCache(err, userKey, allowReadFromCache);
        }
    }

    protected I readFromCache(ApplicationException err, UserKey userKey, boolean allowReadFromCache) throws ApplicationException {
        if (allowReadFromCache && err.getError().isAllowCacheRead()) {
            return readXmlFromCache(userKey);
        } else {
            throw err;
        }
    }

    protected String readXmlText(final String url) throws ApplicationException {
        try {
            return xmlReader.readStringFromUrl(url);
        } catch (Exception err) {
            LOGGER.error(err.getMessage());
            throw new ApplicationException(D2NErrorCode.COULD_NOT_ACCESS_SITE);
        }
    }

    protected I convertTextToObject(String xmlText, boolean writeLatestXml) throws ApplicationException {
        Document doc = readDocumentFromXmlText(xmlText);
        I info = convertXmlDocument(doc);
        if (writeLatestXml) {
            writeLatestXml(info, xmlText);
        }
        return info;
    }

    protected void storeXml(I info, String xmlText) {} // not stored by default
    
    protected I readXmlFromCache(final UserKey userKey) throws ApplicationException {
        LOGGER.error("Reading from cache not supported.");
        throw new ApplicationException(D2NErrorCode.UNSUPPORTED_OPERATION);
    }
    protected void writeLatestXml(I info, String xmlText) throws ApplicationException {} // empty by default

    protected Document readDocumentFromXmlText(final String xmlText) throws ApplicationException {
        try {
            return xmlReader.readDocumentFromString(xmlText);
        } catch (Exception err) {
            LOGGER.error(err.getMessage());
            throw new ApplicationException(D2NErrorCode.COULD_NOT_PARSE_XML);
        }
    }

    public I convertXmlDocument(final Document document) throws ApplicationException {
        try {
            return convert(document);
        } catch (JAXBException err) {
            LOGGER.error(err.getMessage());
            throw new ApplicationException(D2NErrorCode.COULD_NOT_PARSE_XML);
        }
    }

    protected I convert(final Document doc) throws ApplicationException, JAXBException {
        final Node rootNode = doc.getDocumentElement();

        // Read error node and insert into info object
        Node errorNode = ReaderUtils.getChildNode(rootNode, "error");
        if (errorNode != null) {
            JAXBContext jaxbContext = JAXBContext.newInstance(D2NError.class);
            final D2NError error = (D2NError)jaxbContext.createUnmarshaller().unmarshal(errorNode);
            throw new ApplicationException(error.getCode());
        }

        return convert(rootNode);
    }

    protected abstract I convert(final Node rootNode) throws ApplicationException, JAXBException;

    protected abstract String getUrl();

    protected String createGetUrl(final String url, final UserKey userKey) {
        return url + userKey.getKey() + ";sk=" + siteKey.getKey();
    }

    public void setXmlReader(XmlReader xmlReader)               { this.xmlReader = xmlReader; }
    public void setSiteKey(D2NSiteKey siteKey)                     { this.siteKey = siteKey; }
}

