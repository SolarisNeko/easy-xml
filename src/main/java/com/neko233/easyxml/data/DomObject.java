package com.neko233.easyxml.data;


import com.neko233.easyxml.EasyXmlException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.*;

/**
 * @author SolarisNeko
 * Date on 2023-02-02
 */
public class DomObject {

    // xml path = /path/to/your/node, it not remember your index
    private final String xmlPath;
    private final String rootName;
    private final String nodeValue;
    private final Map<String, String> attributes;

    private final DomObject parentNode;
    private DomObject leftNode;
    private DomObject rightNode;
    private final List<DomObject> childrenNodes;

    public DomObject(String rootName, String nodeValue, DomObject parentNode) {
        this.xmlPath = generateDomPath(rootName, parentNode);
        this.nodeValue = Optional.ofNullable(nodeValue).orElse("").trim();
        this.rootName = rootName;
        this.attributes = new HashMap<>(0);
        this.parentNode = parentNode;
        this.childrenNodes = new ArrayList<>(0);
    }

    private String generateDomPath(String rootName, DomObject parentNode) {
        if (parentNode == null) {
            return "/";
        }
        if ("/".equals(parentNode.xmlPath)) {
            return "/" + rootName;
        }
        return parentNode.xmlPath + "/" + rootName;

    }


    public String toXML() throws EasyXmlException {
        return toXML(false);
    }

    /**
     * 转为 XML
     *
     * @return XML String
     * @throws EasyXmlException 解析异常
     */
    public String toXML(boolean isFormat) throws EasyXmlException {
        try {
            DocumentBuilderFactory domFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = domFact.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element el = doc.createElement(this.rootName);
            this.attributes.forEach(el::setAttribute);
            el.setNodeValue(this.nodeValue);
            doc.appendChild(el);

            for (DomObject childrenNode : this.childrenNodes) {
                createNodeTreeByRecursive(doc, el, childrenNode);
            }

            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            String isIndent = isFormat ? "yes" : "no";
            transformer.setOutputProperty(OutputKeys.INDENT, isIndent);
            transformer.transform(domSource, result);
            return writer.toString();
        } catch (ParserConfigurationException | TransformerException e) {
            throw new EasyXmlException(e);
        }
    }

    private static void createNodeTreeByRecursive(Document doc, Element parentNode, DomObject domObject) {
        Element currentNode = doc.createElement(domObject.rootName);
        domObject.attributes.forEach(currentNode::setAttribute);
        currentNode.setTextContent(domObject.nodeValue);
        parentNode.appendChild(currentNode);

        List<DomObject> childNodes = domObject.getChildrenNodes();
        if (childNodes == null || childNodes.size() == 0) {
            return;
        }
        for (DomObject childNode : childNodes) {
            createNodeTreeByRecursive(doc, currentNode, childNode);
        }
    }

    public String getXmlPath() {
        return xmlPath;
    }

    public String getRootName() {
        return rootName;
    }

    public String getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }

    public void addAttribute(String attributeName, String attributeValue) {
        attributes.put(attributeName, attributeValue);
    }


    public void removeAttribute(String attributeName) {
        attributes.remove(attributeName);
    }

    public DomObject getChild(int index) {
        return childrenNodes.get(index);
    }

    public List<DomObject> getChildren() {
        return childrenNodes;
    }

    public void addChild(DomObject childNode) {
        childrenNodes.add(childNode);
    }


    public void removeChild(DomObject childNode) {
        attributes.remove(childNode);
    }


    public DomObject left() {
        return this.leftNode;
    }

    public DomObject right() {
        return this.rightNode;
    }

    // --------- @Data ----


    public String getNodeValue() {
        return nodeValue;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public DomObject getParentNode() {
        return parentNode;
    }

    public DomObject getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(DomObject leftNode) {
        this.leftNode = leftNode;
    }

    public DomObject getRightNode() {
        return rightNode;
    }

    public void setRightNode(DomObject rightNode) {
        this.rightNode = rightNode;
    }

    public List<DomObject> getChildrenNodes() {
        return childrenNodes;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomObject)) return false;

        DomObject domObject = (DomObject) o;

        if (getRootName() != null ? !getRootName().equals(domObject.getRootName()) : domObject.getRootName() != null)
            return false;
        if (getNodeValue() != null ? !getNodeValue().equals(domObject.getNodeValue()) : domObject.getNodeValue() != null)
            return false;
        if (getAttributes() != null ? !getAttributes().equals(domObject.getAttributes()) : domObject.getAttributes() != null)
            return false;
        if (getParentNode() != null ? !getParentNode().equals(domObject.getParentNode()) : domObject.getParentNode() != null)
            return false;
        return getChildrenNodes() != null ? getChildrenNodes().equals(domObject.getChildrenNodes()) : domObject.getChildrenNodes() == null;
    }

    /**
     * no child tree
     *
     * @return hashcode no child node
     */
    @Override
    public int hashCode() {
        int result = getRootName() != null ? getRootName().hashCode() : 0;
        result = 31 * result + (getXmlPath() != null ? getXmlPath().hashCode() : 0);
        result = 31 * result + (getNodeValue() != null ? getNodeValue().hashCode() : 0);
        result = 31 * result + (getAttributes() != null ? getAttributes().hashCode() : 0);
        result = 31 * result + (getParentNode() != null ? getParentNode().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DomObject{" +
                "xmlPath='" + xmlPath + '\'' +
                ", rootName='" + rootName + '\'' +
                ", nodeValue='" + nodeValue + '\'' +
                ", attributes=" + attributes +
                ", parentNode=" + parentNode +
                '}';
    }
}
