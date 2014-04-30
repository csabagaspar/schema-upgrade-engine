package hu.gcs.example.upgrade.engine;

import hu.gcs.example.upgrade.engine.schema.Schema;
import hu.gcs.example.upgrade.engine.schema.SchemaCollection;
import hu.gcs.example.upgrade.engine.schema.SchemaComparator;

import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.jboss.logging.Logger;

@Singleton
@Startup
public class Upgrader {
    private static final int NO_VERSION = 0;

    private final Set<Schema> schemasToUpgrade = new TreeSet<>(new SchemaComparator());

    private SchemaUpgrader schemaUpgrader;
    private SchemaVersionChecker databaseSchemaChecker;
    private SchemaCollection schemaCollector;
    private Logger logger;

    public Upgrader() {
    }

    @Inject
    public Upgrader(final SchemaUpgrader schemaUpgrader, final SchemaCollection schemaCollector,
            final SchemaVersionChecker databaseSchemaChecker, final Logger logger) {
        this.schemaUpgrader = schemaUpgrader;
        this.schemaCollector = schemaCollector;
        this.databaseSchemaChecker = databaseSchemaChecker;
        this.logger = logger;
    }

    @PostConstruct
    public void execute() {
        addInitSchemaIfSchemaVersionTableNotFound();
        addUpgradeSchemas();
        upgrade();
    }

    private void addInitSchemaIfSchemaVersionTableNotFound() {
        if (databaseSchemaChecker.getVersion() < NO_VERSION) {
            schemasToUpgrade.add(schemaCollector.getInitSchema());
        }
    }

    private void addUpgradeSchemas() {
        for (final Schema schema : schemaCollector.getSchemas()) {
            if (schema.getVersion() > databaseSchemaChecker.getVersion()) {
                schemasToUpgrade.add(schema);
            }
        }
    }

    private void upgrade() {
        try {
            for (final Schema schema : schemasToUpgrade) {
                schemaUpgrader.apply(schema);
            }
        } catch (final UpgradeException e) {
            logger.error("Database upgrade failed! " + e);
        }
    }
}
