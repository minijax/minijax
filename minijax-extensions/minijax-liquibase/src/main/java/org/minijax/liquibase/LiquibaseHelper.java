package org.minijax.liquibase;

import static java.util.Collections.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.minijax.MinijaxProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.change.Change;
import liquibase.change.core.DropTableChange;
import liquibase.changelog.ChangeSet;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.diff.DiffGeneratorFactory;
import liquibase.diff.DiffResult;
import liquibase.diff.compare.CompareControl;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import liquibase.serializer.core.xml.XMLChangeLogSerializer;
import liquibase.snapshot.SnapshotControl;

/**
 * The LiquibaseHelper class provides helpers to migrate a database and generate new migrations.
 *
 * It is a thin layer that wraps JPA and Liquibase.
 *
 * By default, migrations are added to src/main/resources/changelog.xml.
 *
 * The recommended workflow is to (1) generate a migration, (2) manually inspect the migration,
 * and (3) run the migration if everything looks ok.
 */
public class LiquibaseHelper {
    private static final Logger LOG = LoggerFactory.getLogger(LiquibaseHelper.class);
    private static final File DEFAULT_RESOURCES_DIR = new File("src/main/resources");
    private static final String MIGRATIONS_DIR = "migrations";
    private static final String MASTER_CHANGELOG_RESOURCE_NAME = "master.changelog.xml";
    private static final String XML_FACTORY = "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl";
    private final String persistenceUnitName;
    private final String driver;
    private final String url;
    private final String username;
    private final String password;
    private final String referenceUrl;
    private final ResourceAccessor resourceAccessor;
    private final File resourcesDir;
    private final File migrationsDir;
    private final File masterChangeLogFile;


    /**
     * Creates a new helper.
     *
     * @param props
     */
    public LiquibaseHelper(final Map<String, String> props) {
        this(
                props,
                new ClassLoaderResourceAccessor(),
                DEFAULT_RESOURCES_DIR,
                MASTER_CHANGELOG_RESOURCE_NAME);
    }


    LiquibaseHelper(
            final Map<String, String> props,
            final ResourceAccessor resourceAccessor,
            final File resourcesDir,
            final String masterChangeLogName) {

        this.resourceAccessor = resourceAccessor;
        this.resourcesDir = resourcesDir;

        persistenceUnitName = props.get(MinijaxProperties.PERSISTENCE_UNIT_NAME);
        driver = props.get(MinijaxProperties.DB_DRIVER);
        url = props.get(MinijaxProperties.DB_URL);
        username = props.get(MinijaxProperties.DB_USERNAME);
        password = props.get(MinijaxProperties.DB_PASSWORD);
        referenceUrl = props.get(MinijaxProperties.DB_REFERENCE_URL);
        migrationsDir = new File(resourcesDir, MIGRATIONS_DIR);
        masterChangeLogFile = new File(migrationsDir, masterChangeLogName);
    }


    public File getResourcesDir() {
        return resourcesDir;
    }


    public File getMasterChangeLogFile() {
        return masterChangeLogFile;
    }


    /**
     * Helper utility to perform a Liquibase database migration.
     *
     * @see liquibase.Liquibase#update(String)
     */
    public void migrate() throws LiquibaseException, SQLException {
        Database database = null;

        try {
            database = getTargetDatabase();
            getLiquibase(database).update(""); // Empty string = all contexts

        } finally {
            closeQuietly(database);
        }
    }


    public File generateMigrations() throws IOException, LiquibaseException, SQLException {
        Database referenceDatabase = null;
        Database targetDatabase = null;

        try {
            referenceDatabase = getReferenceDatabase();
            targetDatabase = getTargetDatabase();
            return generateMigrations(referenceDatabase, targetDatabase);

        } finally {
            closeQuietly(referenceDatabase);
            closeQuietly(targetDatabase);
        }
    }


    /*
     * Private helpers
     */


    private Liquibase getLiquibase(final Database targetDatabase) {
        return new Liquibase(getRelativePath(masterChangeLogFile), resourceAccessor, targetDatabase);
    }


    private String getRelativePath(final File resourceFile) {
        return resourcesDir.toPath().relativize(resourceFile.toPath()).toString();
    }


    /**
     * Returns a database connection.
     *
     * Override this method in tests to avoid actually connecting to databases.
     *
     * @param url
     * @param username
     * @param password
     * @return
     */
    private Connection getConnection(final String url, final String username, final String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }


    private Database getLiquibaseDatabase(final Connection conn) throws DatabaseException {
        return DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
    }

