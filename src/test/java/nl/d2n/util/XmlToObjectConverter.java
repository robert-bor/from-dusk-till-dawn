package nl.d2n.util;

import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.StringReader;

public class XmlToObjectConverter {

    public static Object convertXmlToObject(String xml, Class clazz) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        return jaxbContext.createUnmarshaller().unmarshal(new StringReader(xml));
    }

    public static Object convertXmlToObject(Document doc, Class clazz) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        return jaxbContext.createUnmarshaller().unmarshal(doc);
    }
}
