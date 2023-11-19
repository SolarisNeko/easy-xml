# Easy-XML


XML ORM Util, Use like `FastJSON` 

ps: XML features you must use your Object to parse, can't not use Map Tree 

# JDK Support
| JDK | isOk |
|:----|-----:|
| 8   | ok |
| 11  | ok |
| 17  | ok |

since from v1.2.0

# Dependency
## maven
```xml
<dependency>
   <groupId>com.neko233</groupId>
   <artifactId>easy-xml</artifactId>
   <version>1.6.0</version>
</dependency>
```

## Gradle
```kotlin
implementation("com.neko233:easy-xml:1.6.0")
```

# Code

## XmlObject | 通用的 XML Node 操作对象 
Like JSONObject to use

像 JSON 一样使用 XmlObject

```java

@Test
public void toObject_brother() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
        "<root name=\"test\">" +
        "\t<demo id=\"1\"/>" +
        "\t<demo id=\"2\">123</demo>" +
        "\t<demo id=\"3\"/>" +
        "</root>";

        XmlObject xmlObject = XML.toObject(xml);

        // batch get all children
//        List<XmlObject> children = xmlObject.getChildren();

        // get child 2 and child 3 | but index is -1
        XmlObject dom2 = xmlObject.getChild(1);
        XmlObject dom3 = xmlObject.getChild(2);

        Assert.assertEquals("2", dom2.getAttribute("id"));
        Assert.assertEquals("123", dom2.getNodeValue());
        Assert.assertEquals("1", dom2.left().getAttribute("id"));
        Assert.assertEquals(dom3, dom2.right());
}
```

## XML ORM = String <-> Object

### ORM Class Define 定义你的对象
```java

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "demo") // your root name | <root ...>
@XmlAccessorType(XmlAccessType.FIELD) // how to set value
public class Demo {

    @XmlAttribute(name = "rootId") // attributeName | <root rootId="1" ..>
    private Integer rootId;

//    @XmlRootElement(name = "root")
//<root ... />

    
    // get / set
}

```

### object to XML | 对象 -> XML
```java
String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><demo rootId=\"1\"/>";
        
// to XMl
 Demo demo = new Demo(1);
 String s = XML.toXmlString(demo);
 Assert.assertEquals(targetXml, s);
```

### XML to object | XML -> 对象 
```java
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
```

## XmlFiner (DOM Tree 查询语法)

一个简单的检索语法

```java

    @Test
    public void test_find_3_byRegex() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<root name=\"test\">" +
                "\t<demo id=\"1\">" +
                "\t\t<node3rd id=\"1-1\"/>" +
                "\t</demo>" +
                "\t<demo id=\"2\"/>" +
                "</root>";

        DomObject xmlObject = XML.toObject(xml);

        List<DomObject> xmlObjects = XmlFinder.find(xmlObject, "/demo/node3*");
        assert xmlObjects != null;
        String id = xmlObjects.get(0).getAttribute("id");
        Assert.assertEquals("1-1", id);
    }
```

# License
Easy-XML is released under the Apache 2.0 license.


# Contact Us

EMail: 1417015340@qq.com




