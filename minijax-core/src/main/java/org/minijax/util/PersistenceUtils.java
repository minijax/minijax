package org.minijax.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class PersistenceUtils {
    private static final Logger LOG = LoggerFactory.getLogger(PersistenceUtils.class);


    public static List<String> getDefaultName(final String fileName) {
        try (final InputStream in = PersistenceUtils.class.getClassLoader().getResourceAsStream(fileName)) {
            if (in == null) {
                return Collections.emptyList();
            }
            return scanPersistenceXml(in);
        } catch (final Exception ex) {
            LOG.error("Error reading persistence.xml: {}", ex.getMessage(), ex);
            return null;
        }
    }


    protected static List<String> scanPersistenceXml(final InputStream in) throws Exception {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final Document doc = factory.newDocumentBuilder().parse(in);
        final XPathExpression expr = XPathFactory.newInstance().newXPath().compile("/persistence/persistence-unit/@name");
        final NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
        final List<String> result = new ArrayList<>(nodes.getLength());
        for (int i = 0; i < nodes.getLength(); i++) {
            result.add(((Attr) nodes.item(i)).getValue());
        }
        return result;
    }
}
