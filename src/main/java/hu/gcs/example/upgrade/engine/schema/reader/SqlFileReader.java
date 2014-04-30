package hu.gcs.example.upgrade.engine.schema.reader;

import hu.gcs.example.upgrade.engine.schema.reader.exception.FileReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Set;

public class SqlFileReader {

    private static final String EMPTY_LINE = "";
    private static final String FILE_EXTENSION = ".sql";
    private final String path;

    public SqlFileReader(final String path) {
        this.path = path;
    }

    public Set<String> read(final String filename) {
        final Set<String> lines = new LinkedHashSet<String>();
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader()
                .getResourceAsStream(path + filename + FILE_EXTENSION)))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!EMPTY_LINE.equals(line))
                    lines.add(line);
            }
        } catch (final IOException | NullPointerException e) {
            throw new FileReadException(e);
        }
        return lines;
    }
}
