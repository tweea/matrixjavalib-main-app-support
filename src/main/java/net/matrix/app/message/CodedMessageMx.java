/*
 * 版权所有 2024 Matrix。
 * 保留所有权利。
 */
package net.matrix.app.message;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 编码消息工具。
 */
public final class CodedMessageMx {
    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

    private static final TransformerFactory TRANSFORMER_FACTORY = TransformerFactory.newInstance();

    /**
     * 阻止实例化。
     */
    private CodedMessageMx() {
    }

    /**
     * 创建跟踪级别的消息。
     * 
     * @param code
     *     编码。
     * @param arguments
     *     参数列表。
     * @return 创建的消息。
     */
    public static CodedMessage trace(String code, String... arguments) {
        return new CodedMessage(code, CodedMessageLevel.TRACE, arguments);
    }

    /**
     * 创建调试级别的消息。
     * 
     * @param code
     *     编码。
     * @param arguments
     *     参数列表。
     * @return 创建的消息。
     */
    public static CodedMessage debug(String code, String... arguments) {
        return new CodedMessage(code, CodedMessageLevel.DEBUG, arguments);
    }

    /**
     * 创建信息级别的消息。
     * 
     * @param code
     *     编码。
     * @param arguments
     *     参数列表。
     * @return 创建的消息。
     */
    public static CodedMessage info(String code, String... arguments) {
        return new CodedMessage(code, CodedMessageLevel.INFO, arguments);
    }

    /**
     * 创建警告级别的消息。
     * 
     * @param code
     *     编码。
     * @param arguments
     *     参数列表。
     * @return 创建的消息。
     */
    public static CodedMessage warn(String code, String... arguments) {
        return new CodedMessage(code, CodedMessageLevel.WARN, arguments);
    }

    /**
     * 创建错误级别的消息。
     * 
     * @param code
     *     编码。
     * @param arguments
     *     参数列表。
     * @return 创建的消息。
     */
    public static CodedMessage error(String code, String... arguments) {
        return new CodedMessage(code, CodedMessageLevel.ERROR, arguments);
    }

    /**
     * 创建致命错误级别的消息。
     * 
     * @param code
     *     编码。
     * @param arguments
     *     参数列表。
     * @return 创建的消息。
     */
    public static CodedMessage fatal(String code, String... arguments) {
        return new CodedMessage(code, CodedMessageLevel.FATAL, arguments);
    }

    /**
     * 从文件加载消息。
     * 
     * @param reader
     *     输入流。
     * @return 消息列表。
     * @throws CodedMessageException
     *     加载失败。
     */
    public static List<CodedMessage> load(InputStream reader)
        throws CodedMessageException {
        Document document = load(new StreamSource(reader));
        return load(document.getDocumentElement());
    }

    /**
     * 从文件加载消息。
     * 
     * @param reader
     *     输入流。
     * @return 消息列表。
     * @throws CodedMessageException
     *     加载失败。
     */
    public static List<CodedMessage> load(Reader reader)
        throws CodedMessageException {
        Document document = load(new StreamSource(reader));
        return load(document.getDocumentElement());
    }

    private static Document load(Source source)
        throws CodedMessageException {
        try {
            Transformer transformer = TRANSFORMER_FACTORY.newTransformer();
            DOMResult result = new DOMResult();
            transformer.transform(source, result);
            return (Document) result.getNode();
        } catch (TransformerException e) {
            throw new CodedMessageException(e, "CodedMessage.LoadXMLError");
        }
    }

    private static List<CodedMessage> load(Node node) {
        List<CodedMessage> messageList = new ArrayList<>();

        NodeList childNodeList = node.getChildNodes();
        for (int i = 0; i < childNodeList.getLength(); i++) {
            Node childNode = childNodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if ("message".equals(childNode.getNodeName())) {
                messageList.add(loadMessage(childNode));
            }
        }

        return messageList;
    }

    private static CodedMessage loadMessage(Node node) {
        NamedNodeMap nodeAttributes = node.getAttributes();
        String code = nodeAttributes.getNamedItem("code").getNodeValue();
        long time = Long.parseLong(nodeAttributes.getNamedItem("time").getNodeValue());
        CodedMessageLevel level = CodedMessageLevel.forCode(Integer.valueOf(nodeAttributes.getNamedItem("level").getNodeValue()));
        CodedMessage message = new CodedMessage(code, time, level);
        NodeList childNodeList = node.getChildNodes();
        for (int i = 0; i < childNodeList.getLength(); i++) {
            Node childNode = childNodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if ("argument".equals(childNode.getNodeName())) {
                message.addArgument(childNode.getTextContent().trim());
            } else if ("unformattedArgument".equals(childNode.getNodeName())) {
                message.addUnformattedArgument(childNode.getTextContent().trim());
            } else if ("message".equals(childNode.getNodeName())) {
                message.addMessage(loadMessage(childNode));
            }
        }
        return message;
    }

    /**
     * 保存消息到文件。
     * 
     * @param messageList
     *     消息列表。
     * @param writer
     *     输出流。
     * @throws CodedMessageException
     *     保存失败。
     */
    public static void save(List<CodedMessage> messageList, OutputStream writer)
        throws CodedMessageException {
        Document document = save(messageList);
        save(document, new StreamResult(writer));
    }

    /**
     * 保存消息到文件。
     * 
     * @param messageList
     *     消息列表。
     * @param writer
     *     输出流。
     * @throws CodedMessageException
     *     保存失败。
     */
    public static void save(List<CodedMessage> messageList, Writer writer)
        throws CodedMessageException {
        Document document = save(messageList);
        save(document, new StreamResult(writer));
    }

    private static Document save(List<CodedMessage> messageList)
        throws CodedMessageException {
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new CodedMessageException(e, "CodedMessage.SaveXMLError");
        }

        Document document = documentBuilder.newDocument();
        Element messagesElement = document.createElement("messages");
        document.appendChild(messagesElement);
        for (CodedMessage message : messageList) {
            messagesElement.appendChild(saveMessage(message, document));
        }
        return document;
    }

    private static Element saveMessage(CodedMessage message, Document document) {
        Element messageElement = document.createElement("message");
        messageElement.setAttribute("code", message.getCode());
        messageElement.setAttribute("time", Long.toString(message.getTime()));
        messageElement.setAttribute("level", Integer.toString(message.getLevel().code));
        for (String argument : message.getArguments()) {
            Element argumentElement = document.createElement("argument");
            messageElement.appendChild(argumentElement);
            argumentElement.setTextContent(argument);
        }
        for (String unformattedArgument : message.getUnformattedArguments()) {
            Element unformattedArgumentElement = document.createElement("unformattedArgument");
            messageElement.appendChild(unformattedArgumentElement);
            unformattedArgumentElement.setTextContent(unformattedArgument);
        }
        for (CodedMessage childMessage : message.getMessages()) {
            messageElement.appendChild(saveMessage(childMessage, document));
        }
        return messageElement;
    }

    private static void save(Document document, Result result)
        throws CodedMessageException {
        try {
            Transformer transformer = TRANSFORMER_FACTORY.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), result);
        } catch (TransformerException e) {
            throw new CodedMessageException(e, "CodedMessage.SaveXMLError");
        }
    }
}
