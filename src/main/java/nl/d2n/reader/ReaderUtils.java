package nl.d2n.reader;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReaderUtils {

    public static Node getChildNode(Node parentNode, String childName) {
        NodeList childNodes = parentNode.getChildNodes();
        for (int nodePos = 0; nodePos < childNodes.getLength(); nodePos++) {
            Node childNode = childNodes.item(nodePos);
            if (childName.equals(childNode.getNodeName())) {
                return childNode;
            }
        }
        return null;
    }

}
