package hu.gcs.example.upgrade.engine.schema;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import hu.gcs.example.upgrade.engine.schema.reader.PropertyArrayReader;
import hu.gcs.example.upgrade.engine.schema.reader.ReaderFactory;
import hu.gcs.example.upgrade.engine.schema.reader.SqlFileReader;
import hu.gcs.example.upgrade.engine.schema.reader.VersionReader;
import hu.gcs.example.upgrade.engine.schema.reader.exception.FileReadException;
import hu.gcs.example.upgrade.engine.schema.reader.exception.VersionNotFoundException;

import java.util.LinkedHashSet;
import java.util.Set;

import org.jboss.logging.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchemaCollectionTest {

    private static final String UPGRADE_1_FILE = "upgrade_1";
    private static final String UPGRADE_2_FILE = "upgrade_2";

    @Mock
    private ReaderFactory readerFactory;
    @Mock
    private SchemaFactory schemaFactory;
    @Mock
    private Logger logger;

    @InjectMocks
    private SchemaCollection collection;

    @Test
    public void testLoadSchemasWhileDeployingTheSoftware() throws Exception {
        final PropertyArrayReader propReader = mock(PropertyArrayReader.class);

        final SqlFileReader sqlReader1 = mock(SqlFileReader.class);
        final VersionReader versionReader1 = mock(VersionReader.class);
        final Set<String> statements1 = new LinkedHashSet<String>();

        final SqlFileReader sqlReader2 = mock(SqlFileReader.class);
        final VersionReader versionReader2 = mock(VersionReader.class);
        final Set<String> statements2 = new LinkedHashSet<String>();

        when(readerFactory.createPropertyArrayReader(anyString(), anyString())).thenReturn(propReader);
        final Set<String> filenames = new LinkedHashSet<>();
        filenames.add("upgrade_1");
        filenames.add("upgrade_2");

        when(propReader.read()).thenReturn(filenames);

        when(readerFactory.createFileReader(anyString())).thenReturn(sqlReader1);
        when(readerFactory.createVersionReader()).thenReturn(versionReader1);
        when(sqlReader1.read(UPGRADE_1_FILE)).thenReturn(statements1);
        when(versionReader1.read(UPGRADE_1_FILE)).thenReturn(1);
        when(schemaFactory.createUpgradeSchema(1, statements1)).thenReturn(new UpgradeSchema(1, statements1));

        when(readerFactory.createFileReader(anyString())).thenReturn(sqlReader2);
        when(readerFactory.createVersionReader()).thenReturn(versionReader2);
        when(sqlReader2.read(UPGRADE_2_FILE)).thenReturn(statements2);
        when(versionReader2.read(UPGRADE_2_FILE)).thenReturn(2);
        when(schemaFactory.createUpgradeSchema(2, statements2)).thenReturn(new UpgradeSchema(2, statements2));

        collection.loadCollection();

        assertThat(collection.getSchemas().size(), equalTo(2));

    }

    @Test
    public void testDoNotAddSchemaForCollectionIfHasNoVersion() throws Exception {
        final PropertyArrayReader propReader = mock(PropertyArrayReader.class);
        final VersionReader versionReader = mock(VersionReader.class);

        final Set<String> filesNames = new LinkedHashSet<>();
        filesNames.add("upgrade");

        when(readerFactory.createPropertyArrayReader(anyString(), anyString())).thenReturn(propReader);
        when(propReader.read()).thenReturn(filesNames);

        when(readerFactory.createVersionReader()).thenReturn(versionReader);
        doThrow(VersionNotFoundException.class).when(versionReader).read(anyString());

        collection.loadCollection();

        assertTrue(collection.getSchemas().isEmpty());
    }

    @Test
    public void testDoNotAddSchemaForCollectioWhenAnExceptionOccur() throws Exception {
        final PropertyArrayReader propReader = mock(PropertyArrayReader.class);
        final SqlFileReader sqlReader1 = mock(SqlFileReader.class);
        final VersionReader versionReader1 = mock(VersionReader.class);

        when(readerFactory.createPropertyArrayReader(anyString(), anyString())).thenReturn(propReader);
        final Set<String> filenames = new LinkedHashSet<>();
        filenames.add("fileNonExist");

        when(propReader.read()).thenReturn(filenames);
        when(readerFactory.createFileReader(anyString())).thenReturn(sqlReader1);
        when(readerFactory.createVersionReader()).thenReturn(versionReader1);
        doThrow(FileReadException.class).when(sqlReader1).read(anyString());

        collection.loadCollection();

        assertTrue(collection.getSchemas().isEmpty());
    }
}
