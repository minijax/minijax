package org.minijax.commons;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlUtilsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void testCtor() {
        new XmlUtils();
    }

    @Test
    public void testReadXml() throws IOException {
        final Document doc = XmlUtils.readXml(new File("src/test/resources/note.xml"));
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testReadBadXml() throws IOException {
        XmlUtils.readXml(new File("src/test/resources/bad.xml.txt"));
    }

    @Test
    public void testWriteXml() throws Exception {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document doc = builder.newDocument();
        final Element rootElement = doc.createElement("test");
        doc.appendChild(rootElement);

        final File file = File.createTempFile("tmp", "xml");

        XmlUtils.writeXml(doc, file);

        assertTrue(file.exists());
        assertTrue(file.delete());
    }
}