    private Database getTargetDatabase() throws LiquibaseException, SQLException {
        try {
            Class.forName(driver);
        } catch (final ClassNotFoundException ex) {
            throw new LiquibaseException(ex.getMessage(), ex);
        }
        return getLiquibaseDatabase(getConnection(url, username, password));
    }


    /**
     * Returns the "reference" database.
     *
     * In Liquibase terminology, this is the destination / goal state.
     *
     * We create an empty temporary database, and use JPA to autogenerate the schema.
     *
     * @return Database connection to the temporary reference database.
     */
    private Database getReferenceDatabase() throws DatabaseException, SQLException {
        buildReferenceDatabase();
        return getLiquibaseDatabase(getConnection(referenceUrl, username, password));
    }


    private void buildReferenceDatabase() {
        final Map<String, String> props = new HashMap<>();
        props.put(MinijaxProperties.DB_DRIVER, driver);
        props.put(MinijaxProperties.DB_URL, referenceUrl);
        props.put(MinijaxProperties.DB_USERNAME, username);
        props.put(MinijaxProperties.DB_PASSWORD, password);
        props.put("javax.persistence.schema-generation.database.action", "drop-and-create");

        EntityManagerFactory emf = null;
        try {
            emf = Persistence.createEntityManagerFactory(persistenceUnitName, props);
        } finally {
            closeQuietly(emf);
        }
    }


    private File generateMigrations(final Database referenceDatabase, final Database targetDatabase)
            throws LiquibaseException, IOException {

        if (!resourcesDir.exists()) {
            resourcesDir.mkdirs();
        }

        if (!migrationsDir.exists()) {
            migrationsDir.mkdirs();
        }

        if (masterChangeLogFile.exists()) {
            LOG.info("Checking current database state");
            validateDatabaseState(targetDatabase);

        } else {
            LOG.info("Creating new master changelog");
            writeChangeSets(masterChangeLogFile, emptyList());
        }

        @SuppressWarnings("unchecked")
        final SnapshotControl snapshotControl = new SnapshotControl(
                referenceDatabase,
                liquibase.structure.core.Schema.class,
                liquibase.structure.core.Table.class,
                liquibase.structure.core.Column.class,
                liquibase.structure.core.PrimaryKey.class,
                liquibase.structure.core.Index.class);

        LOG.info("Executing diff");
        final CompareControl compareControl = new CompareControl(snapshotControl.getTypesToInclude());
        final DiffResult diffResult = DiffGeneratorFactory.getInstance().compare(
                referenceDatabase,
                targetDatabase,
                compareControl);

        LOG.info("Converting diff to changelog");
        final DiffOutputControl diffOutputControl = new DiffOutputControl(false, false, true, null);
        final DiffToChangeLog diffToChangeLog = new DiffToChangeLog(diffResult, diffOutputControl);
        diffToChangeLog.setChangeSetAuthor(System.getProperty("user.name"));

        final List<ChangeSet> changeSets = filterChangeSets(diffToChangeLog.generateChangeSets());
        LOG.info("Found {} changes", changeSets.size());
        if (changeSets.isEmpty()) {
            return null;
        }

        final File generatedChangeLogFile = new File(migrationsDir, generateFileName(masterChangeLogFile));
        LOG.info("Writing new changelog: {}", generatedChangeLogFile);
        writeChangeSets(generatedChangeLogFile, changeSets);

        LOG.info("Add migration to master changelog: {}", masterChangeLogFile);
        addIncludeFile(generatedChangeLogFile);

        LOG.info("Cleaning changelog");
        cleanXmlFile(masterChangeLogFile);
        cleanXmlFile(generatedChangeLogFile);

        LOG.info("Diff complete");
        return generatedChangeLogFile;
    }


    /**
     * Validates that the database is in a good state.
     *
     * @param database The database.
     * @param fileName The change log file name.
     * @param resourceAccessor The change log file loader.
     */
    private void validateDatabaseState(final Database database) throws LiquibaseException {
        final Liquibase liquibase = getLiquibase(database);
        final Contexts contexts = new Contexts(); // all contexts
        final LabelExpression labels = new LabelExpression(); // no filters
        final List<ChangeSet> unrunChangeSets = liquibase.listUnrunChangeSets(contexts, labels);
        if (!unrunChangeSets.isEmpty()) {
            throw new IllegalStateException("Unrun change sets!  Please migrate the database first");
        }
    }


