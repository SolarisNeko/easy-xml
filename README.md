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
   <version>1.3.1</version>
</dependency>
```

## Gradle
```kotlin
implementation("com.neko233:easy-xml:1.3.1")
```

# Use
## Annotation
```java
@XmlRootElement(name = "root")
//<root ... />

@XmlAttribute(name = "rootId")
//<root rootId="???" />

@XmlAccessorType(XmlAccessType.FIELD)
//how to create your field value

```
## Code
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

    
    // get / set
}

```

object to XML
```java
String targetXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><demo rootId=\"1\"/>";
        
// to XMl
 Demo demo = new Demo(1);
 String s = XML.toXmlString(demo);
 Assert.assertEquals(targetXml, s);
```

XML to object 
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
简易的爬虫向 DSL 语法. 
复杂的可以 DOM Object -> XML -> Xpath.
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

        DomObject domObject = XML.toObject(xml);

        List<DomObject> domObjects = XmlFinder.find(domObject, "/demo/node3*");
        assert domObjects != null;
        String id = domObjects.get(0).getAttribute("id");
        Assert.assertEquals("1-1", id);
    }
```

# License
Easy-XML is released under the Apache 2.0 license.


# Contact Us

EMail: 1417015340@qq.com




