package com.neko233.easyxml;

import com.neko233.easyxml.data.XmlObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author SolarisNeko
 * Date on 2023-02-19
 */
public class XmlFinderTest {


    @Test
    public void test_find_1() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<root name=\"test\">" +
                "\t<demo id=\"1\">" +
                "\t\t<node3rd id=\"1\"/>" +
                "\t</demo>" +
                "\t<demo id=\"2\"/>" +
                "</root>";

        XmlObject xmlObject = XML.parseToObject(xml);

        List<XmlObject> xmlObjects = XmlFinder.find(xmlObject, "/demo");
        Assert.assertEquals(2, xmlObjects.size());
    }

    @Test
    public void test_find_2() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<root name=\"test\">" +
                "\t<demo id=\"1\">" +
                "\t\t<node3rd id=\"1-1\"/>" +
                "\t</demo>" +
                "\t<demo id=\"2\"/>" +
                "</root>";

        XmlObject xmlObject = XML.parseToObject(xml);

        List<XmlObject> xmlObjects = XmlFinder.find(xmlObject, "/demo/node3rd");
        assert xmlObjects != null;
        String id = xmlObjects.get(0).getAttribute("id");
        Assert.assertEquals("1-1", id);
    }

    @Test
    public void test_find_3_byRegex() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<root name=\"test\">" +
                "\t<demo id=\"1\">" +
                "\t\t<node3rd id=\"1-1\"/>" +
                "\t</demo>" +
                "\t<demo id=\"2\"/>" +
                "</root>";

        XmlObject xmlObject = XML.parseToObject(xml);

        List<XmlObject> xmlObjects = XmlFinder.find(xmlObject, "/demo/node3*");
        assert xmlObjects != null;
        String id = xmlObjects.get(0).getAttribute("id");
        Assert.assertEquals("1-1", id);
    }
}