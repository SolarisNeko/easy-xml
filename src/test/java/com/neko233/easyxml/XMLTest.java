package com.neko233.easyxml;


import com.neko233.easyxml.data.AllXml;
import com.neko233.easyxml.data.Demo;
import com.neko233.easyxml.data.DomObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author SolarisNeko
 * Date on 2023-01-01
 */
public class XMLTest {

    @Test
    public void toObject_original() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<root name=\"test\">" +
                "\t<demo id=\"1\"/>" +
                "</root>";

        DomObject domObject = XML.toObject(xml);

        Assert.assertEquals("root", domObject.getRootName());
        Assert.assertEquals("test", domObject.getAttribute("name"));
        Assert.assertEquals("1", domObject.getChild(0).getAttribute("id"));
    }

    @Test
    public void toObject_brother() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<root name=\"test\">" +
                "\t<demo id=\"1\"/>" +
                "\t<demo id=\"2\">123</demo>" +
                "\t<demo id=\"3\"/>" +
                "</root>";

        DomObject domObject = XML.toObject(xml);

        DomObject dom2 = domObject.getChild(1);
        DomObject dom3 = domObject.getChild(2);
        Assert.assertEquals("2", dom2.getAttribute("id"));
        Assert.assertEquals("123", dom2.getNodeValue());
        Assert.assertEquals("1", dom2.left().getAttribute("id"));
        Assert.assertEquals(dom3, dom2.right());
    }

    @Test
    public void toObject_demo() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><demo rootId=\"1\"/>";

        Demo demo = null;
        try {
            demo = XML.toObject(xml, Demo.class);
        } catch (EasyXmlException e) {
            throw new RuntimeException(e);
        }

        Assert.assertEquals(1, (int) demo.getRootId());
    }

    @Test
    public void toObject() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root>\n" +
                "<demo rootId=\"123\" >\n" +
                "</demo>\n" +
                "<cityRoot rootId=\"0\">\n" +
                "   <any></any>" +
                "  <city cityId=\"101280101\" isMainLand=\"true\" d2=\"&lt;广州&gt;\" d3=\"guangzhou\" d4=\"广东\"/>\n" +
                "  <city cityId=\"101280102\" isMainLand=\"true\" d2=\"番禺\" d3=\"panyu\" d4=\"广东\"/>\n" +
                "  <city cityId=\"101280103\" isMainLand=\"false\" d2=\"&quot;USA\" d3=\"conghua\" d4=\"广东\"/>\n" +
                "</cityRoot>\n" +
                "</root>\n";

        AllXml allXml = null;
        try {
            allXml = XML.toObject(xml, AllXml.class);
        } catch (EasyXmlException e) {
            throw new RuntimeException(e);
        }

        Assert.assertNotNull(allXml);
    }

    @Test
    public void toXml() throws EasyXmlException {
        String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><root><demo rootId=\"1\"/></root>";

        AllXml allXml = AllXml.builder()
                .demo(new Demo(1))
                .build();
        String s = XML.toXmlString(allXml);
        Assert.assertEquals(targetXml, s);
    }

    @Test
    public void toXml_demo() throws EasyXmlException {
        String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><demo rootId=\"1\"/>";

        Demo demo = new Demo(1);
        String s = XML.toXmlString(demo);
        Assert.assertEquals(targetXml, s);
    }
}