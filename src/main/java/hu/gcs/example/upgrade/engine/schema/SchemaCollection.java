package hu.gcs.example.upgrade.engine.schema;

import hu.gcs.example.upgrade.engine.schema.reader.SqlFileReader;
import hu.gcs.example.upgrade.engine.schema.reader.PropertyArrayReader;
import hu.gcs.example.upgrade.engine.schema.reader.ReaderFactory;
import hu.gcs.example.upgrade.engine.schema.reader.VersionReader;
import hu.gcs.example.upgrade.engine.schema.reader.exception.FileReadException;
import hu.gcs.example.upgrade.engine.schema.reader.exception.VersionNotFoundException;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;

import org.jboss.logging.Logger;

@Singleton
public class SchemaCollection {

    private static final String UPGRADE_PACKAGES_PROPERTY = "upgrade_packages";
    private static final String SCHEMA_UPGRADES_FOLDER = "schema_upgrades/";
    private static final String UPGRADE_PROPERTIES_FILE = SCHEMA_UPGRADES_FOLDER + "upgrade.properties";

    private final Set<Schema> schemas = new LinkedHashSet<>();

    private ReaderFactory readerFactory;
    private SchemaFactory schemaFactory;
    private Logger logger;

    public SchemaCollection() {
    }

    @Inject
    public SchemaCollection(final ReaderFactory readerFactory, final SchemaFactory schemaFactory, final Logger logger) {
        this.readerFactory = readerFactory;
        this.schemaFactory = schemaFactory;
        this.logger = logger;
    }

    public Schema getInitSchema() {
        return schemaFactory.createInitSchema();
    }

    @PostConstruct
    public void loadCollection() {
        final PropertyArrayReader propertyReader = readerFactory.createPropertyArrayReader(UPGRADE_PROPERTIES_FILE,
                UPGRADE_PACKAGES_PROPERTY);

        final SqlFileReader sqlFileReader = readerFactory.createFileReader(SCHEMA_UPGRADES_FOLDER);
        final VersionReader versionReader = readerFactory.createVersionReader();

        final Set<String> filenames = propertyReader.read();

        addSchemas(sqlFileReader, versionReader, filenames);
    }

    private void addSchemas(final SqlFileReader sqlFileReader, final VersionReader versionReader,
            final Set<String> filenames) {

        // final Map<String, Set<String>> sqls = new HashMap<>();
        // for (final String filename : filenames) {
        // final Set<String> statements = sqlFileReader.read(filename);
        // sqls.put(filename, statements);
        // }

        for (final String filename : filenames) {
            try {
                schemas.add(schemaFactory.createUpgradeSchema(versionReader.read(filename),
                        sqlFileReader.read(filename)));
            } catch (final FileReadException | VersionNotFoundException e) {
                logger.warn("Exception occurred while file reading...: " + e);
            }
        }
    }

    public Set<Schema> getSchemas() {
        return Collections.unmodifiableSet(schemas);
    }
}
