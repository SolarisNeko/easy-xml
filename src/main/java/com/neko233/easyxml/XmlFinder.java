package com.neko233.easyxml;

import com.neko233.easyxml.data.XmlObject;
import com.neko233.easyxml.utils.EasyXmlStringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * {@link javax.xml.xpath.XPath} this is xpath simple like utils
 * Search & Match stream in XML DOM.
 * Use like Xpath
 * <p>
 * 检索 & 匹配 XML
 * <p>
 *
 * @author SolarisNeko
 * Date on 2023-02-19
 */
public class XmlFinder {

    public static List<XmlObject> find(XmlObject rootObject, String targetPath) {
        if (EasyXmlStringUtils.isBlank(targetPath)) {
            return null;
        }
        if (rootObject == null) {
            return null;
        }

        List<XmlObject> byPath = findByPath(rootObject, targetPath.trim(), 1);
        return Optional.ofNullable(byPath)
                .orElse(new ArrayList<>())
                .stream()
                .filter(getRegexXmlFindPath(targetPath))
                .collect(Collectors.toList());
    }

    public static List<XmlObject> find(String xml, String path) {
        XmlObject xmlObject = XML.parseToObject(xml);
        return find(xmlObject, path);
    }

    private static List<XmlObject> findByPath(XmlObject xmlObject, String path, int startSearchIndex) {
        if ("/".equals(path)) {
            return Collections.singletonList(xmlObject);
        }


        if (startSearchIndex >= path.length()) {
            return null;
        }
        int nextStartSearchIndex = path.indexOf("/", startSearchIndex);
        if (nextStartSearchIndex < 0) {
            return getFileteredNodesInThisNode(xmlObject, path);
        }

        final String subPath = path.substring(0, nextStartSearchIndex);

        final List<XmlObject> targetDomList = getFileteredNodesInThisNode(xmlObject, subPath);

        final List<XmlObject> objects = new ArrayList<>(targetDomList);
        for (XmlObject childDomNode : targetDomList) {
            List<XmlObject> byPath = findByPath(childDomNode, path, nextStartSearchIndex + 1);
            if (byPath == null) {
                continue;
            }
            objects.addAll(byPath);
        }
        return objects;
    }


    @NotNull
    private static List<XmlObject> getFileteredNodesInThisNode(XmlObject xmlObject, String targetPath) {
        List<XmlObject> xmlObjects = Optional.ofNullable(xmlObject.getChildren())
                .orElse(new ArrayList<>());
        return xmlObjects
                .stream()
                .filter(getRegexXmlFindPath(targetPath))
                .collect(Collectors.toList());
    }

    @NotNull
    private static Predicate<XmlObject> getRegexXmlFindPath(String targetPath) {
        return node -> node.getXmlPath().matches(targetPath.replaceAll("\\*", "\\.\\*"));
    }


}
