package hu.gcs.example.upgrade.engine;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.jboss.logging.Logger;

@Singleton
public class SchemaVersionChecker {

    private static final int NO_VERSION = -1;
    private int version = NO_VERSION;

    @Inject
    private EntityManager entityManager;
    @Inject
    private Logger logger;

    public int getVersion() {
        return version;
    }

    @PostConstruct
    public void fetchDatabaseVersion() {
        final Query q = entityManager.createNativeQuery("SELECT MAX(version) FROM schema_version");
        try {
            version = (Integer) q.getSingleResult();
        } catch (final PersistenceException e) {
            logger.warn("Table SCHEMA_VERSION not found!");
        }

    }
}
