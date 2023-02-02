package com.neko233.easyxml.data;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author SolarisNeko
 * Date on 2023-02-02
 */
public class XmlTree {


    @NotNull
    public static DomObject initNodeTree(final Node root, final DomObject parentDomObject) {
        NamedNodeMap attributes = root.getAttributes();
        for (int i = 0; attributes != null && i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            parentDomObject.addAttribute(attribute.getNodeName(), attribute.getNodeValue());
        }

        final NodeList childNodes = root.getChildNodes();
        for (int i = 0; childNodes != null && i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode == null) {
                continue;
            }
            // w3c
            if (childNode.getNodeName().equals("#text")) {
                continue;
            }
            final DomObject childDomObject = new DomObject(childNode.getNodeName());
            final DomObject domObject = initNodeTree(childNode, childDomObject);
            parentDomObject.addChild(domObject);
        }
        return parentDomObject;
    }

}
