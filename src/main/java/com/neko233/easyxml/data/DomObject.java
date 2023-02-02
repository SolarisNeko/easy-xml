package com.neko233.easyxml.data;

import com.neko233.easyxml.EasyXmlException;
import com.neko233.easyxml.XML;

import java.util.*;

/**
 * @author SolarisNeko
 * Date on 2023-02-02
 */
public class DomObject {

    private final String rootName;
    private final Map<String, String> attributes;
    private final List<DomObject> childrenNodes;

    public DomObject(String rootName) {
        this.rootName = rootName;
        this.attributes = new HashMap<>(0);
        this.childrenNodes = new ArrayList<>(0);
    }

    public String getRootName() {
        return rootName;
    }

    public String getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }

    public void addAttribute(String attributeName, String attributeValue) {
        attributes.put(attributeName, attributeValue);
    }


    public void removeAttribute(String attributeName) {
        attributes.remove(attributeName);
    }

    public DomObject getChild(int index) {
        return childrenNodes.get(index);
    }

    public List<DomObject> getChildren() {
        return childrenNodes;
    }

    public void addChild(DomObject childNode) {
        childrenNodes.add(childNode);
    }


    public void removeChild(DomObject childNode) {
        attributes.remove(childNode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DomObject domObject = (DomObject) o;

        if (!Objects.equals(attributes, domObject.attributes)) return false;
        return Objects.equals(childrenNodes, domObject.childrenNodes);
    }

    @Override
    public int hashCode() {
        int result = attributes != null ? attributes.hashCode() : 0;
        result = 31 * result + (childrenNodes != null ? childrenNodes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DomObject{" +
                "rootName='" + rootName + '\'' +
                ", attributes=" + attributes +
                ", childrenNodes=" + childrenNodes +
                '}';
    }

    public String toXml() throws EasyXmlException {
        return XML.toXmlString(this);
    }
}
