package nl.d2n.reader;

import nl.d2n.dao.UserDao;
import nl.d2n.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Service
public class D2NXmlReader extends AbstractXmlReader<InfoWrapper> {

    private static final Logger LOGGER = LoggerFactory.getLogger(D2NXmlReader.class);

    public final static String BASE_URL = "http://www.die2nite.com/xml/?k=";

    @Autowired
    private D2NXmlCache cache;

    @Autowired
    private UserDao userDao;

    @Override
    protected InfoWrapper readXmlFromCache(final UserKey userKey) throws ApplicationException {
        return readXmlFromCache(userDao.getCityId(userKey));
    }

    public InfoWrapper readXmlFromCache(final Integer cityId) throws ApplicationException {
        if (cityId == null) {
            throw new ApplicationException(D2NErrorCode.NOT_IN_GAME);
        }
        InfoWrapper infoWrapper = new InfoWrapper();
        infoWrapper.setInfo(cache.readFromCache(cityId));
        if (infoWrapper.getInfo() == null) {
            String xmlText = cache.readFromFile(cityId);
            infoWrapper = convertTextToObject(xmlText, false);
            // This call is particularly useful after a server restart -- it fills up the cache again
            cache.writeToCache(cityId, infoWrapper.getInfo());
            infoWrapper.setStatus(InfoStatus.READ_FROM_FILE);
        } else { // Only set to stale if reading from the cache; if reading from file it must be treated as new
            infoWrapper.setStatus(InfoStatus.READ_FROM_CACHE);
        }
        return infoWrapper;
    }

    @Override
    protected void storeXml(InfoWrapper info, String xmlText) {
        info.setXml(xmlText);
    }
    
    @Override
    protected void writeLatestXml(InfoWrapper infoWrapper, String xmlText) throws ApplicationException {
        Integer cityId = infoWrapper.getInfo().getGameHeader().getGame().getId();
        cache.writeToCache(cityId, infoWrapper.getInfo());
        cache.writeToFile(cityId, xmlText);
    }

    protected InfoWrapper convert(final Node rootNode) throws ApplicationException, JAXBException {
        // Read data node
        Node dataNode = ReaderUtils.getChildNode(rootNode, "data");
        JAXBContext jaxbContext = JAXBContext.newInstance(Info.class);
        final Info info = (Info)jaxbContext.createUnmarshaller().unmarshal(dataNode);

        // Read header node and insert into info object
        Node headersNode = ReaderUtils.getChildNode(rootNode, "headers");
        jaxbContext = JAXBContext.newInstance(GameHeader.class);
        final GameHeader gameHeader = (GameHeader)jaxbContext.createUnmarshaller().unmarshal(headersNode);
        info.setGameHeader(gameHeader);

        return new InfoWrapper(info);
    }

    protected String getUrl() {
        return BASE_URL;
    }

    public void setCache(D2NXmlCache cache) { this.cache = cache; }
}
