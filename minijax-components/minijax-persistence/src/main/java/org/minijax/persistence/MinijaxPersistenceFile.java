package org.minijax.persistence;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;

import org.minijax.commons.MinijaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MinijaxPersistenceFile {
    private static final Logger LOG = LoggerFactory.getLogger(MinijaxPersistenceFile.class);
    private final Map<String, MinijaxPersistenceUnitInfo> persistenceUnits;

    private MinijaxPersistenceFile(final Map<String, MinijaxPersistenceUnitInfo> persistenceUnits) {
        this.persistenceUnits = persistenceUnits;
    }

    public MinijaxPersistenceUnitInfo getPersistenceUnit(final String name) {
        return persistenceUnits.get(name);
    }

    /**
     * Returns a list of persistence unit names.
     *
     * @param fileName The resource file name to scan.
     * @return A list of persistence unit names.
     */
    public static MinijaxPersistenceFile read(final String fileName) {
        try (final InputStream in = MinijaxPersistenceFile.class.getClassLoader().getResourceAsStream(fileName)) {
            if (in == null) {
                throw new MinijaxException("Persistence unit info not found (\"" + fileName + "\")");
            }

            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            final Document doc = factory.newDocumentBuilder().parse(in);

            // https://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            return parse(doc);

        } catch (final Exception ex) {
            LOG.error("Error reading persistence unit info: {}", ex.getMessage(), ex);
            throw new MinijaxException(ex.getMessage(), ex);
        }
    }

    private static MinijaxPersistenceFile parse(final Document doc) {
        final Map<String, MinijaxPersistenceUnitInfo> persistenceUnits = new HashMap<>();

        final NodeList nodeList = doc.getElementsByTagName("persistence-unit");
        for (int i = 0; i < nodeList.getLength(); i++) {
            final MinijaxPersistenceUnitInfo unit = parseUnit(nodeList.item(i));
            persistenceUnits.put(unit.getPersistenceUnitName(), unit);
        }

        return new MinijaxPersistenceFile(persistenceUnits);
    }

    private static MinijaxPersistenceUnitInfo parseUnit(final Node node) {
        final Element element = (Element) node;
        final String name = element.getAttribute("name");
        final String providerClassName = element.getElementsByTagName("provider").item(0).getTextContent();
        final MinijaxPersistenceUnitInfo unit = new MinijaxPersistenceUnitInfo(name, providerClassName);

        final NodeList classNodes = element.getElementsByTagName("class");
        for (int i = 0; i < classNodes.getLength(); i++) {
            unit.getManagedClassNames().add(classNodes.item(i).getTextContent());
        }

        final NodeList propertiesNodes = element.getElementsByTagName("properties");
        for (int i = 0; i < propertiesNodes.getLength(); i++) {
            final NodeList propertyNodes = ((Element) propertiesNodes.item(i)).getElementsByTagName("property");
            for (int j = 0; j < propertyNodes.getLength(); j++) {
                final Element property = (Element) propertyNodes.item(j);
                unit.getProperties().put(property.getAttribute("name"), property.getAttribute("value"));
            }
        }

        return unit;
    }
}
