package hu.gcs.example.upgrade.integration;

import hu.gcs.example.upgrade.engine.SchemaVersionChecker;
import hu.gcs.example.upgrade.engine.SchemaUpgrader;
import hu.gcs.example.upgrade.engine.UpgradeException;
import hu.gcs.example.upgrade.engine.Upgrader;
import hu.gcs.example.upgrade.engine.resource.EntityManegerResource;
import hu.gcs.example.upgrade.engine.resource.LoggerResource;
import hu.gcs.example.upgrade.engine.schema.InitSchema;
import hu.gcs.example.upgrade.engine.schema.Schema;
import hu.gcs.example.upgrade.engine.schema.SchemaCollection;
import hu.gcs.example.upgrade.engine.schema.SchemaComparator;
import hu.gcs.example.upgrade.engine.schema.SchemaFactory;
import hu.gcs.example.upgrade.engine.schema.UpgradeSchema;
import hu.gcs.example.upgrade.engine.schema.reader.SqlFileReader;
import hu.gcs.example.upgrade.engine.schema.reader.PropertyArrayReader;
import hu.gcs.example.upgrade.engine.schema.reader.ReaderFactory;
import hu.gcs.example.upgrade.engine.schema.reader.VersionReader;
import hu.gcs.example.upgrade.engine.schema.reader.exception.FileReadException;
import hu.gcs.example.upgrade.engine.schema.reader.exception.VersionNotFoundException;
import hu.gcs.example.upgrade.service.Book;
import hu.gcs.example.upgrade.service.BookRepository;
import hu.gcs.example.upgrade.service.BookService;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

//TODO rollback test?
@RunWith(Arquillian.class)
public class BookIntegrationTest {
    @Inject
    private BookService service;

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap
                .create(JavaArchive.class)
                .addClasses(BookService.class, BookRepository.class, Book.class, SchemaVersionChecker.class,
                        SchemaUpgrader.class, Upgrader.class, InitSchema.class, Schema.class, SchemaCollection.class,
                        SchemaFactory.class, UpgradeSchema.class, FileReadException.class, PropertyArrayReader.class,
                        VersionReader.class, SqlFileReader.class, ReaderFactory.class, VersionNotFoundException.class,
                        UpgradeException.class, LoggerResource.class, SchemaComparator.class,
                        EntityManegerResource.class, PropertyArrayReader.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("test-persistence.xml", "persistence.xml")
                .addAsResource("schema_upgrades/upgrade.properties").addAsResource("schema_upgrades/upgrade_1.sql")
                .addAsResource("schema_upgrades/upgrade_2.sql");
    }

    @Test
    public void test() throws Exception {
        service.saveANewBook();
    }

}
