package hu.gcs.example.upgrade.engine.schema.reader;

import hu.gcs.example.upgrade.engine.schema.reader.exception.FileReadException;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

public class PropertyArrayReader {

    private static final String REGEX = ",";

    private final String propertyName;
    private final String file;

    public PropertyArrayReader(final String file, final String propertyName) {
        this.file = file;
        this.propertyName = propertyName;
    }

    public Set<String> read() {
        final Properties properties = new Properties();

        try (final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file)) {
            properties.load(inputStream);
        } catch (final IOException e) {
            throw new FileReadException(e);
        }
        return parseProperties(properties);
    }

    private Set<String> parseProperties(final Properties properties) {
        final Set<String> filenames = new LinkedHashSet<>();
        final String[] packagesNames = properties.getProperty(propertyName).split(REGEX);
        for (int i = 0; i < packagesNames.length; i++) {
            if (!"".equals(packagesNames[i]))
                filenames.add(packagesNames[i].trim());
        }
        return filenames;
    }
}
