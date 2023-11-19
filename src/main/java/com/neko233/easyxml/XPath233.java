package com.neko233.easyxml;

import com.neko233.easyxml.data.XmlObject;
import com.neko233.easyxml.exception.EasyXmlException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * XPath 工具
 */
public class XPath233 {


    /**
     * 根据 XPath 表达式检索 xml Text
     *
     * @param xml             完整的 XML
     * @param xpathExpression XPath 表达式
     * @return 匹配的 XmlObject 列表，如果未找到则为空列表
     */
    public static List<XmlObject> parseXmlText(String xml, String xpathExpression) throws EasyXmlException {
        List<XmlObject> resultList = new ArrayList<>();

        try {
            // 创建 DocumentBuilder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // 解析 XML 字符串并创建 Document 对象
            Document document = builder.parse(new InputSource(new StringReader(xml)));

            // 创建 XPath 对象
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            // 编译 XPath 表达式
            XPathExpression expr = xpath.compile(xpathExpression);

            // 评估 XPath 表达式并获取结果
            NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

            // 处理结果
            for (int i = 0; i < nodeList.getLength(); i++) {
                // 转换为 XmlObject 并添加到结果列表
                XmlObject result = XmlObject.fromW3cNode(nodeList.item(i));
                resultList.add(result);
            }
        } catch (Exception e) {
            throw new EasyXmlException("Error parsing XML by XPath", e);
        }

        return resultList;
    }

    /**
     * 根据 XPath 表达式检索 XmlObject
     *
     * @param document        作为起始节点的 Document
     * @param xpathExpression XPath 表达式
     * @return 匹配的 XmlObject，如果未找到则为 null
     */
    public static XmlObject parseW3cDocument(Document document, String xpathExpression) {
        try {
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            // 编译 XPath 表达式
            XPathExpression expr = xpath.compile(xpathExpression);

            // 评估 XPath 表达式并获取结果
            NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

            // 处理结果
            if (nodeList.getLength() > 0) {
                // 假设第一个匹配的节点是我们想要的
                Node matchedNode = nodeList.item(0);
                // 转换为 XmlObject
                return XmlObject.fromW3cNode(matchedNode);
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据 XPath 表达式检索 XmlObject
     *
     * @param xmlObject       作为起始节点的 XmlObject
     * @param xpathExpression XPath 表达式
     * @return 匹配的 XmlObject，如果未找到则为 null
     */
    public static XmlObject parseXmlObject(XmlObject xmlObject, String xpathExpression) throws EasyXmlException {
        try {
            // 将 XmlObject 转换为 Document
            Document document = xmlObject.toDocument();

            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            // 编译 XPath 表达式
            XPathExpression expr = xpath.compile(xpathExpression);

            // 评估 XPath 表达式并获取结果
            NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

            // 处理结果
            if (nodeList.getLength() > 0) {
                // 假设第一个匹配的节点是我们想要的
                Node matchedNode = nodeList.item(0);
                // 转换为 XmlObject
                return XmlObject.fromW3cNode(matchedNode);
            }
        } catch (XPathExpressionException e) {
            throw new EasyXmlException(e);
        }
        return null;
    }


}
