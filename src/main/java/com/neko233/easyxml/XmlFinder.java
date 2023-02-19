package com.neko233.easyxml;

import com.neko233.easyxml.data.DomObject;
import com.neko233.easyxml.utils.EasyXmlStringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
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

    public static List<DomObject> find(DomObject rootObject, String targetPath) {
        if (EasyXmlStringUtils.isBlank(targetPath)) {
            return null;
        }
        if (rootObject == null) {
            return null;
        }

        List<DomObject> byPath = findByPath(rootObject, targetPath.trim(), 1);
        return Optional.ofNullable(byPath)
                .orElse(new ArrayList<>())
                .stream()
                .filter(getRegexXmlFindPath(targetPath))
                .collect(Collectors.toList());
    }

    public static List<DomObject> find(String xml, String path) {
        DomObject domObject = XML.toObject(xml);
        return find(domObject, path);
    }

    private static List<DomObject> findByPath(DomObject domObject, String path, int startSearchIndex) {
        if ("/".equals(path)) {
            return Collections.singletonList(domObject);
        }


        if (startSearchIndex >= path.length()) {
            return null;
        }
        int nextStartSearchIndex = path.indexOf("/", startSearchIndex);
        if (nextStartSearchIndex < 0) {
            return getFileteredNodesInThisNode(domObject, path);
        }

        final String subPath = path.substring(0, nextStartSearchIndex);

        final List<DomObject> targetDomList = getFileteredNodesInThisNode(domObject, subPath);

        final List<DomObject> objects = new ArrayList<>(targetDomList);
        for (DomObject childDomNode : targetDomList) {
            List<DomObject> byPath = findByPath(childDomNode, path, nextStartSearchIndex + 1);
            if (byPath == null) {
                continue;
            }
            objects.addAll(byPath);
        }
        return objects;
    }


    @NotNull
    private static List<DomObject> getFileteredNodesInThisNode(DomObject domObject, String targetPath) {
        List<DomObject> domObjects = Optional.ofNullable(domObject.getChildren())
                .orElse(new ArrayList<>());
        return domObjects
                .stream()
                .filter(getRegexXmlFindPath(targetPath))
                .collect(Collectors.toList());
    }

    @NotNull
    private static Predicate<DomObject> getRegexXmlFindPath(String targetPath) {
        return node -> node.getXmlPath().matches(targetPath.replaceAll("\\*", "\\.\\*"));
    }


}
