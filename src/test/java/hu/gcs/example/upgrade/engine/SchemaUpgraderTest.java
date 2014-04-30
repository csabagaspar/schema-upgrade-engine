package hu.gcs.example.upgrade.engine;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import hu.gcs.example.upgrade.engine.schema.Schema;
import hu.gcs.example.upgrade.engine.schema.UpgradeSchema;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.jboss.logging.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchemaUpgraderTest {
    @Mock
    EntityManager entityManager;
    @Mock
    Logger logger;
    @InjectMocks
    SchemaUpgrader schemaUpgrader;

    @Test
    public void testApplySchema() throws Exception {
        final Schema schema = createASchemaWithStatements(3);

        final Query query = mock(Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        schemaUpgrader.apply(schema);
        verify(entityManager, times(4)).createNativeQuery(anyString());
    }

    private Schema createASchemaWithStatements(final int numberOfStatements) {
        final Set<String> statements = new LinkedHashSet<>();
        for (int i = 0; i < numberOfStatements; i++) {
            statements.add("select" + i);
        }
        return new UpgradeSchema(1, statements);
    }

    @Test(expected = UpgradeException.class)
    public void testThrowAnUpgradeExceptionAndRollbackStatements() throws Exception {
        final Schema schema = createASchemaWithStatements(1);
        final Query query = mock(Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        doThrow(PersistenceException.class).when(query).executeUpdate();
        schemaUpgrader.apply(schema);
        verify(logger).warn(anyString());

    }
}
