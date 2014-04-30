package hu.gcs.example.upgrade.datasource;

import java.io.File;

import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;

public class H2SqlDriverArchiveAppender implements AuxiliaryArchiveAppender {

    @Override
    public Archive<?> createAuxiliaryArchive() {
        final PomEquippedResolveStage resolver = Maven.resolver().offline().loadPomFromFile("pom.xml");
        final File[] jars = resolver.resolve("com.h2database:h2:1.3.173").withoutTransitivity().asFile();
        return ShrinkWrap.createFromZipFile(JavaArchive.class, jars[0]);
    }

}
