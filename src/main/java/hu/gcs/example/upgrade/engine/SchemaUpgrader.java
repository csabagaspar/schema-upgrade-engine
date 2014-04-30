package hu.gcs.example.upgrade.engine;

import hu.gcs.example.upgrade.engine.schema.Schema;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.jboss.logging.Logger;

public class SchemaUpgrader {

    @Inject
    private EntityManager entityManager;
    @Inject
    private Logger logger;

    public void apply(final Schema schema) {

        logger.info("Started to apply schema version: " + schema.getVersion() + " ...");

        for (final String statement : schema.getStatements()) {
            final Query q = entityManager.createNativeQuery(statement);
            try {
                logger.info("Started to execute statement: " + statement);
                q.executeUpdate();
            } catch (final PersistenceException e) {
                logger.warn("Transaction was rolled back! " + e);
                throw new UpgradeException(e);
            }
        }
    }
}
