package com.neko233.easyxml;

import com.neko233.easyxml.data.DomObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.neko233.easyxml.data.XmlTree.initNodeTree;

/**
 * @author SolarisNeko on 2023-01-01
 **/
public interface XML {

    /**
     * xml转对象
     *
     * @param xml xml Text
     * @return object
     */
    static <T> T toObject(String xml, Class<T> tClass) throws EasyXmlException {
        try {
            StringReader reader = new StringReader(xml);
            JAXBContext jaxbContext = JAXBContext.newInstance(tClass);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (T) jaxbUnmarshaller.unmarshal(reader);
        } catch (Throwable e) {
            throw new EasyXmlException("[EasyXml] xml -> object error", e);
        }
    }

    static DomObject toObject(String xml) {
        Document document = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        StringReader characterStream = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            characterStream = new StringReader(xml);
            InputSource is = new InputSource(characterStream);
            document = db.parse(is);
            characterStream.close();
        } catch (Exception e) {
            throw new RuntimeException("[XmlUtil] can not parse xml content", e);
        } finally {
            if (characterStream != null) {
                characterStream.close();
            }
        }
        final Node root = document.getDocumentElement();
        String nodeValue = root.getNodeValue();
        final DomObject domMap = new DomObject(root.getNodeName(), nodeValue, null);
        initNodeTree(root, domMap);
        return domMap;
    }


    /**
     * 将对象转为流程XML
     *
     * @param obj 对象
     * @return xml Text
     * @throws EasyXmlException any parse error
     */
    static <T> String toXmlString(T obj) throws EasyXmlException {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(obj.getClass());
            StringWriter writer = new StringWriter();
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(obj, writer);
            return writer.toString();
        } catch (Throwable e) {
            throw new EasyXmlException("[EasyXml] object -> XML error", e);
        }
    }


    static <T> T toObject(InputStream input, Class<T> xmlType) throws IOException, EasyXmlException {
        return toObject(input, StandardCharsets.UTF_8, xmlType);
    }

    /**
     * @param input   输入流
     * @param charset 编码
     * @param xmlType xml Class
     * @param <T>     范型
     * @return object
     * @throws IOException      I/O error
     * @throws EasyXmlException parse XML error
     */
    static <T> T toObject(InputStream input, Charset charset, Class<T> xmlType) throws IOException, EasyXmlException {
        if (input == null) {
            return null;
        }

        String xml = ReadXmlUtils.readXmlFromInputStream(input, charset);
        return toObject(xml, xmlType);
    }

    /**
     * Parse UTF8 encoded XML to object
     *
     * @param url the XML {@link URL} to be parsed
     * @return object
     */
    static <T> T toObject(URL url, Class<T> xmlType) throws IOException, EasyXmlException {
        return toObject(url, StandardCharsets.UTF_8, xmlType);
    }

    static <T> T toObject(URL url, Charset charset, Class<T> xmlType) throws IOException, EasyXmlException {
        if (url == null) {
            return null;
        }

        InputStream input = url.openStream();
        String xml = ReadXmlUtils.readXmlFromInputStream(input, charset);

        return toObject(xml, xmlType);
    }


    /**
     * XML Node Tree Liner 线性化
     *
     * @param domObject dom object
     * @return liner Tree to List
     */
    static List<DomObject> liner(DomObject domObject) {
        final List<DomObject> rootChildNodes = Optional.ofNullable(domObject.getChildrenNodes())
                .orElse(new ArrayList<>());
        final List<DomObject> targetList = new ArrayList<>(rootChildNodes);
        for (final DomObject childNode : rootChildNodes) {
            final List<DomObject> liner = liner(childNode);
            targetList.addAll(liner);
        }
        return targetList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
