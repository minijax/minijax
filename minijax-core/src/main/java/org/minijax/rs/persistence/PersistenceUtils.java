package org.minijax.rs.persistence;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * The PersistenceUtils class provides helper methods for parsing a persistence.xml file.
 */
class PersistenceUtils {
    private static final Logger LOG = LoggerFactory.getLogger(PersistenceUtils.class);

    PersistenceUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a list of persistence unit names.
     *
     * @param fileName The resource file name to scan.
     * @return A list of persistence unit names.
     */
    public static List<String> getNames(final String fileName) {
        try (final InputStream in = PersistenceUtils.class.getClassLoader().getResourceAsStream(fileName)) {
            if (in == null) {
                return Collections.emptyList();
            }
            return scanPersistenceXml(in);
        } catch (final Exception ex) {
            LOG.warn("Error reading persistence.xml: {}", ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

    private static List<String> scanPersistenceXml(final InputStream in)
            throws Exception { // NOSONAR
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
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
