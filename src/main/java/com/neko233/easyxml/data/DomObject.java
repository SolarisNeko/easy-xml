package com.neko233.easyxml.data;

import java.util.*;

/**
 * @author SolarisNeko
 * Date on 2023-02-02
 */
public class DomObject {

    private final String rootName;
    private final String nodeValue;
    private final Map<String, String> attributes;

    private final DomObject parentNode;
    private DomObject leftNode;
    private DomObject rightNode;
    private final List<DomObject> childrenNodes;

    public DomObject(String rootName, String nodeValue, DomObject parentNode) {
        this.nodeValue = Optional.ofNullable(nodeValue).orElse("").trim();
        this.rootName = rootName;
        this.attributes = new HashMap<>(0);
        this.parentNode = parentNode;
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


    public DomObject left() {
        return this.leftNode;
    }
    public DomObject right() {
        return this.rightNode;
    }

    // --------- @Data ----


    public String getNodeValue() {
        return nodeValue;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public DomObject getParentNode() {
        return parentNode;
    }

    public DomObject getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(DomObject leftNode) {
        this.leftNode = leftNode;
    }

    public DomObject getRightNode() {
        return rightNode;
    }

    public void setRightNode(DomObject rightNode) {
        this.rightNode = rightNode;
    }

    public List<DomObject> getChildrenNodes() {
        return childrenNodes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomObject)) return false;

        DomObject domObject = (DomObject) o;

        if (getRootName() != null ? !getRootName().equals(domObject.getRootName()) : domObject.getRootName() != null)
            return false;
        if (getNodeValue() != null ? !getNodeValue().equals(domObject.getNodeValue()) : domObject.getNodeValue() != null)
            return false;
        if (getAttributes() != null ? !getAttributes().equals(domObject.getAttributes()) : domObject.getAttributes() != null)
            return false;
        if (getParentNode() != null ? !getParentNode().equals(domObject.getParentNode()) : domObject.getParentNode() != null)
            return false;
        return getChildrenNodes() != null ? getChildrenNodes().equals(domObject.getChildrenNodes()) : domObject.getChildrenNodes() == null;
    }

    @Override
    public int hashCode() {
        int result = getRootName() != null ? getRootName().hashCode() : 0;
        result = 31 * result + (getNodeValue() != null ? getNodeValue().hashCode() : 0);
        result = 31 * result + (getAttributes() != null ? getAttributes().hashCode() : 0);
        result = 31 * result + (getParentNode() != null ? getParentNode().hashCode() : 0);
        result = 31 * result + (getChildrenNodes() != null ? getChildrenNodes().hashCode() : 0);
        return result;
    }
}
