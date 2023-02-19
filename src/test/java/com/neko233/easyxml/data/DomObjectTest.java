package com.neko233.easyxml.data;

import com.neko233.easyxml.XML;
import org.junit.Assert;
import org.junit.Test;

public class DomObjectTest  {


    @Test
    public void toXml_demo() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<root name=\"test\">" +
                "\t<demo id=\"1\">123</demo>" +
                "</root>";

        DomObject domObject = XML.toObject(xml);
        String target = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root name=\"test\"><demo id=\"1\">123</demo></root>";
        Assert.assertEquals(target, domObject.toXML());
    }
}