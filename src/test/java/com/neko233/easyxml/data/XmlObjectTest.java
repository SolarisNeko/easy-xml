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

        XmlObject xmlObject = XML.toObject(xml);
        String target = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root name=\"test\"><demo id=\"1\">123</demo></root>";
        Assert.assertEquals(target, xmlObject.toXML());
    }


    @Test
    public void toXml_if_modify_rootName() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<root name=\"test\">" +
                "\t<demo id=\"1\">123</demo>" +
                "</root>";

        XmlObject xmlObject = XML.toObject(xml);
        xmlObject.rootName("newRoot");

        String target = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><newRoot name=\"test\"><demo id=\"1\">123</demo></newRoot>";
        Assert.assertEquals(target, xmlObject.toXML());
        Assert.assertEquals("/", xmlObject.getXmlPath());
    }


    @Test
    public void toXml_if_modify_rootName_child_1() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<root name=\"test\">" +
                "\t<demo id=\"1\">123</demo>" +
                "</root>";

        XmlObject xmlObject = XML.toObject(xml);
        xmlObject.rootName("newRoot");

        XmlObject demo = xmlObject.getChild(0);
        demo.rootName("newDemo");

        Assert.assertEquals("/newDemo", demo.getXmlPath());
    }
}