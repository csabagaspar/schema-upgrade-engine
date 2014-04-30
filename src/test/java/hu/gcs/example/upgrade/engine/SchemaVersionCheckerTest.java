package hu.gcs.example.upgrade.engine;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

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
public class SchemaVersionCheckerTest {
    @Mock
    private EntityManager entityManager;
    @Mock
    private Query query;
    @Mock
    private Logger logger;

    @InjectMocks
    private SchemaVersionChecker checker;

    @Test
    public void testQueryDatabaseStateReturnWithVersionInfo() throws Exception {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1);

        checker.fetchDatabaseVersion();

        assertThat(checker.getVersion(), equalTo(1));
    }

    @Test
    public void testDatabaseNotInitializedHandleException() throws Exception {
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        doThrow(PersistenceException.class).when(query).getSingleResult();

        checker.fetchDatabaseVersion();

        assertThat(checker.getVersion(), equalTo(-1));
    }
}
