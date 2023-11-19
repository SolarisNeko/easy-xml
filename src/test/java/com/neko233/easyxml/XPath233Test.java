package com.neko233.easyxml;

import com.neko233.easyxml.data.XmlObject;
import com.neko233.easyxml.exception.EasyXmlException;
import org.junit.Test;
import org.w3c.dom.Document;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author SolarisNeko
 * Date on 2023-11-19
 */
public class XPath233Test {


    @Test
    public void testParseXmlByXPath() {
        String xml = "<root><element>Value1</element><element>Value2</element></root>";
        String xpathExpression = "//element";

        try {
            List<XmlObject> result = XPath233.parseXmlText(xml, xpathExpression);

            assertNotNull(result);
            assertEquals(2, result.size());

            // Assuming XmlObject has a method to get the node value
            assertEquals("Value1", result.get(0).getNodeValue());
            assertEquals("Value2", result.get(1).getNodeValue());

        } catch (EasyXmlException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testEvaluateXPathWithDocument() throws EasyXmlException {
        // 创建一个示例 XmlObject
        XmlObject root = new XmlObject("root", null);
        XmlObject child = new XmlObject("child", "childValue", root);
        root.addChild(child);

        // 将 XmlObject 转换为 Document
        Document document = root.toDocument();

        // 使用 XPath233 检索 XmlObject
        XmlObject result = XPath233.parseW3cDocument(document, "/root/child");

        // 验证结果
        assertNotNull(result);
        assertEquals("childValue", result.getNodeValue());
    }

    @Test
    public void testEvaluateXPathWithXmlObject() throws EasyXmlException {
        // 创建一个示例 XmlObject
        XmlObject root = new XmlObject("root", null);
        XmlObject child = new XmlObject("child", "childValue", root);
        root.addChild(child);

        // 使用 XPath233 检索 XmlObject
        XmlObject result = XPath233.parseXmlObject(root, "/root/child");

        // 验证结果
        assertNotNull(result);
        assertEquals("childValue", result.getNodeValue());
    }

    @Test
    public void testEvaluateXPathNotFound() throws EasyXmlException {
        // 创建一个示例 XmlObject
        XmlObject root = new XmlObject("root", null);

        // 使用 XPath233 检索 XmlObject，但是 XPath 表达式找不到匹配
        XmlObject result = XPath233.parseXmlObject(root, "/nonexistent");

        // 验证结果
        assertNull(result);
    }

    @Test
    public void testEvaluateXPathExpressionException() {
        // 创建一个示例 XmlObject
        XmlObject root = new XmlObject("root", null);

        // 使用 XPath233 检索 XmlObject，但是 XPath 表达式无效
        assertThrows(EasyXmlException.class, () -> XPath233.parseXmlObject(root, "//[@invalid"));
    }
}
