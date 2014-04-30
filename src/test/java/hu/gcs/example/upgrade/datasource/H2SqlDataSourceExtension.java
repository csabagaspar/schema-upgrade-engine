package hu.gcs.example.upgrade.datasource;

import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.core.spi.LoadableExtension;

public class H2SqlDataSourceExtension implements LoadableExtension {

    @Override
    public void register(final ExtensionBuilder builder) {
        builder.service(AuxiliaryArchiveAppender.class, H2SqlDataSourceArchiveCreator.class);
        builder.service(AuxiliaryArchiveAppender.class, H2SqlDriverArchiveAppender.class);
    }

}
