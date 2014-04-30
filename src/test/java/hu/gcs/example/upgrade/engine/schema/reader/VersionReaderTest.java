package hu.gcs.example.upgrade.engine.schema.reader;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import hu.gcs.example.upgrade.engine.schema.reader.VersionReader;
import hu.gcs.example.upgrade.engine.schema.reader.exception.VersionNotFoundException;

import org.junit.Test;

public class VersionReaderTest {

    private final VersionReader reader = new VersionReader();

    @Test
    public void testParseVersionInfoFromFile() throws Exception {
        assertThat(reader.read("upgrade1"), equalTo(1));
    }

    @Test(expected = VersionNotFoundException.class)
    public void testNoVersionInfoInFilename() throws Exception {
        reader.read("no_version_info");
    }

}
