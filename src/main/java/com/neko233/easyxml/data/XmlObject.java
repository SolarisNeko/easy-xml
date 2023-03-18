package com.neko233.easyxml.data;


import com.neko233.easyxml.EasyXmlException;
import com.neko233.easyxml.api.XmlKvApi;
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
 * XML 对象
 *
 * @author SolarisNeko
 * Date on 2023-02-02
 */
public class XmlObject implements XmlKvApi {

    public static final String XML_PATH_SEPERATOR = "/";
    // xml path = /path/to/your/node, it not remember your index
    private String xmlPath;
    private String rootName;
    private String nodeValue;
    private Map<String, String> attributes;

    private volatile XmlObject parentNode;
    private XmlObject leftNode;
    private XmlObject rightNode;
    private List<XmlObject> childrenNodes;

    public XmlObject(String rootName, String nodeValue, XmlObject parentNode) {
        this.xmlPath = generateXmlPath(rootName, parentNode);
        this.nodeValue = Optional.ofNullable(nodeValue).orElse("").trim();
        this.rootName = rootName;
        this.attributes = new HashMap<>(0);
        this.parentNode = parentNode;
        this.childrenNodes = new ArrayList<>(0);
    }

    /**
     * 生成 XML Path
     *
     * @param rootName   当前节点名
     * @param parentNode 父节点
     * @return XML Path
     */
    private String generateXmlPath(String rootName, XmlObject parentNode) {
        if (parentNode == null) {
            updateXmlPathListener();
            return "/";
        }
        if ("/".equals(parentNode.xmlPath)) {
            updateXmlPathListener();
            return "/" + rootName;
        }

        updateXmlPathListener();
        return parentNode.xmlPath + "/" + rootName;

    }

    private void updateXmlPathListener() {
        if (Objects.equals("/", this.xmlPath)) {
            return;
        }
        List<XmlObject> children = this.getChildren();
        if (children == null || children.size() == 0) {
            return;
        }
        // tree update xmlPath
        for (XmlObject childNode : children) {
            childNode.xmlPath = generateXmlPath(childNode.rootName, this);
        }
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

            for (XmlObject childrenNode : this.childrenNodes) {
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

    /**
     * 创建节点
     *
     * @param doc
     * @param parentNode
     * @param xmlObject
     */
    private static void createNodeTreeByRecursive(Document doc, Element parentNode, XmlObject xmlObject) {
        Element currentNode = doc.createElement(xmlObject.rootName);
        xmlObject.attributes.forEach(currentNode::setAttribute);
        currentNode.setTextContent(xmlObject.nodeValue);
        parentNode.appendChild(currentNode);

        List<XmlObject> childNodes = xmlObject.getChildrenNodes();
        if (childNodes == null || childNodes.size() == 0) {
            return;
        }
        for (XmlObject childNode : childNodes) {
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

    public XmlObject getChild(int index) {
        return childrenNodes.get(index);
    }

    public List<XmlObject> getChildren() {
        return childrenNodes;
    }

    public void addChild(XmlObject childNode) {
        childrenNodes.add(childNode);
    }


    public void removeChild(XmlObject childNode) {
        attributes.remove(childNode);
    }


    public XmlObject left() {
        return this.leftNode;
    }

    public XmlObject left(XmlObject leftNode) {
        this.leftNode = leftNode;
        return this;
    }

    public XmlObject right() {
        return this.rightNode;
    }

    public XmlObject right(XmlObject rightNode) {
        this.rightNode = rightNode;
        return this;
    }

    /**
     * 父节点
     *
     * @return
     */
    public XmlObject parentNode() {
        return parentNode;
    }

    /**
     * [Special] 设置父节点
     */
    public XmlObject parentNode(XmlObject parentNode) {
        this.parentNode = parentNode;
        this.xmlPath = generateXmlPath(this.rootName, parentNode);
        return this;
    }

    /**
     * 当前节点名
     *
     * @param newRootName
     * @return
     */
    public XmlObject rootName(String newRootName) {
        this.rootName = newRootName;
        this.xmlPath = generateXmlPath(this.rootName, this.parentNode);

        return this;
    }


    // --------- @Data ----


    public String getNodeValue() {
        return nodeValue;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }


    public XmlObject getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(XmlObject leftNode) {
        this.leftNode = leftNode;
    }

    public XmlObject getRightNode() {
        return rightNode;
    }

    public void setRightNode(XmlObject rightNode) {
        this.rightNode = rightNode;
    }

    public List<XmlObject> getChildrenNodes() {
        return childrenNodes;
    }

    // setter

    public void setNodeValue(String nodeValue) {
        this.nodeValue = nodeValue;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }


    public void setChildrenNodes(List<XmlObject> childrenNodes) {
        this.childrenNodes = childrenNodes;
    }


    @Override
    public List<String> getAllKeys() {
        return new ArrayList<>(getAttributes().keySet());
    }

    @Override
    public String getString(Integer index) {
        int count = 0;
        for (String key : getAttributes().keySet()) {
            if (count == index) {
                return getAttribute(key);
            }
            count++;
        }
        return null;
    }

    @Override
    public String getString(String key) {
        return getAttribute(key);
    }

    //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XmlObject)) return false;

        XmlObject xmlObject = (XmlObject) o;

        if (getRootName() != null ? !getRootName().equals(xmlObject.getRootName()) : xmlObject.getRootName() != null)
            return false;
        if (getNodeValue() != null ? !getNodeValue().equals(xmlObject.getNodeValue()) : xmlObject.getNodeValue() != null)
            return false;
        if (getAttributes() != null ? !getAttributes().equals(xmlObject.getAttributes()) : xmlObject.getAttributes() != null)
            return false;
        if (parentNode() != null ? !parentNode().equals(xmlObject.parentNode()) : xmlObject.parentNode() != null)
            return false;
        return getChildrenNodes() != null ? getChildrenNodes().equals(xmlObject.getChildrenNodes()) : xmlObject.getChildrenNodes() == null;
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
        result = 31 * result + (parentNode() != null ? parentNode().hashCode() : 0);
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
