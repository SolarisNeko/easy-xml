package com.neko233.easyxml.data;

import com.neko233.easyxml.api.XmlKvApi;
import com.neko233.easyxml.exception.EasyXmlException;
import com.neko233.easyxml.utils.EasyXmlCollectionUtils;
import com.neko233.easyxml.utils.EasyXmlStringUtils;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.StringWriter;
import java.util.*;

/**
 * XML 对象, 一切都是以 <root> 作为根节点 </root>
 *
 * @author SolarisNeko
 * Date on 2023-02-02
 */
public class XmlObject implements XmlKvApi {

    // xml path = /path/to/your/node, it not remember your index
    private String xmlPath;

    // 当前节点名称 <root .../>, nodeName = root
    private String nodeName;
    //  <root>${nodeValue}</root>
    private String nodeValue;
    //  <root ${key1}="${value1}" /> --> key1 to value1
    private Map<String, String> attributes;

    // parent
    private volatile transient XmlObject parentNode;
    // sibling
    private transient XmlObject leftNode;
    private transient XmlObject rightNode;
    // child
    private transient List<XmlObject> childrenNodes;

    /**
     * @param nodeName   <${nodeName} .../>
     * @param parentNode parent
     */
    public XmlObject(String nodeName, XmlObject parentNode) {
        this.xmlPath = refreshXmlPath(nodeName, parentNode);
        this.nodeName = nodeName;
        this.attributes = new HashMap<>(0);
        this.parentNode = parentNode;
        this.childrenNodes = new ArrayList<>(0);
    }

    /**
     * @param nodeName   <${nodeName} .../>
     * @param nodeValue  <root>${nodeValue}</root>, XML 规范中 nodeValue 其实是一个 child node...
     * @param parentNode parent
     */
    public XmlObject(String nodeName, String nodeValue, XmlObject parentNode) {
        this.xmlPath = refreshXmlPath(nodeName, parentNode);
        this.nodeValue = Optional.ofNullable(nodeValue).orElse("").trim();
        this.nodeName = nodeName;
        this.attributes = new HashMap<>(0);
        this.parentNode = parentNode;
        this.childrenNodes = new ArrayList<>(0);
    }

