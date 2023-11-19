package com.neko233.easyxml.data;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author SolarisNeko
 * Date on 2023-11-19
 */
public class XmlObjectForXPathTest {

    @Test
    public void testXPath() throws Exception {
        // Create an example XmlObject
        XmlObject xmlObject = createExampleXmlObject();

        // Convert XmlObject to XML string
        String xmlString = xmlObject.toXmlString();

        // output
        System.out.println(xmlString);

        // 在XPath中，表达式 //child 的意思是“从当前节点或其下的任何级别选择所有名为 'child' 的节点，无论它们在XML层次结构中的位置如何”
        // 基于当前节点去查找
        List<XmlObject> result = xmlObject.parseByXpathExpression("child");

        // Assert the result
        assertNotNull(result);
        assertEquals(1, result.size());
        XmlObject childNode = result.get(0);
        assertEquals("child", childNode.getNodeName());
        assertEquals("Child Value", childNode.getNodeValue());
    }


    @Test
    public void testW3cNodeConversion() throws Exception {
        // Create an example XmlObject
        XmlObject xmlObject = createExampleXmlObject();

        // Convert XmlObject to W3C Node
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Node w3cNode = xmlObject.toW3cNode(document);

        // Convert W3C Node back to XmlObject
        XmlObject convertedXmlObject = XmlObject.fromW3cNode(w3cNode);

        // Assert the result
        assertNotNull(convertedXmlObject);
        assertEquals(xmlObject.toXmlString(), convertedXmlObject.toXmlString());
    }

    private XmlObject createExampleXmlObject() {
        XmlObject root = new XmlObject("root", "Root Value", null);

        XmlObject child = new XmlObject("child", "Child Value", root);
        child.addAttribute("attribute", "value");

        root.addChild(child);

        return root;
    }
}
