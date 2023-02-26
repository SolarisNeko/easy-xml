package com.neko233.easyxml.data;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Objects;
import java.util.Optional;

/**
 * @author SolarisNeko
 * Date on 2023-02-02
 */
public class XmlTree {


    public static final String TEXT_NODE_NAME = "#text";

    @NotNull
    public static XmlObject initNodeTree(final Node root, final XmlObject parentXmlObject) {
        NamedNodeMap attributes = root.getAttributes();
        for (int i = 0; attributes != null && i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            parentXmlObject.addAttribute(attribute.getNodeName(), attribute.getNodeValue());
        }

        final NodeList childNodes = root.getChildNodes();
        XmlObject leftXmlObject = null;
        for (int i = 0; childNodes != null && i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode == null) {
                continue;
            }
            Node firstChild = childNode.getFirstChild();
            // w3c
            String nodeName = childNode.getNodeName();
            if (TEXT_NODE_NAME.equals(nodeName)) {
                continue;
            }

            // find childNode is #text
            String nodeValue = null;
            if (firstChild != null && Objects.equals(TEXT_NODE_NAME, firstChild.getNodeName())) {
                nodeValue = Optional.ofNullable(firstChild.getNodeValue()).orElse("").trim();
            }

            final XmlObject childXmlObject = new XmlObject(nodeName, nodeValue, parentXmlObject);
            // recursive Tree
            final XmlObject xmlObject = initNodeTree(childNode, childXmlObject);

            parentXmlObject.addChild(xmlObject);

            // brother
            childXmlObject.setLeftNode(leftXmlObject);
            if (leftXmlObject != null) {
                leftXmlObject.setRightNode(childXmlObject);
            }
            leftXmlObject = childXmlObject;
        }
        return parentXmlObject;
    }

}
