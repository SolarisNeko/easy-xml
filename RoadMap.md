# RoadMap - EasyXml

## 1.6.0
1. [new] 接入了 XPath
2. [new] XmlObject 和 w3c Document/Node 做了无缝衔接 
3. [add] XPath by `avax.xml.parsers:jaxp-api`
4. [new] XPath233 utils


## 1.5.0
1. [Add] XML kv API


## 1.4.0
1. [Update] DomObject -> XmlObject
2. [Refactor] 调整 API. Tree Operate 脱离 get/set, 以类似 get parentNode() / set parentNode(XmlObject) 命名
3. [Add] 提供修改 (局部) XmlObject 后, 重新 serialize to XML String 机制. 

## v1.3.1
1. [Add] DomObject.toXML() : String

## 1.3.0
1. [Add] [XmlFinder] query XML object in path language. use like 'Xpath' but simple more.
    查询 XML DOM 对象, 基于 path 语法, 例如 /dom/to/what* 即可获取3级dom对象. 
2. [Add] liner DomObject. XML 处理方式追加

## v1.2.0
deploy v1.2.0
1. [support] JDK-8~17
2. [Add] DomObject Tree support full.
3. [Add] my dependency jar, no dependency jdk.
## v1.1.0
support DomObject base

## v1.0.0
first init project