# Easy-XML
XML ORM Util

Use like FastJSON so easy

=w= 使用 XML 就和 JSON 一样～

ps: XML features you must use your Object to parse, can't not use Map Tree 


# Dependency
## maven
```xml
<dependency>
   <groupId>com.neko233</groupId>
   <artifactId>easy-xml</artifactId>
   <version>1.0.0</version>
</dependency>
```

## Gradle
```kotlin
implementation("com.neko233:easy-xml:1.0.0")
```

# Use
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