    /**
     * 从通用规范读取
     * Static method to create an XmlObject from a org.w3c.dom.Node.
     *
     * @param node The org.w3c.dom.Node to convert.
     * @return XmlObject representing the given Node.
     */
    public static XmlObject fromW3cNode(Node node) {
        if (node == null) {
            return null;
        }

        XmlObject xmlObject = new XmlObject(node.getNodeName(), node.getTextContent(), null);

        // Set attributes
        NamedNodeMap attributes = node.getAttributes();
        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attributeNode = attributes.item(i);
                xmlObject.addAttribute(attributeNode.getNodeName(), attributeNode.getNodeValue());
            }
        }

        // Set children nodes
        NodeList childNodes = node.getChildNodes();
        if (childNodes != null) {
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node childNode = childNodes.item(i);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    XmlObject childXmlObject = fromW3cNode(childNode);
                    childXmlObject.parentNode(xmlObject);
                    xmlObject.addChild(childXmlObject);
                }
            }
        }

        return xmlObject;
    }

    /**
     * 创建 w3c 的 xml 文档 root
     *
     * @return w3c node root
     */
    private Document createW3cDocument() {
        try {
            DocumentBuilderFactory domFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = domFact.newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            // Handle the exception according to your needs
            throw new RuntimeException("XmlObject to w3cNode, auto create Document error", e);
        }
    }

    /**
     * 转换成通用规范
     * Convert XmlObject to org.w3c.dom.Node.
     *
     * @return org.w3c.dom.Node representing this XmlObject.
     */
    public org.w3c.dom.Node toW3cNode() {
        Document document = createW3cDocument();
        return toW3cNode(document);
    }

    /**
     * 转换成通用规范
     * Convert XmlObject to org.w3c.dom.Node.
     *
     * @param document The Document to create nodes.
     * @return org.w3c.dom.Node representing this XmlObject.
     */
    public org.w3c.dom.Node toW3cNode(Document document) {
        Element element = document.createElement(this.nodeName);

        // text 为空的时候不塞入
        if (!EasyXmlStringUtils.isBlank(this.nodeName) && EasyXmlCollectionUtils.isEmpty(this.childrenNodes)) {
            element.setTextContent(this.nodeValue);
        }

        // Set attributes
        for (Map.Entry<String, String> entry : this.attributes.entrySet()) {
            element.setAttribute(entry.getKey(), entry.getValue());
        }

        // Set children nodes
        for (XmlObject childXmlObject : this.childrenNodes) {
            org.w3c.dom.Node childNode = childXmlObject.toW3cNode(document);
            element.appendChild(childNode);
        }

        return element;
    }

    /**
     * 生成 XML Path
     *
     * @param rootName   当前节点名
     * @param parentNode 父节点
     * @return XML Path
     */
    private String refreshXmlPath(String rootName, XmlObject parentNode) {
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
            childNode.xmlPath = refreshXmlPath(childNode.nodeName, this);
        }
    }


    public String toXmlString() throws EasyXmlException {
        return toXmlString(true);
    }

    /**
     * 转为 XML
     *
     * @return XML String
     * @throws EasyXmlException 解析异常
     */
    public String toXmlString(boolean isPrettyFormat) throws EasyXmlException {
        try {
            DocumentBuilderFactory domFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = domFact.newDocumentBuilder();
            Document doc = builder.newDocument();

            // 创建根节点
            Element el = doc.createElement(this.nodeName);
            this.attributes.forEach(el::setAttribute);

            // Only set node value for non-root nodes
            if (this.parentNode != null) {
                el.setTextContent(this.nodeValue);
            }

            doc.appendChild(el);

            for (XmlObject childrenNode : this.childrenNodes) {
                createNodeTreeByRecursive(doc, el, childrenNode);
            }

            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);

            // 设置 Transformer 的属性，包括 XML 头部信息
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            String isIndent = isPrettyFormat ? "yes" : "no";

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
        Element currentNode = doc.createElement(xmlObject.nodeName);
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

    public String getNodeName() {
        return nodeName;
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
        childrenNodes.remove(childNode);
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
        this.xmlPath = refreshXmlPath(this.nodeName, parentNode);
        return this;
    }

    /**
     * 当前节点名
     *
     * @param nodeName
     * @return
     */
    public XmlObject nodeName(String nodeName) {
        this.nodeName = nodeName;
        this.xmlPath = refreshXmlPath(this.nodeName, this.parentNode);

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

    /**
     * [XPath]
     * 执行XPath查询并返回XmlObject列表
     * ps: currentNode = xpath startPath
     * like: root (current) > child, xpath you find child just use "child"
     *
     * @param xpathExpression XPath表达式
     * @return 匹配的XmlObject列表
     */
    public List<XmlObject> parseByXpathExpression(String xpathExpression) throws EasyXmlException {
        try {
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xPath = xPathFactory.newXPath();

            // 编译XPath表达式
            XPathExpression expression = xPath.compile(xpathExpression);

            // 执行XPath查询
            Node rootNode = this.toW3cNode();
            // 节点集查找
            NodeList nodeList = (NodeList) expression.evaluate(rootNode, XPathConstants.NODESET);

            // 将查询结果转换为XmlObject列表
            List<XmlObject> result = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                XmlObject xmlObject = XmlObject.fromW3cNode(node);
                result.add(xmlObject);
            }

            return result;
        } catch (XPathExpressionException e) {
            throw new EasyXmlException("XPath evaluation error", e);
        }
    }

    /**
     * 将 XmlObject 转换为 Document
     *
     * @return 转换后的 Document
     */
    public Document toDocument() {
        try {
            DocumentBuilderFactory domFact = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = domFact.newDocumentBuilder();

            // 创建新的 Document
            Document document = builder.newDocument();

            // 转换 XmlObject 到 Document
            Node rootElement = this.toW3cNode(document);
            document.appendChild(document.importNode(rootElement, true));

            return document;
        } catch (ParserConfigurationException e) {
            // Handle the exception according to your needs
            throw new RuntimeException("XmlObject to Document conversion error", e);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XmlObject)) return false;

        XmlObject xmlObject = (XmlObject) o;

        if (getNodeName() != null ? !getNodeName().equals(xmlObject.getNodeName()) : xmlObject.getNodeName() != null)
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
        int result = getNodeName() != null ? getNodeName().hashCode() : 0;
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
                ", rootName='" + nodeName + '\'' +
                ", nodeValue='" + nodeValue + '\'' +
                ", attributes=" + attributes +
                ", parentNode=" + parentNode +
                '}';
    }


}
