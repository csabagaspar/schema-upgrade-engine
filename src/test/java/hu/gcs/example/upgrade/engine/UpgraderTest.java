package hu.gcs.example.upgrade.engine;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import hu.gcs.example.upgrade.engine.schema.Schema;
import hu.gcs.example.upgrade.engine.schema.SchemaCollection;
import hu.gcs.example.upgrade.engine.schema.UpgradeSchema;

import java.util.LinkedHashSet;
import java.util.Set;

import org.jboss.logging.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UpgraderTest {
    @Mock
    private SchemaVersionChecker databaseSchemaChecker;
    @Mock
    private SchemaCollection schemaCollection;
    @Mock
    private SchemaUpgrader schemaUpgrader;
    @Mock
    private Logger logger;

    @InjectMocks
    private Upgrader upgrader;

    @Test
    public void testInitializeDatabase() throws Exception {
        when(databaseSchemaChecker.getVersion()).thenReturn(-1);
        when(schemaCollection.getInitSchema()).thenReturn(new UpgradeSchema(0, new LinkedHashSet<String>()));

        upgrader.execute();

        verify(schemaUpgrader).apply(any(UpgradeSchema.class));
    }

    @Test
    public void testInitializeAndOneUpgradePackage() throws Exception {
        when(databaseSchemaChecker.getVersion()).thenReturn(-1);
        when(schemaCollection.getInitSchema()).thenReturn(new UpgradeSchema(0, new LinkedHashSet<String>()));
        when(schemaCollection.getSchemas()).thenReturn(createDummySchemaSet(1, 1));

        upgrader.execute();

        verify(schemaUpgrader, times(2)).apply(any(UpgradeSchema.class));
    }

    @Test
    public void testInitializeAndMoreUpgradePackages() throws Exception {
        when(databaseSchemaChecker.getVersion()).thenReturn(-1);
        when(schemaCollection.getInitSchema()).thenReturn(new UpgradeSchema(0, new LinkedHashSet<String>()));
        when(schemaCollection.getSchemas()).thenReturn(createDummySchemaSet(1, 2));

        upgrader.execute();

        verify(schemaUpgrader, times(3)).apply(any(UpgradeSchema.class));
    }

    @Test
    public void testOneUpgradePackageAfterInitialization() throws Exception {
        when(databaseSchemaChecker.getVersion()).thenReturn(1);
        when(schemaCollection.getSchemas()).thenReturn(createDummySchemaSet(2, 2));

        upgrader.execute();

        verify(schemaUpgrader).apply(any(UpgradeSchema.class));
    }

    @Test
    public void testMoreUpgradePackageAfterInitialization() throws Exception {
        when(databaseSchemaChecker.getVersion()).thenReturn(4);
        when(schemaCollection.getSchemas()).thenReturn(createDummySchemaSet(1, 6));

        upgrader.execute();

        verify(schemaUpgrader, times(2)).apply(any(UpgradeSchema.class));
    }

    @Test
    public void testLogUpgradeFailed() throws Exception {
        when(databaseSchemaChecker.getVersion()).thenReturn(-1);
        when(schemaCollection.getInitSchema()).thenReturn(new UpgradeSchema(0, new LinkedHashSet<String>()));
        doThrow(UpgradeException.class).when(schemaUpgrader).apply(any(Schema.class));
        upgrader.execute();
        verify(logger).error(anyString());
    }

    @Test
    public void testIoCNeedsADefaultConstructor() throws Exception {
        assertNotNull(new Upgrader());
    }

    private Set<Schema> createDummySchemaSet(final int start, final int end) {
        final Set<Schema> schemas = new LinkedHashSet<>();
        for (int i = start; i <= end; i++) {
            final Schema s = new UpgradeSchema(i, new LinkedHashSet<String>());
            schemas.add(s);
        }
        return schemas;
    }
}
