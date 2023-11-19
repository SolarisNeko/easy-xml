package com.neko233.easyxml;

import com.neko233.easyxml.data.XmlObject;
import com.neko233.easyxml.exception.EasyXmlException;
import com.neko233.easyxml.utils.ReadXmlUtils;
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
    static <T> T parseToObject(String xml, Class<T> tClass) throws EasyXmlException {
        try {
            StringReader reader = new StringReader(xml);
            JAXBContext jaxbContext = JAXBContext.newInstance(tClass);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (T) jaxbUnmarshaller.unmarshal(reader);
        } catch (Throwable e) {
            throw new EasyXmlException("[EasyXml] xml -> object error", e);
        }
    }

    /**
     * 自动转换成 XmlObject, 当没有给定类型的时候
     *
     * @param xml xml 对象
     * @return XmlObject
     */
    static XmlObject parseToObject(String xml) {
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
        final XmlObject domMap = new XmlObject(root.getNodeName(), nodeValue, null);
        initNodeTree(root, domMap);
        return domMap;
    }


    static <T> T parseToObject(InputStream input, Class<T> xmlType) throws IOException, EasyXmlException {
        return parseToObject(input, StandardCharsets.UTF_8, xmlType);
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
    static <T> T parseToObject(InputStream input, Charset charset, Class<T> xmlType) throws IOException, EasyXmlException {
        if (input == null) {
            return null;
        }

        String xml = ReadXmlUtils.readXmlFromInputStream(input, charset);
        return parseToObject(xml, xmlType);
    }

    /**
     * Parse UTF8 encoded XML to object
     *
     * @param url the XML {@link URL} to be parsed
     * @return object
     */
    static <T> T parseToObject(URL url, Class<T> xmlType) throws IOException, EasyXmlException {
        return parseToObject(url, StandardCharsets.UTF_8, xmlType);
    }

    static <T> T parseToObject(URL url, Charset charset, Class<T> xmlType) throws IOException, EasyXmlException {
        if (url == null) {
            return null;
        }

        InputStream input = url.openStream();
        String xml = ReadXmlUtils.readXmlFromInputStream(input, charset);

        return parseToObject(xml, xmlType);
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

            // 设置属性，包括 XML 头部的信息
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            // 设置为 true 会省略 XML 头部
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);

            // 执行转换
            marshaller.marshal(obj, writer);

            return writer.toString();
        } catch (Throwable e) {
            throw new EasyXmlException("[EasyXml] object -> XML error", e);
        }
    }

    /**
     * 将通用的 XmlObject 转为 XML 文本
     *
     * @param xmlObject 通用对象
     * @return XML
     * @throws EasyXmlException
     */
    static String toXmlString(XmlObject xmlObject) throws EasyXmlException {
        return xmlObject.toXmlString();
    }


    /**
     * XML Node Tree Liner 线性化
     *
     * @param xmlObject dom object
     * @return liner Tree to List
     */
    static List<XmlObject> liner(XmlObject xmlObject) {
        final List<XmlObject> rootChildNodes = Optional.ofNullable(xmlObject.getChildrenNodes())
                .orElse(new ArrayList<>());
        final List<XmlObject> targetList = new ArrayList<>(rootChildNodes);
        for (final XmlObject childNode : rootChildNodes) {
            final List<XmlObject> liner = liner(childNode);
            targetList.addAll(liner);
        }
        return targetList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
