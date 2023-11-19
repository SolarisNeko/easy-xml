package com.neko233.easyxml.data;

import com.neko233.easyxml.XML;
import org.junit.Assert;
import org.junit.Test;

public class XmlObjectTest {


    @Test
    public void toXml_demo() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<root name=\"test\">" +
                "\t<demo id=\"1\">123</demo>" +
                "</root>";

        XmlObject xmlObject = XML.parseToObject(xml);
        String target = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<root name=\"test\">\n" +
                "<demo id=\"1\">123</demo>\n" +
                "</root>\n";
        Assert.assertEquals(target, xmlObject.toXmlString());
    }


    @Test
    public void toXml_if_modify_rootName() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<root name=\"test\">" +
                "\t<demo id=\"1\">123</demo>" +
                "</root>";

        XmlObject xmlObject = XML.parseToObject(xml);
        xmlObject.nodeName("newRoot");

        String target = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<newRoot name=\"test\">\n" +
                "<demo id=\"1\">123</demo>\n" +
                "</newRoot>\n";
        Assert.assertEquals(target, xmlObject.toXmlString());
        Assert.assertEquals("/", xmlObject.getXmlPath());
    }


    @Test
    public void toXml_if_modify_rootName_child_1() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<root name=\"test\">" +
                "\t<demo id=\"1\">123</demo>" +
                "</root>";

        XmlObject xmlObject = XML.parseToObject(xml);
        xmlObject.nodeName("newRoot");

        XmlObject demo = xmlObject.getChild(0);
        demo.nodeName("newDemo");

        Assert.assertEquals("/newDemo", demo.getXmlPath());
    }


    @Test
    public void toXml_getInteger() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<root name=\"test\">" +
                "\t<demo id=\"1\">123</demo>" +
                "</root>";

        XmlObject xmlObject = XML.parseToObject(xml);

        Assert.assertEquals(new Integer(1), xmlObject.getChild(0).getInteger("id"));
    }
}