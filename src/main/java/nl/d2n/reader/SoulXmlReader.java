package nl.d2n.reader;

import nl.d2n.model.ApplicationException;
import nl.d2n.model.Profile;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.w3c.dom.Node;

@Service
public class SoulXmlReader extends AbstractXmlReader<Profile> {

    public final static String BASE_URL = "http://www.die2nite.com/xml/ghost?k=";

    protected Profile convert(final Node rootNode) throws ApplicationException, JAXBException {
        Node dataNode = ReaderUtils.getChildNode(rootNode, "data");
        if (dataNode == null) {
            return null;
        }
        JAXBContext jaxbContext = JAXBContext.newInstance(Profile.class);
        return (Profile)jaxbContext.createUnmarshaller().unmarshal(dataNode);
    }

    protected String getUrl() {
        return BASE_URL;
    }
}