    /**
     * Filters out all ignored change sets.
     *
     * @param changeSets The original list of change sets.
     * @return The filtered list of change sets.
     */
    static List<ChangeSet> filterChangeSets(final List<ChangeSet> changeSets) {
        final List<ChangeSet> result = new ArrayList<>();
        for (final ChangeSet changeSet : changeSets) {
            if (!isIgnoredChangeSet(changeSet)) {
                result.add(changeSet);
            }
        }
        return result;
    }


    /**
     * Returns true if the change set should be ignored.
     *
     * Most tables are managed by JPA.  When we create the temporary "goal" database,
     * JPA creates all of the tables for a normal happy path diff.
     *
     * If using JGroups, there is an extra table created called "JGROUPSPING" which
     * tracks all of the members of the JGroups cluster.  Because this table is not
     * managed by JPA, it is absent from the "goal" database.  Therefore liquibase
     * always tries to drop the table.  We need to ignore that.
     *
     * @param changeSet The candidate change set.
     * @return True if the change set should be ignored.
     */
    static boolean isIgnoredChangeSet(final ChangeSet changeSet) {
        final List<Change> changes = changeSet.getChanges();
        if (changes.size() != 1) {
            return false;
        }

        final Change change = changes.get(0);
        if (!(change instanceof DropTableChange)) {
            return false;
        }

        return ((DropTableChange) change).getTableName().equals("JGROUPSPING");
    }


    static void closeQuietly(final EntityManagerFactory emf) {
        if (emf != null) {
            try {
                emf.close();
            } catch (final Exception ex) {
                LOG.warn("Error closing entity manager factory: {}", ex.getMessage(), ex);
            }
        }
    }


    static void closeQuietly(final Database database) {
        if (database != null) {
            try {
                database.close();
            } catch (final Exception ex) {
                LOG.warn("Error closing database: {}", ex.getMessage(), ex);
            }
        }
    }


    private void writeChangeSets(final File file, final List<ChangeSet> changeSets) throws IOException {
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            final XMLChangeLogSerializer changeLogSerializer = new XMLChangeLogSerializer();
            changeLogSerializer.write(changeSets, outputStream);
            outputStream.flush();
        }
    }


    private void addIncludeFile(final File includeFile) throws IOException {
        final Document doc = readXml(masterChangeLogFile);
        final Element include = doc.createElement("include");
        include.setAttribute("file", getRelativePath(includeFile));
        doc.getDocumentElement().appendChild(include);
        writeXml(doc, masterChangeLogFile);
    }


    /**
     * Cleans the XML file.
     *  1) Updates formatting and indentation.
     *  2) Removes liquibase "objectQuotingStrategy" attributes.
     *
     * @param file The changelog file.
     */
    private static void cleanXmlFile(final File file) throws IOException {
        try {
            final Document doc = readXml(file);
            final XPath xpath = XPathFactory.newInstance().newXPath();
            removeNodes(doc, xpath, "//@objectQuotingStrategy");
            removeNodes(doc, xpath, "//text()[normalize-space()='']");
            writeXml(doc, file);
        } catch (final XPathException ex) {
            throw new IOException(ex.getMessage(), ex);
        }
    }


    private static Document readXml(final File file) throws IOException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        try {
            return factory.newDocumentBuilder().parse(file);
        } catch (ParserConfigurationException | SAXException ex) {
            throw new IOException(ex.getMessage(), ex);
        }
    }


    private static void writeXml(final Document doc, final File file) throws IOException {
        doc.normalize();

        final TransformerFactory transformerFactory = TransformerFactory.newInstance(XML_FACTORY, null);

        try {
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

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


    /**
     * Removes all nodes that match the XPath expression.
     *
     * @param doc The XML document.
     * @param xpath The XPath helper.
     * @param expression The XPath expression.
     */
    private static void removeNodes(final Document doc, final XPath xpath, final String expression)
            throws XPathExpressionException {

        final NodeList nodeList = (NodeList) xpath.evaluate(expression, doc, XPathConstants.NODESET);
        for (int i = 0; i < nodeList.getLength(); ++i) {
            final Node node = nodeList.item(i);
            if (node instanceof Attr) {
                final Attr attr = (Attr) node;
                attr.getOwnerElement().removeAttribute(attr.getNodeName());
            } else {
                node.getParentNode().removeChild(node);
            }
        }
    }


    private static String generateFileName(final File masterChangeLogFile) throws IOException {
        final int id = readXml(masterChangeLogFile).getDocumentElement().getElementsByTagName("include").getLength() + 1;
        return String.format("changelog.%04d.xml", id);
    }
}
