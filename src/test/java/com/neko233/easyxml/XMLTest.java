package com.neko233.easyxml;


import com.neko233.easyxml.dto.AllXml;
import com.neko233.easyxml.dto.Demo;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author SolarisNeko
 * Date on 2023-01-01
 */
public class XMLTest {

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