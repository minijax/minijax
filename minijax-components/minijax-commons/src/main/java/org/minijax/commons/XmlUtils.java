package org.minijax.commons;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * The XmlUtils class provides helper methods for reading and writing XML.
 *
 * XML files are notorious for security issues, configuration issues, etc.
 *
 * Try to use these methods for all XML behavior.
 */
public class XmlUtils {
    private static final String XML_FACTORY = "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl";

    XmlUtils() {
        throw new UnsupportedOperationException();
    }

    public static Document readXml(final File file) throws IOException {
        try (final InputStream inputStream = new FileInputStream(file)) {
            return readXml(inputStream);
        }
    }

    public static Document readXml(final InputStream inputStream) throws IOException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            return factory.newDocumentBuilder().parse(inputStream);
        } catch (ParserConfigurationException | SAXException ex) {
            throw new IOException(ex.getMessage(), ex);
        }
    }

    public static void writeXml(final Document doc, final File file) throws IOException {
        doc.normalize();

        final TransformerFactory transformerFactory = TransformerFactory.newInstance(XML_FACTORY, null);

        try {
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

            final Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(new DOMSource(doc), new StreamResult(file));
        } catch (final TransformerException ex) {
            throw new IOException(ex.getMessage(), ex);
        }
    }
}
