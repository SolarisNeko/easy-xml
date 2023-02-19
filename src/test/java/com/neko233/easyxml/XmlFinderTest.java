package com.neko233.easyxml;

import com.neko233.easyxml.data.DomObject;
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

        DomObject domObject = XML.toObject(xml);

        List<DomObject> domObjects = XmlFinder.find(domObject, "/demo");
        Assert.assertEquals(2, domObjects.size());
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

        DomObject domObject = XML.toObject(xml);

        List<DomObject> domObjects = XmlFinder.find(domObject, "/demo/node3rd");
        assert domObjects != null;
        String id = domObjects.get(0).getAttribute("id");
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

        DomObject domObject = XML.toObject(xml);

        List<DomObject> domObjects = XmlFinder.find(domObject, "/demo/node3*");
        assert domObjects != null;
        String id = domObjects.get(0).getAttribute("id");
        Assert.assertEquals("1-1", id);
    }
}